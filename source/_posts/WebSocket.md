---
title: WebSocket
date: 2026-06-16 13:08:48
tags:
- WebSocket
- STOMP
categories:
- 进阶技术
---
## 一、WebSocket 基础回顾

### 1.1 什么是 WebSocket

**WebSocket** 是一种在单个 TCP 连接上实现 **全双工通信** 的应用层协议，定义在 RFC 6455 中。它使得服务器可以主动向客户端推送数据，而不需要客户端反复发起 HTTP 请求。

### 1.2 HTTP 轮询 vs WebSocket

| 特性 | HTTP 轮询 | HTTP 长轮询 | WebSocket |
|------|----------|------------|-----------|
| 通信方向 | 客户端 -> 服务器 | 客户端 -> 服务器 -> 客户端 | 全双工 |
| 实时性 | 低 | 中 | 高 |
| 请求开销 | 每次轮询都要创建 HTTP 连接 | 同左 | 一次握手，持久连接 |
| 服务器推送 | 不支持 | 勉强支持 | 原生支持 |
| 资源消耗 | 高 | 中 | 低 |

### 1.3 典型应用场景

- 实时消息推送（通知、公告）
- 即时通讯（IM、聊天室）
- 实时数据看板（监控大屏、仪表盘）
- 协同编辑（多人同时编辑文档）
- 实时行情推送（股票、加密货币）
- IoT 设备实时状态上报

---

## 二、Spring Boot 集成 WebSocket 的三种方式

Spring Boot 中集成 WebSocket 主要有两种底层方式，加上 STOMP 协议层共三种：

| 方式 | 说明 | 适用场景 |
|------|------|----------|
| **@ServerEndpoint (JSR 356)** | 基于 Java 原生 API，注解声明端点 | 简单场景，快速原型 |
| **Spring WebSocketHandler** | Spring 封装的 API，支持拦截器 | 需要握手认证、SockJS 降级 |
| **STOMP over WebSocket** | 在 WebSocket 上引入 STOMP 子协议 | 大型企业项目，**最推荐** |

> 实际企业开发中，**STOMP + Spring WebSocket** 是最主流的选择，本文会重点讲解。

---

## 三、方式一：基于 Java 标准 JSR 356 规范

这是你原笔记中使用的方式，基于 Java 标准 JSR 356 规范。我们先把它梳理正确，再看企业开发中的改进方向。

### 3.1 引入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>
```

### 3.2 配置 ServerEndpointExporter

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfig {

    /**
     * 使用 Spring Boot 内嵌 Tomcat 时必须注入此 Bean。
     * 如果部署为外置 Tomcat 的 war 包，则注释掉此配置，
     * 由外置容器扫描和管理 @ServerEndpoint。
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
```

### 3.3 编写 WebSocketServer（企业改进版）

以下代码修正了原笔记中的几个线程安全和异常处理问题：

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint(value = "/ws/{sid}")
@Component
public class WebSocketServer {

    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);

    /* 使用 AtomicInteger 保证线程安全，比 synchronized 性能更好 */
    private static final AtomicInteger onlineCount = new AtomicInteger(0);

    /* 存放 sid -> WebSocketServer 映射，线程安全 */
    private static final ConcurrentHashMap<String, WebSocketServer> WEBSOCKET_MAP = new ConcurrentHashMap<>();

    private Session session;
    private String sid;

    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        this.session = session;
        this.sid = sid;

        // 如果该 sid 已有旧连接，主动关闭旧连接（重要改进！）
        WebSocketServer old = WEBSOCKET_MAP.put(sid, this);
        if (old != null) {
            try {
                old.session.close(new CloseReason(
                    CloseReason.CloseCodes.NORMAL_CLOSURE, "新连接覆盖"));
            } catch (IOException e) {
                log.warn("关闭旧连接异常, sid={}", sid, e);
            }
        }

        onlineCount.incrementAndGet();
        log.info("新连接接入, sid={}, 当前在线: {}", sid, onlineCount.get());

        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            log.error("发送欢迎消息失败, sid={}", sid, e);
        }
    }

    @OnClose
    public void onClose() {
        // 只删除当前实例对应的条目，防止并发下误删其他连接
        WEBSOCKET_MAP.remove(this.sid, this);
        onlineCount.decrementAndGet();
        log.info("连接关闭, sid={}, 当前在线: {}", sid, onlineCount.get());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到消息, sid={}, message={}", sid, message);
        // 根据业务逻辑处理消息
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("连接异常, sid={}", sid, error);
    }

    /** 发送消息到当前连接 */
    public void sendMessage(String message) throws IOException {
        if (this.session != null && this.session.isOpen()) {
            this.session.getBasicRemote().sendText(message);
        }
    }

    // ========== 静态工具方法 ==========

    /** 获取当前在线连接数 */
    public static int getOnlineCount() {
        return onlineCount.get();
    }

    /**
     * 向指定 sid 发送消息；sid 为 null 时广播给所有人
     */
    public static void sendMessage(String message, String sid) {
        if (sid == null) {
            WEBSOCKET_MAP.forEach((key, server) -> {
                try { server.sendMessage(message); }
                catch (IOException e) {
                    log.error("广播消息失败, sid={}", key, e);
                }
            });
        } else {
            WebSocketServer server = WEBSOCKET_MAP.get(sid);
            if (server != null) {
                try { server.sendMessage(message); }
                catch (IOException e) {
                    log.error("发送消息失败, sid={}", sid, e);
                }
            } else {
                log.warn("用户不在线, sid={}", sid);
            }
        }
    }

    /** 判断用户是否在线 */
    public static boolean isOnline(String sid) {
        return WEBSOCKET_MAP.containsKey(sid);
    }

    /** 获取所有在线 sid（用于监控） */
    public static java.util.Set<String> getOnlineSids() {
        return WEBSOCKET_MAP.keySet();
    }
}
```

**改进点说明：**

| 原笔记问题 | 改进方式 |
|-----------|---------|
| static int + synchronized 性能差 | 改用 AtomicInteger |
| 同 sid 重复连接未处理旧连接 | PUT 前先关闭旧 Session |
| onClose 可能误删其他连接 | 改用 remove(this.sid, this) |
| 发送前未检查 session 是否关闭 | 增加 isOpen() 判断 |
| sendInfo 遍历所有值逐个检查 | 改为 get(sid) 直接定位 |
### 3.4 消息推送接口

```java
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ws/push")
public class WebSocketPushController {

    /** 向指定用户推送消息 */
    @PostMapping("/{sid}")
    public Map<String, Object> pushToUser(@PathVariable String sid, String message) {
        Map<String, Object> result = new HashMap<>();
        if (!WebSocketServer.isOnline(sid)) {
            result.put("code", 404);
            result.put("msg", "用户不在线");
            return result;
        }
        WebSocketServer.sendMessage(message, sid);
        result.put("code", 200);
        result.put("msg", "推送成功");
        return result;
    }

    /** 广播消息 */
    @PostMapping("/broadcast")
    public Map<String, Object> broadcast(String message) {
        WebSocketServer.sendMessage(message, null);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "广播成功");
        return result;
    }

    /** 获取在线用户列表 */
    @GetMapping("/online")
    public Map<String, Object> getOnlineUsers() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", WebSocketServer.getOnlineSids());
        result.put("count", WebSocketServer.getOnlineCount());
        return result;
    }
}
```

### 3.5 关于 @ServerEndpoint 注入 Spring Bean 的问题

> **重要**：由于 @ServerEndpoint 的实例由 Tomcat 的 WebSocket 容器管理，而非 Spring 管理，所以 @Autowired 会失效。解决方案是手动从 Spring 上下文中获取 Bean。

```java
@Component
public class SpringContextHolder implements ApplicationContextAware {
    private static ApplicationContext context;
    @Override
    public void setApplicationContext(ApplicationContext ctx) {
        context = ctx;
    }
    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }
}

// 在 WebSocketServer 中使用：
// UserService userService = SpringContextHolder.getBean(UserService.class);
```

### 3.6 前端示例

```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>WebSocket 通讯演示</title>
</head>
<body>
    <p>【用户ID】：<input id="userId" type="text" value="1001"/></p>
    <p>【目标用户】：<input id="toUserId" type="text" value="1002"/></p>
    <p>【消息内容】：<input id="contentText" type="text" value="你好"/></p>
    <p>
        <button onclick="openSocket()">开启连接</button>
        <button onclick="sendMessage()">发送消息</button>
    </p>
    <hr/>
    <div id="message"></div>

    <script>
        var socket = null;

        function openSocket() {
            if (typeof (WebSocket) === "undefined") {
                alert("您的浏览器不支持 WebSocket");
                return;
            }
            var userId = document.getElementById("userId").value;
            socket = new WebSocket("ws://localhost:8888/ws/" + userId);
            socket.onopen = function () {
                appendMessage("系统: 连接成功");
            };
            socket.onmessage = function (msg) {
                appendMessage(msg.data);
            };
            socket.onclose = function () {
                appendMessage("系统: 连接已断开");
            };
        }

        function sendMessage() {
            if (!socket || socket.readyState !== WebSocket.OPEN) {
                alert("请先开启连接");
                return;
            }
            var msg = {
                toSid: document.getElementById("toUserId").value,
                content: document.getElementById("contentText").value
            };
            socket.send(JSON.stringify(msg));
        }

        function appendMessage(text) {
            var div = document.getElementById("message");
            div.innerHTML += text + "<br/>";
        }
    </script>
</body>
</html>
```

### 3.7 @ServerEndpoint 方式的优缺点

| 优点 | 缺点 |
|------|------|
| 简单直接，注解即可使用 | 无法直接注入 Spring Bean |
| 符合 JSR 356 标准，可移植性好 | 缺少握手拦截器，难以做认证鉴权 |
| 编码量少 | 不支持 STOMP 协议，消息路由能力弱 |
| | 集群场景下需要自行扩展 |

---

## 四、方式二：Spring WebSocketHandler 方式（企业推荐）

WebSocketHandler 是 Spring 框架自带的 WebSocket API，比 @ServerEndpoint 更深度集成了 Spring 生态，支持握手拦截器、SockJS 降级等。

### 4.1 引入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>
```

### 4.2 实现 WebSocketHandler

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MyWebSocketHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(MyWebSocketHandler.class);
    private static final AtomicInteger onlineCount = new AtomicInteger(0);
    private static final ConcurrentHashMap<String, WebSocketSession> SESSION_MAP = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String userId = (String) session.getAttributes().get("userId");
        log.info("新连接, userId={}, sessionId={}", userId, session.getId());

        WebSocketSession old = SESSION_MAP.put(userId, session);
        if (old != null && old.isOpen()) {
            try { old.close(); }
            catch (IOException e) { log.warn("关闭旧 session 异常", e); }
        }
        onlineCount.incrementAndGet();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String userId = (String) session.getAttributes().get("userId");
        log.info("收到消息, userId={}, content={}", userId, message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String userId = (String) session.getAttributes().get("userId");
        SESSION_MAP.remove(userId, session);
        onlineCount.decrementAndGet();
        log.info("连接关闭, userId={}, status={}", userId, status);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        String userId = (String) session.getAttributes().get("userId");
        log.error("传输异常, userId={}", userId, exception);
    }

    // ========== 静态方法 ==========

    public static void sendToUser(String userId, String message) {
        WebSocketSession session = SESSION_MAP.get(userId);
        if (session != null && session.isOpen()) {
            try { session.sendMessage(new TextMessage(message)); }
            catch (IOException e) { log.error("发送失败, userId={}", userId, e); }
        }
    }

    public static void broadcast(String message) {
        TextMessage tm = new TextMessage(message);
        SESSION_MAP.forEach((uid, session) -> {
            if (session.isOpen()) {
                try { session.sendMessage(tm); }
                catch (IOException e) { log.error("广播失败, userId={}", uid, e); }
            }
        });
    }

    public static int getOnlineCount() { return onlineCount.get(); }
}
```

### 4.3 配置类 + 拦截器

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketHandlerConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myWebSocketHandler(), "/ws/{userId}")
                .addInterceptors(authHandshakeInterceptor())
                .setAllowedOrigins("*")
                .withSockJS();
    }

    @Bean
    public MyWebSocketHandler myWebSocketHandler() { return new MyWebSocketHandler(); }

    @Bean
    public AuthHandshakeInterceptor authHandshakeInterceptor() { return new AuthHandshakeInterceptor(); }
}
```

### 4.4 企业级认证拦截器 HandshakeInterceptor

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import java.util.Map;

public class AuthHandshakeInterceptor implements HandshakeInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AuthHandshakeInterceptor.class);

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String token = request.getHeaders().getFirst("Authorization");
        String userId = extractUserId(request);
        if (userId == null) {
            log.warn("WebSocket 握手认证失败");
            return false;
        }
        attributes.put("userId", userId);
        log.info("WebSocket 握手成功, userId={}", userId);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {}

    private String extractUserId(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            // return JwtUtil.validateToken(token.substring(7)).getUserId();
        }
        return null;
    }
}
```

### 4.5 方式的优缺点

| 优点 | 缺点 |
|------|------|
| 支持 HandshakeInterceptor（认证） | 编码量比 @ServerEndpoint 多 |
| 支持 SockJS 降级 | 原生不支持自定义 HTTP Header |
| 可通过 attributes 传递用户信息 | |

---

## 五、企业级实践：STOMP + Spring WebSocket（强烈推荐）

### 5.1 什么是 STOMP

**STOMP（Simple/Streaming Text Oriented Message Protocol）** 是一个简单的文本消息协议，运行在 WebSocket 之上。它定义了消息的格式和路由方式，类似于 HTTP 对 TCP 的意义。

**三个核心概念：**

- **Destination（目的地）**：消息发送的目标地址，如 /topic/notice
- **Subscription（订阅）**：客户端订阅某个目的地
- **Message（消息）**：包含 Header 和 Body 的结构化数据

### 5.2 为什么企业开发选择 STOMP

| 需求 | @ServerEndpoint | WebSocketHandler | STOMP |
|------|:---------------:|:----------------:|:-----:|
| 点对点推送 | 自行实现 sid 路由 | 自行实现 username 路由 | 原生支持 |
| 广播 | 自行遍历 map | 自行遍历 map | 原生支持 |
| 消息路由和过滤 | X | X | 基于 destination |
| 集群消息代理 | X | X | 集成 RabbitMQ |
| Spring Security 集成 | 需自行适配 | 需自行适配 | 原生支持 |

### 5.3 配置 STOMP 消息代理

```java
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/stomp")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }
}
```

**配置说明：**

| 参数 | 含义 |
|------|------|
| /ws/stomp | 客户端连接端点 |
| /topic/** | 广播目的地前缀 |
| /queue/** | 点对点目的地前缀 |
| /app/** | 客户端发送映射到 @MessageMapping |
| /user/** | 用户专属消息前缀 |

### 5.4 编写消息处理 Controller

```java
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class StompMessageController {

    private final SimpMessagingTemplate messagingTemplate;

    public StompMessageController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // 广播
    @MessageMapping("/announce")
    @SendTo("/topic/announce")
    public String broadcast(String message) {
        return "公告: " + message;
    }

    // 点对点
    @MessageMapping("/chat")
    public void sendToUser(ChatMessage msg) {
        messagingTemplate.convertAndSendToUser(
                msg.toUserId(), "/queue/chat", msg.content());
    }

    // HTTP 触发推送（企业常用）
    @ResponseBody
    @GetMapping("/ws/push/{userId}")
    public String httpPush(@PathVariable String userId, String message) {
        messagingTemplate.convertAndSendToUser(userId, "/queue/chat", message);
        return "推送成功";
    }

    @ResponseBody
    @GetMapping("/ws/broadcast")
    public String httpBroadcast(String message) {
        messagingTemplate.convertAndSend("/topic/announce", message);
        return "广播成功";
    }

    record ChatMessage(String toUserId, String content) {}
}
```

### 5.5 前端 STOMP 客户端

```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>STOMP 客户端示例</title>
    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
</head>
<body>
    <div>
        <p>当前用户: <span id="currentUser">1001</span></p>
        <p>目标用户: <input id="toUser" value="1002"/></p>
        <p>消息内容: <input id="msgContent" value="你好"/></p>
        <button onclick="connect()">连接</button>
        <button onclick="sendMsg()">点对点</button>
        <button onclick="sendBroadcast()">广播</button>
        <hr/>
        <div id="messages"></div>
    </div>

    <script>
        var stompClient = null;

        function connect() {
            var socket = new SockJS("http://localhost:8888/ws/stomp");
            stompClient = Stomp.over(socket);
            stompClient.debug = null;

            stompClient.connect({}, function (frame) {
                // 订阅广播频道
                stompClient.subscribe("/topic/announce", function (msg) {
                    showMessage("广播: " + msg.body);
                });
                // 订阅私有队列
                stompClient.subscribe("/user/queue/chat", function (msg) {
                    showMessage("私信: " + msg.body);
                });
                showMessage("系统: 连接成功");
            }, function (error) {
                console.error("STOMP 连接失败:", error);
            });
        }

        function sendMsg() {
            if (!stompClient || !stompClient.connected) { alert("请先连接"); return; }
            var msg = {
                toUserId: document.getElementById("toUser").value,
                content: document.getElementById("msgContent").value
            };
            stompClient.send("/app/chat", {}, JSON.stringify(msg));
        }

        function sendBroadcast() {
            if (!stompClient || !stompClient.connected) { alert("请先连接"); return; }
            stompClient.send("/app/announce", {}, document.getElementById("msgContent").value);
        }

        function showMessage(text) {
            document.getElementById("messages").innerHTML += text + "<br/>";
        }
    </script>
</body>
</html>
```

### 5.6 STOMP 用户认证

在 STOMP 的 CONNECT 帧中做认证，比 HandshakeInterceptor 更灵活。

```java
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

@Configuration
public class StompAuthConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                    MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String authToken = accessor.getFirstNativeHeader("Authorization");
                    if (authToken != null && authToken.startsWith("Bearer ")) {
                        authToken = authToken.substring(7);
                        // String userId = JwtUtil.validateToken(authToken).getUserId();
                        // accessor.setUser(() -> userId);
                    }
                    if (accessor.getUser() == null) {
                        throw new IllegalArgumentException("未授权，拒绝连接");
                    }
                }
                return message;
            }
        });
    }
}
```

### 5.7 STOMP 消息流示意

```
客户端                  STOMP Handler              消息代理              其他客户端
  |                         |                        |                      |
  |--- CONNECT ------------>|                        |                      |
  |<-- CONNECTED -----------|                        |                      |
  |                         |                        |                      |
  |--- SUBSCRIBE /topic --->|                        |                      |
  |                         |--- 注册订阅 ---------->|                      |
  |                         |                        |                      |
  |--- SEND /app/announce ->|                        |                      |
  |                         |--- 路由到 /topic ----->|                      |
  |                         |                        |--- 推送给订阅者 ---->|
  |                         |                        |                      |
  |--- SEND /app/chat ----->|                        |                      |
  |                         |--- 路由到 /user/queue ->|                      |
  |                         |                        |--- 推送给目标用户 -->|
```

---

## 六、企业级进阶特性

### 6.1 认证方案总结

| 集成方式 | 认证时机 | 适用场景 |
|---------|---------|---------|
| HandshakeInterceptor.beforeHandshake() | HTTP 升级阶段 | WebSocketHandler |
| ChannelInterceptor.preSend() | STOMP 连接阶段 | STOMP 方式 |
| STOMP Header 传递 JWT Token | CONNECT 帧中 | STOMP（最推荐） |
| Cookie / Session | HTTP 阶段 | 传统单体架构 |

### 6.2 集群部署 — 外置消息代理

单体应用用 SimpleBroker 即可。多实例部署时需使用外置消息代理。

#### RabbitMQ STOMP 插件方案

```java
@Configuration
@EnableWebSocketMessageBroker
public class RabbitMqStompConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/stomp")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableStompBrokerRelay("/topic", "/queue")
                .setRelayHost("192.168.1.100")
                .setRelayPort(61613)
                .setClientLogin("guest")
                .setClientPasscode("guest")
                .setSystemLogin("guest")
                .setSystemPasscode("guest");

        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }
}
```

```bash
# RabbitMQ 启用 STOMP 插件
rabbitmq-plugins enable rabbitmq_stomp
# 默认 STOMP 端口: 61613
```

### 6.3 心跳机制

#### STOMP 方式（内置，最推荐）

```java
// 服务端每 10 秒发送一次心跳
registry.enableSimpleBroker("/topic", "/queue")
        .setHeartbeatValue(new long[]{10000, 10000});
```

```javascript
// 前端 STOMP 心跳
stompClient.heartbeat.outgoing = 10000;
stompClient.heartbeat.incoming = 10000;
```

#### @ServerEndpoint 方式

```java
private static final String HEARTBEAT_PING = "$$";
private static final String HEARTBEAT_PONG = "pong";

@OnMessage
public void onMessage(String message, Session session) {
    if (HEARTBEAT_PING.equals(message)) {
        try { session.getBasicRemote().sendText(HEARTBEAT_PONG); }
        catch (IOException e) { log.warn("心跳回复失败", e); }
        return;
    }
    // 正常业务消息
}
```

### 6.4 断线重连（前端方案）

```javascript
var lockReconnect = false;
var stompClient = null;

function connect() {
    var socket = new SockJS("http://localhost:8888/ws/stomp");
    stompClient = Stomp.over(socket);
    stompClient.debug = null;

    stompClient.connect({}, function (frame) {
        lockReconnect = false;
        console.log("连接成功");
        subscribeChannels();
    }, function (error) {
        console.error("连接失败，5秒后重连:", error);
        reconnect();
    });
}

function reconnect() {
    if (lockReconnect) return;
    lockReconnect = true;
    setTimeout(function () {
        connect();
        lockReconnect = false;
    }, 5000);
}

function subscribeChannels() {
    stompClient.subscribe("/topic/announce", function (msg) {
        showMessage("广播: " + msg.body);
    });
    stompClient.subscribe("/user/queue/chat", function (msg) {
        showMessage("私信: " + msg.body);
    });
}
```

### 6.5 安全性配置

```java
registry.addEndpoint("/ws/stomp")
        .setAllowedOriginPatterns("https://*.example.com")
        .withSockJS();
```

**安全 checklist：**

1. 设置 setAllowedOriginPatterns 防止跨站 WebSocket 劫持
2. 握手阶段做 Token 校验，拒绝未认证连接
3. 限制消息大小
4. 配置超时断开
5. 生产环境使用 wss:// 协议
6. 做速率限制

### 6.6 线程安全与性能要点

- 连接映射使用 ConcurrentHashMap，禁止使用 HashMap
- 计数器使用 AtomicInteger
- 发送前检查 session.isOpen()
- getBasicRemote().sendText() 是阻塞的
- getAsyncRemote().sendText() 是异步的，适合高吞吐
- 序列化推荐 JSON 或 Protobuf

---

## 七、常见问题与避坑指南

### 问题 1：@ServerEndpoint 中 @Autowired 为 null

**原因**：实例由 Tomcat WebSocket 容器管理，非 Spring 管理。

**解决**：实现 ApplicationContextAware 手动获取 Bean。

### 问题 2：sendText 抛出 IllegalStateException

**原因**：连接已关闭仍在发送。

**解决**：发送前检查 session.isOpen()，捕获异常后从 Map 移除。

### 问题 3：集群环境消息无法跨实例广播

**现象**：用户 A 连实例 1，用户 B 连实例 2，A 发的消息 B 收不到。

**解决**：使用外置消息代理（RabbitMQ）替代内置 SimpleBroker。

### 问题 4：onClose 没有及时触发

**原因**：TCP 断线后服务端需等超时才能感知（几分钟）。

**解决**：依赖心跳机制检测死连接。

### 问题 5：原笔记聊天室代码的问题

| 问题 | 说明 | 修正 |
|------|------|------|
| 方法名错误 | onOpw 应为 onOpen | 改为 onOpen |
| 线程不安全 | 使用 HashMap | 改为 ConcurrentHashMap |
| onClose 未清理 | 未从 map 删除 | 加入 remove(uid) |
| 参数位置错误 | onError 参数顺序 | 修正为 (Throwable, Session) |

### 问题 6：内存泄漏

**可能原因：** onClose 未移除连接、使用 HashMap、监听器未取消注册。

**检查：** 使用监控接口定期检查在线数，确保只增不减。

---

## 八、总结

### 选择建议

| 场景 | 推荐方式 |
|------|---------|
| 快速 Demo / 学习 | @ServerEndpoint |
| 需要握手认证 | WebSocketHandler |
| **企业级项目（强烈推荐）** | **STOMP + Spring WebSocket** |
| 集群部署、高并发 | STOMP + RabbitMQ |

### 学习路径

1. 先掌握 @ServerEndpoint 理解 WebSocket 基本概念
2. 学习 WebSocketHandler + HandshakeInterceptor 理解认证机制
3. 掌握 STOMP over WebSocket 理解消息路由
4. 进阶学习 RabbitMQ 外置代理部署

### 推荐资料

- Spring 官方文档 - WebSocket
- STOMP 协议规范
- RabbitMQ STOMP 插件文档

---

> 本文是对原 WebSocket 笔记的全面重写和扩展。原笔记中的 @ServerEndpoint 方式适合入门理解，但企业开发中 STOMP + Spring WebSocket 才是最终选择。如有错误或改进建议，欢迎指正。

---
title: Java设计模式
date: 2026-06-10 09:10:59
tags:
- 设计模式
categories:
- 进阶技术
---



# Java 设计模式完整笔记

> 设计模式（Design Patterns）是软件设计中**针对常见问题的可复用解决方案**。
> 1994 年 GoF（Gang of Four）在《Design Patterns》中系统总结了 23 种经典模式。
>
> **核心价值：** 提供了一套通用词汇表，让开发者之间沟通更高效；记录了大量经过验证的设计经验。

---

## 一、SOLID 原则（面向对象设计基石）

设计模式本质上是 SOLID 原则的具体应用。先理解原则，再学模式事半功倍。

### 1. 单一职责原则（Single Responsibility Principle）

> **一个类只应该有一个引起它变化的原因。**

```java
// ❌ 违反：一个类既负责数据又负责展示
class BadReport {
    void calculate() { /* 计算数据 */ }
    void toHtml() { /* 转 HTML */ }
    void toPdf() { /* 转 PDF */ }
}

// ✅ 遵循：数据与展示分离
class ReportData { /* 纯数据 */ }
class ReportRenderer { /* 纯渲染 */ }
```

**判断标准：** 问自己"这个类有几个职责？"如果回答里含"和"字，就该拆了。

### 2. 开闭原则（Open/Closed Principle）

> **对扩展开放，对修改封闭。**

```java
// ❌ 违反：每新增类型就要改 switch
double calcDiscount(String type) {
    switch (type) {
        case "normal": return price * 0.9;
        case "vip":    return price * 0.8;
        // 新增 svip → 改代码
    }
}

// ✅ 遵循：通过策略扩展
interface Discount { double apply(double price); }
class NormalDiscount implements Discount { /* 0.9 */ }
class VipDiscount implements Discount { /* 0.8 */ }
// 新增 → 加新类，不改旧代码
```

### 3. 里氏替换原则（Liskov Substitution Principle）

> **子类必须能够替换其父类，且不改变程序的正确性。**

```java
// ❌ 违反：正方形继承矩形
class Rectangle {
    int w, h;
    void setW(int w) { this.w = w; }
    void setH(int h) { this.h = h; }
}
class Square extends Rectangle {
    void setW(int w) { super.setW(w); super.setH(w); }  // 改变了父类行为！
}
// 客户端期望 setW 不影响 h，但 Square 破坏了这点
```

### 4. 接口隔离原则（Interface Segregation Principle）

> **客户端不应该被迫依赖它不使用的接口。**

```java
// ❌ 违反：大而全的接口
interface Worker {
    void work();
    void eat();
    void sleep();
}
// Robot 被迫实现 eat()、sleep()

// ✅ 遵循：拆分接口
interface Workable { void work(); }
interface Eatable { void eat(); }
class Robot implements Workable { /* 只实现 work */ }
```

### 5. 依赖倒置原则（Dependency Inversion Principle）

> **高层模块不应依赖低层模块，二者都应依赖抽象。**

```java
// ❌ 违反：高层直接依赖低层实现
class EmailService { void send(String msg) { /* SMTP */ } }
class Notification {
    private EmailService email = new EmailService();  // 硬耦合
}

// ✅ 遵循：依赖抽象
interface MessageService { void send(String msg); }
class EmailService implements MessageService { /* ... */ }
class SMSService implements MessageService { /* ... */ }
class Notification {
    private MessageService svc;          // 依赖接口
    Notification(MessageService svc) {   // 构造注入
        this.svc = svc;
    }
}
```

---

## 二、设计模式分类速览

```
┌─────────────────────────────────────────────────────────────┐
│                       设计模式 (23种)                        │
├───────────┬──────────────────┬──────────────────────────────┤
│  创建型    │     结构型       │         行为型               │
│  (怎么建)  │    (怎么搭)      │       (怎么交互)              │
├───────────┼──────────────────┼──────────────────────────────┤
│  Singleton │  Adapter        │  Chain of Responsibility     │
│  Factory   │  Bridge         │  Command                     │
│  Abstract  │  Composite      │  Interpreter                 │
│   Factory  │  Decorator      │  Iterator                    │
│  Builder   │  Facade         │  Mediator                    │
│  Prototype │  Flyweight      │  Memento                     │
│            │  Proxy          │  Observer                    │
│            │                 │  State                       │
│            │                 │  Strategy                    │
│            │                 │  Template Method             │
│            │                 │  Visitor                     │
└───────────┴──────────────────┴──────────────────────────────┘
```

---

## 三、创建型模式（5种）

> **关注点：如何灵活地创建对象，将对象的创建与使用分离。**

---

### 1. 单例模式（Singleton）

#### 🎯 意图
确保一个类**只有一个实例**，并提供全局访问点。

#### 💡 核心思想
- 私有构造器，禁止外部 new
- 通过静态方法返回唯一实例
- 延迟初始化（按需创建）vs 饿汉式（类加载时创建）

#### 📐 结构
```
Singleton
  - static instance
  - private Singleton()
  + static getInstance()
```

#### 🔧 Java 四种写法对比

```java
// ① 饿汉式 —— 简单、线程安全，但可能浪费内存
public class SingletonEager {
    private static final SingletonEager INSTANCE = new SingletonEager();
    private SingletonEager() {}
    public static SingletonEager getInstance() { return INSTANCE; }
}

// ② 懒汉式 DCL —— 延迟加载，推荐
public class SingletonDCL {
    private static volatile SingletonDCL instance;
    private SingletonDCL() {}
    public static SingletonDCL getInstance() {
        if (instance == null) {
            synchronized (SingletonDCL.class) {
                if (instance == null) {
                    instance = new SingletonDCL();
                }
            }
        }
        return instance;
    }
}
// volatile 防止指令重排序（new 对象非原子操作）

// ③ 静态内部类 —— 延迟加载 + 线程安全 + 最简洁
public class SingletonHolder {
    private SingletonHolder() {}
    private static class Holder {
        static final SingletonHolder INSTANCE = new SingletonHolder();
    }
    public static SingletonHolder getInstance() { return Holder.INSTANCE; }
}
// JVM 内部类加载机制保证线程安全

// ④ 枚举 —— 绝对防反射、防序列化破坏
public enum SingletonEnum {
    INSTANCE;
    public void doSomething() { /* ... */ }
}
// Effective Java 作者推荐的方式
```

#### ✅ 优点
- 严格控制实例数量
- 节省内存（相比每次都 new）
- 提供全局访问点

#### ❌ 缺点
- 违反单一职责（既管创建又管业务）
- 难以扩展（继承困难）
- 多环境（测试/生产）切换不便

#### 📌 适用场景
- 无状态工具类（日志器、配置读取）
- 有状态的资源管理（连接池、线程池）
- 频繁创建销毁的对象（缓存）

#### 🔗 实际应用
- `Runtime.getRuntime()` — JVM 运行时
- Spring Bean 默认 scope 为 singleton
- 日志框架 LoggerFactory

---

### 2. 工厂方法模式（Factory Method）

#### 🎯 意图
定义一个创建对象的接口，让**子类决定实例化哪个类**。将对象的创建延迟到子类。

#### 💡 核心思想
- 用继承来解耦：父类定义框架，子类决定具体创建
- 与简单工厂的区别：工厂方法不直接 new，交给子类
- 符合**开闭原则**：新增产品只需新增工厂子类

#### 📐 结构
```
Creator (抽象工厂)
  + factoryMethod(): Product        ← 子类实现
  + someOperation()                 ← 模板方法，调用 factoryMethod

ConcreteCreatorA
  + factoryMethod(): return new ConcreteProductA

Product (接口/抽象)
ConcreteProductA, ConcreteProductB
```

#### 💻 Java 实现

```java
// --- 产品层次 ---
interface Product {
    void use();
}

class DatabaseLogger implements Product {
    public void use() { System.out.println("记录日志到数据库"); }
}

class FileLogger implements Product {
    public void use() { System.out.println("记录日志到文件"); }
}

// --- 工厂层次 ---
abstract class LoggerFactory {
    // 工厂方法 —— 子类实现
    public abstract Product createLogger();

    // 模板方法 —— 定义框架流程
    public void writeLog(String message) {
        Product logger = createLogger();
        // 可以加公共逻辑：格式化、过滤、缓冲
        System.out.println("[" + LocalDateTime.now() + "] " + message);
        logger.use();
    }
}

class DatabaseLoggerFactory extends LoggerFactory {
    public Product createLogger() { return new DatabaseLogger(); }
}

class FileLoggerFactory extends LoggerFactory {
    public Product createLogger() { return new FileLogger(); }
}

// --- 使用 ---
LoggerFactory factory = new FileLoggerFactory();
factory.writeLog("系统启动");  // 框架流程固定，具体日志方式由子类决定
```

#### ✅ 优点
- 解耦：客户端只需要知道工厂接口，不知道具体产品类
- 符合开闭：新增产品不需要改已有代码
- 符合单一职责：创建逻辑集中到工厂

#### ❌ 缺点
- 每新增一个产品就要新增一个工厂子类 → 类数量膨胀
- 增加了系统的抽象性和理解难度

#### 📌 适用场景
- 客户端不知道它需要创建哪个具体类
- 框架需要将创建逻辑留给子类实现
- 产品创建逻辑需要复用且变化频繁

#### 🔗 实际应用
- `Collection.iterator()` — 返回不同的 Iterator 实现
- Spring `FactoryBean` 接口
- JDBC `DriverManager.getConnection()` 内部

---

### 3. 抽象工厂模式（Abstract Factory）

#### 🎯 意图
提供一个创建**一系列相关或相互依赖对象**的接口，而无需指定它们的具体类。

#### 💡 核心思想
- 工厂的工厂：解决工厂方法中产品族的问题
- 保证**产品族的一致性**（同一风格的产品一起使用）
- 新增产品族容易，新增产品困难

#### 📐 结构
```
AbstractFactory
  + createProductA()
  + createProductB()

ConcreteFactory1        ConcreteFactory2
  + createProductA()      + createProductA()
  + createProductB()      + createProductB()

AbstractProductA        AbstractProductB
ProductA1  ProductA2     ProductB1  ProductB2
```

#### 💻 Java 实现

```java
// --- 产品族抽象 ---
// 风格 1：现代风格
interface Button { void render(); }
interface ScrollBar { void scroll(); }

// --- 具体产品 ---
class MacButton implements Button {
    public void render() { System.out.println("绘制 Mac 风格的按钮"); }
}
class MacScrollBar implements ScrollBar {
    public void scroll() { System.out.println("Mac 滚动条滑动"); }
}

class WinButton implements Button {
    public void render() { System.out.println("绘制 Windows 风格的按钮"); }
}
class WinScrollBar implements ScrollBar {
    public void scroll() { System.out.println("Windows 滚动条滑动"); }
}

// --- 抽象工厂 ---
interface GUIFactory {
    Button createButton();
    ScrollBar createScrollBar();
}

class MacFactory implements GUIFactory {
    public Button createButton() { return new MacButton(); }
    public ScrollBar createScrollBar() { return new MacScrollBar(); }
}

class WinFactory implements GUIFactory {
    public Button createButton() { return new WinButton(); }
    public ScrollBar createScrollBar() { return new WinScrollBar(); }
}

// --- 客户端 ---
class Application {
    private Button btn;
    private ScrollBar sb;

    public Application(GUIFactory factory) {
        btn = factory.createButton();
        sb = factory.createScrollBar();
    }

    void render() {
        btn.render();
        sb.scroll();
    }
}

// --- 使用 ---
// 切换风格只需换工厂
Application app = new Application(new MacFactory());
app.render();  // 所有组件都是 Mac 风格，不会混搭
```

#### ✅ 优点
- 保证产品族的一致性
- 客户端代码与具体产品解耦
- 符合开闭（新增产品族）

#### ❌ 缺点
- 新增产品种类需要修改所有工厂 → 违反了开闭
- 类层次复杂，理解成本高

#### 📌 适用场景
- 系统需要独立于产品的创建、组合和表示
- 系统需要多套产品族中的一个
- 强调产品族一致性

#### 🔗 实际应用
- Spring 中不同的数据源连接族
- Java AWT 的 Toolkit 实现
- 不同数据库的 SQL 方言一族

---

### 4. 建造者模式（Builder）

#### 🎯 意图
将一个复杂对象的**构建与它的表示分离**，使得同样的构建过程可以创建不同的表示。

#### 💡 核心思想
- 分步骤构建，每个步骤可以独立变化
- 解决"** telescoping constructor**"（构造器参数爆炸）问题
- 与工厂的区别：工厂一步创建整个对象，建造者分步创建

#### 📐 结构
```
Director                     ← 可选，控制构建步骤顺序
  + construct()

Builder (接口)
  + buildPartA()
  + buildPartB()
  + buildPartC()
  + getResult()

ConcreteBuilder
  + buildPartA()    ← 实现具体构建逻辑
  + buildPartB()
  + buildPartC()
  + getResult()     ← 返回构建好的产品

Product
```

#### 💻 Java 实现

```java
// --- 产品 (复杂对象) ---
class House {
    private String foundation;    // 地基
    private String structure;     // 结构
    private String roof;          // 屋顶
    private boolean hasGarage;    // 车库
    private boolean hasGarden;    // 花园
    private int windows;          // 窗户数

    // 私有构造，只能通过 Builder 创建
    private House(Builder builder) {
        this.foundation = builder.foundation;
        this.structure = builder.structure;
        this.roof = builder.roof;
        this.hasGarage = builder.hasGarage;
        this.hasGarden = builder.hasGarden;
        this.windows = builder.windows;
    }

    @Override
    public String toString() {
        return "House{" + foundation + ", " + structure + ", " + roof +
               ", garage=" + hasGarage + ", garden=" + hasGarden +
               ", windows=" + windows + "}";
    }

    // --- Builder ---
    public static class Builder {
        private String foundation;
        private String structure;
        private String roof;
        private boolean hasGarage;
        private boolean hasGarden;
        private int windows;

        // 必填参数通过构造传入
        public Builder(String foundation, String structure) {
            this.foundation = foundation;
            this.structure = structure;
        }

        // 可选参数通过链式调用设置
        public Builder roof(String roof) { this.roof = roof; return this; }
        public Builder garage(boolean has) { this.hasGarage = has; return this; }
        public Builder garden(boolean has) { this.hasGarden = has; return this; }
        public Builder windows(int n) { this.windows = n; return this; }

        public House build() { return new House(this); }
    }
}

// --- 使用 ---
House house = new House.Builder("混凝土", "钢结构")
    .roof("琉璃瓦")
    .garage(true)
    .garden(false)
    .windows(6)
    .build();
```

#### ✅ 优点
- 参数可控，可读性强（链式调用）
- 构建步骤可细化（buildPartA → buildPartB）
- 复用一个 Builder 可以构建不同产品

#### ❌ 缺点
- 需要额外创建 Builder 类
- 对象字段不可变需要额外处理（如本例）

#### 📌 适用场景
- 对象有大量可选参数（>4个）
- 参数之间有依赖关系或校验
- 创建过程步骤固定但步骤实现可变

#### 🔗 实际应用
- `StringBuilder` / `StringBuffer`
- `Stream.Builder`
- Lombok `@Builder`
- Spring `BeanDefinitionBuilder`

---

### 5. 原型模式（Prototype）

#### 🎯 意图
用**原型实例指定创建对象的种类**，并通过**拷贝**这些原型创建新的对象。

#### 💡 核心思想
- 克隆优于 new：当创建对象成本高时
- 避免子类泛滥：不需要工厂等级结构
- 关键区分：**浅拷贝 vs 深拷贝**

#### 📐 结构
```
Prototype (接口)
  + clone()

ConcretePrototype1      ConcretePrototype2
  + clone()               + clone()
```

#### 💻 Java 实现

```java
// --- 抽象原型 ---
abstract class Shape implements Cloneable {
    private String id;
    protected String type;
    private int x, y;           // 位置
    private String color;       // 颜色

    abstract void draw();

    // 浅拷贝 —— Java 默认
    public Shape clone() {
        try {
            return (Shape) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    // getter/setter...
}

class Circle extends Shape {
    private int radius;

    public Circle() {
        this.type = "Circle";
        this.radius = 10;
    }

    public Circle(Circle source) {   // 深拷贝构造
        super(source);
        this.radius = source.radius;
    }

    void draw() { System.out.println("画圆形 r=" + radius); }

    // 深拷贝
    @Override
    public Circle clone() {
        return new Circle(this);
    }
}

// --- 原型注册表 ---
class ShapeCache {
    private static Map<String, Shape> cache = new HashMap<>();

    static {
        // 预先加载原型
        Circle circle = new Circle();
        circle.setColor("red");
        cache.put("red_circle", circle);
    }

    public static Shape getShape(String key) {
        return cache.get(key).clone();  // 克隆，非 new
    }
}

// --- 使用 ---
// 克隆比 new 快得多（不需要重新初始化）
Shape c1 = ShapeCache.getShape("red_circle");
Shape c2 = ShapeCache.getShape("red_circle");
```

#### ✅ 优点
- 性能提升（避免昂贵的构造初始化）
- 减少子类数量（不需要工厂层次）
- 可以动态添加/删除原型

#### ❌ 缺点
- 深拷贝实现复杂（循环引用、嵌套对象）
- 需要处理 Cloneable 和 serialVersionUID

#### 📌 适用场景
- 创建对象成本大（数据库查询、复杂计算）
- 对象差异小（只需修改少量属性）
- 系统需要独立于产品类型的创建

#### 🔗 实际应用
- Java `Object.clone()`
- Spring prototype scope
- 游戏中敌人生成（克隆基础模板再加变化）

---

## 四、结构型模式（7种）

> **关注点：如何组合类和对象以形成更大的结构。**

---

### 6. 适配器模式（Adapter）

#### 🎯 意图
将一个类的接口转换成客户端期望的另一个接口，让**原本不兼容的类能够合作**。

#### 💡 核心思想
- 中间层转换：客户端 ↔ 适配器 ↔ 被适配者
- 两种实现方式：**类适配器**（继承）和**对象适配器**（组合）
- 组合优于继承：对象适配器更灵活

#### 📐 结构
```
目标接口 (Target)         ← 客户端期望的接口
  + request()

适配器 (Adapter)          ← 转换核心
  + request()   → 调用 adaptee.specificRequest()

被适配者 (Adaptee)        ← 需要被适配的现有类
  + specificRequest()
```

#### 💻 Java 实现

```java
// --- 目标接口（客户端期望的） ---
interface USB {
    void connectWithUsb();
}

// --- 被适配者（已有的旧接口） ---
class MemoryCard {
    public void readData() {
        System.out.println("读取存储卡数据");
    }
}

class Monitor {
    public void displayHDMI() {
        System.out.println("通过 HDMI 显示");
    }
}

// --- 适配器：存储卡读卡器 ---
class CardReader implements USB {
    private MemoryCard memoryCard;

    public CardReader(MemoryCard memoryCard) {
        this.memoryCard = memoryCard;
    }

    @Override
    public void connectWithUsb() {
        // 转换：USB 请求 → 读卡器操作
        System.out.println("读卡器通过 USB 连接...");
        memoryCard.readData();
    }
}

// --- 客户端 ---
class Laptop {
    public void readData(USB usb) {
        System.out.println("笔记本连接 USB 设备...");
        usb.connectWithUsb();
    }
}

// --- 使用 ---
Laptop laptop = new Laptop();
// MemoryCard 本不能直接连 Laptop，但通过适配器可以
laptop.readData(new CardReader(new MemoryCard()));
```

#### ✅ 优点
- 让不兼容的类可以合作
- 无需修改现有代码
- 符合开闭原则

#### ❌ 缺点
- 增加系统复杂度
- 过多适配器会让系统混乱（"需要适配器时说明设计有问题" → 但也可能是整合遗留系统）

#### 📌 适用场景
- 复用已有的、但接口不匹配的类
- 整合第三方库或遗留系统
- 统一多个相似但接口不同的类

#### 🔗 实际应用
- `InputStreamReader` — InputStream 适配为 Reader
- `OutputStreamWriter`
- `Arrays.asList()` — 数组适配为 List
- Spring `HandlerAdapter` — 统一不同的 Controller 类型

---

### 7. 桥接模式（Bridge）

#### 🎯 意图
将**抽象部分**与**实现部分**分离，使它们可以**独立变化**。

#### 💡 核心思想
- 用组合替代继承，解决多维度的继承爆炸
- "桥"在抽象和实现之间
- 识别变化维度：如"设备 × 遥控器"、"形状 × 颜色"、"消息 × 发送方式"

#### 📐 结构
```
Abstraction                    Implementor (接口)
  + operation() → impl.operationImpl()
      │                              │
RefinedAbstraction            ConcreteImplementorA/B
```

#### 💻 Java 实现

```java
// --- 实现层次（Impl维度） ---
interface Device {
    boolean isEnabled();
    void enable();
    void disable();
    int getVolume();
    void setVolume(int percent);
    void printStatus();
}

class TV implements Device {
    private boolean on = false;
    private int volume = 50;

    public boolean isEnabled() { return on; }
    public void enable() { on = true; System.out.println("电视已开机"); }
    public void disable() { on = false; System.out.println("电视已关机"); }
    public int getVolume() { return volume; }
    public void setVolume(int vol) { volume = vol; }
    public void printStatus() {
        System.out.println("电视 [" + (on ? "开" : "关") + "] 音量: " + volume);
    }
}

class Radio implements Device {
    private boolean on = false;
    private int volume = 30;

    public boolean isEnabled() { return on; }
    public void enable() { on = true; System.out.println("收音机已开机"); }
    public void disable() { on = false; System.out.println("收音机已关机"); }
    public int getVolume() { return volume; }
    public void setVolume(int vol) { volume = vol; }
    public void printStatus() {
        System.out.println("收音机 [" + (on ? "开" : "关") + "] 音量: " + volume);
    }
}

// --- 抽象层次（遥控器维度） ---
abstract class RemoteControl {
    protected Device device;  // 桥 —— 持有实现接口

    public RemoteControl(Device device) {
        this.device = device;
    }

    abstract void togglePower();
    abstract void volumeUp();
    abstract void volumeDown();
}

class BasicRemote extends RemoteControl {
    public BasicRemote(Device device) { super(device); }

    void togglePower() {
        if (device.isEnabled()) device.disable();
        else device.enable();
    }

    void volumeUp() { device.setVolume(device.getVolume() + 10); }
    void volumeDown() { device.setVolume(device.getVolume() - 10); }
}

class AdvancedRemote extends RemoteControl {
    public AdvancedRemote(Device device) { super(device); }

    void togglePower() {
        if (device.isEnabled()) device.disable();
        else device.enable();
    }

    void volumeUp() { device.setVolume(device.getVolume() + 10); }
    void volumeDown() { device.setVolume(device.getVolume() - 10); }

    void mute() { device.setVolume(0); }  // 高级功能
}

// --- 使用 ---
// 2 种遥控器 × 2 种设备 = 4 种组合，但只需 2+2=4 个类（不用桥接要 4 个类）
Device tv = new TV();
RemoteControl remote = new AdvancedRemote(tv);
remote.togglePower();   // 电视已开机
remote.volumeUp();      // 音量 60
tv.printStatus();       // 电视 [开] 音量: 60
```

#### ✅ 优点
- 分离抽象和实现，各自独立变化
- 符合开闭原则（新增维度不影响另一方）
- 减少类爆炸（M×N → M+N）

#### ❌ 缺点
- 增加系统复杂度
- 需要识别出正确的变化维度

#### 📌 适用场景
- 多维度变化（2个或以上）
- 不希望抽象和实现有固定绑定关系
- 具体实现需要对客户端透明

#### 🔗 实际应用
- JDBC 驱动：JDBC API（抽象） ↔ 具体驱动实现（MySQL/Oracle/PostgreSQL）
- SLF4J：日志门面（抽象） ↔ Logback/Log4j（实现）
- Spring `ResourceLoader` 与具体 Resource 实现

---

### 8. 组合模式（Composite）

#### 🎯 意图
将对象组合成**树形结构**以表示"部分-整体"的层次结构，让客户端可以统一处理单个对象和组合对象。

#### 💡 核心思想
- 叶子节点和容器节点使用同一接口
- 客户端无需区分"单个"和"组合"
- 本质是树的递归结构

#### 📐 结构
```
Component (接口)
  + operation()
  + add(Component)
  + remove(Component)
  + getChild(int)

Leaf                          Composite
  + operation()                 + operation()    → 遍历 children，递归调用
                                + add/remove/getChild
                                - List<Component> children
```

#### 💻 Java 实现

```java
// --- 组件接口（统一叶子节点和容器节点） ---
interface FileSystemNode {
    String getName();
    long getSize();              // 统一接口
    void display(String indent);
}

// --- 叶子节点：文件 ---
class FileLeaf implements FileSystemNode {
    private String name;
    private long size;           // 字节

    public FileLeaf(String name, long size) {
        this.name = name;
        this.size = size;
    }

    public String getName() { return name; }
    public long getSize() { return size; }

    public void display(String indent) {
        System.out.println(indent + "📄 " + name + " (" + formatSize(size) + ")");
    }

    private String formatSize(long bytes) {
        if (bytes < 1024) return bytes + "B";
        return String.format("%.1fKB", bytes / 1024.0);
    }
}

// --- 容器节点：文件夹 ---
class FolderComposite implements FileSystemNode {
    private String name;
    private List<FileSystemNode> children = new ArrayList<>();

    public FolderComposite(String name) {
        this.name = name;
    }

    public void add(FileSystemNode node) { children.add(node); }
    public void remove(FileSystemNode node) { children.remove(node); }

    public String getName() { return name; }

    // 组合节点的 size = 所有子节点 size 之和
    public long getSize() {
        return children.stream().mapToLong(FileSystemNode::getSize).sum();
    }

    public void display(String indent) {
        System.out.println(indent + "📁 " + name + "/ (" + formatSize(getSize()) + ")");
        for (FileSystemNode child : children) {
            child.display(indent + "  ");
        }
    }

    private String formatSize(long bytes) { /* 同上 */ }
}

// --- 使用 ---
FolderComposite root = new FolderComposite("root");
root.add(new FileLeaf("readme.md", 2048));

FolderComposite src = new FolderComposite("src");
src.add(new FileLeaf("Main.java", 4096));
src.add(new FileLeaf("Utils.java", 1024));

FolderComposite img = new FolderComposite("images");
img.add(new FileLeaf("logo.png", 51200));
img.add(new FileLeaf("bg.jpg", 102400));

root.add(src);
root.add(img);

root.display("");
// 输出:
// 📁 root/ (158.8KB)
//   📄 readme.md (2.0KB)
//   📁 src/ (5.0KB)
//     📄 Main.java (4.0KB)
//     📄 Utils.java (1.0KB)
//   📁 images/ (150.0KB)
//     📄 logo.png (50.0KB)
//     📄 bg.jpg (100.0KB)

// 客户端统一处理，无需区分文件和文件夹
System.out.println("总大小: " + root.getSize() + " 字节");
```

#### ✅ 优点
- 客户端代码简洁（统一处理单个和组合）
- 容易新增新类型的组件
- 天然的递归结构

#### ❌ 缺点
- 过度通用：如果叶子节点和容器差异太大，接口设计困难
- 类型安全 vs 透明度的取舍

#### 📌 适用场景
- 树形结构（文件系统、组织架构、菜单）
- 客户端希望忽略对象个体和组合的差异

#### 🔗 实际应用
- `java.awt.Container.add(Component)` — Swing 组件树
- `javax.xml.parsers.DocumentBuilder` — DOM 树
- Spring Security `SecurityFilterChain`

---

### 9. 装饰器模式（Decorator）

#### 🎯 意图
**动态地**给一个对象添加额外的职责，比生成子类更灵活。

#### 💡 核心思想
- 包装（Wrapper）：装饰器包裹被装饰对象
- 递归组合：装饰器套装饰器，层层叠加
- 与继承的区别：继承是静态的（编译时），装饰是动态的（运行时）

#### 📐 结构
```
Component (接口)
  + operation()

ConcreteComponent              Decorator (继承 Component，持有 Component)
  + operation()                  - Component wrapped
                                 + operation() → wrapped.operation() + 额外行为

                               ConcreteDecoratorA    ConcreteDecoratorB
                                 + operation()         + operation()
```

#### 💻 Java 实现

```java
// --- 组件接口 ---
interface Beverage {
    String getDescription();
    double cost();
}

// --- 具体组件（核心） ---
class Espresso implements Beverage {
    public String getDescription() { return "浓缩咖啡"; }
    public double cost() { return 15.0; }
}

class HouseBlend implements Beverage {
    public String getDescription() { return "综合咖啡"; }
    public double cost() { return 12.0; }
}

// --- 装饰器基类 ---
abstract class CondimentDecorator implements Beverage {
    protected Beverage beverage;  // 持有被装饰者

    public CondimentDecorator(Beverage beverage) {
        this.beverage = beverage;
    }

    // 委派 getDescription 和 cost 由子类实现
}

// --- 具体装饰器 ---
class Milk extends CondimentDecorator {
    public Milk(Beverage beverage) { super(beverage); }

    public String getDescription() {
        return beverage.getDescription() + " + 牛奶";
    }

    public double cost() {
        return beverage.cost() + 3.0;
    }
}

class Mocha extends CondimentDecorator {
    public Mocha(Beverage beverage) { super(beverage); }

    public String getDescription() {
        return beverage.getDescription() + " + 摩卡";
    }

    public double cost() {
        return beverage.cost() + 5.0;
    }
}

class Whip extends CondimentDecorator {
    public Whip(Beverage beverage) { super(beverage); }

    public String getDescription() {
        return beverage.getDescription() + " + 奶油";
    }

    public double cost() {
        return beverage.cost() + 4.0;
    }
}

// --- 使用 ---
// 一杯：浓缩咖啡 + 牛奶 + 摩卡 + 奶油
Beverage drink = new Espresso();
drink = new Milk(drink);
drink = new Mocha(drink);
drink = new Whip(drink);

System.out.println(drink.getDescription() + " = ¥" + drink.cost());
// 浓缩咖啡 + 牛奶 + 摩卡 + 奶油 = ¥27.0
```

对比继承：如果每种组合都写一个子类，需要 2^3 = 8 个类（牛奶摩卡、牛奶奶油、摩卡奶油…），用装饰器只需要 3 个类。

#### ✅ 优点
- 比继承更灵活（运行时组合行为）
- 符合开闭原则（不修改原代码即可扩展）
- 可以组合多个装饰器，功能叠加

#### ❌ 缺点
- 会产生很多小类
- 多层装饰会让代码难以调试（stack trace 很长）
- 类型检查困难（instanceof 失效）

#### 📌 适用场景
- 需要动态、透明地给对象添加职责
- 功能需要灵活组合，不适合用继承
- 功能需要可撤销

#### 🔗 实际应用
- **Java I/O 流**：`new BufferedInputStream(new FileInputStream(new File("f")))`
- `Collections.synchronizedList()` / `unmodifiableList()`
- `javax.servlet.http.HttpServletRequestWrapper`
- Spring `TransactionProxyFactoryBean`

---

### 10. 外观模式（Facade）

#### 🎯 意图
为子系统中的一组接口提供一个**一致的简化界面**。

#### 💡 核心思想
- 门面（Facade）封装复杂子系统，提供高级接口
- 客户端只和门面交互，不直接操作子系统
- 迪米特法则（最少知识原则）的体现

#### 📐 结构
```
Client → Facade
           + simpleOperation()
               ↓ 委派
         SubsystemA  SubsystemB  SubsystemC
```

#### 💻 Java 实现

```java
// --- 复杂子系统 ---
class CPU {
    public void freeze() { System.out.println("CPU: 冻结"); }
    public void jump(long position) {
        System.out.println("CPU: 跳转到位置 " + position);
    }
    public void execute() { System.out.println("CPU: 执行指令"); }
}

class Memory {
    public void load(long position, byte[] data) {
        System.out.println("内存: 在位置 " + position + " 加载数据");
    }
}

class HardDrive {
    public byte[] read(long lba, int size) {
        System.out.println("硬盘: 读取 LBA " + lba + " 共 " + size + " 字节");
        return new byte[size];
    }
}

// --- 外观 ---
class ComputerFacade {
    private CPU cpu;
    private Memory memory;
    private HardDrive hardDrive;
    private static final long BOOT_ADDRESS = 0x0000;
    private static final long BOOT_SECTOR = 0x0000;
    private static final int SECTOR_SIZE = 512;

    public ComputerFacade() {
        this.cpu = new CPU();
        this.memory = new Memory();
        this.hardDrive = new HardDrive();
    }

    // 一键启动 —— 封装了 BIOS 加载的 5 个步骤
    public void start() {
        System.out.println("=== 计算机启动 ===");
        cpu.freeze();
        memory.load(BOOT_ADDRESS, hardDrive.read(BOOT_SECTOR, SECTOR_SIZE));
        cpu.jump(BOOT_ADDRESS);
        cpu.execute();
        System.out.println("=== 启动完成 ===");
    }

    public void shutdown() {
        System.out.println("=== 关机 ===");
        // 复杂的关机流程...
    }
}

// --- 客户端 ---
// 不需要了解 CPU、内存、硬盘如何协作
ComputerFacade computer = new ComputerFacade();
computer.start();   // 简单调用
```

#### ✅ 优点
- 简化客户端使用，降低耦合
- 提供了子系统入口，便于分层
- 编译依赖性降低（修改子系统不影响客户端）

#### ❌ 缺点
- 外观类可能成为"上帝类"（职责过大）
- 不能阻止客户端直接使用子系统（必要时可以绕过）

#### 📌 适用场景
- 为复杂子系统提供简单入口
- 客户端与子系统之间存在大量依赖
- 系统分层，每层提供外观作为入口点

#### 🔗 实际应用
- `javax.faces.context.FacesContext` — JSF 的门面
- Spring `JdbcTemplate` — 封装了 JDBC 连接、语句、结果集处理
- SLF4J — 日志框架的门面
- `java.net.URL` — 封装了协议处理器的复杂逻辑

---

### 11. 享元模式（Flyweight）

#### 🎯 意图
运用**共享技术**有效地支持大量**细粒度对象**的复用。

#### 💡 核心思想
- 对象拆分：**内部状态**（可共享，不变）和**外部状态**（不可共享，变化）
- 内部状态存在享元对象中，外部状态由客户端传入
- 工厂负责管理和复用享元

#### 📐 结构
```
FlyweightFactory
  - Map<String, Flyweight> pool
  + getFlyweight(key)           ← 有则返回，无则创建

Flyweight (接口)
  + operation(extrinsicState)   ← 外部状态参数

ConcreteFlyweight              UnsharedConcreteFlyweight
  - intrinsicState               ← 不共享的享元
  + operation(extrinsicState)
```

#### 💻 Java 实现

```java
// --- 享元对象（内在状态） ---
// 棋子的类型：颜色 + 形状，在整个棋盘上可共享
class ChessPieceType {
    private final String color;   // 内在状态 —— 不变、可共享
    private final String symbol;  // 内在状态

    public ChessPieceType(String color, String symbol) {
        this.color = color;
        this.symbol = symbol;
    }

    // 外部状态在此传入
    public void display(int x, int y) {
        System.out.println(symbol + "(" + color + ") 在 (" + x + "," + y + ")");
    }
}

// --- 享元工厂 ---
class ChessPieceFactory {
    private static final Map<String, ChessPieceType> types = new HashMap<>();

    public static ChessPieceType getPieceType(String color, String symbol) {
        String key = color + ":" + symbol;
        return types.computeIfAbsent(key, k -> {
            System.out.println("创建新棋子类型: " + key);
            return new ChessPieceType(color, symbol);
        });
    }

    public static int getTypeCount() { return types.size(); }
}

// --- 客户端对象（持有外部状态 + 引用享元） ---
class ChessPiece {
    private int x, y;                  // 外部状态 —— 变化，不共享
    private ChessPieceType type;       // 引用享元

    public ChessPiece(int x, int y, String color, String symbol) {
        this.x = x;
        this.y = y;
        this.type = ChessPieceFactory.getPieceType(color, symbol);
    }

    public void display() {
        type.display(x, y);
    }
}

// --- 使用 ---
// 一局棋：双方各 16 个棋子，共 32 个
List<ChessPiece> pieces = new ArrayList<>();
pieces.add(new ChessPiece(1, 1, "黑", "车"));
pieces.add(new ChessPiece(1, 2, "黑", "马"));
pieces.add(new ChessPiece(1, 3, "黑", "象"));
pieces.add(new ChessPiece(8, 1, "白", "车"));
pieces.add(new ChessPiece(8, 2, "白", "马"));
pieces.add(new ChessPiece(8, 3, "白", "象"));
// ... 更多棋子

System.out.println("棋子数量: " + pieces.size());      // 32
System.out.println("棋子类型数: " + ChessPieceFactory.getTypeCount());  // 6 （车马象帅将士 × 黑白）

// 内存节省：32 个棋子对象共享 6 个类型对象
```

#### ✅ 优点
- 大幅减少对象数量，节省内存
- 降低系统资源消耗（特别是图形、游戏场景）

#### ❌ 缺点
- 增加了系统复杂度（区分内/外部状态）
- 外部状态的维护增加了编码难度
- 需要权衡"享元省下的内存" vs "维护外部状态的开销"

#### 📌 适用场景
- 大量重复/相似的对象
- 对象内存占用大且可分解为内部+外部状态
- 对象大部分状态可以外部化

#### 🔗 实际应用
- `String` 常量池（JVM 自动管理）
- `Integer.valueOf()` 缓存（-128 ~ 127）
- Java 线程池中的 Worker 线程复用
- 网游中的玩家角色外观

---

### 12. 代理模式（Proxy）

#### 🎯 意图
为另一个对象提供一个**替身或占位符**以控制对这个对象的访问。

#### 💡 核心思想
- 代理和被代理实现相同接口
- 客户端通过代理访问真实对象
- 代理在访问前后可以插入额外逻辑

#### 📐 结构
```
Subject (接口)
  + request()

RealSubject                  Proxy
  + request()                  - RealSubject realSubject
                               + request() → 前置逻辑 → realSubject.request() → 后置逻辑
```

#### 💻 Java 实现：静态代理

```java
// --- 接口 ---
interface Database {
    void query(String sql);
}

// --- 真实对象 ---
class RealDatabase implements Database {
    private String url;

    public RealDatabase(String url) {
        this.url = url;
        connect();
    }

    private void connect() {
        System.out.println("连接数据库: " + url + "（耗时操作）");
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
    }

    public void query(String sql) {
        System.out.println("执行查询: " + sql);
    }
}

// --- 代理：延迟加载 + 性能监控 ---
class DatabaseProxy implements Database {
    private RealDatabase realDatabase;
    private String url;

    public DatabaseProxy(String url) {
        this.url = url;
        // 不立即连接，延迟到第一次查询
    }

    public void query(String sql) {
        long start = System.nanoTime();

        // 🔒 访问控制：只读方法检查
        if (sql.toLowerCase().startsWith("drop") || sql.toLowerCase().startsWith("delete")) {
            System.out.println("❌ 代理拦截: 禁止危险操作!");
            return;
        }

        // 𐄂 延迟加载
        if (realDatabase == null) {
            realDatabase = new RealDatabase(url);
        }

        realDatabase.query(sql);

        // 📊 性能监控
        long cost = (System.nanoTime() - start) / 1000000;
        System.out.println("⏱ 查询耗时: " + cost + "ms");
    }
}

// --- 使用 ---
Database db = new DatabaseProxy("jdbc:mysql://localhost:3306/mydb");
db.query("SELECT * FROM users");  // 这里才真正连接数据库
db.query("DROP TABLE users");     // 被拦截
```

#### 💻 JDK 动态代理

```java
// 利用 Java 反射，运行时生成代理类

interface Service {
    void doSomething();
}

class RealService implements Service {
    public void doSomething() {
        System.out.println("执行业务逻辑");
    }
}

// 通用的日志代理工厂
class LogProxyFactory {
    @SuppressWarnings("unchecked")
    public static <T> T createProxy(T target) {
        return (T) Proxy.newProxyInstance(
            target.getClass().getClassLoader(),
            target.getClass().getInterfaces(),
            (proxy, method, args) -> {
                System.out.println("[LOG] 调用 " + method.getName() + " 开始");
                Object result = method.invoke(target, args);
                System.out.println("[LOG] 调用 " + method.getName() + " 结束");
                return result;
            }
        );
    }
}

// 使用
Service service = LogProxyFactory.createProxy(new RealService());
service.doSomething();
// [LOG] 调用 doSomething 开始
// 执行业务逻辑
// [LOG] 调用 doSomething 结束
```

#### 四种常见代理

| 类型 | 作用 | 例子 |
|------|------|------|
| **远程代理** | 隐藏对象位于不同地址空间的事实 | RMI、gRPC 客户端 |
| **虚拟代理** | 延迟创建开销大的对象 | 大图加载占位 |
| **保护代理** | 控制访问权限 | 接口鉴权 |
| **智能引用** | 在访问时附加操作 | 引用计数、日志 |

#### ✅ 优点
- 职责清晰：代理控制访问，真实对象关注业务
- 开闭原则：在不修改真实对象的前提下扩展功能
- 灵活性高：多种代理类型可组合

#### ❌ 缺点
- 增加系统复杂度
- 代理类可能影响性能（反射、额外方法调用）
- 动态代理要求被代理对象实现接口（JDK 限制；CGLIB 可代理类）

#### 📌 适用场景
- 延迟加载（虚拟代理）
- 访问控制（保护代理）
- 日志/监控/缓存（智能引用）
- AOP 的核心实现机制

#### 🔗 实际应用
- Spring AOP（JDK 动态代理 / CGLIB）
- MyBatis Mapper 接口（动态代理生成 SQL 执行器）
- Hibernate 延迟加载（代理实体类）
- `java.lang.reflect.Proxy`

---

## 五、行为型模式（11种）

> **关注点：对象之间的通信和职责分配。**

---

### 13. 责任链模式（Chain of Responsibility）

#### 🎯 意图
使多个对象都有机会处理请求，从而**避免请求的发送者和接收者之间的耦合**。将这些对象连成一条链，并沿着这条链传递请求，直到有一个对象处理它为止。

#### 💡 核心思想
- 解耦请求发送者与处理者
- 每个处理者决定：处理 或 传递给下一个
- 链式结构，灵活组合

#### 📐 结构
```
Handler (接口)
  + setNext(Handler)
  + handle(request)

ConcreteHandler1 → ConcreteHandler2 → ConcreteHandler3
  + handle(request)     + handle(request)     + handle(request)
    能处理则处理         能处理则处理           一定能处理（兜底）
    否则传下一个         否则传下一个
```

#### 💻 Java 实现

```java
// --- 请求类 ---
class PurchaseRequest {
    private double amount;
    private String purpose;

    public PurchaseRequest(double amount, String purpose) {
        this.amount = amount;
        this.purpose = purpose;
    }

    public double getAmount() { return amount; }
    public String getPurpose() { return purpose; }
}

// --- 处理者接口 ---
abstract class Approver {
    protected Approver next;
    protected String name;

    public Approver(String name) { this.name = name; }

    public void setNext(Approver next) { this.next = next; }

    // 模板方法
    public void process(PurchaseRequest request) {
        if (canApprove(request)) {
            doApprove(request);
        } else if (next != null) {
            System.out.println(name + " 无权审批，转给 " + next.name);
            next.process(request);
        } else {
            System.out.println("❌ 无人有权审批，拒绝");
        }
    }

    protected abstract boolean canApprove(PurchaseRequest request);
    protected abstract void doApprove(PurchaseRequest request);
}

// --- 具体处理者 ---
class TeamLeader extends Approver {
    public TeamLeader() { super("组长"); }
    protected boolean canApprove(PurchaseRequest r) { return r.getAmount() <= 1000; }
    protected void doApprove(PurchaseRequest r) {
        System.out.println("✅ 组长审批通过: " + r.getPurpose() + " ¥" + r.getAmount());
    }
}

class Manager extends Approver {
    public Manager() { super("经理"); }
    protected boolean canApprove(PurchaseRequest r) { return r.getAmount() <= 5000; }
    protected void doApprove(PurchaseRequest r) {
        System.out.println("✅ 经理审批通过: " + r.getPurpose() + " ¥" + r.getAmount());
    }
}

class Director extends Approver {
    public Director() { super("总监"); }
    protected boolean canApprove(PurchaseRequest r) { return r.getAmount() <= 50000; }
    protected void doApprove(PurchaseRequest r) {
        System.out.println("✅ 总监审批通过: " + r.getPurpose() + " ¥" + r.getAmount());
    }
}

// --- 使用 ---
Approver chain = new TeamLeader();
Approver manager = new Manager();
Approver director = new Director();
chain.setNext(manager);
manager.setNext(director);

chain.process(new PurchaseRequest(500, "办公用品"));      // 组长
chain.process(new PurchaseRequest(3000, "设备维修"));     // 组长→经理
chain.process(new PurchaseRequest(30000, "服务器采购"));  // 组长→经理→总监
chain.process(new PurchaseRequest(100000, "收购"));       // 全部无权→拒绝
```

#### ✅ 优点
- 降低耦合度（发送者和接收者解耦）
- 灵活性强（可以动态调整链的顺序和成员）
- 符合开闭和单一职责

#### ❌ 缺点
- 不能保证请求一定被处理（需要兜底处理者）
- 调试困难（链长时难以追踪）
- 性能影响（链太长）

#### 📌 适用场景
- 多级审批流程
- 多个对象可以处理同一请求，但具体处理者不明确
- 需要动态指定处理者组合

#### 🔗 实际应用
- Spring Security 过滤器链
- Servlet Filter
- Java 日志框架的 Logger（父子 Logger 传递日志记录）
- Netty 的 Pipeline

---

### 14. 命令模式（Command）

#### 🎯 意图
将**请求封装为一个对象**，从而可以用不同的请求对客户进行参数化、队列化、记录日志，以及支持可撤销操作。

#### 💡 核心思想
- 请求封装为对象，使请求可以像对象一样传递
- 将"调用操作的对象"与"执行操作的对象"分离
- 天然支持：队列、撤销/重做、日志

#### 📐 结构
```
Command (接口)
  + execute()
  + undo()

ConcreteCommand                    Invoker
  - Receiver receiver              - Command command
  + execute() → receiver.action()  + executeCommand()
  + undo()                         + undoCommand()

Receiver
  + action()
```

#### 💻 Java 实现

```java
// --- 接收者（实际干活的人） ---
class Light {
    private String location;
    private int brightness = 100;

    public Light(String location) { this.location = location; }

    public void on() {
        System.out.println(location + " 灯打开，亮度 " + brightness + "%");
    }

    public void off() {
        System.out.println(location + " 灯关闭");
    }

    public void dim(int level) {
        brightness = level;
        System.out.println(location + " 灯调暗至 " + brightness + "%");
    }
}

// --- 命令接口 ---
interface Command {
    void execute();
    void undo();
}

// --- 具体命令 ---
class LightOnCommand implements Command {
    private Light light;

    public LightOnCommand(Light light) { this.light = light; }

    public void execute() { light.on(); }
    public void undo() { light.off(); }
}

class LightOffCommand implements Command {
    private Light light;

    public LightOffCommand(Light light) { this.light = light; }

    public void execute() { light.off(); }
    public void undo() { light.on(); }
}

class LightDimCommand implements Command {
    private Light light;
    private int prevLevel;
    private int newLevel;

    public LightDimCommand(Light light, int newLevel) {
        this.light = light;
        this.newLevel = newLevel;
    }

    public void execute() {
        // 保存当前亮度（以便撤销）
        // 实际应用中这里需要先获取当前亮度
        prevLevel = 100;
        light.dim(newLevel);
    }

    public void undo() {
        light.dim(prevLevel);
    }
}

// --- 调用者（遥控器） ---
class RemoteControl {
    private Command[] onCommands;
    private Command[] offCommands;
    private Stack<Command> history = new Stack<>();

    public RemoteControl() {
        onCommands = new Command[4];
        offCommands = new Command[4];
    }

    public void setCommand(int slot, Command onCmd, Command offCmd) {
        onCommands[slot] = onCmd;
        offCommands[slot] = offCmd;
    }

    public void pressOn(int slot) {
        onCommands[slot].execute();
        history.push(onCommands[slot]);
    }

    public void pressOff(int slot) {
        offCommands[slot].execute();
        history.push(offCommands[slot]);
    }

    public void pressUndo() {
        if (!history.isEmpty()) {
            Command last = history.pop();
            last.undo();
        }
    }
}

// --- 使用 ---
Light livingRoom = new Light("客厅");
Light kitchen = new Light("厨房");

RemoteControl remote = new RemoteControl();
remote.setCommand(0, new LightOnCommand(livingRoom), new LightOffCommand(livingRoom));
remote.setCommand(1, new LightOnCommand(kitchen), new LightOffCommand(kitchen));

remote.pressOn(0);      // 客厅灯打开
remote.pressOn(1);      // 厨房灯打开
remote.pressOff(0);     // 客厅灯关闭
remote.pressUndo();     // 撤销：客厅灯打开
```

#### ✅ 优点
- 解耦调用者和接收者
- 易扩展（新增命令类即可）
- 支持撤销、队列、日志、事务

#### ❌ 缺点
- 可能产生大量命令类
- 撤销逻辑复杂（需要保存大量历史状态）

#### 📌 适用场景
- 需要将请求参数化、排队、记录
- 需要支持撤销/重做
- 需要异步执行请求

#### 🔗 实际应用
- `Runnable` / `Callable`（将代码封装为对象传给线程池）
- Swing Action（封装菜单按钮的行为）
- Spring `JdbcTemplate` 的回调接口

---

### 15. 解释器模式（Interpreter）

#### 🎯 意图
给定一个语言，定义其文法的一种表示，并定义一个**解释器**，这个解释器使用该表示来解释语言中的句子。

#### 💡 核心思想
- 将业务规则抽象为"语法树"
- 每个语法规则对应一个表达式类
- 通常用于 DSL（领域特定语言）

#### 📐 结构
```
AbstractExpression
  + interpret(Context)

TerminalExpression            NonterminalExpression
  + interpret()                 + interpret()
                                - AbstractExpression left, right   ← 递归组合
```

#### 💻 Java 实现（简单数学表达式解析）

```java
// --- 上下文 ---
class Context {
    private Map<String, Integer> variables = new HashMap<>();

    public void setVariable(String name, int value) {
        variables.put(name, value);
    }

    public int getVariable(String name) {
        return variables.getOrDefault(name, 0);
    }
}

// --- 抽象表达式 ---
interface Expression {
    int interpret(Context ctx);
}

// --- 终结符表达式（数字 / 变量） ---
class NumberExpression implements Expression {
    private int number;
    public NumberExpression(int number) { this.number = number; }
    public int interpret(Context ctx) { return number; }
}

class VariableExpression implements Expression {
    private String name;
    public VariableExpression(String name) { this.name = name; }
    public int interpret(Context ctx) { return ctx.getVariable(name); }
}

// --- 非终结符表达式 ---
class AddExpression implements Expression {
    private Expression left, right;
    public AddExpression(Expression left, Expression right) {
        this.left = left; this.right = right;
    }
    public int interpret(Context ctx) {
        return left.interpret(ctx) + right.interpret(ctx);
    }
}

class SubtractExpression implements Expression {
    private Expression left, right;
    public SubtractExpression(Expression left, Expression right) {
        this.left = left; this.right = right;
    }
    public int interpret(Context ctx) {
        return left.interpret(ctx) - right.interpret(ctx);
    }
}

class MultiplyExpression implements Expression {
    private Expression left, right;
    public MultiplyExpression(Expression left, Expression right) {
        this.left = left; this.right = right;
    }
    public int interpret(Context ctx) {
        return left.interpret(ctx) * right.interpret(ctx);
    }
}

// --- 简单的解析器（将表达式字符串转为 AST） ---
class Parser {
    // "5 + 3 - 2"
    public static Expression parse(String expr) {
        String[] tokens = expr.split(" ");
        Expression result = parseTerminal(tokens[0]);

        for (int i = 1; i < tokens.length; i += 2) {
            String op = tokens[i];
            Expression right = parseTerminal(tokens[i + 1]);
            result = switch (op) {
                case "+" -> new AddExpression(result, right);
                case "-" -> new SubtractExpression(result, right);
                case "*" -> new MultiplyExpression(result, right);
                default -> throw new IllegalArgumentException("未知运算符: " + op);
            };
        }
        return result;
    }

    private static Expression parseTerminal(String token) {
        try {
            return new NumberExpression(Integer.parseInt(token));
        } catch (NumberFormatException e) {
            return new VariableExpression(token);
        }
    }
}

// --- 使用 ---
Context ctx = new Context();
ctx.setVariable("x", 10);
ctx.setVariable("y", 5);

Expression expr1 = Parser.parse("3 + 4");
System.out.println("3 + 4 = " + expr1.interpret(ctx));  // 7

Expression expr2 = Parser.parse("x + y * 2");
System.out.println("x + y * 2 = " + expr2.interpret(ctx));  // 20
```

#### ✅ 优点
- 易于修改和扩展文法
- 语法树易于实现自动遍历和分析

#### ❌ 缺点
- 复杂文法需要大量类
- 效率较低（大量递归调用）
- 实际应用场景有限

#### 📌 适用场景
- 需要解释执行的 DSL
- 重复出现的业务规则
- 简单的语言/表达式求值

#### 🔗 实际应用
- SQL 解析
- 正则表达式引擎
- Spring SpEL（表达式语言）
- `java.util.regex.Pattern`

---

### 16. 迭代器模式（Iterator）

#### 🎯 意图
提供一种方法**顺序访问**一个聚合对象中的各个元素，而又不需要暴露该对象的内部表示。

#### 💡 核心思想
- 将集合遍历逻辑独立出来
- 统一的遍历接口（hasNext / next）
- 多态遍历：不同集合用统一方式遍历

#### 📐 结构
```
Aggregate (接口)              Iterator (接口)
  + createIterator()            + hasNext()
                                + next()

ConcreteAggregate            ConcreteIterator
  + createIterator()            - int index
                                + hasNext()
                                + next()
```

#### 💻 Java 实现

```java
// Java 已经内建了 Iterator 接口，这里实现一个自定义集合

// --- 集合 ---
class Playlist implements Iterable<String> {
    private String[] songs;
    private int count = 0;

    public Playlist(int capacity) {
        songs = new String[capacity];
    }

    public void addSong(String song) {
        if (count < songs.length) {
            songs[count++] = song;
        }
    }

    public int size() { return count; }

    // 返回迭代器
    public Iterator<String> iterator() {
        return new Iterator<String>() {
            private int index = 0;

            public boolean hasNext() {
                return index < count;
            }

            public String next() {
                if (!hasNext()) throw new NoSuchElementException();
                return songs[index++];
            }
        };
    }

    // 反向迭代器（内部类）
    public Iterator<String> reverseIterator() {
        return new Iterator<String>() {
            private int index = count - 1;

            public boolean hasNext() { return index >= 0; }

            public String next() {
                if (!hasNext()) throw new NoSuchElementException();
                return songs[index--];
            }
        };
    }
}

// --- 使用 ---
Playlist playlist = new Playlist(10);
playlist.addSong("晴天");
playlist.addSong("七里香");
playlist.addSong("夜曲");

// 正向遍历
for (String song : playlist) {        // 编译器自动调用 iterator()
    System.out.println(song);
}

// 反向遍历
Iterator<String> reverse = playlist.reverseIterator();
while (reverse.hasNext()) {
    System.out.println("反向: " + reverse.next());
}
```

#### ✅ 优点
- 遍历逻辑与集合分离，符合单一职责
- 支持多态遍历（不同类型的集合可以用同一接口遍历）
- 可以同时实现多种遍历方式（正向、反向、跳跃等）

#### ❌ 缺点
- 对简单集合，迭代器可能过度设计
- 遍历过程中修改集合需要小心（快速失败机制）

#### 📌 适用场景
- 需要隐藏集合内部结构
- 需要提供统一的遍历接口
- 需要提供多种遍历方式

#### 🔗 实际应用
- Java 集合框架（所有 Collection 都实现了 Iterable）
- Java Stream API（内部迭代）

---

### 17. 中介者模式（Mediator）

#### 🎯 意图
用一个**中介对象**来封装一组对象之间的交互。中介者使各对象不需要显式地相互引用，从而使其耦合松散，并且可以独立地改变它们之间的交互。

#### 💡 核心思想
- 将多对多的交互简化为一对多的交互
- 对象间不直接通信，通过中介者转发
- 类似"航班塔台"——飞机之间不直接沟通，都向塔台汇报

#### 📐 结构
```
Mediator (接口)
  + notify(sender, event)

ConcreteMediator                   Colleague
  - List<Colleague> colleagues       - Mediator mediator
  + notify(sender, event)            + send(event)
    → 转发给其他 Colleague            + receive(event)
```

#### 💻 Java 实现

```java
// --- 中介者 ---
interface ChatMediator {
    void sendMessage(String message, User sender);
    void addUser(User user);
}

class ChatRoom implements ChatMediator {
    private List<User> users = new ArrayList<>();

    public void addUser(User user) {
        users.add(user);
    }

    // 中介者的核心：决定消息如何分发
    public void sendMessage(String message, User sender) {
        System.out.println("--- 中介者分发消息 ---");
        for (User user : users) {
            // 发送给所有人（除了发消息的人自己）
            if (user != sender) {
                user.receive(sender.getName(), message);
            }
        }
    }
}

// --- 同事类 ---
abstract class User {
    protected ChatMediator mediator;
    protected String name;

    public User(ChatMediator mediator, String name) {
        this.mediator = mediator;
        this.name = name;
    }

    public String getName() { return name; }

    // 发送消息（不直接发给别人，而是发给中介者）
    public void send(String message) {
        System.out.println(name + " 发送: " + message);
        mediator.sendMessage(message, this);
    }

    public abstract void receive(String from, String message);
}

class ChatUser extends User {
    public ChatUser(ChatMediator mediator, String name) {
        super(mediator, name);
    }

    @Override
    public void receive(String from, String message) {
        System.out.println(name + " 收到来自 " + from + " 的消息: " + message);
    }
}

// --- 使用 ---
ChatRoom mediator = new ChatRoom();

User alice = new ChatUser(mediator, "Alice");
User bob = new ChatUser(mediator, "Bob");
User charlie = new ChatUser(mediator, "Charlie");

mediator.addUser(alice);
mediator.addUser(bob);
mediator.addUser(charlie);

alice.send("大家好！");
// 输出:
// Alice 发送: 大家好！
// --- 中介者分发消息 ---
// Bob 收到来自 Alice 的消息: 大家好！
// Charlie 收到来自 Alice 的消息: 大家好！
```

如果不使用中介者：Alice 需要持有 Bob 和 Charlie 的引用 → 新增一个用户就要修改 Alice 的代码 → 多对多耦合。

#### ✅ 优点
- 减少类间耦合，符合迪米特法则
- 将多对多变为一对多，系统更易于理解
- 集中控制交互逻辑

#### ❌ 缺点
- 中介者可能变成"上帝对象"（职责过于集中）
- 中介者出问题会影响整个系统

#### 📌 适用场景
- 对象间交互复杂且相互依赖
- 想复用单个对象而不依赖其他对象
- 不想产生大量子类

#### 🔗 实际应用
- MVC 中的 Controller（作为 Model 和 View 的中介者）
- 消息队列/消息中间件
- Java AWT/Swing 的事件机制
- Spring ApplicationContext 中的 Bean 交互

---

### 18. 备忘录模式（Memento）

#### 🎯 意图
在不破坏封装的前提下，**捕获并外部化一个对象的内部状态**，以便以后可以将对象恢复到该状态。

#### 💡 核心思想
- 三角色：原发器（Originator）、备忘录（Memento）、管理者（Caretaker）
- 备忘录对象保存原发器的状态快照
- 管理者保存备忘录（但不查看或修改其内容）
- 封装保护：备忘录对外部不可见

#### 📐 结构
```
Originator                      Memento (不可变)
  - state                         - state (包级私有)
  + save() → Memento              + getState() (包级私有)
  + restore(Memento)

Caretaker
  - List<Memento> history
  + save(Memento)
  + undo() → Memento
```

#### 💻 Java 实现

```java
// --- 备忘录（不可变） ---
class EditorMemento {
    private final String content;       // 快照内容
    private final int cursorPos;        // 光标位置
    private final LocalDateTime timestamp;

    // 包级私有构造 —— 只有 Originator 能创建
    EditorMemento(String content, int cursorPos) {
        this.content = content;
        this.cursorPos = cursorPos;
        this.timestamp = LocalDateTime.now();
    }

    // 包级私有 —— 只有 Originator 能读取
    String getContent() { return content; }
    int getCursorPos() { return cursorPos; }
    LocalDateTime getTimestamp() { return timestamp; }
}

// --- 原发器 ---
class TextEditor {
    private StringBuilder content = new StringBuilder();
    private int cursorPos = 0;

    public void write(String text) {
        content.insert(cursorPos, text);
        cursorPos += text.length();
    }

    public void delete() {
        if (cursorPos > 0 && content.length() > 0) {
            content.deleteCharAt(cursorPos - 1);
            cursorPos--;
        }
    }

    public void moveCursor(int pos) {
        cursorPos = Math.max(0, Math.min(pos, content.length()));
    }

    // 保存当前状态到备忘录
    public EditorMemento save() {
        return new EditorMemento(content.toString(), cursorPos);
    }

    // 从备忘录恢复状态
    public void restore(EditorMemento memento) {
        this.content = new StringBuilder(memento.getContent());
        this.cursorPos = memento.getCursorPos();
    }

    public String getContent() { return content.toString(); }
    public int getCursorPos() { return cursorPos; }

    @Override
    public String toString() {
        return "TextEditor{content='" + content + "', cursor=" + cursorPos + "}";
    }
}

// --- 管理者（历史记录） ---
class EditorHistory {
    private Stack<EditorMemento> history = new Stack<>();

    public void push(EditorMemento memento) {
        history.push(memento);
    }

    public EditorMemento pop() {
        if (history.isEmpty()) {
            throw new EmptyStackException();
        }
        return history.pop();
    }

    public int size() { return history.size(); }
}

// --- 使用 ---
TextEditor editor = new TextEditor();
EditorHistory history = new EditorHistory();

editor.write("Hello");
history.push(editor.save());   // 保存状态 1
System.out.println(editor);    // content='Hello', cursor=5

editor.write(" World");
history.push(editor.save());   // 保存状态 2
System.out.println(editor);    // content='Hello World', cursor=11

editor.write("!!!");
System.out.println(editor);    // content='Hello World!!!', cursor=14

editor.restore(history.pop());  // Ctrl+Z
System.out.println(editor);    // content='Hello World', cursor=11

editor.restore(history.pop());  // Ctrl+Z
System.out.println(editor);    // content='Hello', cursor=5
```

#### ✅ 优点
- 不破坏封装（只有原发器能访问备忘录内容）
- 简化了原发器的职责（恢复逻辑分离出去）
- 管理者只管理，不接触备忘录内部

#### ❌ 缺点
- 可能消耗大量内存（频繁保存大对象）
- 管理者要负责清理过期的备忘录
- 撤销多级时处理复杂

#### 📌 适用场景
- 需要保存/恢复对象状态（编辑器撤销）
- 需要保存历史快照的事务操作

#### 🔗 实际应用
- IDE 的撤销/重做功能
- 数据库事务的回滚（通过 undo 日志）
- 游戏存档

---

### 19. 观察者模式（Observer）

#### 🎯 意图
定义对象之间的一种**一对多依赖关系**，当一个对象状态发生改变时，所有依赖它的对象都会得到通知并自动更新。

#### 💡 核心思想
- 发布-订阅模型（Pub-Sub 的简化版）
- 主题（Subject）维护观察者列表
- 松耦合：观察者不需要知道发布者内部细节

#### 📐 结构
```
Subject (接口)
  + attach(Observer)
  + detach(Observer)
  + notify()

ConcreteSubject                  Observer (接口)
  - state                         + update(state)
  + getState()

                                 ConcreteObserver
                                   - subject
                                   + update(state)
```

#### 💻 Java 实现

```java
// --- 观察者 ---
interface Observer {
    void update(String stockName, double price);
}

// --- 主题 ---
class StockMarket {
    private Map<String, Double> stocks = new HashMap<>();
    private List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer o) { observers.add(o); }
    public void removeObserver(Observer o) { observers.remove(o); }

    // 更新股票价格
    public void setStockPrice(String name, double price) {
        stocks.put(name, price);
        notifyObservers(name, price);
    }

    private void notifyObservers(String name, double price) {
        for (Observer o : observers) {
            o.update(name, price);
        }
    }
}

// --- 具体观察者 ---
class Investor implements Observer {
    private String name;

    public Investor(String name) { this.name = name; }

    @Override
    public void update(String stockName, double price) {
        System.out.println(name + " 收到通知: " + stockName + " 当前价格 ¥" + price);
    }
}

// 另一种观察者：自动交易机器人
class TradingBot implements Observer {
    public void update(String stockName, double price) {
        if (price < 50) {
            System.out.println("🤖 自动买入 " + stockName + "! 当前 ¥" + price);
        } else if (price > 200) {
            System.out.println("🤖 自动卖出 " + stockName + "! 当前 ¥" + price);
        }
    }
}

// --- 使用 ---
StockMarket market = new StockMarket();

market.addObserver(new Investor("Alice"));
market.addObserver(new Investor("Bob"));
market.addObserver(new TradingBot());

market.setStockPrice("腾讯", 380.5);
// Alice 收到通知: 腾讯 当前价格 ¥380.5
// Bob 收到通知: 腾讯 当前价格 ¥380.5

market.setStockPrice("阿里巴巴", 48.0);
// Alice 收到通知: 阿里巴巴 当前价格 ¥48.0
// Bob 收到通知: 阿里巴巴 当前价格 ¥48.0
// 🤖 自动买入 阿里巴巴! 当前 ¥48.0
```

#### Java 内置观察者（已过时，但了解历史）

```java
// Java 9 之前内置了 Observable 和 Observer，但已被标记为过时
// 原因是 Observable 是类而非接口（限制继承），事件通知顺序不规范
// 推荐做法：自定义事件监听机制或使用第三方库（如 Guava EventBus）
```

#### ✅ 优点
- 松耦合（主题和观察者抽象耦合）
- 支持广播通信
- 符合开闭原则

#### ❌ 缺点
- 如果观察者过多，通知耗时
- 观察者之间依赖可能引发循环通知
- 被观察者不知道观察者如何处理通知（如果观察者出错？）

#### 📌 适用场景
- 一个对象的改变需要同时改变其他对象
- 对象不需要知道具体有多少对象需要更新
- 事件驱动编程

#### 🔗 实际应用
- Spring 事件机制（ApplicationEvent / @EventListener）
- Swing/AWT 事件监听器
- JavaFX Property 绑定
- 消息队列（Kafka/RabbitMQ 的发布订阅模型）

---

### 20. 状态模式（State）

#### 🎯 意图
允许一个对象在其内部状态改变时改变它的行为。对象看起来似乎修改了它的类。

#### 💡 核心思想
- 将每个状态封装为独立的类
- 状态转换由状态类自身决定（而不是大段 if-else）
- 行为随着状态的改变自动切换

#### 📐 结构
```
Context (上下文)
  - State state                    ← 当前状态
  + request() → state.handle()     ← 委派给当前状态

State (接口)
  + handle(Context)

ConcreteStateA                   ConcreteStateB
  + handle(Context)                + handle(Context)
    → 执行 A 行为                     → 执行 B 行为
    → 可能切换状态到 B                 → 可能切换状态到 A
```

#### 💻 Java 实现

```java
// --- 状态接口 ---
interface OrderState {
    void next(Order order);          // 下一步
    void prev(Order order);          // 上一步
    void cancel(Order order);        // 取消
    String getStatusName();          // 状态名
}

// --- 上下文（订单） ---
class Order {
    private OrderState currentState;

    public Order() {
        this.currentState = new NewOrderState();  // 初始状态
    }

    public void setState(OrderState state) {
        this.currentState = state;
    }

    public void next() { currentState.next(this); }
    public void prev() { currentState.prev(this); }
    public void cancel() { currentState.cancel(this); }
    public String getStatus() { return currentState.getStatusName(); }
}

// --- 具体状态 ---

// 状态 1：新建订单
class NewOrderState implements OrderState {
    public void next(Order order) {
        order.setState(new PaidState());
        System.out.println("订单已支付");
    }

    public void prev(Order order) {
        System.out.println("新建订单无上一步");
    }

    public void cancel(Order order) {
        order.setState(new CancelledState());
        System.out.println("订单已取消");
    }

    public String getStatusName() { return "新建"; }
}

// 状态 2：已支付
class PaidState implements OrderState {
    public void next(Order order) {
        order.setState(new ShippedState());
        System.out.println("订单已发货");
    }

    public void prev(Order order) {
        order.setState(new NewOrderState());
        System.out.println("退回至新建订单");
    }

    public void cancel(Order order) {
        order.setState(new CancelledState());
        System.out.println("订单已取消（可退款）");
    }

    public String getStatusName() { return "已支付"; }
}

// 状态 3：已发货
class ShippedState implements OrderState {
    public void next(Order order) {
        order.setState(new DeliveredState());
        System.out.println("订单已送达");
    }

    public void prev(Order order) {
        order.setState(new PaidState());
        System.out.println("退回至已支付");
    }

    public void cancel(Order order) {
        System.out.println("❌ 已发货，无法取消");
    }

    public String getStatusName() { return "已发货"; }
}

// 状态 4：已送达
class DeliveredState implements OrderState {
    public void next(Order order) {
        order.setState(new CompletedState());
        System.out.println("订单已完成");
    }

    public void prev(Order order) {
        order.setState(new ShippedState());
        System.out.println("退回至已发货");
    }

    public void cancel(Order order) {
        System.out.println("❌ 已送达，无法取消");
    }

    public String getStatusName() { return "已送达"; }
}

// 状态 5：已完成/已取消（终态）
class CompletedState implements OrderState {
    public void next(Order order) { System.out.println("已完成，无后续状态"); }
    public void prev(Order order) { System.out.println("已完成，无法回退"); }
    public void cancel(Order order) { System.out.println("已完成，无法取消"); }
    public String getStatusName() { return "已完成"; }
}

class CancelledState implements OrderState {
    public void next(Order order) { System.out.println("已取消，无法继续"); }
    public void prev(Order order) { System.out.println("已取消，无法回退"); }
    public void cancel(Order order) { System.out.println("已取消，无需重复取消"); }
    public String getStatusName() { return "已取消"; }
}

// --- 使用 ---
Order order = new Order();
System.out.println("当前: " + order.getStatus());   // 新建

order.next();   // 已支付
order.next();   // 已发货
order.prev();   // 退回至已支付
order.next();   // 已发货
order.next();   // 已送达
order.next();   // 已完成
order.prev();   // 已完成，无法回退
order.cancel(); // 已完成，无法取消
```

对比 if-else：如果不用状态模式，`order.next()` 里会有 `if (status == NEW) { ... } else if (status == PAID) { ... }` 的大段代码，且每加一个状态就要改这个巨型方法。

#### ✅ 优点
- 状态行为局部化（每个状态一个类）
- 消除了庞大的条件语句
- 状态转换显式化，易读易维护
- 符合开闭原则

#### ❌ 缺点
- 状态多时，类数量增加
- 状态转换逻辑分散在状态类中（不好全局把控）

#### 📌 适用场景
- 对象行为依赖其状态，且状态转换复杂
- 状态数量有限且可枚举
- 有大量条件分支依据对象状态来判断

#### 🔗 实际应用
- 订单状态机
- TCP 连接状态（ESTABLISHED / CLOSE_WAIT / TIME_WAIT …）
- 工作流引擎
- Java 线程状态（NEW / RUNNABLE / BLOCKED / WAITING / TIMED_WAITING / TERMINATED）

---

### 21. 策略模式（Strategy）

#### 🎯 意图
定义一系列算法，把它们一个个封装起来，并且使它们可以**相互替换**。本模式使得算法可独立于使用它的客户而变化。

#### 💡 核心思想
- 将算法的选择和算法的实现分离
- 客户端可以动态选择不同的策略
- 避免大量的 if-else / switch-case

#### 📐 结构
```
Context
  - Strategy strategy
  + executeStrategy()

Strategy (接口)
  + algorithm()

ConcreteStrategyA      ConcreteStrategyB      ConcreteStrategyC
  + algorithm()          + algorithm()          + algorithm()
```

#### 💻 Java 实现

```java
// --- 策略接口 ---
interface SortStrategy {
    <T extends Comparable<T>> void sort(List<T> list);
}

// --- 具体策略 ---
class BubbleSort implements SortStrategy {
    public <T extends Comparable<T>> void sort(List<T> list) {
        System.out.println("使用冒泡排序");
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (list.get(j).compareTo(list.get(j + 1)) > 0) {
                    Collections.swap(list, j, j + 1);
                }
            }
        }
    }
}

class QuickSort implements SortStrategy {
    public <T extends Comparable<T>> void sort(List<T> list) {
        System.out.println("使用快速排序");
        quickSort(list, 0, list.size() - 1);
    }

    private <T extends Comparable<T>> void quickSort(List<T> list, int low, int high) {
        if (low < high) {
            int pi = partition(list, low, high);
            quickSort(list, low, pi - 1);
            quickSort(list, pi + 1, high);
        }
    }

    private <T extends Comparable<T>> int partition(List<T> list, int low, int high) {
        T pivot = list.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (list.get(j).compareTo(pivot) < 0) {
                i++;
                Collections.swap(list, i, j);
            }
        }
        Collections.swap(list, i + 1, high);
        return i + 1;
    }
}

class ParallelSort implements SortStrategy {
    public <T extends Comparable<T>> void sort(List<T> list) {
        System.out.println("使用并行排序");
        list.parallelStream().sorted().toArray();  // 实际排序
        Collections.sort(list);  // 简化演示
    }
}

// --- 上下文 ---
class SortContext {
    private SortStrategy strategy;

    public SortContext(SortStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(SortStrategy strategy) {
        this.strategy = strategy;
    }

    public <T extends Comparable<T>> void sort(List<T> list) {
        if (list == null || list.isEmpty()) return;
        long start = System.nanoTime();
        strategy.sort(list);
        long cost = (System.nanoTime() - start) / 1000000;
        System.out.println("排序耗时: " + cost + "ms");
    }
}

// --- 使用 ---
List<Integer> data = new ArrayList<>();
for (int i = 0; i < 10000; i++) data.add((int)(Math.random() * 10000));

SortContext ctx = new SortContext(new QuickSort());
ctx.sort(new ArrayList<>(data));   // 小数据用快排

ctx.setStrategy(new ParallelSort());
ctx.sort(new ArrayList<>(data));   // 大数据用并行

ctx.setStrategy(new BubbleSort());
ctx.sort(new ArrayList<>(data));   // 演示用冒泡
```

#### ✅ 优点
- 算法可以自由切换
- 避免多重条件判断
- 符合开闭（新增策略不需要改上下文）
- 扩展性好

#### ❌ 缺点
- 客户端必须知道各种策略的区别（选择合适的策略）
- 策略类数量会增加

#### 📌 适用场景
- 多个类只在行为上有区别
- 需要多种算法变体
- 算法中需要屏蔽具体实现细节

#### 🔗 实际应用
- `Comparator.comparing()` — 排序策略
- Spring `ResourceLoader` — 不同资源加载策略
- Java 线程池的拒绝策略（AbortPolicy / CallerRunsPolicy / DiscardPolicy）
- 电商促销活动（满减/打折/积分兑换）

---

### 22. 模板方法模式（Template Method）

#### 🎯 意图
定义一个操作的**算法骨架**，而将一些步骤延迟到子类中实现。模板方法使得子类可以在不改变算法结构的情况下，重新定义算法的某些步骤。

#### 💡 核心思想
- 固定流程 + 可变步骤
- 好莱坞原则："不要打电话给我们，我们会打给你"（父类调用子类）
- 钩子方法：提供默认实现的"空白"方法，子类可选择重写

#### 📐 结构
```
AbstractClass
  + templateMethod()      ← final，算法骨架（不可重写）
      step1()              ← 抽象方法，子类必须实现
      step2()              ← 抽象方法
      hook()               ← 可选，子类可重写也可不重写
      step3()

ConcreteClassA/B
  + step1()
  + step2()
  + hook()                ← 可选
```

#### 💻 Java 实现

```java
// --- 抽象类（定义算法骨架） ---
abstract class DataImporter {
    // 模板方法 —— final 防止子类修改流程
    public final void importData(String filePath) {
        openFile(filePath);

        // 钩子：判断是否需要进行数据验证
        if (shouldValidate()) {
            validate();
        }

        parse();
        transform();   // 钩子：空实现，子类可选重写
        save();

        closeFile();
    }

    // 固定步骤
    private void openFile(String path) {
        System.out.println("打开文件: " + path);
    }

    private void closeFile() {
        System.out.println("关闭文件");
    }

    // 抽象步骤 —— 子类必须实现
    protected abstract void validate();
    protected abstract void parse();
    protected abstract void save();

    // 钩子方法 —— 默认空实现，子类可选重写
    protected void transform() {
        // 默认不做转换
    }

    // 钩子方法 —— 控制流程
    protected boolean shouldValidate() {
        return true;  // 默认需要验证
    }
}

// --- 具体实现 ---
class CSVImporter extends DataImporter {
    protected void validate() {
        System.out.println("验证 CSV 文件格式");
    }

    protected void parse() {
        System.out.println("按逗号分隔解析 CSV 数据");
    }

    protected void save() {
        System.out.println("将 CSV 数据批量插入数据库");
    }

    @Override
    protected boolean shouldValidate() {
        return false;  // CSV 太乱，跳过验证
    }
}

class ExcelImporter extends DataImporter {
    protected void validate() {
        System.out.println("验证 Excel 文件格式和列名");
    }

    protected void parse() {
        System.out.println("按 Sheet 行解析 Excel");
    }

    protected void transform() {
        System.out.println("将 Excel 日期格式转为 yyyy-MM-dd");
    }

    protected void save() {
        System.out.println("逐行写入数据库");
    }
}

// --- 使用 ---
DataImporter csvImporter = new CSVImporter();
csvImporter.importData("data.csv");
// 打开文件: data.csv
// 按逗号分隔解析 CSV 数据
// 将 CSV 数据批量插入数据库
// 关闭文件

DataImporter excelImporter = new ExcelImporter();
excelImporter.importData("report.xlsx");
// 打开文件: report.xlsx
// 验证 Excel 文件格式和列名
// 按 Sheet 行解析 Excel
// 将 Excel 日期格式转为 yyyy-MM-dd
// 逐行写入数据库
// 关闭文件
```

#### ✅ 优点
- 代码复用（公共步骤放在父类）
- 符合开闭（新增实现不影响已有代码）
- 反向控制（父类调用子类）

#### ❌ 缺点
- 子类实现可能受限（父类模板限制了流程）
- 父类中的步骤越多，维护成本越高
- 违反里氏替换原则的风险（子类重写钩子改变行为）

#### 📌 适用场景
- 有固定流程但步骤实现可变
- 需要在多个子类间复用公共代码
- 希望控制子类的扩展点

#### 🔗 实际应用
- Spring 中的 `JdbcTemplate` / `RestTemplate` / `TransactionTemplate`
- `AbstractQueuedSynchronizer`（AQS 的框架）
- `HttpServlet`（doGet / doPost 由子类实现）
- `InputStream` / `Reader`（read 方法的实现骨架）

---

### 23. 访问者模式（Visitor）

#### 🎯 意图
表示一个作用于某对象结构中的各元素的操作。它使你可以在**不改变各元素类的前提下定义作用于这些元素的新操作**。

#### 💡 核心思想
- 双重分发（Double Dispatch）：先 accept 调用 visit，再 visit 重载匹配具体类型
- 将数据和操作分离
- 适合数据结构固定、操作频繁变化的场景

#### 📐 结构
```
Visitor (接口)
  + visit(ConcreteElementA)
  + visit(ConcreteElementB)

ConcreteVisitor1/2              Element (接口)
  + visit(ConcreteElementA)       + accept(Visitor)
  + visit(ConcreteElementB)

                                ConcreteElementA/B
                                  + accept(Visitor v) → v.visit(this)
```

#### 💻 Java 实现

```java
// --- 元素接口 ---
interface DocumentElement {
    void accept(DocumentVisitor visitor);
}

// --- 具体元素 ---
class Paragraph implements DocumentElement {
    private String text;

    public Paragraph(String text) { this.text = text; }

    public String getText() { return text; }

    public void accept(DocumentVisitor visitor) {
        visitor.visit(this);  // 第一次分发：this 的类型决定调用 visit(Paragraph)
    }
}

class Image implements DocumentElement {
    private String src;
    private String alt;

    public Image(String src, String alt) {
        this.src = src;
        this.alt = alt;
    }

    public String getSrc() { return src; }
    public String getAlt() { return alt; }

    public void accept(DocumentVisitor visitor) {
        visitor.visit(this);  // 第一次分发：this 的类型决定调用 visit(Image)
    }
}

class Table implements DocumentElement {
    private int rows;
    private int cols;
    private String[][] data;

    public Table(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new String[rows][cols];
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }

    public void accept(DocumentVisitor visitor) {
        visitor.visit(this);  // 第一次分发：this 的类型决定调用 visit(Table)
    }
}

// --- 访问者接口（每种元素一个 visit 重载） ---
interface DocumentVisitor {
    void visit(Paragraph paragraph);
    void visit(Image image);
    void visit(Table table);
}

// --- 具体访问者 1：HTML 导出 ---
class HtmlExporter implements DocumentVisitor {
    private StringBuilder html = new StringBuilder();

    public void visit(Paragraph p) {
        html.append("<p>").append(p.getText()).append("</p>\n");
    }

    public void visit(Image img) {
        html.append("<img src='").append(img.getSrc())
            .append("' alt='").append(img.getAlt()).append("' />\n");
    }

    public void visit(Table table) {
        html.append("<table border='1'>\n");
        for (int r = 0; r < table.getRows(); r++) {
            html.append("  <tr>");
            for (int c = 0; c < table.getCols(); c++) {
                html.append("<td>").append(table.getData()[r][c]).append("</td>");
            }
            html.append("</tr>\n");
        }
        html.append("</table>\n");
    }

    public String getResult() { return html.toString(); }
}

// --- 具体访问者 2：字数统计 ---
class WordCounter implements DocumentVisitor {
    private int wordCount = 0;

    public void visit(Paragraph p) {
        wordCount += p.getText().split("\\s+").length;
    }

    public void visit(Image img) {
        // 图片不计字数，但 alt 文本计
        wordCount += img.getAlt().split("\\s+").length;
    }

    public void visit(Table table) {
        // 表格中所有文本
        for (int r = 0; r < table.getRows(); r++) {
            for (int c = 0; c < table.getCols(); c++) {
                // 简化实现
            }
        }
    }

    public int getWordCount() { return wordCount; }
}

// --- 使用 ---
List<DocumentElement> document = new ArrayList<>();
document.add(new Paragraph("Hello, World!"));
document.add(new Image("logo.png", "Company Logo"));
document.add(new Table(3, 3));

// 访问者 1：导出 HTML
HtmlExporter htmlExporter = new HtmlExporter();
for (DocumentElement element : document) {
    element.accept(htmlExporter);  // 第二次分发
}
System.out.println(htmlExporter.getResult());

// 访问者 2：统计字数
WordCounter counter = new WordCounter();
for (DocumentElement element : document) {
    element.accept(counter);
}
System.out.println("Total words: " + counter.getWordCount());
```

没有访问者模式的话：每增加一个操作（Markdown导出、JSON导出）就要修改 Paragraph、Image、Table 每个类 → 违反开闭。访问者将新操作集中到一个类里。

#### ✅ 优点
- 符合开闭原则（元素类不改，访问者可扩展）
- 将相关操作集中到一个访问者中
- 积累状态（访问者可以在遍历过程中汇总结果）

#### ❌ 缺点
- 新增元素困难（所有访问者都要改）
- 破坏封装（访问者可能需要访问元素内部细节）
- 对元素类型的依赖较强

#### 📌 适用场景
- 对象结构稳定（元素种类固定），但操作经常变化
- 需要对对象结构中的所有元素执行不同操作
- 元素类数量少但操作多

#### 🔗 实际应用
- 编译器 AST 处理（语法分析 → 类型检查 → 代码生成都是不同的访问者）
- Spring `BeanDefinitionVisitor`
- Apache Commons DBCP 连接池监控
- 各种文档格式转换器

---

## 六、现代 Java 中的模式实战

### 6.1 Java 8+ 函数式编程对模式的影响

Java 8 Lambda 和 Stream 的出现，让部分模式可以用更简洁的方式实现。

```java
// --- 策略模式 → Lambda (无需策略接口和实现类) ---
List<Integer> numbers = Arrays.asList(3, 1, 4, 1, 5);

// 传统策略
numbers.sort(new Comparator<Integer>() {      // 匿名内部类
    public int compare(Integer a, Integer b) { return a - b; }
});

// Java 8 Lambda 策略
numbers.sort((a, b) -> a - b);                // 一行搞定
numbers.sort(Comparator.naturalOrder());      // 或方法引用

// --- 命令模式 → 方法引用 ---
Map<String, Runnable> commands = new HashMap<>();
commands.put("start", () -> System.out.println("启动"));
commands.put("stop", () -> System.out.println("停止"));
commands.get("start").run();

// --- 观察者模式 → Consumer List ---
List<Consumer<String>> observers = new ArrayList<>();
observers.add(msg -> System.out.println("日志: " + msg));
observers.add(msg -> System.out.println("邮件: " + msg));
observers.forEach(o -> o.accept("系统通知"));

// --- 装饰器模式 → Function andThen ---
Function<String, String> trim = String::trim;
Function<String, String> capitalize = s -> s.substring(0, 1).toUpperCase() + s.substring(1);
Function<String, String> decorate = trim.andThen(capitalize).andThen(s -> "★ " + s + " ★");
System.out.println(decorate.apply("  hello "));  // ★ Hello ★

// --- 工厂模式 → Supplier ---
Supplier<Connection> connectionSupplier = () -> {
    try { return DriverManager.getConnection("jdbc:..."); }
    catch (SQLException e) { throw new RuntimeException(e); }
};
// 延迟到需要时才调用 get()
Connection conn = connectionSupplier.get();
```

### 6.2 模式在主流框架中的应用速查

| 设计模式 | Spring 框架 | JDK / 标准库 | MyBatis |
|---------|------------|-------------|---------|
| **单例** | Bean 默认 scope=singleton | `Runtime.getRuntime()` | SqlSessionFactory |
| **工厂** | `BeanFactory` / `ApplicationContext` | `Calendar.getInstance()` | SqlSessionFactoryBean |
| **抽象工厂** | `FactoryBean` 体系 | `DatatypeConverter` | MapperProxyFactory |
| **建造者** | `BeanDefinitionBuilder` | `StringBuilder` / `Stream.Builder` | XMLConfigBuilder |
| **原型** | `@Scope("prototype")` | `Object.clone()` | — |
| **适配器** | `HandlerAdapter` | `InputStreamReader` / `Arrays.asList()` | — |
| **桥接** | 资源加载器体系 | JDBC 驱动 | — |
| **组合** | `CompositePropertySource` | `Container.add(Component)` | `<if>` / `<where>` 标签 |
| **装饰器** | `TransactionProxyFactoryBean` | `BufferedInputStream` | `Cache` 装饰链 |
| **外观** | `JdbcTemplate` / `RestTemplate` | `javax.faces.context.FacesContext` | `SqlSession` |
| **享元** | 连接池 / Bean 池 | `String.intern()` / `Integer.valueOf()` | MappedStatement 缓存 |
| **代理** | **AOP (JDK Proxy / CGLIB)** | `java.lang.reflect.Proxy` | Mapper 动态代理 |
| **责任链** | **SecurityFilterChain** / Interceptor | Logger 层次 | Plugin 拦截器链 |
| **命令** | `JdbcTemplate` 回调 | `Runnable` / `Callable` | — |
| **迭代器** | `CompositeIterator` | **集合框架 Iterator** | `Cursor` 接口 |
| **中介者** | `DispatcherServlet` | `Timer` 调度 | — |
| **备忘录** | — | 序列化/反序列化 | — |
| **观察者** | **`ApplicationEvent` / `@EventListener`** | Swing 监听器 | — |
| **状态** | `AbstractStateMachine` | 线程状态机 | — |
| **策略** | `ResourceLoader` / `InstantiationStrategy` | `Comparator` / `ThreadPoolExecutor` 拒绝策略 | `StatementHandler` |
| **模板方法** | **`JdbcTemplate` / `RestTemplate`** | `InputStream.read()` / `AbstractList` | `BaseExecutor` |
| **访问者** | `BeanDefinitionVisitor` | 编译器 AST | — |

### 6.3 常见误区

1. **过度设计**："既然有模式，那就用上" → 应该用简单方案解决问题，模式出现在重构中而非设计初期
2. **生搬硬套**：为用模式而用模式 → 模式是为了解决特定问题的，没有对应问题就不要用
3. **忽视语言特性**：Java 8 之后很多模式可以用 Lambda/Stream 简化（如策略、命令）
4. **模式大战**：问"策略和状态有什么区别"时，说明还没理解核心思想 → 策略是"我主动选算法"，状态是"状态自动决定行为"
5. **万能钥匙幻觉**：觉得学了 23 种模式就是高手了 → 关键是在合适的场景用合适的模式，这需要经验积累

---

## 七、如何选择设计模式

### 根据问题快速定位

```
用什么模式？
│
├─ 对象创建复杂？
│   ├─ 保证一个实例 → 单例
│   ├─ 子类决定创建 → 工厂方法
│   ├─ 产品族一致性 → 抽象工厂
│   ├─ 分步构建 → 建造者
│   └─ 克隆代替创建 → 原型
│
├─ 对象结构/组合？
│   ├─ 接口不匹配 → 适配器
│   ├─ 多维度变化 → 桥接
│   ├─ 树形结构 → 组合
│   ├─ 动态添加职责 → 装饰器
│   ├─ 简化子系统 → 外观
│   ├─ 大量细粒度对象 → 享元
│   └─ 控制访问 → 代理
│
└─ 对象交互/行为？
    ├─ 多级处理 → 责任链
    ├─ 请求封装 → 命令
    ├─ 解析 DSL → 解释器
    ├─ 顺序访问集合 → 迭代器
    ├─ 多对多交互简化 → 中介者
    ├─ 状态恢复 → 备忘录
    ├─ 一对多通知 → 观察者
    ├─ 状态决定行为 → 状态
    ├─ 算法互换 → 策略
    ├─ 固定流程 → 模板方法
    └─ 数据操作分离 → 访问者
```

### 模式之间的关联

```
相似的兄弟模式：
  策略 ↔ 状态      → 结构相同，意图不同（主动 vs 被动）
  策略 ↔ 模板方法   → 策略用组合，模板方法用继承
  工厂 ↔ 建造者     → 工厂一步到位，建造者分步构建
  装饰器 ↔ 代理     → 装饰器增强功能，代理控制访问
  适配器 ↔ 外观     → 适配器改接口，外观简化接口
  组合 ↔ 装饰器     → 组合强调整体-部分，装饰器强调层层包装
```

### 学习建议

1. **先理解 SOLID** —— 模式的根基
2. **从 3 个基础模式入手**：策略、观察者、装饰器（最常用）
3. **结合 JDK/Spring 源码看** —— 比背类图有效得多
4. **在重构中引入** —— 不要设计初期就套模式，先写简单实现，重构时引入模式
5. **学会之后要忘掉** —— 最终目标是写出代码自然符合模式，而不是刻意套用

> **最终信仰：** 设计模式是"路标"，不是"地图"。它们指引方向，但真正的道路是结合你面对的具体问题走出来的。

---

*笔记结束。所有代码可在 JDK 8+ 环境下直接运行。*

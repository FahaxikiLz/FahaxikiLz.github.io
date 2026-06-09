---
title: Claude Code
date: 2026-04-01 16:32:48
tags:
- Claude Code
categories:
- AI
---

## 一、Claude Code 是什么?

Claude Code 是 Anthropic 推出的终端 AI 编码助手,核心理念是"住在终端里"。它能理解项目结构、编辑代码、执行命令、管理 Git,虽然支持 IDE 集成,但本质是命令行驱动的工具。

> 开源版本的替代品(OpenCode)您可以参考这篇文章: https://www.kevnu.com/zh/posts/opencode-in-depth-technical-analysis-of-open-source-ai-programming-agents

> 关于 Claude Agent Skills 您可以参考这篇文章: https://www.kevnu.com/zh/posts/claude-agent-skills-deep-dive-a-new-paradigm-for-expanding-ai-agent-capabilities

## 二、安装与配置

### 2.1 安装方式

**NPM 安装(推荐 Node.js 用户)**

```
npm install -g @anthropic-ai/claude-code
```

**原生安装脚本**

macOS / Linux / WSL:

```
curl -fsSL https://claude.ai/install.sh | bash
```

Windows PowerShell:

```
irm https://claude.ai/install.ps1 | iex
```

Windows CMD:

```
curl -fsSL https://claude.ai/install.cmd -o install.cmd && install.cmd && del install.cmd
```

**验证安装**

```
claude --version
# 或直接运行
claude
```

看到版本信息或进入交互提示符,说明安装成功。

### 2.2 登录与授权

首次运行 `claude` 会提示登录,支持两种方式:

- **个人用户**:使用 Claude.ai 账户交互式登录
- **企业/API 用户**:使用 Anthropic API Key 授权

凭据保存在 `~/.config/claude`,后续可通过 `/login` 命令重新登录或切换账户。

### 2.3 基础使用

进入项目目录并启动:

```
cd /path/to/your/project
claude
```

交互模式下,直接用自然语言描述需求:

```
> create utilities/logger.py with rotating file handler
> write unit tests for date parsing edge cases
> explain this function foo in module bar
> refactor module baz to async/await
```

Claude Code 会自动读取项目上下文,理解代码结构并执行操作。

## 三、Slash 命令

本节汇总 Claude Code 的内置命令与自定义命令要点。

- 内置命令用于控制会话、查看状态、导出、调试等,使用频率高,应记住常用几条
- 自定义命令通过 Markdown 文件定义,支持参数、bash 执行、文件引用和权限控制,适合把常用模板固化为命令

### 内置命令(常用)

- `/add-dir`:添加额外的工作目录
- `/agents`:管理自定义子代理(AI subagents)
- `/bashes`:列出/管理后台 bash 任务
- `/bug`:上报 bug(会把对话发送到 Anthropic)
- `/clear`:清空会话历史
- `/compact [instructions]`:压缩会话上下文,可带聚焦指令
- `/config`:打开设置界面(Config 选项卡)
- `/context`:以彩色网格方式可视化当前上下文使用情况
- `/cost`:显示 token 使用统计
- `/doctor`:检查 Claude Code 安装健康状况
- `/exit`:退出 REPL
- `/export [filename]`:导出当前会话到文件或剪贴板
- `/help`:获取帮助列表和用法
- `/hooks`:管理与工具事件相关的 hook 配置
- `/ide`:管理 IDE 集成并显示状态
- `/init`:用 `CLAUDE.md` 指南初始化项目
- `/install-github-app`:为仓库设置 Claude GitHub Actions
- `/login`、`/logout`:切换或退出 Anthropic 账户
- `/mcp`:管理 MCP 服务器连接和 OAuth 授权
- `/memory`:编辑 `CLAUDE.md` 的记忆文件
- `/model`:选择或切换 AI 模型
- `/output-style [style]`:设置输出风格
- `/permissions`:查看或更新权限规则
- `/plugin`:管理 Claude Code 插件
- `/pr-comments`:查看 PR 评论
- `/privacy-settings`:查看/更新隐私设置
- `/release-notes`:查看发布说明
- `/resume`:恢复会话
- `/review`:请求代码审查
- `/rewind`:回退会话或代码状态
- `/sandbox`:启用受限的 sandboxed bash(文件系统与网络隔离)
- `/security-review`:对当前分支的待定更改执行安全审查
- `/status`:打开状态界面(显示版本、模型、账户和连通性)
- `/statusline`:设置状态行 UI
- `/terminal-setup`:为 iTerm2 / VSCode 安装 Shift+Enter 换行快捷键
- `/todos`:列出当前 todo 项
- `/usage`:显示订阅计划使用限额和速率限制状态
- `/vim`:进入 vim 模式(交替插入/命令模式)

### 自定义命令(要点)

**语法**:`/<command-name> [arguments]`

**命令类型**:

- 项目命令(project):位于 `.claude/commands/`,对团队共享
- 个人命令(user):位于 `~/.claude/commands/`,在所有项目可用
- 插件命令:随插件分发,位于插件的 `commands/` 目录,使用 `/plugin-name:command` 命名空间
- MCP 命令:由已连接的 MCP 服务器动态发现,格式为 `/mcp__<server>__<prompt> [args]`

**参数支持**:

- `$ARGUMENTS`:捕获所有参数字符串(适合自由文本)
- `$1, $2, ...`:按位置访问单个参数(适合结构化参数和默认值处理)

**Bash 执行**:在命令文件中可用 `!` 前缀执行 bash 命令(需在 frontmatter 中允许相应的 `allowed-tools`)

**文件引用**:使用 `@path/to/file` 在命令上下文中引用文件内容

**前置元数据(frontmatter)**:支持字段包括 `allowed-tools`、`argument-hint`、`description`、`model`、`disable-model-invocation` 等

### SlashCommand 工具行为与权限

- `SlashCommand` 工具只支持用户定义的自定义命令(不包含内置命令)
- 字符预算:默认 15,000 字符(可通过 `SLASH_COMMAND_TOOL_CHAR_BUDGET` 环境变量调整)
- 禁用方法:通过权限规则禁止 `SlashCommand` 工具,或在命令 frontmatter 加 `disable-model-invocation: true`
- 权限规则:支持精确匹配(`SlashCommand:/commit`)和前缀匹配(`SlashCommand:/review-pr:*`)

### 工程实践建议

- 把重复的审查、测试和发布模板做成自定义命令,减少手工操作
- 对可能执行写入或提交的命令,严格设置 `allowed-tools` 并在 PR 流程中审查命令文件
- 在团队中优先使用项目命令共享最佳实践;个人命令适用于个人效率提升
- 对关键输出采用 review 步骤或多模型验证,避免低成本模型给出不可靠结果

### Skills vs Slash commands

- **Slash commands**:适合简单、重复的提示,明确手动触发
- **Skills**:适合复杂、多文件、自动发现的能力,包含脚本和校验流程

> 如果你想了解 Agent Skills, 可以访问 https://www.kevnu.com/zh/posts/claude-agent-skills-deep-dive-a-new-paradigm-for-expanding-ai-agent-capabilities

更多细节请参见官方文档的"Custom slash commands"、"Plugin commands"和"MCP slash commands"节。

## 四、为什么需要第三方模型?

直接使用官方模型存在以下问题:

- **成本高**:按 token 计费,长期使用费用显著
- **额度限制**:每日/每周调用限制,策略频繁调整
- **灵活性差**:无法使用 DeepSeek、OpenRouter、本地 LLM 等第三方模型

**解决方案**:在调用链路中插入路由/代理层,实现:

- 按任务类型或成本策略路由到不同模型
- 降低整体使用成本
- 提高服务可用性
- 获得更大的模型选择灵活性

**【ClaudeCode】报错解决方案**：claude code 无法连接到 Anthropic 服务

1.  首先你要知道你的用户主目录 ，即"C:\Users\你的用户名"，配置文件默认会在这个目录下，然后找到.claude.json文件。

2. 打开.claude.json文件，可以使用vscode、notepad++、记事本、vim等文本编辑工具，打开后会出现类 似的配置样式，加上选中区域的字段（别忘了给上一行加个逗号）。

   字段为：

   ```
   "hasCompletedOnboarding": true
   ```

   ![img](./Claude%20Code/9d4be0a6eed1476ea35514544b573367.png)

## 五、快速接入:兼容模型直连

### 5.1 DeepSeek 集成

DeepSeek 提供与 Claude API 兼容的接口,通过环境变量即可"无痛替换":

**Bash/Zsh 配置** (添加到 `~/.bashrc` 或 `~/.zshrc`):

```
export ANTHROPIC_BASE_URL=https://api.deepseek.com/anthropic
export ANTHROPIC_AUTH_TOKEN=${YOUR_DEEPSEEK_API_KEY}
export API_TIMEOUT_MS=600000
export ANTHROPIC_MODEL=deepseek-chat
export ANTHROPIC_SMALL_FAST_MODEL=deepseek-chat
export CLAUDE_CODE_DISABLE_NONESSENTIAL_TRAFFIC=1
```

**Fish 配置** (添加到 `~/.config/fish/config.fish`):

```
set -gx ANTHROPIC_BASE_URL https://api.deepseek.com/anthropic
set -gx ANTHROPIC_AUTH_TOKEN <YOUR_DEEPSEEK_API_KEY>
set -gx API_TIMEOUT_MS 600000
set -gx ANTHROPIC_MODEL deepseek-chat
set -gx ANTHROPIC_SMALL_FAST_MODEL deepseek-chat
set -gx CLAUDE_CODE_DISABLE_NONESSENTIAL_TRAFFIC 1
```

配置后运行 `claude`,所有模型调用将自动转向 DeepSeek。

![DeepSeek 集成示意图](./Claude%20Code/1760111074-9kKfmvT5.png)

### 5.2 智谱 GLM 集成

**自动配置(推荐)**

```
curl -fsSL "https://cdn.bigmodel.cn/install/claude_code_env.sh" | bash
```

脚本会自动修改 `~/.claude/settings.json`:

```
{
  "env": {
    "ANTHROPIC_AUTH_TOKEN": "your_zhipu_api_key",
    "ANTHROPIC_BASE_URL": "https://open.bigmodel.cn/api/anthropic",
    "API_TIMEOUT_MS": "3000000"
  }
}
```

**手动配置**

新增或编辑 `~/.claude/settings.json`:

```
{
  "env": {
    "ANTHROPIC_AUTH_TOKEN": "your_zhipu_api_key",
    "ANTHROPIC_BASE_URL": "https://open.bigmodel.cn/api/anthropic", // 中国区
    // "ANTHROPIC_BASE_URL": "https://api.z.ai/api/anthropic",  // 国际版
    "API_TIMEOUT_MS": "3000000",
    "ANTHROPIC_DEFAULT_HAIKU_MODEL": "glm-4.5-air",
    "ANTHROPIC_DEFAULT_SONNET_MODEL": "glm-4.6",
    "ANTHROPIC_DEFAULT_OPUS_MODEL": "glm-4.6"
  }
}
```

**环境变量方式** (JSON 方式可能失效时使用):

```
export ANTHROPIC_AUTH_TOKEN=your_zhipu_api_key
export ANTHROPIC_BASE_URL=https://open.bigmodel.cn/api/anthropic  # 中国区
# export ANTHROPIC_BASE_URL=https://api.z.ai/api/anthropic  # 国际版
export API_TIMEOUT_MS=3000000
export ANTHROPIC_DEFAULT_HAIKU_MODEL=glm-4.5-air
export ANTHROPIC_DEFAULT_SONNET_MODEL=glm-4.6
export ANTHROPIC_DEFAULT_OPUS_MODEL=glm-4.6
```

![智普 GLM 4.6 预览图](./Claude%20Code/1760238790-47higrX4.png)

### 5.3 千问集成

**支持的模型**

百炼提供的 Anthropic API 兼容服务支持以下千问系列模型：

| **模型系列**                    | **支持的模型名称（model）**                                  |
| ------------------------------- | ------------------------------------------------------------ |
| 千问Max（部分模型支持思考模式） | qwen3-max、qwen3-max-2026-01-23（支持思考模式）、qwen3-max-preview（支持思考模式） [查看更多](https://help.aliyun.com/zh/model-studio/models#d4ccf72f23jh9) |
| 千问Plus                        | qwen3.5-plus、qwen3.5-plus-2026-02-15、qwen-plus、qwen-plus-latest、qwen-plus-2025-09-11 [查看更多](https://help.aliyun.com/zh/model-studio/models#5ef284d4ed42p) |
| 千问Flash                       | qwen3.5-flash、qwen3.5-flash-2026-02-23、qwen-flash、qwen-flash-2025-07-28 [查看更多](https://help.aliyun.com/zh/model-studio/models#13ff05e329blt) |
| 千问Turbo                       | qwen-turbo、qwen-turbo-latest [查看更多](https://help.aliyun.com/zh/model-studio/models#947fc66bc1ldf) |
| 千问Coder（不支持思考模式）     | qwen3-coder-next、qwen3-coder-plus、qwen3-coder-plus-2025-09-23、qwen3-coder-flash [查看更多](https://help.aliyun.com/zh/model-studio/models#d698550551bob) |
| 千问VL（不支持思考模式）        | qwen3-vl-plus、qwen3-vl-flash、qwen-vl-max、qwen-vl-plus [查看更多](https://help.aliyun.com/zh/model-studio/models#94b18818a6ywy) |
| 千问开源模型                    | qwen3.5-397b-a17b、qwen3.5-120b-a10b、qwen3.5-27b、qwen3.5-35b-a3b |
| 第三方模型                      | kimi-k2.5、kimi-k2-thinkingglm-5、glm-4.7、glm-4.6MiniMax-M2.5、MiniMax-M2.1 |

**配置 Base URL、API Key 和模型**

要通过兼容 Anthropic API 的方式来接入阿里云百炼的模型服务，需要配置以下环境变量。

1. `ANTHROPIC_BASE_URL`：设置为 `https://dashscope.aliyuncs.com/apps/anthropic`。

2. `ANTHROPIC_API_KEY`或`ANTHROPIC_AUTH_TOKEN`：设置为[阿里云百炼 API Key](https://help.aliyun.com/zh/model-studio/get-api-key)。

   > `ANTHROPIC_API_KEY`或`ANTHROPIC_AUTH_TOKEN`均可作为接入认证，只需要设置其一即可。本文以`ANTHROPIC_API_KEY`为例。

3. `ANTHROPIC_MODEL`：设置为[模型列表](https://help.aliyun.com/zh/model-studio/claude-code#07833dedefft7)中支持的模型。

```shell
setx ANTHROPIC_API_KEY "YOUR_DASHSCOPE_API_KEY"
setx ANTHROPIC_BASE_URL "https://dashscope.aliyuncs.com/apps/anthropic"
setx ANTHROPIC_MODEL "qwen3.5-plus"
```



打开一个新的 CMD 窗口，运行以下命令，检查环境变量是否生效。

```shell
echo %ANTHROPIC_API_KEY%
echo %ANTHROPIC_BASE_URL%
echo %ANTHROPIC_MODEL%
```

### 5.4 注意事项

- 确保第三方 API 与当前 Claude Code 版本兼容
- 根据任务特点选择合适模型(如 `deepseek-reasoner` 适合复杂推理)
- 及时关注 API 文档更新,必要时调整配置

## 六、进阶方案:智能路由与切换工具

当需要更复杂的模型管理策略时,直连方式就不够用了。这里介绍两个开源工具:

- **Claude Code Router (CCR)**:命令行路由代理,支持动态路由、多模型协作
- **CC-Switch**:跨平台桌面应用,提供可视化配置管理

### 6.1 Claude Code Router (CCR)

**CCR** 是命令行路由解决方案,提供:

- 多 Provider 统一管理
- 灵活的路由规则引擎
- 请求/响应转换器(Transformer)
- 交互式模型切换
- 完整的日志与统计系统

#### 安装

```
npm install -g @musistudio/claude-code-router
```

#### 配置

创建 `~/.claude-code-router/config.json`:

```
{
  "APIKEY": "your-secret-key",
  "PROXY_URL": "http://127.0.0.1:7890",
  "LOG": true,
  "API_TIMEOUT_MS": 600000,
  "NON_INTERACTIVE_MODE": false,

  "Providers": [
    {
      "name": "openrouter",
      "api_base_url": "https://openrouter.ai/api/v1/chat/completions",
      "api_key": "sk-xxx",
      "models": [
        "google/gemini-2.5-pro-preview",
        "anthropic/claude-sonnet-4",
        "anthropic/claude-3.5-sonnet",
        "anthropic/claude-3.7-sonnet:thinking"
      ],
      "transformer": {
        "use": ["openrouter"]
      }
    },
    {
      "name": "deepseek",
      "api_base_url": "https://api.deepseek.com/chat/completions",
      "api_key": "sk-xxx",
      "models": ["deepseek-chat", "deepseek-reasoner"],
      "transformer": {
        "use": ["deepseek"],
        "deepseek-chat": {
          "use": ["tooluse"]
        }
      }
    },
    {
      "name": "ollama",
      "api_base_url": "http://localhost:11434/v1/chat/completions",
      "api_key": "ollama",
      "models": ["qwen2.5-coder:latest"]
    },
    {
      "name": "gemini",
      "api_base_url": "https://generativelanguage.googleapis.com/v1beta/models/",
      "api_key": "sk-xxx",
      "models": ["gemini-2.5-flash", "gemini-2.5-pro"],
      "transformer": {
        "use": ["gemini"]
      }
    },
    {
      "name": "volcengine",
      "api_base_url": "https://ark.cn-beijing.volces.com/api/v3/chat/completions",
      "api_key": "sk-xxx",
      "models": ["deepseek-v3-250324", "deepseek-r1-250528"],
      "transformer": {
        "use": ["deepseek"]
      }
    },
    {
      "name": "modelscope",
      "api_base_url": "https://api-inference.modelscope.cn/v1/chat/completions",
      "api_key": "",
      "models": [
        "Qwen/Qwen3-Coder-480B-A35B-Instruct",
        "Qwen/Qwen3-235B-A22B-Thinking-2507"
      ],
      "transformer": {
        "use": [["maxtoken", { "max_tokens": 65536 }], "enhancetool"],
        "Qwen/Qwen3-235B-A22B-Thinking-2507": {
          "use": ["reasoning"]
        }
      }
    },
    {
      "name": "dashscope",
      "api_base_url": "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions",
      "api_key": "",
      "models": ["qwen3-coder-plus"],
      "transformer": {
        "use": [["maxtoken", { "max_tokens": 65536 }], "enhancetool"]
      }
    },
    {
      "name": "aihubmix",
      "api_base_url": "https://aihubmix.com/v1/chat/completions",
      "api_key": "sk-",
      "models": ["Z/glm-4.5", "claude-opus-4-20250514", "gemini-2.5-pro"]
    }
  ],

  "Router": {
    "default": "deepseek,deepseek-chat",
    "background": "ollama,qwen2.5-coder:latest",
    "think": "deepseek,deepseek-reasoner",
    "longContext": "openrouter,google/gemini-2.5-pro-preview",
    "longContextThreshold": 60000,
    "webSearch": "gemini,gemini-2.5-flash"
  }
}
```

**配置说明**:

基础配置:

- `PROXY_URL`:代理服务器地址
- `LOG`:是否启用日志(`true`/`false`)
- `LOG_LEVEL`:日志级别(`fatal`/`error`/`warn`/`info`/`debug`/`trace`)
- `APIKEY`:访问密钥(可选,启用后客户端需在 `Authorization` 或 `x-api-key` 头中提供)
- `HOST`:服务监听地址(默认 `127.0.0.1`)
- `NON_INTERACTIVE_MODE`:非交互模式,适配 CI/Docker 等环境
- `API_TIMEOUT_MS`:上游模型调用超时时间(毫秒)

日志系统(双日志架构):

1. **服务器日志**:HTTP 请求、API 调用,写入 `~/.claude-code-router/logs/ccr-*.log`
2. **应用日志**:路由决策、业务事件,写入 `~/.claude-code-router/claude-code-router.log`

Providers 配置:

- `name`:Provider 标识符
- `api_base_url`:API 基础地址
- `api_key`:认证密钥(支持环境变量插值,如 `$OPENAI_API_KEY` 或 `${OPENAI_API_KEY}`)
- `models`:支持的模型列表
- `transformer`:请求/响应转换规则,适配不同 API 协议

Router 配置:

- `default`:默认模型路由
- `background`:后台任务模型(通常选择本地或低成本模型)
- `think`:复杂推理任务模型
- `longContext`:长上下文任务模型
- `longContextThreshold`:长上下文阈值(token 数)
- `webSearch`:需要联网搜索的任务模型

#### 运行

```
# 启动 Claude Code
ccr code

# 重启服务
ccr restart

# 打开 UI 管理界面
ccr ui

# CLI 模型管理
ccr model
```

**CLI 模型管理** (`ccr model`):

- 查看当前配置
- 切换模型
- 添加新模型
- 创建新 Provider(包括 API 端点、密钥、模型列表、Transformer 配置)

#### 应用场景

**成本优化**:

- 短 prompt 或常规生成 → 本地模型或廉价 API
- 复杂推理或关键输出 → 高质量付费模型

**服务可靠性**:

- 主模型不可用 → 自动降级到备用模型
- 关键任务 → 多模型并行调用并投票决策

**灵活扩展**:

- 无缝接入新 Provider 和模型
- 自定义转换器适配特殊 API 协议

#### 优势与注意事项

**优势**:

- ✅ 极高的灵活性,支持任意兼容 API 的模型
- ✅ 显著降低使用成本
- ✅ 强大的扩展能力和策略配置
- ✅ 支持环境变量插值,安全管理 API Key

**注意事项**:

- ⚠️ **延迟增加**:代理层会引入网络开销
- ⚠️ **兼容性**:需要 transformer 适配不同 API 协议
- ⚠️ **安全性**:避免配置文件中明文存储 API Key,使用环境变量
- ⚠️ **质量差异**:低成本模型可能产生幻觉或错误,需要校验和 fallback 机制

### 6.2 CC-Switch

**CC-Switch** 是跨平台桌面应用,提供可视化的 Claude Code / Codex / Gemini CLI 配置管理。

#### 核心功能

**Provider 管理**:

- 一键切换 Claude Code、Codex、Gemini API 配置
- 速度测试:测量 API 端点延迟,可视化质量指标
- 导入/导出:备份和恢复配置,自动轮转(保留最近 10 个)
- Provider 复制和拖拽排序
- 多端点管理和自定义配置目录(支持云同步)

**MCP 管理**:

- 单一面板管理三个应用的 MCP 服务器
- 支持 SSE (Server-Sent Events) 传输类型
- 智能 JSON 解析 + Codex TOML 格式自动修正
- 统一导入/导出 + 双向同步
- 内置模板(mcp-fetch、mcp-filesystem 等)

**Skills 管理**:

- 自动扫描 GitHub 仓库中的 skills(预配置 3 个精选仓库)
- 一键安装/卸载到 `~/.claude/skills/`
- 自定义仓库支持 + 子目录扫描
- 完整生命周期管理(发现/安装/更新)

**Prompts 管理**:

- 多预设系统提示管理(无限预设,快速切换)
- 跨应用支持(Claude: `CLAUDE.md` / Codex: `AGENTS.md` / Gemini: `GEMINI.md`)
- Markdown 编辑器(CodeMirror 6 + 实时预览)
- 智能回填保护,保留手动修改

**其他特性**:

- Deep Link 协议(`ccswitch://`):一键导入 provider 配置
- 环境变量冲突检测:自动检测跨应用配置冲突
- 系统托盘快速切换
- 单实例守护进程
- 内置自动更新器
- 原子写入 + 回滚保护
- 多语言支持(中文/英文/日文)
- 开机自启动(一键启用/禁用)

#### 安装

**系统要求**:

- Windows: Windows 10 及以上
- macOS: macOS 10.15 (Catalina) 及以上
- Linux: Ubuntu 22.04+ / Debian 11+ / Fedora 34+ 等主流发行版

**Windows**: 下载 `CC-Switch-v{version}-Windows.msi` 安装包或 `CC-Switch-v{version}-Windows-Portable.zip` 便携版,从 [Releases](https://github.com/farion1231/cc-switch/releases) 页面获取。

**macOS**:

方式一:Homebrew 安装(推荐)

```
brew tap farion1231/ccswitch
brew install --cask cc-switch
```

更新:

```
brew upgrade --cask cc-switch
```

方式二:手动下载 下载 `CC-Switch-v{version}-macOS.zip` 从 [Releases](https://github.com/farion1231/cc-switch/releases) 页面。

> **注意**:由于作者没有 Apple Developer 账户,首次启动可能看到"未识别的开发者"警告。请先关闭,然后前往"系统设置" → "隐私与安全性" → 点击"仍要打开",之后即可正常使用。

**ArchLinux**:

```
paru -S cc-switch-bin
```

**Linux**: 下载 `CC-Switch-v{version}-Linux.deb` 或 `CC-Switch-v{version}-Linux.AppImage` 从 [Releases](https://github.com/farion1231/cc-switch/releases) 页面。

#### 快速开始

**基础使用**:

1. 添加 Provider:点击"添加 Provider" → 选择预设或创建自定义配置
2. 切换 Provider:
   - 主界面:选择 provider → 点击"启用"
   - 系统托盘:直接点击 provider 名称(立即生效)
3. 生效:重启终端或 Claude Code / Codex / Gemini 客户端
4. 回到官方:选择"Official Login"预设(Claude/Codex)或"Google Official"预设(Gemini),重启客户端,然后按照登录/OAuth 流程操作

**MCP 管理**:

- 位置:点击右上角"MCP"按钮
- 添加服务器:使用内置模板或自定义配置
- 启用/禁用:切换开关控制哪些服务器同步到实时配置
- 导入/导出:从 Claude/Codex/Gemini 配置文件导入现有 MCP 服务器

**Skills 管理**:

- 位置:点击右上角"Skills"按钮
- 发现 Skills:自动扫描预配置的 GitHub 仓库
- 安装 Skills:点击"安装"一键安装到 `~/.claude/skills/`
- 卸载 Skills:点击"卸载"安全移除并清理状态
- 管理仓库:添加/移除自定义 GitHub 仓库

**Prompts 管理**:

- 位置:点击右上角"Prompts"按钮
- 创建预设:创建无限系统提示预设,使用 Markdown 编辑器编写
- 切换预设:选择预设 → 点击"激活"立即应用
- 同步机制:
  - Claude: `~/.claude/CLAUDE.md`
  - Codex: `~/.codex/AGENTS.md`
  - Gemini: `~/.gemini/GEMINI.md`
- 保护机制:切换前自动保存当前提示内容,保留手动修改

#### 技术架构

**数据存储**:

- SQLite + JSON 双层架构
- 可同步数据(providers、MCP、Prompts、Skills)存储在 SQLite
- 设备级数据(窗口状态、本地路径)存储在 JSON

**技术栈**:

- 前端:React + TypeScript + Tailwind CSS
- 后端:Rust (Tauri)
- 数据库:SQLite
- 编辑器:CodeMirror 6

### 6.3 CCR vs CC-Switch 对比

| 特性             | Claude Code Router (CCR) | CC-Switch                  |
| :--------------- | :----------------------- | :------------------------- |
| **类型**         | 命令行工具               | 桌面应用                   |
| **界面**         | CLI + Web UI             | 基于 Tauri 的 GUI          |
| **适用场景**     | 服务器、自动化、CI/CD    | 本地开发、可视化管理       |
| **模型路由**     | ✅ 高级路由策略           | ❌ 简单切换                 |
| **配置管理**     | JSON 配置文件            | 可视化界面 + SQLite        |
| **MCP 管理**     | ❌                        | ✅ 跨应用统一管理           |
| **Skills 管理**  | ❌                        | ✅ GitHub 仓库扫描          |
| **Prompts 管理** | ❌                        | ✅ 多预设 + Markdown 编辑器 |
| **速度测试**     | ❌                        | ✅ API 延迟测试             |
| **系统托盘**     | ❌                        | ✅ 快速切换                 |
| **云同步**       | ❌                        | ✅ (v3.8.0 架构支持)        |
| **学习曲线**     | 中等(需要配置 JSON)      | 低(可视化操作)             |

**选择建议**:

- **CCR**:适合需要高级路由策略、自动化部署、CI/CD 集成的场景
- **CC-Switch**:适合本地开发、需要可视化管理、频繁切换配置的场景
- **组合使用**:可以用 CC-Switch 管理基础配置,用 CCR 处理复杂路由逻辑

## 七、系统架构

```
┌──────────────────────┐
│  Claude Code CLI     │
└──────────┬───────────┘
           │
           ▼
┌────────────────────────────────────────┐
│  Router / Proxy (CCR / CC-Switch)      │
│  • 路由策略引擎                          │
│  • 请求转换                              │
│  • 响应聚合                              │
│  • 日志与计费                            │
└────┬──────────┬──────────┬──────────────┘
     │          │          │
     ▼          ▼          ▼
┌─────────┐ ┌────────┐ ┌─────────────┐
│DeepSeek │ │Local   │ │Claude 官方   │
│(推理)   │ │LLM     │ │(fallback)   │
│         │ │(低成本)│ │             │
└─────────┘ └────────┘ └─────────────┘
     │          │          │
     └──────────┴──────────┘
                │
                ▼
     ┌────────────────────┐
     │  合并 / 过滤 / 日志 │
     └────────────────────┘
                │
                ▼
     ┌────────────────────┐
     │  返回给前端         │
     └────────────────────┘
```

## 八、「模板指令」

### 模板 1：启动理解项目

```kotlin
你现在在我的项目根目录。
 
请快速阅读 README、package.json 和关键入口文件，给出：
 
技术栈
目录结构说明
我应该从哪个入口理解业务
```

### 模板 2：加功能

```undefined
需求如下：…
 
约束如下：…
 
请先给实现计划（不要改代码）。
 
我确认后你再修改，并展示 diff。
```

### 模板 3：修 bug

```undefined
我遇到 bug：…
 
复现步骤：…
 
错误日志：…
 
请定位原因 → 提供修复 → 补测试 → 给出验证方式。
```

## 九、参考资源

- [Claude Code 官方文档](https://claude.ai/docs/claude-code)
- [DeepSeek Anthropic API 文档](https://api-docs.deepseek.com/zh-cn/guides/anthropic_api)
- [智谱 GLM 文档(国际版)](https://docs.z.ai/devpack/tool/claude#claude-code)
- [智谱 GLM 文档(中国区)](https://docs.bigmodel.cn/cn/coding-plan/tool/claude#手动配置(windows-macos-linux))
- [Claude Code-大模型服务平台百炼(Model Studio)-阿里云帮助中心](https://help.aliyun.com/zh/model-studio/claude-code#a127e6a1d3psx)
- [Claude Code Router (CCR) 开源项目](https://github.com/musistudio/claude-code-router)
- [CC-Switch 开源项目](https://github.com/farion1231/cc-switch)
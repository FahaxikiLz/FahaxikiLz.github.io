---
title: 从Java到C#
date: 2024-08-11 15:11:27
tags:
- 从Java到C#
categories:
- 编程语言
---

## 0 配置环境

### 下载 .net core SDK

1.进入微软官网下载地址 <https://dotnet.microsoft.com/zh-cn/download>，显示如下页面直接下载即可

![](C%20Sharp/a95e360716e03113c90aac1cb30226e5.png)

2.下载完成后右键以管理员身份运行，点击安装，然后稍微等待会就可安装完成

![](C%20Sharp/e5d04812a57fb4637445b6dc822df4c3.png)

3.安装完成后，win+r输入cmd 打开**新的**命令提示符并运行命令：dotnet，显示如下就说明安装成功

![](C%20Sharp/4454d41f51b48900aa22148e9db8eef4.png)

### 创建一个 C## 小栗子

#### 创建方式一

1.切换到你要保存该小栗子的目录下，手动创建一个 小栗子 目录并进入该目录后，在地址栏输入 cmd+回车，在新打开的命令提示符中输入 dotnet new console 来创建一个 C## 小栗子，如下

![](C%20Sharp/c2e0ac63809602f22a16be1f4e847ffd.png)![](C%20Sharp/87dafc8a5a077bc697229f7030813ec7.png)

2.然后在上述地址栏用 cmd+回车 打开的命令提示符中输入命令：code .（运行该命令表示在Vs Code 中打开 Demo\_01 工程），如下

![](C%20Sharp/dd5c69a339704e9ec7ab90160d4b2a39.png)

#### 创建方式二

1.切换到你保存要该小栗子的目录下，在地址栏输入 cmd+回车，在新打开的命令提示符中输入 dotnet new console \-o Demo\_01 \-f net7.0 来创建一个 C## 小栗子，如下

![](C%20Sharp/8e2a8fca61da13a6b8f5342a827e6ae0.png)![](C%20Sharp/cf121cb57ce556f7589297791476bfd2.png)

2.如上图所述会在当前目录下创建Demo\_01工程，在上述地址栏用 cmd+回车 打开的命令提示符中输入cd Demo\_01 命令切换到该工程目录下，然后在输入code . 命令（运行该命令表示在Vs Code 中打开该工程），如下

![](C%20Sharp/1ca67fdb8f23e4a15c169759360b6b04.png)

> - `dotnet new console` 命令将为你新建控制台应用。
> - `-o` 参数会创建名为 Demo\_01 的目录，用于存储应用并使用所需文件进行填充。
> - `-f`参数指示你正在创建 .NET 7.0 应用程序。

### Vs Code 中环境准备

1.安装如下两个插件，然后重启 Vs Code

![](C%20Sharp/ed615566e958b41f5591914d7e693c83.png)

![](C%20Sharp/345e14bda0881ed123e9618ac21abea9.png)

2.在 Code Runner 插件中 勾选在终端运行 并打开 settings.json 文件，如下

![](C%20Sharp/4646b01a3f324587272c36362c11e9d7.png)

3.在打开的 settings.json 文件中，在最后一行输入code在弹出的框中选择 code-runner.executorMap 后按回车，自动会填充如下内容（内容不要对比哈，自动填充的，知道是这么会事就行）

```json
"code-runner.executorMap": {
    "javascript": "node",
    "java": "cd $dir && javac $fileName && java $fileNameWithoutExt",
    "c": "cd $dir && gcc $fileName -o $fileNameWithoutExt && $dir$fileNameWithoutExt",
    "cpp": "cd $dir && g++ $fileName -o $fileNameWithoutExt && $dir$fileNameWithoutExt",
    "objective-c": "cd $dir && gcc -framework Cocoa $fileName -o $fileNameWithoutExt && $dir$fileNameWithoutExt",
    "php": "php",
    "python": "python -u",
    "perl": "perl",
    "perl6": "perl6",
    "ruby": "ruby",
    "go": "go run",
    "lua": "lua",
    "groovy": "groovy",
    "powershell": "powershell -ExecutionPolicy ByPass -File",
    "bat": "cmd /c",
    "shellscript": "bash",
    "fsharp": "fsi",
    "csharp": "scriptcs",
    "vbscript": "cscript //Nologo",
    "typescript": "ts-node",
    "coffeescript": "coffee",
    "scala": "scala",
    "swift": "swift",
    "julia": "julia",
    "crystal": "crystal",
    "ocaml": "ocaml",
    "r": "Rscript",
    "applescript": "osascript",
    "clojure": "lein exec",
    "haxe": "haxe --cwd $dirWithoutTrailingSlash --run $fileNameWithoutExt",
    "rust": "cd $dir && rustc $fileName && $dir$fileNameWithoutExt",
    "racket": "racket",
    "scheme": "csi -script",
    "ahk": "autohotkey",
    "autoit": "autoit3",
    "dart": "dart",
    "pascal": "cd $dir && fpc $fileName && $dir$fileNameWithoutExt",
    "d": "cd $dir && dmd $fileName && $dir$fileNameWithoutExt",
    "haskell": "runhaskell",
    "nim": "nim compile --verbosity:0 --hints:off --run",
    "lisp": "sbcl --script",
    "kit": "kitc --run",
    "v": "v run",
    "sass": "sass --style expanded",
    "scss": "scss --style expanded",
    "less": "cd $dir && lessc $fileName $fileNameWithoutExt.css",
    "FortranFreeForm": "cd $dir && gfortran $fileName -o $fileNameWithoutExt && $dir$fileNameWithoutExt",
    "fortran-modern": "cd $dir && gfortran $fileName -o $fileNameWithoutExt && $dir$fileNameWithoutExt",
    "fortran_fixed-form": "cd $dir && gfortran $fileName -o $fileNameWithoutExt && $dir$fileNameWithoutExt",
    "fortran": "cd $dir && gfortran $fileName -o $fileNameWithoutExt && $dir$fileNameWithoutExt",
    "sml": "cd $dir && sml $fileName"
}
```

4.在 setings.json 文件中找到上述内容中的 csharp 项，将其值更改为 cd \$dir \&\& dotnet run \$fileName ，然后保存即可

### 最后运行 C## 小栗子

#### 运行方式一

1.因为配置好了 settings.json 文件，所以可以直接用如下图那个三角形图标运行工程

![](C%20Sharp/265fffef2ac17dcac7e55385a2488b04.png)

#### 运行方式二

1.按如下新建一个终端，然后在终端中输入命令：dotnet run，\(不想配置settings.json可用这种方式\)

![](C%20Sharp/3de2468166d334f3a235c9b9eb1183f4.png)

## 1 C#语法概览

欢迎来到C#的世界！对于刚从Java转过来的开发者来说，你会发现C#和Java有很多相似之处，但C#也有其独特的魅力和强大之处。让我们一起来探索C#的基本语法，并比较一下与Java的异同。

#### 程序结构

C#程序的基本结构与Java非常相似。这里是一个简单的C#程序：

```csharp
using System;

namespace HelloWorld
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("Hello, World!");
        }
    }
}
```

对比Java的版本：

```java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}
```

你会发现，两者的结构非常相似。主要的区别在于：

1.  C#使用`using`关键字导入命名空间，而Java使用`import`。
2.  C#的`Main`方法是`static void Main(string[] args)`，而Java是`public static void main(String[] args)`。
3.  C#使用`Console.WriteLine()`输出，Java使用`System.out.println()`。

在c# 9的最新语法上还可以更简洁，是的没错，只需要一行代码，不需要写命名空间，类，方法，直接编写代码，当然这个方式只存在c#9以上的版本。

```csharp
Console.WriteLine("Hello, World!");
```

#### 命名约定

C#和Java的命名约定有些许不同：

- C#中，方法名和属性名通常使用PascalCase（如`CalculateTotal`）。
- 局部变量和参数使用camelCase（如`totalAmount`）。
- 接口名称以"I"开头（如`IDisposable`）。

而Java中：

- 方法名和变量名都使用camelCase。
- 接口名称不需要特殊前缀。

#### 数据类型

C#和Java的基本数据类型很相似，但也有一些区别：

C#:

```csharp
int x = 10;
long y = 100L;
float f = 3.14f;
double d = 3.14;
decimal m = 100.50m;
bool isTrue = true;
char c = 'A';
string s = "Hello";
```

Java:

```java
int x = 10;
long y = 100L;
float f = 3.14f;
double d = 3.14;
boolean isTrue = true;
char c = 'A';
String s = "Hello";
```

注意C#特有的`decimal`类型，它提供了更高精度的小数计算，特别适合金融相关的应用。

#### 数组

C#和Java的数组声明稍有不同：

C#:

```csharp
int[] numbers = new int[5];
string[] names = { "Alice", "Bob", "Charlie" };
```

Java:

```java
int[] numbers = new int[5];
String[] names = { "Alice", "Bob", "Charlie" };
```

#### 控制结构

C#和Java的控制结构几乎完全相同：

```csharp
// if语句
if (condition)
{
    // code
}
else if (anotherCondition)
{
    // code
}
else
{
    // code
}

// for循环
for (int i = 0; i < 10; i++)
{
    // code
}

// while循环
while (condition)
{
    // code
}

// switch语句
switch (variable)
{
    case value1:
        // code
        break;
    case value2:
        // code
        break;
    default:
        // code
        break;
}
```

这些结构在Java中的写法完全相同。

#### 异常处理

C#和Java的异常处理也非常相似：

C#:

```csharp
try
{
    // 可能抛出异常的代码
}
catch (SpecificException ex)
{
    // 处理特定异常
}
catch (Exception ex)
{
    // 处理一般异常
}
finally
{
    // 总是要执行的代码
}
```

Java的异常处理结构完全相同。

#### 注释

C#和Java的注释方式也是一样的：

```csharp
// 这是单行注释

/*
* 这是多行注释
*/

/// <summary>
/// 这是XML文档注释，类似于Java的Javadoc
/// </summary>
```

#### 小结

通过这个概览，你可以看到C#和Java在语法上有很多相似之处。这意味着作为一个Java开发者，你可以相对轻松地过渡到C#。然而，C#也有其独特的特性和语法糖，使得某些任务更加简洁和高效。

在接下来的章节中，我们将深入探讨C#的各个方面，包括它独特的特性如属性、事件、委托等。这些概念可能对Java开发者来说比较新，但它们是C#强大功能的关键所在。记住，学习一门新的语言不仅是学习语法，更是学习一种新的思维方式。让我们继续我们的C#学习之旅吧！

## 2 变量和数据类型

在C#中，变量和数据类型是编程的基础。对于从Java转过来的开发者来说，你会发现很多熟悉的概念，但C#也有一些独特的特性。让我们深入探讨C#的变量和数据类型，并与Java进行比较。

#### 变量声明

C#和Java的变量声明方式非常相似：

C#:

```csharp
int age = 25;
string name = "Alice";
bool isStudent = true;
```

Java:

```java
int age = 25;
String name = "Alice";
boolean isStudent = true;
```

主要区别在于：

1.  C#使用`string`（小写），而Java使用`String`（大写）。
2.  C#使用`bool`，而Java使用`boolean`。

#### 基本数据类型

C#和Java都有类似的基本数据类型，但C#提供了更多的选择：

| C# 类型 | Java 类型 | 大小  | 范围                              |
| ------- | --------- | ----- | --------------------------------- |
| sbyte   | byte      | 8位   | \-128 到 127                      |
| byte    | \-        | 8位   | 0 到 255                          |
| short   | short     | 16位  | \-32,768 到 32,767                |
| ushort  | \-        | 16位  | 0 到 65,535                       |
| int     | int       | 32位  | \-2\^31 到 2\^31-1                |
| uint    | \-        | 32位  | 0 到 2\^32-1                      |
| long    | long      | 64位  | \-2\^63 到 2\^63-1                |
| ulong   | \-        | 64位  | 0 到 2\^64-1                      |
| float   | float     | 32位  | ±1.5x 10\^-45 到 ±3.4 x 10\^38    |
| double  | double    | 64位  | ±5.0 × 10\^-324 到 ±1.7 × 10\^308 |
| decimal | \-        | 128位 | ±1.0 x 10\^-28 到 ±7.9 x 10\^28   |
| char    | char      | 16位  | U+0000 到 U+FFFF                  |
| bool    | boolean   | 8位   | true或 false                      |

注意C#提供了无符号整数类型（`byte`, `ushort`, `uint`, `ulong`）和`decimal`类型，这些在Java中是没有的。

#### 值类型和引用类型

C#和Java都区分值类型和引用类型，但C#的处理更加灵活：

1.  值类型（Value Types）：

    - 在C#中，所有的基本数据类型（int, float, bool等）和struct都是值类型。
    - 值类型直接存储它们的数据。

2.  引用类型（Reference Types）：

    - 类（class）、接口（interface）、委托（delegate）和数组（array）是引用类型。
    - 引用类型存储对其数据（对象）的引用。

C#独特之处：

- C#允许使用`struct`关键字创建自定义值类型。
- C#的`string`虽然是引用类型，但具有值类型的一些特性（如不可变性）。

#### 可空类型

C#引入了可空类型的概念，这在Java中是没有的：

```csharp
// 编译报错
int nullableInt = null;
bool nullableBool = null;

// 不报错
int? nullableInt = null;
bool? nullableBool = null;
```

可空类型（nullable types）是指可以在一个通常不允许为 null 的值类型上定义一个可以为 null 的类型。在 C# 中，所有的值类型（如 `int`, `bool`, `char`, `enum` 以及 `struct`）默认情况下不能赋值为 `null`。为了使一个值类型能够被赋值为 `null`，你需要使用可空类型。这在处理数据库或用户输入时非常有用。

#### var关键字

C#提供了`var`关键字用于隐式类型声明：

```csharp
var x = 10; // 编译器推断x为int类型
var name = "Alice"; // 编译器推断name为string类型
```

Java从Java 10开始引入了类似的`var`关键字，但使用范围更受限制。

#### 常量

C#使用`const`关键字声明常量：

```csharp
const int MaxValue = 100;
const string AppName = "MyApp";
```

Java使用`final`关键字：

```java
final int MAX_VALUE = 100;
final String APP_NAME = "MyApp";
```

#### Struct

C# 允许使用 `struct` 关键字创建自定义值类型。`struct` 是一种轻量级的类型，通常用于存储少量数据，并且在性能要求较高的场景下使用。与类（`class`）不同，结构体是值类型（而非引用类型），这意味着结构体的实例在赋值或传递时是按值复制的。

**结构体的特性**

- **值类型**：结构体是值类型，存储在栈上，而不是堆上。
- **不可继承**：结构体不能继承自其他结构体或类，但可以实现接口。
- **默认构造函数**：结构体不能定义显式的无参数构造函数，因为编译器会为它生成一个默认的无参构造函数，该构造函数会将所有字段初始化为默认值。
- **字段初始化**：结构体不能像类一样直接初始化字段，必须在构造函数中完成初始化。

**示例**

以下是一个简单的结构体示例，表示一个二维点（Point）：

```csharp
public struct Point
{
    public int X { get; set; }
    public int Y { get; set; }

    public Point(int x, int y)
    {
        X = x;
        Y = y;
    }

    public void Display()
    {
        Console.WriteLine($"Point is at ({X}, {Y})");
    }
}
```

在代码中可以这样使用这个结构体：

```csharp
Point p1 = new Point(10, 20);
p1.Display();  // 输出: Point is at (10, 20)

Point p2 = p1;  // p2 是 p1 的副本
p2.X = 30;
p2.Display();  // 输出: Point is at (30, 20)
p1.Display();  // 输出: Point is at (10, 20) （p1 没有改变）
```

在上面的代码中，`p1` 和 `p2` 是两个不同的结构体实例，当 `p2` 改变时，`p1` 并不会受到影响，这就是值类型的特性。

#### 枚举

C#和Java都支持枚举，但C#的枚举更加灵活：

C#:

```csharp
enum Days
{
    Monday,
    Tuesday,
    Wednesday,
    Thursday,
    Friday,
    Saturday,
    Sunday
}

// 可以指定底层类型和值
enum Status : byte
{
    Inactive = 0,
    Active = 1,
    Suspended = 2
}
```

在 C# 中定义枚举类型时，每个枚举成员的值是按照顺序自动分配的，除非显式地为它们指定值。根据 C# 的枚举定义规则，如果一个枚举成员没有显式地给出值，那么它的值会是前一个枚举成员值加 1。

```csharp
using System;

public class EnumTest
{
    enum Day { Sun, Mon, Tue, Wed, Thu, Fri, Sat };
    
    // 可以指定底层类型和值
    enum Status : byte
    {
        Inactive = 0,
        Active = 1,
        Suspended //值默认是2
    }

    static void Main()
    {
        int x = (int)Day.Sun;
        int y = (int)Day.Fri;
        Console.WriteLine("Sun = {0}", x);
        Console.WriteLine("Fri = {0}", y);
    }
}
```

Java:

```java
enum Days {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY
}
```

C#的枚举可以指定底层类型，而Java的枚举实际上是特殊的类。

#### 类型转换

C#提供了多种类型转换方法：

1.  隐式转换：

    ```csharp
    int x = 10;
    long y = x; // 隐式转换，不需要显式转换
    ```

2.  显式转换（强制类型转换）：

    ```csharp
    doubled = 3.14;
    int i = (int)d; // 显式转换，可能会损失精度
    ```

3.  使用Convert类：

    ```csharp
    string s = "123";
    int i = Convert.ToInt32(s);
    ```

4.  使用Parse方法：

    ```csharp
    string s = "3.14";
    double d = double.Parse(s);
    ```

5.  TryParse方法（安全转换）：

    ```csharp
    string s = "123";
    int result;
    if (int.TryParse(s, out result))
    {
    Console.WriteLine($"Converted value: {result}");
    }
    else
    {
    Console.WriteLine("Conversion failed");
    }
    ```

Java的类型转换相对简单一些，主要依赖于强制类型转换和包装类的方法。

#### 小结

虽然C#和Java在变量和数据类型方面有很多相似之处，但C#提供了更多的选择和灵活性。C#的可空类型、更丰富的基本数据类型、更灵活的枚举和方便的类型转换方法，都为开发者提供了更多的工具来处理各种数据场景。

作为一个从Java转向C#的开发者，你会发现这些额外的特性可以让你的代码更加简洁和表达力更强。在实际编程中，合理利用这些特性可以提高代码的可读性和性能。

在接下来的学习中，我们将深入探讨C#的更多高级特性，如属性、索引器、泛型等。这些概念将进一步展示C#相对于Java的独特优势。继续保持学习的热情，你会发现C#是一个功能丰富、富有表现力的语言！

## 3 运算符和表达式

C#的运算符和表达式与Java有很多相似之处，但也有一些独特的特性。让我们深入了解C#的运算符和表达式，并与Java进行比较。

#### 算术运算符

C#和Java的算术运算符基本相同：

- 加法 \(+\)
- 减法 \(-\)
- 乘法 \(\*\)
- 除法 \(/\)
- 取模 \(\%\)

示例：

```csharp
int a = 10, b = 3;
int sum = a + b;      // 13
int difference = a - b; // 7
int product = a * b;   // 30
int quotient = a / b;  // 3 (整数除法)
int remainder = a % b; // 1
```

注意：C#和Java在整数除法时都会舍去小数部分，如果要得到精确结果，至少有一个操作数应该是浮点数。

#### 赋值运算符

C#和Java的赋值运算符也基本相同：

- 简单赋值 \(=\)
- 复合赋值 \(+=, -=, \*=, /=, \%=\)

C#特有的复合赋值运算符：

- \?\?= \(空合并赋值运算符，C# 8.0引入\)

示例：

```csharp
int x = 5;
x += 3;  // 等同于 x = x + 3
x -= 2;  // 等同于 x = x - 2

string name = null;
name ??= "John";  // 如果name为null，赋值为"John"
```

#### 比较运算符

C#和Java的比较运算符完全相同：

- 等于 \(==\)
- 不等于 \(\!=\)
- 大于 \(>\)
- 小于 \(\<\)
- 大于等于 \(>=\)
- 小于等于 \(\<=\)

示例：

```csharp
int a = 5, b = 7;
bool isEqual = (a == b);// false
bool isNotEqual = (a !=b); // true
bool isGreater = (a > b);// false
bool isLess = (a < b);     // true
bool isGreaterOrEqual = (a >= b); // false
bool isLessOrEqual = (a <= b);// true
```

#### 逻辑运算符

C#和Java的逻辑运算符也是相同的：

- 逻辑与 \(\&\&\)
- 逻辑或 \(||\)
- 逻辑非 \(\!\)

示例：

```csharp
bool a = true, b = false;
bool andResult = a && b;  // false
bool orResult = a || b;   // true
bool notResult = !a;      // false
```

#### 位运算符

C#和Java的位运算符也基本相同：

- 按位与 \(\&\)
- 按位或 \(|\)
- 按位异或 \(\^\)
- 按位取反 \(\~\)
- 左移 \(\<\<\)
- 右移 \(>>\)

C#特有的位运算符：

- 无符号右移 \(>>>\)

示例：

```csharp
int a = 60;// 二进制: 0011 1100
int b = 13;  // 二进制: 0000 1101

int c = a & b;  // 12(二进制: 0000 1100)
int d = a | b;  // 61 (二进制: 0011 1101)
int e = a ^ b;  // 49 (二进制: 0011 0001)
int f = ~a;     // -61 (二进制: 1100 0011, 补码表示)
int g = a << 2; // 240 (二进制: 1111 0000)
int h = a >> 2; // 15 (二进制: 0000 1111)
```

#### 条件运算符

C#和Java都有三元条件运算符：

```csharp
int a = 10, b = 20;
int max = (a > b) ? a : b;  // 20
```

C#特有的条件运算符：

- 空合并运算符 \(??\)：空合并运算符用于在变量为 `null` 时提供一个默认值。如果运算符左侧的值不为 `null`，则返回该值；如果为 `null`，则返回运算符右侧的值。
- 空条件运算符\(?.\)：空条件运算符用于在访问对象的成员（如属性或方法）时进行 `null` 检查。如果对象不为 `null`，则继续访问该成员；如果对象为 `null`，则返回 `null`，而不会抛出 `NullReferenceException`。

示例：

```csharp
string name = null;
string displayName = name ?? "Guest";  // "Guest"

class Person
{
    public string Name { get; set; }
}

Person person = null;
int? nameLength = person?.Name?.Length;  // null
```

#### 类型测试运算符

C#提供了一些Java中没有的类型测试运算符：

- is 运算符：检查对象是否与特定类型兼容
- as 运算符：执行类型转换，如果转换失败，返回null

示例：

```csharp
object obj = "Hello";
if (obj is string)
{
    Console.WriteLine("obj is a string");
}

string str = obj as string;
if (str != null)
{
    Console.WriteLine($"The string is: {str}");
}
```

#### Lambda 表达式

C#和Java都支持Lambda表达式，但语法略有不同：

C#:

```csharp
Func<int, int> square = x => x * x;
int result = square(5);  // 25
```

Java:

```java
Function<Integer, Integer> square = x -> x * x;
int result = square.apply(5);  // 25
```

#### 表达式体成员 \(Expression-bodied members\)

C#允许使用更简洁的语法来定义属性和方法：

```csharp
public class Circle
{
    public double Radius { get; set; }
    public double Diameter => Radius * 2;
    public double CalculateArea() => Math.PI * Radius * Radius;
}
```

这种语法在Java中是不存在的。

#### 字符串插值

C#提供了非常方便的字符串插值语法：

```csharp
string name = "Alice";
int age = 30;
string message = $"My name is {name} and I am {age} years old.";
```

Java在较新的版本中也引入了类似的功能，但语法不同：

```java
String name = "Alice";
int age = 30;
String message = String.format("My name is %s and I am %d years old.", name, age);
```

#### 小结

虽然C#和Java在运算符和表达式方面有很多相似之处，但C#提供了一些额外的特性，如空合并运算符、空条件运算符、表达式体成员等，这些可以让代码更加简洁和表达力更强。

作为一个从Java转向C#的开发者，你会发现这些额外的特性可以让你的代码更加优雅和易读。在实际编程中，合理利用这些特性可以提高代码质量和开发效率。

在接下来的学习中，我们将深入探讨C#的更多高级特性，如LINQ、异步编程等。这些概念将进一步展示C#相对于Java的独特优势。继续保持学习的热情，你会发现C#是一个功能丰富、表达力强的语言！

## 4 控制流语句

控制流语句是编程语言的基本构建块，用于控制程序的执行路径。C#和Java在这方面非常相似，但C#也有一些独特的特性。让我们深入了解C#的控制流语句，并与Java进行比较。

#### if-else 语句

C#和Java的if-else语句几乎完全相同：

```csharp
int x = 10;
if (x > 5)
{
    Console.WriteLine("x is greater than 5");
}
else if (x < 5)
{
    Console.WriteLine("x is less than 5");
}
else
{
    Console.WriteLine("x is equal to 5");
}
```

C#特有的特性：

 1.     可空类型的使用：

```csharp
int? x = null;
if (x.HasValue)
{
    Console.WriteLine($"x has a value: {x.Value}");
}
else
{
    Console.WriteLine("x is null");
}
```

 2.     模式匹配（C# 7.0+）：

```csharp
object obj = "Hello";
if (obj is string s)
{
    Console.WriteLine($"The string is: {s}");
}
```

#### switch 语句

C#的switch语句比Java的更加灵活：

```csharp
int day = 3;
switch (day)
{
    case 1:
        Console.WriteLine("Monday");
        break;
    case 2:
        Console.WriteLine("Tuesday");
        break;
    case 3:
    case 4:
    case 5:
        Console.WriteLine("Midweek");
        break;
    default:
        Console.WriteLine("Weekend");
        break;
}
```

C#特有的特性：

 1.     模式匹配（C# 7.0+）：

```csharp
object obj = 123;
switch (obj)
{
    case int i when i > 100://如果 obj 是一个 int 类型并且值大于 100 
        Console.WriteLine($"Large integer: {i}");
        break;
    case string s:// obj 是否是 string 类型
        Console.WriteLine($"String value: {s}");
        break;
    case null:// obj 是否为 null
        Console.WriteLine("Null value");
        break;
    default:// 如果 obj 不符合以上任何模式，default 分支将被执行
        Console.WriteLine("Unknown type");
        break;
}
```

 2.     switch 表达式（C# 8.0+）：

```csharp
string GetDayType(int day) => day switch
{
    1 => "Monday",
    2 => "Tuesday",
    3 or 4 or 5 => "Midweek",
    _ => "Weekend"
};
```

这里定义了一个方法 `GetDayType`，它接受一个整数参数 `day`，并返回一个字符串。

`day switch` 表示对 `day` 变量进行模式匹配，根据不同的值返回不同的字符串。

#### 循环语句

C#和Java的循环语句非常相似：

 1.     for循环：

```csharp
for (int i = 0; i < 5; i++)
{
    Console.WriteLine(i);
}
```

 2.     while 循环：

```csharp
int i = 0;
while (i < 5)
{
    Console.WriteLine(i);
    i++;
}
```

 3.     do-while 循环：

```csharp
int i = 0;
do
{
    Console.WriteLine(i);
    i++;
} while (i < 5);
```

 4.     foreach 循环：

```csharp
string[] fruits = { "apple", "banana", "cherry" };
foreach (string fruit in fruits)
{
    Console.WriteLine(fruit);
}
```

C#特有的特性：

 1.     LINQ与foreach的结合：

```csharp
List<int> numbers = new List<int> { 1, 2, 3, 4, 5 };
foreach (var num in numbers.Where(n => n % 2 == 0))
{
    Console.WriteLine(num);
}
```

#### 跳转语句

C#和Java都支持以下跳转语句：

1.  break：跳出当前循环或switch语句
2.  continue：跳过当前循环的剩余部分，开始下一次迭代
3.  return：从方法中返回，并可选择返回一个值

C#特有的跳转语句：

 1.     goto：虽然不推荐使用，但C#保留了goto语句

```csharp
int i = 0;
start:
    if (i < 5)
    {
        Console.WriteLine(i);
        i++;
        goto start;
    }
```

#### 异常处理

C#和Java的异常处理机制非常相似：

```csharp
try
{
    int result = 10 / 0;
}
catch (DivideByZeroException ex)
{
    Console.WriteLine($"Division by zero error: {ex.Message}");
}
catch (Exception ex)
{
    Console.WriteLine($"An error occurred: {ex.Message}");
}
finally
{
    Console.WriteLine("This always executes");
}
```

C#特有的特性：

 1.     异常过滤器（C# 6.0+）：

```csharp
try
{
    // 可能抛出异常的代码
}
catch (Exception ex) when (ex.InnerException != null)
{
    Console.WriteLine($"Inner exception: {ex.InnerException.Message}");
}
```

 2.     using 语句（简化资源管理）：

```csharp
using (var file = new System.IO.StreamReader("file.txt"))
{
    string content = file.ReadToEnd();
    Console.WriteLine(content);
}
// file自动关闭
```

 3.     using 声明（C# 8.0+）：

```csharp
using var file = new System.IO.StreamReader("file.txt");
string content = file.ReadToEnd();
Console.WriteLine(content);
// file 在作用域结束时自动关闭
```

#### 小结

虽然C#和Java在控制流语句方面有很多相似之处，但C#提供了一些额外的特性，如模式匹配、switch表达式、异常过滤器等，这些可以让代码更加简洁和表达力更强。

作为一个从Java转向C#的开发者，你会发现这些额外的特性可以让你的代码更加优雅和易读。特别是模式匹配和switch表达式，它们可以大大简化复杂的条件逻辑。

在实际编程中，合理利用这些特性可以提高代码质量和开发效率。例如，使用模式匹配可以使类型检查和转换更加简洁，使用switch表达式可以使复杂的条件判断更加清晰。

在接下来的学习中，我们将深入探讨C#的更多高级特性，如LINQ、异步编程等。这些概念将进一步展示C#相对于Java的独特优势。继续保持学习的热情，你会发现C#是一个功能丰富、表达力强的语言！

## 5 方法和参数

方法（在Java中称为函数）是编程中最基本的代码组织单元。C#和Java在方法定义和使用上有很多相似之处，但C#提供了一些额外的特性，使得方法定义和调用更加灵活。让我们深入探讨C#的方法和参数，并与Java进行比较。

#### 方法定义

C#和Java的基本方法定义非常相似：

```csharp
public int Add(int a, int b)
{
    return a + b;
}
```

Java中的等效代码：

```java
public int add(int a, int b) {
    return a + b;
}
```

主要区别：

1.  C#方法名通常使用PascalCase，而Java使用camelCase。
2.  C#支持方法重载，Java也支持。

#### 参数传递

C#和Java都支持值传递和引用传递，但C#提供了更多选项：

 1.     值参数（默认）：

```csharp
public void IncrementValue(int x)
{
    x++; // 不影响原始值
}
```

 2.     引用参数（ref 关键字）：

```csharp
public void IncrementRef(ref int x)
{
    x++; // 修改原始值
}

// 调用
int num = 5;
IncrementRef(ref num);
Console.WriteLine(num); // 输出 6
```

Java没有直接等效的引用参数，但可以通过包装类或数组实现类似效果。

 3.     输出参数（out 关键字）：

```csharp
public bool TryParse(string s, out int result)
{
    return int.TryParse(s, out result);
}

// 调用
if (TryParse("123", out int number))
{
    Console.WriteLine($"Parsed number: {number}");
}
```

Java没有直接等效的输出参数。

 4.     参数数组（params 关键字）：

```csharp
public int Sum(params int[] numbers)
{
    return numbers.Sum();
}

// 调用
int total = Sum(1, 2, 3, 4, 5);
```

Java使用可变参数（varargs）实现类似功能：

```java
public int sum(int... numbers) {
    return Arrays.stream(numbers).sum();
}
```

#### 方法重载

C#和Java都支持方法重载，允许在同一个类中定义多个同名但参数列表不同的方法：

```csharp
public class Calculator
{
    public int Add(int a, int b)
    {
        return a + b;
    }

    public double Add(double a, double b)
    {
        return a + b;
    }
}
```

Java的方法重载与C#基本相同。

#### 可选参数

C#支持可选参数，这在Java中直到最近才引入：

```csharp
public void Greet(string name, string greeting = "Hello")
{
    Console.WriteLine($"{greeting}, {name}!");
}

// 调用
Greet("Alice"); // 输出: Hello, Alice!
Greet("Bob", "Hi"); // 输出: Hi, Bob!
```

在Java中，你通常需要使用方法重载来实现类似功能：

```java
public void greet(String name) {
    greet(name, "Hello");
}

public void greet(String name, String greeting) {
    System.out.println(greeting + ", " + name + "!");
}
```

#### 命名参数

C#支持命名参数，可以提高代码的可读性：

```csharp
public void CreateUser(string name, int age, bool isAdmin = false)
{
    //方法实现
}

// 调用
CreateUser(name: "Alice", age: 30, isAdmin: true);
CreateUser(age: 25, name: "Bob"); // 可以改变参数顺序
```

Java不支持命名参数，但可以使用建造者模式来实现类似的效果。

#### 表达式体方法

C# 6.0引入了表达式体方法，可以使简单方法的定义更加简洁：

```csharp
public int Add(int a, int b) => a + b;

public string GetFullName(string firstName, string lastName) => $"{firstName} {lastName}";
```

Java不支持这种语法糖。

#### 本地函数

C# 7.0引入了本地函数，允许在方法内定义函数：

```csharp
public int Factorial(int n)
{
    int LocalFactorial(int x)
    {
        return x <= 1 ? 1 : x * LocalFactorial(x - 1);
    }

    return LocalFactorial(n);
}
```

Java不直接支持本地函数，但可以使用匿名内部类或lambda表达式来实现类似功能。

#### 异步方法

C#对异步编程的支持非常强大，使用async和await关键字：

```csharp
public async Task<string> FetchDataAsync(string url)
{
    using var client = new HttpClient();
    return await client.GetStringAsync(url);
}

// 调用
string data = await FetchDataAsync("https://api.example.com");
```

Java也支持异步编程，但语法和使用方式与C#不同，通常使用CompletableFuture：

```java
public CompletableFuture<String> fetchDataAsync(String url) {
    return CompletableFuture.supplyAsync(() -> {
        // 使用HttpClient获取数据
        return "data";
    });
}

// 调用
String data = fetchDataAsync("https://api.example.com").join();
```

#### 扩展方法

C#允许你为现有类型添加新方法，而不需要修改原始类型的定义：

```csharp
public static class StringExtensions
{
    public static bool IsNullOrEmpty(this string str)
    {
        return string.IsNullOrEmpty(str);
    }
}

// 使用
string name = "Alice";
bool isEmpty = name.IsNullOrEmpty();
```

Java不支持扩展方法，但可以使用静态工具类来实现类似功能。

#### 泛型方法

C#和Java都支持泛型方法，允许你编写可以处理多种类型的方法：

```csharp
public T Max<T>(T a, T b) where T : IComparable<T>
{
    return a.CompareTo(b) > 0 ? a : b;
}

// 使用
int maxInt = Max(5, 10);
string maxString = Max("apple", "banana");
```

Java的泛型方法语法略有不同：

```java
public <T extends Comparable<T>> T max(T a, T b) {
    return a.compareTo(b) > 0 ? a : b;
}
```

#### 方法组合与函数式编程

C#对函数式编程有很好的支持，可以轻松组合和传递方法：

```csharp
Func<int, int> square = x => x * x;
Func<int, int> addOne = x => x + 1;

Func<int, int> squareThenAddOne = x => addOne(square(x));

int result = squareThenAddOne(5); // 26
```

Java也支持函数式编程，但语法略有不同：

```java
Function<Integer, Integer> square = x -> x * x;
Function<Integer, Integer> addOne = x -> x + 1;

Function<Integer, Integer> squareThenAddOne = square.andThen(addOne);

int result = squareThenAddOne.apply(5); // 26
```

#### 小结

虽然C#和Java在方法和参数的基本概念上很相似，但C#提供了更多的特性和灵活性。C#的引用参数、输出参数、命名参数、可选参数等特性可以让方法定义和调用更加灵活和清晰。此外，C#的异步方法、扩展方法和表达式体方法等特性可以让代码更加简洁和易读。

作为一个从Java转向C#的开发者，你会发现这些额外的特性可以大大提高你的编程效率和代码质量。例如，命名参数和可选参数可以减少方法重载的需求，扩展方法可以让你更容易地扩展现有类型的功能，而async/await则可以大大简化异步编程的复杂性。

在实际编程中，合理利用这些特性可以让你的代码更加清晰、简洁和易于维护。例如，使用命名参数可以提高代码的可读性，使用扩展方法可以使你的代码更加模块化，而使用异步方法可以提高应用程序的响应性。

随着你对C#的深入学习，你会发现更多强大的特性和用法。保持学习和实践的热情，你将能够充分利用C#的强大功能，成为一个高效的.NET开发者！

## 6 类和对象

类和对象是面向对象编程的核心概念，C#和Java在这方面有很多相似之处，但C#提供了一些额外的特性和语法糖，使得类的定义和使用更加灵活和简洁。让我们深入探讨C#的类和对象，并与Java进行比较。

#### 类的定义

C#和Java的基本类定义非常相似：

```csharp
public class Person
{
    public string Name { get; set; }
    public int Age { get; set; }

    public void SayHello()
    {
        Console.WriteLine($"Hello, my name is {Name} and I'm {Age} years old.");
    }
}
```

Java中的等效代码：

```java
public class Person {
    private String name;
    private int age;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public void sayHello() {
        System.out.println("Hello, my name is " + name + " and I'm " + age + " years old.");
    }
}
```

主要区别：

1.  C#使用属性（Properties）代替了Java的getter和setter方法。
2.  C#的方法名通常使用PascalCase，而Java使用camelCase。

#### 构造函数

C#和Java的构造函数定义类似：

```csharp
public class Person
{
    public string Name { get; set; }
    public int Age { get; set; }

    public Person(string name, int age)
    {
        Name = name;
        Age = age;
    }
}
```

C#特有的特性：

1. 主构造函数（C# 9.0+）

   ```csharp
   public class Person(string name, int age)
   {
       public string Name { get; set; } = name;
       public int Age { get; set; } = age;
   }
   ```

2. Record类型（C# 9.0+）

   C# 9.0引入了Record类型，用于创建不可变的引用类型。

   **主要特性**

   1. **不可变性**：`record` 类型默认是不可变的。虽然你可以手动配置它们为可变，但 `record` 的主要设计目的是创建不可变数据结构。
   2. **值相等性**：`record` 类型的比较是基于值的，而不是基于引用的。这意味着两个具有相同值的 `record` 对象被认为是相等的，即使它们是不同的实例。
   3. **简化的语法**：`record` 提供了简化的语法，可以快速创建包含属性的类型，特别适合用于数据传输。

   ```csharp
   public record Person(string Name, int Age);
   ```

   这个定义相当于创建了一个包含 `Name` 和 `Age` 属性的不可变类，同时自动实现了 `Equals`、`GetHashCode` 和 `ToString` 方法。

   ```csharp
   var person1 = new Person("Alice", 30);
   var person2 = new Person("Alice", 30);
   
   Console.WriteLine(person1 == person2);  // 输出: True (因为值相等)
   Console.WriteLine(person1);  // 输出: Person { Name = Alice, Age = 30 }
   ```

   `record` 类型支持 `with` 表达式，允许你基于现有对象创建新对象，同时只修改某些属性。

   ```csharp
   var person3 = person1 with { Age = 31 }; // 只允许修改Age
   
   Console.WriteLine(person1);  // 输出: Person { Name = Alice, Age = 30 }
   Console.WriteLine(person3);  // 输出: Person { Name = Alice, Age = 31 }
   
   ```

   Java 14+引入了类似的Record特性：

   ```java
   public record Person(String name, int age) {}
   ```


#### 属性

C#的属性是一个强大的特性，可以替代Java中的getter和setter方法：

```csharp
public class Person
{
    private string name;
    public string Name
    {
        get { return name; }
        set { name = value; }
    }

    // 自动实现的属性
    public int Age { get; set; }

    // 只读属性
    // => 是表达式体成员语法，用于简化只有一个表达式的属性或方法的定义。
	// Age >= 18 是属性的计算逻辑，表示如果 Age 的值大于或等于18
    public bool IsAdult => Age >= 18;
}
```

C# 6.0+引入了更简洁的属性语法：

```csharp
public class Person
{
    public string Name { get; set; } = "John Doe";
    
    public int Age { get; set; }
    
    public bool IsAdult => Age >= 18;
}
```

#### 静态成员

C#和Java都支持静态成员：

```csharp
public class MathHelper
{
    public static double PI = 3.14159;

    public static int Add(int a, int b)
    {
        return a + b;
    }
}

// 使用
double pi = MathHelper.PI;
int sum = MathHelper.Add(5, 3);
```

#### 匿名类型

C#支持匿名类型，可以快速创建简单的对象：

```csharp
var person = new { Name = "Alice", Age = 30 };
Console.WriteLine($"{person.Name} is {person.Age} years old");
```

Java也支持匿名类，但主要用于创建接口或抽象类的匿名实现。

#### 对象初始化器

C#支持对象初始化器，可以在创建对象时直接设置属性：

```csharp
var person = new Person
{
    Name = "Alice",
    Age = 30
};
```

Java不直接支持这种语法，通常使用建造者模式来实现类似效果。

#### 部分类（Partial Classes）

C#支持部分类，允许将一个类的定义分散到多个文件中：

```csharp
// File1.cs
public partial class MyClass
{
    public void Method1() { }
}

// File2.cs
public partial class MyClass
{
    public void Method2() { }
}
```

Java不支持部分类的概念。

#### 索引器（Indexers）

C#支持索引器，允许类像数组一样通过索引访问：

```csharp
public class SampleCollection<T>
{
    private T[] arr = new T[100]; // 假设内部存储是一个数组

    public T this[int index]   // 定义索引器
    {
        get
        {
            return arr[index];
        }
        set
        {
            arr[index] = value;
        }
    }
}

// 使用
SampleCollection<string> stringCollection = new SampleCollection<string>();
// 使用索引器设置值
stringCollection[0] = "Hello";
stringCollection[1] = "World";
// 使用索引器获取值
Console.WriteLine(stringCollection[0]);  // 输出 "Hello"
Console.WriteLine(stringCollection[1]);  // 输出 "World"
```

用`List`集合而不是数组来实现索引器

```csharp
public class SampleCollection<T>
{
    private List<T> list = new List<T>(); // 使用List集合

    public T this[int index]   // 定义索引器
    {
        get
        {
            return list[index];
        }
        set
        {
            // 检查index是否超出当前List的范围
            if (index >= list.Count)
            {
                // 如果index超出范围，扩展List的大小
                for (int i = list.Count; i <= index; i++)
                {
                    list.Add(default(T));  // 添加默认值
                }
            }
            list[index] = value;
        }
    }
}

```

Java没有直接等效的特性，通常需要定义专门的get和set方法。

#### 运算符重载

C#允许为自定义类型定义运算符的行为：

```csharp
public struct Complex
{
    public double Real { get; set; }
    public double Imaginary { get; set; }

    public static Complex operator +(Complex c1, Complex c2)
    {
        return new Complex 
        { 
            Real = c1.Real + c2.Real,Imaginary = c1.Imaginary + c2.Imaginary
        };
    }
}

// 使用
var c1 = new Complex { Real = 1, Imaginary = 2 };
var c2 = new Complex { Real = 3, Imaginary = 4 };
var result = c1 + c2;
```

Java不支持运算符重载。

#### 嵌套类型

C#和Java都支持嵌套类型，但C#的访问规则更加灵活：

```csharp
public class OuterClass
{
    private int outerField = 10;

    public class InnerClass
    {
        public void AccessOuterField(OuterClass outer)
        {
            Console.WriteLine(outer.outerField);
        }
    }
}
```

在C#中，嵌套类可以访问外部类的私有成员，而在Java中，内部类需要外部类的实例才能访问其私有成员。

#### 析构函数

析构函数是C#中类的一种特殊方法，用于在对象被垃圾回收器回收之前执行清理操作。**析构函数没有参数、没有返回类型，名字与类名相同，并且前面加上一个波浪号（`~`）。析构函数通常用于释放非托管资源，例如文件句柄、数据库连接等。**

**析构函数的特点：**

- **由垃圾回收器自动调用，开发者无法直接调用析构函数。**
- 不能被重载。
- **不保证立即执行，垃圾回收器何时回收对象是不确定的。**
- 不能有`public`, `private`, `protected`, 或 `internal`修饰符。

```csharp
using System;
namespace LineApplication
{
   class Line
   {
      private double length;   // 线条的长度
      public Line()  // 构造函数
      {
         Console.WriteLine("对象已创建");
      }
      ~Line() //析构函数
      {
         Console.WriteLine("对象已删除");
      }

      public void setLength( double len )
      {
         length = len;
      }
      public double getLength()
      {
         return length;
      }

      static void Main(string[] args)
      {
         Line line = new Line();
         // 设置线条长度
         line.setLength(6.0);
         Console.WriteLine("线条的长度： {0}", line.getLength());  
      }
   }
}
```

#### 属性访问器的可访问性

C#允许为属性的getter 和 setter 单独设置访问级别：

```csharp
public class Person
{
    public string Name { get; private set; }

    public Person(string name)
    {
        Name = name;
    }
}
```

Java不支持这种细粒度的访问控制。

#### [init only setters（C#9.0+）](https://learn.microsoft.com/zh-cn/dotnet/csharp/language-reference/keywords/init)

C#9.0引入了`init only setters`，允许在对象初始化时设置属性值，而之后这些属性是不可变的：

```csharp
var circle = new Circle { Radius = 5 };
// circle.Radius is 5
// circle.Diameter is 10 (2 * 5)

var bigCircle = new Circle { Diameter = 20 };
// bigCircle.Diameter is 20
// bigCircle.Radius is 10 (20 / 2)


public class Circle
{
    private double _radius;

    public double Radius
    {
        get => _radius;
        init => _radius = value;
    }

    public double Diameter
    {
        get => 2 * _radius;
        init => _radius = value / 2;
    }
}
```

Java没有直接等效的特性。

#### 顶级语句（C# 9.0+）

从C# 9.0开始，可以在文件级别直接编写代码，而不需要显式的Main方法：

```csharp
Console.WriteLine("Hello, World!");
```

这个特性简化了小型程序和脚本的编写。Java仍然需要一个包含main方法的类。

#### 模式匹配（C# 7.0+）

C#支持高级的模式匹配，可以在switch语句和is表达式中使用：

```csharp
object obj = "Hello";

if (obj is string s && s.Length > 5)
{
    Console.WriteLine($"It's a long string: {s}");
}

var result = obj switch
{
    string s => $"It's a string: {s}",
    int i => $"It's an int: {i}",
    _ => "It's something else"
};
```

Java支持有限形式的模式匹配（从Java 14开始），但不如C#灵活。

#### 小结

C#提供了丰富的特性来定义和使用类和对象，许多这些特性在Java中是没有直接等价物的。这些特性不仅可以让代码更加简洁和表达力更强，还可以提高开发效率和代码质量。

作为一个从Java转向C#的开发者，你会发现这些额外的特性可以让你以新的方式思考和组织代码。例如，索引器可以让你的自定义类型像数组一样使用，运算符重载可以让你的类型更自然地参与数学运算，而模式匹配则可以简化复杂的类型检查和转换逻辑。

在实际编程中，合理利用这些特性可以大大提高代码的可读性和可维护性。例如，使用属性可以简化数据封装，使用记录类型可以简化不可变数据模型的创建，而使用模式匹配可以使复杂的条件逻辑更加清晰。

随着你对C#的深入学习和实践，你会发现更多强大的特性和用法。保持学习和实践的热情，你将能够充分利用C#的强大功能，成为一个高效的.NET开发者！记住，编程语言只是工具，关键是要理解背后的概念和原理，并能够在实际问题中灵活应用这些知识。

## 7 继承和多态

继承和多态是面向对象编程的核心概念，C#和Java在这方面有许多相似之处，但C#提供了一些额外的特性和语法，使得继承和多态的实现更加灵活和强大。让我们深入探讨C#的继承和多态，并与Java进行比较。

#### 基本继承

C#和Java的基本继承语法略有不同：

C#:

```csharp
public class Animal
{
    public string Name { get; set; }
    public virtual void MakeSound()
    {
        Console.WriteLine("The animal makes a sound");
    }
}

public class Dog : Animal
{
    public override void MakeSound()
    {
        Console.WriteLine("The dog barks");
    }
}
```

Java:

```java
public class Animal {
    private String name;
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public void makeSound() {
        System.out.println("The animal makes a sound");
    }
}

public class Dog extends Animal {
    @Override
    public void makeSound() {
        System.out.println("The dog barks");
    }
}
```

主要区别：

1.  C#使用冒号\(`:`\)表示继承，Java使用`extends`关键字。
2.  C#需要使用`virtual`和`override`关键字来实现方法重写，Java只需要使用`@Override`注解。
3.  C#默认使用属性（Properties）而不是getter和setter方法。

#### 构造函数和继承

在C#中，派生类的构造函数可以显式调用基类的构造函数：

```csharp
public class Animal
{
    public string Name { get; set; }
    public Animal(string name)
    {
        Name = name;
    }
}

public class Dog : Animal
{
    public string Breed { get; set; }
    public Dog(string name, string breed) : base(name)
    {
        Breed = breed;
    }
}
```

Java中的等效代码：

```java
public class Animal {
    private String name;
    public Animal(String name) {
        this.name = name;
    }
}

public class Dog extends Animal {
    private String breed;
    public Dog(String name, String breed) {
        super(name);
        this.breed = breed;
    }
}
```

在Java/C#中，派生类的构造函数在调用基类的构造函数时，如果不显式调用基类的构造函数，编译器会隐式地调用基类的无参数构造函数。如果基类没有无参数的构造函数，而派生类没有显式调用基类的其他构造函数，编译就会失败。

#### 阻止继承

C#使用`sealed`关键字来防止类被继承或方法被重写：

```csharp
public sealed class FinalClass
{
    // 这个类不能被继承
}

public class BaseClass
{
    public virtual void VirtualMethod() { }
}

public class DerivedClass : BaseClass
{
    public sealed override void VirtualMethod() { }// 这个方法不能在子类中被重写
}
```

Java使用`final`关键字实现类似功能：

```java
public final class FinalClass {
    // 这个类不能被继承
}

public class BaseClass {
    public void virtualMethod() { }
}

public class DerivedClass extends BaseClass {
    @Override
    public final void virtualMethod() { }
    // 这个方法不能在子类中被重写
}
```

#### 隐藏基类成员

C#使用`new`关键字来隐藏基类成员：

在C#中，当派生类中的成员与基类中的成员同名时，派生类可以选择“隐藏”基类的成员。这通常通过在派生类的成员前加上`new`关键字来实现。隐藏基类成员意味着在派生类中访问同名成员时，将使用派生类的版本，而不是基类的版本。

**如果在派生类中定义了与基类同名且签名相同的成员（如方法、属性等），即使不使用`new`关键字，也会隐式地隐藏基类的该成员。编译器会发出警告，提醒你可能无意间隐藏了基类的成员。但这不会影响代码的运行，隐藏行为依然会发生。基类中的成员被virtual修饰，同样可以选择使用new或者不使用new来隐藏基类中的成员（同样会被编译器发出警告）；在派生类中可以使用override来重写基类中的成员。**

```csharp
public class BaseClass
{
    public void Method()
    {
        Console.WriteLine("BaseClass.Method");
    }
}

public class DerivedClass : BaseClass
{
    public new void Method()
    {
        Console.WriteLine("DerivedClass.Method");
    }
}

DerivedClass derived = new DerivedClass();
derived.Display();  // 输出 "DerivedClass Display"

BaseClass baseClass = new DerivedClass();
baseClass.Display();  // 输出 "BaseClass Display"
```

有时你可能想在派生类中隐藏基类的成员，而不是覆盖它。C#使用`new`关键字来实现这一点：

```csharp
public class Base
{
    public virtual void Method()
    {
        Console.WriteLine("Base.Method");
    }
}

public class Derived : Base
{
    public new void Method()
    {
        Console.WriteLine("Derived.Method");
    }
}

Base b = new Derived();
b.Method(); // 输出: Base.Method

Derived d = new Derived();
d.Method(); // 输出: Derived.Method
```

Java没有直接等效的语法，但可以通过重新定义方法来实现类似效果。

#### 基类访问

C#使用`base`关键字来访问基类成员，类似于Java中的`super`：

```csharp
class Program
{
    static void Main(string[] args)
    {

        DerivedClass v1 = new DerivedClass();
        v1.Method();
        // 输出：
        // BaseClass Method
        // Additional behavior in derived class

    }
}

public class BaseClass
{
    public virtual void Method()
    {
        Console.WriteLine("BaseClass Method");
    }

}

public class DerivedClass : BaseClass
{
    public override void Method()
    {
        base.Method(); //调用基类方法
        Console.WriteLine("Additional behavior in derived class");
    }
}
```

#### 构造函数链

C#允许在一个类中定义多个构造函数，并使用`this`关键字链接它们：

```csharp
class Program
{
    static void Main(string[] args)
    {
        var p = new Person("zhangsan");
        Console.WriteLine(p.Age);// 0
    }
}

public class Person
{
    public string Name { get; set; }
    public int Age { get; set; }


    public Person(string Name) : this(Name, 0)
    {
        Name = Name;
    }

    public Person(string Name, int Age)
    {
        Name = Name;
        Age = Age;

    }
}

```

#### 派生类中的新成员

C#允许在派生类中添加新的成员，而不需要特殊语法：

```csharp
public class Animal
{
    public virtual void MakeSound() { }
}

public class Dog : Animal
{
    public override void MakeSound()
    {
        Console.WriteLine("Woof");
    }

    public void Fetch() //新方法
    {
        Console.WriteLine("Dog is fetching");
    }
}
```

#### 继承链中的构造函数调用顺序

在C#中，当创建一个派生类的实例时，构造函数的调用顺序是从最基础的类开始，一直到最派生的类：

在C#中，如果派生类的构造函数没有显式调用基类的构造函数（通过`base(...)`），编译器会自动在派生类的构造函数中插入对基类无参数构造函数的调用。

```csharp
public class A
{
    public A() { Console.WriteLine("A"); }
}

public class B : A
{
    public B() { Console.WriteLine("B"); }
}

public class C : B
{
    public C() { Console.WriteLine("C"); }
}

// 使用
var c = new C(); // 输出: A B C
```

#### 虚方法表（VMT）和动态分发

C#使用虚方法表来实现多态。当你调用一个虚方法时，实际调用的方法是在运行时根据对象的实际类型决定的：

```csharp
public class Animal
{
    public virtual void MakeSound()
    {
        Console.WriteLine("Animal sound");
    }
}

public class Dog : Animal
{
    public override void MakeSound()
    {
        Console.WriteLine("Bark");
    }
}

public class Cat : Animal
{
    // 没有重写MakeSound方法
}


Animal animal1 = new Dog();
animal1.MakeSound(); // 输出: Bark

Animal animal2 = new Cat();
animal2.MakeSound(); // 输出: Animal sound
```

这种机制称为动态分发，它是多态的核心实现方式。

#### 最佳实践

1.  优先使用组合而不是继承：继承创建了强耦合，而组合更灵活。

2.  遵循里氏替换原则（LSP）：子类应该可以替换其基类，而不改变程序的正确性。

3.  谨慎使用密封类和方法：虽然它们可以提高性能和安全性，但也限制了扩展性。

4.  使用接口进行抽象：接口提供了更好的解耦和灵活性。

5.  避免深层继承层次：深层继承可能导致复杂性和维护困难。

6.  合理使用抽象类：当你需要在基类中提供一些实现时，使用抽象类而不是接口。

7.  正确使用virtual和override关键字：这些关键字明确表达了你的意图，使代码更易理解和维护。

#### 虚拟属性和索引器

C#允许将属性和索引器声明为虚拟的，这样它们可以在派生类中被重写：

```csharp
public class Base
{
    public virtual int Property { get; set; }
    public virtual int this[int index]
    {
        get =>0;
        set { }
    }
}

public class Derived : Base
{
    private int[] data = new int[10];

    public override int Property
    {
        get => base.Property;
        set => base.Property = value * 2;
    }

    public override int this[int index]
    {
        get => data[index];
        set => data[index] = value;
    }
}
```

#### 继承链中的构造函数和字段初始化顺序

了解C#中构造函数和字段初始化的顺序非常重要：

1. **基类字段的初始化**：首先，基类的字段初始化器按它们在类中出现的顺序进行初始化。
2. **基类的构造函数**：接下来，基类的构造函数被调用。基类构造函数执行前，派生类无法访问基类的任何成员。
3. **派生类字段的初始化**：在基类构造函数完成之后，派生类的字段初始化器按它们在类中出现的顺序进行初始化。
4. **派生类的构造函数**：最后，派生类的构造函数被调用。

```csharp
public class Base
{
    public int BaseField = 1;
    public Base()
    {
        Console.WriteLine($"Base constructor, BaseField = {BaseField}");
    }
}

public class Derived : Base
{
    public int DerivedField = BaseField + 1;
    public Derived()
    {
        Console.WriteLine($"Derived constructor, DerivedField = {DerivedField}");
    }
}

// 输出:
// Base constructor, BaseField = 1
// Derived constructor, DerivedField = 2
```

#### 小结

C#提供了丰富而灵活的继承和多态机制，允许开发者创建复杂的类层次结构和接口设计。相比Java，C#在某些方面提供了更细粒度的控制和更多的语法糖，如显式接口实现、抽象属性等。

作为一个从Java转向C#的开发者，你会发现这些特性可以帮助你创建更加清晰、灵活和可维护的代码。例如，显式接口实现可以解决方法名冲突问题，抽象属性可以更优雅地定义必须由派生类实现的属性。

然而，重要的是要记住，这些强大的特性也带来了复杂性。在使用继承和多态时，始终要考虑代码的可维护性和可读性。遵循SOLID原则，合理使用组合和继承，避免创建过于复杂的继承层次结构。

通过深入理解和合理使用这些特性，你可以充分利用C#的强大功能，创建出更加灵活、可扩展和易于维护的代码。在实际项目中，根据具体需求和设计目标来选择合适的特性和模式，将帮助你成为一个更出色的C#开发者。

当然，让我们继续深入探讨C#中继承和多态的一些高级概念和最佳实践：

#### 抽象工厂模式

抽象工厂模式是一种创建型设计模式，它使用继承和多态来提供一种创建相关对象家族的方式，而无需指定它们的具体类。这在C#中可以优雅地实现：

```csharp
public interface IButton { void Paint(); }
public interface ICheckbox { void Paint(); }

public interface IGUIFactory
{
    IButton CreateButton();
    ICheckbox CreateCheckbox();
}

public class WinButton : IButton
{
    public void Paint() => Console.WriteLine("Windows Button");
}

public class MacButton : IButton
{
    public void Paint() => Console.WriteLine("Mac Button");
}

public class WinCheckbox : ICheckbox
{
    public void Paint() => Console.WriteLine("Windows Checkbox");
}

public class MacCheckbox : ICheckbox
{
    public void Paint() => Console.WriteLine("Mac Checkbox");
}

public class WinFactory : IGUIFactory
{
    public IButton CreateButton() => new WinButton();
    public ICheckbox CreateCheckbox() => new WinCheckbox();
}

public class MacFactory : IGUIFactory
{
    public IButton CreateButton() => new MacButton();
    public ICheckbox CreateCheckbox() => new MacCheckbox();
}

// 使用
IGUIFactory factory = new WinFactory();
var button = factory.CreateButton();
button.Paint(); //输出: Windows Button
```

#### 模板方法模式

模板方法模式定义了一个算法的骨架，允许子类重新定义算法的某些步骤，而不改变算法的结构：

```csharp
public abstract class DataProcessor
{
    public void ProcessData()
    {
        OpenFile();
        ExtractData();
        ParseData();
        AnalyzeData();
        SendReport();
        CloseFile();
    }

    protected abstract void ExtractData();
    protected abstract void ParseData();
    protected abstract void AnalyzeData();

    protected virtual void OpenFile()
    {
        Console.WriteLine("Opening file...");
    }

    protected virtual void SendReport()
    {
        Console.WriteLine("Sending report...");
    }

    protected virtual void CloseFile()
    {
        Console.WriteLine("Closing file...");
    }
}

public class PDFProcessor : DataProcessor
{
    protected override void ExtractData()
    {
        Console.WriteLine("Extracting data from PDF...");
    }

    protected override void ParseData()
    {
        Console.WriteLine("Parsing PDF data...");
    }

    protected override void AnalyzeData()
    {
        Console.WriteLine("Analyzing PDF data...");
    }
}
```

#### 策略模式

策略模式定义了一系列算法，并使它们可以互相替换。这个模式利用了多态性：

```csharp
public interface ISortStrategy
{
    void Sort(List<int> data);
}

public class BubbleSort : ISortStrategy
{
    public void Sort(List<int> data)
    {
        Console.WriteLine("Performing bubble sort");
        // 实现冒泡排序
    }
}

public class QuickSort : ISortStrategy
{
    public void Sort(List<int> data)
    {
        Console.WriteLine("Performing quick sort");
        // 实现快速排序
    }
}

public class Sorter
{
    private ISortStrategy _strategy;

    public Sorter(ISortStrategy strategy)
    {
        _strategy = strategy;
    }

    public void SetStrategy(ISortStrategy strategy)
    {
        _strategy = strategy;
    }

    public void SortData(List<int> data)
    {
        _strategy.Sort(data);
    }
}

// 使用
var sorter = new Sorter(new BubbleSort());
sorter.SortData(new List<int> { 3, 1, 4, 1, 5, 9});
sorter.SetStrategy(new QuickSort());
sorter.SortData(new List<int> { 3, 1, 4, 1, 5, 9 });
```

#### 装饰器模式

装饰器模式允许你动态地给对象添加新的行为，而不改变其结构：

```csharp
public interface ICoffee
{
    string GetDescription();
    double GetCost();
}

public class SimpleCoffee : ICoffee
{
    public string GetDescription() => "Simple Coffee";
    public double GetCost() => 1;
}

public abstract class CoffeeDecorator : ICoffee
{
    protected ICoffee _coffee;

    public CoffeeDecorator(ICoffee coffee)
    {
        _coffee = coffee;
    }

    public virtual string GetDescription() => _coffee.GetDescription();
    public virtual double GetCost() => _coffee.GetCost();
}

public class MilkDecorator : CoffeeDecorator
{
    public MilkDecorator(ICoffee coffee) : base(coffee) { }

    public override string GetDescription() => $"{base.GetDescription()}, Milk";
    public override double GetCost() => base.GetCost() + 0.5;
}

public class SugarDecorator : CoffeeDecorator
{
    public SugarDecorator(ICoffee coffee) : base(coffee) { }

    public override string GetDescription() => $"{base.GetDescription()}, Sugar";
    public override double GetCost() => base.GetCost() + 0.2;
}

// 使用
ICoffee coffee = new SimpleCoffee();
coffee = new MilkDecorator(coffee);
coffee = new SugarDecorator(coffee);
Console.WriteLine($"Description: {coffee.GetDescription()}");
Console.WriteLine($"Cost: ${coffee.GetCost()}");
```

#### 使用协变和逆变优化接口设计

协变和逆变可以使得泛型接口更加灵活：

```csharp
// 协变接口
public interface IProducer<out T>
{
    T Produce();
}

// 逆变接口
public interface IConsumer<in T>
{
    void Consume(T item);
}

public class Animal { }
public class Dog : Animal { }

public class DogProducer : IProducer<Dog>
{
    public Dog Produce() => new Dog();
}

public class AnimalConsumer : IConsumer<Animal>
{
    public void Consume(Animal animal) { }
}

// 使用
IProducer<Animal> animalProducer = new DogProducer(); // 协变
IConsumer<Dog> dogConsumer = new AnimalConsumer(); // 逆变
```

#### 使用接口隔离原则（ISP）

接口隔离原则建议将大接口分割成更小、更具体的接口：

```csharp
// 不好的设计
public interface IWorker
{
    void Work();
    void Eat();void Sleep();
}

// 更好的设计
public interface IWorkable
{
    void Work();
}

public interface IEatable
{
    void Eat();
}

public interface ISleepable
{
    void Sleep();
}

public class Human : IWorkable, IEatable, ISleepable
{
    public void Work() { }
    public void Eat() { }
    public void Sleep() { }
}

public class Robot : IWorkable
{
    public void Work() { }
}
```

#### 使用组合优于继承

虽然继承是一个强大的工具，但它也可能导致紧耦合。

在许多情况下，组合可能是一个更好的选择：

```csharp
// 使用继承
public class Bird
{
    public virtual void Fly()
    {
        Console.WriteLine("Flying...");
    }
}

public class Penguin : Bird
{
    public override void Fly()
    {
        throw new NotSupportedException("Penguins can't fly");
    }
}

// 使用组合
public interface IFlyable
{
    void Fly();
}

public class FlyingBehavior : IFlyable
{
    public void Fly()
    {
        Console.WriteLine("Flying...");
    }
}

public class Bird
{
    private IFlyable _flyingBehavior;

    public Bird(IFlyable flyingBehavior)
    {
        _flyingBehavior = flyingBehavior;
    }

    public void Fly()
    {
        _flyingBehavior.Fly();
    }
}

public class Penguin : Bird
{
    public Penguin() : base(new NullFlyingBehavior()) { }
}

public class NullFlyingBehavior : IFlyable
{
    public void Fly() { } // Do nothing
}
```

#### 小结

这些高级概念和模式展示了如何在实际应用中利用C#的继承和多态特性来创建灵活、可扩展的代码。设计模式如抽象工厂、策略、装饰器等，都是继承和多态的具体应用，可以帮助你解决常见的设计问题。

同时，我们也看到了一些重要的设计原则，如接口隔离原则和组合优于继承。这些原则可以帮助你创建更加模块化、易于维护的代码。

在实际开发中，选择使用继承还是组合，或者选择哪种设计模式，都需要根据具体的问题和上下文来决定。重要的是要理解这些概念背后的原理，并能够灵活地应用它们。

作为一个从Java转向C#的开发者，你会发现这些概念在两种语言中都是适用的，但C#提供了一些额外的特性（如显式接口实现、扩展方法等）可以让这些模式的实现更加优雅。

继续深入学习和实践这些概念，你将能够设计出更加健壮、灵活的软件系统，成为一个更优秀的C#开发者。

## 8 接口和抽象类

接口和抽象类都是C#中用于定义抽象类型的重要工具，但它们有不同的用途和特性。让我们详细探讨这两个概念，并比较它们与Java中对应概念的异同。

#### 接口（Interfaces）

接口在C#中定义了一组方法、属性、事件或索引器的签名，但不提供实现。它们用于定义一个类应该具有的能力，而不关心这些能力如何实现。

##### C#中接口的基本语法：

```csharp
public interface IAnimal
{
    string Name { get; set; }
    void MakeSound();
}

public class Dog : IAnimal
{
    public string Name { get; set; }
    public void MakeSound()
    {
        Console.WriteLine("Woof!");
    }
}
```

##### C# 8.0中的接口新特性：

 1.     默认实现：

```csharp
public interface IAnimal
{
    string Name { get; set; }
    void MakeSound();
    void Eat() => Console.WriteLine($"{Name} is eating.");
}
```

 2.     静态成员：

```csharp
public interface IAnimal
{
    static int Count { get; set; }
    static void IncrementCount() => Count++;
}
```

 3.     私有成员：

```csharp
public interface IAnimal
{
    private static int count;
    public static int Count => count;static void IncrementCount() => count++;
}
```

##### 与Java的比较：

Java8引入了接口默认方法，类似于C# 8.0的默认实现。但C#的接口更加灵活，允许静态成员和私有成员。

#### 抽象类（Abstract Classes）

抽象类是一种不能被实例化的类，用于定义其他类的共同特征。它可以包含抽象方法（没有实现的方法）和具体方法（有实现的方法）。

##### C#中抽象类的基本语法：

```csharp
public abstract class Animal
{
    public string Name { get; set; }
    public abstract void MakeSound();
    public virtual void Eat()
    {
        Console.WriteLine($"{Name} is eating.");
    }
}

public class Dog : Animal
{
    public override void MakeSound()
    {
        Console.WriteLine("Woof!");
    }
}
```

##### **抽象属性**

C#允许在抽象类中定义抽象属性：

```csharp
public abstract class Animal
{
    public abstract string Sound { get; }
}

public class Dog : Animal
{
    public override string Sound => "Woof";
}
```

##### 抽象类中的密封方法

虽然看起来有点矛盾，但C#允许在抽象类中定义密封方法。这可以用来提供一个不能被重写的实现：

```csharp
public abstract class Base
{
    public abstract void AbstractMethod();
    public virtual void VirtualMethod() { }
    public sealed void SealedMethod() { }
}

public class Derived : Base
{
    public override void AbstractMethod() { }
    public override void VirtualMethod() { } // 可以重写
    //无法重写SealedMethod
}
```

##### 与Java的比较：

C#和Java的抽象类概念非常相似。两种语言中，抽象类都可以包含抽象方法和具体方法，都不能被实例化，都可以被其他类继承。

#### 多重继承

C#和Java都不支持类的多重继承，但都允许一个类实现多个接口：

```csharp
public interface IDrawable
{
    void Draw();
}

public interface IResizable
{
    void Resize(int width, int height);
}

public class Rectangle : IDrawable, IResizable
{
    public void Draw()
    {
        Console.WriteLine("Drawing a rectangle");
    }

    public void Resize(int width, int height)
    {
        Console.WriteLine($"Resizing to {width}x{height}");
    }
}
```

#### 接口的多重继承

虽然C#不支持类的多重继承，但接口可以继承多个接口：

```csharp
public interface IA
{
    void MethodA();
}

public interface IB
{
    void MethodB();
}

public interface IC : IA, IB
{
    void MethodC();
}

public class MyClass : IC
{
    public void MethodA() { }
    public void MethodB() { }
    public void MethodC() { }
}
```

#### 多重继承中的“钻石问题”

在 C# 8.0 引入接口的默认实现后，可能会出现类似于传统多重继承中的“钻石问题”。钻石问题是指当一个类通过多条继承路径从多个接口中继承同一个方法时，不确定该类应该使用哪个接口的实现。

##### 接口中定义的不是默认方法

C#允许显式实现接口方法，这在实现多个具有相同方法签名的接口时特别有用：

```csharp
public interface IA
{
    void Method();
}

public interface IB
{
    void Method();
}

public class MyClass : IA, IB
{
    void IA.Method()
    {
        Console.WriteLine("IA.Method");
    }

    void IB.Method()
    {
        Console.WriteLine("IB.Method");
    }

    public void Method()
    {
        Console.WriteLine("MyClass.Method");
    }
}
```

Java不支持这种显式接口实现。

在Java中，如果一个类实现了两个接口，并且这两个接口中有相同的方法签名（如方法名和参数列表相同），那么在实现类中只需要实现一次该方法。

##### 接口中定义的是默认方法

当一个类实现多个具有相同默认方法的接口时，C#要求显式实现该方法：

```csharp
public interface IA
{
    void Method() => Console.WriteLine("IA.Method");
}

public interface IB
{
    void Method() => Console.WriteLine("IB.Method");
}

public class MyClass : IA, IB
{
    public void Method() // 必须实现，否则编译错误
    {
        ((IA)this).Method(); // 调用IA的默认实现
        // 或
        ((IB)this).Method(); // 调用IB的默认实现
    }
}
```

#### 多接口继承与默认实现

C# 8.0引入的接口默认实现允许我们在不破坏现有代码的情况下向接口添加新方法：

```csharp
public interface ILogger
{
    void Log(string message);
    void LogError(string message) => Log($"ERROR: {message}");
}

public class ConsoleLogger : ILogger
{
    public void Log(string message)
    {
        Console.WriteLine(message);
    }
    // 可以选择重写或使用默认的LogError实现
}
```

#### 使用接口作为类型参数

接口可以用作方法的类型参数，这提供了更大的灵活性：

```csharp
public void ProcessItems<T>(IEnumerable<T> items) where T : IComparable<T>
{
    foreach (var item in items.OrderBy(i => i))
    {
        Console.WriteLine(item);
    }
}
```

#### 接口 vs 抽象类

让我们比较一下接口和抽象类的主要区别：

1.  多重继承：

    - C#类可以实现多个接口，但只能继承一个类（包括抽象类）。
    - Java的情况相同。

2.  成员类型：

    - C# 8.0之前，接口只能包含方法、属性、事件和索引器的签名。
    - C# 8.0及以后，接口可以包含默认实现、静态成员和私有成员。  
    - 抽象类可以包含字段、构造函数、析构函数，以及实现方法。

3.  访问修饰符：

    - 接口成员默认是公共的，不能有私有成员（C# 8.0之前）。
    - 抽象类可以有各种访问修饰符的成员。

4.  构造函数：

    - 接口不能包含构造函数。
    - 抽象类可以包含构造函数。

5.  密封成员：

    - 接口不能有密封成员。
    - 抽象类可以包含密封方法，防止它们被重写。

##### 何时使用接口，何时使用抽象类？

1.  使用接口当：

    - 你想定义一个能力或行为，而不关心实现细节。
    - 你需要多重继承。
    - 你想在不相关的类之间定义共同的行为。

2.  使用抽象类当：

    - 你想定义一个模板，其中包含一些共同的实现。
    - 你需要在相关的类之间共享代码。
    - 你需要定义非公共的成员。

##### 实际应用示例

让我们看一个更复杂的例子，展示接口和抽象类的结合使用：

```csharp
public interface IPayable
{
    decimalCalculatePay();
}

public abstract class Employee : IPayable
{
    public string Name { get; set; }
    public int Id { get; set; }

    protected Employee(string name, int id)
    {
        Name = name;
        Id = id;
    }

    public abstract decimal CalculatePay();

    public virtual void DisplayInfo()
    {
        Console.WriteLine($"Employee: {Name}, ID: {Id}");
    }
}

public class FullTimeEmployee : Employee
{
    public decimal MonthlySalary { get; set; }

    public FullTimeEmployee(string name, int id, decimal monthlySalary) : base(name, id)
    {
        MonthlySalary = monthlySalary;
    }

    public override decimal CalculatePay()
    {
        return MonthlySalary;
    }
}

public class PartTimeEmployee : Employee
{
    public decimal HourlyRate { get; set; }
    public int HoursWorked { get; set; }

    public PartTimeEmployee(string name, int id, decimal hourlyRate) : base(name, id)
    {
        HourlyRate = hourlyRate;
    }

    public override decimal CalculatePay()
    {
        return HourlyRate * HoursWorked;
    }

    public override void DisplayInfo()
    {
        base.DisplayInfo();
        Console.WriteLine($"Hourly Rate: {HourlyRate}");
    }
}

public class Contractor : IPayable
{
    public string Name { get; set; }
    public decimal ContractAmount { get; set; }

    public Contractor(string name, decimal contractAmount)
    {
        Name = name;
        ContractAmount = contractAmount;
    }

    public decimal CalculatePay()
    {
        return ContractAmount;
    }
}

// 使用示例
public class PayrollSystem
{
    public void ProcessPayroll(List<IPayable> payables)
    {
        foreach (var payable in payables)
        {
            Console.WriteLine($"Paying {payable.CalculatePay():C}");

            if (payable is Employee employee)
            {
                employee.DisplayInfo();
            }}
    }
}

// 主程序
class Program
{
    static void Main(string[] args)
    {
        var payables = new List<IPayable>
        {
            new FullTimeEmployee("Alice", 1, 5000m),
            new PartTimeEmployee("Bob", 2, 20m) { HoursWorked = 80 },
            new Contractor("Charlie", 3000m)
        };

        var payrollSystem = new PayrollSystem();
        payrollSystem.ProcessPayroll(payables);
    }
}
```

在这个例子中：

1.  `IPayable` 接口定义了一个通用的支付计算方法。
2.  `Employee` 抽象类实现了 `IPayable` 接口，并提供了一些共同的功能。
3.  `FullTimeEmployee` 和 `PartTimeEmployee` 继承自 `Employee`，实现了特定的薪资计算逻辑。
4.  `Contractor` 直接实现了 `IPayable` 接口，因为它与雇员有不同的特征。
5.  `PayrollSystem` 利用多态性处理不同类型的可支付对象。

#### 设计技巧

 1.     接口隔离原则：创建小而专注的接口，而不是一个大而全的接口。

```csharp
public interface IWorkable
{
    void Work();
}

public interface IEatable
{
    void Eat();
}

public class Human : IWorkable, IEatable
{
    public void Work() { /* ... */ }
    public void Eat() { /* ... */ }
}

public class Robot : IWorkable
{
    public void Work() { /* ... */ }
}
```

 2.     使用抽象类作为模板：

```csharp
public abstract class ReportGenerator
{
    public void GenerateReport()
    {
        CollectData();
        FormatData();
        OutputReport();
    }

    protected abstract void CollectData();
    protected abstract void FormatData();
    protected virtual void OutputReport()
    {
        Console.WriteLine("Outputting report...");
    }
}

public class PDFReportGenerator : ReportGenerator
{
    protected override void CollectData() { /* ... */ }
    protected override void FormatData() { /* ... */ }
    //使用基类的OutputReport
}
```

 3.     组合接口创建更复杂的契约：

```csharp
public interface IReadable
{
    string Read();
}

public interface IWritable
{
    void Write(string content);
}

public interface IFile : IReadable, IWritable
{
    string Name { get; set; }
}

public class TextFile : IFile
{
    public string Name { get; set; }
    public string Read() { /* ... */ }
    public void Write(string content) { /* ... */ }
}
```

#### 最佳实践

1.  优先使用接口：接口提供了更好的灵活性和可测试性。
2.  抽象类用于定义模板：当你有一组相关的类需要共享一些实现时，使用抽象类。
3.  利用C# 8.0的新特性：默认实现可以帮助你在不破坏现有代码的情况下扩展接口。
4.  考虑可测试性：接口更容易模拟（mock），有利于单元测试。
5.  避免过度抽象：只有当你确实需要抽象时才创建接口或抽象类。
6.  遵循SOLID原则：特别是单一责任原则和接口隔离原则。

#### 小结

接口和抽象类是C#中两个强大的抽象工具，它们各有优势和适用场景。接口提供了更大的灵活性和多重继承的能力，而抽象类则允许你定义共同的实现和状态。在设计中合理使用这两种工具，可以创建出更加灵活、可维护和可扩展的代码结构。

作为一个从Java转向C#的开发者，你会发现这两个概念在两种语言中有很多相似之处。但C# 8.0引入的接口新特性，如默认实现和私有成员，为接口提供了更多的功能，使得接口和抽象类之间的界限变得更加模糊。这为代码设计提供了更多的选择和灵活性。

在实际开发中，根据具体需求选择合适的抽象工具，并遵循良好的设计原则，将帮助你创建出高质量的C#代码。随着你对这些概念的深入理解和实践，你将能够更好地利用C#语言的特性，设计出优雅而强大的软件架构。

很好，让我们继续深入探讨接口和抽象类的高级应用，以及它们在现代C#开发中的最佳实践。

#### 高级应用场景

##### 1\. 依赖注入与接口

依赖注入是现代软件开发中的一个重要概念，特别是在使用Asp.Net Core等框架时。接口在这里扮演着关键角色。

```csharp
public interface IUserRepository
{
    Task<User> GetUserByIdAsync(int id);
    Task<IEnumerable<User>> GetAllUsersAsync();
    Task AddUserAsync(User user);
}

public class UserRepository : IUserRepository
{
    private readonly ApplicationDbContext _context;

    public UserRepository(ApplicationDbContext context)
    {
        _context = context;
    }

    public async Task<User> GetUserByIdAsync(int id)
    {
        return await _context.Users.FindAsync(id);
    }

    public async Task<IEnumerable<User>> GetAllUsersAsync()
    {
        return await _context.Users.ToListAsync();
    }

    public async Task AddUserAsync(User user)
    {
        await _context.Users.AddAsync(user);
        await _context.SaveChangesAsync();
    }
}

// 在Startup.cs中注册服务
public void ConfigureServices(IServiceCollection services)
{
    services.AddScoped<IUserRepository, UserRepository>();
}

// 在控制器中使用
public class UserController :ControllerBase
{
    private readonly IUserRepository _userRepository;

    public UserController(IUserRepository userRepository)
    {
        _userRepository = userRepository;
    }

    [HttpGet("{id}")]
    public async Task<ActionResult<User>> GetUser(int id)
    {
        var user = await _userRepository.GetUserByIdAsync(id);
        if (user == null)
        {
            return NotFound();
        }
        return user;
    }
}
```

这种方法允许我们轻松地替换实现，例如，我们可以创建一个模拟版本用于测试：

```csharp
public class MockUserRepository : IUserRepository
{
    private readonly List<User> _users = new();

    public async Task<User> GetUserByIdAsync(int id)
    {
        return await Task.FromResult(_users.FirstOrDefault(u => u.Id == id));
    }

    // 实现其他方法...
}

// 在测试中使用
[Fact]
public async Task GetUser_ReturnsUser_WhenUserExists()
{
    // Arrange
    var mockRepo = new MockUserRepository();
    var controller = new UserController(mockRepo);
    var testUser = new User { Id = 1, Name = "Test User" };
    await mockRepo.AddUserAsync(testUser);

    // Act
    var result = await controller.GetUser(1);

    // Assert
    var actionResult = Assert.IsType<ActionResult<User>>(result);
    var returnValue = Assert.IsType<User>(actionResult.Value);
    Assert.Equal(testUser.Id, returnValue.Id);
    Assert.Equal(testUser.Name, returnValue.Name);
}
```

##### 2\. 抽象类作为领域模型的基类

在领域驱动设计（DDD）中，抽象类常常用作实体或值对象的基类。

```csharp
public abstract class Entity
{
    public int Id { get; protected set; }

    public override bool Equals(object obj)
    {
        var other = obj as Entity;

        if (ReferenceEquals(other, null))
            return false;

        if (ReferenceEquals(this, other))
            return true;

        if (GetType() != other.GetType())
            return false;

        if (Id ==0 || other.Id == 0)
            return false;

        return Id == other.Id;
    }

    public static bool operator ==(Entity a, Entity b)
    {
        if (ReferenceEquals(a, null) && ReferenceEquals(b, null))
            return true;

        if (ReferenceEquals(a, null) || ReferenceEquals(b, null))
            return false;

        return a.Equals(b);
    }

    public static bool operator !=(Entity a, Entity b)
    {
        return !(a == b);
    }

    public override int GetHashCode()
    {
        return (GetType().ToString() + Id).GetHashCode();
    }
}

public class User : Entity
{
    public string Name { get; set; }
    public string Email { get; set; }
}

public class Product : Entity
{
    public string Name { get; set; }
    public decimal Price { get; set; }
}
```

这个抽象基类提供了通用的相等性比较逻辑，所有继承自它的实体都会自动获得这些功能。

##### 3\. 接口默认实现的高级应用

C# 8.0引入的接口默认实现可以用于创建可选功能或提供默认行为。

```csharp
public interface ILogger
{
    void Log(string message);

    void LogError(string message) => Log($"ERROR: {message}");
    void LogWarning(string message) => Log($"WARNING: {message}");
    void LogInfo(string message) => Log($"INFO: {message}");
}

public class ConsoleLogger : ILogger
{
    public void Log(string message)
    {
        Console.WriteLine($"[{DateTime.Now}] {message}");
    }

    // 可以选择性地重写默认实现
    public void LogError(string message)
    {
        Console.ForegroundColor = ConsoleColor.Red;
        Log($"ERROR: {message}");
        Console.ResetColor();
    }
}

public class FileLogger : ILogger
{
    private readonly string _path;

    public FileLogger(string path)
    {
        _path = path;
    }

    public void Log(string message)
    {
        File.AppendAllText(_path, $"[{DateTime.Now}] {message}\n");
    }

    // 使用默认实现的其他方法
}
```

这种方法允许我们在接口中定义通用行为，同时保留了实现类专它的灵活性。

#### 最佳实践和设计模式

##### 1\. 策略模式

策略模式是一个典型的使用接口的设计模式：

```csharp
public interface IPaymentStrategy
{
    void Pay(decimal amount);
}

public class CreditCardPayment : IPaymentStrategy
{
    public void Pay(decimal amount)
    {
        Console.WriteLine($"Paying {amount} using Credit Card");
    }
}

public class PayPalPayment : IPaymentStrategy
{
    public void Pay(decimal amount)
    {
        Console.WriteLine($"Paying {amount} using PayPal");
    }
}

public class ShoppingCart
{
    private readonly IPaymentStrategy _paymentStrategy;

    public ShoppingCart(IPaymentStrategy paymentStrategy)
    {
        _paymentStrategy = paymentStrategy;
    }

    public void Checkout(decimal amount)
    {
        _paymentStrategy.Pay(amount);
    }
}

// 使用
var cart = new ShoppingCart(new CreditCardPayment());
cart.Checkout(100.50m);

cart = new ShoppingCart(new PayPalPayment());
cart.Checkout(200.75m);
```

##### 2\. 模板方法模式

模板方法模式是抽象类的一个经典应用：

```csharp
public abstract class PizzaMaker
{
    public void MakePizza()
    {
        PrepareDough();
        AddSauce();
        AddToppings();Bake();
        Cut();
    }

    protected abstract void AddToppings();

    protected virtual void PrepareDough()
    {
        Console.WriteLine("Preparing pizza dough");
    }

    protected virtual void AddSauce()
    {
        Console.WriteLine("Adding tomato sauce");
    }

    protected virtual void Bake()
    {
        Console.WriteLine("Baking for 15 minutes at 200°C");
    }

    protected virtual void Cut()
    {
        Console.WriteLine("Cutting the pizza into 8 slices");
    }
}

public class MargheritaPizza : PizzaMaker
{
    protected override void AddToppings()
    {
        Console.WriteLine("Adding mozzarella and basil");
    }
}

public class PepperoniPizza : PizzaMaker
{
    protected override void AddToppings()
    {
        Console.WriteLine("Adding pepperoni and extra cheese");
    }

    protected override void Bake()
    {
        Console.WriteLine("Baking for 20 minutes at 180°C");
    }
}

// 使用
var margherita = new MargheritaPizza();
margherita.MakePizza();

var pepperoni = new PepperoniPizza();
pepperoni.MakePizza();
```

#### 结论

接口和抽象类是C#中强大的抽象化工具，它们在软件设计中扮演着关键角色。通过正确使用这些工具，我们可以创建更加灵活、可维护和可扩展的代码结构。

- 接口适用于定义对象的能力或行为，特别是在需要多重继承或定义跨类型契约时。
- 抽象类适用于定义一组相关类的共同特征和行为，特别是在需要共享实现代码时。

在实际开发中，你可能会发现同时使用这两种工具的情况。例如，你可能会创建一个抽象基类来实现一个或多个接口，然后让具体类继承这个抽象基类。

记住，好的设计不仅仅是使用正确的工具，还包括遵循SOLID原则、设计模式和最佳实践。随着你在C#开发中积累更多经验，你将能够更加自如地在不同场景中选择和应用这些抽象化工具。

最后，随着C#语言的不断发展（特别是C# 8.0引入的接口新特性），保持对语言新特性的了解和学习也很重要。这些新特性可能会为你提供更多的设计选择，帮助你写出更好的代码。

## 9 委托和事件

委托是C#中的一种类型，它表示对具有特定参数列表和返回类型的方法的引用。事件则是基于委托的一种机制，用于对象之间的通信。让我们深入了解这两个概念。

### 9.1 委托（Delegates）

委托允许将方法作为参数传递，这为实现回调和事件处理提供了基础。

##### 基本语法

```csharp
// 定义委托
public delegate void SimpleDelegate(string message);

// 使用委托
public class DelegateDemo
{
    public void DisplayMessage(string message)
    {
        Console.WriteLine($"Message: {message}");
    }

    public void Run()
    {
        SimpleDelegate del = DisplayMessage;
        del("Hello, Delegates!");
    }
}

// 使用
var demo = new DelegateDemo();
demo.Run();// 输出: Message: Hello, Delegates!
```

##### 多播委托

委托可以指向多个方法，这被称为多播委托。

```csharp
public class MulticastDelegateDemo
{
    public static void Method1(string message)
    {
        Console.WriteLine($"Method1: {message}");
    }

    public static void Method2(string message)
    {
        Console.WriteLine($"Method2: {message}");
    }

    public void Run()
    {
        SimpleDelegate del = Method1;
        del += Method2;  // 添加另一个方法
        del("Hello, Multicast!");
    }
}

// 使用
var demo = new MulticastDelegateDemo();
demo.Run();
// 输出:
// Method1: Hello, Multicast!
// Method2: Hello, Multicast!
```

##### 匿名方法和Lambda 表达式

C# 2.0 引入了匿名方法，C# 3.0 进一步引入了 Lambda 表达式，它们都可以用来创建内联委托。

```csharp
public class AnonymousAndLambdaDemo
{
    public void Run()
    {
        // 匿名方法
        SimpleDelegate anonymousDelegate = delegate(string message)
        {
            Console.WriteLine($"Anonymous: {message}");
        };

        // Lambda 表达式
        SimpleDelegate lambdaDelegate = (message) => Console.WriteLine($"Lambda: {message}");

        anonymousDelegate("Hello from anonymous method!");
        lambdaDelegate("Hello from lambda expression!");
    }
}
```

### 9.2 事件（Events）

事件提供了一种方式，使得一个类可以通知其他类发生了某些事情。事件使用委托来实现。

##### 基本语法

```csharp
public class Publisher
{
    // 定义一个委托类型
    public delegate void EventHandler(string message);

    // 声明一个事件
    public event EventHandler SomethingHappened;

    public void DoSomething()
    {
        // 触发事件
        SomethingHappened?.Invoke("Something just happened!");
    }
}

public class Subscriber
{
    public void HandleEvent(string message)
    {
        Console.WriteLine($"Event handled: {message}");
    }
}

// 使用
var publisher = new Publisher();
var subscriber = new Subscriber();

publisher.SomethingHappened += subscriber.HandleEvent;
publisher.DoSomething();// 输出: Event handled: Something just happened!
```

##### 标准事件模式

.NET Framework 定义了一个标准的事件模式，使用`EventHandler` 和 `EventArgs`。

```csharp
public class CustomEventArgs : EventArgs
{
    public string Message { get; set; }
}

public class StandardEventPublisher
{
    public event EventHandler<CustomEventArgs> CustomEvent;

    protected virtual void OnCustomEvent(CustomEventArgs e)
    {
        CustomEvent?.Invoke(this, e);
    }

    public void TriggerEvent()
    {
        OnCustomEvent(new CustomEventArgs { Message = "Custom event triggered" });
    }
}

public class StandardEventSubscriber
{
    public void HandleCustomEvent(object sender, CustomEventArgs e)
    {
        Console.WriteLine($"Event received: {e.Message}");
    }
}

// 使用
var publisher = new StandardEventPublisher();
var subscriber = new StandardEventSubscriber();

publisher.CustomEvent += subscriber.HandleCustomEvent;
publisher.TriggerEvent();  // 输出: Event received: Custom event triggered
```

### 9.3 委托和事件的高级应用

##### 1\. 异步编程

委托在异步编程中扮演重要角色，尽管现在更常用的是 Task-based 异步模式。

```csharp
public class AsyncDelegateDemo
{
    public delegate int AsyncCalculation(int x, int y);

    public static int Add(int x, int y)
    {
        Thread.Sleep(1000);  // 模拟耗时操作
        return x + y;
    }

    public async Task RunAsync()
    {
        AsyncCalculation del = Add;
        var result = await Task.Run(() => del(5, 3));
        Console.WriteLine($"Async result: {result}");
    }
}
```

##### 2\. LINQ 和函数式编程

委托是LINQ和函数式编程风格的基础。

```csharp
public class LinqDemo
{
    public void Run()
    {
        var numbers = new List<int> { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

        var evenNumbers = numbers.Where(n => n % 2 == 0).Select(n => n * n).ToList();

        Console.WriteLine(string.Join(", ", evenNumbers));
    }
}
```

##### 3\. 事件聚合器

在复杂的应用程序中，可以使用事件聚合器模式来集中管理事件。

```csharp
public class EventAggregator
{
    private Dictionary<Type, List<object>> eventHandlers = new Dictionary<Type, List<object>>();

    public void Subscribe<TEvent>(Action<TEvent> handler)
    {
        var eventType = typeof(TEvent);
        if (!eventHandlers.ContainsKey(eventType))
        {
            eventHandlers[eventType] = new List<object>();
        }
        eventHandlers[eventType].Add(handler);
    }

    public void Publish<TEvent>(TEvent eventToPublish)
    {
        var eventType = typeof(TEvent);
        if (eventHandlers.ContainsKey(eventType))
        {
            foreach (var handler in eventHandlers[eventType].Cast<Action<TEvent>>())
            {
                handler(eventToPublish);
            }
        }
    }
}

// 使用
public class EventAggregatorDemo
{
    public class UserLoggedInEvent
    {
        public string Username { get; set; }
    }

    public void Run()
    {
        var aggregator = new EventAggregator();

        aggregator.Subscribe<UserLoggedInEvent>(e =>Console.WriteLine($"User logged in: {e.Username}"));

        aggregator.Publish(new UserLoggedInEvent { Username = "JohnDoe" });
    }
}
```

### 10.4 最佳实践

 1.     使用`EventHandler` 和 `EventHandler<TEventArgs>` 作为事件委托类型。
 2.     总是检查事件是否为null 再调用（使用 `?.` 操作符）。
 3.     考虑使用 `System.Action` 和 `System.Func` 代替自定义委托类型。
 4.     在多线程环境中使用事件时要小心，考虑线程安全问题。
 5.     避免在循环中频繁触发事件，考虑批处理或节流。

```csharp
// 好的实践
public event EventHandler<CustomEventArgs> GoodEvent;

protected virtual void OnGoodEvent(CustomEventArgs e)
{
    GoodEvent?.Invoke(this, e);
}

// 避免的做法
public delegate void BadEventDelegate(int someParameter);
public event BadEventDelegate BadEvent;

public void TriggerBadEvent()
{
    if (BadEvent != null)
    {
        BadEvent(42);  // 不安全，可能在检查和调用之间变为null
    }
}
```

#### 结论

委托和事件是C#中非常强大的特性，它们为实现灵活的、松耦合的代码提供了基础。委托允许方法作为参数传递，这为回调、异步编程和LINQ等功能提供了支持。事件则建立在委托的基础上，提供了一种标准的、类型安全的方式来实现发布-订阅模式。

通过使用委托和事件，你可以创建更加模块化和可扩展的代码。它们在图形用户界面编程、异步编程、事件驱动系统等多个领域都有广泛应用。

然而，使用委托和事件也需要谨慎。过度使用可能导致代码难以追踪和调试。在设计系统时，应该仔细考虑何时使用委托和事件，以及如何最好地组织和管理它们。

随着你在C#开发中积累更多经验，你会发现委托和事件在各种场景中的巧妙应用。掌握这些概念将帮助你编写更加灵活、高效的代码，并更好地理解和利用.NET框架的许多高级特性。

# 10泛型

欢迎来到我们的C#泛型深度探索之旅！对于从Java转向.NET Core的开发者来说，泛型可能是一个熟悉而又陌生的概念。虽然两种语言都支持泛型，但C#的泛型实现在某些方面更加强大和灵活。让我们一起来探讨这些差异，并通过实际案例来加深理解。

## 10.1 泛型基础

首先，让我们回顾一下泛型的基本概念。泛型允许我们编写可以处理多种数据类型的代码，而不需要为每种类型都编写重复的代码。这不仅提高了代码的复用性，也增强了类型安全性。

#### C#示例：

```csharp
public class GenericList<T>
{
    private List<T> items = new();

    public void Add(T item) => items.Add(item);
    public T GetItem(int index) => items[index];
}

// 使用示例
var intList = new GenericList<int>();
intList.Add(10);
Console.WriteLine(intList.GetItem(0)); // 输出: 10

var stringList = new GenericList<string>();
stringList.Add("Hello");
Console.WriteLine(stringList.GetItem(0)); // 输出: Hello
```

#### Java示例：

```java
public class GenericList<T> {
    private List<T> items = new ArrayList<>();

    public void add(T item) {
        items.add(item);
    }

    public T getItem(int index) {
        return items.get(index);
    }
}

// 使用示例
GenericList<Integer> intList = new GenericList<>();
intList.add(10);
System.out.println(intList.getItem(0)); // 输出: 10

GenericList<String> stringList = new GenericList<>();
stringList.add("Hello");
System.out.println(stringList.getItem(0)); // 输出: Hello
```

看起来很相似，对吧？但是，让我们深入探讨一下C#泛型的一些独特特性。

## 10.2 泛型约束

C#允许我们对泛型类型参数添加约束，这在Java中是没有直接对应的功能。

#### C#示例：

```csharp
public class Calculator<T> where T : struct, IComparable<T>
{
    public T Max(T a, T b) => a.CompareTo(b) > 0 ? a : b;
}

// 使用示例
var calc = new Calculator<int>();
Console.WriteLine(calc.Max(5, 10)); // 输出: 10

// 下面的代码会导致编译错误，因为string不是值类型
// var stringCalc = new Calculator<string>();
```

在这个例子中，我们限制了T必须是值类型（struct）并且实现了IComparable接口。

#### Java的近似实现：

Java没有直接的泛型约束，但可以通过接口来实现类似的功能：

```java
public class Calculator<T extends Comparable<T>> {
    public T max(T a, T b) {
        return a.compareTo(b) > 0 ? a : b;
    }
}

// 使用示例
Calculator<Integer> calc = new Calculator<>();
System.out.println(calc.max(5, 10)); // 输出: 10

// 这是允许的，因为Java没有值类型的概念
Calculator<String> stringCalc = new Calculator<>();
System.out.println(stringCalc.max("apple", "banana")); // 输出: banana
```

注意，Java的泛型约束相对较弱，无法像C#那样限制为值类型。

## 10.3 泛型方法

C#和Java都支持泛型方法，但C#的语法更加简洁。

#### C#示例：

```csharp
public static class Utilities
{
    public static T[] CreateArray<T>(params T[] elements) => elements;

    public static void Swap<T>(ref T a, ref T b)
    {
        T temp = a;
        a = b;
        b = temp;
    }
}

// 使用示例
int[] numbers = Utilities.CreateArray(1, 2, 3);
Console.WriteLine(string.Join(", ", numbers)); // 输出: 1, 2, 3

int x = 5, y = 10;
Utilities.Swap(ref x, ref y);
Console.WriteLine($"x = {x}, y = {y}"); // 输出: x = 10, y = 5
```

#### Java示例：

```java
public class Utilities {
    @SafeVarargs
    public static <T> T[] createArray(T... elements) {
        return elements;
    }

    public static <T> void swap(T[] arr, int i, int j) {
        T temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}

// 使用示例
Integer[] numbers = Utilities.createArray(1, 2, 3);
System.out.println(Arrays.toString(numbers)); // 输出: [1, 2, 3]

Integer[] arr = {5, 10};
Utilities.swap(arr, 0, 1);
System.out.println("x = " + arr[0] + ", y = " + arr[1]); // 输出: x = 10, y = 5
```

注意，Java不支持引用参数，所以我们不能像C#那样直接交换两个变量的值。

## 10.4 默认值和约束

C#允许我们为泛型类型参数指定默认值，这在Java中是不可能的。

#### C#示例：

```csharp
public class DefaultValueExample<T> where T : struct
{
    public T GetDefaultValue() => default(T);
}

// 使用示例
var example = new DefaultValueExample<int>();
Console.WriteLine(example.GetDefaultValue()); // 输出: 0

var dateExample = new DefaultValueExample<DateTime>();
Console.WriteLine(dateExample.GetDefaultValue()); // 输出: 1/1/0001 12:00:00 AM
```

#### Java示例：

Java没有直接对应的功能，但可以通过反射或其他方式模拟：

```java
public class DefaultValueExample<T> {
    private final Class<T> type;

    public DefaultValueExample(Class<T> type) {
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    public T getDefaultValue() {
        if (type.equals(int.class)) return (T) Integer.valueOf(0);
        if (type.equals(boolean.class)) return (T) Boolean.FALSE;
        // ... 其他基本类型的处理
        return null; // 对于引用类型，返回null
    }
}

// 使用示例
DefaultValueExample<Integer> example = new DefaultValueExample<>(int.class);
System.out.println(example.getDefaultValue()); // 输出: 0

DefaultValueExample<Boolean> boolExample = new DefaultValueExample<>(boolean.class);
System.out.println(boolExample.getDefaultValue()); // 输出: false
```

## 10.5 泛型继承

C#支持泛型类的继承，并允许在派生类中指定或保持开放泛型类型参数：

```csharp
class Program
{
    static void Main(string[] args)
    {

        var v = new StringDerived();
        v.Data = "hello word";
        v.PrintUpperCase();// HELLO WORD

        var v2 = new GenericDerived<string>();
        v2.PrintType();// String
    }
}

public class GenericBase<T>
{
    public T Data { get; set; }
}

public class StringDerived : GenericBase<string>
{
    public void PrintUpperCase()
    {
        Console.WriteLine(Data.ToUpper());
    }
}

public class GenericDerived<T> : GenericBase<T>
{
    public void PrintType()
    {
        Console.WriteLine(typeof(T).Name);
    }
}
```



## 10.6 泛型协变和逆变

C#支持泛型接口和委托的协变和逆变，这为类型系统带来了更大的灵活性。Java从1.5版本开始也支持协变，但范围较小。

**协变（Covariance）**：允许将一个泛型类型的子类赋值给泛型类型的父类实例，常用于返回类型，使用`out`关键字。

**逆变（Contravariance）**：允许将一个泛型类型的父类赋值给泛型类型的子类实例，常用于参数类型，使用`in`关键字。

#### C#示例：

```csharp
// 协变
IEnumerable<string> strings = new List<string>();
IEnumerable<object> objects = strings; // 这是允许的

// 逆变
Action<object> objectAction = obj => Console.WriteLine(obj);
Action<string> stringAction = objectAction; // 这是允许的

// 同时使用协变和逆变
interface IConverter<in TInput, out TOutput>
{
    TOutput Convert(TInput input);
}

class StringToIntConverter : IConverter<string, int>
{
    public int Convert(string input) => int.Parse(input);
}

IConverter<string, int> converter = new StringToIntConverter();
int result = converter.Convert("42");
Console.WriteLine(result); // 输出: 42
```

#### Java示例：

Java的泛型协变较为有限，主要通过通配符实现：

```java
// 协变（只读）
List<String> strings = new ArrayList<>();
List<? extends Object> objects = strings; // 这是允许的

// Java不支持泛型方法的逆变
// 下面的代码在Java中是不允许的：
// Consumer<Object> objectConsumer = obj -> System.out.println(obj);
// Consumer<String> stringConsumer = objectConsumer;

// Java中没有直接对应C#的in和out关键字的概念
interface Converter<T, R> {
    R convert(T input);
}

class StringToIntConverter implements Converter<String, Integer> {
    @Override
    public Integer convert(String input) {
        return Integer.parseInt(input);
    }
}

Converter<String, Integer> converter = new StringToIntConverter();
int result = converter.convert("42");
System.out.println(result); // 输出: 42
```

## 10.7 协变和逆变在委托中的应用

C#支持在委托中使用协变和逆变：

```csharp
delegate T Factory<out T>();
delegate void Action<in T>(T obj);

class Animal { }
class Dog : Animal { }

class Program
{
    static Dog CreateDog() => new Dog();
    static void HandleAnimal(Animal a) { }

    static void Main()
    {
        Factory<Dog> dogFactory = CreateDog;
        Factory<Animal> animalFactory = dogFactory; // 协变

        Action<Animal> animalHandler = HandleAnimal;
        Action<Dog> dogHandler = animalHandler; // 逆变
    }
}
```

## 10.8 使用协变和逆变优化接口设计

协变和逆变可以使得泛型接口更加灵活：

```csharp
// 协变接口
public interface IProducer<out T>
{
    T Produce();
}

// 逆变接口
public interface IConsumer<in T>
{
    void Consume(T item);
}

public class Animal { }
public class Dog : Animal { }

public class DogProducer : IProducer<Dog>
{
    public Dog Produce() => new Dog();
}

public class AnimalConsumer : IConsumer<Animal>
{
    public void Consume(Animal animal) { }
}

// 使用
IProducer<Animal> animalProducer = new DogProducer(); // 协变
IConsumer<Dog> dogConsumer = new AnimalConsumer(); // 逆变
```



### 结论

通过这些示例，我们可以看到C#的泛型在某些方面比Java的更加强大和灵活。C#提供了更丰富的类型约束、协变和逆变支持，以及默认值处理等特性。这些特性使得C#在处理复杂的泛型场景时更加得心应手。

对于从Java转向.NET Core的开发者，熟悉这些差异不仅能帮助你更好地理解和使用C#的泛型，还能让你在设计API时充分利用C#的独特优势。记住，虽然基本概念相似，但细节上的差异可能会对代码的结构和性能产生重大影响。

在实际开发中，你会发现C#的泛型能够帮助你编写出更加类型安全、更加灵活的代码。特别是在处理集合、算法实现、依赖注入等场景时，C#的泛型优势会更加明显。

最后，不要忘记C#还有一些其他与泛型相关的高级特性，比如泛型协变和逆变在异步编程中的应用、泛型特化等。这些topics可能需要更深入的学习和实践。继续探索，你会发现C#泛型的更多精彩之处！

# 11 LINQ \(Language Integrated Query\)

欢迎来到LINQ的世界！对于从Java转向.NET Core的开发者来说，LINQ可能是最令人兴奋的特性之一。LINQ为C#带来了强大的查询能力，使得处理集合和数据变得异常简单和优雅。让我们深入探讨LINQ，并与Java中的相似功能进行比较。

## 11.1LINQ基础

LINQ允许我们使用类似SQL的语法直接在代码中查询数据，无论是内存中的集合、数据库还是XML文档。

#### C#示例：

```csharp
var numbers = new List<int> { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

// 查询语法
var evenNumbers = from num in numbers
                  where num % 2 == 0
                  select num;

// 方法语法
var evenNumbersMethod = numbers.Where(num => num % 2 == 0);

Console.WriteLine(string.Join(", ", evenNumbers)); // 输出: 2, 4, 6, 8, 10
```

#### Java示例：

Java8引入的Stream API提供了类似的功能，但语法和能力有所不同：

```java
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

List<Integer> evenNumbers = numbers.stream()
                .filter(num -> num % 2 == 0)
                                   .collect(Collectors.toList());

System.out.println(String.join(", ", evenNumbers.stream().map(Object::toString).collect(Collectors.toList())));
// 输出: 2, 4, 6, 8, 10
```

## 11.2 复杂查询

LINQ的强大之处在于它能够轻松处理复杂的查询，包括排序、分组和连接操作。

#### C#示例：

```csharp
var people = new List<Person>
{
    new Person { Name = "Alice", Age = 25, City = "New York" },
    new Person { Name = "Bob", Age = 30, City = "London" },
    new Person { Name = "Charlie", Age = 35, City = "New York" },
    new Person { Name = "David", Age = 40, City = "London" }
};

var query = from person in people
            where person.Age > 30
            orderby person.Name
            group person by person.City into cityGroup
            select new
            {
                City = cityGroup.Key,
                Count = cityGroup.Count(),
                AverageAge = cityGroup.Average(p => p.Age)
            };

foreach (var result in query)
{
    Console.WriteLine($"City: {result.City}, Count: {result.Count}, Average Age: {result.AverageAge}");
}
// 输出:
// City: London, Count: 1, Average Age: 40
// City: New York, Count: 1, Average Age: 35
```

#### Java示例：

Java的Stream API也可以实现类似的功能，但语法会更加复杂：

```java
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Person {
    String name;
    int age;
    String city;

    //构造函数、getter和setter省略
}

List<Person> people = Arrays.asList(
    new Person("Alice", 25, "New York"),
    new Person("Bob", 30, "London"),
    new Person("Charlie", 35, "New York"),
    new Person("David", 40, "London")
);

Map<String, List<Person>> groupedByCity = people.stream()
    .filter(person -> person.age > 30)
    .sorted((p1, p2) -> p1.name.compareTo(p2.name))
    .collect(Collectors.groupingBy(person -> person.city));

groupedByCity.forEach((city, cityGroup) -> {
    double averageAge = cityGroup.stream().mapToInt(p -> p.age).average().orElse(0);
    System.out.printf("City: %s, Count: %d, Average Age: %.2f%n", city, cityGroup.size(), averageAge);
});
// 输出:
// City: London, Count: 1, Average Age: 40.00
// City: New York, Count: 1, Average Age: 35.00
```

## 11.3 延迟执行

LINQ查询默认是延迟执行的，这意味着查询只在结果被实际使用时才执行。这与Java的Stream API类似。

#### C#示例：

```csharp
var numbers = new List<int> { 1, 2, 3, 4, 5 };

var query = numbers.Select(n => {
    Console.WriteLine($"Processing {n}");
    return n * 2;
});

Console.WriteLine("Query defined.");
foreach (var num in query)
{
    Console.WriteLine($"Result: {num}");
}

// 输出:
// Query defined.
// Processing 1
// Result: 2
// Processing 2
// Result: 4
// Processing 3
// Result: 6
// Processing 4
// Result: 8
// Processing 5
// Result: 10
```

#### Java示例：

```java
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

Stream<Integer> stream = numbers.stream().map(n -> {
    System.out.println("Processing " + n);
    return n * 2;
});

System.out.println("Stream defined.");
stream.forEach(num -> System.out.println("Result: " + num));

// 输出:
// Stream defined.
// Processing 1
// Result: 2
// Processing 2
// Result: 4
// Processing 3
// Result: 6
// Processing 4
// Result: 8
// Processing 5
// Result: 10
```

## 11.4 查询提供者

LINQ的一个强大特性是它可以针对不同的数据源使用相同的查询语法。例如，LINQ to SQL允许我们使用LINQ语法直接查询数据库。

#### C#示例（使用Entity Framework Core）：

```csharp
using (var context = new MyDbContext())
{
    var query = from user in context.Users
                where user.Age > 18
                orderby user.Name
                select new { user.Name, user.Age };

    foreach (var item in query)
    {
        Console.WriteLine($"Name: {item.Name}, Age: {item.Age}");
    }
}
```

这个查询会被转换为SQL并在数据库中执行。

#### Java示例：

Java没有直接等价的功能。JPA（Java Persistence API）提供了类似的查询能力，但语法不同：

```java
EntityManager em = // 获取EntityManager
TypedQuery<User> query = em.createQuery(
    "SELECT NEW com.example.UserDTO(u.name, u.age) " +
    "FROM User u WHERE u.age >18 ORDER BY u.name", User.class);

List<UserDTO> results = query.getResultList();
for (UserDTO user : results) {
    System.out.println("Name: " + user.getName() + ", Age: " + user.getAge());
}
```

## 11.5 LINQ的其他特性

LINQ还有许多其他强大的特性，例如：

1.  即时查询（使用ToList\(\)、ToArray\(\)等方法）
2.  自定义查询操作符
3.  并行LINQ（PLINQ）用于并行查询处理

#### C#示例（PLINQ）：

```csharp
var numbers = Enumerable.Range(1, 1000000);

var evenSquares = numbers
    					.AsParallel()//将序列转换为并行查询，这意味着后续的查询操作会在多个线程上并行执行，以提高性能。
    					.Where(n => n % 2 == 0)
                        .Select(n => n * n);

Console.WriteLine($"Count of even squares: {evenSquares.Count()}");
```

#### Java示例（并行流）：

```java
import java.util.stream.IntStream;

long count = IntStream.range(1, 1000001)
                      .parallel()
                      .filter(n -> n % 2 == 0)
                      .mapToLong(n -> (long)n * n)
                      .count();

System.out.println("Count of even squares: " + count);
```

### 结论

LINQ是C#中一个非常强大和灵活的特性，它大大简化了数据查询和处理的代码。虽然Java8引入的Stream API提供了类似的功能，但LINQ在语法简洁性和统一性方面仍然具有优势。

对于从Java转向.NET Core的开发者，掌握LINQ将极大地提高你的生产力。LINQ不仅可以用于内存中的集合，还可以无缝地应用于各种数据源，如数据库和XML。

在实际开发中，你会发现LINQ在处理复杂数据查询、数据转换和数据分析时特别有用。它可以帮助你编写更加简洁、可读性更强的代码，同时保持良好的性能。

记住，虽然LINQ强大，但也要注意合理使用。过度复杂的LINQ查询可能会影响代码的可读性和性能。在编写LINQ查询时，始终要考虑查询的效率和可维护性。

最后，随着你对LINQ的深入了解，你会发现它不仅仅是一个查询工具，而是一种强大的编程范式，可以改变你思考和解决问题的方式。继续探索LINQ的高级特性，你会发现更多令人兴奋的可能性！

# 12 异步编程 \(async/await\)

在现代软件开发中，异步编程已经成为提高应用程序性能和响应性的关键技术之一。C# 中的 async/await 语法提供了一种简洁而强大的方式来处理异步操作，使得编写非阻塞代码变得更加容易。对于从Java 转向 .NET Core 的开发者来说，理解 C# 中的异步编程模型及其与 Java 的差异是非常重要的。让我们深入探讨 C# 的异步编程，并与 Java 进行对比。

### 异步编程的基础概念

在开始之前，我们需要理解几个关键概念：

1.  **异步操作**：不会立即完成的操作，通常涉及 I/O 或长时间运行的计算。
2.  **非阻塞**：允许程序在等待异步操作完成时继续执行其他代码。
3.  **Task**：表示异步操作的对象。
4.  **async/await**：C# 中用于简化异步编程的关键字。

### C# 中的async/await

C# 的 async/await 模型提供了一种直观的方式来编写异步代码，使其看起来像同步代码。这大大简化了异步编程，提高了代码的可读性和可维护性。

#### 基本语法

```csharp
public async Task<string> FetchDataAsync()
{
    // 模拟异步操作
    await Task.Delay(1000);
    return "Data fetched!";
}

// 调用异步方法
public async Task UseDataAsync()
{
    string result = await FetchDataAsync();
    Console.WriteLine(result);
}
```

在这个例子中：

- `async` 关键字标记方法为异步方法。
- `Task<T>` 表示异步操作，其中 `T` 是返回值的类型。
- `await` 关键字用于等待异步操作完成，而不阻塞线程。

#### 与 Java 的对比

Java 8 引入了 CompletableFuture 来处理异步操作，但语法和使用方式与 C# 的async/await 有很大不同。让我们看一个 Java 的例子：

```java
public CompletableFuture<String> fetchDataAsync() {
    return CompletableFuture.supplyAsync(() -> {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Data fetched!";
    });
}

// 调用异步方法
public void useDataAsync() {
    fetchDataAsync().thenAccept(result -> System.out.println(result));
}
```

对比可以看出：

1.  C# 的 async/await 语法更加直观，代码结构更接近同步代码。
2.  Java 需要使用 CompletableFuture 和 lambda 表达式，这可能导致代码嵌套和复杂性增加。
3.  C# 的异常处理更自然，可以直接使用 try-catch，而 Java 通常需要在 CompletableFuture 中处理异常。

### 深入理解 Task 和 ValueTask

在 C# 的异步编程中，Task 和 ValueTask 是两个核心概念，它们都用于表示异步操作，但有一些重要的区别。

#### Task

Task 是.NET 中表示异步操作的标准方式。它是一个引用类型，可以表示无返回值的操作（Task）或有返回值的操作（Task）。

```csharp
public async Task DoSomethingAsync()
{
    await Task.Delay(1000);
    Console.WriteLine("Task completed");
}

public async Task<int> CalculateAsync()
{
    await Task.Delay(1000);
    return 42;
}
```

Task 的优点：

- 可以表示复杂的异步操作。
- 支持取消、延续和异常处理。
- 可以轻松组合多个异步操作。

#### ValueTask

ValueTask 是一个较新的概念，设计用于优化性能，特别是在异步操作可能立即完成的情况下。它是一个值类型，可以避免在某些情况下分配额外的内存。

```csharp
public ValueTask<int> GetValueAsync()
{
    if (cachedValue.HasValue)
    {
        return new ValueTask<int>(cachedValue.Value);
    }
    return new ValueTask<int>(FetchValueAsync());
}

private async Task<int> FetchValueAsync()
{
    await Task.Delay(1000);
    return 42;
}
```

ValueTask 的优点：

- 在同步完成的情况下可以避免内存分配。
- 适用于频繁调用且经常同步完成的方法。

#### 何时使用 ValueTask

- 当方法频繁被调用，并且大部分情况下可以同步完成时。
- 当你需要优化性能，减少内存分配时。

#### 注意事项

- ValueTask 不应该被多次使用，这可能导致不可预知的行为。
- Task 更适合长时间运行或复杂的异步操作。

### 异步编程最佳实践

 1. **避免异步死锁**：不要在同步上下文中等待异步方法，特别是在 UI 线程中。

    **不要在同步方法中调用 `Task.Wait()` 或 `Task.Result`**：

    - 这些方法会阻塞调用线程，直到任务完成。如果此时任务的完成需要回到调用线程继续执行，而调用线程又被阻塞，就会产生死锁。
    - 例如，在UI线程中调用 `await task.ConfigureAwait(false)` 而不是 `task.Wait()` 或 `task.Result`，以避免阻塞UI线程。

    **使用 `async`/`await` 模式**：

    - 如果可能，将调用链完全转换为异步调用。这意味着你应该尽量使用 `async`/`await`，而不是在同步方法中等待异步操作。

    **避免在 UI 线程中执行繁重的计算任务**：

    - 这可以通过将繁重任务移到后台线程（例如使用 `Task.Run`）来实现，确保 UI 线程始终保持响应状态。

    **配置 `ConfigureAwait(false)`**：

    - 如果你不需要在 UI 线程或原始同步上下文中恢复执行任务，可以使用 `ConfigureAwait(false)` 来避免死锁。这通常适用于库代码，确保它们不会在特定的同步上下文中运行。

    ```csharp
    // 错误的做法：可能导致死锁
    public void LoadData()
    {
        var data = GetDataAsync().Result; // 阻塞 UI 线程，可能导致死锁
    }
    
    // 正确的做法：使用 async/await
    public async Task LoadDataAsync()
    {
        var data = await GetDataAsync(); // 非阻塞，避免死锁
    }
    
    private async Task<string> GetDataAsync()
    {
        await Task.Delay(1000); // 模拟异步操作
        return "Data Loaded";
    }
    
    ```

2. **使用 ConfigureAwait\(false\)**：在不需要同步上下文的地方，使用 ConfigureAwait\(false\) 可以提高性能。

   `ConfigureAwait(false)` 告诉任务在完成后不需要在原始同步上下文（例如 UI 线程）上继续执行。对于一些不依赖特定上下文的异步操作，这可以避免上下文切换，进而提升性能。

   ```csharp
   public async Task<string> FetchDataAsync()
   {
       using (var httpClient = new HttpClient())
       {
           // ConfigureAwait(false) 避免了在 UI 线程中恢复执行
           var response = await httpClient.GetStringAsync("https://example.com").ConfigureAwait(false);
           return response;
       }
   }
   
   
   public async Task UpdateUIAsync()
   {
       string data = await FetchDataAsync();
       // 更新 UI 控件，需要在 UI 线程上执行
       myLabel.Text = data; // 不使用 ConfigureAwait(false)，因为需要在 UI 线程上执行
   }
   
   ```

   **注意事项**

   - **UI 相关的代码不要使用 `ConfigureAwait(false)`**：如果在 UI 线程中需要访问或更新 UI 控件，确保不使用 `ConfigureAwait(false)`，否则会导致异常或无法更新 UI。
   - **保持一致性**：在库代码或服务代码中，最好始终使用 `ConfigureAwait(false)`，以确保代码的行为一致且性能最佳。

   **何时不使用 `ConfigureAwait(false)`**

   - 当你确实需要在某个特定的上下文中执行后续操作时，例如在 UI 线程中更新控件，应该避免使用 `ConfigureAwait(false)`。

3. **正确处理异常**：使用 try-catch 块来处理异步方法中的异常。

   ```csharp
       public async Task ProcessDataAsync()
       {
           try
           {
               var data = await FetchDataAsync();
               await ProcessAsync(data);
           }
           catch (HttpRequestException ex)
           {
               Console.WriteLine($"Failed to fetch data: {ex.Message}");
           }
           catch (Exception ex)
           {
               Console.WriteLine($"An error occurred: {ex.Message}");
           }
       }
   ```

4. **合理使用 Task.WhenAll 和 Task.WhenAny**：当需要并行执行多个异步操作时，这些方法非常有用。

   `Task.WhenAll` 和 `Task.WhenAny` 是用于并行执行和管理多个异步任务的常用方法。它们在处理多个异步操作时非常有用，能够帮助你简化代码并提高性能。下面分别介绍它们的用法和适用场景：

   **1. `Task.WhenAll`**

   `Task.WhenAll` 方法用于等待多个任务并行完成。当所有任务完成时，它会返回一个包含所有任务结果的数组。如果其中任何一个任务抛出异常，`Task.WhenAll` 会聚合这些异常并抛出一个 `AggregateException`。

   **适用场景**

   - 需要同时启动多个独立的异步操作，并且只有在所有操作都完成后才继续执行后续逻辑。
   - 适合处理需要同时获取多份数据的场景，比如并行地从多个API获取数据。

   **示例**

   ```csharp
   public async Task FetchAllDataAsync()
   {
       var httpClient = new HttpClient();
   
       // 创建多个异步操作的任务
       var task1 = httpClient.GetStringAsync("https://example.com/api/data1");
       var task2 = httpClient.GetStringAsync("https://example.com/api/data2");
       var task3 = httpClient.GetStringAsync("https://example.com/api/data3");
   
       // 使用 Task.WhenAll 等待所有任务完成
       string[] results = await Task.WhenAll(task1, task2, task3);
   
       // 处理所有任务的结果
       Console.WriteLine("Data 1: " + results[0]);
       Console.WriteLine("Data 2: " + results[1]);
       Console.WriteLine("Data 3: " + results[2]);
   }
   ```

   **2. `Task.WhenAny`**

   `Task.WhenAny` 方法用于等待多个任务中**第一个完成的任务**。它会在第一个任务完成时返回该任务，不论其成功或失败。可以用于需要尽早处理任务结果的场景。

   **适用场景**

   - 希望并行启动多个任务，并且在其中任何一个任务完成时就立即处理其结果。
   - 常用于竞态条件（race conditions）中，优先处理最快完成的任务，而忽略其余任务。

   **示例**

   ```csharp
   public async Task FetchFirstAvailableDataAsync()
   {
       var httpClient = new HttpClient();
   
       // 创建多个异步操作的任务
       var task1 = httpClient.GetStringAsync("https://example.com/api/data1");
       var task2 = httpClient.GetStringAsync("https://example.com/api/data2");
       var task3 = httpClient.GetStringAsync("https://example.com/api/data3");
   
       // 使用 Task.WhenAny 等待第一个完成的任务
       Task<string> firstCompletedTask = await Task.WhenAny(task1, task2, task3);
   
       // 处理第一个完成任务的结果
       string result = await firstCompletedTask; // 使用 await 获取结果
       Console.WriteLine("First completed data: " + result);
   }
   ```

5. **避免不必要的异步**：如果操作是快速完成的，使用同步方法可能更合适。

6. **使用异步流**：在C# 8.0 及以后版本中，可以使用异步流（IAsyncEnumerable）来处理异步数据序列。

   从 C# 8.0 开始，`IAsyncEnumerable` 和 `await foreach` 语法提供了一种新的方式来处理异步数据序列。这些特性非常适合处理从异步操作中逐步获取的数据流，比如从网络、数据库或文件系统中获取的数据流。

   `IAsyncEnumerable` 允许你处理异步的集合或数据流。在传统的 `IEnumerable` 中，你可以同步地遍历数据，而 `IAsyncEnumerable` 允许你异步地遍历数据，这对于处理大数据流或需要等待异步操作的数据源非常有用。

   **使用 `IAsyncEnumerable` 的主要特点**

   - **异步遍历**：你可以使用 `await foreach` 遍历 `IAsyncEnumerable`，每次获取一个数据项时都可以等待异步操作完成。
   - **节省内存**：适用于处理大数据集或无限数据流，因为数据是按需加载的，而不是一次性加载到内存中。
   - **与异步方法兼容**：非常适合与异步 API 一起使用，如从网络或数据库中逐步获取数据。

   **示例：如何使用异步流**

   以下是一个使用异步流的简单示例：

   ```csharp
   using System;
   using System.Collections.Generic;
   using System.Threading.Tasks;
   
   public class AsyncStreamExample
   {
       // 返回一个异步流的方法
       public async IAsyncEnumerable<int> GetNumbersAsync()
       {
           for (int i = 0; i < 10; i++)
           {
               await Task.Delay(100); // 模拟异步操作
               yield return i; // 逐步返回数据
           }
       }
   
       // 使用异步流的方法
       public async Task ProcessNumbersAsync()
       {
           await foreach (var number in GetNumbersAsync())
           {
               Console.WriteLine(number); // 处理每个数据项
           }
       }
   }
   ```

   **使用异步流的场景**

   1. **从外部数据源异步获取数据**：例如，逐步从远程 API 或数据库中获取数据。
   2. **处理大型数据集**：例如，逐步加载和处理大型文件内容或大数据集。
   3. **实现实时数据流**：例如，处理实时传输的数据流，如实时日志或传感器数据。

   **注意事项**

   - **异常处理**：`await foreach` 可以处理异步流中的异常，捕获 `IAsyncEnumerable` 的异常处理与常规的异步操作类似。
   - **性能优化**：尽管异步流能够处理大数据集，但也要注意性能优化，比如避免不必要的异步操作。

### 结论

C# 的 async/await 模型为异步编程提供了一个强大而直观的工具。相比 Java 的 CompletableFuture，C# 的方法更加简洁和易于理解。Task 和 ValueTask 的引入进一步增强了异步编程的灵活性和性能。

对于从 Java 转向 .NET Core 的开发者来说，掌握这些概念和最佳实践将大大提高编写高效、可维护的异步代码的能力。记住，异步编程不仅仅是about使用正确的关键字，更是关于理解并发和异步操作的本质，以及如何在实际应用中最好地利用它们。

通过不断练习和实践，你将能够充分利用 C# 异步编程的强大功能，创建响应更快、更高效的应用程序。

[异步编程 \- C# |Microsoft学习](https://learn.microsoft.com/zh-cn/dotnet/csharp/asynchronous-programming/)

[异步编程模式 | Microsoft Learn](https://learn.microsoft.com/zh-cn/dotnet/standard/asynchronous-programming-patterns/)

# 13 异常处理

异常处理是任何编程语言中的关键概念，对于从Java转向.NET Core的开发者来说，理解两种语言在异常处理上的异同点至关重要。虽然C#和Java在异常处理的基本概念上有很多相似之处，但C#提供了一些独特的特性和语法糖，可以使异常处理更加灵活和强大。让我们深入探讨C#的异常处理机制，并与Java进行对比。

## 13.1 基本异常处理结构

C#和Java的基本异常处理结构非常相似，都使用try-catch-finally块。

#### C#示例：

```csharp
try
{
    // 可能抛出异常的代码
    int result = 10 / 0;
}
catch (DivideByZeroException ex)
{
    Console.WriteLine($"除零错误：{ex.Message}");
}
catch (Exception ex)
{
    Console.WriteLine($"发生了一个错误：{ex.Message}");
}
finally
{
    Console.WriteLine("这里的代码总是会执行");
}
```

#### Java示例：

```java
try {
    // 可能抛出异常的代码
    int result = 10 / 0;
} catch (ArithmeticException ex) {
    System.out.println("除零错误：" + ex.getMessage());
} catch (Exception ex) {
    System.out.println("发生了一个错误：" + ex.getMessage());
} finally {
    System.out.println("这里的代码总是会执行");
}
```

主要区别：

2.  C#的异常类型名称略有不同，例如DivideByZeroException而不是ArithmeticException。
3.  C#使用\$进行字符串插值，这是一个更现代和方便的字符串格式化方法。

## 13.2 异常过滤器（C# 6.0+）

C#引入了异常过滤器，这是Java中没有的特性。异常过滤器允许你在catch块中添加额外的条件。

```csharp
try
{
    // 可能抛出异常的代码
}
catch (Exception ex) when (ex.Message.Contains("specific error"))
{
    Console.WriteLine("捕获到特定错误");
}
catch (Exception ex)
{
    Console.WriteLine($"其他错误：{ex.Message}");
}
```

这个特性在Java中是不存在的，Java需要在catch块内部使用if语句来实现类似的功能。

## 13.3 使用when关键字进行模式匹配（C# 7.0+）

C# 7.0引入了模式匹配，可以在catch块中使用when关键字结合模式匹配来处理异常。

```csharp
try
{
    // 可能抛出异常的代码
}
catch (Exception ex) when (ex is FileNotFoundException fnf)
{
    Console.WriteLine($"文件未找到：{fnf.FileName}");
}
catch (Exception ex) when (ex is UnauthorizedAccessException uae)
{
    Console.WriteLine($"访问被拒绝：{uae.Message}");
}
```

这种方式比Java的instanceof检查更加简洁和强大。

## 13.4 抛出异常

C#和Java在抛出异常方面非常相似，都使用throw关键字。

#### C#示例：

```csharp
if (someCondition)
{
    throw new ArgumentException("无效的参数");
}
```

#### Java示例：

```java
if (someCondition) {
    throw new IllegalArgumentException("无效的参数");
}
```

主要区别在于异常类的名称可能略有不同。

## 13.5 自定义异常

在C#和Java中创建自定义异常的方式也很相似，但C#提供了更多的构造函数选项。

#### C#示例：

```csharp
public class CustomException : Exception
{
    public CustomException() { }
    public CustomException(string message) : base(message) { }
    public CustomException(string message, Exception inner) : base(message, inner) { }
    protected CustomException(
        System.Runtime.Serialization.SerializationInfo info,
        System.Runtime.Serialization.StreamingContext context) : base(info, context) { }
}
```

#### Java示例：

```java
public class CustomException extends Exception {
    public CustomException() { }
    public CustomException(String message) { super(message); }
    public CustomException(String message, Throwable cause) { super(message, cause); }
    public CustomException(Throwable cause) { super(cause); }
}
```

C#的自定义异常类通常包括一个用于序列化的构造函数，这在Java中是不需要的。

## 13.6 异常处理中的性能考虑

C#和Java在异常处理的性能开销上有相似之处，但C#提供了一些独特的优化机会。

 1.     **使用Nullable类型避免异常：** C#的Nullable可以用来表示可能不存在的值，而不是抛出异常。

```csharp
 int? result = SomeMethod();
 if (result.HasValue)
 {
     Console.WriteLine(result.Value);
 }
 else
 {
     Console.WriteLine("No value");
 }

```

2.  **使用TryParse模式：** C#的许多内置类型提供TryParse方法，可以避免抛出异常。

    ```csharp
    if (int.TryParse(input, out int number))
    {
    Console.WriteLine($"解析成功：{number}");
    }
    else
    {
    Console.WriteLine("解析失败");
    }
    ```

3.  **异常过滤器的性能：** C#的异常过滤器不会展开堆栈，这可能比在catch块内部进行条件检查更高效。

## 13.7 异步方法中的异常处理

C#在异步方法中处理异常时特别强大，这要归功于async/await语法。

```csharp
public async Task ProcessAsync()
{
    try
    {
        await SomeAsyncMethod();
    }
    catch (SpecificException ex)
    {
        await HandleSpecificExceptionAsync(ex);
    }
    catch (Exception ex)
    {
        await LogExceptionAsync(ex);
        throw;
    }
}

private async Task SomeAsyncMethod()
{
    // 模拟一些异步操作
    await Task.Delay(1000);

    // 模拟抛出异常
    throw new SpecificException("Something went wrong!");
}

private async Task HandleSpecificExceptionAsync(SpecificException ex)
{
    // 处理特定异常的逻辑
    await Task.Delay(500);
    Console.WriteLine($"Handled SpecificException: {ex.Message}");
}

private async Task LogExceptionAsync(Exception ex)
{
    // 异常日志记录逻辑
    await Task.Delay(500);
    Console.WriteLine($"Logged Exception: {ex.Message}");
}

```

相比之下，Java的CompletableFuture需要使用不同的方法来处理异常：

```java
public CompletableFuture<Void> processAsync() {
    return someAsyncMethod()
        .exceptionally(ex -> {
            if (ex instanceof SpecificException) {
                return handleSpecificException((SpecificException) ex);
            } else {
                logException(ex);
                throw new CompletionException(ex);
            }
        });
}
```

C#的方法更加直观，并且更容易与同步代码保持一致的结构。

## 13.8 新的C# 8.0特性：Using声明

C# 8.0引入了using声明，这是一种简化资源管理和异常处理的新方法。

```csharp
public void ProcessFile(string path)
{
    using var file = new StreamReader(path);
    //文件会在方法结束时自动关闭
    // 即使发生异常也是如此
}
```

在使用 `using` 声明时，如果在代码块中发生了异常，资源的释放仍然会得到保障。即使抛出了异常，`using` 声明管理的资源也会在异常传播前被正确地释放。这是因为 `using` 声明的资源释放逻辑是在 `finally` 块中执行的，而 `finally` 块保证了无论是否发生异常，都会执行资源清理。

这比Java的try-with-resources语句更加简洁。

### 结论

虽然C#和Java在异常处理的基本概念上有很多相似之处，但C#提供了一些独特的特性，如异常过滤器、模式匹配和更简洁的资源管理语法。这些特性使得C#的异常处理更加灵活和强大。

对于从Java转向.NET Core的开发者来说，熟悉这些差异和新特性将有助于编写更加健壮和高效的代码。记住，好的异常处理不仅仅是捕获和抛出异常，还包括合理使用异常、优化性能以及提高代码的可读性和可维护性。

在实际开发中，建议充分利用C#提供的这些特性，同时保持对性能影响的警惑。适当的异常处理策略可以显著提高应用程序的稳定性和用户体验。继续探索和实践这些概念，你将能够在.NET Core环境中更加自如地处理各种异常情况。

# 14 文件I/O操作

文件I/O操作是几乎所有应用程序中的基本功能。对于从Java转向.NET Core的开发者来说，了解两个平台在文件处理上的异同点非常重要。虽然基本概念相似，但.NET Core提供了一些独特的特性和语法糖，使文件操作更加简洁和高效。让我们深入探讨.NET Core的文件I/O操作，并与Java进行对比。

## 14.1 基本文件操作

#### 读取文件

##### C#示例：

```csharp
string content = File.ReadAllText("path/to/file.txt");

// 或者逐行读取
string[] lines = File.ReadAllLines("path/to/file.txt");

// 使用流读取
using var stream = new StreamReader("path/to/file.txt");
string line;
while ((line = stream.ReadLine()) != null)
{
    Console.WriteLine(line);
}
```

##### Java示例：

```java
String content = new String(Files.readAllBytes(Paths.get("path/to/file.txt")));

// 或者逐行读取
List<String> lines = Files.readAllLines(Paths.get("path/to/file.txt"));

// 使用缓冲读取器
try (BufferedReader reader = new BufferedReader(new FileReader("path/to/file.txt"))) {
    String line;
    while ((line = reader.readLine()) != null) {
        System.out.println(line);
    }
}
```

主要区别：

1.  C#的`File`类提供了更多便捷的静态方法。
2.  C#使用`using`语句自动管理资源，而Java使用try-with-resources。
3.  C#的`StreamReader`类似于Java的`BufferedReader`。

#### 写入文件

##### C#示例：

```csharp
File.WriteAllText("path/to/file.txt", "Hello, World!");

// 追加内容
File.AppendAllText("path/to/file.txt", "Additional content");

// 使用流写入
using var writer = new StreamWriter("path/to/file.txt");
writer.WriteLine("Hello, World!");
writer.WriteLine("Another line");
```

##### Java示例：

```java
Files.write(Paths.get("path/to/file.txt"), "Hello, World!".getBytes());

// 追加内容
Files.write(Paths.get("path/to/file.txt"), "Additional content".getBytes(), StandardOpenOption.APPEND);

// 使用缓冲写入器
try (BufferedWriter writer = new BufferedWriter(new FileWriter("path/to/file.txt"))) {
    writer.write("Hello, World!");
    writer.newLine();
    writer.write("Another line");
}
```

主要区别：

1.  C#的`File`类方法更加直观。
2.  Java需要显式指定字符编码和打开选项。
3.  C#的`StreamWriter`类似于Java的`BufferedWriter`。

## 14.2 文件和目录操作

#### 文件操作

##### C#示例：

```csharp
// 检查文件是否存在
bool exists = File.Exists("path/to/file.txt");

// 复制文件
File.Copy("source.txt", "destination.txt", overwrite: true);

// 移动文件
File.Move("oldpath.txt", "newpath.txt");

// 删除文件
File.Delete("path/to/file.txt");
```

##### Java示例：

```java
// 检查文件是否存在
boolean exists = Files.exists(Paths.get("path/to/file.txt"));

// 复制文件
Files.copy(Paths.get("source.txt"), Paths.get("destination.txt"), StandardCopyOption.REPLACE_EXISTING);

// 移动文件
Files.move(Paths.get("oldpath.txt"), Paths.get("newpath.txt"), StandardCopyOption.REPLACE_EXISTING);

// 删除文件
Files.delete(Paths.get("path/to/file.txt"));
```

主要区别：

1.  C#使用静态`File`类方法，而Java使用`Files`类的静态方法。
2.  Java需要显式指定复制和移动的选项。

#### 目录操作

##### C#示例：

```csharp
// 创建目录
Directory.CreateDirectory("path/to/new/directory");

// 获取文件列表
string[] files = Directory.GetFiles("path/to/directory");

// 获取子目录列表
string[] subdirectories = Directory.GetDirectories("path/to/directory");

// 删除目录
Directory.Delete("path/to/directory", recursive: true);
```

##### Java示例：

```java
// 创建目录
Files.createDirectories(Paths.get("path/to/new/directory"));

// 获取文件列表
try (Stream<Path> paths = Files.list(Paths.get("path/to/directory"))) {
    paths.filter(Files::isRegularFile).forEach(System.out::println);
}

// 获取子目录列表
try (Stream<Path> paths = Files.list(Paths.get("path/to/directory"))) {
    paths.filter(Files::isDirectory).forEach(System.out::println);
}

// 删除目录
Files.walk(Paths.get("path/to/directory"))
    .sorted(Comparator.reverseOrder())
    .map(Path::toFile)
    .forEach(File::delete);
```

主要区别：

1.  C#的`Directory`类提供了更直观的方法。
2.  Java的目录操作更加灵活，但可能需要更多代码。
3.  C#删除目录可以通过一个方法调用完成，而Java需要遍历删除。

## 14.3 异步文件操作

.NET Core对异步文件操作的支持非常出色，这是相对于Java的一个显著优势。

##### C#示例：

```csharp
public async Task ProcessFileAsync(string filePath)
{
    string content = await File.ReadAllTextAsync(filePath);
    // 处理内容
    await File.WriteAllTextAsync("output.txt", processedContent);
}
```

##### Java示例：

Java的文件I/O操作主要是同步的。虽然可以使用`CompletableFuture`来模拟异步操作，但并不如C#原生支持那么方便：

```java
public CompletableFuture<Void> processFileAsync(String filePath) {
    return CompletableFuture.supplyAsync(() -> {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            // 处理内容
            Files.write(Paths.get("output.txt"), processedContent.getBytes());
            return null;
        } catch (IOException e) {
            throw new CompletionException(e);
        }
    });
}
```

主要区别：

1.  C#提供了原生的异步文件I/O方法。
2.  Java需要手动将同步操作包装在`CompletableFuture`中。

## 14.4 文件流和内存流

#### 文件流

##### C#示例：

```csharp
using var fileStream = new FileStream("path/to/file.txt", FileMode.Open, FileAccess.Read);
byte[] buffer = new byte[1024];
int bytesRead;
while ((bytesRead = await fileStream.ReadAsync(buffer, 0, buffer.Length)) > 0)
{
    // 处理读取的数据
}
```

##### Java示例：

```java
try (FileInputStream fis = new FileInputStream("path/to/file.txt")) {
    byte[] buffer = new byte[1024];
    int bytesRead;
    while ((bytesRead = fis.read(buffer)) != -1) {
        // 处理读取的数据
    }
}
```

#### 内存流

##### C#示例：

```csharp
using var memoryStream = new MemoryStream();
byte[] data = Encoding.UTF8.GetBytes("Hello, World!");
await memoryStream.WriteAsync(data, 0, data.Length);

memoryStream.Position = 0;
string result = Encoding.UTF8.GetString(memoryStream.ToArray());
```

##### Java示例：

```java
ByteArrayOutputStream baos = new ByteArrayOutputStream();
byte[] data = "Hello, World!".getBytes(StandardCharsets.UTF_8);
baos.write(data);

String result = baos.toString(StandardCharsets.UTF_8);
```

主要区别：

1.  C#的`Stream`类提供了异步方法，而Java的流操作主要是同步的。
2.  C#的`MemoryStream`可以直接重置位置，而Java的`ByteArrayOutputStream`需要转换为`ByteArrayInputStream`来重新读取。

## 14.5 文件监视

.NET Core和Java都提供了文件系统监视功能，但实现方式略有不同。

##### C#示例：

```csharp
using var watcher = new FileSystemWatcher("path/to/directory");
watcher.Created += OnFileCreated;
watcher.Changed += OnFileChanged;
watcher.Deleted += OnFileDeleted;
watcher.Renamed += OnFileRenamed;

// 启用或禁用 FileSystemWatcher 以开始或停止引发事件。设置为 true，表示开始监视。
watcher.EnableRaisingEvents = true;

// 事件处理方法
private static void OnFileCreated(object sender, FileSystemEventArgs e)
{
    Console.WriteLine($"File created: {e.FullPath}");
}
```

##### Java示例：

```java
// 获取要监控的目录路径
Path path = Paths.get("path/to/directory");

try (
    // 创建 WatchService 实例，负责监控文件系统的更改
    WatchService watchService = FileSystems.getDefault().newWatchService()
) {
    // 将路径注册到 WatchService，监控文件创建、修改和删除事件
    path.register(watchService, 
                  StandardWatchEventKinds.ENTRY_CREATE,   // 文件创建事件
                  StandardWatchEventKinds.ENTRY_MODIFY,   // 文件修改事件
                  StandardWatchEventKinds.ENTRY_DELETE);  // 文件删除事件

    // 无限循环等待事件发生
    while (true) {
        // 获取下一个 WatchKey，当有文件系统事件时才返回
        WatchKey key = watchService.take();
        
        // 处理 WatchKey 中的每个事件
        for (WatchEvent<?> event : key.pollEvents()) {
            // 输出事件的类型和受影响的文件名
            System.out.println("Event kind: " + event.kind() + ". File affected: " + event.context() + ".");
        }
        
        // 重置 WatchKey 以继续监控后续事件
        key.reset();
    }
} catch (IOException | InterruptedException e) {
    // 捕获并处理可能的 I/O 异常和线程中断异常
    e.printStackTrace();
}

```

主要区别：

1.  C#使用事件驱动模型，而Java使用轮询模型。
2.  C#的实现更加简洁和直观。
3.  Java的实现需要显式的循环和异常处理。

## 14.6 文件压缩

.NET Core和Java都提供了文件压缩功能，但API略有不同。

##### C#示例：

```csharp
using System.IO.Compression;

// 创建 zip 文件
using (var zipArchive = ZipFile.Open("archive.zip", ZipArchiveMode.Create))
{
    // 将 file1.txt 文件添加到 zip 压缩包中，压缩包中的文件名保持为 file1.txt
    zipArchive.CreateEntryFromFile("file1.txt", "file1.txt");
    
    // 将 file2.txt 文件添加到 zip 压缩包中，压缩包中的文件名保持为 file2.txt
    zipArchive.CreateEntryFromFile("file2.txt", "file2.txt");
}

// 解压 zip 文件
ZipFile.ExtractToDirectory("archive.zip", "extractPath");
```

##### Java示例：

```java
import java.util.zip.*;

// 创建zip文件
try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream("archive.zip"))) {
    addToZipFile("file1.txt", zos);
    addToZipFile("file2.txt", zos);
}

// 解压zip文件
try (ZipInputStream zis = new ZipInputStream(new FileInputStream("archive.zip"))) {
    ZipEntry zipEntry = zis.getNextEntry();
    while (zipEntry != null) {
        String fileName = zipEntry.getName();
        File newFile = new File("extractPath/" + fileName);
        // 提取文件
        // ...zipEntry = zis.getNextEntry();
    }
}

private static void addToZipFile(String fileName, ZipOutputStream zos) throws IOException {
    File file = new File(fileName);
    try (FileInputStream fis = new FileInputStream(file)) {
        ZipEntry zipEntry = new ZipEntry(fileName);
        zos.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }
        zos.closeEntry();
    }
}
```

主要区别：

1.  C#提供了更高级的`ZipFile`类，使压缩和解压操作更加简单。
2.  Java需要手动处理每个zip条目，代码较为冗长。
3.  C#的API设计更加用户友好，而Java的API提供了更底层的控制。

### 结论

虽然.NET Core和Java在文件I/O操作的基本概念上有许多相似之处，但.NET Core提供了一些独特的特性和更简洁的API，使得文件操作更加方便和高效。对于从Java转向.NET Core的开发者来说，主要需要注意以下几点：

1.  **静态方法vs对象方法**：C#更倾向于使用静态方法（如`File`和`Directory`类），而Java则更多地使用对象方法。

2.  **异步支持**：.NET Core对异步文件I/O的原生支持是一个显著优势，可以更容易地编写高性能的文件操作代码。

3.  **资源管理**：C#的`using`语句和Java的try-with-resources语句都提供了自动资源管理，但C#的语法更加简洁。

4.  **API设计**：总体而言，.NET Core的文件I/O API设计更加直观和用户友好，而Java的API则提供了更多的底层控制。

5.  **文件监视**：C#的事件驱动模型比Java的轮询模型更加简洁和易用。

6.  **压缩操作**：C#提供了更高级的API来处理文件压缩，使得操作更加简单。

在实际开发中，熟悉这些差异将有助于更快地适应.NET Core环境，并充分利用其提供的特性来编写高效、简洁的文件I/O代码。同时，.NET Core的跨平台特性也使得文件操作代码可以在不同的操作系统上运行，提供了更大的灵活性。

随着技术的不断发展，建议持续关注.NET Core在文件I/O方面的新特性和改进，以便在项目中充分利用这些优势。通过实践和深入学习，你将能够在.NET Core环境中更加得心应手地处理各种文件操作任务。  
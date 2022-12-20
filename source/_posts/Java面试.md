---
title: Java面试
date: 2022-12-20 14:40:05
tags:
- Java面试
categories: 
- 面试
---

## 1.自增变量

<img src="Java%E9%9D%A2%E8%AF%95/image-20221220144044920.png" alt="image-20221220144044920" style="zoom: 30%;" />

答案：

i = 4

j = 1

k = 11

## 2.单例模式

单例设计模式，即某个类在整个系统中只能有一个实例对象可被获取和使用的代码模式。

### 饿汉式

直接创建对象，不存在线程安全问题

#### 直接实例化饿汉式(简洁直观)

```java
/**
 * 饿汉式
 * 1.构造方法私有化
 * 2.自行创建，并且用静态变量
 * 3.保存向外提供这个实例
 * 4.强调这是一个单例，我们可以用final
 */
public class Singleton {

    final static Singleton SINGLETON = new Singleton();

    private Singleton() {
    }

}
```

#### 直接实例化饿汉式(简洁直观)

```java
/**
 * 使用枚举
 *
 * 枚举类的构造方法是私有的
 */
enum Singleton {

    SINGLETON

}
```

#### 静态代码块饿汉式(适合复杂实例化)

```java

/**
 * 使用静态代码块
 */
class Singleton {

    static Singleton singleton;

    static {
        Properties properties = new Properties();
        try {
            properties.load(Singleton.class.getClassLoader().getResourceAsStream("info.properties"));

            new Singleton((String) properties.get("info"));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Singleton(String info) {
    }
}
```

### 懒汉式

延迟创建对象，存在现在安全

#### 线程不安全（适用于单例）

```java
/**
* 1、构造器私有化
* 2、用一个静态变量保存这个唯一的实例
* 3、提供一个静态方法，获取这个实例对象
*/
public class Singleton {

    static Singleton4 instance;
    private Singleton4() {}

    public static Singleton4 getInstance() {
            if (instance == null) {
                instance = new Singleton4();
            }
            return instance;

    }
}

```

#### 线程安全（适用于多线程）

```java
/**
 * 懒汉式
 * 1、构造器私有化
 * 2、用一个静态变量保存这个唯一的实例
 * 3、提供一个静态方法，获取这个实例对象
 */
class Singleton {

    static Singleton singleton;

    private Singleton() {
    }

    public Singleton getInstance() {

        if (singleton == null) {//这一步主要是来优化程序，如果不为null就不用等获取锁，进去同步代码块判断了
            synchronized (Singleton.class) {
                if (singleton == null) {
                    singleton = new Singleton();
                }
            }
        }

        return singleton;
    }
}
```

#### 静态内部类模式 (适用于多线程)

```java
/**
 * 1、内部类被加载和初始化时，才创建INSTANCE实例对象
 * 2、静态内部类不会自动创建,随着外部类的加载初始化而初始化，他是要单独去加载和实例化的
 * 3、因为是在内部类加载和初始化时，创建的，因此线程安全
 */
class Singleton {

    private Singleton() {
    }

    static class innner {
        private static Singleton singleton = new Singleton();
    }

    public Singleton getInstance() {
        return innner.singleton;
    }
}
```


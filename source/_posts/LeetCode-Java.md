---
title: LeetCode_Java
date: 2023-01-03 21:25:02
tags:
- LeetCode_Java
categories: 
- LeetCode
---

### Java中 intValue，parseInt，Valueof 这三个关键字的区别

```
intValue()是把Integer对象类型变成int的基础数据类型；
 
parseInt()是把String 变成int的基础数据类型；
 
Valueof()是把给定的String参数转化成Integer对象类型；（现在JDK版本支持自动装箱拆箱了。）
 
intValue()用法与另外两个不同，比如int i = new Integer("123"),    j = i.intValue(); 相当于强制类型转换（强制类型转换事实上就是调用的这个方法）
 
另外两个用法：   Integer.Valueof()  ， Integer.parseInt()  用的是Interger类名。i.intValue()用的是对象i

 
另外：
 
Integer a=new Integer(1)
 
Integer a=Integer.valueOf(1);
 
两个都是得到一个Integer对象，但是Integer.valueOf的效率高。
```


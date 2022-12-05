---
title: ES6-ES11
date: 2022-8-31 15:10:56
update: 2022-10-17 19:04:30
comments: true
tags:
- ES
categories:
- 前端
---

# ES6新特性

## let

> **1.变量不能重复声明,var可以重复声明**
>
> **2.块级作用域**
>
> 3.不存在变量提升,即不可以在声明之前使用,而var却可以
>
> 4.不影响作用域链

```js
<script>
        // 声明变量
        // let a;
        // let b,c,d;
        // let f = 1,g = 'iloveyou',h=[]


        // 1.变量不能重复声明,var可以重复声明
        // let a = 0;
        // let a = 1//报错

        // 2.块级作用域  全局  函数  eval
        // if  else  while  for中的{}都可以
        // {
        //     let a = 0
        // }
        // console.log(a)//报错

        // 3.不存在变量提升,即不可以在声明之前使用,而var却可以
        // console.log(a)//报错
        // let a = 0

        // 4.不影响作用域链
        // {
        //     let a = 0
        //     function test() {
        //         console.log(a)
        //     }
        //     test()//输出:0
        // }


        
    </script>
```

### let案例

```html

<style>
    .item {
        height: 200px;
        width: 200px;
        border: 1px solid black;
    }
</style>

<body>

    <div>
        <h2>点击切换颜色 </h2>
        <div class="item"></div>
        <div class="item"></div>
        <div class="item"></div>
        <div class="item"></div>
    </div>


    <script>
        let items = document.getElementsByClassName('item')

        // for (var i = 0; i < items.length; i++) {
        for (let i = 0; i < items.length; i++) {
            items[i].onclick = function () {
                // this.style.background = 'red'
                items[i].style.background = 'red'
            }
        }


    </script>

</body>
```

## const

> **1.一定要赋初始值**
>
> **2.一般常量使用大写(潜规则)**
>
> **3.常量的值不能修改**
>
> **4.块级作用域**
>
> **5.对于数据和对象的元素修改,不算对常量的修改,不会报错**

```js
<script>
        //声明常量
        // const SCHOOL = 'wk'

        // 1.一定要赋初始值
        // const SCHOOL;//报错

        // 2.一般常量使用大写(潜规则)
        // const a = 100;

        // 3.常量的值不能修改
        // const a = 0
        // a = 1//报错

        // 4.块级作用域
        // {
        //     const a = 0
        // }
        // console.log(a)//报错

        // 5.对于数据和对象的元素修改,不算对常量的修改,不会报错
        // const el = ['a','b','c']
        // el.push('d')
        // console.log(el)//['a', 'b', 'c', 'd']
    </script>
```

## 解构赋值

```js
		
<script>
    	// ES6允许按照一定模式从数组和对象中提取值,对变量进行赋值,这被称为解构赋值
        // 1.数组的解构
        // const person = ['张三','李四','王五','赵六']
        // let [zhang,li,wang,zhao] = person//这里使用var也可以
        // console.log(zhang)
        // console.log(li)
        // console.log(wang)
        // console.log(zhao)


        // 2.对象的解构
        const zhang = {
            name: '张三',
            age: 18,
            play: function () {//对象中定义函数
                console.log('welcome to lol')
            }
        }

        // let {name,age,play} = zhang
        // console.log(name)
        // console.log(age)
        // console.log(play)
        // play()

        let { name } = zhang//此处的name与对象中的name对应
        console.log(name)

		let { name: username } = zhang//将name解构赋值出来，重命名为username
        console.log(username)

		// 3.连续解构赋值
		// const obj = { a: { b: { c: 1 } } }
        // const { b: { c } } = obj.a
        // console.log(c)

        //const obj = { a: { b: 1 } }
        // 连续解构赋值，并将b重命名为data
        //const { a: { b: data } } = obj
        //console.log(data)
    </script>
```

## 模板字符串

```js
 <script>
        //ES6引入新的声明字符串的方式 ` `   ' '   " " 
        // 1.声明
        // let str  = `我也是一个字符串哦!`
        // console.log(str,typeof str)

        // 2.内容中可以直接出现换行符
        // let str = `<ul>
        //             <li>111</li>
        //             <li>222</li>
        //         </ul>`

        // 3.变量拼接
        let person='瑶瑶'
        let out = `我最爱的人是${person}`
        console.log(out)
    </script>
```

## 简化对象写法

```js
<script>
        // ES6允许在大括号里面,直接写入变量和函数,作为对象的属性和方法
        let name = '张三'
        let test = function () {
            console.log("hello!!!!")
        }

        const person = {
            // name:name,//简化前
            name,//简化后
            // test:test,//简化前
            test,//简化后
            // test1:function(){//对象中定义函数
            test1() {//简化后
                console.log("你好啊!!!")
            }
        }

        console.log(person)
    </script>
```

## 箭头函数

> **1.this是静态的,this始终指向函数声明时所在的作用域下的this值**
>
> **2.不能作为构造实例化对象**
>
> **3.不能使用arguments变量**

```js
<script>
        // ES6允许使用箭头 => 定义函数
        // 声明一个函数
        // let fn = function(){}//常规声明
        // let fn = (a, b) => {//使用箭头函数
        //     return a + b
        // }
        // let result = fn(1,2)
        // console.log(result)

        // 1.this是静态的,this始终指向函数声明时所在的作用域下的this值
        // function getName(){
        //     console.log(this.name)
        // }
        // let getName2=()=>{
        //     console.log(this.name)
        // }

        // // 设置window对象的name属性
        // window.name='张三'
        // const perosn = {
        //     name:'zhangsan'
        // }

        // 直接调用
        // getName()//张三
        // getName2()//张三

        // call调用
        // getName.call(perosn)//zhangsan
        // getName2.call(perosn)//张三


        // 2.不能作为构造实例化对象
        // let person =(name,age)=>{
        //     this,name = name
        //     this.age = age
        // }
        // let me = new perosn('lz',21)
        // console.log(me)//报错
    
    	// 实例化对象。
        // function person(name) {
        //     this.name = name
        // }
        // let p = new person('张三')//实例化
        // console.log(p)//person {name: '张三'}

        // 3.不能使用arguments变量
        // let fn =()=>{
        //     console.log(arguments)
        // }
        // fn(1,2,3)//报错

        // 4.箭头函数的简写
        // 1)省略小括号,当形参有且只有一个的时候
        // let add = n => {
        //     return n + n
        // }
        // console.log(add(8))//16

        // 2)省略花括号,当代码体只有一条语句的时候,此时return必须省略,而且语句的执行结果就是函数的返回值
        //let pow = n => n * n
        //console.log(pow(8))

    </script>
```

### 补充（call()方法）

> 通过 call()，您能够使用属于另一个对象的方法。
>
> ```js
> var person = {
>     fullName() {
>         return this.firstName + " " + this.lastName;
>    	}
> }
> var person1 = {
>     firstName:"Bill",
>     lastName: "Gates",
> }
> var person2 = {
>     firstName:"Steve",
>     lastName: "Jobs",
> }
> person.fullName.call(person1);  // 将返回 "Bill Gates"
> ```

### 补充（js实例化)

>```js
>// 实例化对象。
>// function person(name) {
>//     this.name = name
>// }
>// let p = new person('张三')//实例化
>// console.log(p)//person {name: '张三'}
>```

### 补充（Arguments）

> `arguments` 是一个对应于传递给函数的参数的**类数组对象**。
>
> `arguments`对象是所有（非箭头）函数中都可用的**局部变量**。你可以使用`arguments`对象在函数中引用函数的参数。此对象包含传递给函数的每个参数，第一个参数在索引0处。
>
> ```JS
> function func1() {
> console.log(arguments)//类似python中的打包
> //输出：Arguments(3) [1,2,3, callee: ƒ, Symbol(Symbol.iterator): ƒ]
>     
> console.log(arguments[0]);
> // expected output: 1
> 
> console.log(arguments[1]);
> // expected output: 2
> 
> console.log(arguments[2]);
> // expected output: 3
> }
> 
> func1(1, 2, 3);
> 
> ```

### 箭头函数案例

```js
<script>
    let el = document.getElementById('root')
    // el.addEventListener('click', function () {//普通函数写法
    //     let _this = this
    //     setTimeout(function () {
    //         _this.style.background = 'red'
    //     }, 2000)
    // })
    el.addEventListener('click', function () {//使用箭头函数的写法
        setTimeout(() => {
            this.style.background = 'red'
        }, 2000)
    })


    const arr = [1,2,3,4,5,6,7,8,9,10]
    // let result = arr.filter(function(item){//普通函数
    //     if(item % 2 == 0 ){
    //         return true
    //     }else{
    //         return false
    //     }
    // })
    let result = arr.filter(item=>{//箭头函数
        if(item % 2 == 0 ){
            return true
        }else{
            return false
        }
    })
    console.log(result)
</script>
```

## 函数参数默认值

```js
<script>
        //ES6允许给函数参数赋值初始值
        // 1.形参初始值具有默认值的参数，一般位置要靠后（潜规则）
        // function fn(a,b,c=10){
        //     return a+b+c
        // }
        // let result = fn(1,2)//不传入第三个参数就是用默认值，传入就使用传入的值，和python中一样
        // console.log(result)


        // 2.与解构赋值结合
        // function fn(a){//直接传入对象，需要a.xxx
        //     console.log(a.host)
        //     console.log(a.username)
        //     console.log(a.password)
        //     console.log(a.port)
        // }

        // function fn({ host = '127.0.0.1', username, password, port }) {//与解构赋值结合。
        //     console.log(host)
        //     console.log(username)
        //     console.log(password)
        //     console.log(port)
        // }

        // fn({
        //     host: 'lz',//不传入第一个值，就使用默认值
        //     username: 'root',
        //     password: 'root',
        //     port: 3306
        // })
    </script>
```

## 函数的rest参数

```js
<script>
        // ES6引入rest参数，用于获取函数的实参，用来代替arguments
        // ES5获取实参的方式
        // function fn() {
        //     console.log(arguments)//输出类数组对象
        // }

        // fn(1,2,3,4)

        // function fn(...args) {
        //     console.log(args)//输出数组
        // }
        // fn(1, 2, 3, 4)


        // rest参数必须放到参数最后
        // function fn(a,b,...args) {
        //     console.log(a)//1
        //     console.log(b)//2
        //     console.log(args)//[3, 4, 5, 6, 7, 8, 9]
        // }
        // fn(1,2,3,4,5,6,7,8,9)
    </script>
```

## spread扩展运算符

```js
<script>
        // ...扩展运算符能将数组转换为都好分割的参数序列
        // 声明一个数组
        // const person = ['张三', '李四', '王五']


        // function fn() {
        //     console.log(arguments)//Arguments(3) ['张三', '李四', '王五', callee: ƒ, Symbol(Symbol.iterator): ƒ]
        // }
        // fn(...person)//相当于fn('张三', '李四', '王五')
    
    
    	const person={name:'lz',age:21}
		console.log(...person)//不可以展开一个对象
    </script>
```

### 扩展运算符应用

```js
<div></div>
    <div></div>
    <div></div>
    <script>
        // 1.数组的合并
        // const a = [1,2,3]
        // const b = [4,5,6]
        // const c = [...a,...b]
        // console.log(c)


        // 2.数组的克隆（浅克隆）
        // const a = [1,2,3]
        // const b= [...a]
        // console.log(b)


        // 3.将伪数组转为真正的数组
        // const divs = document.querySelectorAll('div')
        // const a = [...divs]
        // console.log(a)//[div, div, div]

        // 将arguments转为真正的数组
        // function fn() {
        //     const a = [...arguments]
        //     console.log(a)//[1, 2, 3, 4]
        // }
        // fn(1,2,3,4)

        // 4.向对象添加属性
        // const person1 = { name: '张三', age: 18 }
        // const person = { ...person1, address: '山东济宁' }
        // console.log(person)//{name: '张三', age: 18, address: '山东济宁'}


    	// 对象覆盖。键名相同的后面的覆盖前面的，键名不相同的保留
        // const person1 = { name: '张三', age: 18,address1:'山东济宁' }
        // const person2 = { name: '李四', age: 28,address2:'山东潍坊'}
        // const perosn = { ...person1, ...person2 }
        // console.log(perosn)//{name: '李四', age: 28，address1:'山东济宁',address2:'山东潍坊'}
    	/*
    		不能将对象直接展开，例如，const person ={name:'lz',age:21}   
    						   consolo.log(...person),报错
    	*/
    </script>
```

## Symbol（不懂）???

> - ES6引入了一种新的**原始数据类型 Symbol**，表示独一无二的值。它是JavaScript语言的第七种数据类型，是一种类似于字符串的数据类型。
> - Symbol特点
>   1. Symbol 的值是唯一的，**用来解决命名冲突的问题**
>   
>   2. Symbol值**不能与其他数据进行运算**
>   
>   3. Symbol定义的对象属性**不能使用`for...in`循环遍历**，但是可以使用**`Reflect.ownKeys`**来**获取对象的所有键名**
>   
>      - 静态方法 **`Reflect.ownKeys()`** 返回一个由目标对象自身的属性键组成的数组。
>   
>      ```js
>      		const object1 = {
>                  property1: 42,
>                  property2: 13
>              };
>      
>              const array1 = ['lz'];
>      
>              console.log(Reflect.ownKeys(object1));//["property1", "property2"]
>      
>              console.log(Reflect.ownKeys(array1));//['0', 'length']
>      ```

```js
<script>
        // 创建Symbol
        // let s = Symbol()
        // console.log(s, typeof s)//Symbol() 'symbol'

        // let s1 = Symbol('lz')
        // let s2 = Symbol('lz')
        // console.log(s1 === s2)//false

        // // Symbol.for创建
        // let s3 = Symbol.for('lz')
        // let s4 = Symbol.for('lz')
        // console.log(s3 === s4)//true


        //js中的数据类型
        // USONB   you are so NB
        // u    undefined
        // s    string  symbol
        // o    object
        // n    null    number
        // b    boolean
    </script>
```

### Symbol创建对象属性

```js
	<script>
        //向对象中添加方法 up down
        // let game = {
        //     name: '俄罗斯方块',
        //     up: function () { },
        //     down: function () { }
        // };

        // // 声明一个对象
        // let ob = {
        //     up: Symbol(),
        //     down: Symbol()
        // }

        // game[ob.up] = function () {
        //     console.log("我可以改变形状");
        // }

        // game[ob.down] = function () {
        //     console.log("我可以快速下降!!");
        // }

        // console.log(game)//{name: '俄罗斯方块', up: ƒ, down: ƒ, Symbol(): ƒ, Symbol(): ƒ}


        // let youxi = {
        //     name: '狼人杀',
        //     [Symbol('say')]: function () {
        //         console.log('我可以发言了')
        //     },
        //     [Symbol('zibao')]: function () {
        //         console.log('我可以自爆')
        //     }
        // }

        // console.log(youxi)//{name: '狼人杀', Symbol(say): ƒ, Symbol(zibao): ƒ}
    </script>
```

### Symbol内置属性

![image-20211111213614412](ES6-ES11/image-20211111213614412.png)

```js
<script>
        // class Person {
        //     static [Symbol.hasInstance](params) {
        //         console.log(params)
        //         console.log("我被用来检测类型了")
        //     }
        // }
        // let o = {}
        // console.log(o instanceof Person)


        // const arr = [1, 2, 3]
        // const arr2 = [4, 5, 6]
        // arr2[Symbol.isConcatSpreadable] =false//设置数组是否展开
        // console.log(arr.concat(arr2))
    </script>
```

## 迭代器???

> 遍历器（Iterator）就是一种机制。它是一种**接口**，为各种不同的数据结构提供统一问机制。任何
>
> **数据结构只要部署 Iterator 接口，就可以完成遍历操作。**
>
> 1) ES6 创造了一种新的遍历命令 **`for...of`** 循环，**Iterator 接口主要供 `for...of `消费**
>
> 2) 原生具备 iterator 接口的数据(可用 for...of 遍历)
>
> ​	a) Array
>
> ​	b) Arguments
>
> ​	c) Set
>
> ​	d) Map
>
> ​	e) String
>
> ​	f) TypedArray
>
> ​	g) NodeList
>
> 3) 工作原理
>
> ​	a) 创建一个指针对象，指向当前数据结构的起始位置
>
> ​	b) 第一次调用对象的 next 方法，指针自动指向数据结构的第一个成员
>
> ​	c) 接下来不断调用 next 方法，指针一直往后移动，直到指向最后一个成员
>
> ​	d) 每调用 next 方法返回一个包含 value 和 done 属性的对象
>
> **注**: **需要自定义遍历数据的时候，要想到迭代器。**

```js
<script>
        // 声明一个数组
        const persons = ['张三', '李四', '王五', '赵六']

        // for (var i in persons) {//for...in...遍历出的是索引
        //     console.log(i)
        // }
        // for (var i of persons) {//for...of...遍历出的是值
        //     console.log(i)
        // }


        // 原理
        let iterator = persons[Symbol.iterator]();
        // 调用对象的next方法
        console.log(iterator.next())//{value: '张三', done: false}
        console.log(iterator.next())//{value: '李四', done: false}
        console.log(iterator.next())//{value: '王五', done: false}
        console.log(iterator.next())//{value: '赵六', done: false}
        console.log(iterator.next())//{value: undefined, done: true}
    </script>
```

### 迭代器自定义遍历对象

```js
<script>
        //声明一个对象
        // const banji = {
        //     name: "终极一班",
        //     stus: [
        //         'xiaoming',
        //         'xiaoning',
        //         'xiaotian',
        //         'knight'
        //     ],
        //     [Symbol.iterator]() {
        //         //索引变量
        //         let index = 0;
        //         
        //         let _this = this;
        //         return {
        //             next: function () {
        //                 if (index < _this.stus.length) {
        //                     const result = { value: _this.stus[index], done: false };
        //                     //下标自增
        //                     index++;
        //                     //返回结果
        //                     return result;
        //                 } else {
        //                     return { value: undefined, done: true };
        //                 }
        //             }
        //         };
        //     }
        // }

        // //遍历这个对象 
        // for (let v of banji) {
        //     console.log(v);
        // }

    </script>
```

## 生成器函数???

> 1) \* 的位置没有限制
>
> 2) **生成器函数返回的结果是迭代器对象**，调用迭代器对象的 **next 方法可以得到yield 语句后的值**
>
> 3) yield 相当于函数的暂停标记，也可以认为是函数的分隔符，每调用一次 next方法，执行一段
>
> 代码
>
> 4) **next 方法可以传递实参，作为 yield 语句的返回值**

```js
<script>
        // 生成器其实就是一个特殊的函数
        // 异步编程。生成函数需要带一个 * 
        // yield函数代码的分隔符
        function * fen(){
            console.log('aaaaaaaaa')
            yield '1111111111111111'
            console.log('bbbbbbbbb')
            yield '2222222222222222'
            console.log('ccccccccc')
            yield '3333333333333333'
        }

        let iterator = fen()
        // iterator.next()//每次调用next方法都会执行fen生成器yield语句及前面部分
        // iterator.next()
        // iterator.next()
        // console.log(iterator.next())//{value: '1111111111111111', done: false}
        // console.log(iterator.next())//{value: '2222222222222222', done: false}
        // console.log(iterator.next())//{value: '3333333333333333', done: false}

        // 遍历
        // for(let v of fen()){
        //     console.log(v) //将yie遍历输出
        // }

    </script>
```

### 生成器函数参数

```js
<script>
        function * gen(arge){
            console.log(arge)//aaa
            let one = yield 111
            console.log(one)//bbb
            let two = yield 222
            console.log(two)//ccc
            let three = yield 333
            console.log(three)//ddd
        }

        let iterator = gen('aaa')
        console.log(iterator.next())
        console.log(iterator.next('bbb'))//第二个传参作为第一个yield返回值，以此类推
        console.log(iterator.next('ccc'))
        console.log(iterator.next('ddd'))
    </script>
```

### 生成器函数实例

```js
<script>
        // 异步编程
        // 1s 控制台输出1111   2s 控制台输出22222   3s  控制台输出333333
        // function one() {
        //     setTimeout(() => {
        //         console.log('111111111')
        //         iterator.next()
        //     }, 1000);
        // }
        // function two() {
        //     setTimeout(() => {
        //         console.log('222222')
        //         iterator.next()
        //     }, 2000);
        // }
        // function three() {
        //     setTimeout(() => {
        //         console.log('33333333')
        //         iterator.next()
        //     }, 3000);
        // }


        // function* gen() {
        //     one()
        //     two()
        //     three()
        // }

        // let iterator = gen()
        // iterator.next()



        function getUsers() {
            setTimeout(() => {
                let data = '用户数据'
                iterator.next(data)
            }, 1000);
        }
        function getOrders() {
            setTimeout(() => {
                let data = '订单数据'
                iterator.next(data)
            }, 1000);
        }
        function getGoods() {
            setTimeout(() => {
                let data = '商品数据'
                iterator.next(data)
            }, 1000);
        }

        function* gen() {
            let users = yield getUsers
            console.log(users)
            let orders = yield getOrders
            console.log(orders)
            let goods = yield getGoods
            console.log(goods)
        }

        let iterator = gen()
        iterator.next()


    </script>
```

## Promise基本语法

> Promise 是 ES6 引入的**异步编程的新解决方案**。语法上 Promise 是一个构造函数，用来封装异步操作并可以
>
> 获取其成功或失败的结果。
>
> Promise,简单来说就是一个容器，里面保存着某个未来才会结束的时间(通常是一个异步操作的结果)

> 是promise对象的
>
> 1. promise对象本身实例化
> 3. then方法的返回结果是promise对象
> 3. catch方法的返回结果是promise对象
> 4. async返回值是promise对象
> 5. axios请求

```js
<script>
        // 实例化Promise对象
        const p = new Promise(function (resolve, reject) {
            setTimeout(() => {
                // let data = '数据获取成功'
                // resolve(data)//使用resolve方法表示成功，对应.then中第一个参数函数执行

                let err = '数据获取失败'
                reject(err)//使用reject方法表示失败，对应.then中第二个参数函数执行
            }, 1000);
        })

        // 调用promise对象的then方法
        p.then(function (value) {//value接收resolve函数传的参
            console.log(value)
        }, function (reason) {//reason接收reject函数传的参
            console.log(reason)
        })
    </script>
```

### Promise读取文件

```js
//1. 引入 fs 模块
const fs = require('fs');

//2. 调用方法读取文件
// fs.readFile('./resources/为学.md', (err, data)=>{
//     //如果失败, 则抛出错误
//     if(err) throw err;
//     //如果没有出错, 则输出内容
//     console.log(data.toString());
// });

//3. 使用 Promise 封装
const p = new Promise(function(resolve, reject){
    fs.readFile("./file/江城子.md", (err, data)=>{
        //判断如果失败
        if(err) reject(err);
        //如果成功
        resolve(data);
    });
});

p.then(function(value){
    console.log(value.toString());
}, function(reason){
    console.log("读取失败!!");
});
```

### Promise封装AJAX

```js
<script>
        // 封装Promise方法
        const p = new Promise((resolve, reject) => {

            // 1.创建对象
            const xhr = new XMLHttpRequest();

            // 2.初始化
            xhr.open('GET', 'https://api.apiopen.top/getJoke')

            // 3.发送
            xhr.send()

            // 4.绑定事件，处理响应结果
            xhr.onreadystatechange = function () {
                // 判断
                if (xhr.readyState === 4) {
                    // 判断响应状态码200--299
                    if (xhr.status >= 200 && xhr.status < 300) {
                        // 表示成功
                        resolve(xhr.response);
                    } else {
                        // 如果失败
                        reject(xhr.status)
                    }
                }
            }
        })

        p.then(function (value) {
            console.log(value)
            // console.log(value.result)
            
        }, function (reason) {
            console.log(reason)
        })
    </script>
```

### Promise_then方法

> **then方法用于获取promise对象成功或失败返回值的数据**
>
> **then方法的返回结果是promise对象**，对象状态由回调函数的执行结果决定

```js
<script>
        // 创建promise对象
        const p = new Promise((reslove, reject) => {
            setTimeout(() => {
                reslove('用户数据')
                // reject('出错了')
            }, 1000);
        })


        // 调用then方法  then方法的返回结果是promise对象，对象状态由回调函数的执行结果决定
        // 1.如果回调函数中返回的结果是 非promise类型的数据，状态为成功，返回值为对象的成功值
        const result = p.then(function (value) {
            // console.log(value)
            // 1.非promise类型的属性。成功
            // return 'iloveyou'
            // 2.是promise对象。这里返回的promise是成功then返回值就是成功，这里返回的promise是失败then返回的就是失败
            // return new Promise((resolve, reject) => {
            //     resolve('ok')//[[PromiseResult]]:ok
            //     reject('error')//[[PromiseResult]]:error
            // })
            // 3.抛出错误。这里抛出错误，then返回值就是失败
            // throw new Error('出错了')//[[PromiseResult]]: Error: 出错了
            // throw '出错了'//[[PromiseResult]]: 出错了
        }, function (reason) {
            // console.log(reason)
            // 1.非promise类型的属性
            // return 'iloveyou'
            // 2.是promise对象
            // return new Promise((resolve, reject) => {
            //     resolve('ok')//[[PromiseResult]]:ok
            //     reject('error')//[[PromiseResult]]:error
            // })
            // 3.抛出错误
            // throw new Error('出错了')//[[PromiseResult]]: Error: 出错了
            // throw '出错了'//[[PromiseResult]]: 出错了
        })


        // 链式调用。利用的就是then方法返回值就是promise对象，就可以继续.then了
        // p.then(value => {
				return ...
        // }).then(value => {

        // })

        console.log(result)
    </script>
```

### Promise实践_读取多个文件

```js
const fs = require('fs')

// fs.readFile('./file/赤壁赋.md', (err, data1) => {
//     fs.readFile('./file/江城子.md', (err, data2) => {
//         fs.readFile('./file/声声慢.md', (err, data3) => {
//             let result = data1 + '\r\n' + data2 + '\r\n' + data3
//             console.log(result)
//         })
//     })
// })


// 使用promise实现
const P = new Promise((resolve, reject) => {
    fs.readFile('./file/赤壁赋.md', (err, data) => {
        resolve(data)
    })
})

P.then(value => {
    return new Promise((resolve, reject) => {
        fs.readFile('./file/声声慢.md', (err, data) => {
            resolve([value, data])
        })
    })
}).then(value => {
    return new Promise((resolve, reject) => {
        fs.readFile('./file/江城子.md', (err, data) => {
            // 压入
            value.push(data)
            resolve(value)
        })
    })
}).then(value => {
    console.log(value.toString())
})
```

### Promise_catch方法

> **catch方法用于获取promise对象失败返回值的数据**

```js
<script>
        let p = new Promise((resolve, reject) => {
            // 设置p对象的状态为失败，并设置失败的值
            reject('出错了')
        })

        // p.then(function (value) { }, function (reason) {
        //     console.log(reason)
        // })

		//catch方法相当于then方法只有第二个参数函数
        p.catch((reason) => {
            console.log(reason)
        })
    </script>
```

## Set集合

> ES6 提供了新的数据结构 Set（集合）。它类似于数组，但**成员的值都是唯一的**，**集合实现了 iterator接口**，
>
> 所以可以使用『扩展运算符』和『for...of...』进行遍历，集合的属性和方法：
>
> 1. size 返回集合的元素个数；
> 2. add 增加一个新元素，返回当前集合；
> 3. delete 删除元素，返回 boolean 值；
> 4. has 检测集合中是否包含某个元素，返回 boolean 值；
> 5.  clear 清空集合，返回 undefined；
>
> - 类似python中的集合，去重

```js
<script>
        // 声明一个set集合,set集合自动去重。和python中的集合类似以及方法
        // let s1 = new Set();
        // console.log(s1, typeof (s1))//Set(0) {size: 0} object

        // set参数传入一个可迭代对象
        let s2 = new Set([1, 2, 3, 1])
        // console.log(s2)
        // 元素个数
        // console.log(s2.size)
        // 添加新的元素
        // s2.add(4)
        // console.log(s2)
        // 删除元素
        // s2.delete(1)
        // console.log(s2)
        // 检测该元素在不在集合中
        // console.log(s2.has(1))
        // 清空
        // s2.clear()
        // console.log(s2)

        // for...of...遍历集合
        for (var v of s2) {
            console.log(v)
        }
    </script>
```

### Set集合案例

```js
<script>
        const arr = [1, 2, 3, 2, 4, 3, 5, 1, 6, 7, 5, 8, 9, 6]
        // 1.数组去重
        // let s1 = [...new Set(arr)]
        // console.log(s1)

        // 2.交集
        // let arr2 = [4, 5, 6, 4, 6]
        // let result = [...new Set(arr)].filter(item => {
        //     let s2 = new Set(arr2)
        //     if (s2.has(item)) {
        //         return true
        //     } else {
        //         false
        //     }
        // })
        // console.log(result)//[4, 5, 6]

        // let arr2 = [4, 5, 6, 4, 6]
        // let result = [...new Set(arr)].filter(item => new Set(arr2).has(item))
        // console.log(result)//[4, 5, 6]

        // 3.并集
        // let arr2 = [4, 5, 6, 4, 6]
        // const result = [...new Set([...arr, ...arr2])]
        // console.log(result)//(9) [1, 2, 3, 4, 5, 6, 7, 8, 9]


        // 4.差集
        // let arr2 = [4, 5, 6, 4, 6]
        // let result = [...new Set(arr)].filter(item => !new Set(arr2).has(item))
        // console.log(result)//[1, 2, 3, 7, 8, 9]
    </script>
```

## Map集合

> ES6 提供了 Map 数据结构。它类似于对象，也是**键值对的集合**。但是“键”的范围不限于字符串，各种类型的
>
> 值（包括对象）都可以当作键。Map 也实现了iterator 接口，所以可以使用『扩展运算符』和『for...of...』
>
> 进行遍历；Map 的属性和方法：
>
> 1. size 返回 Map 的元素个数；
> 2. set 增加一个新元素，返回当前 Map；
> 3. delete 删除一个元素
> 4. keys 获取键
> 5. values 获取键值
> 6. get 返回键名对象的键值；
> 7. has 检测 Map 中是否包含某个元素，返回 boolean 值；
> 8. clear 清空集合，返回 undefined；
>
> - 类似python中的字典

```js
<script>
        // 声明Map。类似python的字典
        // let m = new Map()
        // // 添加元素
        // m.set('name', '尚硅谷')//Map(1) {'name' => '尚硅谷'}
        // m.set('change', function () {//Map(1) {'change' => ƒ}
        //     console.log('hello')
        // })
        // let key = {
        //     school: '尚硅谷'
        // }
        // m.set(key, ['上海', '北京', '深圳'])//Map(1) {{…} => Array(3)}

        // size
        // console.log(m.size)

        // 删除
        // m.delete(key)

        // 获取
        // console.log(m.keys())//获取所有键
        // console.log(m.values())//获取所有值
        // console.log(m.get(key))//获取指定键对应的值
        // console.log(m)
    
    
    	//以数组作为参数传入构造函数。必须是二位数组，一维数组时会报错。
    	let m = new Map([['name','lz']])
        console.log(m)//Map(1) {size: 1, name => lz}

		//数组中传入多个值，只会使用到前两个值
		let m = new Map([['name','lz','a',1]])
        console.log(m)////Map(1) {size: 1, name => lz}
		
		//如果传入的是对象，不会报错，但也不能正常赋值。只会默认赋值为undifened
		let m = new Map([{'name':'lz'}])
        console.log(m)
    </script>
```

## Class类

```js
	<script>
        // ES5中的实例化写法
        function phone(brand, price) {
            //放在对象上的属性
            this.brand = brand
            this.price = price
        }

        // 放在对象原型上的属性
        phone.prototype.memory = '256g';

        // 往原型上添加方法添加方法
        phone.prototype.call = function () { }

        let p = new phone('Huawei', 5999)

        // 往对象上添加方法添加方法
        p.call1 = function () { }

        console.log(p)


        // ES6中class类的实例化
        class shouji {
            // 构造方法,名字不能修改
            // 放在对象上的属性
            constructor(brand, price) {
                this.brand = brand
                this.price = price
            }
            // 往原型上添加方法
            call() { }
        }

        // 放在原型上的属性
        shouji.prototype.memory = '256g';

        let s = new shouji('1+', 3999)
        // 往对象上添加方法
        s.call1 = function(){}
        s.call()
        console.log(s)

    </script>
```

### 类的静态成员

> 静态成员变量以及方法在实例化对象中访问不到。只有通过`类名.属性/方法访问`

```js
<script>
        // ES5写法
        // function phone() {

        // }

        // phone.name = '华为'
        // phone.change = function () {
        //     console.log('我可以改变世界！！！')
        // }
        // phone.prototype.size = '5.5inch'

        // let p = new phone()
        // console.log(p.name)//undefined
        // console.log(p.change)//undefined
        // console.log(p.size)//5.5inch
        // console.log(phone.name)//phone
        // console.log(phone.change)
        // // ƒ () {
        // //     console.log('我可以改变世界！！！')
        // // }
        // console.log(phone.size)//undefined


        // ES6写法
        class phone {
            // 静态属性
            static name = '华为'
            static change() {
                console.log('我可以改变世界')
            }
        }

        let p = new phone()
        console.log(phone.name)//华为
        console.log(phone.change)
        // ƒ change() {
        //         console.log('我可以改变世界')
        //     }
        console.log(p.name)//undefined
        console.log(p.change)//undefined

    </script>
```

### 类的继承

```js
<script>
        // ES5写法
        // function phone(brand, price) {
        //     this.brand = brand
        //     this.price = price
        // }
        // phone.prototype.call12 = function () {
        //     console.log('我可以打电话')
        // }

        // function smartPhone(brand, price, color, size) {
        //     phone.call(this, brand, price);
        //     this.color = color
        //     this.size = size
        // }

        // // 设置子级构造函数的原型
        // smartPhone.prototype = new phone;
        // smartPhone.prototype.constructor = smartPhone;

        // // 声明子类的方法
        // smartPhone.prototype.photo = function () {
        //     console.log('我可以拍照')
        // }
        // smartPhone.prototype.play = function () {
        //     console.log('我可以打游戏')
        // }

        // const p = new smartPhone('Huawei', 2499, '黑', '15.5inch')
        // console.log(p)


        // ES6写法，更贴合面向对象语言的写法，和java的写法类似
        class phone {
            constructor(brand, price) {
                this.brand = brand
                this.price = price
            }
            call() {
                console.log('我可以打电话')
            }
        }

        class smartPhone extends phone {
            constructor(brand, price, color, size) {
                super(brand, price)// phone.call(this, brand, price)
                this.color = color
                this.size = size
            }
            photo() {
                console.log('拍照')
            }
            play() {
                console.log('打游戏')
            }
            call() {
                console.log('我可以视频通话')//重写父类的方法
            }
        }

        let p = new smartPhone('Huawei', 2499, '黑', '15.5inch')
        console.log(p)
        p.call()
        p.photo()
        p.play()
    </script>
```

### class类的set和get方法

```js
<script>
        class phone {
            get price() {//price不是函数
                console.log('价格属性被读取了')
                return 'iloveyou'
            }
            set price(newVal) {
                console.log('价格属性被修改了')
            }
        }

        let p = new phone()
        p.price
        console.log(p.price)
        p.price = 2499
    </script>
```

## 数值扩展

> 1. Number.EPSILON是js表示的最小精度		epsilon——小的正数
> 2. Number.isFinite检测一个数值是否为有限数     finite——有限的
> 3. Number.isNaN检测一个数值是否为NaN
> 4. Number.isInteger判断一个数是否为整数      integer——整数
> 5. Math.trunc将数值的小数部分抹掉     trunc——将数字截尾取整
> 6. Math.sign判断一个数到底为正数 负数 还是零

```js
<script>
        // Number.EPSILON是js表示的最小精度
        // EPSILON属性的值接近于2.2204460492503130808472633361816E-16
        // function equal(a, b) {
        //     if (Math.abs(a - b) < Number.EPSILON) {
        //         return true
        //     } else {
        //         return false;
        //     }
        // }

        // console.log(0.1 + 0.2 === 0.3)//false
        // console.log(equal(0.1 + 0.2, 0.3))//true

        // 1.二进制和八进制
        // let b = 0b101010
        // let o = 0o156723
        // let d = 100
        // let x = 0xfff

        // console.log(b)//42
        // console.log(o)//56787
        // console.log(d)//100
        // console.log(x)//4095


        // 2.Number.isFinite检测一个数值是否为有限数
        // console.log(Number.isFinite(100))//true
        // console.log(Number.isFinite(100/0))//false
        // console.log(Number.isFinite(Infinity))//false

        // 3.Number.isNaN检测一个数值是否为NaN
        // 4.Number.parseInt   Number.parseFloat字符串转整数

        // 5.Number.isInteger判断一个数是否为整数
        // console.log(Number.isInteger(100))//ture
        // console.log(Number.isInteger(3.14))//false

        // 6.Math.trunc将数值的小数部分抹掉
        // console.log(Math.trunc(3.14))//3

        // 7.Math.sign判断一个数到底为正数 负数 还是零
        // console.log(Math.sign(100))//1
        // console.log(Math.sign(-100))//-1
        // console.log(Math.sign(0))//0
    </script>
```

## 对象方法扩展

> 1. Object.is判断两个值是否完全相等
> 2. Object.assign对象的合并
> 3. Object.setPrototypeOf设置原型对象
> 4. Object.getPrototypeOf获取原型对象

```js
<script>
        // 1.Object.is判断两个值是否完全相等。和===比较像，但又不完全一样
        // console.log(Object.is(120, 120))//true
        // console.log(Object.is(120, 121))//false
        // console.log(Object.is(NaN, NaN))//true
        // console.log(NaN === NaN)//false

        // 2.Object.assign对象的合并
        // const p1 = {
        //     name: '张三',
        //     age: 18,
        //     address1: '山东济宁'
        // }
        // const p2 = {
        //     name: '李四',
        //     age: 19,
        //     address2: '山东潍坊'
        // }
        // // console.log(Object.assign(p1, p2))//p2将p1中相同的覆盖，不相同的保留
        // console.log({ ...p1, ...p2 })//和Object.assign一样

        // 3.Object.setPrototypeOf设置原型对象  Object.getPrototypeOf获取原型对象
        // const person = {
        //     name: 'lz'
        // }
        // const city = {
        //     address: ['济宁', '潍坊']
        // }
        // Object.setPrototypeOf(person,city)
        // console.log(person)
        // console.log(Object.getPrototypeOf(person))
    </script>
```

## 模块化

```js
// 分别暴露
// export let person = 'lz'
// export function say() {
//     console.log('我是你爹')
// }

// 统一暴露
// let person = 'lz'
// function say() {
//     console.log('我是你爹')
// }
// export { person, say }

// // 默认暴露
// export default {
//     person: 'lz',
//     say() {
//         console.log('我是你爹')
//     }
// }
```

```js
<script type="module">
        // 1.通用的导入方式
        // 引入m1.js模块内容
        // import * as m1 from './file/m1.js'
        // console.log(m1)

        // // 默认暴露需要加一个default，它是放在default对象中的
        // m1.default.say()

        // 2.解构赋值形式
        // 分别暴露
        // import {person,say} from './file/m1.js'
        //  统一暴露
        // import {person,say} from './file/m1.js'
        //  默认暴露。默认暴露必须对defult重命名
        // import { default as m } from './file/m1.js'
        // console.log(m.person)

        // 3.简便形式。针对默认暴露。对随便命名一个变量名即可
        // import m from './file/m1.js'
        // console.log(m.person)

    </script>

    <!-- 另一种引入方式。新建一个入口文件app.js，将模块引入，然后在此引入app.js -->
    <script src="./file/app.js" type="module"></script>
```

<hr/>

# ES7新特性

> 1. includes：判断某元素是否存在数组中
> 2. **：幂

```js
	<script>
        // includes 判断某元素是否存在数组中    indexOf()
        // const person = ['张三', '李四', '王五']
        // console.log(person.includes('李四'))//true
        // console.log(person.includes('小明'))//false

        // ** 幂
        // console.log(2**10)//1024
        // console.log(Math.pow(2,10))//1024
    </script>
```

# ES8新特性

## async

> - **async：定义异步函数**
> - async 和 await 两种语法结合可以让异步代码看起来像同步代码一样；
>   1. **async 函数的返回值为 promise 对象；**
>   2. promise 对象的结果由 async 函数执行的返回值决定；
> - **async用于申明一个function是异步的**，类似promise，[但是还是有区别的](https://juejin.cn/post/6862999620009050119/)。
>
> 
>
> async-await 与promise的关系
> 不存在谁替代谁的，因为**async-await是寄生于Promise**。Generator 的语法糖。

```js
<script>
        // async函数，返回一个Promise对象
        // 返回的结果不是一个promise类型的对象，返回的结果就是成功promise对象
        async function fn() {
            // return 'lz'//返回一个promise对象
            // return;//返回一个promise对象

            // 抛出错误，返回的结果是一个失败promise对象
            // throw new Error('出错了')

            // 返回的结果如果是一个promise对象
            return new Promise((resolve, reject) => {
                // resolve('成功数据')//返回一个成功的promise对象
                reject('出错了')//返回一个失败的promise对象
            })
        }

        let result = fn()

        result.then(
            function (value) {
                console.log(value)
            },
            function (reason) {
                console.log(reason)
            })

    </script>
```

## await

> - **await:暂停异步函数的执行**
>   1. await 必须写在 async 函数中；async中不必须有await
>   2. await 右侧的表达式一般为 promise 对象；
>   3. **await 返回的是 promise 成功的值；**
>   4. **await 的 promise 失败了, 就会抛出异常, 需要通过 try...catch 捕获处理；**
>
> 
>
> **用await修饰的异步方法可以直接获得promise对象的值。等待这个异步方法执行完成。**

```js
<script>
        let p = new Promise((resolve, reject) => {
            // resolve('成功的数据')
            reject('出错了')
        })

        // await要放在async中
        async function fn() {
            try {
                let a = await p//await返回promise对象成功或失败的数据
                console.log(a)
            } catch (error) {
                console.log(error)
            }

        }

        fn()
    </script>
```

### async和await结合读取多个文件

```js
const fs = require('fs')

function readCibifu() {
    return new Promise((resolve, reject) => {
        fs.readFile("./file/赤壁赋.md", (err, data) => {
            if (err) {
                reject(err)
            } else {
                resolve(data)
            }
        })
    })
}
function readJangchengzi() {
    return new Promise((resolve, reject) => {
        fs.readFile("./file/江城子.md", (err, data) => {
            if (err) {
                reject(err)
            } else {
                resolve(data)
            }
        })
    })
}
function readShengshengman() {
    return new Promise((resolve, reject) => {
        fs.readFile("./file/声声慢.md", (err, data) => {
            if (err) {
                reject(err)
            } else {
                resolve(data)
            }
        })
    })
}

async function fn() {
    let cibifu = await readCibifu()
    let jangchengzi = await readJangchengzi()
    let shengshengman = await readShengshengman()

    console.log(cibifu.toString())
    console.log(jangchengzi.toString())
    console.log(shengshengman.toString())
}
fn()
```

### async和await封装AJAX请求

```js
 <script>
        // 发送AJAX请求，返回的结果是Promise对象
        function sendAJAX(url) {
            return new Promise((resolve, rejcet) => {
                // 1.创建对象
                const x = new XMLHttpRequest();

                // 2.初始化
                x.open('GET', url)

                // 3.发送
                x.send()

                // 4.事件绑定
                x.onreadystatechange = function () {
                    if (x.readyState === 4) {
                        if (x.status >= 200 && x.status <= 300) {
                            resolve(x.response)
                        } else {
                            rejcet(x.status)
                        }
                    }
                }
            })
        }

        // promise then方法测试
        // sendAJAX('https://api.apiopen.top/getJoke').then(function (value) {
        //     console.log(value)
        // }, function (reason) { })


        // async和await测试
        async function fn() {
            let result = await sendAJAX('https://api.apiopen.top/getJoke')
            console.log(result)
        }
        fn()
    </script>
```

## 对象方法扩展

### 对象的[ ]

### Object.keys()

### Object.values()

### Object.entries()

```js
<script>
        const school = {
            name: '尚硅谷',
            city: ['上海', '深圳', '北京'],
            xueke: ['前端', 'java', '数据库']
        }

        // 获取对象所有键
        // console.log(Object.keys(school))//['name', 'city', 'xueke']
        // console.log(school.keys())//报错，没有keys()这个函数，只有在Map集合中可以使用

        // 获取对象所有值
        // console.log(Object.values(school))//['尚硅谷', Array(3), Array(3)]


		// 对象的[ ]
		const person = { name: 'lz' }
		//对对象元素的添加和修改
		person['age'] = '18'
		person['age'] = '21'

		console.log(Object.keys(person))
		console.log(Object.values(person))

		// 调用变量
		console.log(person.name)//lz
        console.log(person['name'])//lz

		//delete语句
		const obj = { a: 1, b: 2 }
        delete obj['a']
        console.log(obj)


        // entries。整个对象变成一个数组，对象中的每一个键值对放到一个数组中
        // console.log(Object.entries(school))
        /**
         *  [Array(2), Array(2), Array(2)]
            0: (2) ['name', '尚硅谷']
            1: (2) ['city', Array(3)]
            2: (2) ['xueke', Array(3)]
         */

        //  借助entries创建Map
        // const m = new Map(Object.entries(school))
        // console.log(m)//Map(3) {'name' => '尚硅谷', 'city' => Array(3), 'xueke' => Array(3)}
        // console.log(m.get('city'))// ['上海', '深圳', '北京']

		
    	//let m = new Map([['name','lz']])
        //console.log(m)//Map(1) {size: 1, name => lz}


    </script>
```

### Object.getOwnPropertyDescriptors()

> 该方法返回指定对象所有自身属性的描述对象

```js
const person = {
    name: 'lz',
    age: 22
}      
console.log(Object.getOwnPropertyDescriptors(person));
//打印出来是一个对象,也有键值，键跟传入对象的键一样，值也是一个对象（包含value及属性特性）

//跟用Object.create创建的对象类似
const obj = Object.create(null, {
    name: {
        //设置值
        value: 'USC',
        //属性特性
        writable: true,	//是否可写
		configurable: true,	//是否可删除
        enumerable: true	//是否可枚举
    }
});
```

![image-20221012163908931](ES6-ES11/image-20221012163908931.png)

# ES9新特性

## spread扩展运算符和rest参数

```js
<script>
        // Rest参数与spread扩展运算符在ES6中已经引入，不过ES6只针对于数组
        // 在ES9中为对象提供了像数组一样的rest参数和扩展运算符

        // function fn({name,...userInfo}) {
        //     console.log(name)
        //     console.log(userInfo)//{age: 18, school: 'wk'}
        // }

        // function fn1(...user) {
        //     console.log(user)//[1, 2, 3]
        // }

        // fn({
        //     name: 'lz',
        //     age: 18,
        //     school: 'wk'
        // })

        // fn1(1, 2, 3)


        // const skillOne = {
        //     q: '天音波'
        // }
        // const skillTwo = {
        //     w: '金钟罩'
        // }
        // const skillThree = {
        //     e: '天雷破'
        // }
        // const skillFour = {
        //     r: '神龙摆尾'
        // }

        // const skill = { ...skillOne, ...skillTwo, ...skillThree, ...skillFour }
        // console.log(skill)//{q: '天音波', w: '金钟罩', e: '天雷破', r: '神龙摆尾'}
    </script>
```

## 正则

> **exec() 方法用于检索字符串中的正则表达式的匹配。**如果 exec() 找到了匹配的文本，则**返回一个结果数组。**否则，返回 null。此数组的第 0 个元素是与正则表达式相匹配的文本，第 1 个元素是与 RegExpObject 的第 1 个子表达式相匹配的文本（如果有的话），第 2 个元素是与 RegExpObject 的第 2 个子表达式相匹配的文本（如果有的话），以此类推。

### 正则命名分组

```js
<div id="root">
        <a href="http://www.baidu.com">百度</a>
    </div>
    <script>
        // 命名捕获分组

        // 声明一个字符串
        // let str = '<a href="http://www.baidu.com">百度</a>'

        // // 提取url和标签文本.
        // .*匹配零个或多个除换行符以外的字符
        // const reg = /<a href="(.*)">(.*)<\/a>/  //注意href后面不要有空格

        // const result = reg.exec(str)
        // console.log(result)//['<a href="http://www.baidu.com">百度</a>', 'http://www.baidu.com', '百度', index: 0, input: '<a href="http://www.baidu.com">百度</a>', groups: undefined]

        // console.log(result[1])//http://www.baidu.com
        // console.log(result[2])//百度



        // let str = '<a href="http://www.baidu.com">百度</a>'
        // const reg = /<a href="(?<url>.*)">(?<text>.*)<\/a>/

        // const result = reg.exec(str)
        // console.log(result)
        /**
         * groups:
            text: "百度"
            url: "http://www.baidu.com"
         */


        let el = document.getElementById('root').innerHTML

        const reg = /<a href=(?<url>.*)>(?<text>.*)<\/a>/

        const result = reg.exec(el)
        console.log(el)
        console.log(result)

    </script>
```

### 正则断言

```js
<script>
        // 声明字符串
        let str = 'js5211314你知道么555555啦啦啦'

        // // 正向断言
		// \d+配置至少一个数字
        // const reg = /\d+(?=啦啦啦)/
        // const result = reg.exec(str)

        // 反向断言
        // const reg = /(?<=么)\d+/   //？是方向断言中必带的
        // const result = reg.exec(str)
        // console.log(result)
    </script>
```

### 正则dotAll模式

```js
<script>
        // dot  .   元字符  匹配除换行符以外的任意单个字符

        let str = `
            <ul>
                <li>
                    <a>肖申克的救赎</a>
                    <p>上映时间：1994-09-10</p>
                </li>
                <li>
                    <a>阿甘正传</a>
                    <p>上映时间：1994-09-10</p>
                </li>
            </ul>`

        // 声明正则
        const reg = /<li>.*?<a>(.*?)<\/a>.*?<p>(.*?)<\/p>/gs    //加一个？是禁止贪婪。g是全局。s是使用元字符必须添加的

        // 匹配执行
        // const result = reg.exec(str)
        let result
        let data = []
        while (result = reg.exec(str)) {
            console.log(result)
            data.push({ title: result[1], time: result[2] })
        }
        console.log(data)
        /*
            0: {title: '肖申克的救赎', time: '上映时间：1994-09-10'}
            1: {title: '阿甘正传', time: '上映时间：1994-09-10'}
        */
    </script>
```

# ES10新特性

## Object.fromEntries

```js
<script>
        // 二维数组
        // const result = [
        //     ['name', 'lz']
        // ]
        // console.log(Object.fromEntries(result))//{name: 'lz'}

        // Map集合
        const m = new Map()
        m.set('name', 'lz')
        console.log(Object.fromEntries(m))//{name: 'lz'}
    </script>
```

## trimStart和trimEnd

```js
	<script>
        let str ='    iloveyou    '
        console.log(str)
        console.log(str.trimStart())//清除开始的空格
        console.log(str.trimEnd())//清除结束的空格
        console.log(str.trim())//清楚两端的空格
    </script>
```

## Array.prototype.flat和flatMap

```js
<script>
        //flat 将多维数组转化为低维数组
        // const arr = [1,2,3,[4,5]]
        // console.log(arr.flat())//(5) [1, 2, 3, 4, 5]


        // const arr = [1, 2, 3, [4, 5, [6, 7]]]
        // // flat参数为深度，是一个数字  3-1=2
        // console.log(arr.flat(2))//(7) [1, 2, 3, 4, 5, 6, 7]
   


        const arr1 = [1, 2, [3], [4, 5], 6, []];
		const flattened = arr1.flatMap(num => num);
		console.log(flattened);
		// expected output: Array [1, 2, 3, 4, 5, 6]
   </script>
```

## Symbol.prototype.description

> `description` 是一个只读属性，它会返回 [`Symbol`](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/Symbol) 对象的可选描述的字符串。

```js
<script>
        let s = Symbol('lz')
        console.log(s.description)//lz
    </script>
```

# ES11新特性

## 私有属性

```js
<script>
        class person {
            name;
            #age;
            #weight;
            constructor(name, age, weight) {
                this.name = name;
                this.#age = age;
                this.#weight = weight
            }

            personInfo() {
                console.log(this.#age)//可以在类的内部访问私有属性
                console.log(this.#weight)
            }
        }

        let p = new person('lz', 21, 160)
        console.log(p)//person {name: 'lz', #age: 21, #weight: 160}
        // console.log(p.#age)//不允许在类外部访问私有属性
        // console.log(p.#weightS)

        p.personInfo()
    </script>
```

## Promise.allSettle和Promise.all

```js
    <script>
        let p1 = new Promise((resolve, reject) => {
            setTimeout(() => {
                // resolve('成功数据-1')
                reject('出错了-1')
            }, 1000);
        })
        let p2 = new Promise((resolve, reject) => {
            setTimeout(() => {
                resolve('成功数据-2')
                // reject('出错了-2')
            }, 1000);
        })

        // Promise.allSettle执行始终是成功的，返回p1和p2的成功或失败结果
        // const result = Promise.allSettled([p1, p2])
        /*
        [[PromiseResult]]: Array(2)
            0: {status: 'rejected', value: '出错了-1'}
            1: {status: 'fulfilled', value: '成功数据-2'}
        */



        // 相当于 &&　有一个失败，执行就失败
        let result = Promise.all([p1, p2])
        /*
        都成功：
            [[PromiseResult]]: Array(2)
                0: "成功数据-1"
                1: "成功数据-2"

        有一个失败：Uncaught (in promise) 出错了-1
        */
        console.log(result)
    </script>
```

## String.prototype.matchAll

> **`matchAll()`** 方法返回一个包含所有匹配正则表达式的结果及分组捕获组的迭代器。

```js
<script>
        let str = `
            <ul>
                <li>
                    <a>肖申克的救赎</a>
                    <p>上映时间：1994-09-10</p>
                </li>
                <li>
                    <a>阿甘正传</a>
                    <p>上映时间：1994-09-10</p>
                </li>
            </ul>`
        // 声明正则表达式
        let reg = /<li>(.*?)<a>(.*?)<\/a>(.*?)<p>(.*?)<\/p>/gs

        //result赋值为可迭代对象
        const result = str.matchAll(reg)
        for (var i of result) {
            console.log(i)
        }
    </script>
</body>
```

## 可选链操作符

```js
<script>
        function fn(config) {
            // 先看config有吗,再看config.db有吗,再看config.db.host
            // let result = config && config.db && config.db.host
    
    		// let result = config?.db?.host

            // 这样传参了还好,如果不传参直接报错,上两种方式不会报错,直接undefined
            // let result = config.db.host

        }

        fn({
            db: {
                host: '192.168.0.1',
                password: 'root'
            },
            cache: {
                host: '192.168.0.2',
                password: 'admin'
            }
        })
    </script>
```

## 动态import加载

```js
export function hello(){
    alert('hello!!!!')
}
```

```js
let btn = document.getElementById('btn')

btn.onclick = function () {
    // import函数的返回结果是Promise对象
    import('./hello.js').then(module => {
        // module中包含着hello.js中的函数
        console.log(module)
        module.hello()
    })
}
```

```js
<body>
    <button id="btn">点击弹窗</button>
    <!-- js的外联式引入 -->
    <script src="./file/main.js"></script>
</body>
```

## BigInt

```js
<script>
        // 大整型
        // let n = 521n
        // console.log(n,typeof n)//521n bigint

        // 函数
        // let n = 521
        // console.log(BigInt(n))//521n


        // 大整型运算
        let num = Number.MAX_SAFE_INTEGER
        console.log(num)//9007199254740991
        console.log(num + 1)//9007199254740992
        console.log(num + 2)//9007199254740992

        // 大整数据不可以和普通整型运算
        console.log(BigInt(num) + BigInt(1))//9007199254740992n
        console.log(BigInt(num) + BigInt(2))//9007199254740993n
    </script>
```

## globalThis

```js
<script>
        // 始终指向全局window
        console.log(globalThis)//Window {window: Window, self: Window, document: document, name: '', location: Location, …}
    </script>
```

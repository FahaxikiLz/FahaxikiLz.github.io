---
title: Mysql和JDBC
date: 2022-8-31 15:10:56
categories: 
- 数据库
tags: 
- MySql
- JDBC
---

# MySQL基础篇

## [Windows安装MySQL](https://blog.csdn.net/wangpaiblog/article/details/112000033)

## Linux安装Mysql

### 1.下载Linux版MySQL安装包

> 下载地址：[MySQL :: Download MySQL Community Server (Archived Versions)](https://downloads.mysql.com/archives/community/)

![image-20221030150821856](MySql%E5%92%8CJDBC/image-20221030150821856.png)

### 2.上传MySQL安装包

### 3.创建目录,并解压

```
mkdir mysql

tar -xvf mysql-8.0.26-1.el7.x86_64.rpm-bundle.tar -C mysql
```

### 4. 安装mysql的安装包

```
cd mysql

rpm -ivh mysql-community-common-8.0.26-1.el7.x86_64.rpm 

rpm -ivh mysql-community-client-plugins-8.0.26-1.el7.x86_64.rpm 

rpm -ivh mysql-community-libs-8.0.26-1.el7.x86_64.rpm 

rpm -ivh mysql-community-libs-compat-8.0.26-1.el7.x86_64.rpm

yum install openssl-devel

rpm -ivh  mysql-community-devel-8.0.26-1.el7.x86_64.rpm

rpm -ivh mysql-community-client-8.0.26-1.el7.x86_64.rpm

rpm -ivh  mysql-community-server-8.0.26-1.el7.x86_64.rpm

```

### 5.启动MySQL服务

```
systemctl start mysqld
```

```
systemctl restart mysqld
```

```
systemctl stop mysqld
```

### 6.查询自动生成的root用户密码

```
grep 'temporary password' /var/log/mysqld.log
```

> 命令行执行指令 :

```
mysql -u root -p
```

> 然后输入上述查询到的自动生成的密码, 完成登录 .

### 7.修改root用户密码

> 登录到MySQL之后，需要将自动生成的不便记忆的密码修改了，修改成自己熟悉的便于记忆的密码。

```
ALTER  USER  'root'@'localhost'  IDENTIFIED BY '1234';
```

> 执行上述的SQL会报错，原因是因为设置的密码太简单，密码复杂度不够。我们可以设置密码的复杂度为简单类型，密码长度为4。

```
set global validate_password.policy = 0;
set global validate_password.length = 4;
```

> 降低密码的校验规则之后，再次执行上述修改密码的指令。

### 8.创建用户

> 默认的root用户只能当前节点localhost访问，是无法远程访问的，我们还需要创建一个root账户，用户远程访问

```
create user 'root'@'%' IDENTIFIED WITH mysql_native_password BY '1234';
```

### 9.并给root用户分配权限

```
grant all on *.* to 'root'@'%';
```

### 10.重新连接MySQL

```
mysql -u root -p
```

### 11.通过Navicat远程连接MySQL

## 所用sql脚本

```mysql
-- 如果test数据库不存在，就创建test数据库：
CREATE DATABASE IF NOT EXISTS test;

-- 切换到test数据库
USE test;

-- 删除classes表和students表（如果存在）：
DROP TABLE IF EXISTS classes;
DROP TABLE IF EXISTS students;

-- 创建classes表：
CREATE TABLE classes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 创建students表：
CREATE TABLE students (
    id BIGINT NOT NULL AUTO_INCREMENT,
    class_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    gender VARCHAR(1) NOT NULL,
    score INT NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 插入classes记录：
INSERT INTO classes(id, name) VALUES (1, '一班');
INSERT INTO classes(id, name) VALUES (2, '二班');
INSERT INTO classes(id, name) VALUES (3, '三班');
INSERT INTO classes(id, name) VALUES (4, '四班');

-- 插入students记录：
INSERT INTO students (id, class_id, name, gender, score) VALUES (1, 1, '小明', 'M', 90);
INSERT INTO students (id, class_id, name, gender, score) VALUES (2, 1, '小红', 'F', 95);
INSERT INTO students (id, class_id, name, gender, score) VALUES (3, 1, '小军', 'M', 88);
INSERT INTO students (id, class_id, name, gender, score) VALUES (4, 1, '小米', 'F', 73);
INSERT INTO students (id, class_id, name, gender, score) VALUES (5, 2, '小白', 'F', 81);
INSERT INTO students (id, class_id, name, gender, score) VALUES (6, 2, '小兵', 'M', 55);
INSERT INTO students (id, class_id, name, gender, score) VALUES (7, 2, '小林', 'M', 85);
INSERT INTO students (id, class_id, name, gender, score) VALUES (8, 3, '小新', 'F', 91);
INSERT INTO students (id, class_id, name, gender, score) VALUES (9, 3, '小王', 'M', 89);
INSERT INTO students (id, class_id, name, gender, score) VALUES (10, 3, '小丽', 'F', 85);

-- OK:
SELECT 'ok' as 'result:';
```

## DDL（数据定义语言）

### 操作数据库

> - 列出所有数据库
>
>   ```mysql
>   show databases;
>   ```
>
> - 创建数据库
>
>   ```mysql
>   create datebase 数据库名;
>   ```
>
> - 删除数据库
>
>   ```mysql
>   drop detabase 数据库名;
>   ```
>
> - 对一个数据库进行操作
>
>   ```mysql
>   use 数据库名;
>   ```

### 操作表

> - 查看所有数据库表
>
>   ```mysql
>   show tables;
>   ```
>
> - 创建数据库表
>
>   ```mysql
>   CREATE TABLE 表名(
>   	字段1 字段1类型 [COMMENT 字段1注释],
>   	字段2 字段2类型 [COMMENT 字段2注释],
>   	字段3 字段3类型 [COMMENT 字段3注释],
>   	...
>   	字段n 字段n类型 [COMMENT 字段n注释]
>   )[ COMMENT 表注释 ];
>   ```
>
> - 删除数据库
>
>   ```mysql
>   drop table 数据库名;
>   ```
>
> - 查看一个表的结构
>
>   ```mysql
>   DESC 数据库表名;
>   ```
>
> - 查看创建表的SQL语句
>
>   ```mysql
>   show create table 数据库表名;
>   ```
>
> - 修改表就比较复杂。如果要给`students`表新增一列`birth`
>
>   ```mysql
>   alert table students add COLUMN birth VARCHAR(10) NOT NULL;
>   ```
>
> - 要修改`birth`列，例如把列名改为`birthday`，类型改为`VARCHAR(20)`
>
>   ```mysql
>   ALTER TABLE students CHANGE COLUMN birth birthday VARCHAR(20) NOT NULL;
>   ```
>
> - 删除列
>
>   ```mysql
>   ALTER TABLE students DROP COLUMN birthday;
>   ```

### 注意事项

> - UTF8字符集长度为3字节，有些符号占4字节，所以推荐用utf8mb4字符集

## DCL

### 管理用户

> 查询用户：
>
> ```mysql
> USE mysql;
> SELECT * FROM user;
> ```
>
> 创建用户:
> `CREATE USER '用户名'@'主机名' IDENTIFIED BY '密码';`
>
> 修改用户密码：
> `ALTER USER '用户名'@'主机名' IDENTIFIED WITH mysql_native_password BY '新密码';`
>
> 删除用户：
> `DROP USER '用户名'@'主机名';`

```mysql
-- 创建用户test，只能在当前主机localhost访问
create user 'test'@'localhost' identified by '123456';
-- 创建用户test，能在任意主机访问
create user 'test'@'%' identified by '123456';
create user 'test' identified by '123456';
-- 修改密码
alter user 'test'@'localhost' identified with mysql_native_password by '1234';
-- 删除用户
drop user 'test'@'localhost';
```

#### 注意事项

> 主机名可以使用 % 通配

### 权限控制

| 权限                | 说明               |
| ------------------- | ------------------ |
| ALL, ALL PRIVILEGES | 所有权限           |
| SELECT              | 查询数据           |
| INSERT              | 插入数据           |
| UPDATE              | 修改数据           |
| DELETE              | 删除数据           |
| ALTER               | 修改表             |
| DROP                | 删除数据库/表/视图 |
| CREATE              | 创建数据库/表      |

> 更多权限请看[权限一览表](#权限一览表 "权限一览表")
>
> 查询权限：
> `SHOW GRANTS FOR '用户名'@'主机名';`
>
> 授予权限：
> `GRANT 权限列表 ON 数据库名.表名 TO '用户名'@'主机名';`
>
> 撤销权限：
> `REVOKE 权限列表 ON 数据库名.表名 FROM '用户名'@'主机名';`

#### 注意事项

> - 多个权限用逗号分隔
> - 授权时，数据库名和表名可以用 * 进行通配，代表所有

## DML（数据操作语言）

### Insert

```mysql
insert into 表名 (字段1, 字段2, ...) VALUES (值1, 值2, ...);
```

### Delete

```mysql
delete from 表名 where ...;
```

### Update

```mysql
update 表名 set 字段1 = 值1，字段2 = 值2,...where...;
```

### 注意事项

> - 字符串和日期类型数据应该包含在引号中
> - 插入的数据大小应该在字段的规定范围内

## DQL（数据查询语言）

### 基本查询

> - 查询语法
>
>   ```mysql
>   SELECT * FROM <表名>
>   ```
>
> - 设置别名
>
>   ```mysql
>   SELECT 字段1 [ AS 别名1 ], 字段2 [ AS 别名2 ], 字段3 [ AS 别名3 ], ... FROM 表名;
>   
>   SELECT 字段1 [ 别名1 ], 字段2 [ 别名2 ], 字段3 [ 别名3 ], ... FROM 表名;
>   ```
>
> - 去除重复记录
>
>   ```mysql
>   SELECT DISTINCT 字段列表 FROM 表名;
>   ```

### 条件查询

```mysql
SELECT 字段列表 FROM 表名 WHERE 条件列表;
```

| 比较运算符          | 功能                                       |
| ------------------- | ------------------------------------------ |
| >                   | 大于                                       |
| >=                  | 大于等于                                   |
| <                   | 小于                                       |
| <=                  | 小于等于                                   |
| =                   | 等于                                       |
| <> 或 !=            | 不等于                                     |
| BETWEEN ... AND ... | 在某个范围内（含最小、最大值）             |
| IN(...)             | 在in之后的列表中的值，多选一               |
| LIKE 占位符         | 模糊匹配（_匹配单个字符，%匹配任意个字符） |
| IS NULL             | 是NULL                                     |

| 逻辑运算符 | 功能                         |
| ---------- | ---------------------------- |
| AND 或 &&  | 并且（多个条件同时成立）     |
| OR 或 \|\| | 或者（多个条件任意一个成立） |
| NOT 或 !   | 非，不是                     |

> - `NOT`条件**`NOT class_id = 2`其实等价于`class_id <> 2`**，因此，`NOT`查询不是很常用。
> - **如果不加括号，条件运算按照`NOT`、`AND`、`OR`的优先级进行**，即`NOT`优先级最高，其次是`AND`，最后是`OR`。加上括号可以改变优先级。

```mysql
SELECT * from students WHERE score BETWEEN 50 and 90

SELECT * from students WHERE score in (50,60,70,80,90)

SELECT * from students WHERE name like '%明%'

SELECT * from students WHERE name like '_明_'

SELECT * from students WHERE score is NULL;

SELECT * from students WHERE score > 50 and score <80

SELECT * from students WHERE score > 50 or score <80

SELECT * from students WHERE not score = 90
```

### 排序查询

> - 升序(ASC)：`select * from xxx order by score ASC`，**<span style="color:red">默认升序</span>**
> - 降序(DESC)：`select * from xxx order by score DESC`
> - **如果有`WHERE`子句，那么`ORDER BY`子句要放到`WHERE`子句后面。**
> - `SELECT * from students ORDER BY score DESC,id ASC`，先以成绩降序，成绩相同的以id升序

### 分页查询

> - **分页实查询可以<span style="color:orange">通过`LIMIT M OFFSET N `</span>子句实现。**第一个数据**从N开始，每页M个**。**可以<span style="color:orange">简写成 `LIMIT N,M`</span>**

#### 注意事项

> - 起始索引从0开始，起始索引 = （查询页码 - 1） * 每页显示记录数
> - 分页查询是数据库的方言，不同数据库有不同实现，MySQL是LIMIT
> - 如果查询的是第一页数据，起始索引可以省略，直接简写 LIMIT 10

### 聚合查询

#### 聚合函数

| 函数  | 说明                                   |
| :---- | :------------------------------------- |
| count | 统计数量                               |
| SUM   | 计算某一列的合计值，该列必须为数值类型 |
| AVG   | 计算某一列的平均值，该列必须为数值类型 |
| MAX   | 计算某一列的最大值                     |
| MIN   | 计算某一列的最小值                     |

> - **<span style="color:green">使用聚合查询时可以起别名</span>`select count(*) peoplecount from students score>90`**
>
> - **<span style="color:green">如果聚合查询的`WHERE`条件没有匹配到任何行，`COUNT()`会返回0，而`SUM()`、`AVG()`、`MAX()`和`MIN()`会返回`NULL`</span>**

#### 分组查询

> 如果我们要统计一班的学生数量，我们知道，可以用`SELECT COUNT(*) num FROM students WHERE class_id = 1;`。如果要继续统计二班、三班的学生数量，难道必须不断修改`WHERE`条件来执行`SELECT`语句吗？
>
> ```mysql
> select class_id,count(*) peoplecount from students GROUP BY class_id
> ```
>
> ![image-20220321202706833](MySql和JDBC/image-20220321202706833.png)
>
> 执行这个查询，`COUNT()`的结果不再是一个，而是3个，这是因为，**<span style="color:blue">`GROUP BY`子句指定了按`class_id`分组</span>**，因此，执行该`SELECT`语句时，会把`class_id`相同的列先分组，再分别计算，因此，得到了3行结果。

> - `SELECT name, class_id, COUNT(*) num FROM students GROUP BY class_id;`不出意外，执行这条查询我们会得到一个语法错误，因为在任意一个分组中，只有`class_id`都相同，`name`是不同的，SQL引擎不能把多个`name`的值放入一行记录中。因此，聚合查询的列中，只能放入分组的列。
> - `SELECT class_id, gender, COUNT(*) num FROM students GROUP BY class_id, gender;`统计各班的男生和女生人数

#### 注意事项

> - **执行顺序：where > 聚合函数 > having**
> - 分组之后，查询的字段一般为聚合函数和分组字段，查询其他字段无任何意义

#### where和having的区别

> - 执行时机不同：where是分组之前进行过滤，不满足where条件不参与分组；having是分组后对结果进行过滤。
> - 判断条件不同：<span style="color:orange">**where不能对聚合函数进行判断，而having可以。**</span>

### DQL执行顺序

> <span style="color:purple">**FROM -> WHERE -> GROUP BY -> SELECT -> ORDER BY -> LIMIT**</span>

## 函数

### 字符串函数

| 函数                             | 功能                                                      |
| -------------------------------- | --------------------------------------------------------- |
| CONCAT(s1, s2, ..., sn)          | 字符串拼接，将s1, s2, ..., sn拼接成一个字符串             |
| LOWER(str)                       | 将字符串全部转为小写                                      |
| UPPER(str)                       | 将字符串全部转为大写                                      |
| LPAD(str, n, pad)                | 左填充，用字符串pad对str的左边进行填充，达到n个字符串长度 |
| RPAD(str, n, pad)                | 右填充，用字符串pad对str的右边进行填充，达到n个字符串长度 |
| TRIM(str)                        | 去掉字符串头部和尾部的空格                                |
| SUBSTRING(str, start, len)       | 返回从字符串str从start位置起的len个长度的字符串           |
| REPLACE(column, source, replace) | 替换字符串                                                |

```mysql
-- 拼接
SELECT CONCAT('Hello', 'World');
-- 小写
SELECT LOWER('Hello');
-- 大写
SELECT UPPER('Hello');
-- 左填充
SELECT LPAD('01', 5, '-');
-- 右填充
SELECT RPAD('01', 5, '-');
-- 去除空格
SELECT TRIM(' Hello World ');
-- 切片（起始索引为1）
SELECT SUBSTRING('Hello World', 1, 5);
```

### 数值函数

| 函数        | 功能                             |
| ----------- | -------------------------------- |
| CEIL(x)     | 向上取整                         |
| FLOOR(x)    | 向下取整                         |
| MOD(x, y)   | 返回x/y的模                      |
| RAND()      | 返回0~1内的随机数                |
| ROUND(x, y) | 求参数x的四舍五入值，保留y位小数 |

### 日期函数

| 函数                               | 功能                                              |
| ---------------------------------- | ------------------------------------------------- |
| CURDATE()                          | 返回当前日期                                      |
| CURTIME()                          | 返回当前时间                                      |
| NOW()                              | 返回当前日期和时间                                |
| YEAR(date)                         | 获取指定date的年份                                |
| MONTH(date)                        | 获取指定date的月份                                |
| DAY(date)                          | 获取指定date的日期                                |
| DATE_ADD(date, INTERVAL expr type) | 返回一个日期/时间值加上一个时间间隔expr后的时间值 |
| DATEDIFF(date1, date2)             | 返回起始时间date1和结束时间date2之间的天数        |

```mysql
-- DATE_ADD
SELECT DATE_ADD(NOW(), INTERVAL 70 YEAR);
```

### 流程函数

| 函数                                                         | 功能                                                      |
| ------------------------------------------------------------ | --------------------------------------------------------- |
| IF(value, t, f)                                              | 如果value为true，则返回t，否则返回f                       |
| IFNULL(value1, value2)                                       | 如果value1不为空，返回value1，否则返回value2              |
| CASE WHEN [ val1 ] THEN [ res1 ] ... ELSE [ default ] END    | 如果val1为true，返回res1，... 否则返回default默认值       |
| CASE [ expr ] WHEN [ val1 ] THEN [ res1 ] ... ELSE [ default ] END | 如果expr的值等于val1，返回res1，... 否则返回default默认值 |

```mysql
select
	name,
	(case when age > 30 then '中年' else '青年' end)
from employee;
select
	name,
	(case workaddress when '北京市' then '一线城市' when '上海市' then '一线城市' else '二线城市' end) as '工作地址'
from employee;
```

## 约束constraint

> 约束是作用于表中字段上的，可以再创建表/修改表的时候添加约束。

### 约束类型

| 约束                    | 描述                                                     | 关键字      |
| ----------------------- | -------------------------------------------------------- | ----------- |
| 非空约束                | 限制该字段的数据不能为null                               | NOT NULL    |
| 唯一约束                | 保证该字段的所有数据都是唯一、不重复的                   | UNIQUE      |
| 主键约束                | 主键是一行数据的唯一标识，要求非空且唯一                 | PRIMARY KEY |
| 默认约束                | 保存数据时，如果未指定该字段的值，则采用默认值           | DEFAULT     |
| 检查约束（8.0.1版本后） | 保证字段值满足某一个条件                                 | CHECK       |
| 外键约束                | 用来让两张图的数据之间建立连接，保证数据的一致性和完整性 | FOREIGN KEY |

```sql
create table user(
	id int primary key auto_increment,
	name varchar(10) not null unique,
	age int check(age > 0 and age < 120),
	status char(1) default '1',
	gender char(1)
);
```

### 主键约束

> - 对于关系表，有个很重要的约束，就是任意两条记录不能重复。<span style="color:orange">不能重复不是指两条记录不完全相同，而是指能够通过某个字段唯一区分出不同的记录，这个字段被称为**主键**。</span>
> - 对主键的要求，最关键的一点是：**记录一旦插入到表中，主键最好不要再修改，**因为主键是用来唯一定位记录的，修改了主键，会造成一系列的影响。
> - 选取主键的一个基本原则是：<span style="color:orange">**不使用任何业务相关的字段作为主键。**</span>而应该使用BIGINT自增或者GUID类型。<span style="color:orange">**主键也不应该允许`NULL`。**</span>

#### 联合主键

> 关系数据库实际上还允许通过多个字段唯一标识记录，即两个或更多的字段都设置为主键，这种主键被称为联合主键。
>
> <span style="color:green">**对于联合主键，允许一列有重复，只要不是所有主键列都重复即可**</span>
>
> ![image-20220321164844007](MySql和JDBC/image-20220321164844007.png)
>
> 如果我们把上述表的`id_num`和`id_type`这两列作为联合主键，那么上面的3条记录都是允许的，因为没有两列主键组合起来是相同的。
>
> 没有必要的情况下，我们尽量不使用联合主键，因为它给关系表带来了复杂度的上升。

### 外键约束

> students表

![image-20220321184155880](MySql和JDBC/image-20220321184155880.png)

> class表

![image-20220321184225207](MySql和JDBC/image-20220321184225207.png)

> 设置外键（使用图形化工具），在需要设置外键的表右键选设计表

![image-20220321184351430](MySql和JDBC/image-20220321184351430.png)

> 设置外键（设置sql语句）
>
> ```mysql
> ALTER TABLE school
> ADD CONSTRAINT fk_class_id
> FOREIGN KEY (class_id)
> REFERENCES class (id);
> ```
>
> 外键约束的名称`fk_class_id`可以任意，`FOREIGN KEY (class_id)`指定了`class_id`作为外键，`REFERENCES class (id)`指定了这个外键将关联到`class`表的`id`列（即`class`表的主键）。
>
> 通过定义外键约束，关系数据库可以保证无法插入无效的数据。即如果`class`表不存在`id=99`的记录，`students`表就无法插入`class_id=99`的记录。
>
> 要删除一个外键约束，也是通过`ALTER TABLE`实现的：
>
> ```sql
> ALTER TABLE students
> DROP FOREIGN KEY fk_class_id;
> ```

#### 删除/更新行为

| 行为        | 说明                                                         |
| ----------- | ------------------------------------------------------------ |
| NO ACTION   | 当在父表中删除/更新对应记录时，首先检查该记录是否有对应外键，如果有则不允许删除/更新（与RESTRICT一致） |
| RESTRICT    | 当在父表中删除/更新对应记录时，首先检查该记录是否有对应外键，如果有则不允许删除/更新（与NO ACTION一致） |
| CASCADE     | 当在父表中删除/更新对应记录时，首先检查该记录是否有对应外键，如果有则也删除/更新外键在子表中的记录 |
| SET NULL    | 当在父表中删除/更新对应记录时，首先检查该记录是否有对应外键，如果有则设置子表中该外键值为null（要求该外键允许为null） |
| SET DEFAULT | 父表有变更时，子表将外键设为一个默认值（Innodb不支持）       |

> 命令

```sql
ALTER TABLE 表名 ADD CONSTRAINT 外键名称 FOREIGN KEY (外键字段) REFERENCES 主表名(主表字段名) ON UPDATE 行为 ON DELETE 行为;
```

> 图形化界面

![image-20221029171858453](MySql%E5%92%8CJDBC/image-20221029171858453.png)

## 多表查询

### 关系模型

#### 一对多

> 实现：<span style="color:orange">**在多的一方建立外键，指向一的一方的主键**</span>

#### 多对多

> 通过一个表的外键关联到另一个表，我们可以定义出一对多关系。有些时候，还需要定义“多对多”关系。例如，一个老师可以对应多个班级，一个班级也可以对应多个老师，因此，班级表和老师表存在多对多关系。
>
> <span style="color:orange">**多对多关系实际上是通过两个一对多关系实现的，即通过一个中间表，**</span>关联两个一对多关系，就形成了多对多关系
>
> 实现：**<span style="color:orange">建立第三张中间表，中间表至少包含两个外键，分别关联两方主键</span>**
>
> ![image-20220321185146617](MySql和JDBC/image-20220321185146617.png)
>
> ![image-20220321185417131](MySql和JDBC/image-20220321185417131.png)
>
> ![image-20220321185205665](MySql和JDBC/image-20220321185205665.png)
>
> 通过中间表`teacher_class`可知`teachers`到`classes`的关系：
>
> - `id=1`的张老师对应`id=1,2`的一班和二班；
> - `id=2`的王老师对应`id=1,2`的一班和二班；
> - `id=3`的李老师对应`id=1`的一班；
> - `id=4`的赵老师对应`id=2`的二班。
>
> 同理可知`classes`到`teachers`的关系：
>
> - `id=1`的一班对应`id=1,2,3`的张老师、王老师和李老师；
> - `id=2`的二班对应`id=1,2,4`的张老师、王老师和赵老师；
>
> 因此，通过中间表，我们就定义了一个“多对多”关系。

#### 一对一

> **一对一关系是指，一个表的记录对应到另一个表的唯一一个记录。**
>
> 例如，`students`表的每个学生可以有自己的联系方式，如果把联系方式存入另一个表`contacts`，我们就可以得到一个“一对一”关系：
>
> ![image-20220321185237547](MySql和JDBC/image-20220321185237547.png)
>
> 有细心的童鞋会问，既然是一对一关系，那为啥不给`students`表增加一个`mobile`列，这样就能合二为一了？
>
> 如果业务允许，完全可以把两个表合为一个表。但是，有些时候，如果某个学生没有手机号，那么，`contacts`表就不存在对应的记录。实际上，一对一关系准确地说，是`contacts`表一对一对应`students`表。
>
> 还有一些应用会把一个大表拆成两个一对一的表，目的是把经常读取和不经常读取的字段分开，以获得更高的性能。例如，把一个大的用户表分拆为用户基本信息表`user_info`和用户详细信息表`user_profiles`，大部分时候，只需要查询`user_info`表，并不需要查询`user_profiles`表，这样就提高了查询速度。

#### 小结

> **关系数据库通过外键可以实现一对多、多对多和一对一的关系。**外键既可以通过数据库来约束，**也可以不设置约束，仅依靠应用程序的逻辑来保证。**

### 连接查询

> 连接查询是另一种类型的多表查询。连接查询对多个表进行JOIN运算，简单地说，就是先确定一个主表作为结果集，然后，把其他表的行有选择性地“连接”在主表结果集上。

#### 内连接——INNER JOIN

![](MySql和JDBC/inter.png)

> INNER JOIN查询的写法是：
>
> 1. 先确定主表，仍然使用`FROM <表1>`的语法；
>
> 2. 再确定需要连接的表，使用`INNER JOIN <表2>`的语法；
>
> 3. 然后<span style="color:orange">**确定连接条件，使用`ON <条件...>`，**</span>这里的条件是`s.class_id = c.id`，表示`students`表的`class_id`列与`classes`表的`id`列相同的行需要连接；
>
> 4. 可选：加上`WHERE`子句、`ORDER BY`等子句。
>
> 5. <span style="color:orange">**隐式内连接：`SELECT 字段列表 FROM 表1, 表2 WHERE 条件 ...;`**</span>
>
>    <span style="color:orange">**显式内连接：`SELECT 字段列表 FROM 表1 [ INNER ] JOIN 表2 ON 连接条件 ...;`**</span>

```mysql
select s.name,s.gender,c.name class_name from students s INNER JOIN classes c on s.class_id = c.id
```

#### 外连接——OUTER JOIN

> 外连接分为三种：
>
> 1. RIGHT OUTER JOIN——右外连接
> 2. LEFT OUTER JOIN——左外连接
> 3. FULL OUTER JOIN——全外连接

![image-20220321211739782](MySql和JDBC/image-20220321211739782.png)

![image-20220321211757912](MySql和JDBC/image-20220321211757912.png)

##### RIGHT OUTER JOIN——右外连接

![](MySql和JDBC/right.png)

> 依赖副表
>
> RIGHT OUTER JOIN返回右表都存在的行。**如果某一行仅在右表存在，那么结果集就会以`NULL`填充剩下的字段。**

```mysql
select s.id,s.name,s.score,c.name class_name from students s RIGHT OUTER JOIN classes c on s.class_id = c.id
```

![image-20220321211913904](MySql和JDBC/image-20220321211913904.png)

##### LEFT OUTER JOIN——左外连接

![](MySql和JDBC/left.png)

> 依赖主表。
>
> LEFT OUTER JOIN返回左表都存在的行。**如果某一行仅在左表存在，那么结果集就会以`NULL`填充剩下的字段。**

```mysql
select s.id,s.name,s.score,c.name class_name from students s LEFT OUTER JOIN classes c on s.class_id = c.id
```

![image-20220321212014398](MySql和JDBC/image-20220321212014398.png)

##### FULL OUTER JOIN——全外连接

![](MySql和JDBC/full.png)

> 把两张表的所有记录全部选择出来，并且，自动把对方不存在的列填充为NULL
>
> <span style="color:orange">**MySQL 不支持全连接，但可以通过左外连接 + UNION + 右外连接实现**</span>
>
> ```mysql
> SELECT s.id, s.name, s.class_id, c.name class_name, s.gender, s.score
> FROM students s
> RIGHT JOIN classes c ON class_id = c.id
> UNION
> SELECT s.id, s.name, s.class_id, c.name class_name, s.gender, s.score
> FROM classes c
> LEFT JOIN students s ON class_id = c.id;
> ```

![image-20220321213853359](MySql和JDBC/image-20220321213853359.png)

### 联合查询 union, union all

> - 把多次查询的结果合并，形成一个新的查询集
>
> - 语法：
>
>   ```mysql
>   SELECT 字段列表 FROM 表A ...
>   UNION [ALL]
>   SELECT 字段列表 FROM 表B ...
>   ```
>
> - **UNION ALL 会有重复结果，UNION 不会**
> - **联合查询比使用or效率高，不会使索引失效**

### 笛卡尔查询

> - SELECT查询不但可以从一张表查询数据，还可以从多张表同时查询数据。<span style="color:purple">**查询多张表的语法是：`SELECT * FROM <表1>,<表2>`。**</span>
> - 这种多表查询，又称**笛卡尔查询**，使用笛卡尔查询时要非常小心，由于结果集是目标表的行数乘积，对两个各自有100行记录的表进行笛卡尔查询将返回1万条记录，对两个各自有1万行记录的表进行笛卡尔查询将返回1亿条记录。**（<span style="color:purple">在多表查询时，需要消除无效的笛卡尔积</span>）**
> - 可以利用投影查询的”设置列的别名“来给两个表各自的`id`和`name`列起别名。<span style="color:purple">**多表查询时，要使用`表名.列名`这样的方式来引用列和设置别名**</span>，这样就避免了结果集的列名重复问题。但是，用`表名.列名`这种方式列举两个表的所有列实在是很麻烦，所以<span style="color:purple">**SQL还允许给表设置一个别名**</span>

```mysql
SELECT s.id s_id,s.name s_name,c.id c_id,c.name c_name FROM students s,classes c where s.class_id = c.id
```

### 自连接查询

> - 当前表与自身的连接查询，自连接必须使用表别名
> - 语法：<span style="color:orange">**`SELECT 字段列表 FROM 表A 别名A JOIN 表A 别名B on 条件 ...;`**</span>
> - **自连接查询，可以是内连接查询，也可以是外连接查询**

```mysql
-- 查询员工及其所属领导的名字
select a.name, b.name from employee a join employee b where a.manager = b.id;
-- 没有领导的也查询出来
select a.name, b.name from employee a left join employee b on a.manager = b.id;
```

```mysql
SELECT a.`o_name`AS '科目' ,b.`o_name`'类别' FROM `obj`AS a join`obj`AS b where a.`o_id` = b.`id`
```

![image-20221004195703178](MySql和JDBC/image-20221004195703178.png)

### 子查询

> - SQL语句中嵌套SELECT语句，称谓嵌套查询，又称子查询。
> - 语法：**<span style="color:orange">`SELECT * FROM t1 WHERE column1 = ( SELECT column1 FROM t2);`</span>**
> - **子查询外部的语句可以是 INSERT / UPDATE / DELETE / SELECT 的任何一个**

#### 标量子查询

>- 子查询返回的结果是单个值（数字、字符串、日期等）
>- 常用操作符：- < > > >= < <=

```mysql
-- 查询销售部所有员工
select id from dept where name = '销售部';
-- 根据销售部部门ID，查询员工信息
select * from employee where dept = 4;
-- 合并（子查询）
select * from employee where dept = (select id from dept where name = '销售部');

-- 查询xxx入职之后的员工信息
select * from employee where entrydate > (select entrydate from employee where name = 'xxx');
```

#### 列子查询

> - 返回的结果是一列（可以是多行）
>
> - 常用操作符：
>
>   | 操作符 | 描述                                   |
>   | ------ | -------------------------------------- |
>   | IN     | 在指定的集合范围内，多选一             |
>   | NOT IN | 不在指定的集合范围内                   |
>   | ANY    | 子查询返回列表中，有任意一个满足即可   |
>   | SOME   | 与ANY等同，使用SOME的地方都可以使用ANY |
>   | ALL    | 子查询返回列表的所有值都必须满足       |

```mysql
-- 查询销售部和市场部的所有员工信息
select * from employee where dept in (select id from dept where name = '销售部' or name = '市场部');
-- 查询比财务部所有人工资都高的员工信息
select * from employee where salary > all(select salary from employee where dept = (select id from dept where name = '财务部'));
-- 查询比研发部任意一人工资高的员工信息
select * from employee where salary > any (select salary from employee where dept = (select id from dept where name = '研发部'));
```

#### 行子查询

> - 返回的结果是一行（可以是多列）
> - 常用操作符：=, <, >, IN, NOT IN

```mysql
-- 查询与xxx的薪资及直属领导相同的员工信息
select * from employee where (salary, manager) = (12500, 1);
select * from employee where (salary, manager) = (select salary, manager from employee where name = 'xxx');
```

#### 表子查询

> - 返回的结果是多行多列
> - 常用操作符：IN

```mysql
-- 查询与xxx1，xxx2的职位和薪资相同的员工
select * from employee where (job, salary) in (select job, salary from employee where name = 'xxx1' or name = 'xxx2');
-- 查询入职日期是2006-01-01之后的员工，及其部门信息
select e.*, d.* from (select * from employee where entrydate > '2006-01-01') as e left join dept as d on e.dept = d.id;
```

### [ON 与 WHERE 的区别](https://www.jianshu.com/p/d923cf8ae25f)

> **先执行 `ON`，后执行 `WHERE`；`ON` 是建立关联关系，不符合`ON`条件的为`null`；`WHERE` 是对关联关系的筛选**。

![image-20221030103115011](MySql%E5%92%8CJDBC/image-20221030103115011.png)

## 实用sql语句

### 插入或替换

> 如果我们希望插入一条新记录（INSERT），但如果记录已经存在，就先删除原记录，再插入新记录。此时，可以使用`REPLACE`语句，这样就不必先查询，再决定是否先删除再插入：
>
> ```mysql
> REPLACE INTO students (id, class_id, name, gender, score) VALUES (1, 1, '小明', 'F', 99);
> ```
>
> 若`id=1`的记录不存在，`REPLACE`语句将插入新记录，否则，当前`id=1`的记录将被删除，然后再插入新记录。

### 插入或更新

> 如果我们希望插入一条新记录（INSERT），但如果记录已经存在，就更新该记录，此时，可以使用`INSERT INTO ... ON DUPLICATE KEY UPDATE ...`语句：
>
> ```mysql
> INSERT INTO students (id, class_id, name, gender, score) VALUES (1, 1, '小明', 'F', 99) ON DUPLICATE KEY UPDATE name='小明', gender='F', score=99;
> ```
>
> 若`id=1`的记录不存在，`INSERT`语句将插入新记录，否则，当前`id=1`的记录将被更新，更新的字段由`UPDATE`指定。

### 插入或忽略

> 如果我们希望插入一条新记录（INSERT），但如果记录已经存在，就啥事也不干直接忽略，此时，可以使用`INSERT IGNORE INTO ...`语句：
>
> ```mysql
> INSERT IGNORE INTO students (id, class_id, name, gender, score) VALUES (1, 1, '小明', 'F', 99);
> ```
>
> 若`id=1`的记录不存在，`INSERT`语句将插入新记录，否则，不执行任何操作。

### 快照

> 如果想要对一个表进行快照，即复制一份当前表的数据到一个新表，可以结合`CREATE TABLE`和`SELECT`：
>
> ```mysql
> -- 对class_id=1的记录进行快照，并存储为新表students_of_class1:
> CREATE TABLE students_of_class1 SELECT * FROM students WHERE class_id=1;
> ```
>
> 新创建的表结构和`SELECT`使用的表结构完全一致。

### 写入查询结果集

> 如果查询结果集需要写入到表中，可以结合`INSERT`和`SELECT`，将`SELECT`语句的结果集直接插入到指定表中。
>
> 例如，创建一个统计成绩的表`statistics`，记录各班的平均成绩：
>
> ```mysql
> CREATE TABLE statistics (
>     id BIGINT NOT NULL AUTO_INCREMENT,
>     class_id BIGINT NOT NULL,
>     average DOUBLE NOT NULL,
>     PRIMARY KEY (id)
> );
> ```
>
> 然后，我们就可以用一条语句写入各班的平均成绩：
>
> ```mysql
> INSERT INTO statistics (class_id, average) SELECT class_id, AVG(score) FROM students GROUP BY class_id;
> ```
>
> 确保`INSERT`语句的列和`SELECT`语句的列能一一对应，就可以在`statistics`表中直接保存查询的结果：
>
> ```
> > SELECT * FROM statistics;
> +----+----------+--------------+
> | id | class_id | average      |
> +----+----------+--------------+
> |  1 |        1 |         86.5 |
> |  2 |        2 | 73.666666666 |
> |  3 |        3 | 88.333333333 |
> +----+----------+--------------+
> 3 rows in set (0.00 sec)
> ```

### 强制使用指定索引

> 在查询的时候，数据库系统会自动分析查询语句，并选择一个最合适的索引。但是很多时候，数据库系统的查询优化器并不一定总是能使用最优索引。如果我们知道如何选择索引，可以**<span style="color:orange">使用`FORCE INDEX`强制查询使用指定的索引。</span>**例如：
>
> ```mysql
> SELECT * FROM students FORCE INDEX (idx_class_id) WHERE class_id = 1 ORDER BY id DESC;
> ```
>
> **指定索引的前提是索引`idx_class_id`必须存在。**

## 事务

> **数据库系统保证在一个事务中的所有SQL要么全部执行成功，要么全部不执行**
>
> - **begin/start transaction：开启事务**
> - **commit：提交事务**
> - **rollback：回滚事务，整个事务会失败**
>
> 注：在mysql中输入命令时后面要加上**`;`**  

```sql
-- 查看事务提交方式
SELECT @@AUTOCOMMIT;
-- 设置事务提交方式，1为自动提交，0为手动提交，该设置只对当前会话有效
SET @@AUTOCOMMIT = 0;


-- 开启事务，开启事务默认关闭自动提交
begin;

select * from account where name = '张三';
update account set money = money - 1000 where name = '张三';
update account set money = money + 1000 where name = '李四';

-- 提交事务
commit;

-- 回滚事务
rollback;
```

### 四大特性ACID

> - **原子性**(Atomicity)：事务是不可分割的最小操作单元，要么全部成功，要么全部失败
> - **一致性**(Consistency)：事务完成时，必须使所有数据都保持一致状态
> - **隔离性**(Isolation)：数据库系统提供的隔离机制，保证事务在不受外部并发操作影响的独立环境下运行
> - **持久性**(Durability)：事务一旦提交或回滚，它对数据库中的数据的改变就是永久的

### 隔离级别

> 对于两个并发执行的事务，如果涉及到操作同一条记录的时候，可能会发生问题。因为并发操作会带来数据的不一致性，包括**脏读、不可重复读、幻读**等。数据库系统提供了隔离级别来让我们有针对性地选择事务的隔离级别，避免数据不一致的问题。
>
> | 问题       | 描述                                                         |
> | ---------- | ------------------------------------------------------------ |
> | 脏读       | 一个事务读到另一个事务还没提交的数据                         |
> | 不可重复读 | 一个事务先后读取同一条记录，但两次读取的数据不同             |
> | 幻读       | 在一个事务中，第一次查询某条记录，发现没有，但是，当试图更新这条不存在的记录时，竟然能成功，并且，再次读取同一条记录，它就神奇地出现了。 |
>
> SQL标准定义了**4种隔离级别**，分别对应可能出现的问题：
>
> | Isolation Level                   | 脏读（Dirty Read） | 不可重复读（Non Repeatable Read） | 幻读（Phantom Read） |
> | :-------------------------------- | :----------------- | :-------------------------------- | :------------------- |
> | Read Uncommitte(读未提交)         | Yes                | Yes                               | Yes                  |
> | Read Committed（读已提交）        | -                  | Yes                               | Yes                  |
> | Repeatable Read（可重复读。默认） | -                  | -                                 | Yes                  |
> | Serializable（串行化）            | -                  | -                                 | -                    |

> 查看当前隔离级别：
>
> ```sql
> select @@transaction_isolation
> ```
>
> 设置隔离级别
>
> ```sql
> set session transaction isolation level read uncommitted
> ```

#### Read Uncommitted

> **Read Uncommitted是隔离级别最低的一种事务级别**。在这种隔离级别下，一个事务会读到另一个事务更新后但未提交的数据，如果另一个事务回滚，那么当前事务读到的数据就是脏数据，这就是脏读（Dirty Read）。
>
> 我们来看一个例子。
>
> 首先，我们准备好`students`表的数据，该表仅一行记录：
>
> ```mysql
> mysql> select * from students;
> +----+-------+
> | id | name  |
> +----+-------+
> |  1 | Alice |
> +----+-------+
> 1 row in set (0.00 sec)
> ```
>
> 然后，分别开启两个MySQL客户端连接，按顺序依次执行事务A和事务B：
>
> ![image-20220201174440467](MySql和JDBC/image-20220201174440467.png)
>
> 当事务A执行完第3步时，它更新了`id=1`的记录，但并未提交，而事务B在第4步读取到的数据就是未提交的数据。
>
> 随后，事务A在第5步进行了回滚，事务B再次读取`id=1`的记录，发现和上一次读取到的数据不一致，这就是脏读。
>
> 可见，在Read Uncommitted隔离级别下，一个事务可能读取到另一个事务更新但未提交的数据，这个数据有可能是脏数据。

![image-20220201180832178](MySql和JDBC/image-20220201180832178.png)	

#### Read Committed

> 在Read Committed隔离级别下，一个事务可能会遇到不可重复读（Non Repeatable Read）的问题。
>
> 不可重复读是指，在一个事务内，多次读同一数据，在这个事务还没有结束时，如果另一个事务恰好修改了这个数据，那么，在第一个事务中，两次读取的数据就可能不一致。
>
> 我们仍然先准备好`students`表的数据：
>
> ```mysql
> mysql> select * from students;
> +----+-------+
> | id | name  |
> +----+-------+
> |  1 | Alice |
> +----+-------+
> 1 row in set (0.00 sec)
> ```
>
> 然后，分别开启两个MySQL客户端连接，按顺序依次执行事务A和事务B：
>
> ![image-20220201175300380](MySql和JDBC/image-20220201175300380.png)
>
> 当事务B第一次执行第3步的查询时，得到的结果是`Alice`，随后，由于事务A在第4步更新了这条记录并提交，所以，事务B在第6步再次执行同样的查询时，得到的结果就变成了`Bob`，因此，在Read Committed隔离级别下，事务不可重复读同一条记录，因为很可能读到的结果不一致。

![image-20220201181010311](MySql和JDBC/image-20220201181010311.png)

#### Repeatable Read（默认的隔离级别）

> 在Repeatable Read隔离级别下，一个事务可能会遇到幻读（Phantom Read）的问题。
>
> 幻读是指，在一个事务中，第一次查询某条记录，发现没有，但是，当试图更新这条不存在的记录时，竟然能成功，并且，再次读取同一条记录，它就神奇地出现了。
>
> 我们仍然先准备好`students`表的数据：
>
> ```mysql
> mysql> select * from students;
> +----+-------+
> | id | name  |
> +----+-------+
> |  1 | Alice |
> +----+-------+
> 1 row in set (0.00 sec)
> ```
>
> 然后，分别开启两个MySQL客户端连接，按顺序依次执行事务A和事务B：
>
> ![image-20220201180333223](MySql和JDBC/image-20220201180333223.png)

<img src="MySql和JDBC/image-20220201180611696.png" alt="image-20220201180611696" />

#### Serializable

> **Serializable是最严格的隔离级别**。在Serializable隔离级别下，所有事务按照次序依次执行，因此，脏读、不可重复读、幻读都不会出现。
>
> 虽然Serializable隔离级别下的事务具有最高的安全性，但是，由于事务是串行执行，所以**效率会大大下降**，应用程序的性能会急剧降低。如果没有特别重要的情景，一般都不会使用Serializable隔离级别。
>
> 如果没有指定隔离级别，数据库就会使用默认的隔离级别。在MySQL中，如果使用InnoDB，默认的隔离级别是Repeatable Read。

## Java类型和SQL类型对应

| Java类型           | SQL类型                  |
| ------------------ | ------------------------ |
| boolean            | BIT                      |
| byte               | TINYINY                  |
| short              | SMALLINT                 |
| int                | INTEGER                  |
| long               | BIGINT                   |
| String             | CHAR,VARCHAR,LONGVARCHAR |
| byte    array      | BINARY,VAR BINARY        |
| java.sql.Date      | DATE                     |
| java.sql.Time      | TIME                     |
| java.sql.Timestamp | TIMESTAMP                |

# MySQL进阶篇

## 存储引擎

> MySql体系结构

![image-20221030140506271](MySql%E5%92%8CJDBC/image-20221030140506271.png)

> - 存储引擎就是存储数据、建立索引、更新/查询数据等技术的实现方式。存储引擎是基于表而不是基于库的，所以存储引擎也可以被称为表引擎。
> - MySql默认存储引擎是InnoDB。

```sql
-- 查询建表语句
show create table account;
-- 建表时指定存储引擎
CREATE TABLE 表名(
	...
) ENGINE=INNODB;
-- 查看当前数据库支持的存储引擎
show engines;
```

### InnoDB

> InnoDB 是一种兼顾高可靠性和高性能的通用存储引擎，在 MySQL 5.5 之后，InnoDB 是默认的 MySQL 引擎。
>
> - 特点：
>   - DML 操作遵循 ACID 模型，支持**事务**
>   - **行级锁**，提高并发访问性能
>   - 支持**外键**约束，保证数据的完整性和正确性
>
> - 文件：
>   - xxx.ibd: xxx代表表名，InnoDB 引擎的每张表都会对应这样一个表空间文件，存储该表的表结构（frm、sdi）、数据和索引。
>
> - 参数：innodb_file_per_table，决定多张表共享一个表空间还是每张表对应一个表空间。
> - 查看是多张表对应一个共享空间还是每张表对应一个表空间：
>   `show variables like 'innodb_file_per_table';`。**默认是ON，即每张表对应一个表空间**
>
> - 从idb文件提取表结构数据：
>   （在cmd运行）
>   `ibd2sdi xxx.ibd`

> InnoDB逻辑存储结构

![image-20221030141617876](MySql%E5%92%8CJDBC/image-20221030141617876.png)

### MyISAM

> MyISAM 是 MySQL 早期的默认存储引擎。
>
> 特点：
>
> - 不支持事务，不支持外键
> - 支持表锁，不支持行锁
> - 访问速度快
>
> 文件：
>
> - xxx.sdi: 存储表结构信息
> - xxx.MYD: 存储数据
> - xxx.MYI: 存储索引
>

### Memory

> Memory 引擎的表数据是存储在内存中的，受硬件问题、断电问题的影响，只能将这些表作为临时表或缓存使用。
>
> 特点：
>
> - 存放在内存中，速度快
> - hash索引（默认）
>
> 文件：
>
> - xxx.sdi: 存储表结构信息
>

### 存储引擎特点

| 特点         | InnoDB              | MyISAM | Memory |
| ------------ | ------------------- | ------ | ------ |
| 存储限制     | 64TB                | 有     | 有     |
| 事务安全     | 支持                | -      | -      |
| 锁机制       | 行锁                | 表锁   | 表锁   |
| B+tree索引   | 支持                | 支持   | 支持   |
| Hash索引     | -                   | -      | 支持   |
| 全文索引     | 支持（5.6版本之后） | 支持   | -      |
| 空间使用     | 高                  | 低     | N/A    |
| 内存使用     | 高                  | 低     | 中等   |
| 批量插入速度 | 低                  | 高     | 高     |
| 支持外键     | 支持                | -      | -      |

### 存储引擎的选择

> 在选择存储引擎时，应该根据应用系统的特点选择合适的存储引擎。对于复杂的应用系统，还可以根据实际情况选择多种存储引擎进行组合。
>
> - InnoDB: 如果应用**对事物的完整性有比较高的要求，在并发条件下要求数据的一致性，数据操作除了插入和查询之外，还包含很多的更新、删除操作**，则 InnoDB 是比较合适的选择
> - MyISAM: 如果应用是**以读操作和插入操作为主，只有很少的更新和删除操作，并且对事务的完整性、并发性要求不高**，那这个存储引擎是非常合适的。---> 被MongoDB替代
> - Memory: **将所有数据保存在内存中，访问速度快，通常用于临时表及缓存。Memory 的缺陷是对表的大小有限制，太大的表无法缓存在内存中，而且无法保障数据的安全性。** ---> 被Redis替代
>
> **电商中的足迹和评论适合使用 MyISAM 引擎，缓存适合使用 Memory 引擎。**

## 索引

> 索引是帮助 MySQL **高效获取数据**的**数据结构（有序）**。在数据之外，数据库系统还维护着满足特定查找算法的数据结构，这些数据结构以某种方式引用（指向）数据，这样就可以在这些数据结构上实现高级查询算法，这种数据结构就是索引。
>
> 优缺点：
>
> 优点：
>
> - 提高数据检索效率，降低数据库的IO成本
> - 通过索引列对数据进行排序，降低数据排序的成本，降低CPU的消耗
>
> 缺点：
>
> - 索引列也是要占用空间的
> - 索引大大提高了查询效率，但降低了更新的速度，比如 INSERT、UPDATE、DELETE

### 索引结构

> **MySQL的索引是在存储引擎层实现的，不同的存储引擎有不同的结构**

| 索引结构            | 描述                                                         |
| ------------------- | ------------------------------------------------------------ |
| B+Tree              | 最常见的索引类型，大部分引擎都支持B+树索引                   |
| Hash                | 底层数据结构是用哈希表实现，只有精确匹配索引列的查询才有效，不支持范围查询 |
| R-Tree(空间索引)    | 空间索引是 MyISAM 引擎的一个特殊索引类型，主要用于地理空间数据类型，通常使用较少 |
| Full-Text(全文索引) | 是一种通过建立倒排索引，快速匹配文档的方式，类似于 Lucene, Solr, ES |

| 索引       | InnoDB        | MyISAM | Memory |
| ---------- | ------------- | ------ | ------ |
| B+Tree索引 | 支持          | 支持   | 支持   |
| Hash索引   | 不支持        | 不支持 | 支持   |
| R-Tree索引 | 不支持        | 支持   | 不支持 |
| Full-text  | 5.6版本后支持 | 支持   | 不支持 |

#### B-Tree

**二叉树**

<img src="MySql%E5%92%8CJDBC/image-20221030155259065.png" alt="image-20221030155259065" style="zoom:60%;" />

> 二叉树缺点:顺序插入时，会形成一个链表，查询性能大大降低。大数据量情况下，层级较深，检索速度慢。

**红黑树**

> 二叉树的缺点可以用红黑树来解决

<img src="MySql%E5%92%8CJDBC/image-20221030155510915.png" alt="image-20221030155510915" style="zoom:50%;" />

> 红黑树也存在大数据量情况下，层级较深，检索速度慢的问题。

**B-Tree**

> 为了解决上述问题，可以使用 B-Tree 结构。
> B-Tree (多路平衡查找树) 以一棵最大度数（max-degree，指一个节点的子节点个数）为5（5阶）的 b-tree 为例（每个节点最多存储4个key，5个指针）

![image-20221030155707467](MySql%E5%92%8CJDBC/image-20221030155707467.png)

#### B+Tree

![image-20221030155802922](MySql%E5%92%8CJDBC/image-20221030155802922.png)

> 与 B-Tree 的区别：
>
> - **所有的数据都会出现在叶子节点**
> - **叶子节点形成一个单向链表**
>
> MySQL 索引数据结构对经典的 B+Tree 进行了优化。在原 B+Tree 的基础上，增加一个指向相邻叶子节点的链表指针，就形成了带有顺序指针的 B+Tree，提高区间访问的性能。

![image-20221030155906001](MySql%E5%92%8CJDBC/image-20221030155906001.png)

#### Hash

> 哈希索引就是**采用一定的hash算法，将键值换算成新的hash值，映射到对应的槽位上，然后存储在hash表中。**
> 如果两个（或多个）键值，映射到一个相同的槽位上，他们就产生了hash冲突（也称为hash碰撞），可以通过链表来解决。

![image-20221030161220477](MySql%E5%92%8CJDBC/image-20221030161220477.png)

> 特点：
>
> - Hash索引只能用于对等比较（=、in），不支持范围查询（betwwn、>、<、...）
> - 无法利用索引完成排序操作
> - 查询效率高，通常只需要一次检索就可以了，效率通常要高于 B+Tree 索引
>
> 存储引擎支持：
>
> - Memory
> - InnoDB: 具有自适应hash功能，hash索引是存储引擎根据 B+Tree 索引在指定条件下自动构建的

#### 面试题

> 1. 为什么 InnoDB 存储引擎选择使用 B+Tree 索引结构？
>
> - 相对于二叉树，层级更少，搜索效率高
> - 对于 B-Tree，无论是叶子节点还是非叶子节点，都会保存数据，这样导致一页中存储的键值减少，指针也跟着减少，要同样保存大量数据，只能增加树的高度，导致性能降低
> - 相对于 Hash 索引，B+Tree 支持范围匹配及排序操作

### 索引分类

| 分类     | 含义                                                 | 特点                     | 关键字   |
| -------- | ---------------------------------------------------- | ------------------------ | -------- |
| 主键索引 | 针对于表中主键创建的索引                             | 默认自动创建，只能有一个 | PRIMARY  |
| 唯一索引 | 避免同一个表中某数据列中的值重复                     | 可以有多个               | UNIQUE   |
| 常规索引 | 快速定位特定数据                                     | 可以有多个               |          |
| 全文索引 | 全文索引查找的是文本中的关键词，而不是比较索引中的值 | 可以有多个               | FULLTEXT |

> 在 InnoDB 存储引擎中，根据索引的存储形式，又可以分为以下两种：

| 分类                      | 含义                                                       | 特点                 |
| ------------------------- | ---------------------------------------------------------- | -------------------- |
| 聚集索引(Clustered Index) | 将数据存储与索引放一块，索引结构的叶子节点保存了行数据     | 必须有，而且只有一个 |
| 二级索引(Secondary Index) | 将数据与索引分开存储，索引结构的叶子节点关联的是对应的主键 | 可以存在多个         |

![image-20221030162353601](MySql%E5%92%8CJDBC/image-20221030162353601.png)

> 聚集索引选取规则：
>
> - **如果存在主键，主键索引就是聚集索引**
> - **如果不存在主键，将使用第一个唯一(UNIQUE)索引作为聚集索引**
> - **如果表没有主键或没有合适的唯一索引，则 InnoDB 会自动生成一个 rowid 作为隐藏的聚集索引**

> sql语句执行的流程

![image-20221030162506805](MySql%E5%92%8CJDBC/image-20221030162506805.png)

#### 思考题

> 1\. 以下 SQL 语句，哪个执行效率高？为什么？
>
> ```mysql
> select * from user where id = 10;
> select * from user where name = 'Arm';
> -- 备注：id为主键，name字段创建的有索引
> ```
>
> 答：第一条语句，因为第二条需要回表查询，相当于两个步骤。
>
> 2\. InnoDB 主键索引的 B+Tree 高度为多少？
>
> 答：假设一行数据大小为1k，一页中可以存储16行这样的数据。InnoDB 的指针占用6个字节的空间，主键假设为bigint，占用字节数为8.
> 可得公式：`n * 8 + (n + 1) * 6 = 16 * 1024`，其中 8 表示 bigint 占用的字节数，n 表示当前节点存储的key的数量，(n + 1) 表示指针数量（比key多一个）。算出n约为1170。
>
> 如果树的高度为2，那么他能存储的数据量大概为：`1171 * 16 = 18736`；
> 如果树的高度为3，那么他能存储的数据量大概为：`1171 * 1171 * 16 = 21939856`。
>
> 另外，如果有成千上万的数据，那么就要考虑分表，涉及运维篇知识。

### 语法

> - 创建索引：
>   `CREATE [ UNIQUE | FULLTEXT ] INDEX index_name ON table_name (index_col_name, ...);`
>   如果不加 CREATE 后面不加索引类型参数，则创建的是常规索引
> - 查看索引：
>   `SHOW INDEX FROM table_name;`
> - 删除索引：
>   `DROP INDEX index_name ON table_name;`

```sql
-- name字段为姓名字段，该字段的值可能会重复，为该字段创建索引
create index idx_user_name on tb_user(name);
-- phone手机号字段的值非空，且唯一，为该字段创建唯一索引
create unique index idx_user_phone on tb_user (phone);
-- 为profession, age, status创建联合索引
create index idx_user_pro_age_stat on tb_user(profession, age, status);
-- 为email建立合适的索引来提升查询效率
create index idx_user_email on tb_user(email);

-- 删除索引
drop index idx_user_email on tb_user;
```

### 性能分析

#### 查看执行频次

> 查看当前数据库的 INSERT, UPDATE, DELETE, SELECT 访问频次：
> `SHOW GLOBAL STATUS LIKE 'Com_______';` 或者 `SHOW SESSION STATUS LIKE 'Com_______';`
> 例：`show global status like 'Com_______'`

#### 慢查询日志

> 慢查询日志记录了所有执行时间超过指定参数（long_query_time，单位：秒，默认10秒）的所有SQL语句的日志。
> MySQL的慢查询日志默认没有开启，需要在MySQL的配置文件（/etc/my.cnf）中配置如下信息：
>
> - 开启慢查询日志开关
>   `slow_query_log=1`
> - 设置慢查询日志的时间为2秒，SQL语句执行时间超过2秒，就会视为慢查询，记录慢查询日志`long_query_time=2`
>   更改后记得重启MySQL服务，日志文件位置：`/var/lib/mysql/localhost-slow.log`
> - 查看慢查询日志开关状态：
>   `show variables like 'slow_query_log';`

#### profile

> show profile 能在做SQL优化时帮我们了解时间都耗费在哪里。通过 have_profiling 参数，能看到当前 
>
> - MySQL 是否支持 profile 操作：
>   `SELECT @@have_profiling;`
> - profiling 默认关闭，可以通过set语句在session/global级别开启 profiling：
>   `SET profiling = 1;`
> - 查看所有语句的耗时：
>   `show profiles;`
> - 查看指定query_id的SQL语句各个阶段的耗时：
>   `show profile for query query_id;`
> - 查看指定query_id的SQL语句CPU的使用情况
>   `show profile cpu for query query_id;`

#### explain

> EXPLAIN 或者 DESC 命令获取 MySQL 如何执行 SELECT 语句的信息，包括在 SELECT 语句执行过程中表如何连接和连接的顺序。
> 语法：
>
> ```sql
> # 直接在select语句之前加上关键字 explain / desc
> EXPLAIN SELECT 字段列表 FROM 表名 HWERE 条件;
> ```
>
> EXPLAIN 各字段含义：
>
> - id：select 查询的序列号，表示查询中执行 select 子句或者操作表的顺序（id相同，执行顺序从上到下；id不同，值越大越先执行）
> - select_type：表示 SELECT 的类型，常见取值有 SIMPLE（简单表，即不适用表连接或者子查询）、PRIMARY（主查询，即外层的查询）、UNION（UNION中的第二个或者后面的查询语句）、SUBQUERY（SELECT/WHERE之后包含了子查询）等
> - type：表示连接类型，性能由好到差的连接类型为 NULL、system、const、eq_ref、ref、range、index、all
> - possible_key：可能应用在这张表上的索引，一个或多个
> - Key：实际使用的索引，如果为 NULL，则没有使用索引
> - Key_len：表示索引中使用的字节数，该值为索引字段最大可能长度，并非实际使用长度，在不损失精确性的前提下，长度越短越好
> - rows：MySQL认为必须要执行的行数，在InnoDB引擎的表中，是一个估计值，可能并不总是准确的
> - filtered：表示返回结果的行数占需读取行数的百分比，filtered的值越大越好





















































































































































































































## SQL 优化

### 插入数据

普通插入：

1. 采用批量插入（一次插入的数据不建议超过1000条）
2. 手动提交事务
3. 主键顺序插入

大批量插入：
如果一次性需要插入大批量数据，使用insert语句插入性能较低，此时可以使用MySQL数据库提供的load指令插入。

```mysql
# 客户端连接服务端时，加上参数 --local-infile（这一行在bash/cmd界面输入）
mysql --local-infile -u root -p
# 设置全局参数local_infile为1，开启从本地加载文件导入数据的开关
set global local_infile = 1;
select @@local_infile;
# 执行load指令将准备好的数据，加载到表结构中
load data local infile '/root/sql1.log' into table 'tb_user' fields terminated by ',' lines terminated by '\n';
```

### 主键优化

数据组织方式：在InnoDB存储引擎中，表数据都是根据主键顺序组织存放的，这种存储方式的表称为索引组织表（Index organized table, IOT）

页分裂：页可以为空，也可以填充一般，也可以填充100%，每个页包含了2-N行数据（如果一行数据过大，会行溢出），根据主键排列。
页合并：当删除一行记录时，实际上记录并没有被物理删除，只是记录被标记（flaged）为删除并且它的空间变得允许被其他记录声明使用。当页中删除的记录到达 MERGE_THRESHOLD（默认为页的50%），InnoDB会开始寻找最靠近的页（前后）看看是否可以将这两个页合并以优化空间使用。

MERGE_THRESHOLD：合并页的阈值，可以自己设置，在创建表或创建索引时指定

> 文字说明不够清晰明了，具体可以看视频里的PPT演示过程：https://www.bilibili.com/video/BV1Kr4y1i7ru?p=90

主键设计原则：

- **满足业务需求的情况下，尽量降低主键的长度**
- **插入数据时，尽量选择顺序插入，选择使用 AUTO_INCREMENT 自增主键**
- **尽量不要使用 UUID 做主键或者是其他的自然主键，如身份证号**
- **业务操作时，避免对主键的修改**

### order by优化

1. Using filesort：通过表的索引或全表扫描，读取满足条件的数据行，然后在排序缓冲区 sort buffer 中完成排序操作，所有不是通过索引直接返回排序结果的排序都叫 FileSort 排序
2. Using index：通过有序索引顺序扫描直接返回有序数据，这种情况即为 using index，不需要额外排序，操作效率高

如果order by字段全部使用升序排序或者降序排序，则都会走索引，但是如果一个字段升序排序，另一个字段降序排序，则不会走索引，explain的extra信息显示的是`Using index, Using filesort`，如果要优化掉Using filesort，则需要另外再创建一个索引，如：`create index idx_user_age_phone_ad on tb_user(age asc, phone desc);`，此时使用`select id, age, phone from tb_user order by age asc, phone desc;`会全部走索引

最左前缀法则：

如果索引关联了多列（联合索引），要遵守最左前缀法则，**最左前缀法则指的是查询从索引的最左列开始，并且不跳过索引中的列。**
**如果跳跃某一列，索引将部分失效（后面的字段索引失效）。**

联合索引中，出现范围查询（<, >），范围查询右侧的列索引失效。可以用>=或者<=来规避索引失效问题。

总结：

- **根据排序字段建立合适的索引，多字段排序时，也遵循最左前缀法则**
- **尽量使用覆盖索引**
- **多字段排序，一个升序一个降序，此时需要注意联合索引在创建时的规则（ASC/DESC）**
- **如果不可避免出现filesort，大数据量排序时，可以适当增大排序缓冲区大小 sort_buffer_size（默认256k）**

### group by优化

- **在分组操作时，可以通过索引来提高效率**
- **分组操作时，索引的使用也是满足最左前缀法则的**

如索引为`idx_user_pro_age_stat`，则句式可以是`select ... where profession order by age`，这样也符合最左前缀法则

### limit优化

常见的问题如`limit 2000000, 10`，此时需要 MySQL 排序前2000000条记录，但仅仅返回2000000 - 2000010的记录，其他记录丢弃，查询排序的代价非常大。
优化方案：一般分页查询时，通过创建覆盖索引能够比较好地提高性能，可以通过覆盖索引加子查询形式进行优化

例如：

```mysql
-- 此语句耗时很长
select * from tb_sku limit 9000000, 10;
-- 通过覆盖索引加快速度，直接通过主键索引进行排序及查询
select id from tb_sku order by id limit 9000000, 10;
-- 下面的语句是错误的，因为 MySQL 不支持 in 里面使用 limit
-- select * from tb_sku where id in (select id from tb_sku order by id limit 9000000, 10);
-- 通过连表查询即可实现第一句的效果，并且能达到第二句的速度
select * from tb_sku as s, (select id from tb_sku order by id limit 9000000, 10) as a where s.id = a.id;
```

### count优化

MyISAM 引擎把一个表的总行数存在了磁盘上，因此执行 count(\*) 的时候会直接返回这个数，效率很高（前提是不适用where）；
InnoDB 在执行 count(\*) 时，需要把数据一行一行地从引擎里面读出来，然后累计计数。
优化方案：自己计数，如创建key-value表存储在内存或硬盘，或者是用redis

count的几种用法：

- 如果count函数的参数（count里面写的那个字段）不是NULL（字段值不为NULL），累计值就加一，最后返回累计值
- 用法：count(\*)、count(主键)、count(字段)、count(1)
- count(主键)跟count(\*)一样，因为主键不能为空；count(字段)只计算字段值不为NULL的行；count(1)引擎会为每行添加一个1，然后就count这个1，返回结果也跟count(\*)一样；count(null)返回0

各种用法的性能：

- count(主键)：InnoDB引擎会遍历整张表，把每行的主键id值都取出来，返回给服务层，服务层拿到主键后，直接按行进行累加（主键不可能为空）
- count(字段)：没有not null约束的话，InnoDB引擎会遍历整张表把每一行的字段值都取出来，返回给服务层，服务层判断是否为null，不为null，计数累加；有not null约束的话，InnoDB引擎会遍历整张表把每一行的字段值都取出来，返回给服务层，直接按行进行累加
- count(1)：InnoDB 引擎遍历整张表，但不取值。服务层对于返回的每一层，放一个数字 1 进去，直接按行进行累加
- count(\*)：InnoDB 引擎并不会把全部字段取出来，而是专门做了优化，不取值，服务层直接按行进行累加

按效率排序：count(字段) < count(主键) < count(1) < count(\*)，所以尽量使用 count(\*)

### update优化（避免行锁升级为表锁）

InnoDB 的行锁是针对索引加的锁，不是针对记录加的锁，并且该索引不能失效，否则会从行锁升级为表锁。

如以下两条语句：
`update student set no = '123' where id = 1;`，这句由于id有主键索引，所以只会锁这一行；
`update student set no = '123' where name = 'test';`，这句由于name没有索引，所以会把整张表都锁住进行数据更新，解决方法是给name字段添加索引

<hr/>



































































































# JDBC篇

## JDBC连接

> Java代码中如果要访问MySQL，那么必须编写代码操作JDBC接口。注意到JDBC接口是Java标准库自带的，所以可以直接编译。而具体的JDBC驱动是由数据库厂商提供的，例如，MySQL的JDBC驱动由Oracle提供。因此，访问某个具体的数据库，我们只需要引入该厂商提供的JDBC驱动，就可以通过JDBC接口来访问，这样保证了Java程序编写的是一套数据库访问代码，却可以访问各种不同的数据库，因为他们都提供了标准的JDBC驱动	
>
> 因为JDBC接口并不知道我们要使用哪个数据库，所以，用哪个数据库，我们就去使用哪个数据库的“实现类”，我们把某个数据库实现了JDBC接口的jar包称为JDBC驱动。
>
> 首先得找一个MySQL的JDBC驱动。所谓JDBC驱动，其实就是一个第三方jar包，我们直接添加一个Maven依赖就可以了：
>
> ```xml
> <dependency>
>       <groupId>mysql</groupId>
>       <artifactId>mysql-connector-java</artifactId>
>       <version>5.1.47</version>
>       <scope>runtime</scope>
> </dependency>
> ```

> 使用JDBC时，我们先了解什么是Connection。Connection代表一个JDBC连接，它相当于Java程序到数据库的连接（通常是TCP连接）。打开一个Connection时，需要准备URL、用户名和口令，才能成功连接到数据库。
>
> URL是由数据库厂商指定的格式，例如，MySQL的URL是：
>
> ```java
> jdbc:mysql://<hostname>:<port>/<db>?key1=value1&key2=value2
> ```
>
> 假设数据库运行在本机`localhost`，端口使用标准的`3306`，数据库名称是`learnjdbc`，那么URL如下：
>
> ```java
> jdbc:mysql://localhost:3306/learnjdbc?useSSL=false&characterEncoding=utf8
> ```
>
> 后面的两个参数表示不使用SSL加密，使用UTF-8作为字符编码（注意MySQL的UTF-8是`utf8`）。
>
> 要获取数据库连接，使用如下代码：
>
> ```java
> // JDBC连接的URL, 不同数据库有不同的格式:
> String JDBC_URL = "jdbc:mysql://localhost:3306/test";
> String JDBC_USER = "root";
> String JDBC_PASSWORD = "password";
> // 获取连接:
> Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
> // TODO: 访问数据库...
> // 关闭连接:
> conn.close();
> ```
>
> 核心代码是`DriverManager`提供的静态方法`getConnection()`。`DriverManager`会自动扫描classpath，找到所有的JDBC驱动，然后根据我们传入的URL自动挑选一个合适的驱动。
>
> 因为JDBC连接是一种昂贵的资源，所以使用后要及时释放。使用`try (resource)`来自动释放JDBC连接是一个好方法：
>
> ```java
> try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
>  ...
> }
> ```

## JDBC查询

> 在第一步，通过`Connection`提供的`createStatement()`方法创建一个`Statement`对象，用于执行一个查询；
>
> 第二步，执行`Statement`对象提供的`executeQuery("SELECT * FROM students")`并传入SQL语句，执行查询并获得返回的结果集，使用`ResultSet`来引用这个结果集；
>
> 第三步，反复调用`ResultSet`的`next()`方法并读取每一行结果。

```java
package com.demo16_JDBC;

import java.sql.*;

public class demo01_jdbc查询 {
    public static void main(String[] args) throws SQLException {
        final String jdbc_url = "jdbc:mysql://localhost:3306/learnjdbc";
        final String jdbc_user = "root";
        final String jdbc_password = "123456";

        try (Connection connection = DriverManager.getConnection(jdbc_url, jdbc_user, jdbc_password)) {
            try (Statement statement = connection.createStatement();) {
                try (ResultSet resultSet = statement.executeQuery("select name,id,gender,grade,score from students");) {
                    //rs.next()用于判断是否有下一行记录，如果有，将自动把当前行移动到下一行（一开始获得ResultSet时当前行不是第一行）；
                    while (resultSet.next()) {
                        //必须根据SELECT的列的对应位置来调用getLong(1)，getString(2)这些方法，否则对应位置的数据类型不对，将报错。如果select查询直接是*的话，这个get(1 2 ...)是在设计表的时候的列的顺序
                        long id = resultSet.getLong(2); // 注意：索引从1开始
                        String name = resultSet.getString(1);
                        long gender = resultSet.getLong(3);
                        long grade = resultSet.getLong(4);
                        long score = resultSet.getLong(5);
                        System.out.println(id + "-" + name + "-" + gender + "-" + grade + "-" + score);
                    }
                }
            }
        }
    }
}

```

### sql注入

> 使用`Statement`拼字符串非常容易引发SQL注入的问题,如果用户的输入是一个精心构造的字符串，就可以拼出意想不到的SQL，这个SQL也是正确的，但它查询的条件不是程序设计的意图。
>
> **使用`PreparedStatement`可以完全避免SQL注入的问题**，因为`PreparedStatement`始终使用`?`作为占位符，并且把数据连同SQL本身传给数据库，这样可以保证每次传给数据库的SQL语句是相同的，只是占位符的数据不同，还能高效利用数据库本身对查询的缓存。**`PreparedStatement`比`Statement`更安全，而且更快。**
>
> <span style="color:red">**使用Java对数据库进行操作时，必须使用PreparedStatement，严禁任何通过参数拼字符串的代码！**</span>

```java
package com.demo16_JDBC;

import java.sql.*;

public class demo01_jdbc查询 {
    public static void main(String[] args) throws SQLException {
        final String jdbc_url = "jdbc:mysql://localhost:3306/learnjdbc";
        final String jdbc_user = "root";
        final String jdbc_password = "123456";

        /**
         * 使用preparedStatement
         */

        try (Connection connection = DriverManager.getConnection(jdbc_url, jdbc_user, jdbc_password);) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("select name,id,gender,grade,score from students where grade = ? and score >=?");) {
                //这里的1  2 和上面查询语句的？时一一对应的
                preparedStatement.setObject(1, 2);
                preparedStatement.setObject(2, 90);
                try (ResultSet resultSet = preparedStatement.executeQuery();) {

                    while (resultSet.next()) {
                        //必须根据SELECT的列的对应位置来调用getLong(1)，getString(2)这些方法，否则对应位置的数据类型不对，将报错。
                        long id = resultSet.getLong(2);
                        String name = resultSet.getString(1);
                        int gender = resultSet.getInt(3);
                        int grade = resultSet.getInt(4);
                        long score = resultSet.getLong(5);
                        System.out.println(id + "-" + name + "-" + gender + "-" + grade + "-" + score);
                    }
                }
            }
        }
    }
}

```

## JDBC更新

### 插入

> 插入操作是`INSERT`，即插入一条新记录。通过JDBC进行插入，本质上也是用`PreparedStatement`执行一条SQL语句，不过最后执行的不是`executeQuery()`，而是`executeUpdate()`
>
> 当成功执行`executeUpdate()`后，返回值是`int`，表示插入的记录数量。

```java
package com.demo16_JDBC;

import java.sql.*;
import java.util.Date;

public class demo02_插入 {
    public static void main(String[] args) throws SQLException {
        final String jdbc_url = "jdbc:mysql://localhost:3306/learnjdbc";
        final String jdbc_user = "root";
        final String jdbc_password = "123456";

        try (Connection connection = DriverManager.getConnection(jdbc_url, jdbc_user, jdbc_password);) {
            // 插入语句中的列标可以和数据库中的顺序不一致
            try (PreparedStatement preparedStatement = connection.prepareStatement("insert into students(name,id,gender,grade,score) values(?,?,?,?,?)");) {
                //这里的1 2...和上面插入语句中的？是一一对应的，也就是说与列标对应
                preparedStatement.setObject(2, new Date().getTime());
                preparedStatement.setObject(1, "lz001");
                preparedStatement.setObject(3, 1);
                preparedStatement.setObject(4, 10);
                preparedStatement.setObject(5, 100);
                int i = preparedStatement.executeUpdate();
                System.out.println(i);
            }
        }
    }
}

```

### 插入并获取主键

> 如果数据库的表设置了自增主键，那么在执行`INSERT`语句时，并不需要指定主键，数据库会自动分配主键。对于使用自增主键的程序，有个额外的步骤，就是如何获取插入后的自增主键的值。
>
> 要获取自增主键，不能先插入，再查询。因为两条SQL执行期间可能有别的程序也插入了同一个表。**获取自增主键的正确写法是在创建`PreparedStatement`的时候，指定一个`RETURN_GENERATED_KEYS`标志位，表示JDBC驱动必须返回插入的自增主键**

```java
package com.demo16_JDBC;

import java.sql.*;
import java.util.Date;

public class demo02_插入 {
    public static void main(String[] args) throws SQLException {
        final String jdbc_url = "jdbc:mysql://localhost:3306/learnjdbc";
        final String jdbc_user = "root";
        final String jdbc_password = "123456";

        /**
         * 插入数据返回主键
         */
        try (Connection connection = DriverManager.getConnection(jdbc_url, jdbc_user, jdbc_password);) {
            //将id设置为主键，就不用再设置id的值了，主键是自增长的。必须传入常量Statement.RETURN_GENERATED_KEYS，否则JDBC驱动不会返回自增主键；
            try (PreparedStatement preparedStatement = connection.prepareStatement("insert into students(name,gender,grade,score) values(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);) {
                preparedStatement.setObject(1, "lz");
                preparedStatement.setObject(2, 1);
                preparedStatement.setObject(3, 10);
                preparedStatement.setObject(4, 100);
                int i = preparedStatement.executeUpdate();
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys();) {
                    if (generatedKeys.next()) {
                        System.out.println(generatedKeys.getLong(1));// 注意：索引从1开始
                    }
                }
            }
        }
    }
}

```

> 必须调用`getGeneratedKeys()`获取一个`ResultSet`对象，这个对象包含了数据库自动生成的主键的值，读取该对象的每一行来获取自增主键的值。如果一次插入多条记录，那么这个`ResultSet`对象就会有多行返回值。如果插入时有多列自增，那么`ResultSet`对象的每一行都会对应多个自增值（自增列不一定必须是主键）。

### 更新

> 更新操作是`UPDATE`语句，它可以一次更新若干列的记录。更新操作和插入操作在JDBC代码的层面上实际上没有区别，除了SQL语句不同：
>
> ```java
> try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
>      try (PreparedStatement ps = conn.prepareStatement("UPDATE students SET name=? WHERE id=?")) {
>          ps.setObject(1, "Bob"); // 注意：索引从1开始
>          ps.setObject(2, 999);
>          int n = ps.executeUpdate(); // 返回更新的行数
>      }
> }
> ```
>
> `executeUpdate()`返回数据库实际更新的行数。返回结果可能是正数，也可能是0（表示没有任何记录更新）。

### 删除

> 删除操作是`DELETE`语句，它可以一次删除若干列。和更新一样，除了SQL语句不同外，JDBC代码都是相同的：
>
> ```java
> try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
>      try (PreparedStatement ps = conn.prepareStatement("DELETE FROM students WHERE id=?")) {
>          ps.setObject(1, 999); // 注意：索引从1开始
>          int n = ps.executeUpdate(); // 删除的行数
>      }
> }
> ```

### 提取通用配置

```java
package com.lz.fruit.dao.base;

import java.sql.*;

public abstract class baseDAO {

    protected final String url = "jdbc:mysql://localhost:3306/fruitdb?useSSL=false&characterEncoding=utf8";
    protected final String username = "root";
    protected final String password = "123456";


    protected Connection connection;
    protected PreparedStatement preparedStatement;
    protected ResultSet resultSet;


    //    连接数据库
    protected Connection getConnect() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //    释放资源
    protected void close(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
//        数据从下往上关闭
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

```

## 提取通用增删改

```java
package com.lz.fruit.dao.base;

import java.sql.*;

public abstract class baseDAO {

	......

    //    通用增删改
    protected int executeUpdate(String sql, Object... params) {
        try {
            connection = getConnect();
            preparedStatement = connection.prepareStatement(sql);

            if (params != null && params.length > 0) {
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setObject(i + 1, params[i]);
                }
            }
//            返回执行条数
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return 0;
    }

}

```

## 提取通用查询

> 方法一。感觉这个好用，但是`newInstance()`方法好像要淘汰了
>
> 注意：在获取列名的时候，获取的要和实体类中的属性名一致。如果不一致可以在执行sql语句时起别名，执行getColumnlabel()方法，获取别名

```java
    /**
     * 通用查询,方法一
     * 
     * @return
     */
    public List<T> executeQuery(Class<T> clazz, String sql, Object... params) {

        ArrayList<T> fruits = new ArrayList<>();

        try {
            connection = DriverManager.getConnection(url, username, password);
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
//                实例化,相当于xxx x = new xxx()
                T t = clazz.newInstance();
//                获取数据集元数据
                ResultSetMetaData metaData = resultSet.getMetaData();
//                获取列数
                int columnCount = metaData.getColumnCount();

                for (int i = 0; i < columnCount; i++) {
//                    获取列的值
                    Object columnValue = resultSet.getObject(i + 1);
//                    获取列的名,获取的列名和实体类中的属性名是一样的必须.
//                    如果不一样就需要给sql语句起别名,然后这里执行getColumnlabel()
                    String columnName = metaData.getColumnName(i + 1);

//                    获取字段属性
                    Field declaredField = clazz.getDeclaredField(columnName);

                    declaredField.setAccessible(true);

//                    设置字段属性的值
                    declaredField.set(t, columnValue);

                }
                fruits.add(t);
            }
//            在while循环外return出去
            return fruits;
        } catch (SQLException | InstantiationException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return null;
    }
```

> 方法二
>
> 方法一是通过传入实体类的反射，方法二是通过一系列方法获得，之后的操作相同

```java
    /**
     * 类似方法一种传入的实体类的反射
     */
    //T的Class对象
    private Class entityClass;

    public baseDAO() {
        //getClass() 获取Class对象，当前我们执行的是new FruitDAOImpl() , 创建的是FruitDAOImpl的实例
        //那么子类构造方法内部首先会调用父类（baseDAO）的无参构造方法
        //因此此处的getClass()会被执行，但是getClass获取的是FruitDAOImpl的Class
        //所以getGenericSuperclass()获取到的是baseDAO的Class
        Type genericType = getClass().getGenericSuperclass();//com.lz.fruit.dao.base.baseDAO<com.lz.fruit.pojo.Fruit>
        //ParameterizedType 参数化类型
        Type[] actualTypeArguments = ((ParameterizedType) genericType).getActualTypeArguments();//class com.lz.fruit.pojo.Fruit
        //获取到的<T>中的T的真实的类型
        Type actualType = actualTypeArguments[0];//class com.lz.fruit.pojo.Fruit
        try {
            entityClass = Class.forName(actualType.getTypeName());//class com.lz.fruit.pojo.Fruit
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    /**
     * 给预处理命令对象设置参数
     */
    private void setParams(PreparedStatement psmt, Object... params) throws SQLException {
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                psmt.setObject(i + 1, params[i]);
            }
        }
    }


    /**
     * 通过反射技术给obj对象的property属性赋propertyValue值
     */
    private void setValue(Object obj, String property, Object propertyValue) {
        Class clazz = obj.getClass();
        try {
            //获取property这个字符串对应的属性名 ， 比如 "fid"  去找 obj对象中的 fid 属性
            Field field = clazz.getDeclaredField(property);
            if (field != null) {
                field.setAccessible(true);
                field.set(obj, propertyValue);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * 执行查询，返回List
     */
    protected List<T> executeQuery(String sql, Object... params) {
        List<T> list = new ArrayList<>();
        try {
            connection = getConnect();
            preparedStatement = connection.prepareStatement(sql);
            setParams(preparedStatement, params);
            resultSet = preparedStatement.executeQuery();

            //通过rs可以获取结果集的元数据
            //元数据：描述结果集数据的数据 , 简单讲，就是这个结果集有哪些列，什么类型等等

            ResultSetMetaData rsmd = resultSet.getMetaData();
            //获取结果集的列数
            int columnCount = rsmd.getColumnCount();
            //6.解析rs
            while (resultSet.next()) {
                T entity = (T) entityClass.newInstance();

                for (int i = 0; i < columnCount; i++) {
                    String columnName = rsmd.getColumnName(i + 1);            //fid   fname   price
                    Object columnValue = resultSet.getObject(i + 1);     //33    苹果      5
                    setValue(entity, columnName, columnValue);
                }
                list.add(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return list;
    }


    /**
     * 执行查询，返回单个实体对象
     */
    protected T load(String sql, Object... params) {
        try {
            connection = getConnect();
            preparedStatement = connection.prepareStatement(sql);
            setParams(preparedStatement, params);
            resultSet = preparedStatement.executeQuery();

            //通过rs可以获取结果集的元数据
            //元数据：描述结果集数据的数据 , 简单讲，就是这个结果集有哪些列，什么类型等等

            ResultSetMetaData rsmd = resultSet.getMetaData();
            //获取结果集的列数
            int columnCount = rsmd.getColumnCount();
            //6.解析rs
            if (resultSet.next()) {
                T entity = (T) entityClass.newInstance();

                for (int i = 0; i < columnCount; i++) {
                    String columnName = rsmd.getColumnName(i + 1);            //fid   fname   price
                    Object columnValue = resultSet.getObject(i + 1);     //33    苹果      5
                    setValue(entity, columnName, columnValue);
                }
                return entity;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return null;
    }


    /**
     * 执行复杂查询，返回例如统计结果
     */
    protected Object[] executeComplexQuery(String sql, Object... params) {
        try {
            connection = getConnect();
            preparedStatement = connection.prepareStatement(sql);
            setParams(preparedStatement, params);
            resultSet = preparedStatement.executeQuery();

            //通过rs可以获取结果集的元数据
            //元数据：描述结果集数据的数据 , 简单讲，就是这个结果集有哪些列，什么类型等等

            ResultSetMetaData rsmd = resultSet.getMetaData();
            //获取结果集的列数
            int columnCount = rsmd.getColumnCount();
            Object[] columnValueArr = new Object[columnCount];
            //6.解析rs
            if (resultSet.next()) {
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = resultSet.getObject(i + 1);     //33    苹果      5
                    columnValueArr[i] = columnValue;
                }
                return columnValueArr;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return null;
    }

```

## insert获取自增列的值

> 如果数据库的表设置了自增主键，那么在执行`INSERT`语句时，并不需要指定主键，数据库会自动分配主键。对于使用自增主键的程序，有个额外的步骤，就是如何获取插入后的自增主键的值。
>
> 要获取自增主键，不能先插入，再查询。因为两条SQL执行期间可能有别的程序也插入了同一个表。**获取自增主键的正确写法是在创建`PreparedStatement`的时候，指定一个`RETURN_GENERATED_KEYS`标志位，表示JDBC驱动必须返回插入的自增主键**

```java
package com.demo16_JDBC;

import java.sql.*;
import java.util.Date;

public class demo02_插入 {
    public static void main(String[] args) throws SQLException {
        final String jdbc_url = "jdbc:mysql://localhost:3306/learnjdbc";
        final String jdbc_user = "root";
        final String jdbc_password = "123456";

        /**
         * 插入数据返回主键
         */
        try (Connection connection = DriverManager.getConnection(jdbc_url, jdbc_user, jdbc_password);) {
            //将id设置为主键，就不用再设置id的值了，主键是自增长的。必须传入常量Statement.RETURN_GENERATED_KEYS，否则JDBC驱动不会返回自增主键；
            try (PreparedStatement preparedStatement = connection.prepareStatement("insert into students(name,gender,grade,score) values(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);) {
                preparedStatement.setObject(1, "lz");
                preparedStatement.setObject(2, 1);
                preparedStatement.setObject(3, 10);
                preparedStatement.setObject(4, 100);
                int i = preparedStatement.executeUpdate();
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys();) {
                    if (generatedKeys.next()) {
                        System.out.println(generatedKeys.getLong(1));// 注意：索引从1开始
                    }
                }
            }
        }
    }
}

```

> 必须调用`getGeneratedKeys()`获取一个`ResultSet`对象，这个对象包含了数据库自动生成的主键的值，读取该对象的每一行来获取自增主键的值。如果一次插入多条记录，那么这个`ResultSet`对象就会有多行返回值。如果插入时有多列自增，那么`ResultSet`对象的每一行都会对应多个自增值（自增列不一定必须是主键）。

### 在通用增删改中获取

```java
//    通用增删改
protected int executeUpdate(String sql, Object... params) {
    //判断是不是insert语句
    boolean insertFlag = false;
    insertFlag = sql.trim().toUpperCase().startsWith("INSERT");
    try {
        connection = getConnect();
        
        if (insertFlag) {
            //insert语句设置自增列
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        } else {
            preparedStatement = connection.prepareStatement(sql);
        }
        
        setParams(preparedStatement, params);
        int count = preparedStatement.executeUpdate();

        if (insertFlag) {
            //获取自增列的值
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                return ((Long) resultSet.getLong(1)).intValue();
            }
        } 
	 return count;
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        close(connection, preparedStatement, resultSet);
    }
    return 0;
}
```

### JDBC事务

> 要在JDBC中执行事务，本质上就是如何把多条SQL包裹在一个数据库事务中执行。我们来看JDBC的事务代码：
>
> ```java
> Connection conn = openConnection();
> try {
>  // 关闭自动提交:
>  conn.setAutoCommit(false);
>  // 执行多条SQL语句:
>  insert(); update(); delete();
>  // 提交事务:
>  conn.commit();
> } catch (SQLException e) {
>  // 回滚事务:
>  conn.rollback();
> } finally {
>  conn.setAutoCommit(true);
>  conn.close();
> }
> ```
>
> 其中，开启事务的关键代码是`conn.setAutoCommit(false)`，表示关闭自动提交。提交事务的代码在执行完指定的若干条SQL语句后，调用`conn.commit()`。要注意事务不是总能成功，如果事务提交失败，会抛出SQL异常（也可能在执行SQL语句的时候就抛出了），此时我们必须捕获并调用`conn.rollback()`回滚事务。最后，在`finally`中通过`conn.setAutoCommit(true)`把`Connection`对象的状态恢复到初始值。
>
> 实际上，默认情况下，我们获取到`Connection`连接后，总是处于“自动提交”模式，也就是每执行一条SQL都是作为事务自动执行的，这也是为什么前面几节我们的更新操作总能成功的原因：因为默认有这种“隐式事务”。只要关闭了`Connection`的`autoCommit`，那么就可以在一个事务中执行多条语句，事务以`commit()`方法结束。
>
> 如果要设定事务的隔离级别，可以使用如下代码：
>
> ```java
> // 设定隔离级别为READ COMMITTED:
> conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
> ```
>
> 如果没有调用上述方法，那么会使用数据库的默认隔离级别。MySQL的默认隔离级别是`REPEATABLE READ`。

```java
package com.demo16_JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class demo07_jdbc事务 {
    public static void main(String[] args) throws SQLException {
        final String url = "jdbc:mysql://localhost:3306/learnjdbc";
        final String username = "root";
        final String password = "123456";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
            // 关闭自动提交
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO students (name, gender, grade, score) VALUES (?, ?, ?, ?)");) {
                preparedStatement.setObject(1, "事务");
                preparedStatement.setObject(2, 1);
                preparedStatement.setObject(3, 1);
                preparedStatement.setObject(4, 100);

                int i = preparedStatement.executeUpdate();
                connection.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
        }
    }
}

```

## jdbcBatch

```java
package com.lz.fruit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class demo01_jdbcBatch {
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/fruitdb?useSSL=false&characterEncoding=utf8";
        String username = "root";
        String password = "123456";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement("insert into t_fruit(fid,fname,price,fcount,remark) values (?,?,?,?,?)");
            for (int i = 0; i < 10; i++) {
                preparedStatement.setObject(1, 120 + i);
                preparedStatement.setObject(2, "damie");
                preparedStatement.setObject(3, i);
                preparedStatement.setObject(4, i);
                preparedStatement.setObject(5, i);
                preparedStatement.addBatch();

//                如果任务过多,可以分一千处理一次
                if (i % 1000==0){
                    preparedStatement.executeBatch();
                    preparedStatement.clearBatch();//清空批处理
                }
            }

            int[] ints = preparedStatement.executeBatch();
            for (int anInt : ints) {
                System.out.println(anInt);
            }
            System.out.println(ints);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

```

## JDBC连接池

> 在执行JDBC的增删改查的操作时，如果每一次操作都来一次打开连接，操作，关闭连接，那么创建和销毁JDBC连接的开销就太大了。为了避免频繁地创建和销毁JDBC连接，我们可以通过连接池（Connection Pool）复用已经创建好的连接。
>
> JDBC连接池有一个标准的接口`javax.sql.DataSource`，注意这个类位于Java标准库中，但仅仅是接口。要使用JDBC连接池，我们必须选择一个JDBC连接池的实现。常用的JDBC连接池有：
>
> - HikariCP
> - C3P0
> - BoneCP
> - Druid

### HikariCP

> 前使用最广泛的是HikariCP。我们以HikariCP为例，要使用JDBC连接池，先添加HikariCP的依赖如下：
>
> ```XML
> <dependency>
>      <groupId>com.zaxxer</groupId>
>      <artifactId>HikariCP</artifactId>
>      <version>2.7.1</version>
> </dependency>
> ```
>
> 紧接着，我们需要创建一个`DataSource`实例，这个实例就是连接池：
>
> ```JAVA
> HikariConfig config = new HikariConfig();
> config.setJdbcUrl("jdbc:mysql://localhost:3306/test");
> config.setUsername("root");
> config.setPassword("password");
> config.addDataSourceProperty("connectionTimeout", "1000"); // 连接超时：1秒
> config.addDataSourceProperty("idleTimeout", "60000"); // 空闲超时：60秒
> config.addDataSourceProperty("maximumPoolSize", "10"); // 最大连接数：10
> DataSource ds = new HikariDataSource(config);
> ```
>
> 注意创建`DataSource`也是一个非常昂贵的操作，所以通常`DataSource`实例总是作为一个全局变量存储，并贯穿整个应用程序的生命周期。
>
> 有了连接池以后，我们如何使用它呢？和前面的代码类似，只是获取`Connection`时，把`DriverManage.getConnection()`改为`ds.getConnection()`：
>
> ```JAVA
> try (Connection conn = ds.getConnection()) { // 在此获取连接
>  ...
> } // 在此“关闭”连接
> ```
>
> 通过连接池获取连接时，并不需要指定JDBC的相关URL、用户名、口令等信息，因为这些信息已经存储在连接池内部了（创建`HikariDataSource`时传入的`HikariConfig`持有这些信息）。一开始，连接池内部并没有连接，所以，第一次调用`ds.getConnection()`，会迫使连接池内部先创建一个`Connection`，再返回给客户端使用。当我们调用`conn.close()`方法时（`在try(resource){...}`结束处），不是真正“关闭”连接，而是释放到连接池中，以便下次获取连接时能直接返回。
>
> 因此，连接池内部维护了若干个`Connection`实例，如果调用`ds.getConnection()`，就选择一个空闲连接，并标记它为“正在使用”然后返回，如果对`Connection`调用`close()`，那么就把连接再次标记为“空闲”从而等待下次调用。这样一来，我们就通过连接池维护了少量连接，但可以频繁地执行大量的SQL语句。

```java
package com.demo16_JDBC;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class demo04_连接池HikariCP {
    public static void main(String[] args) throws SQLException {

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://localhost/learnjdbc?useSSL=false&characterEncoding=utf8");
        hikariConfig.setUsername("root");
        hikariConfig.setPassword("123456");
        hikariConfig.addDataSourceProperty("connectionTimeout", "1000");//连接超时 1s
        hikariConfig.addDataSourceProperty("idleTimeout", "5000");//空闲超时 5s
        hikariConfig.addDataSourceProperty("maximumPoolSize", "10");//最大连接数 10
        HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);


        try (Connection connection = hikariDataSource.getConnection();) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("select id,name from students where score >= ?");) {
                preparedStatement.setObject(1, 90);
                try (ResultSet resultSet = preparedStatement.executeQuery();) {

                    while (resultSet.next()) {
                        long aLong = resultSet.getLong(1);
                        String string = resultSet.getString(2);
                        System.out.println(aLong);
                        System.out.println(string);
                    }
                }
            }
        }


    }
}

```

### druid连接池

> 添加druid的依赖、数据库驱动
>
> ```xml
> <dependency>
>      <groupId>com.alibaba</groupId>
>      <artifactId>druid</artifactId>
>      <version>1.1.8</version>
> </dependency>
> 
> <dependency>
>      <groupId>mysql</groupId>
>      <artifactId>mysql-connector-java</artifactId>
>      <version>8.0.19</version>
> </dependency>    
> ```

#### 纯代码模式

```java
package com.demo16_JDBC;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class demo05_连接池druid_纯代码 {
    public static void main(String[] args) throws SQLException {

        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl("jdbc:mysql://localhost:3306/learnjdbc");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("123456");

        //下面都是可选的配置
        druidDataSource.setInitialSize(10);  //初始连接数，默认0
        druidDataSource.setMaxActive(30);  //最大连接数，默认8
        druidDataSource.setMinIdle(10);  //最小闲置数
        druidDataSource.setMaxWait(2000);  //获取连接的最大等待时间，单位毫秒
        druidDataSource.setPoolPreparedStatements(true); //缓存PreparedStatement，默认false
        druidDataSource.setMaxOpenPreparedStatements(20); //缓存PreparedStatement的最大数量，默认-1（不缓存）。大于0时会自动开启缓存PreparedStatement，所以可以省略上一句代码


        try (DruidPooledConnection connection = druidDataSource.getConnection();) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("select * from students");) {
                try (ResultSet resultSet = preparedStatement.executeQuery();) {
                    while (resultSet.next()) {
                        long id = resultSet.getLong(1);
                        String name = resultSet.getString(2);
                        System.out.println(id + "-" + name);
                    }
                }
            }
        }
    }
}

```

#### 配置文件模式

```properties
#druid.properties
url=jdbc:mysql://localhost:3306/learnjdbc?serverTimezone=UTC
username=root
password=123456

##初始连接数，默认0
initialSize=10
#最大连接数，默认8
maxActive=30
#最小闲置数
minIdle=10
#获取连接的最大等待时间，单位毫秒
maxWait=2000
#缓存PreparedStatement，默认false
poolPreparedStatements=true
#缓存PreparedStatement的最大数量，默认-1（不缓存）。大于0时会自动开启缓存PreparedStatement，所以可以省略上一句设置
maxOpenPreparedStatements=20
```

```java
package com.demo16_JDBC;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

public class demo06_连接池druid_properties配置文件 {
    public static void main(String[] args) throws Exception {

        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream("src/druid.properties");
        properties.load(fileInputStream);
        DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);

//        这种方式读取properties文件也可以,但是注意一个问题项目中有resources目录，这个文件一定要放在resources目录下，没有这个目录可以放在src中，读取文件流就放在src下
//        InputStream resourceAsStream = gitClass().getResourceAsStream("/druid.properties");
//        properties.load(resourceAsStream);
//        DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);

        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("select * from students");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            long id = resultSet.getLong(1);
            String name = resultSet.getString(2);
            System.out.println(id + "-" + name);
        }
    }
}

```

## 问题

![image-20220313211951449](MySql和JDBC/image-20220313211951449.png)

> 可能就是在连接的时候没写Class.forName()
>
> ```
> Class.forName("com.mysql.jdbc.Driver")
> ```
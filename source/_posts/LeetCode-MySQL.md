---
title: LeetCode_MySQL
date: 2023-01-03 21:24:59
tags:
- LeetCode_MySQL
categories: 
- LeetCode
---

# mysql中语句的执行顺序

from > on > join > where > group by > 聚合函数 > having > select > distinct > order by > limit

# 两字段使用in

```mysql
...(e.Salary,e.DepartmentId) in (select max(Salary),DepartmentId from Employee group by De
```

# 取count最大值

```mysql
select cc.customer_number from (select count(*) c,o.customer_number from Orders o group by o.customer_number) cc order by cc.c desc limit 1

# 求count(*)的最大值

# 获取count(*)，子查询生成的这个表，再对这个count从高到低排序，然后limit 1 取第一个值，就是最大值了，
```

# mysql中if...else等函数

| 函数                                                         | 功能                                                    |
| :----------------------------------------------------------- | :------------------------------------------------------ |
| IF(value, t, f)                                              | 如果value为true，则返回t，否则返回f                     |
| IFNULL(value1, value2)                                       | 如果value1不为空，返回value1，否则返回value2            |
| CASE WHEN [ val1 ] THEN [ res1 ] … ELSE [ default ] END      | 如果val1为true，返回res1，… 否则返回default默认值       |
| CASE [ expr ] WHEN [ val1 ] THEN [ res1 ] … ELSE [ default ] END | 如果expr的值等于val1，返回res1，… 否则返回default默认值 |

## if

```mysql
		IF(expr1,expr2,expr3)
```

```mysql
		SELECT
			IF(1>0, '真', '假')
		FROM
			Table
```

## if...else

```mysql
        IF search_condition THEN
            statement_list
        ELSE
            statement_list
        END IF;
       
```

```mysql
        if stu_grade >= 90 then 
            select stu_grade,'A';  
        elseif stu_grade < 90 and stu_grade >= 80 then 
            select stu_grade,'B';  
        elseif stu_grade < 80 and stu_grade >= 70 then 
            select stu_grade,'C';  
        elseif stu_grade < 70 and stu_grade >= 60 then 
            select stu_grade,'D';  
        else 
            select stu_grade,'E'; 
        end if; 
```

## case

```mysql
		SELECT
			CASE 1
				WHEN 1 THEN '字段的值是1'
				WHEN 2 THEN '字段的值是2'
				ELSE '字段的值3'
			END
		FROM
			Table
```

```mysql
        SELECT OrderID, Quantity,
        CASE
            WHEN Quantity > 30 THEN "The quantity is greater than 30"
            WHEN Quantity = 30 THEN "The quantity is 30"
            ELSE "The quantity is under 30"
        END
        FROM OrderDetails;
```

# mysql中出现`Unknown column ‘xxx‘ in ‘having clause‘`

```mysql
# 这是因为在使用group by分组时，后面如果需要再加一个having进行判断，则所判断的字段需要在select后面出现

# 查询字段要有operation
select s.stock_name,s.operation from Stocks s group by s.stock_name HAVING s.operation ="Buy"
```

# select后无from

```mysql
	SELECT
    IFNULL(
      (SELECT DISTINCT Salary
       FROM Employee
       ORDER BY Salary DESC
        LIMIT 1 OFFSET 1),
    NULL) AS SecondHighestSalary
		
		
	# 先查出来salary，再select ifnull判断salary是否为空
	
	# select后面没有from；mysql中的用法，把值输出的意思    例如：select 1+1
```


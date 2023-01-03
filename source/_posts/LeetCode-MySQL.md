---
title: LeetCode_MySQL
date: 2023-01-03 21:24:59
tags:
- LeetCode_MySQL
categories: 
- LeetCode
---

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


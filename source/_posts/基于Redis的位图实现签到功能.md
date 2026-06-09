---
title: 基于Redis的位图实现签到功能
date: 2023-02-01 20:05:29
tags:
- 基于Redis的位图实现签到功能
categories:
- 解决方案
---

### 场景需求

适用场景如签到送积分、签到领取奖励等，大致需求如下：

- 签到1天送1积分，连续签到2天送2积分，3天送3积分，3天以上均送3积分等。
- 如果连续签到中断，则重置计数，每月初重置计数。
- 当月签到满3天领取奖励1，满5天领取奖励2，满7天领取奖励3……等等。
- 显示用户某个月的签到次数和首次签到时间。
- 在日历控件上展示用户每月签到情况，可以切换年月显示……等等。

### 设计思路

对于用户签到数据，如果每条数据都用K/V的方式存储，当用户量大的时候内存开销是非常大的。而位图(BitMap)是由一组bit位组成的，每个bit位对应0和1两个状态，虽然内部还是采用String类型存储，但Redis提供了一些指令用于直接操作位图，可以把它看作是一个bit数组，数组的下标就是偏移量。它的优点是内存开销小、效率高且操作简单，很适合用于签到这类场景。

Redis提供了以下几个指令用于操作位图：

- [SETBIT](http://redisdoc.com/bitmap/setbit.html)
- [GETBIT](http://redisdoc.com/bitmap/getbit.html)
- [BITCOUNT](http://redisdoc.com/bitmap/bitcount.html)
- [BITPOS](http://redisdoc.com/bitmap/bitpos.html)
- [BITOP](http://redisdoc.com/bitmap/bitop.html)
- [BITFIELD](http://redisdoc.com/bitmap/bitfield.html)

考虑到每月初需要重置连续签到次数，最简单的方式是按用户每月存一条签到数据（也可以每年存一条数据）。Key的格式为`u:sign:uid:yyyyMM`，Value则采用长度为4个字节（32位）的位图（最大月份只有31天）。位图的每一位代表一天的签到，1表示已签，0表示未签。

例如`u:sign:1000:201902`表示ID=1000的用户在2019年2月的签到记录。

```sh
# 用户2月17号签到
SETBIT u:sign:1000:201902 16 1 # 偏移量是从0开始，所以要把17减1

# 检查2月17号是否签到
GETBIT u:sign:1000:201902 16 # 偏移量是从0开始，所以要把17减1

# 统计2月份的签到次数
BITCOUNT u:sign:1000:201902

# 获取2月份前28天的签到数据
BITFIELD u:sign:1000:201902 get u28 0

# 获取2月份首次签到的日期
BITPOS u:sign:1000:201902 1 # 返回的首次签到的偏移量，加上1即为当月的某一天
```



#### 示例代码

```java
import redis.clients.jedis.Jedis;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 基于Redis位图的用户签到功能实现类
 * <p>
 * 实现功能：
 * 1. 用户签到
 * 2. 检查用户是否签到
 * 3. 获取当月签到次数
 * 4. 获取当月连续签到次数
 * 5. 获取当月首次签到日期
 * 6. 获取当月签到情况
 */
public class UserSignDemo {
    private Jedis jedis = new Jedis();

    /**
     * 用户签到
     *
     * @param uid  用户ID
     * @param date 日期
     * @return 之前的签到状态
     */
    public boolean doSign(int uid, LocalDate date) {
        int offset = date.getDayOfMonth() - 1;
        return jedis.setbit(buildSignKey(uid, date), offset, true);
    }

    /**
     * 检查用户是否签到
     *
     * @param uid  用户ID
     * @param date 日期
     * @return 当前的签到状态
     */
    public boolean checkSign(int uid, LocalDate date) {
        int offset = date.getDayOfMonth() - 1;
        return jedis.getbit(buildSignKey(uid, date), offset);
    }

    /**
     * 获取用户签到次数
     *
     * @param uid  用户ID
     * @param date 日期
     * @return 当前的签到次数
     */
    public long getSignCount(int uid, LocalDate date) {
        return jedis.bitcount(buildSignKey(uid, date));
    }

    /**
     * 获取当月连续签到次数
     *
     * @param uid  用户ID
     * @param date 日期
     * @return 当月连续签到次数
     */
    public long getContinuousSignCount(int uid, LocalDate date) {
        int signCount = 0;
        String type = String.format("u%d", date.getDayOfMonth());
        List<Long> list = jedis.bitfield(buildSignKey(uid, date), "GET", type, "0");
        if (list != null && list.size() > 0) {
            // 取低位连续不为0的个数即为连续签到次数，需考虑当天尚未签到的情况
            long v = list.get(0) == null ? 0 : list.get(0);
            for (int i = 0; i < date.getDayOfMonth(); i++) {
                if (v >> 1 << 1 == v) {
                    // 低位为0且非当天说明连续签到中断了
                    if (i > 0) break;
                } else {
                    signCount += 1;
                }
                v >>= 1;
            }
        }
        return signCount;
    }

    /**
     * 获取当月首次签到日期
     *
     * @param uid  用户ID
     * @param date 日期
     * @return 首次签到日期
     */
    public LocalDate getFirstSignDate(int uid, LocalDate date) {
        long pos = jedis.bitpos(buildSignKey(uid, date), true);
        return pos < 0 ? null : date.withDayOfMonth((int) (pos + 1));
    }

    /**
     * 获取当月签到情况
     *
     * @param uid  用户ID
     * @param date 日期
     * @return Key为签到日期，Value为签到状态的Map
     */
    public Map<String, Boolean> getSignInfo(int uid, LocalDate date) {
        Map<String, Boolean> signMap = new HashMap<>(date.getDayOfMonth());
        String type = String.format("u%d", date.lengthOfMonth());
        List<Long> list = jedis.bitfield(buildSignKey(uid, date), "GET", type, "0");
        if (list != null && list.size() > 0) {
            // 由低位到高位，为0表示未签，为1表示已签
            long v = list.get(0) == null ? 0 : list.get(0);
            for (int i = date.lengthOfMonth(); i > 0; i--) {
                LocalDate d = date.withDayOfMonth(i);
                signMap.put(formatDate(d, "yyyy-MM-dd"), v >> 1 << 1 != v);
                v >>= 1;
            }
        }
        return signMap;
    }

    private static String formatDate(LocalDate date) {
        return formatDate(date, "yyyyMM");
    }

    private static String formatDate(LocalDate date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    private static String buildSignKey(int uid, LocalDate date) {
        return String.format("u:sign:%d:%s", uid, formatDate(date));
    }

    public static void main(String[] args) {
        UserSignDemo demo = new UserSignDemo();
        LocalDate today = LocalDate.now();

        { // doSign
            boolean signed = demo.doSign(1000, today);
            if (signed) {
                System.out.println("您已签到：" + formatDate(today, "yyyy-MM-dd"));
            } else {
                System.out.println("签到完成：" + formatDate(today, "yyyy-MM-dd"));
            }
        }

        { // checkSign
            boolean signed = demo.checkSign(1000, today);
            if (signed) {
                System.out.println("您已签到：" + formatDate(today, "yyyy-MM-dd"));
            } else {
                System.out.println("尚未签到：" + formatDate(today, "yyyy-MM-dd"));
            }
        }

        { // getSignCount
            long count = demo.getSignCount(1000, today);
            System.out.println("本月签到次数：" + count);
        }

        { // getContinuousSignCount
            long count = demo.getContinuousSignCount(1000, today);
            System.out.println("连续签到次数：" + count);
        }

        { // getFirstSignDate
            LocalDate date = demo.getFirstSignDate(1000, today);
            System.out.println("本月首次签到：" + formatDate(date, "yyyy-MM-dd"));
        }

        { // getSignInfo
            System.out.println("当月签到情况：");
            Map<String, Boolean> signInfo = new TreeMap<>(demo.getSignInfo(1000, today));
            for (Map.Entry<String, Boolean> entry : signInfo.entrySet()) {
                System.out.println(entry.getKey() + ": " + (entry.getValue() ? "√" : "-"));
            }
        }
    }

}
```

#### 运行结果

```java
您已签到：2019-02-18
您已签到：2019-02-18
本月签到次数：11
连续签到次数：8
本月首次签到：2019-02-02
当月签到情况：
2019-02-01: -
2019-02-02: √
2019-02-03: √
2019-02-04: -
2019-02-05: -
2019-02-06: √
2019-02-07: -
2019-02-08: -
2019-02-09: -
2019-02-10: -
2019-02-11: √
2019-02-12: √
2019-02-13: √
2019-02-14: √
2019-02-15: √
2019-02-16: √
2019-02-17: √
2019-02-18: √
2019-02-19: -
2019-02-20: -
2019-02-21: -
2019-02-22: -
2019-02-23: -
2019-02-24: -
2019-02-25: -
2019-02-26: -
2019-02-27: -
2019-02-28: -
```

### 参考链接

- [Redis 命令参考](http://redisdoc.com/)
- [Redis 深度历险：核心原理与应用实践](https://juejin.im/book/5afc2e5f6fb9a07a9b362527/section/5b330620e51d4558e03ce7f8)
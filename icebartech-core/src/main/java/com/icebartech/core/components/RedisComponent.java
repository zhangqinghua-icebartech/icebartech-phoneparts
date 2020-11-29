package com.icebartech.core.components;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.icebartech.core.properties.SiteProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisComponent {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Bean("UserRedisTemplate")
    public RedisTemplate<String, Object> userRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        Jackson2JsonRedisSerializer<Object> j2jrs = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 解决jackson2无法反序列化LocalDateTime的问题
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        om.registerModule(new JavaTimeModule());
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        j2jrs.setObjectMapper(om);
        // 序列化 value 时使用此序列化方法
        template.setValueSerializer(j2jrs);
        template.setHashValueSerializer(j2jrs);
        // 序列化 key 时
        StringRedisSerializer srs = new StringRedisSerializer();
        template.setKeySerializer(srs);
        template.setHashKeySerializer(srs);
        template.afterPropertiesSet();
        return template;
    }


    @Autowired
    private SiteProperties siteProperties;

    public String composeKey(String group, String key) {
        return siteProperties.getAppName().concat(":").concat(group).concat(":").concat(key);
    }

    /**
     * 指定缓存失效时间
     *
     * @param group 分组名
     * @param key   键
     * @param time  时间(秒)
     * @return
     */
    public void expire(String group, String key, long time) {
        if (time > 0) {
            redisTemplate.expire(composeKey(group, key), time, TimeUnit.SECONDS);
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param group 分组名
     * @param key   键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public Long getExpire(String group, String key) {
        return redisTemplate.getExpire(composeKey(group, key), TimeUnit.SECONDS);
    }


    /**
     * 判断key是否存在
     *
     * @param group 分组名
     * @param key   键
     * @return true 存在 false不存在
     */
    public Boolean hasKey(String group, String key) {
        return redisTemplate.hasKey(composeKey(group, key));
    }

    /**
     * 删除缓存
     *
     * @param group 分组名
     * @param keys  可以传一个值 或多个
     */
    public void del(String group, String... keys) {
        if (keys != null && keys.length > 0) {
            for (String key : keys) {
                redisTemplate.delete(composeKey(group, key));
            }
        }
    }

    //================================Object=================================

    /**
     * 普通缓存获取
     *
     * @param group 分组名
     * @param key   键
     * @return 值
     */
    public Object get(String group, String key) {
        return key == null ? null : redisTemplate.opsForValue().get(composeKey(group, key));
    }

    /**
     * 获取符合pattern规则的所有key
     *
     * @param keyPattern 可支持正则表达式
     * @return 值
     */
    public Set<String> getKeys(String group, String keyPattern) {
        return redisTemplate.keys(composeKey(group, keyPattern));
    }

    /**
     * 获取符合pattern规则的所有key-value集合
     *
     * @param keyPattern 可支持正则表达式
     * @return 值
     */
    public Map<String, Object> getValues(String group, String keyPattern) {
        Map<String, Object> rtnMap = new HashMap<>();
        Set<String> keys = getKeys(group, keyPattern);
        for (String key : keys) {
            rtnMap.put(key, redisTemplate.opsForValue().get(key));
        }
        return rtnMap;
    }

    /**
     * 普通缓存放入
     *
     * @param group 分组名
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public void set(String group, String key, Object value) {
        set(group, key, value, -1L);
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param group 分组名
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public void set(String group, String key, Object value, long time) {
        redisTemplate.opsForValue().set(composeKey(group, key), value);
        if (time > 0) {
            expire(group, key, time);
        }
    }

    /**
     * 递增+1
     *
     * @param group 分组名
     * @param key   键
     * @return
     */
    public Long incr(String group, String key) {
        return incr(group, key, 1L);
    }

    /**
     * 递增+n
     *
     * @param group 分组名
     * @param key   键
     * @param by    要增加几(大于0)
     * @return
     */
    public Long incr(String group, String key, long by) {
        if (by < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(composeKey(group, key), by);
    }

    /**
     * 递减-1
     *
     * @param group 分组名
     * @param key   键
     * @return
     */
    public Long decr(String group, String key) {
        return decr(group, key, 1L);
    }

    /**
     * 递减-n
     *
     * @param group 分组名
     * @param key   键
     * @param by    要减少几(大于0)
     * @return
     */
    public Long decr(String group, String key, long by) {
        if (by < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(composeKey(group, key), -by);
    }

    //================================Map=================================

    /**
     * HashGet
     *
     * @param group 分组名
     * @param key   键 不能为null
     * @param item  项 不能为null
     * @return 值
     */
    public Object hashGet(String group, String key, String item) {
        return redisTemplate.opsForHash().get(composeKey(group, key), item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param group 分组名
     * @param key   键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hashMapGet(String group, String key) {
        return redisTemplate.opsForHash().entries(composeKey(group, key));
    }

    /**
     * HashSet
     *
     * @param group 分组名
     * @param key   键
     * @param map   对应多个键值
     * @return true 成功 false 失败
     */
    public void hashMapPut(String group, String key, Map<String, Object> map) {
        hashMapPut(group, key, map, -1);
    }

    /**
     * HashSet 并设置时间
     *
     * @param group 分组名
     * @param key   键
     * @param map   对应多个键值
     * @param time  时间(秒)
     * @return true成功 false失败
     */
    public void hashMapPut(String group, String key, Map<String, Object> map, long time) {
        redisTemplate.opsForHash().putAll(composeKey(group, key), map);
        if (time > 0) {
            expire(group, key, time);
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param group 分组名
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public void hashPut(String group, String key, String item, Object value) {
        hashPut(group, key, item, value, -1);
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param group 分组名
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public void hashPut(String group, String key, String item, Object value, long time) {
        redisTemplate.opsForHash().put(composeKey(group, key), item, value);
        if (time > 0) {
            expire(group, key, time);
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param group 分组名
     * @param key   键 不能为null
     * @param item  项 可以使多个 不能为null
     */
    public void hashDel(String group, String key, Object... item) {
        redisTemplate.opsForHash().delete(composeKey(group, key), item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param group 分组名
     * @param key   键 不能为null
     * @param item  项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hashHasKey(String group, String key, String item) {
        return redisTemplate.opsForHash().hasKey(composeKey(group, key), item);
    }

    /**
     * hash递增+1
     *
     * @param group 分组名
     * @param key   键
     * @param item  项
     * @return
     */
    public double hashIncr(String group, String key, String item) {
        return hashIncr(group, key, item, 1);
    }

    /**
     * hash递增+n
     *
     * @param group 分组名
     * @param key   键
     * @param item  项
     * @param by    要增加几(大于0)
     * @return
     */
    public double hashIncr(String group, String key, String item, double by) {
        return redisTemplate.opsForHash().increment(composeKey(group, key), item, by);
    }

    /**
     * hash递减-1
     *
     * @param group 分组名
     * @param key   键
     * @param item  项
     * @return
     */
    public double hashDecr(String group, String key, String item) {
        return hashDecr(group, key, item, 1);
    }

    /**
     * hash递减-n
     *
     * @param group 分组名
     * @param key   键
     * @param item  项
     * @param by    要减少几(大于0)
     * @return
     */
    public double hashDecr(String group, String key, String item, double by) {
        return redisTemplate.opsForHash().increment(composeKey(group, key), item, -by);
    }

    //============================set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param group 分组名
     * @param key   键
     * @return
     */
    public Set<Object> setGet(String group, String key) {
        try {
            return redisTemplate.opsForSet().members(composeKey(group, key));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param group 分组名
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public Boolean setHasKey(String group, String key, Object value) {
        return redisTemplate.opsForSet().isMember(composeKey(group, key), value);
    }

    /**
     * 将数据放入set缓存
     *
     * @param group  分组名
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public Long setPut(String group, String key, Object... values) {
        return redisTemplate.opsForSet().add(composeKey(group, key), values);
    }

    /**
     * 将set数据放入缓存
     *
     * @param group  分组名
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public Long setPut(String group, String key, long time, Object... values) {
        Long count = redisTemplate.opsForSet().add(composeKey(group, key), values);
        if (time > 0) {
            expire(group, key, time);
        }
        return count;
    }

    /**
     * 获取set缓存的长度
     *
     * @param group 分组名
     * @param key   键
     * @return
     */
    public Long setGetSize(String group, String key) {
        return redisTemplate.opsForSet().size(composeKey(group, key));
    }

    /**
     * 移除值为value的
     *
     * @param group  分组名
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public Long setDel(String group, String key, Object... values) {
        return redisTemplate.opsForSet().remove(composeKey(group, key), values);
    }

    //===============================list=================================


    /**
     * 获取队列缓存的内容
     *
     * @param group 分组名
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return
     */
    public List<Object> listGet(String group, String key, long start, long end) {
        return redisTemplate.opsForList().range(composeKey(group, key), start, end);
    }

    /**
     * 获取队列缓存的长度
     *
     * @param group 分组名
     * @param key   键
     * @return
     */
    public Long listGetSize(String group, String key) {
        return redisTemplate.opsForList().size(composeKey(group, key));
    }

    /**
     * 通过索引 获取队列中的值
     *
     * @param group 分组名
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object listGetIndex(String group, String key, long index) {
        return redisTemplate.opsForList().index(composeKey(group, key), index);
    }

    /**
     * 将元素放入队列(右侧放入)
     *
     * @param group 分组名
     * @param key   键
     * @param value 值
     * @return
     */
    public void listRPush(String group, String key, Object value) {
        listRPush(group, key, value, -1);
    }

    /**
     * 将元素放入队列(右侧放入)
     *
     * @param group 分组名
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public void listRPush(String group, String key, Object value, long time) {
        redisTemplate.opsForList().rightPush(composeKey(group, key), value);
        if (time > 0) {
            expire(group, key, time);
        }
    }

    /**
     * 将list放入队列(右侧放入)
     *
     * @param group 分组名
     * @param key   键
     * @param value 值
     * @return
     */
    public void listRPush(String group, String key, List<Object> value) {
        listRPush(group, key, value, -1);
    }

    /**
     * 将list放入队列(右侧放入)
     *
     * @param group 分组名
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public void listRPush(String group, String key, List<Object> value, long time) {
        redisTemplate.opsForList().rightPushAll(composeKey(group, key), value);
        if (time > 0) {
            expire(group, key, time);
        }
    }

    /**
     * 将元素放入队列(左侧放入)
     *
     * @param group 分组名
     * @param key   键
     * @param value 值
     * @return
     */
    public void listLPush(String group, String key, Object value) {
        listLPush(group, key, value, -1);
    }

    /**
     * 将元素放入队列(左侧放入)
     *
     * @param group 分组名
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public void listLPush(String group, String key, Object value, long time) {
        redisTemplate.opsForList().leftPush(composeKey(group, key), value);
        if (time > 0) {
            expire(group, key, time);
        }
    }

    /**
     * 将list放入队列(左侧放入)
     *
     * @param group 分组名
     * @param key   键
     * @param value 值
     * @return
     */
    public void listLPush(String group, String key, List<Object> value) {
        listLPush(group, key, value, -1);
    }

    /**
     * 将list放入队列(左侧放入)
     *
     * @param group 分组名
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public void listLPush(String group, String key, List<Object> value, long time) {
        redisTemplate.opsForList().leftPushAll(composeKey(group, key), value);
        if (time > 0) {
            expire(group, key, time);
        }
    }

    /**
     * 从队列的左边出队一个元素
     *
     * @param group 分组名
     * @param key   键
     * @return
     */
    public Object listLPop(String group, String key) {
        return redisTemplate.opsForList().leftPop(composeKey(group, key));
    }

    /**
     * 从队列的右边出队一个元素
     *
     * @param group 分组名
     * @param key   键
     * @return
     */
    public Object listRPop(String group, String key) {
        return redisTemplate.opsForList().rightPop(composeKey(group, key));
    }

    /**
     * 从队列的右边出队一个元素，并将其追加到另一个列表
     *
     * @param group 分组名
     * @param key1  键1
     * @param key2  键2
     * @return
     */
    public Object listRPopLPush(String group, String key1, String key2) {
        return redisTemplate.opsForList().rightPopAndLeftPush(composeKey(group, key1), composeKey(group, key2));
    }

    /**
     * 根据索引修改队列中的某条数据
     *
     * @param group 分组名
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public void listUpdIndex(String group, String key, long index, Object value) {
        redisTemplate.opsForList().set(composeKey(group, key), index, value);
    }

    /**
     * 移除N个值为value
     *
     * @param group 分组名
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public Long listRemove(String group, String key, long count, Object value) {
        return redisTemplate.opsForList().remove(composeKey(group, key), count, value);
    }

    //===============================GEO=================================

    /**
     * 新增GEO坐标
     *
     * @param group  分组名
     * @param key    键
     * @param point  坐标点
     * @param member 地址标识
     * @return
     */
    public Boolean geoAdd(String group, String key, Point point, Object member) {
        Long addedNum = redisTemplate.opsForGeo().add(composeKey(group, key), point, member);
        return null != addedNum && addedNum > 0;
    }

    /**
     * 新增GEO坐标
     *
     * @param group    分组名
     * @param key      键
     * @param location 坐标地址
     * @return
     */
    public Boolean geoAdd(String group, String key, RedisGeoCommands.GeoLocation<Object> location) {
        Long addedNum = redisTemplate.opsForGeo().add(composeKey(group, key), location);
        return null != addedNum && addedNum > 0;
    }

    /**
     * 批量新增GEO坐标
     *
     * @param group     分组名
     * @param key       键
     * @param locations 坐标地址列表
     * @return
     */
    public Long geoAdd(String group, String key, List<RedisGeoCommands.GeoLocation<Object>> locations) {
        return redisTemplate.opsForGeo().add(composeKey(group, key), locations);
    }

    /**
     * 获取保存GEO坐标
     *
     * @param group   分组名
     * @param key     键
     * @param members 坐标标识
     * @return
     */
    public List<Point> gGet(String group, String key, Object... members) {
        return redisTemplate.opsForGeo().position(composeKey(group, key), members);
    }

    /**
     * 计算两个坐标的距离
     *
     * @param group   分组名
     * @param key     键
     * @param member1 坐标1标识
     * @param member2 坐标2标识
     * @param unit    单位 m 表示单位为米。km 表示单位为千米。mi 表示单位为英里。ft 表示单位为英尺。
     * @return
     */
    public Distance geoDist(String group, String key, Object member1, Object member2, RedisGeoCommands.DistanceUnit unit) {
        return redisTemplate.opsForGeo().distance(composeKey(group, key), member1, member2, unit);
    }

    /**
     * 根据中心点坐标获取周边坐标
     *
     * @param group  分组名
     * @param key    键
     * @param center 中心点
     * @param radius 半径(单位:米)
     * @param limit  查询数量
     * @return
     */
    public GeoResults<RedisGeoCommands.GeoLocation<Object>> geoNearByXY(String group, String key, Point center, double radius, int limit) {
        Circle circle = new Circle(center, new Distance(radius));
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().includeCoordinates().sortAscending().limit(limit);
        return redisTemplate.opsForGeo().radius(composeKey(group, key), circle, args);
    }

    /**
     * 根据现有坐标为中心点获取周边坐标
     *
     * @param group  分组名
     * @param key    键
     * @param member 坐标标识
     * @param radius 半径(单位:米)
     * @param limit  查询数量
     * @return
     */
    public GeoResults<RedisGeoCommands.GeoLocation<Object>> geoNearByPlace(String group, String key, Object member, double radius, int limit) {
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().includeCoordinates().sortAscending().limit(limit);
        return redisTemplate.opsForGeo().radius(composeKey(group, key), member, new Distance(radius), args);
    }

    /**
     * 获取geoHash值
     *
     * @param group   分组名
     * @param key     键
     * @param members 坐标标识
     * @return
     */
    public List<String> geoHash(String group, String key, Object... members) {
        return redisTemplate.opsForGeo().hash(composeKey(group, key), members);
    }

    /**
     * 删除
     *
     * @param group  分组名
     * @param key    键
     * @param member 坐标标识
     * @return
     */
    public Boolean geoRemove(String group, String key, Object member) {
        Long addedNum = redisTemplate.opsForGeo().remove(composeKey(group, key), member);
        return null != addedNum && addedNum > 0;
    }

    /**
     * 返回列表中指定区间内的元素，区间以偏移量 START 和 END 指定。 其中 0 表示列表的第一个元素， 1 表示列表的第二个元素，以此类推。
     * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     *
     * @param group 分组名
     * @param key   键
     * @param start
     * @param end
     * @return
     */
    public Set<Object> zRange(String group, String key, int start, int end) {
        return redisTemplate.opsForZSet().range(composeKey(group, key), start, end);
    }

    /**
     * 从列表中移除匹配的值。
     *
     * @param group  分组名
     * @param key    键
     * @param values
     */
    public Long zRemove(String group, String key, Object... values) {
        return redisTemplate.opsForZSet().remove(composeKey(group, key), values);
    }

    /**
     * 用于将成员元素及其分数值加入到有序集当中。
     * 如果value已存在，那么更新这个成员的分数值，并通过重新插入这个成员元素，来保证该成员在正确的位置上。
     *
     * @param group 分组名
     * @param key   键
     * @param value
     * @param score
     * @return
     */
    public Boolean zAdd(String group, String key, String value, double score) {
        return redisTemplate.opsForZSet().add(composeKey(group, key), value, score);
    }

    /**
     * 创建一个Redis锁
     *
     * @param key 锁Key，如：UserService#insert(String, String)
     * @return got the lock or not
     */
    @SuppressWarnings("ConstantConditions")
    public boolean lock(String key, long lock_expire) {
        String lockKey = composeKey("redislock", key);
        long expireAt = System.currentTimeMillis() + lock_expire * 1000;

        if(redisTemplate.opsForValue().setIfAbsent(lockKey, expireAt)){
            redisTemplate.expire(lockKey, lock_expire, TimeUnit.SECONDS);
            return true;
        }

        return false;
    }

    public void unlock(String key) {
        redisTemplate.delete(composeKey("redislock", key));
    }
}
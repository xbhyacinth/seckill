package org.seckill.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * 支持主备切换和集群
 * User: tang
 * Date: 16-7-20
 * Time: 下午5:28
 * To change this template use File | Settings | File Templates.
 */
public class TKillCacheUtils {
    private static final Logger logger = LoggerFactory.getLogger(TKillCacheUtils.class);

    /**
     * 配置缓存集群Map，支持多个缓存集群的配置，利用Key进行区分，在缓存切换时根据Key从中获取对应的缓存服务进行使用
     */
    protected Map<String, JedisCacheUtils> jedisCacheUtilsGroup;

    /**
     * 设置缓存对象
     *
     * @param key 缓存对象标识
     * @param exp 缓存过期时间
     * @param o   缓存对象
     */
    public void set(String key, int exp, Object o) {
        if (o == null) {
            return;
        }
        for (Map.Entry<String, JedisCacheUtils> entry : jedisCacheUtilsGroup.entrySet()) {
            try {
                entry.getValue().setObject(key, exp, o);
            } catch (Exception e) {
                logger.error("Failed to add a object to the cache cluster！cache cluster key is" + entry.getKey(), e);
            }
        }
    }

    /**
     * 设置缓存string
     *
     * @param key 缓存对象标识
     * @param exp 缓存过期时间
     * @param o   缓存对象
     */
    public void set(String key, int exp, String o) {
        if (o == null) {
            return;
        }
        for (Map.Entry<String, JedisCacheUtils> entry : jedisCacheUtilsGroup.entrySet()) {
            try {
                entry.getValue().setStr(key, exp, o);
            } catch (Exception e) {
                logger.error("Failed to add a object to the cache cluster！cache cluster key is" + entry.getKey(), e);
            }
        }
    }
    /**
     *  将key对应value+1
     *
     * @param key 缓存对象标识
     */
    public void decr(String key) {
        for (Map.Entry<String, JedisCacheUtils> entry : jedisCacheUtilsGroup.entrySet()) {
            try {
                entry.getValue().decr(key);
            } catch (Exception e) {
                logger.error("Failed to add a object to the cache cluster！cache cluster key is" + entry.getKey(), e);
            }
        }
    }
    /**
     * 将key对应value+1
     *
     * @param key 缓存对象标识
     */
    public void incr(String key) {
        for (Map.Entry<String, JedisCacheUtils> entry : jedisCacheUtilsGroup.entrySet()) {
            try {
                entry.getValue().incr(key);
            } catch (Exception e) {
                logger.error("Failed to incr the value to the cache cluster！cache cluster key is" + entry.getKey(), e);
            }
        }
    }

    /**
     * 持久化对象到缓存集群中
     *
     * @param key 缓存对象标识
     * @param o   缓存对象
     */
    public void setEx(String key, Object o) {
        if (o == null) {
            return;
        }
        for (Map.Entry<String, JedisCacheUtils> entry : jedisCacheUtilsGroup.entrySet()) {
            try {
                entry.getValue().setObject(key, o);
            } catch (Exception e) {
                logger.error("Failed to add a object to the cache cluster！cache cluster key is" + entry.getKey(), e);
            }
        }
    }


    /**
     * 从缓存集群中获取对象
     *
     * @param key 缓存对象标识
     * @return
     */
    public Object get(String key) {
        Object o = null;
        JedisCacheUtils jedisCacheUtils = getCacheClusterInUse();
        if (jedisCacheUtils == null) {
            return o;
        }
        try {
            o = jedisCacheUtils.getObject(key);
        } catch (Exception e) {
            logger.error("Failed to get an object from cache cluster！", e);
            for (Map.Entry<String, JedisCacheUtils> entry : jedisCacheUtilsGroup.entrySet()) {
                if(entry.getKey()=="1"){
                    continue;
                }
                try {
                    return entry.getValue().getObject(key);
                } catch (Exception e1) {
                    logger.error("Failed to get an object from cache cluster！cache cluster key is" + entry.getKey(), e1);
                }
            }
        }
        return o;
    }

    /**
     * 从缓存集群中获取对象
     *
     * @param key 缓存对象标识
     * @return
     */
    public String getStr(String key) {
        String o = "";
        JedisCacheUtils jedisCacheUtils = getCacheClusterInUse();
        if (jedisCacheUtils == null) {
            return o;
        }
        try {
            o = jedisCacheUtils.getStr(key);
        } catch (Exception e) {
            logger.error("Failed to get an String from cache cluster！", e);
            for (Map.Entry<String, JedisCacheUtils> entry : jedisCacheUtilsGroup.entrySet()) {
                if(entry.getKey()=="1"){
                    continue;
                }
                try {
                    return entry.getValue().getStr(key);
                } catch (Exception e1) {
                    logger.error("Failed to get an String from cache cluster！cache cluster key is" + entry.getKey(), e1);
                }
            }
        }
        return o;
    }

    /**
     * 从缓存集群中删除key对象的对象
     *
     * @param key 缓存对象标识
     * @return
     */
    public void delete(String key) {
        for (Map.Entry<String, JedisCacheUtils> entry : jedisCacheUtilsGroup.entrySet()) {
            try {
                entry.getValue().delete(key);
            } catch (Exception e) {
                logger.error("Failed to delete an object from the cache cluster！cache cluster key is" + entry.getKey(), e);
            }
        }
    }


    /**
     * 判断key对应的缓存是否存在
     *
     * @param key
     * @return
     */
    public boolean isExist(String key) {
        JedisCacheUtils jedisCacheUtils = getCacheClusterInUse();
        if (jedisCacheUtils == null) {
            return false;
        }
        Boolean isExist = false;
        try {
            isExist = jedisCacheUtils.exists(key);
        } catch (Exception e) {
            logger.error("Failed to judge whether the object is exist!", e);
            for (Map.Entry<String, JedisCacheUtils> entry : jedisCacheUtilsGroup.entrySet()) {
                if(entry.getKey()=="1"){
                    continue;
                }
                try {
                    return  entry.getValue().exists(key);
                } catch (Exception e1) {
                    logger.error("Failed to judge whether the object is exist!！cache cluster key is" + entry.getKey(), e1);
                }
            }
        }
        return isExist;

    }



    /**
     * 设置属性字符串
     *
     * @param mapName
     * @param field
     * @param value
     */
    public void setFieldStr(String mapName, String field, String value) {
        for (Map.Entry<String, JedisCacheUtils> entry : jedisCacheUtilsGroup.entrySet()) {
            try {
                entry.getValue().setFieldStr(mapName, field, value);
            } catch (Exception e) {
                logger.error("Failed to add a field string to the cache cluster！cache cluster key is" + entry.getKey(), e);
            }
        }
    }

    /**
     * 获取属性的值
     *
     * @param key
     * @param fieldName
     */
    public String getFiledStr(String key, String fieldName) {
        JedisCacheUtils jedisCacheUtils = getCacheClusterInUse();
        if (jedisCacheUtils == null) {
            return null;
        }
        String fieldStr = null;
        try {
            fieldStr = jedisCacheUtils.getFieldStr(key, fieldName);
        } catch (Exception e) {
            logger.error("Failed to get field string from cache cluster!", e);
            for (Map.Entry<String, JedisCacheUtils> entry : jedisCacheUtilsGroup.entrySet()) {
                if(entry.getKey()=="1"){
                    continue;
                }
                try {
                    return  entry.getValue().getFieldStr(key, fieldName);
                } catch (Exception e1) {
                    logger.error("Failed to get field string from cache cluster！cache cluster key is" + entry.getKey(), e1);
                }
            }
        }
        return fieldStr;
    }

    protected JedisCacheUtils getCacheClusterInUse() {
        return jedisCacheUtilsGroup.get("1");
    }

    /**
     * 获取当前从的缓存集群
     *
     * @return
     */
    protected JedisCacheUtils getCacheClusterNotInUse() {
        return jedisCacheUtilsGroup.get("2");
    }
    /**
     * 获取正在使用的缓存主集群
     *
     * @return
     */

    public void setJedisCacheUtilsGroup(Map<String, JedisCacheUtils> jedisCacheUtilsGroup) {
        this.jedisCacheUtilsGroup = jedisCacheUtilsGroup;
    }
}

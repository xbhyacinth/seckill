package org.seckill.util;


import com.jd.jim.cli.Cluster;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 秒杀加入jimdb集群
 * Redis缓存工具类，利用京东统一的Jedis缓存云服务，对Jedis服务进行封装，进行异常捕获、监控以及日志打印。
 * 2016-07-20 升级redis集群为Jimdb集群
 * User: tang
 * To change this template use File | Settings | File Templates.
 */
public class JedisCacheUtils {

    public static final String ENCODEING = "UTF-8";

    /**
     * 机房标识 YZ:亦庄机房 HC:黄村机房 YF:永丰机房
     */
    private String flag;

    /**
     * 定义Jimdb缓存服务调用命令
     */
    private Cluster cluster;

    /**
     * Return the type of the value stored at key in form of a string. The type
     * can be one of  "none", "string", "list", "set". "none" is returned if the
     * key does not exist.
     *
     * @param key
     * @return
     */
    public String type(String key) {
        return cluster.type(key).name();
    }

    /**
     * Delete an object by key
     *
     * @param key the key under which this object should be added
     * @return
     */
    public Long delete(String key) {
        return cluster.del(key);
    }

    /**
     * Judge whether an object is exist by key
     *
     * @param key the key under which this object should be added
     * @return
     */
    public Boolean exists(String key) {
        return cluster.exists(key);
    }

    /**
     * Set string expre to redis cluster
     *
     * @param key   the key under which this object should be added
     * @param value string
     */
    public void setStr(String key, String value) {
        cluster.set(key, value);
    }

    /**
     * Add an object which has expiration time for <Code>exp</Code> to the cache cluster
     *
     * @param key   the key under which this object should be added
     * @param exp   expiration time
     * @param value String
     */
    public void setStr(String key, int exp, String value) {
        cluster.setEx(key, value, exp, TimeUnit.SECONDS);
    }

    /**
     * Get string by key
     *
     * @param key the key under which this object should be added
     * @return String
     */
    public String getStr(String key) {
        return cluster.get(key);
    }

    /**
     * The value of key increased by 1, if the value does not exist or is not digital,
     * the default is set to 0, and then implemented by 1
     *
     * @param key the key under which this object should be added
     * @return
     */
    public Long incr(String key) {
        return cluster.incr(key);
    }

    /**
     * The value of key by 1, if the value does not exist or is not digital,
     * the default is set to 0, and then execute the minus 1
     *
     * @param key the key under which this object should be added
     */
    public void decr(String key) {
        cluster.decr(key);
    }

    /**
     * Setting the map property value of map, key and value are String
     *
     * @param mapName name of map
     * @param field   field's name of object
     * @param value   field's value of object
     */
    public void setFieldStr(String mapName, String field, String value) {
        cluster.hSet(mapName, field, value);

    }


    /**
     * Gets the map attribute value of map, key and value are String
     *
     * @param mapName name of map
     * @param field   field's name of object
     */
    public String getFieldStr(String mapName, String field) {
        return cluster.hGet(mapName, field);
    }


    /**
     * Delete the specified attribute in Map, map key and value are String
     *
     * @param mapName name of map
     * @param field   field's name of object
     * @return 1 is success ,0 is no field deleted
     */
    public Long delFieldStr(String mapName, String field) {
        return cluster.hDel(mapName, field);
    }


    /**
     * The existence of the entire map object, no expiration time, MAP key and value are String
     *
     * @param mapName name of map
     * @param map     map
     */
    public void setMapStr(String mapName, Map<String, String> map) {
        cluster.hMSet(mapName, map);
    }


    /**
     * The existence of the entire map object, the expiration time is exp seconds, MAP key and value are String
     *
     * @param mapName name of map
     * @param exp     expiration time
     * @param map     map
     */
    public void setMapStr(String mapName, int exp, Map<String, String> map) {
        cluster.hMSet(mapName, map);
        if (exp > 0) {
            cluster.expire(mapName, exp, TimeUnit.SECONDS);
        }
    }


    /**
     * Gets the map object through the mapName, key and value obtained is String map
     *
     * @param mapName name of map
     * @return map
     */
    public Map<String, String> getMapStr(String mapName) {
        return cluster.hGetAll(mapName);
    }

    /**
     * If the query attribute in Map, map, key and value are String
     *
     * @param mapName name of map
     * @param field   field's name of name
     * @return
     */
    public Boolean existsFieldStr(String mapName, String field) {
        return cluster.hExists(mapName, field);
    }


    /**
     * add an object which has expiration time for permanent to the cache cluster
     *
     * @param key the key under which this object should be added
     * @param o   an object set to cache cluster
     */
    public void setObject(String key, Object o) {
        cluster.set(this.getBytes(key), SerializeUtils.serialize(o));
    }

    /**
     * add an object with expiration time for <Code>exp</Code> to the cache cluster
     *
     * @param key the key under which this object should be added
     * @param exp expiration time
     * @param o   an object set to cache cluster
     */
    public void setObject(String key, int exp, Object o) {
        byte[] keyBytes = this.getBytes(key);
        cluster.setEx(keyBytes, SerializeUtils.serialize(o),exp,TimeUnit.SECONDS);
    }

    /**
     * Get an object by key
     *
     * @param key the key under which this object should be added
     * @return
     */
    public Object getObject(String key) throws Exception {
        byte[] bytes = cluster.get(this.getBytes(key));
        if (bytes != null && bytes.length != 0) {
            return SerializeUtils.unserialize(bytes);
        }
        return null;
    }


    /**
     * Stored in the map object, no expiration time, of which map key and value are Object objects
     *
     * @param mapName name of map
     * @param map     map
     */
    public void setMapObject(String mapName, Map<String, Object> map) {
        this.setMapObject(mapName, 0, map);
    }

    /**
     * Stored in the map object, which contains the expiration time, map key and value are Object objects
     *
     * @param mapName name of map
     * @param exp     expiration time
     * @param map     map
     */
    public void setMapObject(String mapName, int exp, Map<String, Object> map) {
        if (map == null) {
            return;
        }
        Map<byte[], byte[]> byteMap = new HashMap<byte[], byte[]>();
        Set<Map.Entry<String, Object>> entrySet = map.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            byteMap.put(getBytes(entry.getKey()), SerializeUtils.serialize(entry.getValue()));
        }
        byte[] mapNameBytes = getBytes(mapName);
        cluster.hMSet(mapNameBytes, byteMap);
        if (exp > 0) {
            cluster.expire(mapNameBytes, exp, TimeUnit.SECONDS);
        }
    }

    /**
     * Obtain the map object, which map key and value are Object objects
     *
     * @param mapName name of map
     * @return map
     */
    public Map<String, Object> getMapObject(String mapName) throws Exception {
        Map<byte[], byte[]> byteMap = cluster.hGetAll(getBytes(mapName));
        if (byteMap == null) {
            return null;
        }
        Map<String, Object> objectMap = new HashMap<String, Object>();

        Set<Map.Entry<byte[], byte[]>> entrySet = byteMap.entrySet();
        for (Map.Entry<byte[], byte[]> entry : entrySet) {
            try {
                objectMap.put(new String(entry.getKey(), ENCODEING), SerializeUtils.unserialize(entry.getValue()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return objectMap;
    }

    /**
     * Setting the map property value, which map key and value are Object objects
     *
     * @param mapName name of map
     * @param field   field's name
     * @param object  an object set to cache cluster
     */
    public void setFieldObject(String mapName, String field, Object object) {
        byte[] bytes = cluster.hGet(this.getBytes(mapName), this.getBytes(field));
        if (bytes != null) {
            cluster.hSet(this.getBytes(mapName), this.getBytes(field), SerializeUtils.serialize(object));
        }
    }

    /**
     * Gets the value of the Map attribute in the map, key and value are Object objects
     *
     * @param mapName name of map
     * @param field   field's name
     */
    public Object getFieldObject(String mapName, String field) throws Exception {
        byte[] bytes = cluster.hGet(this.getBytes(mapName), this.getBytes(field));
        if (bytes != null) {
            return SerializeUtils.unserialize(bytes);
        }
        return null;
    }

    /**
     * Value of the Map attribute in the map deletion, key and value are Object objects
     *
     * @param mapName name of map
     * @param field   field's name
     * @return 1 is success ,0 is no field deleted
     */
    public Long delFieldObject(String mapName, String field) {
        return cluster.hDel(this.getBytes(mapName), this.getBytes(field));
    }

    /**
     * The value of this attribute the existence of query in map
     *
     * @param mapName name of map
     * @param field   field's name
     * @return 是否存在
     */
    public boolean exsistsFieldObject(String mapName, String field) {
        return cluster.hExists(getBytes(mapName), getBytes(field));
    }

    /**
     * Gets an array of String byte[]
     *
     * @param string
     * @return
     */
    private byte[] getBytes(String string) {
        try {
            if (string != null) {
                return string.getBytes(ENCODEING);
            }
        } catch (UnsupportedEncodingException e) {
        }
        return null;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getFlag() {
        return flag;
    }
}

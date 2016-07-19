package org.seckill.util;

/**
 * Created by Administrator on 2016/7/17.
 */
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;

import org.seckill.dao.cache.RedisDao;
import org.springframework.beans.factory.annotation.Autowired;

public class RedisUtil {

//    public static JedisPool pool = null;

    @Autowired
	private RedisDao redisDao;
	
//    static ThreadLocal<Jedis> tr = new ThreadLocal<Jedis>();
//    public static Jedis getResource(){
//        Jedis jedis;
//        long start;
//        if(Constants.DEBUG){
//            start = System.currentTimeMillis();
//        }
//        if(Constants.THREADLOCAL_MODEL){
//            jedis = tr.get();
//            if(jedis == null){
//                jedis = pool.getResource();
//                tr.set(jedis);
//            }
//        }else {
//            jedis = pool.getResource();
//        }
//        if(Constants.DEBUG){
//            long end = System.currentTimeMillis();
//            Constants.borrowCount.incrementAndGet();
//            Constants.borrowWait.addAndGet(end-start);
//        }
//        return jedis;
//    }

//    private static JedisPool getPool() {
//        if (pool == null) {
//            JedisPoolConfig config = new JedisPoolConfig();
//            config.setMaxIdle(Constants.MAX_REDIS);
//            config.setMinIdle(Constants.MAX_REDIS);
////            config.setTestOnBorrow(true);
//            config.setMaxTotal(Constants.MAX_REDIS);
//            config.setFairness(true);
//            config.setBlockWhenExhausted(true);
//
//            String redis_host = "127.0.0.1";
//            int redis_port = 6379;
//            pool = new JedisPool(config, redis_host, redis_port);
//        }
//        if(!Constants.THREADLOCAL_MODEL){
//            Jedis[] array = new Jedis[Constants.MAX_REDIS];
//            for(int i=0; i< Constants.MAX_REDIS; i++){
//                array[i] = pool.getResource();
//            }
//            for (int i = 0; i < Constants.MAX_REDIS; i++) {
//                close(array[i]);
//            }
//        }
//        return pool;
//    }
//    public static void close(AutoCloseable... list){
//
//      for(AutoCloseable closeable: list){
//            if(Constants.THREADLOCAL_MODEL && closeable instanceof Jedis){
//                return;
//            }
//            try {
//                if(closeable != null){
//                    closeable.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//
//    }

    public static void main(String[] args) {
//        Jedis redis = RedisUtil.getResource();
//        redis.set("yooo", "hehehe");
//        System.out.println(redis.get("yooo"));
    }
}

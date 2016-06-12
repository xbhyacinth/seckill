package org.seckill.dao.cache;

import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dyuproject.protostuff.runtime.RuntimeSchema;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisDao {
	private final JedisPool jedisPool;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);
	public RedisDao(String ip, int port){
		jedisPool = new JedisPool(ip,port);
	}
	
	public Seckill getSeckill(long seckillId) {
		//redis操作逻辑
		try{
			Jedis jedis = jedisPool.getResource();
			try{
				String key = "seckill: " + seckillId;
				//并没有实现内部序列化操作，自定义序列化
				jedis
			}finally{
				jedis.close();
			}
		}catch(Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}

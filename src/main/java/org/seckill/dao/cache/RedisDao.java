package org.seckill.dao.cache;

import org.seckill.entity.Seckill;
import org.seckill.service.RedisService.SeckillRedisService;
import org.seckill.util.RedisUtil;
import org.seckill.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedisDao {
	private final JedisPool jedisPool;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);
	public RedisDao(String ip, int port){
		jedisPool = new JedisPool(ip,port);
	}

	/**
	 * 获取Seckill缓存
	 * @param seckillId
	 * @return
	 */
	public Seckill getSeckill(long seckillId){
		//redis操作逻辑
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "seckill:" + seckillId;
				//并没有实现内部序列化操作
				// get->byte[] ->反序列化 ->Object(Seckill)
				//采用自定义序列化
				//protostuff : pojo
				byte[] bytes = jedis.get(key.getBytes());
				//缓存重新获取
				if (null != bytes) {
					//空对象
					Seckill seckill = schema.newMessage();
					ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
					//seckill被反序列化
					return seckill;
				}
			} finally {
				jedis.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	//批量查找商品 批量查询商品，并且库存真实
	public List<Seckill> getRellSeckill(){
		Jedis jedis = null;
		try {
			jedis = RedisUtil.getResource();
			SeckillRedisService seckillRedisService = new SeckillRedisService();
			Set<Integer> ids = seckillRedisService.allIds(jedis);
			List<Integer> itemIds = new ArrayList(ids);
			int len = itemIds.size();
			String[]  keys = new String[len];

			int i = 0;
			for(Integer itemId : itemIds){
				keys[i++] = "seckill:" +itemId;
			}
			Map<Integer,Integer> stocklist = seckillRedisService. getStockAll(itemIds, jedis);
//			List<byte[]>  items = jedis.mget(keys);
            return  null;
		}
		finally {
			Utils.close(jedis);
		}
	}

	/**
	 * 缓存Seckill对象
	 * @param seckill
	 * @return
	 */
	public String putSeckill(Seckill seckill){
		//set Object[Seckill] -> 序列化 -> byte[]
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "seckill:" + seckill.getSeckillId();
				byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema,
						LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));

				//timeout cache, here set one hour
				int timeout = 60 * 60;
				String result = jedis.setex(key.getBytes(), timeout, bytes);
				return result;
			}finally {
				jedis.close();
			}
		}catch (Exception e){
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 获取token
	 * @param seckill
	 * @return
	 */
	public String getToken(long seckillId){
		//set Object[Seckill] -> 序列化 -> byte[]
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "token:" + seckillId;
				return jedis.rpop(key);
			}finally {
				jedis.close();
			}
		}catch (Exception e){
			logger.error(e.getMessage(), e);
		}
		return null;
	}
}

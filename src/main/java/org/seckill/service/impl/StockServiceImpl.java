package org.seckill.service.impl;

import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dao.cache.RedisDao;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.service.SeckillService;
import org.seckill.service.StockService;
import org.seckill.util.StockCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import com.dyuproject.protostuff.ProtostuffIOUtil;

import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by cdliubei@jd.com on 2016/7/17.
 */
@Service
public class StockServiceImpl implements StockService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    
    @Autowired
    private SeckillDao seckillDao;
    
    @Autowired
    private SuccessKilledDao successKilledDao;

    @Autowired
    private RedisDao redisDao;

    public long initRedisStock(long seckillId) {
        initRedis(seckillId);
        Seckill seckill = seckillDao.queryById(seckillId);
        StockCache.stockCache = seckillDao.queryAll(0, 12);
        Set<String> tokenSets = createToken(seckillId,seckill.getNumber());
        for(String token : tokenSets){
            redisDao.pushToken(seckillId,token);
        }
        return tokenSets.size();
    }


    private void initRedis(long seckillId) {
    	try {
			Jedis jedis = redisDao.getJedisPool().getResource();
			try {
				jedis.del("token:" + seckillId);  //清空token队列
				jedis.del("tokenSet:" + seckillId); //清空tokenSet
				String stockRef = "Seckill:STOCK:" + seckillId;
				jedis.del(stockRef); //清空库存
				Seckill seckill = seckillDao.queryById(seckillId);
				jedis.set(stockRef, String.valueOf(seckill.getNumber()));//设置库存
			} finally {
				jedis.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}


	public Set<String> createToken(long seckillId,int stockNum){
        Set<String> sets = new HashSet<String>();
        int i = stockNum*15;
        String slat = "creatToken";
        for(;i>0;i--){
            String uuid = UUID.randomUUID().toString();
            String base = uuid + slat;
            String token = DigestUtils.md5DigestAsHex(base.getBytes());
            sets.add(token);
        }
        return sets;
    }


	public long saveToMySql(Long seckillId) {
		try {
			Jedis jedis = redisDao.getJedisPool().getResource();
			try {
				Set<String> orders = jedis.keys("SECKILL:ORDER:*:" +seckillId);
				List<SuccessKilled> successOrders = new ArrayList<SuccessKilled>(orders.size());
				for(String order : orders) {
					String[] s = order.split(":");
					SuccessKilled successKilled = new SuccessKilled();
					successKilled.setSeckillId(seckillId);
					successKilled.setUserPhone(Long.valueOf(s[2]));
					successKilled.setState((short)0);
					successOrders.add(successKilled);
					jedis.del(order);
				}
				int count = successKilledDao.batchInsert(successOrders);
				String numString = jedis.get("Seckill:STOCK:"+seckillId);
				if(!StringUtils.isEmpty(numString)) {
					Seckill seckill = new Seckill();
					seckill.setNumber(Integer.valueOf(numString));
					seckill.setSeckillId(seckillId);
					seckillDao.updateBySecKill(seckill);
				}
				StockCache.stockCache = seckillDao.queryAll(0, 12);
				return count;
			} finally {
				jedis.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return 0;
	}

}

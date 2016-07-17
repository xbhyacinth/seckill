package org.seckill.service.impl;

import org.seckill.dao.cache.RedisDao;
import org.seckill.entity.Seckill;
import org.seckill.service.SeckillService;
import org.seckill.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by cdliubei@jd.com on 2016/7/17.
 */
@Service
public class StockServiceImpl implements StockService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Autowired
    private RedisDao redisDao;

    public long initRedisStock(long seckillId) {
        initRedis(seckillId);
        Seckill seckill = seckillService.getById(seckillId);
        Set<String> tokenSets = createToken(seckillId,seckill.getNumber());
        for(String token : tokenSets){
            redisDao.pushToken(seckillId,token);
        }
        return tokenSets.size();
    }


    private void initRedis(long seckillId) {
    	redisDao.del("token:" + seckillId);  //清空token队列
    	redisDao.del("tokenSet:" + seckillId); //清空tokenSet
//    	String stockRef = "Seckill:STOCK:" + seckillId;
//    	redisDao.del(stockRef); //清空库存
//    	Seckill seckill = seckillService.getById(seckillId);
    
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

}

package org.seckill.service.RedisService;

import org.seckill.dao.cache.RedisDao;
import org.seckill.util.RedisUtil;
import org.seckill.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * Created by Administrator on 2016/7/17.
 */
@Service
public class OrderRedisService {
    public static final long success = 1024;
    public static final long failed = 403;
    public static final long ORDER_EXIST = 401;
    public static final String ORDER_PREFIX = "SECKILL:ORDER:";
    public static final String STOCK_PREFIX = "Seckill:STOCK:";

    @Autowired
    private RedisDao redisDao;

    public long getcartAndMakeOrder(Integer userId, Integer itemId, Jedis jedis, String md5) {
    	if(redisDao.verifyToken(md5,itemId)) {
    		List<String> stockKeyList = new ArrayList<String>();
            stockKeyList.add(STOCK_PREFIX + itemId.toString());
            List<String> orderKeyList = new ArrayList<String>();
            orderKeyList.add(ORDER_PREFIX + userId + ":" + itemId.toString());
            return (Long) jedis.eval(cmd, stockKeyList, orderKeyList);
    	}
        return 403L;
    }

    final String cmd = "" +
            "if redis.call(\"get\", ARGV[1]) then return " + ORDER_EXIST + " end " +
            "local a=0 local stock={} " +
            "for j = 1,#KEYS " +
            "do stock[j] = redis.call('decrby', KEYS[j],1) " +
            "if(tonumber(stock[j]) < 0) then " +
            "a=1 " +
            "end " +
            "end " +
            "if a==1 then " +
            "for j=1,#KEYS do redis.call('incrby', KEYS[j],1 ) end " +
            "return " + failed +
            " end " +
            "redis.call('set', ARGV[1], 1) " +
            "return " + success +
            "";

    public static void main(String[] args) {
               Jedis jedis = null;
//        try {
//            jedis = RedisUtil.getResource();
//            OrderRedisService orderService = new OrderRedisService();
//            long i1= orderService.getcartAndMakeOrder(3,1111,jedis );
//            long i2 = orderService.getcartAndMakeOrder(3,2222,jedis);
//            long i3 = orderService.getcartAndMakeOrder(3,2222,jedis);
//
//            System.out.println(i1);
//            System.out.println(i2);
//            System.out.println(i3);
//        } finally {
//            Utils.close(jedis);
//        }

    }

	public Jedis getJedis() {
		return redisDao.getJedisPool().getResource();
	}
}


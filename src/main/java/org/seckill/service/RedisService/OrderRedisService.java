package org.seckill.service.RedisService;

import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * Created by Administrator on 2016/7/17.
 */
public class OrderRedisService {
    public static final long success= 1024;
    public static final long failed = 403;
    public static final long ORDER_EXIST = 401;
    public static final String ORDER_PREFIX = "SECKILL:ORDER:";
    public static final String STOCK_PREFIX = "Seckill:STOCK:";

    final String getcartAndMakeOrder = "local itemlist = redis.call('get', \"YUAN:CART:\"..KEYS[1])\n" +
            "local carttable = {}\n" +
            "if itemlist then \n" +
            "\tcarttable = cjson.decode(itemlist)\n" +
            "end\n" +
            "if redis.call(\"get\", \"YUAN:ORDER:\"..KEYS[1]) then return 401 end                                                                      \n" +
            "local a=0 local stock={}\n" +
            "for j = 1,#carttable \n" +
            "    do stock[j] = redis.call('decrby', \"YUAN:STOCK:\"..carttable[j][\"food_id\"], carttable[j][\"count\"])\n" +
            "    if(tonumber(stock[j]) < 0) then\n" +
            "        a=1 \n" +
            "    end \n" +
            "end \n" +
            "if a==1 then \n" +
            "    for j=2,#KEYS do redis.call('incrby', \"YUAN:STOCK:\"..carttable[j][\"food_id\"], carttable[j][\"count\"]) end \n" +
            "    return 403 \n" +
            "end \n" +
            "redis.call('set', \"YUAN:ORDER:\"..KEYS[1], itemlist)\n" +
            "return 1024";

    public long getcartAndMakeOrder(Integer userId,Integer itemId, Jedis jedis){
        List<String> stockKeyList =new ArrayList<String>();
        stockKeyList.add(STOCK_PREFIX +itemId.toString());
        List<String> orderKeyList=new ArrayList<String>();
        orderKeyList.add(ORDER_PREFIX +userId+":"+itemId.toString());
        return(Long)jedis.eval(cmd,stockKeyList, orderKeyList);
    }
    final String cmd = ""+
            "if redis.call(\"get\", ARGV[1]) then return "+ORDER_EXIST + " end " +
            "local a=0 local stock={} " +
            "for j = 2,#KEYS " +
            "do stock[j] = redis.call('decrby', KEYS[j],1) " +
            "if(tonumber(stock[j]) < 0) then " +
            "a=1 " +
            "end " +
            "end " +
            "if a==1 then " +
            "for j=2,#KEYS do redis.call('incrby', KEYS[j],1 ) end "+
            "return "+failed+
            " end " +
            "redis.call('set', ARGV[1], 1) " +
            "return "+success+
            "";


}

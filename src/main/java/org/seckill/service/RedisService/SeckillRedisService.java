package org.seckill.service.RedisService;

import org.seckill.util.RedisUtil;
import org.seckill.util.Utils;
import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * Created by Administrator on 2016/
 */
public class SeckillRedisService {
    public static final long success= 1024;
    public static final long failed = 403;
    public static final long ORDER_EXIST = 401;

    public static final String STOCK_PREFIX = "Seckill:STOCK:";

    public int  getStock(Integer itemId,Jedis jedis){
    final int stock = Integer.parseInt(jedis.get(STOCK_PREFIX + itemId));
    return  stock;
    }
    public boolean subStock(int itemId, int stock,Jedis jedis){
        return (jedis.decrBy(STOCK_PREFIX + itemId, stock) >= 0);
    }

    public boolean containsKey(Integer itemId,Jedis jedis){
        boolean contain = jedis.exists(STOCK_PREFIX  + itemId);
        return  contain;
    }
    public Set<Integer> allIds(Jedis jedis) {

        Set<Integer> ids = new TreeSet<Integer>();
        Set<String> keys = jedis.keys("Seckill:STOCK:*");
        for(String key:keys){
            if(key.startsWith(STOCK_PREFIX )){
                int begin = key.lastIndexOf(":") + 1;
                int id = Integer.parseInt(key.substring(begin));
                ids.add(id);
            }
        }
        return ids;
    }

    public  Map<Integer,Integer> getAllList(Jedis jedis){
        List<Integer> itemIds = new ArrayList(allIds(jedis));

       return   getStockAll(itemIds,jedis);
    }

    public boolean addStock(int itemId, int stock,Jedis jedis){
        return (jedis.incrBy(STOCK_PREFIX + itemId, stock) >= 0);
    }

    public Map<Integer,Integer> getStockAll( List<Integer> itemIds,Jedis jedis){
        int len = itemIds.size();
        String[]  ids = new String[len];

        int i = 0;
        for(Integer foodId : itemIds){
            ids[i++] = STOCK_PREFIX +foodId;
        }

        List<String> stocks = jedis.mget(ids);
        Map<Integer,Integer> res = new HashMap<Integer,Integer>();
        for(int j = 0;j < len ;j++){
            res.put(itemIds.get(j),Integer.parseInt(stocks.get(j)));
        }
        return res;
    }

    public String setStock(int itemId, int stock,Jedis jedis){
        return jedis.set(STOCK_PREFIX + itemId, stock+"");
    }
    public static void main(String[] args) {
        Jedis jedis= null;
//        try{
//            jedis = RedisUtil.getResource();
//            SeckillRedisService stockService = new SeckillRedisService();
//
//            stockService.setStock(2222, 1000,jedis);
//            stockService.setStock(1111, 0,jedis);
//            final int yoo = stockService.getStock(2222,jedis);
//
//            List<Integer> foodIds = new ArrayList<Integer>();
//            List<Integer> counts = new ArrayList<Integer>();
//            foodIds.add(2222);
//            foodIds.add(1111);
//
//            Map<Integer,Integer> map = stockService.getStockAll(foodIds, jedis);
//            for(Integer i:map.keySet()){
//                System.out.println("key:"+i);
//                System.out.println("value:"+map.get(i));
//            }
//            System.out.println(map);
//        }finally {
//            Utils.close(jedis);
//        }

    }

}

package org.seckill.dao.cache;

import org.seckill.constant.CacheConstant;
import org.seckill.entity.Seckill;
import org.seckill.util.TKillCacheUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: tang
 * Date: 16-7-20
 * Time: 下午6:08
 * To change this template use File | Settings | File Templates.
 */
public class KillCacheDao {

    @Resource
    private TKillCacheUtils tKillCacheUtils;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    public Seckill getSec(long secId){
        String key= CacheConstant.CacheKey.getKillKey(secId);
        Seckill seckill=new Seckill();
        try {
            seckill=(Seckill)tKillCacheUtils.get(key);

        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
        return seckill;
    }

    /**
     * 缓存Seckill对象
     * @param seckill
     * @return
     */
    public void setKill(Seckill seckill){

            try {
                String key = CacheConstant.CacheKey.getKillKey(seckill.getSeckillId());
                tKillCacheUtils.set(key,CacheConstant.Expiration.TEN_MINUTES, seckill);

        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 获取token
     * @param seckillId
     * @return
     */
    public void getToken(long seckillId){
        //set Object[Seckill] -> 序列化 -> byte[]
            try {
                String key = "token:" + seckillId;
                 tKillCacheUtils.delete(key);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 将token push进redis队列
     */
    public void pushToken(long seckillId,String token){
            try {
                String key = "token:" + seckillId;
                tKillCacheUtils.set(key,CacheConstant.Expiration.TEN_MINUTES, token);

        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 清空key
     */
    public void del(String key){
            try {
                tKillCacheUtils.delete(key);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }



    public void setTokenSet(long seckillId,String token) {
            try {
                String key = "tokenSet:" + seckillId;
                tKillCacheUtils.set(key,CacheConstant.Expiration.TEN_MINUTES,token);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }

    public boolean verifyToken(String md5, long seckillId) {

            try {
                String key = "tokenSet:" + seckillId;
                return tKillCacheUtils.isExist(key);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
        return false;
    }

}

package org.seckill.constant;

/**
 * Created with IntelliJ IDEA.
 * User: tang
 * Date: 16-7-20
 * Time: 下午6:16
 * To change this template use File | Settings | File Templates.
 */
public class CacheConstant {

    /**
     * Expiration of cache
     */
    public static class Expiration {

        /**
         * Expiration of prototype
         */
        public static final Integer PROTOTYPE_EXPIRATION = 2592000;

        /**
         * three days
         */
        public static final Integer THREE_DAY = 259200;

        /**
         * half a day
         */
        public static final Integer HALF_A_DAY = 43200;

        /**
         * one day
         */
        public static final Integer ONE_DAY = 86400;

        /**
         * a hour
         */
        public static final Integer ONE_HOUR = 3600;

        /**
         * five minutes
         */
        public static final Integer COMMON_EXPIRATION = 300;

        /**
         * thirty minutes
         */
        public static final Integer HALF_HOUR = 1800;

        /**
         * five minutes
         */
        public static final Integer FIVE_MINUTES = 300;

        /**
         * ten minutes
         */
        public static final Integer TEN_MINUTES = 600;

        /**
         * 代理登陆过期时间
         */
        public static final int PROXY_LOGIN_KEY_EXPIRATION = 120;

        /**
         * 验证码过期时间1分钟
         */
        public static final int ONE_MINUTE = 60;

        /**
         *  three hour
         */
        public static final Integer THREE_HOUR = 10800;
    }

    /**
     * Cache key
     */
    public static class CacheKey {

        public static String getTokenKey(long seckillId){
            return "token:"+String.valueOf(seckillId);
        }
        public static String getTokenSetKey(long seckillId){
            return "tokenSet:"+String.valueOf(seckillId);
        }
        public static String getKillKey(long seckillId){
            return "SecKill"+String.valueOf(seckillId);
        }
        public static String getStockKey(long seckillId){
            return "Seckill:STOCK:"+String.valueOf(seckillId);
        }
    }
}

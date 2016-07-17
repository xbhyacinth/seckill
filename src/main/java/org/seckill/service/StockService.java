package org.seckill.service;

/**
 * Created by cdliubei@jd.com on 2016/7/17.
 */
public interface StockService {

    long initRedisStock(long seckillId);

	long saveToMySql(Long seckillId);
}

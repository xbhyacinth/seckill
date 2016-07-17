package org.seckill.dao;

import java.util.*;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

public interface SeckillDao {

	/**
	 * 减库存
	 * @param seckillId
	 * @param killTime
	 * @return
	 */
	int reduceNumber(@Param("seckillId")long seckillId, @Param("killTime")Date killTime);
	
	/**
	 * 根据id查询秒杀对象
	 * @param seckillId
	 * @return
	 */
	Seckill queryById(long seckillId);
	
	/**
	 * 根据偏移量查询秒杀商品列表
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<Seckill> queryAll(@Param("offset")int offset, @Param("limit")int limit);

	/**
	 * 使用存储过程执行秒杀
	 * @param paramMap
	 */
	void executeSeckillByProcedure(Map<String, Object> paramMap);

    /**
     * 添加对象通过Seckill
     * @param seckill
     * @return
     */
    void addBySeckill(Seckill seckill);

    /**
     * 修改Seckill
     * @param seckill
     * @return
     */
    void updateBySecKill(Seckill seckill);
}

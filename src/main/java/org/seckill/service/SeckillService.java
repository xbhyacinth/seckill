package org.seckill.service;

import java.util.List;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

/**
 * 业务接口：站在使用者的角度设计接口
 * 三个方面：方法定义粒度、参数、返回类型友好
 * @author baironglin
 *
 */

public interface SeckillService {

	/**
	 * 查询所有秒杀记录
	 * @return
	 */
	List<Seckill> getSeckillList();
	
	/**
	 * 按Id查询秒杀记录
	 * @param seckillId
	 * @return
	 */
	Seckill getById(long seckillId);
	
	/**
	 * 秒杀开启时输出接口地址，否则输出系统时间和秒杀开始时间
	 */
	Exposer exportSeckillUrl(long seckillId);
	
	/**
	 * 执行秒杀
	 * @param id
	 * @param userPhone
	 * @param md5
	 */
	SeckillExecution executeSeckill(long id, long userPhone, String md5) throws SeckillException,RepeatKillException,SeckillCloseException;
	
	/**
	 * 执行秒杀操作通过存储过程
	 * @param seckillId
	 * @param userPhone
	 * @param md5
	 * @return
	 * @throws SeckillException
	 * @throws RepeatKillException
	 * @throws SeckillCloseException
	 */
    SeckillExecution executeSeckillByProcedure(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException;
}

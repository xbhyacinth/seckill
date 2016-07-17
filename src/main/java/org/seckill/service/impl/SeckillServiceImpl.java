package org.seckill.service.impl;

import org.apache.commons.collections.MapUtils;
import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dao.cache.RedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SeckillServiceImpl implements SeckillService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SeckillDao seckillDao;

	@Autowired
	private SuccessKilledDao successKilledDao;

	@Autowired
	private RedisDao redisDao;

	//md5盐值字符串，混淆
	private final String slat = "djfjhueuweur832hsiudy87e81@&^&#";

	public List<Seckill> getSeckillList() {
		return seckillDao.queryAll(0, 4);
	}

	public Seckill getById(long seckillId) {
		return seckillDao.queryById(seckillId);
	}

	public Exposer exportSeckillUrl(long seckillId) {
//		Seckill seckill = seckillDao.queryById(seckillId);
//		if(seckill == null) {
//			return new Exposer(false,seckillId);
//		}
		//1、访问redis
		Seckill seckill = redisDao.getSeckill(seckillId);
		if(null == seckill){
			//2、访问数据库
			seckill = getById(seckillId);
			if(null == seckill){
				//当前无秒杀库存商品
				return new Exposer(false, seckillId);
			}else{
				//3、放入redis
				redisDao.putSeckill(seckill);
			}
		}

		Date startTime = seckill.getStartTime();
		Date endTime  = seckill.getEndTime();
		Date nowTime = new Date();
		if(nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
			return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
		}
//		String md5 = getMD5(seckillId);
		String md5 = redisDao.getToken(seckillId);    //****************************
		redisDao.setTokenSet(md5,seckillId);
		return new Exposer(true,md5,seckillId);
	}

	private String getMD5(long seckillId) {
		String base = seckillId + '/' + slat;
		String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
		return md5;
	}

	@Transactional
	/**
	 * 使用注解控制事务的优点：
	 * 1：开发团队达成一致约定，明确标注事务方法的编程风格
	 * 2：保证事务方法的执行时间尽可能短，不要穿插其他网络操作RPC/HTTP请求，或者剥离到事务外部方法
	 * 3：不是所有的方法都需要事务，一条修改或者只读
	 */
	public SeckillExecution executeSeckill(long id, long userPhone, String md5)
			throws SeckillException, RepeatKillException, SeckillCloseException {
//		if(md5 == null || !md5.equals(getMD5(id))) {
		if(null == md5 || !redisDao.verifyToken(md5,id)){   //**************************
			throw new SeckillException("seckill data rewrite!");
		}
		Date now = new Date();
		try{
			//减库存，插入记录
			int updateCount = seckillDao.reduceNumber(id, now);
			if(updateCount <= 0) {
				//没有更新到记录
				throw new SeckillCloseException("seckill is closed!");
			} else {
				//记录购买行为
				int insertCount = successKilledDao.insertSuccessKilled(id, userPhone);
				if(insertCount <= 0) {
					throw new RepeatKillException("repeat seckill!");
				} else {
					SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(id,userPhone);
					return new SeckillExecution(id,SeckillStateEnum.SUCCESS,successKilled);
				}
			}
		} catch(SeckillCloseException e1) {
			throw e1;
		} catch(RepeatKillException e2) {
			throw e2;
		} catch(Exception e) {
			logger.error(e.getMessage(),e);
			//所有编译器异常转换成运行时异常
			throw new SeckillException("seckill inner error: " + e.getMessage());
		}
	}

	/**
	 *
	 */
	public SeckillExecution executeSeckillByProcedure(long seckillId,
													  long userPhone, String md5) throws SeckillException,
			RepeatKillException, SeckillCloseException {
//		if(null == md5 || !md5.equals(getMD5(seckillId))){
		if(null == md5 || !redisDao.verifyToken(md5,seckillId)){  //*********************************
            //数据被篡改了
            return new SeckillExecution(seckillId, SeckillStateEnum.DATA_REWRITE);
        }

        Date killTime = new Date();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("seckillId", seckillId);
        paramMap.put("phone", userPhone);
        paramMap.put("killTime", killTime);
        paramMap.put("result", null);

        //执行完存储过程之后，result被赋值
        try {
            seckillDao.executeSeckillByProcedure(paramMap);
            //获取result值
            int result = MapUtils.getInteger(paramMap, "result", -2);
            if(result == 1){
                SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS,successKilled);
            }else{
                return new SeckillExecution(seckillId, SeckillStateEnum.stateOf(result));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
        }
	}

}

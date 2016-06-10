package org.seckill.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml",
					   "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SeckillService seckillService;
	@Test
	public void testGetSeckillList() {
		List<Seckill> list = seckillService.getSeckillList();
		logger.info("list={}",list);
	}

	@Test
	public void testGetById() {
		Seckill seckill = seckillService.getById(1000L);
		logger.info("seckill={}", seckill);
	}
	
    /**
     * 集成测试：秒杀完整流程，可重复执行
     */
    @Test
    public void testSeckillLogic() {

        long id = 1001;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        logger.info("exposer={}",exposer);
        if (exposer.isExposed()) {

            long phone = 13141397575L;
            String md5 = exposer.getMd5();

            try {
                SeckillExecution seckillExecution = seckillService.executeSeckill(id, phone, md5);
                logger.info("result={}",seckillExecution);
            } catch (RepeatKillException e) {
            	logger.error(e.getMessage());
            } catch (SeckillCloseException e) {
            	logger.error(e.getMessage());
            }

        } else {
        	logger.warn("秒杀未开始：{}",exposer.toString());
        }

    }

	@Test
	public void testExportSeckillUrl() {
		long id = 1000;
		Exposer exposer = seckillService.exportSeckillUrl(id);
		logger.info("exposer={}",exposer); //[exposed=true, md5=cf49de03ce15dbfab929bcd61857222c, seckillId=1000, now=0, start=0, end=0]
	}

	@Test
	public void testExecuteSeckill() {
		long id = 1000L;
		long phone = 13141397575L;
		String md5 = "cf49de03ce15dbfab929bcd61857222c";
		try{
			SeckillExecution seckillExecution = seckillService.executeSeckill(id, phone, md5);
			logger.info("seckillExcution={}",seckillExecution);
		} catch(RepeatKillException e1) {
			logger.error(e1.getMessage());
		} catch(SeckillCloseException e) {
			logger.error(e.getMessage());
		}
	
	}

}

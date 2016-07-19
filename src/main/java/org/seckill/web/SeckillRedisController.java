package org.seckill.web;

import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.service.RedisService.OrderRedisService;
import org.seckill.util.RedisUtil;
import org.seckill.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import redis.clients.jedis.Jedis;

/**
 * Created by cdliubei@jd.com on 2016/7/17.
 */
@Controller
@RequestMapping(value="/seckill01")
public class SeckillRedisController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private OrderRedisService orderRedisService;
    
    @RequestMapping(value = "/{seckillId}/{md5}/execution", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
                                                   @PathVariable("md5") String md5,
                                                   @CookieValue(value = "killPhone", required = false) Long phone) {
        if (seckillId == null || md5 == null) {
            return new SeckillResult<SeckillExecution>(false, "执行秒杀信息不完整，缺少编号或者url");
        }
        if (phone == null) {
            return new SeckillResult<SeckillExecution>(false, "未注册");

        }
        Jedis jedis = null;
        try {
            jedis = orderRedisService.getJedis();
            long i1= orderRedisService.getcartAndMakeOrder(phone.intValue(),seckillId.intValue(),jedis, md5);
            //秒杀成功
            if (i1==1024){
                return new SeckillResult<SeckillExecution>(true, new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS));
            }
            //重复秒杀
            if (i1 ==401 ){
                return new SeckillResult<SeckillExecution>(true, new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL));
            }
            //秒杀结束
            if(i1 == 403){
                return  new SeckillResult<SeckillExecution>(true,new SeckillExecution(seckillId,SeckillStateEnum.END));
            }

            return  new SeckillResult<SeckillExecution>(true,new SeckillExecution(seckillId,SeckillStateEnum.INNER_ERROR));
        } finally {
            jedis.close();
        }

    }
}


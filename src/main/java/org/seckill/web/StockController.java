package org.seckill.web;

import org.seckill.service.SeckillService;
import org.seckill.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by cdliubei@jd.com on 2016/7/17.
 */
@Controller
@RequestMapping(value="/stock")
public class StockController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;
    @Autowired
    private StockService stockService;

    @RequestMapping(value="/init/{seckillId}",method= RequestMethod.GET)
    @ResponseBody
    public String list(Model model,@PathVariable("seckillId")Long seckillId) {
        long size = stockService.initRedisStock(seckillId);
        return "init "+size+" token finish!";
    }
}

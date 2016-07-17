package org.seckill.web;

import java.util.Date;
import java.util.List;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value="/seckill")
public class SeckillController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SeckillService seckillService;
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String list(Model model) {
		List<Seckill> list = seckillService.getSeckillList();
		model.addAttribute("list", list);
		return "list"; //WEB-INF/jsp/list.jsp
	}
	
	@RequestMapping(value="/{seckillId}/detail",method=RequestMethod.GET)
	public String detail(@PathVariable("seckillId")Long seckillId, Model model) {
		if(seckillId == null) {
			return "redirect:/seckill/list";
		}
		Seckill seckill = seckillService.getById(seckillId);
		if(seckill == null) {
			return "forward:/seckill/list";
		}
		model.addAttribute("seckill",seckill);
		return "detail";
	}
	
	@RequestMapping(value="/{seckillId}/exposer",method=RequestMethod.POST,produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public SeckillResult<Exposer> exposer(@PathVariable("seckillId")Long seckillId) {
		 SeckillResult<Exposer> result = null;
		 if(seckillId == null) {
			 return new SeckillResult<Exposer>(false,"请输入商品编号");
		 }
		 try{
			 Exposer exposer = seckillService.exportSeckillUrl(seckillId);
			 result = new SeckillResult<Exposer>(true,exposer);
		 }catch(Exception e) {
			 logger.error(e.getMessage(),e);
			 result = new SeckillResult<Exposer>(false,e.getMessage());
		 }
		 
		return result;
	}
	
	@RequestMapping(value="/{seckillId}/{md5}/execution",method=RequestMethod.POST,produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId")Long seckillId, 
												   @PathVariable("md5")String md5,
												   @CookieValue(value="killPhone",required=false)Long phone) {
		if(seckillId ==null || md5 == null) {
			return new SeckillResult<SeckillExecution>(false,"执行秒杀信息不完整，缺少编号或者url");
		}
		if(phone == null) {
			return new SeckillResult<SeckillExecution>(false,"未注册");

		}
		try{
			//利用spring
			SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId, phone, md5);
			return new SeckillResult<SeckillExecution>(true, seckillExecution);
		} catch(RepeatKillException e) {
			SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
			return new SeckillResult<SeckillExecution>(true, seckillExecution);
		} catch(SeckillCloseException e) {
			SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.END);
			return new SeckillResult<SeckillExecution>(true, seckillExecution);
		} catch(Exception e) {
			logger.error(e.getMessage(),e);
			SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
			return new SeckillResult<SeckillExecution>(true, seckillExecution);
		}
	}
	
	@RequestMapping(value = "time/now",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Long> execute() {
        return new SeckillResult<Long>(true,new Date().getTime());
    }


    @RequestMapping(value="/addKillSku")
    @ResponseBody
    public void addKillSku(@ModelAttribute Seckill seckill) {
        SeckillResult result=new SeckillResult();
        try{
            seckillService.addSeckill(seckill);
            result.setSuccess(true);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            result.setError(e.getMessage());
            result.setSuccess(false);
        }
    }

    @RequestMapping(value="/updateKillSku")
    @ResponseBody
    public void updateKillSku(@ModelAttribute Seckill seckill) {
        SeckillResult result=new SeckillResult();
        try{
            seckillService.updateSeckill(seckill);
            result.setSuccess(true);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            result.setError(e.getMessage());
            result.setSuccess(false);
        }

    }
}

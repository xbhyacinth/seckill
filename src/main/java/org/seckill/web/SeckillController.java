package org.seckill.web;

import java.text.SimpleDateFormat;
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

    @RequestMapping(value="/manage",method=RequestMethod.GET)
    public String manage(Model model) {
        List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute("manage", list);
        return "manage"; //WEB-INF/jsp/list.jsp
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
			//利用spring控制事务
//			SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId, phone, md5);
			//利用mysql存储过程
			SeckillExecution seckillExecution = seckillService.executeSeckillByProcedure(seckillId, phone, md5);
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


    @RequestMapping(value="/{name}/{number}/{startTime}/{endTime}/addKillSku",method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult addKillSku(@PathVariable("name") String name,@PathVariable("number") Integer number,@PathVariable("startTime") String startTime,@PathVariable("endTime") String endTime) {
        SeckillResult result=new SeckillResult();
        Seckill seckill=new Seckill();
        SimpleDateFormat  sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        startTime=startTime.replace("a","-");
        startTime=startTime.replace("b"," ");
        startTime=startTime.replace("c",":");
        endTime=endTime.replace("a","-");
        endTime=endTime.replace("b"," ");
        endTime=endTime.replace("c",":");
        try{
            Date st=sdf.parse(startTime);
            Date et=sdf.parse(endTime);
            seckill.setName(name);
            seckill.setNumber(number);
            seckill.setStartTime(st);
            seckill.setEndTime(et);
            seckill.setCreateTime(new Date());
            seckillService.addSeckill(seckill);
            result.setSuccess(true);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            result.setError(e.getMessage());
            result.setSuccess(false);
        }
        return result;
    }

    //    @RequestMapping(value="/{name}/{number}/{startTime}/{endTime}/addKillSku",method=RequestMethod.POST,produces={"application/json;charset=UTF-8"})
//    @ResponseBody
//    public void addKillSku(@PathVariable("name") String name,@PathVariable("number") Integer number) {
    @RequestMapping(value="/{seckillId}/{number}/updateKillSku")
    @ResponseBody
    public SeckillResult updateKillSku(@PathVariable("seckillId") long seckillId,@PathVariable("number") Integer number) {
        SeckillResult result=new SeckillResult();
        Seckill seckill=new Seckill();
        seckill.setNumber(number);
        seckill.setSeckillId(seckillId);
        try{
            seckillService.updateSeckill(seckill);
            result.setSuccess(true);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            result.setError(e.getMessage());
            result.setSuccess(false);
        }
        return result;
    }




}

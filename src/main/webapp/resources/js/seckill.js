/** 存放主要交互逻辑
 * javascri 模块化
 */
var seckill ={
	//封装秒杀相关的ajax地址
	URL: {
		now: function(){
			return '/seckill/time/now';
		},
		exposer: function(seckillId){
			return '/seckill/' + seckillId + '/exposer';
		},
		execution: function(seckillId,md5){
			return '/seckill/' + seckillId + '/' + md5 + '/execution';
		},
        addgoods:function(){
            return '/seckill/addKillSku';
        },
        updategoods:function(seckillId,number){
            return '/seckill/' + seckillId +'/'+number+'/updateKillSku';
        }
	},
	//验证手机号
	validatePhone: function(phone){
		if(phone && phone.length == 11 && !isNaN(phone))
			return true;
		else
			return false;
	},
	//处理秒杀逻辑
	handleSeckill: function(seckillId,node){
		node.hide()
			.html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
		$.post(seckill.URL.exposer(seckillId), {}, function(result){
			if(result && result['success']) {
				var exposer = result['data'];
				if(exposer['exposed']){
					//开启秒杀
					var md5 = exposer['md5'];
					var killUrl = seckill.URL.execution(seckillId,md5);
					console.log('killUrl=' + killUrl);
					//绑定一次点击事件
					$('#killBtn').one('click',function(){
						//执行秒杀请求
						//1.先禁用按钮
						$(this).addClass('disable');
						//2.发送秒杀请求
						$.post(killUrl, {}, function(result){
							if(result && result['success']){
								var killResult = result['data'];
								var state = killResult['state'];
								var stateInfo = killResult['stateInfo'];
								//3.显示秒杀结果
								node.html('<span class="label label-success">' + stateInfo + '</span>');
							}
						});
					});
					node.show();
				}else {
					//未开启秒杀
					var now = exposer['now'];
					var start = exposer['start'];
					var end = exposer['end'];
					seckill.countdown(seckillId, now, start, end);
				}
			}else {
				console.log('result=' + result);
			}
		});
	},
	//时间判断
	countdown: function(seckillId,now,startTime,endTime){
		var seckillBox = $('#seckill-box');
		if(now > endTime) {
			seckillBox.html('秒杀结束!');
		}else if(now < startTime) {
			//秒杀未开始,计时事件绑定
			var killTime = new Date(startTime + 1000);
			seckillBox.countdown(killTime,function(event){
				//时间格式
				var format = event.strftime('秒杀倒计时：%D天 %H时 %M分 %S秒');
				seckillBox.html(format);
			}).on('finish.countdown',function(){ //时间完成后回调
				//获取秒杀地址，控制显示逻辑，执行秒杀
				seckill.handleSeckill(seckillId,seckillBox);
			});
			
		}else {
			//秒杀开始
			seckill.handleSeckill(seckillId,seckillBox);
		}
	},
	//封装页面秒杀逻辑
	detail: {
		//详情页初始化
		init : function(params) {
			//用户手机验证登录，计时交互
			//在cookie中查找手机号
			var killPhone = $.cookie('killPhone');
			//验证手机号
			if(!seckill.validatePhone(killPhone)) {
				//绑定phone
				var killPhoneModal = $('#killPhoneModal');
				killPhoneModal.modal({
					show: true, //显示弹出层
					backdrop: 'static', //禁止位置关闭
					keyboard: false //关闭键盘事件
				});
				$('#killPhoneBtn').click(function(){
					var inputPhone = $('#killPhoneKey').val();
					if(seckill.validatePhone(inputPhone)) {
						//电话写入cookie
						$.cookie('killPhone',inputPhone,{expires: 7, path: '/seckill'});
						//刷新页面
						window.location.reload();
					}else {
						$('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误!</label>').show(300);
					}
				});
			}
			//已经登录,计时交互
			var seckillId = params['seckillId'];
			var startTime = params['startTime'];
			var endTime = params['endTime'];
			$.get(seckill.URL.now(), {}, function(result){
				if(result && result['success']) {
					var now = result['data'];
					//时间判断
					seckill.countdown(seckillId,now,startTime,endTime);
				}else {
					console.log('result=' + result);
				}
			});
			
		}
	},

    add: {
        //添加商品初始化
        init : function(params) {
            $('#addgoods').click(function(){
                $('#addGoodsDialog').modal({
                    show: true, //显示弹出层
                    backdrop: 'static', //禁止位置关闭
                    keyboard: false ,//关闭键盘事件
                });
                $('#addBtn').click(function(){

                    var name=  $('#name').val();
                    var number= $('#number').val();
                    var startTime=$('#startTime').val();
                    var endTime=$('#endTime').val();

                    if(name.length==0||name == null){
                        alert("商品名称不能为空！") ;
                        return;
                    }
                    if(number.length==0||number == null){
                        alert("商品库存不能为空！") ;
                        return;
                    }
                    if(startTime.length==0||startTime == null){
                        alert("开始时间不能为空！") ;
                        return;
                    }
                    if(endTime.length==0||endTime == null){
                        alert("结束时间不能为空！") ;
                        return;
                    }


                    startTime=startTime.replace('-','a');
                    startTime=startTime.replace('-','a');
                    startTime=startTime.replace(' ','b');
                    startTime=startTime.replace(':','c');
                    startTime=startTime.replace(':','c');
                    endTime=endTime.replace('-','a');
                    endTime=endTime.replace('-','a');
                    endTime=endTime.replace(' ','b');
                    endTime=endTime.replace(':','c');
                    endTime=endTime.replace(':','c');

                   var  urls= '/seckill/' + name + '/' + number +'/'+startTime+'/'+endTime+'/addKillSku';
                    $.post(urls, {}, function(result){
                        if(result && result['success']) {
                            window.location.reload();
                        }else {
                            alert("添加商品失败！");
                        }
                    });
                });
            });
       }
    },


    update:{
        //修改库存初始化
        init : function(params) {

            $('#logout').click(function(){
                alert("注销成功！");
                setCookie("killPhone", 1, -1);
                window.location.reload();
            });

            $('.updategoods').click(function(){
                var itemid = this.getAttribute("itemid");   //商品iid
                $('#updateGoodsDialog').modal({
                    show: true, //显示弹出层
                    backdrop: 'static', //禁止位置关闭
                    keyboard: false//关闭键盘事件

                });
                $('#updateBtn').click(function(){
                    var number=$('#newnumber').val();
                    if(number.length==0||number == null){
                        alert("库存不能为空！") ;
                        return;
                    }
                    $.post(seckill.URL.updategoods(itemid,number), {}, function(result){
                        if(result && result['success']) {
                            window.location.reload();
                        }else {
                            alert("更新库存失败！");
                        }
                    });
                });
            });
        }
    }
}
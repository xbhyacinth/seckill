<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<div class="about">
    <ul class="area" style="width: 1020px;">
        <li class="lw fl">
            <span>关于我们</span>
            <a href="http://www.huipingou.net/help/info/cid-3/id-1" target="_blank">服务条款</a>
            <a href="http://www.huipingou.net/help/info/cid-3/id-4" target="_blank">关于我们</a>
            <a href="http://www.huipingou.net/help/info/cid-3/id-5" target="_blank" _hover-ignore="1">诚聘英才</a>
        </li>
        <li class="lw fl" _hover-ignore="1">
            <span>帮助中心</span>
            <a href="http://www.huipingou.net/help/info/cid-4/id-2" target="_blank">积分规则</a>
            <a href="http://www.huipingou.net/help/info/cid-4/id-7" target="_blank">新手上路</a>
            <a href="http://www.huipingou.net/help/info/cid-4/id-8" target="_blank">常见问题</a>
            <a href="http://www.huipingou.net/help/info/cid-4/id-12" target="_blank">秒杀技巧</a>
        </li>
        <li class="lw fl" _hover-ignore="1">
            <span _hover-ignore="1">商务合作</span>
            <a href="http://www.huipingou.net/help/info/cid-5/id-9" target="_blank">商家报名</a>
            <a href="http://www.huipingou.net/help/info/cid-5/id-6" target="_blank">友情链接</a>
            <a href="http://www.huipingou.net/help/info/cid-5/id-10" target="_blank">审核说明</a>
            <a href="http://www.huipingou.net/help/info/cid-5/id-11" target="_blank">广告赞助</a>
        </li>
        <li class="lw w2 fl" _hover-ignore="1">
            <span _hover-ignore="1">关注我们</span>
            <a href="http://widget.weibo.com/dialog/follow.php?fuid=1794961912" target="_blank" rel="nofollow">新浪微博</a>
            <a href="http://e.t.qq.com/jiukuaiwu99" target="_blank" rel="nofollow">腾讯微博</a>
            <a href="http://user.qzone.qq.com/2719920774" target="_blank" rel="nofollow">QQ空间</a>
            <a href="http://wpa.qq.com/msgrd?v=3&amp;uin=448109455&amp;site=qq&amp;menu=yes" class="contractKf" title="在线客服" target="_blank" rel="nofollow">在线客服</a>
        </li>
        <li class="w3 fl" _hover-ignore="1">
            <span _hover-ignore="1">下次怎么来?</span>
            <h3><a href="http://www.huipingou.net/desktop" title="下载桌面快捷方式" target="_blank">下载桌面快捷方式</a></h3>
            <h3>记住域名：<a href="javascript:void(0);"><em>www.huipingou.net</em></a></h3>
            <h5 style="font-size:12px;">收藏本站：<a class="clt" href="javascript:void(0);" onclick="return addfavorite(this,'惠品购','http://www.huipingou.net');"><u>加入收藏</u></a></h5>
        </li>
        <li class="w4 fl">
            <span>关注惠品购</span>
            <h4>
                <img src="static/images/code.png" class="fl" alt="" width="72" height="73">
                <p>关注惠品购，秒杀早知道<br> 如何关注？
                    <br>1) 百度搜索“<em>惠品购</em>”
                    <br>2) 用手机扫描左侧二维码
                </p>
            </h4>
        </li>
    </ul>
    <div class="clear"></div>
    <!--版权-->
    <div class="area footer">
        Copyright @ 2010-2014惠品购  版权所有  http://www.huipingou.net  All Rights Reserved. (沪ICP备15018522号-1)<br>
    </div>
</div>



<%--弹出层 添加商品--%>
<div id="addGoodsDialog" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h3 class="modal-title text-center">
                    <span class="glyphicon glyphicon-phone"> </span>商品添加
                </h3>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-xs-8 col-xs-offset-2">
                        商品名称：<input type="text" name="name" id="name" placeholder="填写商品名称" class="form-control">
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-8 col-xs-offset-2">
                        商品数量：<input type="text" name="number" id="number" placeholder="填写商品数量" class="form-control">
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-8 col-xs-offset-2">
                        开始时间：<input type="text" name="startTime" id="startTime" placeholder="开始时间" class="form-control">
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-8 col-xs-offset-2">
                        结束时间：<input type="text" name="endTime" id="endTime" placeholder="结束时间" class="form-control">
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <%--验证信息--%>
                <span id="killPhoneMessage" class="glyphicon"> </span>
                <button type="button" id="addBtn" class="btn btn-success">
                    Submit
                </button>
            </div>

        </div>
    </div>
</div>

</body>
<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>

<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

<%--使用CDN 获取公共js http://www.bootcdn.cn/--%>
<%--jQuery Cookie操作插件--%>
<script src="http://cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>
<%--jQuery countDown倒计时插件--%>
<script src="http://cdn.bootcss.com/jquery.countdown/2.1.0/jquery.countdown.min.js"></script>
<!-- 引入js逻辑 -->
<script src="/resources/js/seckill.js" type="text/javascript"></script>
<script type="text/javascript">
    $(function() {
        //使用EL传入参数
        seckill.add.init({
        });
    });
</script>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <!-- 引入jstl -->
<%@include file="common/tag.jsp" %>
	<%@include file="common/head.jsp" %>
    <title>秒杀列表页面1</title>
  </head>
  <body>
    <%@include file="common/header.jsp" %>
    <div class="container">
        <div class="container-wrap">
            <div class="content">
                <div class="last-box">
                    <div class="title">
                        <h3>宝贝即将售罄中，再不抢就没了</h3>
                    </div>
                    <div class="qg-last-list">
                    <% java.util.Date date =new java.util.Date();%>
                    <c:forEach var="sk" items="${list}">
                        <div class="qg-item qg-ing">
                            <img class="qg-img" src="https://img.alicdn.com/bao/uploaded/i1/109480251167762542/TB2tg2XsFXXXXa4XpXXXXXXXXXX_!!0-juitemmedia.jpg_220x220q90.jpg">
                            <div class="qg-detail">
                                <div class="name">
                                    <p class="des">${sk.name}</p>
                                    <p class="subtitle">${sk.name}</p>
                                </div>

                                <div class="process">
                                    <div class="process-text">
                                        <span>库存：<em>${sk.number}</em></span>
                                        <span>开始时间：<em><fmt:formatDate value="${sk.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></em></span>
                                        <span>结束世界： <em><fmt:formatDate value="${sk.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/></em> </span>
                                    </div>

                                </div>
                                <div class="link">
                                    <c:choose>
                                        <c:when test="${sk.number <= 0}">
                                            <div class="over-btn">已结结束</div>
                                        </c:when>
                                        <c:otherwise>

                                            <c:set var="nowDate">
                                                <fmt:formatDate value="<%=new java.util.Date()%>" pattern="yyyy-MM-dd HH:mm:ss" type="date"/>
                                            </c:set>
                                            <c:set var="startDate">
                                                <fmt:formatDate value="${sk.startTime}" pattern="yyyy-MM-dd HH:mm:ss" type="date"/>
                                            </c:set>
                                            <c:set var="endDate">
                                                <fmt:formatDate value="${sk.endTime}" pattern="yyyy-MM-dd HH:mm:ss" type="date"/>
                                            </c:set>
                                            <c:choose>
                                                <c:when test="${nowDate > endDate}">
                                                    <div class="over-btn">已结结束</div>
                                                </c:when>
                                                <c:when test="${nowDate <= startDate}">
                                                    <div class="nostart-btn">还未开始</div>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="link-btn"><a href="/seckill/${sk.seckillId}/detail" target="_blank">立即秒杀</a></div>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:otherwise>
                                    </c:choose>

                                </div>
                            </div>
                        </div>
                    </c:forEach>
                    </div>
                </div>
            </div>

            <div id="J_Aside" class="aside">
                <div class="aside-wrap">
                    <div class="module" data-spm="a2150ht">
                        <style>
                            #J_Aside .qrcode {width: 180px; height: 234px; margin-bottom: 0;}
                            #J_Aside .aside-wrap {padding-top: 0;}
                        </style>
                        <img class="qrcode" src="/resources/images/right.jpg" alt="烫烫烫">
                    </div>
                </div>
            </div>


        </div>
    </div>

    <%@include file="common/footer.jsp" %>
    <%--弹出层 添加商品--%>
    <div id="addGoodsDialog" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h3 class="modal-title text-center">
                        <span class="glyphicon glyphicon-plus"> </span>商品添加
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
                            商品库存：<input type="text" name="number" id="number" placeholder="填写商品库存" class="form-control">
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
                        添加商品
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
        seckill.update.init({});
    });
</script>
</html>
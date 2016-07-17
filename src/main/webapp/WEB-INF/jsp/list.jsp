<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <!-- 引入jstl -->
<%@include file="common/tag.jsp" %>
	<%@include file="common/head.jsp" %>
<<<<<<< HEAD
    <title>秒杀列表页面</title>
=======
    <title>秒杀列表页面1</title>

>>>>>>> 337c107e2e60f6b007955e92eb5272e62774a48f
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
                    <c:forEach var="sk" items="${list}">
                        <a href="/seckill/${sk.seckillId}/detail" target="_blank" class="qg-item qg-ing">
                            <img class="qg-img" src="">
                            <div class="qg-detail">
                                <div class="name">
                                    <p class="des">${sk.name}</p>
                                    <p class="subtitle">${sk.name}</p>
                                </div>

                                <div class="link">
                                    <div class="price">
                                        <span class="original-price">¥<i>68</i></span>
                                        <span class="promo-price">¥<em>32<span>.8</span></em></span>
                                    </div>
                                    <div class="link-btn">马上抢</div>
                                </div>
                                <div class="process">
                                    <div class="process-text">
                                        <span class="percent">已抢购91%</span>
                                        <span class="num">已抢5941件</span>
                                    </div>
                                    <div class="process-bar"><span style="width: 91%"></span></div>
                                </div>
                            </div>
                        </a>
                    </c:forEach>
                    </div>

                    <div class="last-footer">
                        <div class="last-footer-text">即将售罄的100个宝贝都在这了，继续刷新查看最新排名</div>
                        <div class="last-footer-line"></div>
                    </div>
                </div>
            </div>

            <div id="J_Aside" class="aside">
                <div class="aside-wrap">
                    <div class="module" data-spm="a2150ht">
                        <style>
                            #J_Aside .qrcode {width: 180px; height: 240px; margin-bottom: 0;}
                            #J_Aside .aside-wrap {padding-top: 0;}
                        </style>
                        <img class="qrcode" src="./res/TB1PxFlKpXXXXXKaXXXtxAfIVXX-600-800.jpg" alt="淘抢购支付宝服务窗">
                        <ul class="nav-link">
                            <li><a href="http://qiang.taobao.com/brand.htm">品牌抢购</a></li>
                            <li><a href="http://qiang.taobao.com/final.htm">最后疯抢</a></li>
                            <li><a href="http://qiang.taobao.com/theme.htm?displayType=5">今日必抢</a></li>
                            <li><a href="http://qiang.taobao.com/theme.htm?displayType=6">抢洋货</a></li>
                        </ul>
                    </div>
                </div>
            </div>


        </div>
    </div>






	<!-- 页面显示部分 -->
	<div class="container">
		<div class="panel panel-default">
			<div class="panel-heading text-center">
				<h2>秒杀列表</h2>

                <a class="btn btn-info" href="/seckill/addKillSku" target="_blank">添加秒杀商品</a>
			</div>
			<div class="panel-body">
				<table class="table table-hover">
					<thead>
						<tr>
							<th>名称</th>
							<th>库存</th>
							<th>开始时间</th>
							<th>结束时间</th>
							<th>创建时间</th>
							<th>详情页</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="sk" items="${list}">
							<tr>
							<td>${sk.name}</td>
							<td>${sk.number}</td>
							<td><fmt:formatDate value="${sk.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							<td><fmt:formatDate value="${sk.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							<td><fmt:formatDate value="${sk.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td> 
							<td><a class="btn btn-info" href="/seckill/${sk.seckillId}/detail" target="_blank">秒杀</a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
  <%@include file="common/footer.jsp" %>
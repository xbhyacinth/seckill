<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <!-- 引入jstl -->
<%@include file="common/tag.jsp" %>
	<%@include file="common/head.jsp" %>
    <title>商品管理页面</title>
  </head>
  <body>
  <div class="app-nav">
      <div class="app-nav-wrap">
          <a class="logo" href="/seckill/list">首页</a>
          <ul class="app-nav-list">
              <li><a  href="/seckill/list">首页</a></li>
              <li><a class="active"  href="/seckill/manage">商品管理</a></li>
              <li><a  id="addgoods" href="javascript:">添加商品</a></li>
          </ul>
          <div class="app-nav-right">
              <ul class="feature-list">
                  <li class="limit"><i></i>限时限量抢</li>
                  <li class="lowest-price"><i></i>30天低价</li>
                  <li class="free-shipping"><i></i>全场包邮</li>
              </ul>
              <a class="seller-apply" id="logout" href="javascript:void(0)" target="_blank"><i></i>用户注销</a>
          </div>
      </div>
  </div>
    <div class="container">
        <div class="panel" style="width:1190px;margin: auto">
        <div class="panel-body">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>名称</th>
                    <th>库存</th>
                    <th>开始时间</th>
                    <th>结束时间</th>
                    <th>创建时间</th>
                    <th>管理</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="sk" items="${manage}">
                    <tr>
                        <td>${sk.name}</td>
                        <td>${sk.number}</td>
                        <td><fmt:formatDate value="${sk.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td><fmt:formatDate value="${sk.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td><fmt:formatDate value="${sk.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td><a class="btn btn-info updategoods" href="javascript:void(0)"  itemid="${sk.seckillId}">修改库存</a></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
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

    <%--弹出层 修改商品--%>
    <div id="updateGoodsDialog" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h3 class="modal-title text-center">
                        <span class="glyphicon glyphicon-phone"> </span>修改库存
                    </h3>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-xs-8 col-xs-offset-2">
                            商品库存：<input type="text" name="newnumber" id="newnumber" placeholder="填写商品库存" class="form-control">
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <%--验证信息--%>
                    <span id="killPhoneMessage" class="glyphicon"> </span>
                    <button type="button" id="updateBtn" class="btn btn-success">
                        修改库存
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
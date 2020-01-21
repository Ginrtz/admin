<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<title>Unicorn Admin</title>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link rel="stylesheet" href="${ctxStatic }/css/bootstrap.min.css" />
<link rel="stylesheet" href="${ctxStatic }/css/font-awesome.css" />
<link rel="stylesheet" href="${ctxStatic }/css/common/loading.css" />
<link rel="stylesheet" href="${ctxStatic }/css/unicorn-login.css" />
<!--[if lt IE 9]>
<script type="text/javascript" src="${ctxStatic }/js/respond.min.js"></script>
<![endif]-->
</head>
<body>
	<div id="container">
		<div id="logo">
			<img src="${ctxStatic }/img/logo.png" alt="" />
		</div>
		<div id="user">
			<div class="avatar">
				<div class="inner"></div>
				<img src="${ctxStatic }/img/demo/av1_1.jpg" />
			</div>
			<div class="text">
				<h4>
					Hello,
					<span class="user_name"></span>
				</h4>
			</div>
		</div>
		<div id="loginbox">
			<form id="loginform">
				<p>登&emsp;&emsp;录</p>
				<div class="input-group input-sm">
					<span class="input-group-addon">
						<i class="fa fa-user"></i>
					</span>
					<input class="form-control" type="text" id="username" placeholder="用户名" />
				</div>
				<div class="input-group input-sm">
					<span class="input-group-addon">
						<i class="fa fa-lock"></i>
					</span>
					<input class="form-control" type="password" id="password" placeholder="密码" />
				</div>
				<div class="form-actions clearfix">
					<div class="pull-left">
						<a href="#registerform" class="flip-link to-register blue">注册新用户</a>
					</div>
					<div class="pull-right">
						<a href="#recoverform" class="flip-link to-recover grey">忘记密码？</a>
					</div>
					<input type="submit" class="btn btn-block btn-primary btn-default" value="登&emsp;&emsp;录" />
				</div>
				<div class="footer-login">
					<div class="pull-left text">其他登录方式</div>
					<div class="pull-right btn-social">
						<a class="btn btn-weixin" href="#">
							<i class="fa fa-weixin"></i>
						</a>
						<a class="btn btn-qq" href="#">
							<i class="fa fa-qq"></i>
						</a>
						<a class="btn btn-weibo" href="#">
							<i class="fa fa-weibo"></i>
						</a>
					</div>
				</div>
			</form>
			<form id="recoverform" action="#">
				<p>请输入注册时使用的邮箱</p>
				<div class="input-group">
					<span class="input-group-addon">
						<i class="fa fa-envelope"></i>
					</span>
					<input class="form-control" type="text" placeholder="邮箱地址" />
				</div>
				<div class="form-actions clearfix">
					<div class="pull-left">
						<a href="#loginform" class="grey flip-link to-login">返回登录</a>
					</div>
					<div class="pull-right">
						<a href="#registerform" class="blue flip-link to-register">注册新用户</a>
					</div>
					<input type="submit" class="btn btn-block btn-inverse" value="找回密码" />
				</div>
			</form>
			<form id="registerform" action="#">
				<p>填写必要信息来注册新用户</p>
				<div class="input-group">
					<span class="input-group-addon">
						<i class="fa fa-user"></i>
					</span>
					<input class="form-control" type="text" placeholder="用户名" />
				</div>
				<div class="input-group">
					<span class="input-group-addon">
						<i class="fa fa-lock"></i>
					</span>
					<input class="form-control" type="password" placeholder="密码" />
				</div>
				<div class="input-group">
					<span class="input-group-addon">
						<i class="fa fa-lock"></i>
					</span>
					<input class="form-control" type="password" placeholder="确认密码" />
				</div>
				<div class="input-group">
					<span class="input-group-addon">
						<i class="fa fa-envelope"></i>
					</span>
					<input class="form-control" type="text" placeholder="邮箱" />
				</div>
				<div class="form-actions clearfix">
					<div class="pull-left">
						<a href="#loginform" class="grey flip-link to-login">返回登录</a>
					</div>
					<div class="pull-right">
						<a href="#recoverform" class="grey flip-link to-recover">忘记密码？</a>
					</div>
					<input type="submit" class="btn btn-block btn-success" value="注&emsp;&emsp;册" />
				</div>
			</form>
		</div>
	</div>
	<script src="${ctxStatic }/js/common/jquery.min.js"></script>
	<script src="${ctxStatic }/js/common/jsencrypt.min.js"></script>
	<script src="${ctxStatic }/js/common/es6-promise.auto.min.js"></script>
	<script src="${ctxStatic }/js/common/qs.js"></script>
	<script src="${ctxStatic }/js/common/axios.min.js"></script>
	<script src="${ctxStatic }/js/common/request.js"></script>
	<script src="${ctxStatic }/js/common/jquery-ui.custom.js"></script>
	<script src="${ctxStatic }/js/layer/layer.js"></script>
	<script src="${ctxStatic }/js/common/sysUtil.js"></script>
	<script>
		const pub_key = '${SERVER_PUB_KEY}';
		const ctx = '${ctx}';
	</script>
	<script src="${ctxStatic }/js/login/unicorn.login.js"></script>
</body>
</html>

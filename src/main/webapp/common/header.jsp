<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%>
<c:set var="webRoot" value="<%=basePath%>" />
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<title>Unicorn Admin</title>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link rel="stylesheet" href="/static/css/colorpicker.css" />
<link rel="stylesheet" href="/static/css/datepicker.css" />
<link rel="stylesheet" href="/static/css/bootstrap.min.css" />
<link rel="stylesheet" href="/static/css/font-awesome.css" />
<link rel="stylesheet" href="/static/css/fullcalendar.css" />
<link rel="stylesheet" href="/static/css/jquery-ui.css" />
<link rel="stylesheet" href="/static/css/jquery.gritter.css" />
<link rel="stylesheet" href="/static/css/jquery.jscrollpane.css" />
<link rel="stylesheet" href="/static/css/select2.css" />
<link rel="stylesheet" href="/static/css/unicorn.css" />
<link rel="stylesheet" href="/static/css/icheck/flat/blue.css" />
<!--[if lt IE 9]>
<script type="text/javascript" src="/static/js/respond.min.js"></script>
<![endif]-->
</head>
<body data-color="grey" class="flat">
	<div id="wrapper">
		<div id="header">
			<h1>
				<a href="./index.html">Unicorn Admin</a>
			</h1>
			<a id="menu-trigger" href="#">
				<i class="fa fa-bars"></i>
			</a>
		</div>
		<div id="user-nav">
			<ul class="btn-group">
				<li class="btn">
					<a title="" href="#">
						<i class="fa fa-user"></i>
						<span class="text">Profile</span>
					</a>
				</li>
				<li class="btn dropdown" id="menu-messages">
					<a href="#" data-toggle="dropdown" data-target="#menu-messages" class="dropdown-toggle">
						<i class="fa fa-envelope"></i>
						<span class="text">Messages</span>
						<span class="label label-danger">5</span>
						<b class="caret"></b>
					</a>
					<ul class="dropdown-menu messages-menu">
						<li class="title">
							<i class="fa fa-envelope-alt"></i>
							Messages
							<a class="title-btn" href="#" title="Write new message">
								<i class="fa fa-share"></i>
							</a>
						</li>
						<li class="message-item">
							<a href="#">
								<img alt="User Icon" src="img/demo/av1.jpg" />
								<div class="message-content">
									<span class="message-time"> 3 mins ago </span>
									<span class="message-sender"> Nunc Cenenatis </span>
									<span class="message"> Hi, can you meet me at the office tomorrow morning? </span>
								</div>
							</a>
						</li>
						<li class="message-item">
							<a href="#">
								<img alt="User Icon" src="img/demo/av1.jpg" />
								<div class="message-content">
									<span class="message-time"> 3 mins ago </span>
									<span class="message-sender"> Nunc Cenenatis </span>
									<span class="message"> Hi, can you meet me at the office tomorrow morning? </span>
								</div>
							</a>
						</li>
						<li class="message-item">
							<a href="#">
								<img alt="User Icon" src="img/demo/av1.jpg" />
								<div class="message-content">
									<span class="message-time"> 3 mins ago </span>
									<span class="message-sender"> Nunc Cenenatis </span>
									<span class="message"> Hi, can you meet me at the office tomorrow morning? </span>
								</div>
							</a>
						</li>
					</ul>
				</li>
				<li class="btn">
					<a title="" href="#">
						<i class="fa fa-cog"></i>
						<span class="text">Settings</span>
					</a>
				</li>
				<li class="btn">
					<a title="" href="login.html">
						<i class="fa fa-share"></i>
						<span class="text">Logout</span>
					</a>
				</li>
			</ul>
		</div>
		<div id="switcher">
			<div id="switcher-inner">
				<h3>Theme Options</h3>
				<h4>Colors</h4>
				<p id="color-style">
					<a data-color="orange" title="Orange" class="button-square orange-switcher" href="#"></a>
					<a data-color="turquoise" title="Turquoise" class="button-square turquoise-switcher" href="#"></a>
					<a data-color="blue" title="Blue" class="button-square blue-switcher" href="#"></a>
					<a data-color="green" title="Green" class="button-square green-switcher" href="#"></a>
					<a data-color="red" title="Red" class="button-square red-switcher" href="#"></a>
					<a data-color="purple" title="Purple" class="button-square purple-switcher" href="#"></a>
					<a href="#" data-color="grey" title="Grey" class="button-square grey-switcher"></a>
				</p>
				<h4 class="visible-lg">Layout Type</h4>
				<p id="layout-type">
					<a data-option="flat" class="button" href="#">Flat</a>
					<a data-option="old" class="button" href="#">Old</a>
				</p>
			</div>
			<div id="switcher-button">
				<i class="fa fa-cogs"></i>
			</div>
		</div>
		<%@ include file="menu.jsp"%>
		<div id="content">
			<div id="breadcrumb">
				<a href="#" title="Go to Home" class="tip-bottom">
					<i class="fa fa-home"></i>
					Home
				</a>
				<a href="#" class="current">Dashboard</a>
			</div>
			<div class="container-fluid">
<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html>
<head>
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Pragma" content="no-cache"> 
    <meta http-equiv="Cache-Control" content="no-cache"> 
    <meta http-equiv="Expires" content="Sat, 01 Dec 2001 00:00:00 GMT">
    
    <title>Nhom 17- Sang Thu 7</title>
    
    <link href="static/css/bootstrap.min.css" rel="stylesheet">
     <link href="static/css/style.css" rel="stylesheet">
    <!-- include summernote css/js-->
<link
	href="static/summernote-0.8.3-dist/dist/summernote.css"
	rel="stylesheet">
</head>
<body>

	<!-- /.container -->
	<div class="container" style="padding: 10px">
		<h1>
			<center>Đăng bài viết</center>
			<hr/>
		</h1>
		<div class="col-md-8 col-md-offset-2" >
		  	<form:form action="/" enctype="multipart/form-data"
			modelAttribute="post" role="form">
			<div class="form-group">
				<label for="title" cssclass="control-label">Tiêu đề</label>
				<form:input path="title" cssClass="form-control" />
			</div>
			<div class="form-group">
				<label for="content" cssclass="control-label">Mô tả</label>
				<form:textarea id="input-content"  name="content" cssClass="form-control" path="content" />
			</div>
			<div class="form-group">
				<label cssclass="control-label" for="name">File</label> 
				<input id="file" type="file" name="file">
			</div>
			<div class="form-group">
				<form:label path="url" cssClass="form-control" />
			</div>
			<button  type="submit" class="btn btn-primary pull-right" style="margin-top:50px;">Insert</button>
			</form:form>
		  </div>
		  <div class="col-md-8 col-md-offset-2" align="center">
			<h1 class="text-center">List Document</h1>
				<table class="table table-default">
				<tr>
					<th>Id</th>
					<th>Title</th>
					<th>Content</th>
					<th>Url</th>
					<th>Detail</th>
					<th>Download</th>
				</tr>
				<c:forEach var="d" items="${posts}">
					<tr class="nn-record">
						<td>${d.id}</td>
						<td>${d.title}</td>
						<td>${d.content}</td>
						<td>${d.url}</td>
						<td><a href="${d.url}" target="_blank"><span class="glyphicon glyphicon-export"></span></a></td>
						<td><a href="/download?url=${d.url}"><span class="glyphicon glyphicon-download-alt"></span></a></td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</div>
	
	<script src="static/js/jquery-1.11.1.min.js"></script>    
    <script src="static/js/bootstrap.min.js"></script>
    <script
	src="static/summernote-0.8.3-dist/dist/summernote.js"></script>
    <script>
		$(document).ready(function() {
			$('#input-content').summernote({
				  height: 150,                 // set editor height
				  minHeight: null,             // set minimum height of editor
				  maxHeight: null,             // set maximum height of editor
				  focus: false                  // set focus to editable area after initializing summe
			});
		});
	</script>
</body>
</html>

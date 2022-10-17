<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>** Board Update Spring_MVC2 **</title>
	<link rel="stylesheet" type="text/css" href="resources/myLib/myStyle.css">
</head>
<body>
<h2>** Board Update Spring_MVC2 **</h2>
<hr>
<form action="bupdate" method="Post">
	<table>
		<tr height="40"><td bgcolor="MistyRose">Seq</td>
			<td><input type="text" name="seq" value="${apple.seq}" size="20" readonly></td></tr>
								<!-- 서버에서 필요한 정보 -->
		<tr height="40"><td bgcolor="MistyRose">I D</td>
			<td><input type="text" name="id" value="${apple.id}" size="20" readonly></td></tr>
		<tr height="40"><td bgcolor="MistyRose">Title</td> 
			<td><input type="text" name="title" value="${apple.title}"></td></tr>
								<!-- 서버에서 필요한 정보 -->
		<tr height="40"><td bgcolor="MistyRose">Content</td>
			<td><textarea rows="5" cols="50" name="content" >${apple.content}</textarea></td>
								<!-- 서버에서 필요한 정보 -->
		</tr>
		<tr height="40"><td bgcolor="MistyRose">RegDate</td>
			<td><input type="text" name="regdate" value="${apple.regdate}" readonly></td></tr>
		<tr height="40"><td bgcolor="MistyRose">조회수</td>
			<td><input type="text" name="cnt" value="${apple.cnt}" size="20" readonly></td></tr>
		<tr><td></td>
			<td><input type="submit" value="글수정">&nbsp;&nbsp;
				<input type="reset" value="취소">
			</td>
		</tr>
	</table>
</form>	
<c:if test="${not empty message}">
<hr>
${message}<br>
</c:if>
<hr>
<c:if test="${loginID==apple.id || loginID=='admin'}">
	&nbsp;&nbsp;<a href="bdelete?seq=${apple.seq}&root=${apple.root}">[글삭제]</a>
</c:if>
&nbsp;&nbsp;<a href="blist">boardList</a>
&nbsp;&nbsp;<a href="javascript:history.go(-1)">이전으로</a>
&nbsp;&nbsp;<a href="home">[Home]</a>
</body>
</html>
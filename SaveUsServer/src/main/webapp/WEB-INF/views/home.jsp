<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Hello world!  
</h1>

<P>  The time on the server is ${serverTime}. </P>
<p> ID : ${id}
<form id="inserForm" action="/SaveUsServer/insert.do" enctype="multipart/form-data" method="POST">
<input type="file" id="photo" name="photo">
<input type='submit' value='post'>
</form>
</body>
</html>

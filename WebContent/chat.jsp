<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<html>
<head>

<link rel="stylesheet" type="text/css" href="cb_style.css">


<title>Locker Room</title>

</head>

 

<body>

 

<h1></DIC>Locker Room</h1>

<h4>You are signed in as ${username}.</h4>

<!-- 
You can also use below method to retrieve variable:
<%= request.getAttribute("username") %> 
-->
 
<div id="chatBox">

<c:set var="messages">
   <%= session.getAttribute("messages") %>
</c:set>

<c:forEach var="message" items="${messages}">
    <c:out value="${message}" /><br />
</c:forEach>

</div>

 

<div id="usersOnLine">

<c:set var="userList">
   <%= session.getAttribute("userList") %>
</c:set>

<c:forEach var="user" items="${userList}">
    <c:out value="${user}" /><br />
</c:forEach>

</div>

 

<form id="messageForm" action="ChatServlet" method="post">

    <input id="message" name="message" type="text">

    <input id="send" type="submit" value="Send">

<div id="serverRes"></div>

</form>

</body>

</html>

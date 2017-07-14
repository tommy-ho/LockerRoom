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


<c:forEach var="message" items="${messages}" varStatus="status">

	<c:if test="${status.first}">
		<c:if test="${status.last}">
        	${message.replaceFirst("\\[", "").replaceAll("\\]$", "")}  <br />
        </c:if>
    </c:if>

	<c:if test="${status.first}">
		<c:if test="${!status.last}">
        	${message.replaceFirst("\\[", "")}  <br />
        </c:if>
    </c:if>
    
    <c:if test="${!status.first}">
        <c:if test="${!status.last}">
        	${message}  <br />
        </c:if>
    </c:if>
    
    <c:if test="${status.last}">
		<c:if test="${!status.first}">
        	${message.replaceAll("\\]$", "")}  <br />
        </c:if>
    </c:if>
    
</c:forEach>

</div>

 

<div id="usersOnLine">

<c:set var="userList">
   <%= session.getAttribute("userList") %>
</c:set>

<c:forEach var="user" items="${userList}">
    ${user.replaceFirst("\\[", "").replaceAll("\\]$", "")} <br />
</c:forEach>

</div>

 

<form id="messageForm" action="ChatServlet" method="post">

    <input id="message" name="message" type="text">

    <input id="send" type="submit" name="request" value="Send">
    
    <input id="refresh" type="submit" name="request" value="Refresh">

<div id="serverRes"></div>

</form>

</body>

</html>

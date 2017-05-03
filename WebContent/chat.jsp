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
 

<div id="chatBox"></div>

 

<div id="usersOnLine"></div>

 

<form id="messageForm">

    <input id="message" type="text">

    <input id="send" type="submit" value="Send">

<div id="serverRes"></div>

</form>

</body>

</html>

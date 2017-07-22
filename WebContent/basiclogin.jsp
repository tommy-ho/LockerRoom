<html>
<head>

<link rel="stylesheet" type="text/css" href="cb_style.css">


<title>Locker Room Login</title>

</head>

 

<body>

 

<h1>Locker Room Login</h1>
<span id="signInName"> MUST BE 18+ TO ENTER</span>


<form id="signInForm" action="LoginServlet" method="post"> <!-- WEB-INF/classes/LockerRoomServlet -->
	<div align="left">Username</div><input id="username" name="signIn" type="text"> <br><br>
	<div align="left">Password</div><input id="username" name="password" type="text"> <br><br>
	<input id="signInButton" type="submit" value="Enter the room" onclick="">
</form>



</body>

</html>

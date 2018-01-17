<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta charset="UTF-8">
  <title>Locker Room</title>
  
  
  
      <link rel="stylesheet" href="css/style.css">

  
</head>

<body>
  <!--Google Font - Work Sans-->
<link href='https://fonts.googleapis.com/css?family=Work+Sans:400,300,700' rel='stylesheet' type='text/css'>

<form id="signInForm" action="LoginServlet" method="post"> <!-- WEB-INF/classes/LockerRoomServlet -->
<div class="container">
  <div class="profile--open">
  	<button class="profile__avatar" id="toggleProfile">
    	<img src="images/register.png" alt="Avatar" /> 
    </button>
    <div class="profile__form">
      <div class="profile__fields">
        <div class="field">
        <div>${status}</div><br><br>
          <input name="signIn" type="text" id="fieldUser" class="input" required pattern=.*\S.* />
          <label for="fieldUser" class="label">Username</label>
        </div>
        <div class="field">
          <input name="password" type="password" id="fieldPassword" class="input" required pattern=.*\S.* />
          <label for="fieldPassword" class="label">Password</label>
        </div>
		<div class="profile__footer">
          <button name="request" value="register" type="submit" id="register" class="btn">Sign up</button>
        </div>
      </div>
     </div>
  </div>
</div>
</form>

<div class="profile__topRight">
	<a href="http://localhost:8080/LockerRoom/index.jsp"><button class="btn">Home</button></a><br>
	<a href="http://localhost:8080/LockerRoom/login.jsp"><button class="btn">Enter</button></a><br>
	<a href="http://localhost:8080/LockerRoom/register.jsp"><button class="btn">Register</button></a><br>
	<a href="http://localhost:8080/LockerRoom/changepw.jsp"><button class="btn">Change</button></a><br>
	<a href="http://localhost:8080/LockerRoom/deleteaccount.jsp"><button class="btn">Delete</button></a>
</div>

</body>
</html>

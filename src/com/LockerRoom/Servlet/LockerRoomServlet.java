package com.LockerRoom.Servlet;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.LockerRoom.Client.LockerRoomClient;

@WebServlet("/myservlet")
public class LockerRoomServlet extends HttpServlet {

	public LockerRoomServlet() {
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		if (request.getParameter("signIn") != null){
			String username = request.getParameter("signIn");
			request.setAttribute("username", username);
			new Thread(new LockerRoomClient(username)).start();
		}
		
		response.sendRedirect("chat.jsp");
	}

}

package com.LockerRoom.Servlet;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.LockerRoom.Client.*;
import com.LockerRoom.Server.*;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static LockerRoomServer lrs;

	public LoginServlet() {
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		if (request.getParameter("signIn") != ""){
			String username = request.getParameter("signIn");
			request.getSession().setAttribute("username", username);
			System.out.println(username);
			
			if (lrs == null){
				lrs = LockerRoomServer.getInstance();
				new Thread(lrs).start();
			}
			
			new ClientThread(new LockerRoomClient(username)).start();
				
			try {
				sendUserList(request.getSession());
				request.getRequestDispatcher("chat.jsp").forward(request, response);
			} catch (ServletException e) {
				e.printStackTrace();
			}

		} else {
			response.sendRedirect("retrylogin.jsp");
		}
	}
	
	protected void sendUserList(HttpSession session){
		ArrayList<String> userList = new ArrayList<String>();
		for (int i = 0; i < LockerRoomClient.getLrcList().size(); i++){
			userList.add(LockerRoomClient.getLrcList().get(i).getUsername());
		}
		
		session.setAttribute("userList", userList);
	}

}

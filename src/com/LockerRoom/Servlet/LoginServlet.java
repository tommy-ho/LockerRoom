package com.LockerRoom.Servlet;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.LockerRoom.Client.*;
import com.LockerRoom.Server.*;

/**
 * This is the servlet for login.jsp
 * 
* @author  Tommy Ho
* */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static LockerRoomServer lrs;
	
	/**
	 * This doPost() method utilizes HTTP POST protocol and will submit the
	 * information provided and forward the user to the chat page,
	 * generating a new thread for the client and a new thread for the
	 * server (if it is null).
	 *
	 * @param
	 * @return
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		if (request.getParameter("signIn") != ""){
			String username = request.getParameter("signIn");
			request.getSession().setAttribute("username", username);
			request.getSession().setAttribute("messages", new String(""));
			
			if (lrs == null){
				lrs = LockerRoomServer.getInstance();
				new Thread(lrs).start();;
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
	
	/**
	 * This method iterates through the static ArrayList of clients in the
	 * LockerRoomClient class, and stores that in the HttpSession object
	 * for the JSP pages to access.
	 *
	 * @param
	 * @return
	 * @see
	 */
	private void sendUserList(HttpSession session){
		ArrayList<String> userList = new ArrayList<String>();
		for (int i = 0; i < LockerRoomClient.getLrcList().size(); i++){
			userList.add(LockerRoomClient.getLrcList().get(i).getUsername());
		}
		session.setAttribute("userList", userList);
	}
	
	//deleted processMessage() and getUser()

}

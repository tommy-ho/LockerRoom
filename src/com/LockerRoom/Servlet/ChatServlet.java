package com.LockerRoom.Servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.LockerRoom.Client.ClientArrayList;
import com.LockerRoom.Client.LockerRoomClient;

/**
 * Servlet implementation class ChatServlet
 */
@WebServlet("/ChatServlet")
public class ChatServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChatServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("message") != ""){
			String username = (String) request.getSession().getAttribute("username");
			String message = request.getParameter("message");
			getUser(username).getMessageFromServlet(message);; //Submits message from JSP form to client for handling
			
			HttpSession session = request.getSession();
			
			processMessage(session, getUser(username).getMessageBuffer());
			sendUserList(session);
			
			request.getRequestDispatcher("chat.jsp").forward(request,response);

		} else {
			try {
				request.getRequestDispatcher("chat.jsp").forward(request, response);
			} catch (ServletException e) {
				e.printStackTrace();
			}
		}
		
	
	}
	
	protected LockerRoomClient getUser(String username){
		ClientArrayList<LockerRoomClient> cal = LockerRoomClient.getLrcList();
		for (int i = 0; i < cal.size(); i++){
			if (cal.get(i).getUsername().equals(username)){
				return cal.get(i);
			}
		}
		return null;
	}
	
	
	protected void processMessage(HttpSession session, ArrayList<String> messageBuffer){
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		session.setAttribute("messages", messageBuffer);

		for (int i = 0; i < messageBuffer.size(); i++){
			System.out.println(i + " " + messageBuffer.get(i)); //testing
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

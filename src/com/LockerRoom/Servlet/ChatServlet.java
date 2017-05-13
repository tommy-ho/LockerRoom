package com.LockerRoom.Servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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
			processMessage(getUser(username).getMessageBuffer());
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
	
	
	//NEXT METHOD TO CODE, process each line and output to jsp page
	protected void processMessage(ArrayList<String> messageBuffer){
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < messageBuffer.size(); i++){
			System.out.println(i + " " + messageBuffer.get(i)); //testing
		}
	}
	
	

}

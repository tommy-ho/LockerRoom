package com.LockerRoom.Servlet;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.LockerRoom.Client.LockerRoomClient;


/**
 * This is the servlet for chat.jsp
 * 
* @author  Tommy Ho
* */
@WebServlet("/ChatServlet")
public class ChatServlet extends LockerRoomServlet {
	
	private static final long serialVersionUID = 1L;
  
	/**
	 * This doPost() method utilizes HTTP POST and submits the text provided
	 * in the JSP page as messages to be processed by the LockerRoomClient and
	 * then by the LockerRoomServer. This method also provides refresh
	 * functionality if the user clicks "Refresh" instead.
	 * <p>
	 * This method also calls on the processMessage() method so that the redirect
	 * to the refreshed page will have the most up-to-date messages.
	 * 
	 * @param
	 * @return
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * @see LockerRoomClient#getMessageFromServlet(String)
	 * @see #processMessage(HttpSession, ArrayList)
	 * @see #sendUserList(HttpSession)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String req = request.getParameter("request");
		
		if (req.equals("Refresh")) {
			refreshMessages(request, response);
		} else if (req.equals("Send")) {
			if (request.getParameter("message") != ""){
				sendMessage(request, response);
			} else {
				refreshMessages(request, response);
			}
		} else if (req.equals("Disconnect")){
			promptDisconnect(request, response);
		}
	}

	/**
	 * processMessage() takes the messages stored in each LockerRoomClient
	 * and stores it in a HttpSession object. The JSP pages will read from
	 * this object and display to the end user.
	 * 
	 * @param session the HttpSession that stores data from page to page
	 * @param messageBuffer the array of messages stored in each LockerRoomClient
	 * @return
	 * @see
	 */
	private void processMessage(HttpSession session, ArrayList<String> messageBuffer){
		try {
			Thread.sleep(1);
			//Allows server side ClientManager thread time to run publishMessage()
			//and send message back to client thread, which will then add it to
			//the messageBuffer
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		session.setAttribute("messages", messageBuffer);
		for (int i = 0; i < messageBuffer.size(); i++){
			System.out.println(i + " " + messageBuffer.get(i)); //Prints into console
		}
	}
	
	/**
	 * This method accesses the LockerRoomClient's message buffer array,
	 * and runs processMessage() while skipping the part where you send via
	 * getMessageFromServlet(). This allows refreshing of the page without
	 * sending any messages.
	 *
	 * @param
	 * @return
	 * @see #processMessage(HttpSession, ArrayList)
	 * @see #sendUserList(HttpSession)
	 */
	private void refreshMessages(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		processMessage(session, getUser(username).getMessageBuffer());
		sendUserList(session);
		request.getRequestDispatcher("chat.jsp").forward(request, response);
	}
	
	/**
	 * This method accesses the LockerRoomClient's message buffer array,
	 * and runs processMessage(), and also utilizes the LockerRoomClient's
	 * getMessageFromServlet() method to send the message.
	 *
	 * @param
	 * @return
	 * @see #processMessage(HttpSession, ArrayList)
	 * @see #sendUserList(HttpSession)
	 */
	private void sendMessage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		String message = request.getParameter("message");
		getUser(username).getMessageFromServlet(message);;
		processMessage(session, getUser(username).getMessageBuffer());
		sendUserList(session);
		request.getRequestDispatcher("chat.jsp").forward(request,response);
	}
	
	private void promptDisconnect(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		getUser(username).disconnectFromServer();
		request.getSession().setAttribute("status", "You have left the room...");
		request.getRequestDispatcher("disconnected.jsp").forward(request,response);
	}
	
}

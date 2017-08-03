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
 * Servlet implementation class LockerRoomServlet
 */
@WebServlet("/LockerRoomServlet")
public abstract class LockerRoomServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * This method iterates through the static ArrayList of clients in the
	 * LockerRoomClient class, and stores that in the HttpSession object
	 * for the JSP pages to access.
	 *
	 * @param
	 * @return
	 * @see
	 */
	void sendUserList(HttpSession session){
		ArrayList<String> userList = new ArrayList<String>();
		for (int i = 0; i < LockerRoomClient.getLrcList().size(); i++){
			userList.add(LockerRoomClient.getLrcList().get(i).getUsername());
		}
		session.setAttribute("userList", userList);
	}
	
	/**
	 * The getUser() method takes in a string parameter and iterates through the
	 * LockerRoomClient's static ArrayList of clients in the session to retrieve
	 * the LockerRoomClient object.
	 * 
	 * @param username the name of the LockerRoomClient being retrieved
	 * @return the LockerRoomClient with the matching username
	 * @see
	 */
	LockerRoomClient getUser(String username){
		ClientArrayList<LockerRoomClient> cal = LockerRoomClient.getLrcList();
		for (int i = 0; i < cal.size(); i++){
			if (cal.get(i).getUsername().equals(username)){
				return cal.get(i);
			}
		}
		return null;
	}


}

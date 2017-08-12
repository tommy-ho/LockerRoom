package com.LockerRoom.Servlet;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.LockerRoom.Client.*;
import com.LockerRoom.Server.*;
import com.LockerRoom.Utils.LockerRoomRegistrar;

/**
 * This is the servlet for login.jsp
 * 
* @author  Tommy Ho
* */
@WebServlet("/LoginServlet")
public class LoginServlet extends LockerRoomServlet {

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
		String req = request.getParameter("request");
		if (req.equals("register")){
			doRegister(request, response);
		} else if (req.equals("login")){
			doLogin(request, response);
		}
	}
	
	private void doRegister(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String username = request.getParameter("signIn");
		String password = request.getParameter("password");
		System.out.println("Username: " + username);
		System.out.println("Password: " + password);
		
		try {
			if (LockerRoomRegistrar.registerUser(username, password)){
				request.getSession().setAttribute("status", "You have joined the brotherhood! Enter your credentials below:");
				request.getRequestDispatcher("login.jsp").forward(request, response);
			} else {
				request.getSession().setAttribute("status", "Your name sounds awefully familiar...come again?");
				request.getRequestDispatcher("register.jsp").forward(request, response);
			}
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}
	
	private void doLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (request.getParameter("signIn").equals("")){
			String username = request.getParameter("signIn");
			String password = request.getParameter("password");
			request.getSession().setAttribute("username", username);
			request.getSession().setAttribute("messages", new String(""));
			
			try {
				if (LockerRoomRegistrar.checkUserExist(username)){
					if (LockerRoomRegistrar.checkPW(username, password)){
						if (LockerRoomRegistrar.isUserLoggedIn(username)){
							request.getSession().setAttribute("status", "This account is already logged in...");
							request.getRequestDispatcher("login.jsp").forward(request, response);
						} else {
							if (lrs == null){
								lrs = LockerRoomServer.getInstance();
								new Thread(lrs).start();;
							}
							new ClientThread(new LockerRoomClient(username)).start();
							sendUserList(request.getSession());
							request.getRequestDispatcher("chat.jsp").forward(request, response);
						}
					} else {
						request.getSession().setAttribute("status", "Are you trying to impersonate someone?");
						request.getRequestDispatcher("login.jsp").forward(request, response);
					}
				} else {
					request.getSession().setAttribute("status", "Name doesn't ring a bell...");
					request.getRequestDispatcher("login.jsp").forward(request, response);
				}
			} catch (ServletException e) {
				e.printStackTrace();
			}
		} else {
			response.sendRedirect("index.jsp");
		}
	}

}

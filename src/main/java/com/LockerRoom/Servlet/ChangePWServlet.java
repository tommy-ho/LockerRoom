package com.LockerRoom.Servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.LockerRoom.Utils.LockerRoomRegistrar;

/**
 * This is the servlet for changepw.jsp
 * 
* @author  Tommy Ho
* */
@WebServlet("/ChangePWServlet")
public class ChangePWServlet extends LockerRoomServlet{

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		doChangePW(request, response);
	}
	
	private void doChangePW(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String username = request.getParameter("signIn");
		String oldpw = request.getParameter("password");
		String newpw = request.getParameter("newpassword");
		try {
			if (LockerRoomRegistrar.changePW(username, oldpw, newpw)){
				request.getSession().setAttribute("status", "Password changed. Try your new credentials:");
				request.getRequestDispatcher("login.jsp").forward(request, response);
			} else {
				request.getSession().setAttribute("status", "Credentials not found. Try again");
				request.getRequestDispatcher("changepw.jsp").forward(request, response);
			}
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}


}

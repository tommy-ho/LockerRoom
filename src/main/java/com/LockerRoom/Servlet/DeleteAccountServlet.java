package com.LockerRoom.Servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.LockerRoom.Utils.LockerRoomRegistrar;

/**
 * Servlet implementation class DeleteAccountServlet
 */
@WebServlet("/DeleteAccountServlet")
public class DeleteAccountServlet extends LockerRoomServlet {
	
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doDeleteAccount(request, response);
	}

	private void doDeleteAccount(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String username = request.getParameter("signIn");
		String pw = request.getParameter("password");
		String confirmPW = request.getParameter("confirmPassword");
		
		try {
			
			if (pw.equals(confirmPW)){
				if (LockerRoomRegistrar.removeUser(username, pw)){
					request.getSession().setAttribute("status", "You have left the brotherhood!");
					request.getRequestDispatcher("login.jsp").forward(request, response);
					//return to page confirm deleted
				} else {
					request.getSession().setAttribute("status", "Credentials not found, try again:");
					request.getRequestDispatcher("deleteaccount.jsp").forward(request, response);
					//return some credentials don't match
				}
			} else {
				request.getSession().setAttribute("status", "Mismatched passwords, try again:");
				request.getRequestDispatcher("deleteaccount.jsp").forward(request, response);
				//return that password doesn't match
			}
			

		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}

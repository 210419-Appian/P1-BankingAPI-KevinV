package com.revature.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.User;
import com.revature.models.UserDTO;
import com.revature.services.UserService;

//requests to /login will go to this specific Controller
//this Controller is NOT a servlet (no extending anything)
public class LoginController {
	private UserService uServ = new UserService();
	private ObjectMapper om = new ObjectMapper();
	
	public void login(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException{
		//login credentials will come in via req.body parameters
		BufferedReader reader = req.getReader();
		StringBuilder sb = new StringBuilder();

		String line = reader.readLine();
		while(line!=null) {
			sb.append(line);
			line = reader.readLine();
		}

		String finalString = new String(sb);

		//then create avenger off of full string using object Mapper
		UserDTO u = om.readValue(finalString, UserDTO.class);
		
		
		RequestDispatcher rd = null;
		PrintWriter out = res.getWriter();
		User user = null;
		
		//login returns userObj if logged in, null otherwise
		user = uServ.login(u);
		
		
		if(u.username != null && user != null) {
			System.out.println(user);
			res.setStatus(200);	//successful Login
			HttpSession ses = req.getSession();
			
			//an active session holds the user's role, for the sake of permissions in later routes
			ses.setAttribute("userID", user.getUserID());
			ses.setAttribute("username", u.getUsername());
			ses.setAttribute("role", user.getRole().getRoleID());
			
			//show custom logged-in confirmation
			rd = req.getRequestDispatcher("loginSuccess");	
			rd.forward(req, res);
		} else {
			res.setStatus(400);	//failure Login
			res.setContentType("text/html");
			out.print("<span style='color:red; text-align:center'>Invalid Username/Password</span>");
			System.out.println("DTO SAYS: "+u.username + ", " + u.password);
		}


	}
	

}

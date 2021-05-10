package com.revature.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.JSONmessage;
import com.revature.models.User;
import com.revature.models.UserDTO;
import com.revature.services.UserService;

//requests to /users will go to this specific Controller
//this Controller is NOT a servlet (no extending anything)
public class UserController {
	private UserService uServ = new UserService();
	private ObjectMapper om = new ObjectMapper();

	public void getAllUsers(HttpServletRequest req, HttpServletResponse res) throws IOException{
		List<User> list = uServ.getAllUsers();

		//convert this list Object into JSON, to send it within a res body
		String json = om.writeValueAsString(list);
		String[] allUsers = json.split(",");
		for(String s: allUsers) {
			System.out.println(s);
			System.out.println();
		}

		PrintWriter pw = res.getWriter();
		pw.print(json);
		res.setStatus(200);
		res.setContentType("application/json");
	}

	public void updateUser(HttpServletRequest req, HttpServletResponse res) throws IOException{
		//req body comes in with many <br>. we add line by line via StringBuilder
		StringBuilder in = new StringBuilder();

		//req body has a builtin method to return an Object that reads the body line by line
		BufferedReader reader = req.getReader();

		String line = reader.readLine();
		while(line != null) {
			in.append(line);
			line = reader.readLine();
		}
		String fullBodyContents = new String(in);

		//now the object mapper can read the req Body into a Java Object
		System.out.println(fullBodyContents);
		User u = om.readValue(fullBodyContents, User.class);
		System.out.println("Newly modified User: ---" + u.getUsername());

		//ensure that only admins or currentUser is requesting this
		HttpSession ses = req.getSession(false);
		PrintWriter out = res.getWriter();
		JSONmessage unauthorizedMsg = new JSONmessage("The requested action is not permitted");
		String unauthorized = om.writeValueAsString(unauthorizedMsg);


		if(ses == null) {
			out = res.getWriter();
			System.out.println("No Active Sesssion in PUT /users");
			out.print(unauthorized);
			res.setStatus(401);
			res.setContentType("application/json");
			return;
		} 
		String currentUser = ses.getAttribute("username").toString();
		int currentRole = Integer.parseInt(ses.getAttribute("role").toString());
		System.out.println("current user= " + currentUser + ", role="+ currentRole);
		if(currentRole != 1 && !(currentUser.equals(u.getUsername()))) {
			out = res.getWriter();
			out.print(unauthorized);
			res.setStatus(401);
			res.setContentType("application/json");
			return;
		}

		//we've ensured that only admins or currentUser is requesting this
		uServ.updateUser(u);
		System.out.println("updated user");
		User updatedUser = uServ.getUserByUserName(u.getUsername());
		String updatedUserJSON = om.writeValueAsString(updatedUser);
		out.print(updatedUserJSON);
		res.setStatus(200);
		res.setContentType("application/json");
	}

	public void getUserById(HttpServletRequest req, HttpServletResponse res, int id) throws IOException {
		HttpSession ses = req.getSession(false);
		PrintWriter out = res.getWriter();
		JSONmessage unauthorizedMsg = new JSONmessage("The requested action is not permitted");
		String unauthorized = om.writeValueAsString(unauthorizedMsg);

		if(ses == null) {
			out = res.getWriter();
			System.out.println("No Active Sesssion in GET /users/{id}");
			out.print(unauthorized);
			res.setStatus(401);
			res.setContentType("application/json");
			return;
		} 
		int currentUserID = Integer.parseInt(ses.getAttribute("userID").toString());
		int currentRole = Integer.parseInt(ses.getAttribute("role").toString());
		if(currentRole != 1 && currentRole != 2 && currentUserID!=id) {
			out = res.getWriter();
			out.print(unauthorized);
			System.out.println("GET users/{id} wasn't a match");
			res.setStatus(401);
			res.setContentType("application/json");
			return;
		}

		//security passed; return userByID as JSON in res
		User byID = uServ.getUserByID(id);
		String byIDJSON = om.writeValueAsString(byID);
		out.print(byIDJSON);
		res.setStatus(200);
		res.setContentType("application/json");

	}

	@SuppressWarnings("resource")
	public void registerNewUser(HttpServletRequest req, HttpServletResponse res) throws IOException{
		//req body comes in with many <br>. we add line by line via StringBuilder
		StringBuilder in = new StringBuilder();

		//req body has a builtin method to return an Object that reads the body line by line
		BufferedReader reader = req.getReader();

		String line = reader.readLine();
		while(line != null) {
			in.append(line);
			line = reader.readLine();
		}
		String fullBodyContents = new String(in);

		//now the object mapper can read the req Body into a Java Object
		System.out.println(fullBodyContents);
		UserDTO tempUser = om.readValue(fullBodyContents, UserDTO.class);


		//ensure that only admin requests this
		HttpSession ses = req.getSession(false);
		PrintWriter out = res.getWriter();
		JSONmessage unauthorizedMsg = new JSONmessage("The requested action is not permitted");
		String unauthorized = om.writeValueAsString(unauthorizedMsg);

		if(ses == null) {
			out = res.getWriter();
			System.out.println("No Active Sesssion in POST /register");
			out.print(unauthorized);
			res.setStatus(401);
			res.setContentType("application/json");
			return;
		} 

		int currentRole = Integer.parseInt(ses.getAttribute("role").toString());
		if(currentRole != 1) {
			out = res.getWriter();
			out.print(unauthorized);
			System.out.println("only admins can /register users");
			res.setStatus(401);
			res.setContentType("application/json");
			return;
		}
		
		//security passed
		//create new User (userID will be serial)
		List<String> usernames = uServ.getAllUsernames();
		for(String s: usernames) {
			//if new Username already exists, cancel the registration
			if(tempUser.username.equals(s)) {
				JSONmessage alreadyTaken = new JSONmessage("Invalid fields (username is taken)");
				String cancelled = om.writeValueAsString(alreadyTaken);
				out = res.getWriter();
				out.print(cancelled);
				res.setStatus(400);
				res.setContentType("application/json");
				out.close();
				return;
			}
		}
		if(uServ.addNewUser(tempUser)) {
			System.out.println("added NEW user: " +tempUser.getUsername());
			User updatedUser = uServ.getUserByUserName(tempUser.getUsername());
			String updatedUserJSON = om.writeValueAsString(updatedUser);
			out.print(updatedUserJSON);
			res.setStatus(201);
			res.setContentType("application/json");
		}

	}
}

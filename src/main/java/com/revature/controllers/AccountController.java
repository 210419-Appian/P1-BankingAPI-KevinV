package com.revature.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.Account;
import com.revature.models.AccountDTO;
import com.revature.models.JSONmessage;
import com.revature.services.AccountService;
import com.revature.services.UserService;

//requests to /account will go to this specific Controller
//this Controller is NOT a servlet (no extending anything)
public class AccountController {
	private UserService uServ = new UserService();
	private AccountService aServ = new AccountService();
	private ObjectMapper om = new ObjectMapper();

	public void getAllAccounts(HttpServletRequest req, HttpServletResponse res) throws IOException{
		List<Account> list = aServ.getAllAccounts();

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

	public void getAccountById(HttpServletRequest req, HttpServletResponse res, int id) throws IOException {
		HttpSession ses = req.getSession(false);
		PrintWriter out = res.getWriter();
		JSONmessage unauthorizedMsg = new JSONmessage("The requested action is not permitted");
		String unauthorized = om.writeValueAsString(unauthorizedMsg);

		if(ses == null) {
			out = res.getWriter();
			System.out.println("No Active Sesssion in GET /accounts/{id}");
			out.print(unauthorized);
			res.setStatus(401);
			res.setContentType("application/json");
			return;
		} 
		Account byID = aServ.getAccountById(id);
		int currentUserID = Integer.parseInt(ses.getAttribute("userID").toString());
		int currentRole = Integer.parseInt(ses.getAttribute("role").toString());
		if(currentRole != 1 && currentRole != 2 && currentUserID!=byID.getOwner().getUserID()) {
			out = res.getWriter();
			out.print(unauthorized);
			System.out.println("GET accounts/{id} wasn't a match");
			res.setStatus(401);
			res.setContentType("application/json");
			return;
		}

		//security passed; return accountByID as JSON in res

		String byIDJSON = om.writeValueAsString(byID);
		out.print(byIDJSON);
		res.setStatus(200);
		res.setContentType("application/json");
	}

	public void getAccountsByStatus(HttpServletRequest req, HttpServletResponse res, int statusID) throws IOException {
		HttpSession ses = req.getSession(false);
		PrintWriter out = res.getWriter();
		JSONmessage unauthorizedMsg = new JSONmessage("The requested action is not permitted");
		String unauthorized = om.writeValueAsString(unauthorizedMsg);

		if(ses == null) {
			out = res.getWriter();
			System.out.println("No Active Sesssion in GET /users");
			out.print(unauthorized);
			res.setStatus(401);
			res.setContentType("application/json");
			return;
		} 

		int currentRole = Integer.parseInt(ses.getAttribute("role").toString());
		if(currentRole != 1 && currentRole != 2) {
			out = res.getWriter();
			out.print(unauthorized);
			res.setStatus(401);
			res.setContentType("application/json");
			return;
		}

		//security passed; return accountsByStatusID as JSON in res
		List<Account> byStatusID = aServ.getAccountsByStatus(statusID);
		String byStatusIDJSON = om.writeValueAsString(byStatusID);
		out.print(byStatusIDJSON);
		res.setStatus(200);
		res.setContentType("application/json");
	}

	public void getAccountsByUserId(HttpServletRequest req, HttpServletResponse res, int userID) throws IOException {
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
		if(currentRole != 1 && currentRole != 2 && currentUserID!=userID) {
			out = res.getWriter();
			out.print(unauthorized);
			System.out.println("GET users/{id} wasn't a match");
			res.setStatus(401);
			res.setContentType("application/json");
			return;
		}

		//security passed; return userByID as JSON in res
		List<Account> byUserID = aServ.getAccountsByUserId(userID);
		String byUserIDJSON = om.writeValueAsString(byUserID);
		out.print(byUserIDJSON);
		res.setStatus(200);
		res.setContentType("application/json");
	}

	public void updateAccount(HttpServletRequest req, HttpServletResponse res) throws IOException {
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
		AccountDTO aDTO = om.readValue(fullBodyContents, AccountDTO.class);

		//ensure that only admins are requesting this
		HttpSession ses = req.getSession(false);
		PrintWriter out = res.getWriter();
		JSONmessage unauthorizedMsg = new JSONmessage("The requested action is not permitted");
		String unauthorized = om.writeValueAsString(unauthorizedMsg);


		if(ses == null) {
			out = res.getWriter();
			System.out.println("No Active Sesssion in PUT /accounts");
			out.print(unauthorized);
			res.setStatus(401);
			res.setContentType("application/json");
			return;
		} 
		int currentRole = Integer.parseInt(ses.getAttribute("role").toString());
		if(currentRole != 1) {
			out = res.getWriter();
			out.print(unauthorized);
			res.setStatus(401);
			res.setContentType("application/json");
			return;
		}

		//we've ensured that only admins are requesting this
		aServ.updateAccount(aDTO);
		System.out.println("updated account");
		Account updatedAccount = aServ.getAccountById(aDTO.getAccountID());
		String updatedAccountJSON = om.writeValueAsString(updatedAccount);
		out.print(updatedAccountJSON);
		res.setStatus(200);
		res.setContentType("application/json");
	}
}

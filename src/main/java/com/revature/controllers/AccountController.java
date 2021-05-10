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
import com.revature.models.TransferDTO;
import com.revature.services.AccountService;

//requests to /account will go to this specific Controller
//this Controller is NOT a servlet (no extending anything)
public class AccountController {
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

	public void withdraw(HttpServletRequest req, HttpServletResponse res) throws IOException{
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
		System.out.println("This DTO should only have 2 things");
		System.out.println(aDTO.accountID);
		System.out.println(aDTO.statusID + " should be null");
		System.out.println(aDTO.amount);

		//ensure that only admin or currentUserID = {accountID} request this
		HttpSession ses = req.getSession(false);
		PrintWriter out = res.getWriter();
		JSONmessage unauthorizedMsg = new JSONmessage("The requested action is not permitted");
		String unauthorized = om.writeValueAsString(unauthorizedMsg);

		if(ses == null) {
			out = res.getWriter();
			System.out.println("No Active Sesssion in POST /accounts/withdraw");
			out.print(unauthorized);
			res.setStatus(401);
			res.setContentType("application/json");
			return;
		} 
		Account byID = aServ.getAccountById(aDTO.getAccountID());
		int currentUserID = Integer.parseInt(ses.getAttribute("userID").toString());
		int currentRole = Integer.parseInt(ses.getAttribute("role").toString());
		if(currentRole != 1 && currentUserID != byID.getOwner().getUserID()) {
			out = res.getWriter();
			out.print(unauthorized);
			System.out.println("POST accounts/withdraw wasn't a match");
			res.setStatus(401);
			res.setContentType("application/json");
			return;
		}

		//security passed
		//update account with amount from aDTO, then aServ.updateAccount(newAccountDTO a)
		byID.setBalance(byID.getBalance() - aDTO.getAmount());

		//if withdraw would make balance negative, cancel the withdraw entirely
		if(byID.getBalance() < 0.0) {
			JSONmessage insufficientFunds = new JSONmessage("insufficient funds; withdraw cancelled");
			String cancelled = om.writeValueAsString(insufficientFunds);
			out = res.getWriter();
			out.print(cancelled);
			res.setStatus(401);
			res.setContentType("application/json");
			return;
		}

		AccountDTO updatedDTO = new AccountDTO(byID.getAccountID(),byID.getBalance(),byID.getStatus().getStatusID(),byID.getType().getTypeID(),byID.getOwner().getUserID());
		aServ.updateAccount(updatedDTO);

		JSONmessage successMsg = new JSONmessage("$"+aDTO.getAmount()+" has been withdrawn from Account #"+aDTO.accountID);
		String withdrawn = om.writeValueAsString(successMsg);
		out.print(withdrawn);
		res.setStatus(200);
		res.setContentType("application/json");

	}

	public void deposit(HttpServletRequest req, HttpServletResponse res) throws IOException {
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
		System.out.println("This DTO should only have 2 things");
		System.out.println(aDTO.accountID);
		System.out.println(aDTO.statusID + " should be null");
		System.out.println(aDTO.amount);

		//ensure that only admin or currentUserID = {accountID} request this
		HttpSession ses = req.getSession(false);
		PrintWriter out = res.getWriter();
		JSONmessage unauthorizedMsg = new JSONmessage("The requested action is not permitted");
		String unauthorized = om.writeValueAsString(unauthorizedMsg);

		if(ses == null) {
			out = res.getWriter();
			System.out.println("No Active Sesssion in POST /accounts/deposit");
			out.print(unauthorized);
			res.setStatus(401);
			res.setContentType("application/json");
			return;
		} 
		Account byID = aServ.getAccountById(aDTO.getAccountID());
		int currentUserID = Integer.parseInt(ses.getAttribute("userID").toString());
		int currentRole = Integer.parseInt(ses.getAttribute("role").toString());
		if(currentRole != 1 && currentUserID != byID.getOwner().getUserID()) {
			out = res.getWriter();
			out.print(unauthorized);
			System.out.println("POST accounts/deposit wasn't a match");
			res.setStatus(401);
			res.setContentType("application/json");
			return;
		}

		//security passed
		//update account with amount from aDTO, then aServ.updateAccount(newAccountDTO a)
		byID.setBalance(byID.getBalance() + aDTO.getAmount());
		AccountDTO updatedDTO = new AccountDTO(byID.getAccountID(),byID.getBalance(),byID.getStatus().getStatusID(),byID.getType().getTypeID(),byID.getOwner().getUserID());
		aServ.updateAccount(updatedDTO);

		JSONmessage successMsg = new JSONmessage("$"+aDTO.getAmount()+" has been deposited to Account #"+aDTO.accountID);
		String deposited = om.writeValueAsString(successMsg);
		out.print(deposited);
		res.setStatus(200);
		res.setContentType("application/json");

	}

	public void transfer(HttpServletRequest req, HttpServletResponse res) throws IOException {
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
		TransferDTO tDTO = om.readValue(fullBodyContents, TransferDTO.class);
		System.out.println("This DTO should only have 3 things");
		System.out.println("source acc: " + tDTO.sourceAccountID);
		System.out.println("target acc: " + tDTO.targetAccountID);
		System.out.println("transfering : $" + tDTO.amount);

		//ensure that only admin or currentUserID = {sourceAccountID} request this
		HttpSession ses = req.getSession(false);
		PrintWriter out = res.getWriter();
		JSONmessage unauthorizedMsg = new JSONmessage("The requested action is not permitted");
		String unauthorized = om.writeValueAsString(unauthorizedMsg);

		if(ses == null) {
			out = res.getWriter();
			System.out.println("No Active Sesssion in POST /accounts/withdraw");
			out.print(unauthorized);
			res.setStatus(401);
			res.setContentType("application/json");
			return;
		} 
		Account srcByID = aServ.getAccountById(tDTO.sourceAccountID);
		int currentUserID = Integer.parseInt(ses.getAttribute("userID").toString());
		int currentRole = Integer.parseInt(ses.getAttribute("role").toString());
		if(currentRole != 1 && currentUserID != srcByID.getOwner().getUserID()) {
			out = res.getWriter();
			out.print(unauthorized);
			System.out.println("source Acc & current Session acc weren't a match");
			res.setStatus(401);
			res.setContentType("application/json");
			return;
		}

		//security passed
		//update src_account withdraw tDTO.amount, 
		//then update target_account deposit tDTO.amount 
		srcByID.setBalance(srcByID.getBalance() - tDTO.amount);
		//if withdraw would make balance negative, cancel the withdraw entirely
		if(srcByID.getBalance() < 0.0) {
			JSONmessage insufficientFunds = new JSONmessage("insufficient funds; transfer cancelled");
			String cancelled = om.writeValueAsString(insufficientFunds);
			out = res.getWriter();
			out.print(cancelled);
			res.setStatus(401);
			res.setContentType("application/json");
			return;
		}
		Account targetByID = aServ.getAccountById(tDTO.targetAccountID);

		targetByID.setBalance(targetByID.getBalance() + tDTO.amount);
		AccountDTO updatedSrcDTO = new AccountDTO(srcByID.getAccountID(),srcByID.getBalance(),srcByID.getStatus().getStatusID(),srcByID.getType().getTypeID(),srcByID.getOwner().getUserID());
		aServ.updateAccount(updatedSrcDTO);

		AccountDTO updatedTargetDTO = new AccountDTO(targetByID.getAccountID(),targetByID.getBalance(),targetByID.getStatus().getStatusID(),targetByID.getType().getTypeID(),targetByID.getOwner().getUserID());
		aServ.updateAccount(updatedTargetDTO);

		JSONmessage successMsg = new JSONmessage("$"+tDTO.amount+" has been transfered from Account #"+ tDTO.sourceAccountID + " to Account #" + tDTO.targetAccountID);
		String deposited = om.writeValueAsString(successMsg);
		out.print(deposited);
		res.setStatus(200);
		res.setContentType("application/json");
	}

	public void createNewAccount(HttpServletRequest req, HttpServletResponse res) throws IOException{
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
		AccountDTO tempAcc = om.readValue(fullBodyContents, AccountDTO.class);


		//ensure that only admin, employee, & currentUserID = {:ownerID} requests this
		HttpSession ses = req.getSession(false);
		PrintWriter out = res.getWriter();
		JSONmessage unauthorizedMsg = new JSONmessage("The requested action is not permitted");
		String unauthorized = om.writeValueAsString(unauthorizedMsg);

		if(ses == null) {
			out = res.getWriter();
			System.out.println("No Active Sesssion in POST /accounts");
			out.print(unauthorized);
			res.setStatus(401);
			res.setContentType("application/json");
			return;
		} 
		
		int currentUserID = Integer.parseInt(ses.getAttribute("userID").toString());
		int currentRole = Integer.parseInt(ses.getAttribute("role").toString());
		if(currentRole != 1 && currentRole != 2 && currentUserID!=tempAcc.ownerID) {
			JSONmessage wrongPerson = new JSONmessage("You must be an admin, employee, or the accountHolder to create this account");
			String cancelAccCreate = om.writeValueAsString(wrongPerson);
			out = res.getWriter();
			out.print(cancelAccCreate);
			res.setStatus(400);
			res.setContentType("application/json");
			return;
		}
		
		//security passed
		//create new Account using AccountDTO
		if(aServ.addNewAccount(tempAcc)) {
			System.out.println("added NEW account for userID #: " +tempAcc.ownerID);
			List<Account> updatedAccount = aServ.getAccountsByUserId(tempAcc.ownerID);
			String updatedAccountJSON = om.writeValueAsString(updatedAccount);
			out.print(updatedAccountJSON);
			res.setStatus(201);
			res.setContentType("application/json");
		} else {
			res.setContentType("text/html");
			res.setStatus(401);
			out.print("<h1>Something went wrong in Account Creation</h1>");
			
		}
	}
}

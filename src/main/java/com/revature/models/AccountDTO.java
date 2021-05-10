package com.revature.models;
/*
 * DTO - Data Transfer Object
 * Temporary objects that are used to store info coming from outside our application,
 * if that information doesn't perfectly fit any already-existing ObjectType we made.
 * 
 * aka "this is an intermediary class to update/withdraw/deposit, compared to a full Account object"
 * 
 * We check this accountID against the current session/cookie.
 * If cookie.username == admin 
 * OR cookie.accountID == this.accountID
 * The withdraw/deposit succeeds
 */
public class AccountDTO {
	public int accountID;
	public double balance;
	public int statusID;
	public int typeID;
	public int ownerID;
	public double amount;
	
	public AccountDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	//for withdraw/deposit
	public AccountDTO(int accountID, double amount) {
		super();
		this.accountID = accountID;
		this.amount = amount;
	}



	//for create/update Account
	public AccountDTO(int accountID, double balance, int statusID, int typeID, int ownerID) {
		super();
		this.accountID = accountID;
		this.balance = balance;
		this.statusID = statusID;
		this.typeID = typeID;
		this.ownerID = ownerID;
		this.amount = 0;
	}



	public int getAccountID() {
		return accountID;
	}
	public void setAccountID(int accountID) {
		this.accountID = accountID;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public int getStatusID() {
		return statusID;
	}
	public void setStatusID(int statusID) {
		this.statusID = statusID;
	}
	public int getTypeID() {
		return typeID;
	}
	public void setTypeID(int typeID) {
		this.typeID = typeID;
	}
	public int getOwnerID() {
		return ownerID;
	}
	public void setOwnerID(int ownerID) {
		this.ownerID = ownerID;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
}

package com.revature.models;

public class Account {
	private int accountID;
	private double balance;
	private AccountStatus status;
	private AccountType type;
	private User owner;
	
	public Account() {
		super();
	}
	
	//the constructor WITH accountID is for retrieving from DB
	public Account(int accountID, double balance, AccountStatus status, AccountType type, User owner) {
		super();
		this.accountID = accountID;
		this.balance = balance;
		this.status = status;
		this.type = type;
		this.owner = owner;
	}

	//the constructor WITHOUT accountID is for writing a new object to DB
	public Account(double balance, AccountStatus status, AccountType type, User owner) {
		super();
		this.accountID = accountID;
		this.balance = balance;
		this.status = status;
		this.type = type;
		this.owner = owner;
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

	public AccountStatus getStatus() {
		return status;
	}

	public void setStatus(AccountStatus status) {
		this.status = status;
	}

	public AccountType getType() {
		return type;
	}

	public void setType(AccountType type) {
		this.type = type;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + accountID;
		long temp;
		temp = Double.doubleToLongBits(balance);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (accountID != other.accountID)
			return false;
		if (Double.doubleToLongBits(balance) != Double.doubleToLongBits(other.balance))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Account [" + accountID + ", balance=" + balance + ", \nstatus=" + status + ", \ntype=" + type
				+ ", \nowner=" + owner + "]";
	}
	
	
	

}

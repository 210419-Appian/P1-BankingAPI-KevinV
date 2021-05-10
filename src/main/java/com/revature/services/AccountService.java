package com.revature.services;

import java.util.List;

import com.revature.DAO.AccountDAO;
import com.revature.DAO.AccountDAOImpl;
import com.revature.models.Account;
import com.revature.models.AccountDTO;

public class AccountService {
	
	private AccountDAO accDAO = new AccountDAOImpl();

	public List<Account> getAllAccounts() {
		return accDAO.getAllAccounts();
	}

	public Account getAccountById(int id) {
		return accDAO.getAccountById(id);
	}

	public List<Account> getAccountsByStatus(int statusID) {
		return accDAO.getAccountsByStatusId(statusID);
	}

	public List<Account> getAccountsByUserId(int userID) {
		return accDAO.getAccountsByUserId(userID);
	}

	public boolean updateAccount(AccountDTO aDTO) {
		return accDAO.updateAccount(aDTO);
		
	}

	public boolean addNewAccount(AccountDTO tempAcc) {
		return accDAO.addAccount(tempAcc);
	}


}

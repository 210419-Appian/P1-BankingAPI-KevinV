package com.revature.DAO;

import java.util.List;

import com.revature.models.Account;
import com.revature.models.AccountDTO;

public interface AccountDAO {
	
	public void addAccount(Account a);
	
	public List<Account> getAllAccounts();
	public Account getAccountById(int id);
	public List<Account> getAccountsByStatusId(int statusid);
	public List<Account> getAccountsByUserId(int userID);
	
	public boolean updateAccount(AccountDTO aDTO);
	

}

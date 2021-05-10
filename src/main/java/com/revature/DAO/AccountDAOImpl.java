package com.revature.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.models.Account;
import com.revature.models.AccountDTO;
import com.revature.utils.ConnectionUtil;

public class AccountDAOImpl implements AccountDAO {
	
	private static AccountTypeDAO typeDAO = new AccountTypeDAOImpl();
	private static AccountStatusDAO statusDAO = new AccountStatusDAOImpl();
	private static UserDAO uDAO = new UserDAOImpl();

	@Override
	public void addAccount(Account a) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Account> getAllAccounts() {
		try(Connection conn = ConnectionUtil.getConnection()){

			String sql = "SELECT * from accounts ORDER BY accountid ASC;";

			Statement statement = conn.createStatement();
			ResultSet result = statement.executeQuery(sql);

			List<Account> list = new ArrayList<>();

			//resultSet's cursor means we can use result.next to return true and move the cursor
			while(result.next()) {
				//populate each Account Obj with columns from the database table
				Account a = new Account(
						result.getInt("accountID"), 
						result.getDouble("balance"), 
						null, //status
						null,	//type 
						null	//owner 
						);
				/*
				 * Handling FK <--> ObjectType field Conversion
				 * 	we get the whole objects with associated DAOs findByID(id)
				 */
				int status= result.getInt("status");
				a.setStatus(statusDAO.getAccStatusById(status));
				int type = result.getInt("type");
				a.setType(typeDAO.getAccTypeById(type));
				int owner = result.getInt("owner");
				a.setOwner(uDAO.getUserById(owner));
				
				//finally, add each Account to the List
				list.add(a);
			}
			return list;

		}catch(SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Account getAccountById(int id) {
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "SELECT * FROM accounts WHERE accountID = ?;";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			
			ResultSet result = stmt.executeQuery();


			List<Account> list = new ArrayList<>();

			//resultSet's cursor means we can use result.next to return true and move the cursor
			while(result.next()) {
				//populate each Account Obj with columns from the database table
				Account a = new Account(
						result.getInt("accountID"), 
						result.getDouble("balance"), 
						null, //status
						null,	//type 
						null	//owner 
						);
				/*
				 * Handling FK <--> ObjectType field Conversion
				 * 	we get the whole objects with associated DAOs findByID(id)
				 */
				int status= result.getInt("status");
				a.setStatus(statusDAO.getAccStatusById(status));
				int type = result.getInt("type");
				a.setType(typeDAO.getAccTypeById(type));
				int owner = result.getInt("owner");
				a.setOwner(uDAO.getUserById(owner));
				
				//finally, add each Account to the List
				list.add(a);
			}
			return list.get(0);

		}catch(SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public List<Account> getAccountsByStatusId(int statusid) {
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "SELECT * FROM accounts WHERE status = ?;";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, statusid);
			
			ResultSet result = stmt.executeQuery();


			List<Account> list = new ArrayList<>();

			//resultSet's cursor means we can use result.next to return true and move the cursor
			while(result.next()) {
				//populate each Account Obj with columns from the database table
				Account a = new Account(
						result.getInt("accountID"), 
						result.getDouble("balance"), 
						null, //status
						null,	//type 
						null	//owner 
						);
				/*
				 * Handling FK <--> ObjectType field Conversion
				 * 	we get the whole objects with associated DAOs findByID(id)
				 */
				int status= result.getInt("status");
				a.setStatus(statusDAO.getAccStatusById(status));
				int type = result.getInt("type");
				a.setType(typeDAO.getAccTypeById(type));
				int owner = result.getInt("owner");
				a.setOwner(uDAO.getUserById(owner));
				
				//finally, add each Account to the List
				list.add(a);
			}
			return list;

		}catch(SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public List<Account> getAccountsByUserId(int userID) {
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "SELECT * FROM accounts WHERE OWNER = ?;";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userID);
			
			ResultSet result = stmt.executeQuery();


			List<Account> list = new ArrayList<>();

			//resultSet's cursor means we can use result.next to return true and move the cursor
			while(result.next()) {
				//populate each Account Obj with columns from the database table
				Account a = new Account(
						result.getInt("accountID"), 
						result.getDouble("balance"), 
						null, //status
						null,	//type 
						null	//owner 
						);
				/*
				 * Handling FK <--> ObjectType field Conversion
				 * 	we get the whole objects with associated DAOs findByID(id)
				 */
				int status= result.getInt("status");
				a.setStatus(statusDAO.getAccStatusById(status));
				int type = result.getInt("type");
				a.setType(typeDAO.getAccTypeById(type));
				int owner = result.getInt("owner");
				a.setOwner(uDAO.getUserById(owner));
				
				//finally, add each Account to the List
				list.add(a);
			}
			return list;

		}catch(SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean updateAccount(AccountDTO a) {
		try(Connection conn = ConnectionUtil.getConnection()){

			String sql = "UPDATE accounts "
					+ "SET balance = ?,"
					+ "status = ?,"
					+ "type = ?,"
					+ "owner = ?"
					+ "WHERE accountid = ?;";
			int index = 0;
			
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setDouble(++index, a.getBalance());
			statement.setInt(++index, a.getStatusID());
			statement.setInt(++index, a.getTypeID());
			statement.setInt(++index, a.getOwnerID());
			
			statement.setInt(++index, a.getAccountID());
			
			statement.execute();
			return true;

		}catch(SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

}

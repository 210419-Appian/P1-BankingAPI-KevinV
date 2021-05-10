package com.revature.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.models.User;
import com.revature.models.UserDTO;
import com.revature.utils.ConnectionUtil;

public class UserDAOImpl implements UserDAO {

	private static RoleDAO rDAO = new RoleDAOImpl();

	@Override
	public boolean addUser(UserDTO a) {
		try(Connection conn = ConnectionUtil.getConnection()){

			String sql = "INSERT INTO users (username,password,fname,lname,email,roleID) VALUES (?,?,?,?,?,?);";
			
			int index = 0;
			
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(++index, a.getUsername());
			statement.setString(++index, a.getPassword());
			statement.setString(++index, a.fname);
			statement.setString(++index, a.lname);
			statement.setString(++index, a.email);
			statement.setInt(++index, a.roleID);
			
			statement.execute();
			return true;

		}catch(SQLException e) {
			e.printStackTrace();
		}

		return false;

	}

	@Override
	public List<User> getUsers() {
		try(Connection conn = ConnectionUtil.getConnection()){

			String sql = "SELECT * from users ORDER BY userid ASC;";

			Statement statement = conn.createStatement();
			ResultSet result = statement.executeQuery(sql);

			List<User> list = new ArrayList<>();

			//resultSet's cursor means we can use result.next to return true and move the cursor
			while(result.next()) {
				//populate each avenger Obj with columns from the database table
				User u = new User(
						result.getInt("userID"), 
						result.getString("username"), 
						result.getString("password"), 
						result.getString("fname"), 
						result.getString("lname"), 
						result.getString("email"),
						null	//get the Role obj separately
						);
				/*
				 * Handling FK <--> ObjectType field Conversion
				 * 	we get the whole Role object with roleDAO findByID(roleID)
				 */
				int roleID= result.getInt("roleID");
				u.setRole(rDAO.findRoleByID(roleID));
				
				//finally, add each User to the List
				list.add(u);
			}
			return list;

		}catch(SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public User getUserByUsername(String name) {
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "SELECT * FROM users WHERE username = ?;";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, name);
			
			ResultSet result = stmt.executeQuery();

			User user = new User();

			//resultSet's cursor means we can use result.next to return true and move the cursor
			while(result.next()) {
				//populate each avenger Obj with columns from the database table
				user.setUserID(result.getInt("userid"));
				user.setUsername(result.getString("username"));
				user.setPassword(result.getString("password"));
				user.setFname(result.getString("fname"));
				user.setLname(result.getString("lname"));
				user.setEmail(result.getString("email"));
				
				/*
				 * Handling FK <--> ObjectType field Conversion
				 * 	we get the whole Role object with roleDAO findByID(roleID)
				 */
				int roleID= result.getInt("roleID");
				user.setRole(rDAO.findRoleByID(roleID));
				
			}
			return user;

		}catch(SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public User getUserById(int id) {
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "SELECT * FROM users WHERE userID = ?;";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			
			ResultSet result = stmt.executeQuery();

			User user = new User();

			//resultSet's cursor means we can use result.next to return true and move the cursor
			while(result.next()) {
				//populate each avenger Obj with columns from the database table
				user.setUserID(result.getInt("userid"));
				user.setUsername(result.getString("username"));
				user.setPassword(result.getString("password"));
				user.setFname(result.getString("fname"));
				user.setLname(result.getString("lname"));
				user.setEmail(result.getString("email"));
				
				/*
				 * Handling FK <--> ObjectType field Conversion
				 * 	we get the whole Role object with roleDAO findByID(roleID)
				 */
				int roleID= result.getInt("roleID");
				user.setRole(rDAO.findRoleByID(roleID));
				
			}
			return user;

		}catch(SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean updateUser(User u) {
		try(Connection conn = ConnectionUtil.getConnection()){

			String sql = "UPDATE users "
					+ "SET username = ?,"
					+ "password = ?,"
					+ "fname = ?,"
					+ "lname = ?,"
					+ "email = ?, "
					+ "roleID = ?"
					+ "WHERE userID = ?;";
			int index = 0;
			
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(++index, u.getUsername());
			statement.setString(++index, u.getPassword());
			statement.setString(++index, u.getFname());
			statement.setString(++index, u.getLname());
			statement.setString(++index, u.getEmail());
			if(u.getRole()!= null) {
				statement.setInt(++index, u.getRole().getRoleID());
			} else {
				statement.setInt(++index, 3);				
			}
			statement.setInt(++index, u.getUserID());
			
			statement.execute();
			return true;

		}catch(SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean deleteUser(User u) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteUserByUsername(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteUserById(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<String> getAllUsernames() {
		try(Connection conn = ConnectionUtil.getConnection()){

			String sql = "SELECT username FROM users;";

			Statement statement = conn.createStatement();
			ResultSet result = statement.executeQuery(sql);

			List<String> list = new ArrayList<>();

			//resultSet's cursor means we can use result.next to return true and move the cursor
			while(result.next()) {
				//populate each username Obj with columns from the database table
				String s = result.getString("username");
				//finally, add each username to the List
				list.add(s);
			}
			return list;

		}catch(SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

}

package com.revature.services;

import java.util.List;

import com.revature.DAO.UserDAO;
import com.revature.DAO.UserDAOImpl;
import com.revature.models.User;
import com.revature.models.UserDTO;

public class UserService {
	
	private UserDAO uDAO = new UserDAOImpl();
	
	public User login(UserDTO u) {
		User userDB = uDAO.getUserByUsername(u.username);
		if(userDB != null && userDB.getPassword().equals(u.password)) {
			return userDB;
		}
		return null;
	}

	public List<User> getAllUsers() {
		return uDAO.getUsers();
	}

	public boolean updateUser(User u) {
		return uDAO.updateUser(u);
		
	}

	public User getUserByID(int userID) {
		return uDAO.getUserById(userID);
	}

	public User getUserByUserName(String username) {
		return uDAO.getUserByUsername(username);
	}

}

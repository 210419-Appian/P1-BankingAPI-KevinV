package com.revature.DAO;

import java.util.List;

import com.revature.models.User;
import com.revature.models.UserDTO;

public interface UserDAO {
	
	
	public boolean addUser(UserDTO tempUser);
	
	public List<User> getUsers();
	public User getUserByUsername(String name);
	public User getUserById(int id);
	
	public boolean updateUser(User u);
	
	public boolean deleteUser(User u);
	public boolean deleteUserByUsername(String name);
	public boolean deleteUserById(int id);

	public List<String> getAllUsernames();

}

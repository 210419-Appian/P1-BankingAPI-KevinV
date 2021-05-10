package com.revature.models;

/*
 * DTO - Data Transfer Object
 * Temporary objects that are used to store info coming from outside our application,
 * if that information doesn't perfectly fit any already-existing ObjectType we made.
 * 
 * aka "this is an intermediary class to login, compared to a full User object"
 * 
 * These kinds of classes don't care about Encapsulation -> public fields
 */
public class UserDTO {
	public String username;
	public String password;
	
	public UserDTO() {
		super();
	}
	public UserDTO(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}

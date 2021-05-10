package com.revature.models;
/*
 * DTO - Data Transfer Object
 * Temporary objects that are used to store info coming from outside our application,
 * if that information doesn't perfectly fit any already-existing ObjectType we made.
 * 
 * aka "this is an intermediary class to withdraw/deposit, compared to a full Account object"
 * 
 * We check this sourceAccountID against the current session/cookie.
 * If cookie.username == admin 
 * OR cookie.accountID == this.sourceAccountID
 * The transfer succeeds
 */
public class TransferDTO {
	public int sourceAccountID;
	public int targetAccountID;
	public double amount;

}

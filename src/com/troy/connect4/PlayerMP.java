package com.troy.connect4;

public class PlayerMP {
	
	private int id;
	private String username;
	
	public PlayerMP(int id, String username) {
		
		this.id = id;
		this.username = username;
	}

	public int getId() {
		return id;
	}
	
	public String getUsername() {
		return username;
	}

}

package com.troy.connect4.board;

public class Move {
	
	public int xCoord, color, yCoord;
	public float currentY = -1f, yVel = 0.0f;
	
	public Move(int xCoord, int yCoord, int color) {

		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.color = color;
	}
	
	public Move(Move other) {
		this.xCoord = other.xCoord;
		this.currentY = other.currentY;
		this.yVel = other.yVel;
	}

	public void move(float amount){
		yVel += amount;
		currentY += yVel;
	}
	
	

}

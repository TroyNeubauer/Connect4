package com.troy.connect4.board;

public class WinningMove {
	public int minX, minY, maxX, maxY, color;

	public WinningMove(int minX, int minY, int maxX, int maxY, int color) {
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
		this.color = color;
	}
	
	public String toString(){
		return "WinningMove minX " + minX + " minY " + minY + " maxX " + maxX + " maxY " + maxY + " color " + color;
	}
	
	

}

package com.troy.connect4.gamestate;

import java.awt.Graphics;

public interface GameState {
	
	public void onStart();

	public void update(float delta);
	
	public void render(Graphics g);
	
}

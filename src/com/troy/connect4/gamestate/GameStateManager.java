package com.troy.connect4.gamestate;

import java.awt.Graphics;

public class GameStateManager {
	
	private static GameState currentState;
	

	private GameStateManager() {
	}

	public static void init(GameState startingState) {
		setState(startingState);
	}

	public static void setState(GameState newState) {
		newState.onStart();
		currentState = newState;
	}
	
	public static void update(float delta){
		currentState.update(delta);
	}
	
	public static void render(Graphics g){
		currentState.render(g);
	}

}

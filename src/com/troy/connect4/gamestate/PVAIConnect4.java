package com.troy.connect4.gamestate;

import com.troy.connect4.board.Board;
import com.troy.troyberry.math.Maths;

public class PVAIConnect4 extends Connect4GameState {

	private float total = 0f, time;

	@Override
	public void updateSub(float delta) {
		time = Maths.randRange(0.2f, 4.5f);

		if (board.didRedMoveLast()) {
			total += delta;
			if (total > time) {
				total = 0;
				board.move(Maths.randRange(0, board.width - 1), Board.BLUE);
			}
		}
	}

	@Override
	public void onClick(int tile, boolean didLocalMove) {
		
	}

}

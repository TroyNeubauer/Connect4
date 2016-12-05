package com.troy.connect4.gamestate;

import com.troy.connect4.board.Board;

public class PVPOfflineConnect4 extends Connect4GameState {

	public void onStart() {
		super.onStart();
	}

	@Override
	public void updateSub(float delta) {
	}

	public void onClick(int tile, boolean didLocalMove) {
		if (board.didRedMoveLast() && !didLocalMove) {
			board.move(tile, Board.BLUE);
			
		}
	}

}

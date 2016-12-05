package com.troy.connect4.gamestate;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.troy.connect4.Main;
import com.troy.connect4.board.Board;
import com.troy.troyberry.java.input.KeyBoard;
import com.troy.troyberry.math.Maths;
import com.troy.troyberry.utils.ArrayUtil;
import com.troy.troyberry.utils.graphics.java.ui.*;

public abstract class Connect4GameState implements GameState {

	protected Board board;
	private boolean lastPress = true;
	int xOffset;
	private UI pauseUI;

	public Connect4GameState() {
		createUI();
		board = new Board(8, 7);
	}

	@Override
	public void onStart() {
		
	}

	@Override
	public void update(float delta) {
		if (KeyBoard.isKeyPressed(KeyEvent.VK_ESCAPE)) pauseUI.setVisible(!pauseUI.isVisible());
		board.update(delta);
		pauseUI.update(delta);
		xOffset = (Main.frame.getWidth() - Main.frame.getHeight()) / 2;
		boolean didLocalMove = false;
		int tile = -1;
		if (!lastPress && Mouse.isButtonDown(1)) {
			int width = Main.frame.getHeight() / board.width;
			tile = (Mouse.x - xOffset) / width;

			if (tile == 0) {
				tile = (Mouse.x - xOffset - width) / width;
			}
			if (tile >= 0 && tile < board.width) {
				didLocalMove = attemptLocalMove(tile);
				onClick(tile, didLocalMove);
			}
			
		}
		updateSub(delta);
		lastPress = Mouse.isButtonDown(1);
	}

	public abstract void updateSub(float delta);

	public abstract void onClick(int tile, boolean didLocalMove);

	private boolean attemptLocalMove(int tile) {
		if (board.didBlueMoveLast()) {
			board.move(tile, Board.RED);
			return true;
		}
		return false;
	}

	@Override
	public void render(Graphics g) {
		
		board.render(g, xOffset, 0, Main.frame.getHeight(), Main.frame.getHeight());
		pauseUI.render(g);
	}

	private void createUI() {
		pauseUI = new UI(Main.frame);
		pauseUI.setVisible(false);
		pauseUI.add(new UILabel(0.5, 0.2, "Pause Menu", 0, new Font("timesnewroman", Font.BOLD, 60)));

		UIButton btg = new UIButton(0.5, 0.4, 0.25, 0.11, "Back to Game", 0x3388ff, 0, 1.1, new Font("timesnewroman", Font.BOLD, 40));
		btg.setClickListener(new ClickListener() {

			@Override
			public void onClick() {
				pauseUI.setVisible(false);
				lastPress = true;
			}
		});
		pauseUI.add(btg);

		UIButton exit = new UIButton(0.5, 0.85, 0.25, 0.11, "Exit", 0x3388ff, 0, 1.1, new Font("timesnewroman", Font.BOLD, 40));
		exit.setClickListener(new ClickListener() {

			@Override
			public void onClick() {
				GameStateManager.setState(new TitleScreenState());
			}
		});
		pauseUI.add(exit);
	}

}

package com.troy.connect4.board;

import java.awt.*;
import java.util.*;
import java.util.List;

import com.troy.connect4.Main;
import com.troy.troyberry.utils.graphics.java.GraphicsUtil;
import com.troy.troyberry.utils.graphics.java.ui.Mouse;

public class Board {
	private static Font font = new Font("timesnewroman", Font.BOLD, 80);
	public static final int RED = 1, BLUE = 2, UN_TAKEN = 0, PRE_RED = RED + 3, PRE_BLUE = BLUE + 3;
	public static final int LENGTH_TO_WIN = 4;
	private List<Move> moves = new ArrayList<Move>();
	public final int width, height;
	private int[] board;
	private boolean blueMovedLast = true, tie = false;
	private List<WinningMove> winning = new ArrayList<WinningMove>();
	private LinkedList<Integer> movesHistory = new LinkedList<Integer>();

	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		this.board = new int[width * height];
	}

	public int getHeight(int x) {
		if (x < 0 || x >= width) return 0;
		for (int i = height - 1; i >= 0; i--) {
			int value = board[x + i * width];
			if (value == UN_TAKEN) return i;
		}

		return 0;
	}

	public void render(Graphics g, int xOffset, int yOffset, int boardWidth, int boardHeight) {
		int squareWidth = boardWidth / this.width;
		int squareHeight = boardHeight / this.width;
		for (int i = 0; i < board.length; i++) {
			int value = board[i];
			int x = (i % width) * squareWidth + xOffset;
			int y = (i / width) * squareHeight + yOffset;
			if (value == RED) g.setColor(Color.RED);
			else if (value == BLUE) g.setColor(Color.BLUE);
			else g.setColor(Color.black);
			if (value == RED || value == BLUE) g.fillOval(x + 1, y + 1, squareWidth - 2, squareHeight - 2);
			g.setColor(Color.black);
			g.drawRect(x, y, squareWidth, squareHeight);
		}
		for (Move m : moves) {
			if (m.color == RED) g.setColor(Color.red);
			else if (m.color == BLUE) g.setColor(Color.blue);
			g.fillOval(m.xCoord * squareWidth + 1 + xOffset, (int) (m.currentY * squareHeight) + 1, squareWidth - 2, squareHeight - 2);
		}
		if (winning.size() > 0) {
			int winner = 0;
			for (WinningMove m : winning) {
				g.setColor(Color.black);
				g.drawLine(m.minX * squareWidth + xOffset + squareWidth / 2, m.minY * squareHeight + squareHeight / 2,
						m.maxX * squareWidth + xOffset + squareWidth / 2, m.maxY * squareHeight + squareHeight / 2);

			}
			for (WinningMove m : winning) {
				g.setColor(Color.black);
				if (m.color != winner && winner != 0) {
					GraphicsUtil.drawCenteredString(g, "Both Players Win!", Main.frame.getWidth() / 2, Main.frame.getHeight() / 2, font);
				} else {
					GraphicsUtil.drawCenteredString(g, (m.color == RED ? "Red" : "Blue") + " Won", Main.frame.getWidth() / 2,
							Main.frame.getHeight() / 2, font);
				}
			}
		}
		if (tie) {
			GraphicsUtil.drawCenteredString(g, "Tie", Main.frame.getWidth() / 2, Main.frame.getHeight() / 2, font);
		}
	}

	private void checkTie() {
		int blanks = 0;
		for (int i = 0; i < board.length; i++) {
			if (board[i] == UN_TAKEN) blanks++;
		}
		if (blanks == 0 && winning.isEmpty()) {
			tie = true;
		}
	}

	public void update(float delta) {
		checkTie();
		Iterator<Move> i = moves.iterator();
		while (i.hasNext()) {
			Move m = i.next();
			m.move(delta * 0.1f);

			if (((int) m.currentY) >= m.yCoord) {
				set(m.xCoord, m.yCoord, m.color);
				checkWin(m.xCoord, m.yCoord, m.color);
				i.remove();
			}
		}
	}

	public void checkWin(int x, int y, int check) {
		int origionalX = x, origionalY = y;
		for (int y1 = -LENGTH_TO_WIN; y1 <= LENGTH_TO_WIN; y1++) {
			for (int x1 = -LENGTH_TO_WIN; x1 <= LENGTH_TO_WIN; x1++) {
				x = origionalX + x1;
				y = origionalY + y1;
				didWin(check, x, y, 1, 0);
				didWin(check, x, y, -1, 0);
				didWin(check, x, y, 0, 1);
				didWin(check, x, y, 0, -1);
				didWin(check, x, y, 1, 1);
				didWin(check, x, y, -1, -1);
				didWin(check, x, y, -1, 1);
				didWin(check, x, y, 1, -1);
			}
		}
	}

	private boolean didWin(int check, int x, int y, int rowDelta, int colDelta) {
		int origionalX = x, origionalY = y;
		boolean win = true;
		for (int count = 0; count < LENGTH_TO_WIN; count++) {
			if (x < width && x >= 0 && y < width && y >= 0) {
				if (x < 0 || x >= width || y < 0 || y >= height) {
					win = false;
					break;
				}
				int test = board[x + y * width];
				if (test != check) {
					win = false;
					break;
				}
			} else {
				win = false;
				break;
			}
			x += rowDelta;
			y += colDelta;
		}
		if (win) {
			winning.add(new WinningMove(origionalX, origionalY, rowDelta * (LENGTH_TO_WIN - 1) + origionalX,
					colDelta * (LENGTH_TO_WIN - 1) + origionalY, check));
		}
		return win;

	}

	public void set(int x, int y, int color) {
		if (x < 0 || x >= width || y < 0 || y >= height) return;
		board[x + y * width] = color;
	}

	public void printBoard() {
		for (int i = 0; i < width * height; i++) {
			switch (board[i]) {
			case RED:
				System.out.print("R ");
				break;
			case BLUE:
				System.out.print("B ");
				break;
			case UN_TAKEN:
				System.out.print("0 ");
				break;
			case PRE_RED:
				System.out.print("PR");
				break;
			case PRE_BLUE:
				System.out.print("PB");
				break;
			}
			if (((i + 1) % width) == 0) {
				System.out.println();
			}
		}
	}

	public void move(int x, int color) {
		int height = getHeight(x);
		if (height < 0) return;
		if (winning.size() > 0) return;
		set(x, height, (color == RED) ? PRE_RED : PRE_BLUE);
		moves.add(new Move(x, height, color));
		movesHistory.add(x);
		if (color == RED) blueMovedLast = false;
		else if (color == BLUE) blueMovedLast = true;
	}

	public void removeLastMove() {
		if (!movesHistory.isEmpty()) {
			int x = movesHistory.getLast();
			for (int i = 0; i < height; i++) {
				int color = board[x + i * width];
				if (color == PRE_RED || color == PRE_BLUE) {
					board[x + i * width] = UN_TAKEN;
					Iterator<Move> it = moves.iterator();
					while (it.hasNext()) {
						Move m = (Move) it.next();
						if (m.xCoord == x && m.yCoord == i) {
							it.remove();
						}
					}
					break;
				}
			}
		}
	}

	public boolean didBlueMoveLast() {
		return blueMovedLast;
	}

	public boolean didRedMoveLast() {
		return !blueMovedLast;
	}

	public void setTurn(int color) {
		if(color == RED){
			blueMovedLast = true;
		}else if(color == BLUE){
			blueMovedLast = false;
		}else{
			
		}
	}

}

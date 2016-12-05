package com.troy.connect4.gamestate;

import java.awt.Font;
import java.awt.Graphics;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.troy.connect4.Main;
import com.troy.connect4.PlayerMP;
import com.troy.connect4.board.Board;
import com.troy.troyberry.utils.ArrayUtil;
import com.troy.troyberry.utils.graphics.java.ui.UITextField;

public abstract class OnlineConnect4Base extends Connect4GameState {

	protected static Font bigFont = new Font("timesnewroman", Font.BOLD, 75);
	protected static Font smallFont = new Font("timesnewroman", Font.BOLD, 32);
	private static final int READ_BUFFER_SIZE = 1024;

	protected List<PlayerMP> otherPlayers = new ArrayList<PlayerMP>();
	protected boolean waitingForPlayer = true;
	protected int clientID = -1;
	protected Socket socket = null;
	protected String username;

	public void parsePacket(String message, byte[] data) {
		if (message.contains("I")) {
			clientID = Integer.parseInt(message.charAt(1) + "");
			boolean hasStartMove = message.charAt(2) == '1';
			boolean isPlaying = message.charAt(3) == '1';
			board.setTurn(hasStartMove ? Board.RED : Board.BLUE);
			otherPlayers.add(new PlayerMP(clientID, username + " (You)"));
			try {
				socket.getOutputStream().write(("I" + username).getBytes());
			} catch (Exception e) {
			}
		} else if (message.startsWith("M")) {
			int x = Integer.parseInt(message.charAt(1) + "");
			System.out.println("recieved move packet! Move is " + x);
			board.move(x, Board.BLUE);
		}

	}

	public void connect(InetAddress serverAddress, int port, String username) {
		new Thread(listen).start();
		try {
			socket = new Socket(serverAddress, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.username = username;

	}

	protected Runnable listen = () -> {
		while (true) {
			byte[] data = new byte[READ_BUFFER_SIZE];
			try {
				int count = socket.getInputStream().read(data);
				if (count < 1) continue;
			} catch (Exception e) {
			}

			if (ArrayUtil.isEmpty(data)) continue;

			String message = new String(data);

			parsePacket(message, data);
		}
	};

}

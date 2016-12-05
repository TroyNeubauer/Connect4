package com.troy.connect4.gamestate;

import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.troy.connect4.Main;
import com.troy.connect4.PlayerMP;
import com.troy.connect4.board.Board;
import com.troy.troyberry.utils.ArrayUtil;
import com.troy.troyberry.utils.graphics.java.GraphicsUtil;

public class PVPOnlineConnect4 extends OnlineConnect4Base {

	@Override
	public void updateSub(float delta) {

	}

	@Override
	public void render(Graphics g) {
		super.render(g);
		if (waitingForPlayer) {
			GraphicsUtil.drawCenteredString(g, "Waiting for opponent", Main.frame.getWidth() / 2, Main.frame.getHeight() / 4, bigFont);
		} else {
			if (board.didRedMoveLast()) g.setColor(Color.red);
			else g.setColor(Color.blue);
			g.setFont(smallFont);
			g.drawString((board.didRedMoveLast() ? "Opponents " : "Your") + "Turn", 10, 40);
		}
		int counter = 0;
		g.setFont(smallFont);
		g.setColor(Color.black);
		g.drawString("Online Players:", 10, 90);
		for (PlayerMP p : new ArrayList<>(otherPlayers)) {
			if (p.getUsername().contains("You")) g.setColor(Color.blue);
			else if (p.getId() != this.clientID && p.getId() < 3) g.setColor(Color.CYAN);
			else g.setColor(Color.yellow);
			g.drawString(p.getUsername(), 20, 130 + counter * 45);
			counter++;
		}
	}

	@Override
	public void onClick(int tile, boolean didLocalMove) {
		if (didLocalMove) {
			if (waitingForPlayer) {
				board.removeLastMove();
			}
		}
	}

	@Override
	public void parsePacket(String message, byte[] data) {
		if (message.startsWith("L")) {
			int otherId = Integer.parseInt(message.charAt(1) + "");
			message = new String(ArrayUtil.trimZeros(data));
			String otherUsername = message.substring(2, message.length());
			if (otherId != this.clientID && otherId < 3) {
				waitingForPlayer = false;
			}
			PlayerMP otherPlayer = new PlayerMP(otherId, otherUsername);
			otherPlayers.add(otherPlayer);

		}
		super.parsePacket(message, data);
	}

}

package com.troy.connect4.gamestate;

import java.awt.Color;
import java.awt.Graphics;
import java.util.*;

import com.troy.connect4.PlayerMP;
import com.troy.troyberry.utils.ArrayUtil;

public class PVPSpectatorConnect4 extends OnlineConnect4Base {

	private Comparator<PlayerMP> c = new Comparator<PlayerMP>() {

		@Override
		public int compare(PlayerMP n0, PlayerMP n1) {
			if (n0.getId() > n1.getId()) {
				return +1;
			} else if (n1.getId() > n0.getId()) {
				return -1;
			}
			return 0;
		}
	};

	@Override
	public void onStart() {

	}

	@Override
	public void update(float delta) {

	}

	@Override
	public void render(Graphics g) {
		super.render(g);

		int counter = 0;
		g.setFont(smallFont);
		g.setColor(Color.black);
		g.drawString("Online Players:", 10, 90);
		Collections.sort(otherPlayers, c);
		for (PlayerMP p : new ArrayList<>(otherPlayers)) {
			System.out.println(p.getId());
			if (p.getId() == 1) g.setColor(Color.red);
			else if (p.getId() == 2) g.setColor(Color.blue);
			else g.setColor(Color.yellow);
			g.drawString(p.getUsername(), 20, 130 + counter * 45);
			counter++;
		}
	}

	@Override
	public void updateSub(float delta) {

	}

	@Override
	public void onClick(int tile, boolean didLocalMove) {

	}

	@Override
	public void parsePacket(String message, byte[] data) {
		if (message.startsWith("L")) {
			int otherId = Integer.parseInt(message.charAt(1) + "");
			message = new String(ArrayUtil.trimZeros(data));
			String otherUsername = message.substring(2, message.length());
			PlayerMP otherPlayer = new PlayerMP(otherId, otherUsername);
			System.out.println("spectator adding player " + otherUsername + " id" + otherId);
			otherPlayers.add(otherPlayer);

		}
		super.parsePacket(message, data);
	}

}

package com.troy.connect4.gamestate;

import java.io.IOException;
import java.net.*;

import javax.swing.JOptionPane;

import com.troy.connect4.Main;

public class ConnectionManager {

	private static DatagramSocket socket;

	public static void connect(InetAddress serverAddress, int port, String username) {
		createSocket();
		byte[] data = "S".getBytes();
		DatagramPacket p = new DatagramPacket(data, data.length, serverAddress, port);
		try {
			for (int i = 0; i < 5; i++) {
				socket.send(p);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		boolean problem = false;
		byte[] replyData = new byte[16];
		DatagramPacket reply = new DatagramPacket(replyData, replyData.length);
		try {
			socket.receive(reply);
		} catch (IOException e) {
			e.printStackTrace();
			problem = true;
		}
		String message = new String(replyData);
		int playerCount = -1;
		try {
			playerCount = Integer.parseInt(message.charAt(0) + "");
		} catch (Exception e) {
			problem = true;
		}
		if (!problem) {
			OnlineConnect4Base newState = null;
			if (playerCount < 2) {
				newState = new PVPOnlineConnect4();
			} else {
				newState = new PVPSpectatorConnect4();
			}
			GameStateManager.setState(newState);
			newState.connect(serverAddress, port, username);
			
		} else {
			JOptionPane.showMessageDialog(Main.frame, "Unable to connect to the server " + serverAddress.getHostName() + ":" + port);
			GameStateManager.setState(new TitleScreenState());
		}
	}

	private static void createSocket() {
		if (socket == null) {
			try {
				socket = new DatagramSocket();
			} catch (SocketException e) {
				e.printStackTrace();
			}
		}
	}

	private ConnectionManager() {
	}

}

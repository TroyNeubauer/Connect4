package com.troy.connect4.gamestate;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.net.InetAddress;

import javax.swing.JOptionPane;

import com.troy.connect4.Main;
import com.troy.troyberry.java.input.KeyBoard;
import com.troy.troyberry.utils.graphics.java.GraphicsUtil;
import com.troy.troyberry.utils.graphics.java.ui.*;

public class TitleScreenState implements GameState {

	private static Font bigFont = new Font("timesnewroman", Font.BOLD, 50);
	private static Font font = new Font("timesnewroman", Font.PLAIN, 35);
	private static Font smallFont = new Font("timesnewroman", Font.PLAIN, 25);
	private static UI ui;
	private UITextField ip, port, username;

	@Override
	public void update(float delta) {
		ui.update(delta);
	}

	@Override
	public void render(Graphics g) {
		ui.render(g);
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onStart() {
		ui = new UI(Main.frame);
		ui.add(new UILabel(0.5, 0.1, "Connect 4", 0x0, bigFont));
		ui.add(new UILabel(0.5, 0.165, "By Troy Neubauer", 0x0, smallFont));
		UIButton spButton = new UIButton(0.5, 0.35, 0.3, 0.135, "Single Player", 0x3143e2, 0x0, 1.2, bigFont);
		spButton.setClickListener(new ClickListener() {

			@Override
			public void onClick() {
				spUi();
			}
		});
		ui.add(spButton);
		UIButton mpButton = new UIButton(0.5, 0.55, 0.3, 0.135, "Multi Player", 0x3143e2, 0x0, 1.2, bigFont);
		mpButton.setClickListener(new ClickListener() {

			@Override
			public void onClick() {
				mpUi();
			}
		});
		ui.add(mpButton);
		UIButton quit = new UIButton(0.5, 0.8, 0.25, 0.1, "Quit", 0x888888, 0x0, 1.2, bigFont);
		quit.setClickListener(new ClickListener() {

			@Override
			public void onClick() {
				System.exit(0);
			}
		});
		ui.add(quit);
	}

	private void mpUi() {
		ui = new UI(Main.frame);
		ui.add(new UILabel(0.5, 0.08, "Connect to a Server", 0x0, bigFont));
		ui.add(new UILabel(0.33, 0.35, "IP:", 0x0, smallFont));
		ip = new UITextField(0.5, 0.35, 0.285, 0.1, 0x0, 0xff00ff, bigFont, "eg 123.45.67.89");
		port = new UITextField(0.5, 0.5, 0.175, 0.1, 0x0, 0xff00ff, bigFont, "").setText("8123");
		username = new UITextField(0.5, 0.22, 0.3, 0.1, 0x0, 0xff00ff, bigFont, "Bob");
		ui.add(username);
		ui.add(new UILabel(0.3, 0.22, "Username:", 0x0, smallFont));
		ui.add(ip);
		ui.add(new UILabel(0.33, 0.5, "Port:", 0x0, smallFont));
		ui.add(port);
		ui.select(username);

		UIButton joinButton = new UIButton(0.5, 0.7, 0.3, 0.12, "Join", 0x0505ff, 0x0, 1.15, bigFont);
		joinButton.setClickListener(new ClickListener() {

			@Override
			public void onClick() {
				if(username.currentText.isEmpty()){
					JOptionPane.showMessageDialog(Main.frame,
							"Please enter a valid username");
					return;
				}
				InetAddress serverAddress = null;
				int portNumber = 0;
				try {
					serverAddress = InetAddress.getByName(ip.currentText);
					portNumber = Integer.parseInt(port.currentText);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(Main.frame,
							"Invalid Ip " + ip.currentText + ":" + port.currentText);
					return;
				}
				ConnectionManager.connect(serverAddress, portNumber, username.currentText);

			}
		});
		ui.add(joinButton);

		UIButton cancelButton = new UIButton(0.5, 0.85, 0.25, 0.1, "Cancel", 0x05ff05, 0x0, 1.15, font);
		cancelButton.setClickListener(new ClickListener() {

			@Override
			public void onClick() {
				onStart();
			}
		});
		ui.add(cancelButton);
	}

	private void spUi() {
		ui = new UI(Main.frame);
		ui.add(new UILabel(0.5, 0.15, "Choose a gamemode", 0, bigFont));

		UIButton spAI = new UIButton(0.5, 0.36, 0.3, 0.13, "Player vs AI", 0x00ffff, 0, 1.15, font);
		spAI.setClickListener(new ClickListener() {

			@Override
			public void onClick() {
				GameStateManager.setState(new PVAIConnect4());
			}
		});
		ui.add(spAI);

		UIButton spPvp = new UIButton(0.5, 0.55, 0.3, 0.13, "Player vs Player", 0x00ffff, 0, 1.15, font);
		spPvp.setClickListener(new ClickListener() {

			@Override
			public void onClick() {
				GameStateManager.setState(new PVPOfflineConnect4());
			}
		});
		ui.add(spPvp);

		UIButton cancelButton = new UIButton(0.5, 0.85, 0.25, 0.1, "Cancel", 0x05ff05, 0x0, 1.15, font);
		cancelButton.setClickListener(new ClickListener() {

			@Override
			public void onClick() {
				onStart();
			}
		});
		ui.add(cancelButton);
	}

}

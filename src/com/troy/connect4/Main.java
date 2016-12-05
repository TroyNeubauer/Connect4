package com.troy.connect4;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JFrame;

import com.troy.connect4.gamestate.*;
import com.troy.troyberry.java.input.KeyBoard;
import com.troy.troyberry.utils.general.CrashReport;
import com.troy.troyberry.utils.general.VersionManager;
import com.troy.troyberry.utils.graphics.java.ui.Mouse;

public class Main extends Canvas {

	public static AtomicBoolean running = new AtomicBoolean(false);
	public static Main main;
	public static JFrame frame;
	private static CrashReport crashReport;
	private static float frameTimeSeconds = 0.0f;
	private static long now, last, timer = 0L;
	private static int FPSCounter, currentFPS = 0;

	public Main(String[] args) {

		super();
	}

	public void loop() throws Exception {
		running.set(true);

		while (running.get()) {
			KeyBoard.update();
			GameStateManager.update(getFrameTimeSeconds());
			BufferStrategy bs = getBufferStrategy();
			Graphics g = bs.getDrawGraphics();
			g.setColor(Color.white);
			g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
			g.setColor(Color.black);
			{
				GameStateManager.render(g);
			}
			now = System.nanoTime();
			long difference = (now - last);
			timer += difference;
			frameTimeSeconds = difference / 1000000000f;
			FPSCounter++;
			if(timer > 1000000000L){
				timer = 0;
				currentFPS = FPSCounter;
				FPSCounter = 0;
			}
			
			bs.show();
			Toolkit.getDefaultToolkit().sync();
			g.dispose();
			last = now;
		}
	}

	public static float getFrameTimeSeconds() {
		return frameTimeSeconds;
	}

	public static void setupDisplay() throws Exception {
		last = System.nanoTime();
		frame = new JFrame();
		frame.setVisible(false);
		frame.setSize(1440, 810);
		frame.setResizable(true);
		frame.setTitle("Game");
		frame.setLocationRelativeTo(null);
		frame.setTitle(new Version().getWindowTitle());

		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setEnabled(true);
		frame.add(main);
		frame.addWindowListener(l);
		frame.setVisible(true);
		frame.requestFocus();
		frame.requestFocusInWindow();

		boolean hasSetUpGraphics = false;
		BufferStrategy bs = null;

		while (!hasSetUpGraphics) {
			bs = main.getBufferStrategy();
			if (bs == null) {
				main.createBufferStrategy(3);
			} else {
				hasSetUpGraphics = true;
			}
		}

	}

	public static void main(String[] args) {
		VersionManager.setVersion(new Version());

		try {
			main = new Main(args);
		} catch (Exception e) {
			crashReport = new CrashReport("Initalizing Game", e).print();
		}

		try {
			Main.setupDisplay();
		} catch (Exception e) {
			crashReport = new CrashReport("Setting Up Display", e).print();
		}

		try {
			GameStateManager.init(new TitleScreenState());
		} catch (Exception e) {
			crashReport = new CrashReport("Initalizing Game", e).print();
		}
		try {
			main.loop();
		} catch (Exception e) {
			crashReport = new CrashReport("Running Game Loop", e).print();
		}
		frame.dispose();
		System.exit(0);
	}

	private static WindowListener l = new WindowListener() {

		@Override
		public void windowOpened(WindowEvent e) {

		}

		@Override
		public void windowIconified(WindowEvent e) {

		}

		@Override
		public void windowDeiconified(WindowEvent e) {

		}

		@Override
		public void windowDeactivated(WindowEvent e) {

		}

		@Override
		public void windowClosing(WindowEvent e) {
			running.set(false);
		}

		@Override
		public void windowClosed(WindowEvent e) {

		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub

		}
	};
}
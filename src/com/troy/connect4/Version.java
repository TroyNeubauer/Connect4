package com.troy.connect4;

import com.troy.troyberry.utils.general.IVersion;

public class Version extends IVersion {
	
	public static final int PORT = 8123;

	public Version() {
		super("Connect 4 ", 0, 2, 0);
		this.addInfo("Alpha");
	}

}

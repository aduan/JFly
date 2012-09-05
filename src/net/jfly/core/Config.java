package net.jfly.core;

import net.jfly.config.Constants;

public class Config {
	private static final Constants constants = new Constants();

	public static Constants getConstants() {
		return constants;
	}

}

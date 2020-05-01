package org.gamestate;

import org.joml.Vector2f;

import engine.font.newrenderer.TextMaster;
import engine.gamestate.*;

public class MainGameState implements IGameState {

	@Override
	public void Update() {

	}

	@Override
	public void Stop() {

	}

	@Override
	public void Init() {
		TextMaster.CreateText("Utsaah",
				"In this topic, we will learn how to add a char to the beginning, middle and end of a string in Java.\r\n"
						+ "\r\n"
						+ "for insert, a char at the beginning and end of the string the simplest way is using string.\r\n"
						+ "\r\n" + "Add a character to a string in the beginning\r\n"
						+ "for insert, in the beginning, we can just use additional operation.\r\n" + "Example:\r\n"
						+ "\r\n" + "char ch='A';\r\n" + "String str=\"pple\";\r\n"
						+ "str= ch+str;   // str will change to \"Apple\"\r\n"
						+ "For insert a char at the end of a string also we can use additional operation.\r\n \t"
						+ "Example:\r\n" + "\r\n" + "",
				new Vector2f(), 2, 1, 20, 100);
	}
}

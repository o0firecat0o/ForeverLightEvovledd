package org.gamestate;

import org.joml.Vector2f;

import engine.font.newrenderer.StringObject;
import engine.font.newrenderer.TextMaster;
import engine.gamestate.*;
import engine.math.Mathf;

public class MainGameState implements IGameState {

	@Override
	public void Update() {

	}

	@Override
	public void Stop() {

	}

	@Override
	public void Init() {
		StringObject stringObject = TextMaster.CreateText("Utsaah",
				"UPDATE 15.5\n" + "- New \r#fffffftitle screen\n" + "- Trade system overhauled\n"
						+ "- Caste System re\bquires a civic now\n" + "- Galaxy generation tweaked\n"
						+ "- Housing \rreduced\n" + "- many bugfixes\n" + "- localizat\bion improvements\n"
						+ "- compatibility improvements",
				new Vector2f(), 0, 1, 20, 100);
		TextMaster.amendToText(stringObject, "\nHello");

	}
}

package org.gamestate;

import org.joml.Vector2f;

import engine.font.newrenderer.StringObject;
import engine.font.newrenderer.TextMaster;
import engine.font.newrenderer.TextRenderer;
import engine.gamestate.*;
import engine.math.Mathf;
import engine.object.GameObject;

public class MainGameState implements IGameState {

	@Override
	public void Update() {
		gameObject.transform.setRotation(gameObject.transform.getRotation() + 0.2f);
	}

	@Override
	public void Stop() {

	}

	GameObject gameObject;

	@Override
	public void Init() {
		gameObject = new GameObject();
		String text = "UPDATE 15.5\n" + "- New \r#fffffftitle screen\n" + "- Trade system overhauled\n"
				+ "- Caste System re\bquires a civic now\n" + "- Galaxy generation tweaked\n" + "- Housing \rreduced\n"
				+ "- many bugfixes\n" + "- localizat\bion improvements\n" + "- compatibility improvements";
		gameObject.AddComponent(new TextRenderer("Utsaah", text, 1, 20, 100));

	}
}

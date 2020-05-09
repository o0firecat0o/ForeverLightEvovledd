package org.gamestate;

import engine.font.newrenderer.TextRenderer;
import engine.gamestate.IGameState;
import engine.object.GameObject;

public class MainGameState implements IGameState {

	@Override
	public void Update() {
		textrenderer.setText(textrenderer.getText() + "a");
	}

	@Override
	public void Stop() {

	}

	TextRenderer textrenderer;

	@Override
	public void Init() {
		GameObject gameObject = new GameObject();
		textrenderer = gameObject.AddComponent(new TextRenderer("Hello world"));

	}
}

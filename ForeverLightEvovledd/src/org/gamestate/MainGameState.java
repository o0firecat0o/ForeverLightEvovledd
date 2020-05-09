package org.gamestate;

import org.joml.Vector2f;

import engine.font.newrenderer.StringObject;
import engine.font.newrenderer.TextMaster;
import engine.font.newrenderer.TextRenderer;
import engine.gamestate.*;
import engine.math.Mathf;
import engine.object.GameObject;
import engine.object.UIObject;
import engine.object.UIObject.UIPositions;

public class MainGameState implements IGameState {

	@Override
	public void Update() {

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

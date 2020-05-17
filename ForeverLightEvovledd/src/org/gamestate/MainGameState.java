package org.gamestate;

import org.joml.Vector2i;
import org.joml.Vector3f;

import engine.component.Camera;
import engine.component.graphic.Texture;
import engine.component.graphic.spriteRendererComponent.DefaultRender;
import engine.gamestate.IGameState;
import engine.input.InputKey;
import engine.main.Render;
import engine.object.GameObject;

public class MainGameState implements IGameState {

	@Override
	public void Update() {
		System.out.println(Camera.MAIN.WorldToScreenPosition(gameObject2.transform.getPositionVector2f()));
		// System.out.println(Camera.MAIN.InputMousePositionV2f());
		// System.out.println(InputMousePos.ScreenPos);
		if (InputKey.OnKeysDown(290)) {
			Render.setWindowSize(new Vector2i(1024, 768));
		}
		if (InputKey.OnKeysDown(291)) {
			Render.setWindowSize(new Vector2i(1280, 720));
		}
		if (InputKey.OnKeysDown(292)) {
			Render.setWindowSize(new Vector2i(1600, 900));
		}
		if (InputKey.OnKeysDown(293)) {
			Render.setWindowSize(new Vector2i(1920, 1080));
		}
	}

	@Override
	public void Stop() {

	}

	GameObject gameObject2;

	@Override
	public void Init() {

		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				GameObject gameObject = new GameObject();
				gameObject.transform.position.set(i * 100, j * 100, 5);
				gameObject.AddComponent(new DefaultRender().SetTexture(Texture.getTexture("Block1")));
			}
		}

		gameObject2 = new GameObject();
		gameObject2.AddComponent(new DefaultRender().SetTexture(Texture.getTexture("Block5")));
		gameObject2.transform.setPosition(new Vector3f(-600, -600, 6));
	}
}

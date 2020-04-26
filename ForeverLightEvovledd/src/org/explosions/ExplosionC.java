package org.explosions;

import engine.component.graphic.*;
import engine.component.graphic.spriteRendererComponent.DefaultRender;
import engine.main.Render;
import engine.object.Component;
import engine.random.Random;

//TODO: this class is unused?
public class ExplosionC extends Component {

	int timer = 0;

	@Override
	protected void Update() {
		timer++;
		if (timer > 600) {
			gameObject.InitDestroy();
		}
	}

	@Override
	protected void Start() {
		gameObject.AddComponent(new SpriteRenderer())
				.addSpriteRendererComponent(new DefaultRender().SetFrameBuffer(Render.mainFrameBuffer));
		gameObject.GetComponent(SpriteRenderer.class).SetTexture(Texture.getTexture("Red"));
		gameObject.transform.rotation = Random.RandomFloat(0f, (float) Math.PI * 2);
		gameObject.transform.setScale(2);
	}

	public ExplosionC() {
		super();
	}

}

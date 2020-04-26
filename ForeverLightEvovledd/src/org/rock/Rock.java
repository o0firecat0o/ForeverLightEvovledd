package org.rock;

import org.joml.Vector2f;

import engine.component.graphic.*;
import engine.component.graphic.spriteRendererComponent.DefaultRender;
import engine.component.physic.*;
import engine.object.Component;

public class Rock extends Component {

	@Override
	protected void Update() {

	}

	@Override
	protected void Start() {

		gameObject.transform.setScale(4);

		// add the sprite renderer
		gameObject.AddComponent(new SpriteRenderer()).addSpriteRendererComponent(new DefaultRender());

		gameObject.GetComponent(SpriteRenderer.class).SetTexture(Texture.getTexture("meteorite1"));

		gameObject.AddComponent(new RigidBody());
		// add the rigidBody
		RigidFixutre rFixutre;

		// TODO: make the rock convex, visually
		Vector2f[] vertices = new Vector2f[5];

		vertices[0] = new Vector2f(13, 92);
		vertices[1] = new Vector2f(23, 21);
		vertices[2] = new Vector2f(121, 24);
		vertices[3] = new Vector2f(143, 107);
		vertices[4] = new Vector2f(53, 134);

		rFixutre = new RigidFixutre(vertices, new Vector2f(160, 160),
				new Vector2f(gameObject.transform.getScale().x / 2, gameObject.transform.getScale().y / 2),
				gameObject.GetComponent(RigidBody.class), new Vector2f(), 0);

		gameObject.GetComponent(RigidBody.class).setCategoryBits(EntityCategory.ROCK);
		gameObject.GetComponent(RigidBody.class).setMaskBits(EntityCategory.ROCK);
		gameObject.GetComponent(RigidBody.class).addMaskBits(EntityCategory.TEAM1);
		gameObject.GetComponent(RigidBody.class).addMaskBits(EntityCategory.TEAM2);
	}
}

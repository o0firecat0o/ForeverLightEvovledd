package org.explosions;

import org.joml.*;

import engine.component.graphic.*;
import engine.component.graphic.instancedRendering.particle.*;
import engine.main.Render;
import engine.math.Mathf;
import engine.object.Component;
import engine.random.Random;

public class ExplosionB extends Component {

	@Override
	protected void Update() {
		gameObject.transform.setScale(gameObject.transform.getScale().mul(0.95f));
		if (gameObject.transform.getScale().x <= 0) {
			gameObject.InitDestroy();
		}
	}

	@Override
	protected void Start() {
		gameObject.AddComponent(new SpriteRenderer()).addSpriteRendererComponent(new ParticleRenderer() {

			int timer = 0;

			@Override
			public void Start() {
				super.Start();

				for (int i = 0; i < 50; i++) {
					Particle particle = new engine.component.graphic.instancedRendering.particle.Particle() {

						Vector2f acceleartion = Mathf.Vector2fFromAngle(Random.RandomFloat(-3.14f, 3.14f));

						@Override
						public void Update() {

							transform.addPosition(acceleartion);
							if (timer > 300) {
								transform.setScale(transform.getScale().add(new Vector2f(-0.01f, -0.01f)));
							} else {
								transform.setScale(transform.getScale().add(new Vector2f(0.01f, 0.01f)));
							}

							if (transform.getScale().x < 0) {
								Destroy();
							}
						}
					};

					particle.color = new Vector4f(0, 1, 1, 1);
					particle.transform.position.set(gameObject.transform.position);

					Particles.add(particle);
				}
				SetFrameBuffer(Render.finalBlur);
			}

			@Override
			public void CustomUpdate() {
				timer++;
				if (Particles.size() == 0) {
					gameObject.InitDestroy();
				}
			}
		});
	}

	public ExplosionB() {
		super();
	}

}

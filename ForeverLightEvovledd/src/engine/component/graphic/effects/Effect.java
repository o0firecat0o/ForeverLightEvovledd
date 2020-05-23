package engine.component.graphic.effects;

import org.joml.Vector2f;
import org.joml.Vector4f;

import engine.component.graphic.instancedRendering.particle.Particle;
import engine.component.graphic.instancedRendering.particle.ParticleRenderer;
import engine.main.Render;
import engine.math.Mathf;
import engine.object.GameObject;

public class Effect {

	/**
	 * Add a tail to gameObject
	 * 
	 * @param spriteRenderer
	 * @param TextureOveride          enter -1 if overriding is NOT intended
	 * @param number_of_interpolation 0 if you dont want any, 1 if you want 1 in
	 *                                between, etc
	 */
	public static void Tail(GameObject gameObject, int Texture_Overide, int number_of_interpolation,
			Vector4f projectileColor) {
		gameObject.AddComponent(new ParticleRenderer() {

			// Previous position of the gameObject
			Vector2f PreviousPosition;
			Vector2f CurrentPosition = new Vector2f();

			@Override
			public void CustomUpdate() {
				// Update the previous position and the current position

				// TODO: make it more efficient: not checking the thing each
				// time

				// if it is the first frame
				if (PreviousPosition == null) {
					// prevent previous position stuck at 0,0,0
					PreviousPosition = gameObject.transform.getPositionVector2f();
				} else {
					PreviousPosition = CurrentPosition;
				}
				CurrentPosition = gameObject.transform.getPositionVector2f();

				for (int i = 0; i < number_of_interpolation + 1; i++) {
					Vector2f position_of_particle = Mathf.interpolation(PreviousPosition, CurrentPosition,
							(1f / (number_of_interpolation + 1)) * i);

					// Start making the particles
					Particle particle = new Particle() {

						@Override
						public void Update() {
							if (transform.getScale().x <= 0 || transform.getScale().y <= 0) {
								Particles.remove(this);
							}
							transform.setScale(
									new Vector2f(transform.getScale().x - 0.01f, transform.getScale().y - 0.01f));
						}
					};
					particle.color = projectileColor;

					Particles.add(particle);
					particle.transform.setPosition(position_of_particle);
					particle.transform.setPosition(-4f);
					particle.transform.rotation = gameObject.transform.rotation;
					particle.transform.setScale(gameObject.transform.getScale());
				}

			}

			@Override
			public void Start() {
				super.Start();

				SetFrameBuffer(Render.mainFrameBuffer);
				SetFrameBuffer(Render.glowFrameBuffer);
			}
		});
	}
}

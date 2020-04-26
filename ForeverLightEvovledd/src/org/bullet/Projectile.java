package org.bullet;

import org.explosions.ExplosionA;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import org.joml.*;

import engine.component.graphic.*;
import engine.component.graphic.effects.Effect;
import engine.component.graphic.spriteRendererComponent.DefaultRender;
import engine.component.physic.*;
import engine.main.Render;
import engine.object.*;

public class Projectile extends Component implements IContact {
	public int Team;
	protected Vector2f acceleration;
	protected Vec2 initialVelocity;
	protected int bulletTexture;
	protected Vector2f bulletSize;
	protected int disappearTimer;
	protected float damage;

	/**
	 * Create a new projectile; Add it into a gameObject; DO NOT need rigidbody/
	 * rigidfixture/ spriteRenderer class, they are included
	 * 
	 * @param acceleration
	 *            The acceleration of the bullet after shooting
	 * @param Team
	 *            the team of the bullet
	 * @param initialVelocity
	 *            the initial velocity, should be same as ship velocity
	 * @param bulletTexture
	 * @param bulletSize
	 *            in diameter
	 */
	public Projectile(Vector2f acceleration, int Team, Vec2 initialVelocity, int bulletTexture, Vector2f bulletSize,
			int disappearTimer) {
		this.acceleration = acceleration;
		this.Team = Team;
		this.initialVelocity = initialVelocity;
		this.bulletTexture = bulletTexture;
		this.bulletSize = bulletSize;
		this.disappearTimer = disappearTimer;
	}

	@Override
	public void Start() {
		gameObject.transform.setScale(bulletSize);
		gameObject.AddComponent(new RigidBody());
		RigidFixutre rigidFixutre = gameObject
				.AddComponent(new RigidFixutre(ShapeType.circle, new Vector2f(bulletSize.x / 2, bulletSize.y / 2),
						gameObject.GetComponent(RigidBody.class), new Vector2f(), 0));
		gameObject.GetComponent(RigidBody.class).setTrigger(true);
		if (Team == 1) {
			gameObject.GetComponent(RigidBody.class).setCategoryBits(EntityCategory.TEAM1BULLET);
			gameObject.GetComponent(RigidBody.class).setMaskBits(EntityCategory.TEAM2);
			gameObject.GetComponent(RigidBody.class).addMaskBits(EntityCategory.TEAM2SHIELD);
		} else if (Team == 2) {
			gameObject.GetComponent(RigidBody.class).setCategoryBits(EntityCategory.TEAM2BULLET);
			gameObject.GetComponent(RigidBody.class).setMaskBits(EntityCategory.TEAM1);
			gameObject.GetComponent(RigidBody.class).addMaskBits(EntityCategory.TEAM1SHIELD);
		}

		// set the initial velocity
		gameObject.GetComponent(RigidBody.class).setVelocity(initialVelocity);

		rigidFixutre.RegisterContactCallBack(this);
		gameObject.AddComponent(new SpriteRenderer()).SetTexture(bulletTexture)
				.addSpriteRendererComponent(new DefaultRender().SetFrameBuffer(Render.mainFrameBuffer));
		// set the color of the projectile itself
		gameObject.GetComponent(SpriteRenderer.class).getSpriteRendererComponent(DefaultRender.class).Color.set(0.6f,
				0.6f, 1f);

		Effect.Tail(gameObject.GetComponent(SpriteRenderer.class), Texture.getTexture("BlurDot"), 3,
				new Vector4f(0.6f, 0.2f, 1f, 1f));

	}

	int timer = 0;

	@Override
	protected void Update() {
		Movement();
		timer++;
		if (timer > disappearTimer) {
			gameObject.InitDestroy();
		}
	}

	protected void Movement() {
		gameObject.GetComponent(RigidBody.class).AddForce(acceleration);
	}

	@Override
	protected void Destroy() {
		super.Destroy();
	}

	protected void Explosion(Vector2f location) {
		// explosion effect
		GameObject explosionGO = new GameObject();
		explosionGO.transform.setPosition(location);
		explosionGO.AddComponent(new ExplosionA());
	}

	@Override
	public void Contact(org.jbox2d.dynamics.contacts.Contact contact, Fixture target) {
		RigidFixutre rFixutre = (RigidFixutre) target.m_userData;

		if (rFixutre.gameObject.HasComponentExtendedFromInterface(IDamageable.class)) {
			((IDamageable) rFixutre.gameObject.GetComponentExtendedFromInterface(IDamageable.class)).applyDamage(10);

			Explosion(gameObject.transform.getPositionVector2f());

			gameObject.InitDestroy();
		}
	}
}

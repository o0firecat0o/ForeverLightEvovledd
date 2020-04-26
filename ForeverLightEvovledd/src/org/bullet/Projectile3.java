package org.bullet;

import org.explosions.ExplosionB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import org.joml.Vector2f;
import org.ship.ShipBlockComponent;

import engine.component.physic.*;
import engine.math.Mathf;
import engine.object.GameObject;

//tacticle missle
public class Projectile3 extends Projectile implements IContact {

	GameObject target;
	private float range = 40;

	public Projectile3(Vector2f acceleration, int Team, Vec2 initialVelocity, int bulletTexture, Vector2f bulletSize,
			int disappearTimer) {
		super(acceleration, Team, initialVelocity, bulletTexture, bulletSize, disappearTimer);
	}

	@Override
	protected void Movement() {
		gameObject.GetComponent(RigidBody.class)
				.AddForce(Mathf.Vector2fFromAngle(gameObject.transform.rotation).mul(10));

	}

	@Override
	protected void Update() {
		super.Update();
		if (timer % 30 == 0) {
			if (target == null || !GameObject.Exist(target)) {
				target = findTarget();
			}
		}
		rotation();
	}

	private GameObject findTarget() {
		// find all the nearby fixtures
		Fixture[] nearbyFixtures;
		if (Team == 1) {
			nearbyFixtures = Physics.OverlapCircleBasic(gameObject.transform.position, range,
					EntityCategory.TEAM2.getValue());
		} else {
			nearbyFixtures = Physics.OverlapCircleBasic(gameObject.transform.position, range,
					EntityCategory.TEAM1.getValue());
		}

		// check if there exist any nearby fixture
		if (nearbyFixtures.length == 0) {
			return null;
		} else {
			// if there is nearby fixture, return the one that have
			// shipblockComponent attached to it
			for (int i = 0; i < nearbyFixtures.length; i++) {
				RigidFixutre rFixutre = (RigidFixutre) nearbyFixtures[i].getUserData();
				if (rFixutre.gameObject.HasComponentExtendedFromClass(ShipBlockComponent.class)) {
					return rFixutre.gameObject;
				}
			}
			return null;
		}
	}

	@Override
	protected void Explosion(Vector2f location) {
		// explosion effect
		GameObject explosionGO = new GameObject();
		explosionGO.transform.setPosition(gameObject.transform.getPositionVector2f()
				.add(Mathf.Vector2fFromAngle(gameObject.transform.rotation).mul(200)));
		explosionGO.AddComponent(new ExplosionB());
	}

	/**
	 * Calculate the rotation offset, in order for the turrent to face the right
	 * direction
	 */
	private void rotation() {
		if (target == null) {
			return;
		}
		float TargetAngle = Mathf
				.AngleFromVector2f(target.transform.getPositionVector2f().sub(gameObject.transform.getPositionVector2f()));
		float RequiredRotation = Mathf.returnDirection(gameObject.transform.getRotation(), TargetAngle);

		if (RequiredRotation > 0) {
			gameObject.GetComponent(RigidBody.class).setRotation((float) (gameObject.transform.rotation - 0.01));
		}

		if (RequiredRotation < 0) {
			gameObject.GetComponent(RigidBody.class).setRotation((float) (gameObject.transform.rotation + 0.01));
		}
	}
}

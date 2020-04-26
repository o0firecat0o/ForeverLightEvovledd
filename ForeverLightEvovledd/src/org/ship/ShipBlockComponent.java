package org.ship;

import org.bullet.IDamageable;
import org.component.CircularHealthBar;
import org.jbox2d.dynamics.Fixture;
import org.joml.*;

import engine.component.graphic.*;
import engine.component.graphic.spriteRendererComponent.DefaultRender;
import engine.component.physic.*;
import engine.math.Mathf;
import engine.object.*;
import engine.timer.*;

public class ShipBlockComponent extends Component implements IDamageable {

	public final ShipBlockData shipBlockData;
	public final ShipComponent shipComponent;
	private GameObject HealthBarObject;

	public GameObject target = null;
	protected float range = 25f;
	public Timer timer;

	public ShipBlockComponent(ShipBlockData shipBlockData, ShipComponent shipComponent) {
		super();
		this.shipBlockData = shipBlockData;
		this.shipComponent = shipComponent;
	}

	@Override
	protected void Update() {
		// if the target gameObject has already been destroyed, then set the
		// target reference back to null
		if (target != null && !UpdatableObject.Exist(target)) {
			target = null;
		}

		if (HealthBarObject != null) {
			HealthBarObject.transform.setPosition(gameObject.transform.position);
		}

	}

	@Override
	protected void Destroy() {
		HealthBarObject.InitDestroy();
		HealthBarObject = null;
		target = null;
		if (timer != null) {
			timer.Destroy();
		}
		super.Destroy();
	}

	/**
	 * the function which the shipBlock is killed;
	 */
	public void Die() {
		shipComponent.shipData.CalculateTotalMass();
		gameObject.InitDestroy();
	}

	/**
	 * return the team of this shipObject
	 */
	protected int Team() {
		return shipComponent.shipData.Team;
	}

	@Override
	protected void Start() {
		// set the scale of the gameObject;
		gameObject.transform.setScale(new Vector2f(shipBlockData.size, shipBlockData.size));
		// add the sprite renderer
		gameObject.AddComponent(new SpriteRenderer()).addSpriteRendererComponent(new DefaultRender());

		// set the texture
		String texture = "Block" + shipBlockData.type;
		gameObject.GetComponent(SpriteRenderer.class).SetTexture(Texture.getTexture(texture));

		// calculate the real coordinate, for example, block 2 will offset by
		// 0.5;
		Vector2f realCoordinate = shipBlockData.Coordinate();
		if (shipBlockData.size % 2 == 0) {
			realCoordinate.add(new Vector2f(0.5f, 0.5f));
		}

		// add the physic rigidFixture
		RigidFixutre rFixutre;
		switch (shipBlockData.type) {
		case 15:
			rFixutre = new RigidFixutre(ShapeType.triangle,
					new Vector2f(shipBlockData.size * 0.5f, shipBlockData.size * 0.5f),
					shipComponent.gameObject.GetComponent(RigidBody.class), new Vector2f(realCoordinate.mul(100)),
					shipBlockData.rotation);
			break;
		default:
			rFixutre = new RigidFixutre(ShapeType.box,
					new Vector2f(shipBlockData.size * 0.5f, shipBlockData.size * 0.5f),
					shipComponent.gameObject.GetComponent(RigidBody.class), new Vector2f(realCoordinate.mul(100)),
					shipBlockData.rotation);
			break;
		}
		gameObject.AddComponent(rFixutre);

		// set the parent rigidbody entity/ mask
		// component is made?
		if (shipComponent.shipData.Team == 1) {
			gameObject.GetComponent(RigidFixutre.class).setCategoryBits(EntityCategory.TEAM1);
			gameObject.GetComponent(RigidFixutre.class).setMaskBits(EntityCategory.TEAM2BULLET);
			gameObject.GetComponent(RigidFixutre.class).addMaskBits(EntityCategory.TEAM2);
		} else if (shipComponent.shipData.Team == 2) {
			gameObject.GetComponent(RigidFixutre.class).setCategoryBits(EntityCategory.TEAM2);
			gameObject.GetComponent(RigidFixutre.class).setMaskBits(EntityCategory.TEAM1BULLET);
			gameObject.GetComponent(RigidFixutre.class).addMaskBits(EntityCategory.TEAM1);
		}
		// add the helper class back so it can be detected by helper physic
		// overlay
		gameObject.GetComponent(RigidFixutre.class).addMaskBits(EntityCategory.HELPER);
		// add the rock class so it can collide with rock
		gameObject.GetComponent(RigidFixutre.class).addMaskBits(EntityCategory.ROCK);

		// add circular health bar
		HealthBarObject = new GameObject();
		HealthBarObject.AddComponent(new CircularHealthBar(shipBlockData.MaxHealth, shipBlockData.getCurrentHealth()));
		HealthBarObject.GetComponent(CircularHealthBar.class).setColor(new Vector3f(0.2f, 0.6f, 0.3f));
		HealthBarObject.transform.setScale(shipBlockData.size);
	}

	@Override
	public void applyDamage(float Damage) {
		shipBlockData.setCurrentHealth(shipBlockData.getCurrentHealth() - Damage);
		HealthBarObject.GetComponent(CircularHealthBar.class).setCurValue(shipBlockData.getCurrentHealth());
		if (shipBlockData.getCurrentHealth() <= 0) {
			Die();
		}
	}

	/**
	 * start finding target and storing them
	 */
	public void setFindTarget(boolean set) {
		if (set) {
			timer = new Timer(60, new ITimerFunction() {
				@Override
				public void run() {
					// find the target if target is null
					if (target == null) {
						target = findRandomTarget(range);
					}
					// remove the target reference if the target is out of range
					else if (Mathf.Distance(target, gameObject) > range * 100) {
						target = null;
					}
				}
			});
		} else {
			timer.Destroy();
			target = null;
		}
	}

	public <T extends ShipBlockComponent> GameObject findRandomTarget(float range) {
		// find all the nearby fixtures
		Fixture[] nearbyFixtures;
		if (shipComponent.shipData.Team == 1) {
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

}

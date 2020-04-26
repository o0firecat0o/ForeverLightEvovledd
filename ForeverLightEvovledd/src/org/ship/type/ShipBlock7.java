package org.ship.type;

import org.bullet.Projectile;
import org.joml.Vector2f;
import org.ship.*;
import org.ship.turrentPrefab.BasicTurrent;

import engine.component.graphic.*;
import engine.component.physic.RigidBody;
import engine.math.Mathf;
import engine.object.GameObject;

public class ShipBlock7 extends ShipBlockComponent {

	BasicTurrent basicTurrent;

	public ShipBlock7(ShipBlockData shipBlockData, ShipComponent shipComponent) {
		super(shipBlockData, shipComponent);
	}

	@Override
	public void Destroy() {
		super.Destroy();
		basicTurrent.Destroy();
	}

	@Override
	public void Update() {
		super.Update();
		basicTurrent.Update();
	}

	// shoot Direction
	boolean shootRight = false;

	@Override
	public void Start() {
		super.Start();
		gameObject.GetComponent(SpriteRenderer.class).SetTexture(Texture.getTexture("Block7b"));

		basicTurrent = new BasicTurrent(this, Texture.getTexture("Block7c"), 7) {
			@Override
			public void Shoot() {
				// sideward component
				Vector2f offset;

				if (shootRight) {
					offset = Mathf.Vector2fFromAngle(
							(float) (basicTurrent.turrentGameObject.transform.rotation + Math.PI / 2)).mul(20);
				} else {
					offset = Mathf.Vector2fFromAngle(
							(float) (basicTurrent.turrentGameObject.transform.rotation - Math.PI / 2)).mul(20);
				}

				// change the turrent shooting side
				shootRight = !shootRight;

				// forward component
				offset.add(Mathf.Vector2fFromAngle((basicTurrent.turrentGameObject.transform.rotation)).mul(33));

				// Actually spawning the projectile
				GameObject ProjectileGO = new GameObject();
				ProjectileGO.transform.setPosition(gameObject.transform.position);
				ProjectileGO.transform.addPosition(offset);
				ProjectileGO.AddComponent(new Projectile(
						Mathf.Vector2fFromAngle(basicTurrent.turrentGameObject.transform.rotation), Team(),
						shipComponent.gameObject.GetComponentExtendedFromClass(RigidBody.class).getVelocity(),
						Texture.getTexture("bullet"), new Vector2f(0.2f, 0.2f), 120));
			}
		};

		super.setFindTarget(true);
	}

}

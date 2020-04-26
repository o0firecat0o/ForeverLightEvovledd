package org.ship.type;

import org.bullet.Projectile3;
import org.joml.Vector2f;
import org.ship.*;
import org.ship.turrentPrefab.BasicTurrent;

import engine.component.graphic.*;
import engine.component.physic.RigidBody;
import engine.math.Mathf;
import engine.object.GameObject;

public class ShipBlock17 extends ShipBlockComponent {

	BasicTurrent basicTurrent;

	public ShipBlock17(ShipBlockData shipBlockData, ShipComponent shipComponent) {
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

	@Override
	public void Start() {
		super.Start();
		gameObject.GetComponent(SpriteRenderer.class).SetTexture(Texture.getTexture("Block17a"));

		basicTurrent = new BasicTurrent(this, Texture.getTexture("Block17b"), 300) {
			@Override
			public void Shoot() {
				GameObject ProjectileGO = new GameObject();
				ProjectileGO.transform.setPosition(gameObject.transform.position);
				// this set is temporary, use physic set to make it permanent
				ProjectileGO.transform.setRotation(basicTurrent.turrentGameObject.transform.rotation);
				ProjectileGO.AddComponent(new Projectile3(
						Mathf.Vector2fFromAngle(basicTurrent.turrentGameObject.transform.rotation), Team(),
						shipComponent.gameObject.GetComponentExtendedFromClass(RigidBody.class).getVelocity(),
						Texture.getTexture("BulletB"), new Vector2f(4f, 4f), 3000));
				ProjectileGO.GetComponent(RigidBody.class)
						.setRotation(basicTurrent.turrentGameObject.transform.rotation);

			}
		};
		super.setFindTarget(true);
	}
}

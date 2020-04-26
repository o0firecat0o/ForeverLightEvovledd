package org.ship.type;

import org.bullet.Projectile;
import org.joml.Vector2f;
import org.ship.*;
import org.ship.turrentPrefab.BasicTurrent;

import engine.component.graphic.*;
import engine.component.physic.RigidBody;
import engine.math.Mathf;
import engine.object.GameObject;

public class ShipBlock5 extends ShipBlockComponent {

	BasicTurrent basicTurrent;

	public ShipBlock5(ShipBlockData shipBlockData, ShipComponent shipComponent) {
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
		gameObject.GetComponent(SpriteRenderer.class).SetTexture(Texture.getTexture("Block1"));

		basicTurrent = new BasicTurrent(this, Texture.getTexture("Block5a"), 30) {
			@Override
			public void Shoot() {
				GameObject ProjectileGO = new GameObject();
				ProjectileGO.transform.setPosition(gameObject.transform.position);
				ProjectileGO.AddComponent(new Projectile(
						Mathf.Vector2fFromAngle(basicTurrent.turrentGameObject.transform.rotation), Team(),
						shipComponent.gameObject.GetComponentExtendedFromClass(RigidBody.class).getVelocity(),
						Texture.getTexture("bullet"), new Vector2f(0.2f, 0.2f), 120));

			}
		};
		super.setFindTarget(true);
	}
}

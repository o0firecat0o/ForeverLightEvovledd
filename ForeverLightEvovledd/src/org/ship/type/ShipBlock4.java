package org.ship.type;

import org.joml.Vector2f;
import org.shield.ShieldComponent;
import org.ship.*;

import engine.object.*;

public class ShipBlock4 extends ShipBlockComponent {

	public ShipBlock4(ShipBlockData shipBlockData, ShipComponent shipComponent) {
		super(shipBlockData, shipComponent);
	}

	GameObject shieldObject;

	@Override
	public void Start() {
		super.Start();
		shieldObject = new GameObject();
		shieldObject.transform.setScale(new Vector2f(15, 15));
		shieldObject.transform.setPosition(gameObject.transform.position);

		shieldObject.AddComponent(new ShieldComponent(shipComponent.shipData.Team));
	}

	@Override
	public void Update() {
		// remove the shieldObject if it no longer exist
		if (shieldObject != null) {
			if (UpdatableObject.Exist(shieldObject)) {
				shieldObject.transform.setPosition(gameObject.transform.position);
			} else {
				shieldObject = null;
			}
		}
		super.Update();
	}

	@Override
	public void Die() {
		// destroy the whole ship
		shipComponent.Die();

		if (shieldObject != null) {
			shieldObject.InitDestroy();
			shieldObject = null;
		}
		super.Die();
	}
}

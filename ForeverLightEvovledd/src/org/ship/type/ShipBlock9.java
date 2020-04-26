package org.ship.type;

import org.explosions.ExplosionC;
import org.ship.*;
import org.ship.turrentPrefab.BasicTurrent;

import engine.component.graphic.*;
import engine.component.physic.*;
import engine.object.GameObject;

public class ShipBlock9 extends ShipBlockComponent {

	BasicTurrent basicTurrent;

	public ShipBlock9(ShipBlockData shipBlockData, ShipComponent shipComponent) {
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

		RayCastObject rayCastObject;

		if (Team() == 1) {
			rayCastObject = Physics.RayCasting(basicTurrent.turrentGameObject.transform.getPositionVector2f(),
					basicTurrent.turrentGameObject.transform.getPositionForward(3000), EntityCategory.TEAM2.getValue());
		} else {
			rayCastObject = Physics.RayCasting(basicTurrent.turrentGameObject.transform.getPositionVector2f(),
					basicTurrent.turrentGameObject.transform.getPositionForward(3000), EntityCategory.TEAM1.getValue());
		}

		if (rayCastObject == null) {
			GameObject g = new GameObject();
			g.transform.setPosition(basicTurrent.turrentGameObject.transform.getPositionForward(3000));
			g.AddComponent(new ExplosionC());
		} else {
			GameObject g = new GameObject();
			g.transform.setPosition(rayCastObject.point);
			g.AddComponent(new ExplosionC());
		}

	}

	@Override
	public void Start() {
		super.Start();
		gameObject.GetComponent(SpriteRenderer.class).SetTexture(Texture.getTexture("Block9b"));

		basicTurrent = new BasicTurrent(this, Texture.getTexture("Block9a"), 30) {
			@Override
			public void Shoot() {

			}
		};
		super.setFindTarget(true);
	}
}

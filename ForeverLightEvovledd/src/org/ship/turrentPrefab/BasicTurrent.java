package org.ship.turrentPrefab;

import org.ship.ShipBlockComponent;

import engine.component.graphic.SpriteRenderer;
import engine.component.graphic.spriteRendererComponent.DefaultRender;
import engine.math.Mathf;
import engine.object.GameObject;
import engine.timer.*;

/**
 * This class is written to simplify shipblock turrent instantiation
 * 
 * @author Ringo
 *
 */
public abstract class BasicTurrent {

	ShipBlockComponent shipBlockComponent; // The parent shipblock
	public GameObject turrentGameObject; // the self gameobject

	float turrentGameObjectRotationOffset = 0;
	int shootInterval;

	public int getShootInterval() {
		return shootInterval;
	}

	public void setShootInterval(int shootInterval) {
		this.shootInterval = shootInterval;
		shootTimer.SetThreshold(shootInterval);
	}

	Timer shootTimer;

	public void Update() {
		rotation();
		SetTurrentPosition();
	}

	public void Destroy() {
		turrentGameObject.InitDestroy();
	}

	/**
	 * 
	 * @param shipBlock
	 *            just type "this"
	 * @param Texture
	 *            the texture of the turrent
	 */
	public BasicTurrent(ShipBlockComponent shipBlock, int Texture, int shootInterval) {
		this.shipBlockComponent = shipBlock;
		turrentGameObject = new GameObject();
		turrentGameObject.transform.setScale(shipBlock.gameObject.transform.getScale());

		turrentGameObject.AddComponent(new SpriteRenderer().SetTexture(Texture))
				.addSpriteRendererComponent(new DefaultRender());
		// add the timer, hence it will be called when needed to shoot
		shootTimer = new Timer(getShootInterval(), new ITimerFunction() {
			@Override
			public void run() {
				if (shipBlock.gameObject != null) {
					Shoot();
				}
			}
		});
		setShootInterval(shootInterval);
	}

	public void ForceReload() {
		shootTimer.SetTime(0);
	}

	public abstract void Shoot();

	public void SetTurrentPosition() {
		turrentGameObject.transform.setPosition(shipBlockComponent.gameObject.transform.position);
		turrentGameObject.transform.rotation = shipBlockComponent.gameObject.transform.rotation
				+ turrentGameObjectRotationOffset;
	}

	/**
	 * Calculate the rotation offset, in order for the turrent to face the right
	 * direction
	 */
	private void rotation() {
		if (shipBlockComponent.target == null) {
			return;
		}
		float TargetAngle = Mathf.AngleFromVector2f(
				shipBlockComponent.target.transform.getPositionVector2f().sub(turrentGameObject.transform.getPositionVector2f()));
		float RequiredRotation = Mathf.returnDirection(turrentGameObject.transform.getRotation(), TargetAngle);

		if (RequiredRotation > 0) {
			turrentGameObjectRotationOffset -= 0.01f;
		}

		if (RequiredRotation < 0) {
			turrentGameObjectRotationOffset += 0.01f;
		}
	}
}

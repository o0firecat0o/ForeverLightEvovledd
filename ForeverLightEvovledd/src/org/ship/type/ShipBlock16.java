package org.ship.type;

import java.util.*;

import org.bullet.Projectile2;
import org.joml.*;
import org.ship.*;
import org.ship.turrentPrefab.BasicTurrent;

import engine.component.graphic.*;
import engine.component.graphic.instancedRendering.*;
import engine.component.physic.RigidBody;
import engine.main.Render;
import engine.math.*;
import engine.object.GameObject;

public class ShipBlock16 extends ShipBlockComponent {

	public ShipBlock16(ShipBlockData shipBlockData, ShipComponent shipComponent) {
		super(shipBlockData, shipComponent);
	}

	@Override
	public void Destroy() {
		basicTurrent.Destroy();
		bullet.InitDestroy();
		super.Destroy();
	}

	@Override
	public void UpdateRender() {
		super.UpdateRender();

		if (GameObject.Exist(bullet)) {
			hookLocation = bullet.transform.getPositionVector2f();

			// make the hook visual
			float distance = hookLocation.distance(gameObject.transform.getPositionVector2f());
			float totalNumberOfChain = distance / 100;

			// TODO: reuse the arrays, to save memories
			List<InstancedRenderObject> chain1 = new ArrayList<>();
			List<InstancedRenderObject> chain2 = new ArrayList<>();

			// rotation of the hook
			float rotation = Mathf
					.AngleFromVector2f(Mathf.minusVector(hookLocation, gameObject.transform.getPositionVector2f()));

			for (int i = 0; i < totalNumberOfChain; i++) {

				Vector2f position = Mathf.interpolation(gameObject.transform.getPositionVector2f(), hookLocation,
						i / totalNumberOfChain);

				if (i % 2 != 0) {
					chain1.add(new InstancedRenderObject(Maths.createTransformationMatrix(new Vector3f(position, 1),
							rotation, new Vector2f(1.5f, 1.5f)), new Vector4f(1, 1, 1, 1)));
				} else {
					chain2.add(new InstancedRenderObject(Maths.createTransformationMatrix(new Vector3f(position, 1),
							rotation, new Vector2f(1.5f, 1.5f)), new Vector4f(1, 1, 1, 1)));
				}
			}

			InstancedRenderer.Add(chain1, Texture.getTexture("Block16c"), Render.mainFrameBuffer.FrameBufferID);
			InstancedRenderer.Add(chain2, Texture.getTexture("Block16f"), Render.mainFrameBuffer.FrameBufferID);
		}

	}

	@Override
	public void Update() {
		basicTurrent.Update();
		if (!GameObject.Exist(bullet)) {
			bullet = null;
		}
		super.Update();
	}

	private BasicTurrent basicTurrent;

	private Vector2f hookLocation = new Vector2f();
	private GameObject bullet;

	@Override
	public void Start() {
		super.Start();
		gameObject.GetComponent(SpriteRenderer.class).SetTexture(Texture.getTexture("Block16e"));
		basicTurrent = new BasicTurrent(this, Texture.getTexture("Block16d"), 20) {

			@Override
			public void Shoot() {
				if (bullet == null) {
					bullet = new GameObject();
					bullet.transform.setPosition(gameObject.transform.position);
					bullet.transform.setRotation(basicTurrent.turrentGameObject.transform.rotation);
					bullet.AddComponent(new Projectile2(
							Mathf.Vector2fFromAngle(basicTurrent.turrentGameObject.transform.rotation).mul(16), Team(),
							shipComponent.gameObject.GetComponentExtendedFromClass(RigidBody.class).getVelocity(),
							Texture.getTexture("bullet3"), new Vector2f(4f, 4f), 600));
					bullet.GetComponent(Projectile2.class).originalShipBlock = gameObject;
				}
			}
		};
		super.setFindTarget(true);
	}
}

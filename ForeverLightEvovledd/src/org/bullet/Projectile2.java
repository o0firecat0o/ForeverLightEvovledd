package org.bullet;

import java.util.*;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import org.joml.*;

import engine.component.graphic.Texture;
import engine.component.graphic.instancedRendering.*;
import engine.component.physic.*;
import engine.main.Render;
import engine.math.*;
import engine.object.GameObject;

public class Projectile2 extends Projectile implements IContact {

	public GameObject pullGameObject; // the target which is hit by me
	public GameObject originalShipBlock;

	public Projectile2(Vector2f acceleration, int Team, Vec2 initialVelocity, int bulletTexture, Vector2f bulletSize,
			int disappearTimer) {
		super(acceleration, Team, initialVelocity, bulletTexture, bulletSize, disappearTimer);
	}

	@Override
	public void UpdateRender() {
		super.UpdateRender();
		Vector2f hookLocation;
		hookLocation = gameObject.transform.getPositionVector2f();

		if (originalShipBlock == null) {
			return;
		}

		// make the hook visual
		float distance = hookLocation.distance(originalShipBlock.transform.getPositionVector2f());
		float totalNumberOfChain = distance / 100;

		// TODO: reuse the arrays, to save memories
		List<InstancedRenderObject> chain1 = new ArrayList<>();
		List<InstancedRenderObject> chain2 = new ArrayList<>();

		// rotation of the hook
		float rotation = Mathf
				.AngleFromVector2f(Mathf.minusVector(hookLocation, originalShipBlock.transform.getPositionVector2f()));

		for (int i = 0; i < totalNumberOfChain; i++) {

			Vector2f position = Mathf.interpolation(originalShipBlock.transform.getPositionVector2f(), hookLocation,
					i / totalNumberOfChain);

			if (i % 2 != 0) {
				chain1.add(new InstancedRenderObject(
						Maths.createTransformationMatrix(new Vector3f(position, 1), rotation, new Vector2f(1.5f, 1.5f)),
						new Vector4f(1, 1, 1, 1)));
			} else {
				chain2.add(new InstancedRenderObject(
						Maths.createTransformationMatrix(new Vector3f(position, 1), rotation, new Vector2f(1.5f, 1.5f)),
						new Vector4f(1, 1, 1, 1)));
			}
		}

		InstancedRenderer.Add(chain1, Texture.getTexture("Block16c"), Render.mainFrameBuffer.FrameBufferID);
		InstancedRenderer.Add(chain2, Texture.getTexture("Block16f"), Render.mainFrameBuffer.FrameBufferID);
	}

	@Override
	public void Start() {
		super.Start();

		// reset the mask bits and category bits
		if (Team == 1) {
			gameObject.GetComponent(RigidBody.class).setCategoryBits(EntityCategory.TEAM1BULLET);
			gameObject.GetComponent(RigidBody.class).setMaskBits(EntityCategory.TEAM2);
		} else if (Team == 2) {
			gameObject.GetComponent(RigidBody.class).setCategoryBits(EntityCategory.TEAM2BULLET);
			gameObject.GetComponent(RigidBody.class).setMaskBits(EntityCategory.TEAM1);
		}
		// gameObject.GetComponent(RigidBody.class).setPhysicsDetermineTransform(false);
	}

	@Override
	protected void Update() {
		super.Update();

		// the projectile is hooked to something
		if (GameObject.Exist(pullGameObject)) {
			gameObject.transform.position.set(pullGameObject.transform.position);
			pullGameObject.GetComponent(RigidFixutre.class).rigidBody.AddForce(originalShipBlock.transform
					.getPositionVector2f().sub(gameObject.transform.getPositionVector2f()).normalize().mul(40));
		}
		// if the hook is already hooked, and the object has been destroyed
		// already
		else if (disappearTimer == 128000) {
			gameObject.InitDestroy();
		} else {
			// the following part is when the projectile is still not hooked to
			// anything
			// if the hook is near end of life
			if (timer > disappearTimer - 30) {
				gameObject.GetComponent(RigidBody.class).setPhysicsDetermineTransform(false);
			}
		}
	}

	@Override
	protected void Destroy() {
		super.Destroy();
		if (pullGameObject != null) {
			pullGameObject.InitDestroy();
		}
	}

	@Override
	public void Contact(org.jbox2d.dynamics.contacts.Contact contact, Fixture target) {
		// if already hooked onto something
		if (disappearTimer == 128000) {
			return;
		}

		disappearTimer = 128000;
		RigidFixutre rFixutre = (RigidFixutre) target.m_userData;

		if (rFixutre.gameObject.HasComponentExtendedFromInterface(IDamageable.class)) {
			pullGameObject = rFixutre.gameObject;
			// make sure that when we set the postion of the projectile to stick
			// to the shipblock,
			// the physic will stick to the logic instead of physic
			gameObject.GetComponent(RigidBody.class).setPhysicsDetermineTransform(false);
		}
	}

}

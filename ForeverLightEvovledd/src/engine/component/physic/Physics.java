package engine.component.physic;

import java.util.*;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import org.joml.*;

import engine.main.Logic;
import engine.math.Mathf;

public class Physics {
	public static Map<Fixture, org.jbox2d.dynamics.contacts.Contact> OverlapCircle(Vector3f position, float radius,
			int MaskedBit) {
		HelperPhysicBody helperPhysicBody = new HelperPhysicBody(position, radius, MaskedBit);
		Logic.world.step(0f, 8, 3);
		Map<Fixture, org.jbox2d.dynamics.contacts.Contact> returnList = helperPhysicBody.returnContacts();
		helperPhysicBody.Destroy();
		return returnList;
	}

	public static Fixture[] OverlapCircleBasic(Vector3f position, float radius, int MaskedBit) {
		HelperPhysicBody helperPhysicBody = new HelperPhysicBody(position, radius, MaskedBit);
		Logic.world.step(0f, 8, 3);
		Set<Fixture> fixtures = helperPhysicBody.returnContacts().keySet();
		helperPhysicBody.Destroy();
		return fixtures.toArray(new Fixture[fixtures.size()]);
	}

	public static RayCastObject RayCasting(Vector2f point1, Vector2f point2, int EntityCategory) {

		// converting the logic position back to the physic position
		Vec2 pointA = new Vec2(point1.x / 100, point1.y / 100);
		Vec2 pointB = new Vec2(point2.x / 100, point2.y / 100);

		HelperRaycastBody raycastBody = new HelperRaycastBody(EntityCategory);

		try {
			Logic.world.raycast(raycastBody, pointA, pointB);
		} catch (NullPointerException e) {
			return null;
		}

		if (raycastBody.targetfixture != null) {
			return new RayCastObject(raycastBody.targetfixture, Mathf.Vec2toVector2f(raycastBody.point.mul(100)),
					Mathf.Vec2toVector2f(raycastBody.normal.mul(100)));
		} else {
			return null;
		}

	}
}

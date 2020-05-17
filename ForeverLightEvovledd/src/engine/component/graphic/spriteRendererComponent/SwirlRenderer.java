package engine.component.graphic.spriteRendererComponent;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector4f;

import engine.component.Camera;
import engine.main.Render;

public class SwirlRenderer extends SpriteRendererComponent {

	// contain all swirl from all swirl renderer
	// array will be cleared every render
	public static ArrayList<Swirl> swirlList = new ArrayList<>();

	public float radius = 0.5f;
	public float angle = 0.4f;
	public boolean rotating = true; // to start swirling or not
	public boolean reducingSize = true;

	class Swirl {
		final Vector2f position;
		final float radius;
		final float angle;

		Swirl(Vector2f position, float radius, float angle) {
			this.position = position;
			this.radius = radius;
			this.angle = angle;
		}
	}

	public static ArrayList<Vector4f> returnALlSwirlinVector4f() {
		ArrayList<Vector4f> arrayList = new ArrayList<>();

		for (Swirl swirl : swirlList) {
			arrayList.add(new Vector4f(swirl.position.x, swirl.position.y, swirl.radius, swirl.angle));
		}

		return arrayList;
	}

	public void setRotation(boolean rotating) {
		this.rotating = rotating;
	}

	@Override
	public void Render(int FrameBufferObjectID) {
		swirlList.add(new Swirl(Camera.MAIN.WorldToOpenGLPosition(gameObject.transform.getPositionVector2f()), radius,
				angle));
	}

	@Override
	protected void Update() {
		if (rotating) {
			angle += 0.01f;
		}
		if (reducingSize) {
			radius -= 0.004f;
			if (radius < 0) {
				gameObject.InitDestroy();
			}
		}
	}

	@Override
	protected void Start() {
		FrameBufferIDs.add(Render.swirlFrameBuffer.FrameBufferID);
	}

}

package engine.object;

import org.joml.Vector2f;

public class UIObject extends GameObject {

	public static enum UIPositions {
		NULL, BottomCentered, TopCentered, Centered, RightCentered, TopRight
	}

	public UIPositions uIPositions;
	public Vector2f uIPositionsOffset = new Vector2f();

	@Override
	public void Update() {
		super.Update();

		switch (uIPositions) {
		case NULL:
			transform.position.x = uIPositionsOffset.x;
			transform.position.y = uIPositionsOffset.y;
			break;
		case BottomCentered:
			transform.position.x = (uIPositionsOffset.x + 1280 / 2);
			transform.position.y = (uIPositionsOffset.y - 720 / 2);
			break;
		case TopCentered:
			transform.position.x = (uIPositionsOffset.x + 1280 / 2);
			transform.position.y = (uIPositionsOffset.y + 720);
			break;
		case Centered:
			transform.position.x = (uIPositionsOffset.x + 1280 / 2);
			transform.position.y = (uIPositionsOffset.y + 720 / 2);
			break;
		case RightCentered:
			transform.position.x = (uIPositionsOffset.x + 1280);
			transform.position.y = (uIPositionsOffset.y + 720 / 2);
			break;
		case TopRight:
			transform.position.x = (uIPositionsOffset.x);
			transform.position.y = (uIPositionsOffset.y + 720);
		}
	}

	public UIObject(UIPositions uiPositions, Vector2f uIPositionsOffset, float z_position) {
		this.uIPositions = uiPositions;
		this.uIPositionsOffset = uIPositionsOffset;
		transform.position.z = z_position;

		blockRayCast = true;
	}
}

package org.gamestate;

import org.explosions.ExplosionC;
import org.joml.Vector2f;
import org.rock.Rock;
import org.ship.*;

import engine.component.Camera;
import engine.gamestate.IGameState;
import engine.input.InputMouseButton;
import engine.object.GameObject;
import engine.random.Random;

public class GamingGameState implements IGameState {

	@Override
	public void Update() {
		if (InputMouseButton.OnMouseDown(0)) {
			GameObject gameObject = new GameObject();
			gameObject.AddComponent(new ExplosionC());

			GameObject gameObject2 = new GameObject();
			gameObject2.transform.setPosition(Camera.MAIN.InputMousePositionV2f());
			gameObject2.AddComponent(new ExplosionC());
			Camera.MAIN.InputMousePositionV2f();
			//
			// int c = Physics.RayCasting(new Vector2f(),
			// Camera.MAIN.InputMousePositionV2f(),
			// EntityCategory.TEAM1.getValue());
			// System.out.println(c);
		}
	}

	@Override
	public void Stop() {

	}

	@Override
	public void Init() {
		// MakeShip(1, true, -1, new Vector2f());
		for (int i = 0; i < 1; i++) {
			MakeShip(1, false, -1, Random.RandomVector(3000));
		}

		for (int i = 0; i < 1; i++) {
			MakeShip(2, true, -1, Random.RandomVector(3000));
		}

		// for (int i = 0; i < 20; i++) {
		// MakeRock(new Vector2f(Random.RandomVector(3000)));
		// }
	}

	public ShipComponent MakeShip(int Team, boolean isControlling, float ConnectionID, Vector2f location) {
		ShipData sData = new ShipData();
		sData.AddShipBlock(0, 0, 2, 0, 4);
		sData.AddShipBlock(0, -2, 2, 0, 15);
		// sData.AddShipBlock(4, 4, 4, (float) Math.PI, 15);
		sData.AddShipBlock(0, 6, 6, 0, 12);
		sData.AddShipBlock(8, 8, 4, 0, 17);
		sData.AddShipBlock(-6, 4, 4, 0, 9);

		sData.setTeam(Team);
		ShipComponent shipComponent = sData.BuildShip(location);
		shipComponent.setControlling(isControlling);
		shipComponent.ControllingID = ConnectionID;

		return shipComponent;
	}

	public Rock MakeRock(Vector2f location) {
		GameObject rockGO = new GameObject();
		rockGO.transform.setPosition(location);
		Rock rock = rockGO.AddComponent(new Rock());
		return rock;
	}

}

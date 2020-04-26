package org.ship.type;

import java.util.*;

import org.component.CircularHealthBar;
import org.joml.*;
import org.ship.*;

import engine.math.Mathf;
import engine.object.GameObject;

public class ShipBlock12 extends ShipBlockComponent {

	Map<Integer, ShipComponent> Drones = new HashMap<>();

	public ShipBlock12(ShipBlockData shipBlockData, ShipComponent shipComponent) {
		super(shipBlockData, shipComponent);
	}

	@Override
	public void Destroy() {
		super.Destroy();

		replenishTimerBar.InitDestroy();

		// destroy all the drones
		for (int i = 0; i < Drones.size(); i++) {
			Drones.get(i).Die();
		}
	}

	@Override
	public void Update() {
		super.Update();

		if (replenishTimerBar != null) {
			replenishTimerBar.GetComponent(CircularHealthBar.class).setCurValue(replenishTimer);
			replenishTimerBar.transform.setPosition(gameObject.transform.getPositionVector2f());
		}

		if (replenishTimer < 600) {
			replenishTimer++;
		} else if (droneCount < 8) {
			droneCount++;
			replenishTimer = 0;
			MakeDrone();
		}

		for (int i = 0; i < Drones.size(); i++) {

			ShipComponent drone = Drones.get(i);

			// if there is no drone assign to this position, continue to next
			// position
			if (drone == null) {
				continue;
			}

			// check if the drone has been destroyed, if destroyed, free up the
			// position
			if (drone.isDead) {
				Drones.put(i, null);
				droneCount -= 1;
				continue;
			}

			Vector2f dockingPosition = new Vector2f();
			dockingPosition = gameObject.transform.getPositionVector2f();
			dockingPosition.add(Mathf.Vector2fFromAngle(gameObject.transform.rotation + i * 0.78539f).mul(200));

			// check if the drone is docked, if it is docked, give the docked
			// position
			if (drone.isDocked) {
				drone.gameObject.transform.setPosition(dockingPosition);
			}
			// check if the drone is docking, if it is docking, guide the drone
			// to dock
			if (drone.isDocking) {
				drone.dockingTarget.set(dockingPosition);
			}
		}
	}

	private int replenishTimer = 0;
	private int droneCount = 8;
	GameObject replenishTimerBar;

	@Override
	public void Start() {
		super.Start();

		super.setFindTarget(true);

		// Make all the positions
		for (int i = 0; i < 8; i++) {
			Drones.put(i, null);
		}

		MakeDrone();
		MakeDrone();
		MakeDrone();
		MakeDrone();
		MakeDrone();
		MakeDrone();
		MakeDrone();
		MakeDrone();

		// make the replenishBar;
		replenishTimerBar = new GameObject();
		replenishTimerBar.AddComponent(new CircularHealthBar(600, replenishTimer));
		replenishTimerBar.GetComponent(CircularHealthBar.class).setColor(new Vector3f(1f, 1f, 0.8f));
		replenishTimerBar.transform.setScale(5);
	}

	public ShipComponent MakeDrone() {
		ShipData sData = new ShipData();
		sData.AddShipBlock(0, 0, 1, 0, 14);

		sData.setTeam(shipComponent.shipData.Team);
		ShipComponent shipComponent = sData.BuildShip(gameObject.transform.getPositionVector2f());
		shipComponent.setControlling(false);
		shipComponent.isDrone = true;
		shipComponent.isDocked = true;

		for (int i = 0; i < 8; i++) {
			// if the position is not occupied yet
			if (Drones.get(i) == null) {
				Drones.put(i, shipComponent);
				return shipComponent;
			}
		}

		System.err.println("Drones limit exceed, @ShipBlock12, MakeDrone");

		return null;
	}
}

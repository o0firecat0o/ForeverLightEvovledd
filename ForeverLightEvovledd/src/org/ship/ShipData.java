package org.ship;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.ship.type.*;

import engine.object.GameObject;

/**
 * Data class Ships
 * 
 * @author Ringo
 *
 */
@SuppressWarnings("serial")
public class ShipData implements java.io.Serializable {

	public final ArrayList<ShipBlockData> ShipBlockList = new ArrayList<ShipBlockData>();
	public float TotalMass = 0;
	public int Team = 0;

	public void Destroy() {
		ShipBlockList.clear();
	}

	public void setTeam(int Team) {
		this.Team = Team;
	}

	/**
	 * add a ShipBlockData to the ShipData
	 * 
	 * @param x
	 * @param y
	 * @param size
	 * @param type
	 * @param rotation
	 *            in radians
	 */
	public void AddShipBlock(int x, int y, int size, float rotation, int type) {
		if (size == 0) {
			throw new RuntimeException("Invalid ShipBlock Size");
		}
		ShipBlockData shipBlockData = new ShipBlockData(x, y, size, rotation, type);
		if (hasOverLap(shipBlockData)) {
			throw new RuntimeException("After adding a shipblock at coordinate: " + x + "," + y + " overlap happened");
		}
		ShipBlockList.add(shipBlockData);

		CalculateTotalMass();
	}

	/**
	 * Do the ship has overlaping shipblock after adding the new shipBlock?
	 * 
	 * @param shipBlockData
	 * @return
	 */
	public boolean hasOverLap(ShipBlockData shipBlockData) {
		ShipBlockList.add(shipBlockData);
		boolean returnBool = hasOverLap();
		ShipBlockList.remove(shipBlockData);
		return returnBool;
	}

	/**
	 * Do the ship has overlaping shipblock?
	 * 
	 * @return
	 */
	public boolean hasOverLap() {
		ArrayList<Vector2f> occupiedVector2f = new ArrayList<>();
		for (int i = 0; i < ShipBlockList.size(); i++) {
			ShipBlockData testShipBlockData = ShipBlockList.get(i);

			// testing the shipBlock against all the recorded vector2
			// if the recorded vector2 contain the shipblock, then return has
			// overlap
			for (int j = 0; j < occupiedVector2f.size(); j++) {
				Vector2f testVector = occupiedVector2f.get(j);
				if (testShipBlockData.ReturnOccupied().contains(testVector)) {
					return true;
				}
			}

			occupiedVector2f.addAll(testShipBlockData.ReturnOccupied());
		}
		return false;
	}

	public float CalculateTotalMass() {
		TotalMass = 0;
		for (int i = 0; i < ShipBlockList.size(); i++) {
			ShipBlockData testShipBlockData = ShipBlockList.get(i);
			if (testShipBlockData.getCurrentHealth() <= 0) {
				continue;
			}
			TotalMass += testShipBlockData.returnMass();
		}
		return TotalMass;
	}

	public ShipComponent BuildShip(Vector2f position) {
		GameObject shipObject = new GameObject();
		// set the position of the ship
		shipObject.transform.setPosition(position);
		// add the shipcomponent to the ship
		ShipComponent shipComponent = new ShipComponent(this);
		shipObject.AddComponent(shipComponent);
		// add the ship blocks
		for (int i = 0; i < ShipBlockList.size(); i++) {
			GameObject shipBlock = new GameObject();

			ShipBlockData shipBlockData = ShipBlockList.get(i);

			// set the transform of the gameObject

			// will be overrided by RigidBody later, but this can ensure the
			// shipBlock data is in position when the shipBlock is in the Start
			// function
			shipBlock.transform.setPosition(position.x + shipBlockData.x() * 100, position.y + shipBlockData.y() * 100);
			shipBlock.transform.setRotation((float) Math.toRadians(shipBlockData.rotation));

			if (shipBlockData.size % 2 == 0) {
				shipBlock.transform.addPosition(new Vector2f(50, 50));
			}

			// make new shipBlockComponent
			ShipBlockComponent shipBlockComponent;
			switch (shipBlockData.type) {
			case 4:
				shipBlockComponent = new ShipBlock4(shipBlockData, shipComponent);
				break;
			case 5:
				shipBlockComponent = new ShipBlock5(shipBlockData, shipComponent);
				break;
			case 7:
				shipBlockComponent = new ShipBlock7(shipBlockData, shipComponent);
				break;
			case 9:
				shipBlockComponent = new ShipBlock9(shipBlockData, shipComponent);
				break;
			case 13:
				shipBlockComponent = new ShipBlock13(shipBlockData, shipComponent);
				break;
			case 12:
				shipBlockComponent = new ShipBlock12(shipBlockData, shipComponent);
				break;
			case 14:
				shipBlockComponent = new ShipBlock14(shipBlockData, shipComponent);
				break;
			case 16:
				shipBlockComponent = new ShipBlock16(shipBlockData, shipComponent);
				break;
			case 17:
				shipBlockComponent = new ShipBlock17(shipBlockData, shipComponent);
				break;

			default:
				shipBlockComponent = new ShipBlockComponent(shipBlockData, shipComponent);
				break;
			}

			shipBlock.AddComponent(shipBlockComponent);
		}
		return shipComponent;
	}
}

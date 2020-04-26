package org.ship;

import java.util.ArrayList;

import org.joml.Vector2f;

/**
 * Data Class Ship Block Data
 * 
 * @author Ringo
 *
 */
@SuppressWarnings("serial")
public class ShipBlockData implements java.io.Serializable {

	private final Vector2f Coordinate = new Vector2f();
	public final int size;
	public final int type;
	public final float rotation;

	public float MaxHealth = 3000;
	private float CurrentHealth = 3000;

	public void setCurrentHealth(float currentHealth) {
		this.CurrentHealth = currentHealth;
	}

	public float getCurrentHealth() {
		return CurrentHealth;
	}

	public int x() {
		return (int) Coordinate.x;
	}

	public int y() {
		return (int) Coordinate.y;
	}

	public Vector2f Coordinate() {
		return new Vector2f(Coordinate);
	}

	public float returnMass() {
		return size * size;
	}

	public ShipBlockData(int x, int y, int size, float rotation, int type) {
		Coordinate.x = x;
		Coordinate.y = y;
		this.size = size;
		this.type = type;
		this.rotation = rotation;
	}

	/**
	 * Return the coordinates of neighbouring block
	 * 
	 * @return Neighbour Coordinates
	 */
	public ArrayList<Vector2f> ReturnNeighbour() {
		ArrayList<Vector2f> returnList = new ArrayList<>();
		if (size == 1) {
			returnList.add(new Vector2f(x() + 1, y()));
			returnList.add(new Vector2f(x() - 1, y()));
			returnList.add(new Vector2f(x(), y() - 1));
			returnList.add(new Vector2f(x(), y() + 1));
		} else if (size == 2) {
			returnList.add(new Vector2f(x() - 1, y()));
			returnList.add(new Vector2f(x() - 1, y() + 1));
			returnList.add(new Vector2f(x(), y() - 1));
			returnList.add(new Vector2f(x(), y() + 2));
			returnList.add(new Vector2f(x() + 1, y() - 1));
			returnList.add(new Vector2f(x() + 1, y() + 2));
			returnList.add(new Vector2f(x() + 2, y()));
			returnList.add(new Vector2f(x() + 2, y() + 1));

		} else {
			throw new RuntimeException("Size inavlid");
		}

		return returnList;
	}

	/**
	 * Return the occupied coordinates of self block
	 * 
	 * @return
	 */
	public ArrayList<Vector2f> ReturnOccupied() {
		ArrayList<Vector2f> returnList = new ArrayList<>();
		if (size == 1) {
			returnList.add(new Vector2f(x(), y()));
		} else if (size == 2) {
			returnList.add(new Vector2f(x(), y()));
			returnList.add(new Vector2f(x(), y() + 1));
			returnList.add(new Vector2f(x() + 1, y()));
			returnList.add(new Vector2f(x() + 1, y() + 1));
		} else if (size == 3) {
			returnList.add(new Vector2f(x(), y()));
			returnList.add(new Vector2f(x(), y() + 1));
			returnList.add(new Vector2f(x(), y() - 1));
			returnList.add(new Vector2f(x() + 1, y()));
			returnList.add(new Vector2f(x() + 1, y() + 1));
			returnList.add(new Vector2f(x() + 1, y() - 1));
			returnList.add(new Vector2f(x() - 1, y()));
			returnList.add(new Vector2f(x() - 1, y() + 1));
			returnList.add(new Vector2f(x() - 1, y() - 1));
		} else if (size == 4) {
			returnList.add(new Vector2f(x(), y()));
			returnList.add(new Vector2f(x(), y() + 1));
			returnList.add(new Vector2f(x(), y() + 2));
			returnList.add(new Vector2f(x(), y() - 1));

			returnList.add(new Vector2f(x() + 1, y()));
			returnList.add(new Vector2f(x() + 1, y() + 1));
			returnList.add(new Vector2f(x() + 1, y() + 2));
			returnList.add(new Vector2f(x() + 1, y() - 1));

			returnList.add(new Vector2f(x() - 1, y()));
			returnList.add(new Vector2f(x() - 1, y() + 1));
			returnList.add(new Vector2f(x() - 1, y() + 2));
			returnList.add(new Vector2f(x() - 1, y() - 1));

			returnList.add(new Vector2f(x() + 2, y()));
			returnList.add(new Vector2f(x() + 2, y() + 1));
			returnList.add(new Vector2f(x() + 2, y() + 2));
			returnList.add(new Vector2f(x() + 2, y() - 1));

		} else if (size == 6) {
			returnList.add(new Vector2f(x(), y()));
			returnList.add(new Vector2f(x(), y() + 1));
			returnList.add(new Vector2f(x(), y() + 2));
			returnList.add(new Vector2f(x(), y() + 3));
			returnList.add(new Vector2f(x(), y() - 1));
			returnList.add(new Vector2f(x(), y() - 2));

			returnList.add(new Vector2f(x() + 1, y()));
			returnList.add(new Vector2f(x() + 1, y() + 1));
			returnList.add(new Vector2f(x() + 1, y() + 2));
			returnList.add(new Vector2f(x() + 1, y() + 3));
			returnList.add(new Vector2f(x() + 1, y() - 1));
			returnList.add(new Vector2f(x() + 1, y() - 2));

			returnList.add(new Vector2f(x() + 2, y()));
			returnList.add(new Vector2f(x() + 2, y() + 1));
			returnList.add(new Vector2f(x() + 2, y() + 2));
			returnList.add(new Vector2f(x() + 2, y() + 3));
			returnList.add(new Vector2f(x() + 2, y() - 1));
			returnList.add(new Vector2f(x() + 2, y() - 2));

			returnList.add(new Vector2f(x() + 3, y()));
			returnList.add(new Vector2f(x() + 3, y() + 1));
			returnList.add(new Vector2f(x() + 3, y() + 2));
			returnList.add(new Vector2f(x() + 3, y() + 3));
			returnList.add(new Vector2f(x() + 3, y() - 1));
			returnList.add(new Vector2f(x() + 3, y() - 2));

			returnList.add(new Vector2f(x() - 1, y()));
			returnList.add(new Vector2f(x() - 1, y() + 1));
			returnList.add(new Vector2f(x() - 1, y() + 2));
			returnList.add(new Vector2f(x() - 1, y() + 3));
			returnList.add(new Vector2f(x() - 1, y() - 1));
			returnList.add(new Vector2f(x() - 1, y() - 2));

			returnList.add(new Vector2f(x() - 2, y()));
			returnList.add(new Vector2f(x() - 2, y() + 1));
			returnList.add(new Vector2f(x() - 2, y() + 2));
			returnList.add(new Vector2f(x() - 2, y() + 3));
			returnList.add(new Vector2f(x() - 2, y() - 1));
			returnList.add(new Vector2f(x() - 2, y() - 2));

		} else {
			throw new RuntimeException("Size inavlid @ ShipBlockData ... returnOccupied()");
		}
		return returnList;
	}
}

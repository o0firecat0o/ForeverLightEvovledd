package org.ship;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import engine.component.physic.RigidBody;
import engine.input.InputKey;
import engine.math.Mathf;
import engine.network.*;
import engine.object.*;
import engine.timer.*;

public class ShipComponent extends Component {

	public final ShipData shipData;
	private engine.timer.Timer findTargetTimer;
	private GameObject target;

	/**
	 * has been destroyed, as a indicator so that other class can remove this
	 * component. i.e. setting it to null
	 */
	public boolean isDead = false;

	/**
	 * is it a Drone? if it is a Drone, then movement will be controlled in a
	 * different way
	 */
	public boolean isDrone = false;

	/**
	 * is the player controlling the ship?
	 */
	private boolean Controlling = false;
	/**
	 * Who is controlling the ship, same as NetworkID;
	 */
	public float ControllingID = -1f;

	public void setControlling(boolean Controlling) {
		this.Controlling = Controlling;
		// if it is an AI:
		if (!Controlling) {
			findTargetTimer = new Timer(60, new ITimerFunction() {
				@Override
				public void run() {
					if (target == null) {
						if (shipData.Team == 1) {
							target = FindTarget(2);
						}
						if (shipData.Team == 2) {
							target = FindTarget(1);
						}
					}
					if (target != null && !UpdatableObject.Exist(target)) {
						target = null;
					}
				}
			});
		}
		// if it is not an AI, destroy the find target timer
		else {
			if (findTargetTimer != null) {
				findTargetTimer.Destroy();
			}
		}
	}

	public boolean getControlling() {
		return Controlling;
	}

	@Override
	protected void Update() {
		// if the ship is controlled by player
		// and if the player is ME
		if (Controlling && ControllingID == MPNetworkManager.ConnectionID) {
			Movement();
		} else if (!Controlling) {
			if (isDrone) {
				DroneMovement();
			} else {
				BotMovement();
			}
		}
	}

	private int Accelerate;
	private int Decelerate;
	private int TurnLeft;
	private int TurnRight;

	@Override
	protected void Start() {
		gameObject.AddComponent(new RigidBody());
		gameObject.GetComponent(RigidBody.class).setLinearDrag(0.08f).setAngularDrag(2f);
		gameObject.setTag("Ship");

		// Register all the functions for Online use
		Accelerate = MPNetworkManager.RegisterFunction(new INetworkFunction() {
			@Override
			public void run(Object[] objects) {
				Accelerate();
			}
		});
		Decelerate = MPNetworkManager.RegisterFunction(new INetworkFunction() {

			@Override
			public void run(Object[] objects) {
				Decelerate();
			}
		});

		TurnLeft = MPNetworkManager.RegisterFunction(new INetworkFunction() {

			@Override
			public void run(Object[] objects) {
				TurnLeft();
			}
		});

		TurnRight = MPNetworkManager.RegisterFunction(new INetworkFunction() {

			@Override
			public void run(Object[] objects) {
				TurnRight();
			}
		});
	}

	public ShipComponent(ShipData shipData) {
		super();
		this.shipData = shipData;
	}

	/**
	 * The ship has been destroyed;
	 */
	public void Die() {
		gameObject.InitDestroy();
		isDead = true;
	}

	@Override
	protected void Destroy() {
		// TODO: fix this, this is weird
		shipData.Destroy();
		// destroy the timer
		if (findTargetTimer != null) {
			findTargetTimer.Destroy();
		}
		super.Destroy();
	}

	private void Movement() {
		if (InputKey.OnKeysHold(GLFW.GLFW_KEY_W)) {
			MPNetworkManager.sendFunction(Accelerate, null);
		}
		if (InputKey.OnKeysHold(GLFW.GLFW_KEY_S)) {
			MPNetworkManager.sendFunction(Decelerate, null);
		}
		if (InputKey.OnKeysHold(GLFW.GLFW_KEY_A)) {
			MPNetworkManager.sendFunction(TurnLeft, null);
		}
		if (InputKey.OnKeysHold(GLFW.GLFW_KEY_D)) {
			MPNetworkManager.sendFunction(TurnRight, null);
		}
	}

	///////////////////
	// The following part is for drone
	///////////////////

	public boolean isDocked = true;
	// is the drone trying to go back to the mother ship?
	public boolean isDocking = false;
	// the drone will try to go to this position to dock
	public final Vector2f dockingTarget = new Vector2f();

	private void Dock() {
		isDocking = true;

		// normal movement
		float TargetAngle = Mathf.AngleFromVector2f(dockingTarget.sub(gameObject.transform.getPositionVector2f()));
		float returnDirection = Mathf.returnDirection(gameObject.transform.rotation, TargetAngle);

		if (returnDirection > 0.3f) {
			TurnRight();
		} else if (returnDirection < 0.3f) {
			TurnLeft();
		}
		if (Math.abs(returnDirection) < 0.3f) {
			Accelerate();
		}

		// if close to the dockingTarget, docking sucessful
		if (dockingTarget.distance(gameObject.transform.getPositionVector2f()) < 1000) {
			isDocked = true;
			isDocking = false;
		}
	}

	private void unDock() {
		gameObject.GetComponent(RigidBody.class).setPhysicsDetermineTransform(true);
		isDocked = false;
	}

	private void DroneMovement() {
		if (target == null) {
			if (!isDocked) {
				Dock();
			}
		} else {
			if (isDocked) {
				unDock();
			}
			BotMovement();
		}

		if (isDocked) {
			gameObject.GetComponent(RigidBody.class).setPhysicsDetermineTransform(false);
		}
	}

	/////////////
	// Drone part end
	////////////

	private void BotMovement() {
		if (target == null) {
			return;
		}
		float TargetAngle = Mathf
				.AngleFromVector2f(target.transform.getPositionVector2f().sub(gameObject.transform.getPositionVector2f()));
		float returnDirection = Mathf.returnDirection(gameObject.transform.rotation, TargetAngle);

		float TargetDistance = Mathf.Distance(gameObject, target);

		if (returnDirection > 0.3f) {
			TurnRight();
		} else if (returnDirection < 0.3f) {
			TurnLeft();
		}
		if (Math.abs(returnDirection) < 0.3f && TargetDistance > 1500) {
			Accelerate();
		}
		if (Math.abs(returnDirection) < 0.3f && TargetDistance < 1500) {
			Decelerate();
		}
	}

	public void Accelerate() {
		gameObject.GetComponent(RigidBody.class)
				.AddForce(Mathf.Vector2fFromAngle(gameObject.transform.rotation).mul(shipData.TotalMass));
	}

	public void Decelerate() {
		gameObject.GetComponent(RigidBody.class)
				.AddForce(Mathf.Vector2fFromAngle(gameObject.transform.rotation).mul(-shipData.TotalMass));
	}

	public void TurnLeft() {
		gameObject.GetComponent(RigidBody.class).AddAngularForce(shipData.TotalMass * 3);
	}

	public void TurnRight() {
		gameObject.GetComponent(RigidBody.class).AddAngularForce(-shipData.TotalMass * 3);
	}

	/**
	 * Find the closet Ships
	 * 
	 * @return
	 */
	public GameObject FindTarget(int Team) {
		GameObject returnObject = null;
		ArrayList<GameObject> shipObjects = GameObject.getGameObjectswithTag("Ship");
		float distance = 100000000;
		for (int i = 0; i < shipObjects.size(); i++) {
			GameObject shipObject = shipObjects.get(i);
			if (shipObject.GetComponent(ShipComponent.class).shipData.Team == Team) {
				float v_d = Mathf.Distance(gameObject, shipObject);
				if (v_d < distance) {
					returnObject = shipObject;
					distance = v_d;
				}
			}
		}
		return returnObject;
	}

}

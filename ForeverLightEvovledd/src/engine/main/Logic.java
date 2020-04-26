package engine.main;

import java.util.concurrent.TimeUnit;

import org.gamestate.MainGameState;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

import engine.component.DebugScreen;
import engine.component.physic.*;
import engine.gamestate.GameState;
import engine.input.*;
import engine.network.MPNetworkManager;
import engine.object.*;
import engine.timer.Timer;

public class Logic implements Runnable {

	private boolean running = false;

	public static int ups;

	// world of Physics
	public static final World world = new World(new Vec2(0, 0), false);

	// debug screen
	public static DebugScreen DEBUG_SCREEN;

	public void init() {
		// set the callback for physics
		world.setContactListener(new RigidBodyContactListener());

		// GameState.SwitchGameState(gameManager);
		GameState.SwitchGameState(new MainGameState());

		// set DebugScreen
		DEBUG_SCREEN = new GameObject().AddComponent(new DebugScreen());
		DEBUG_SCREEN.gameObject.stay_on_stage = true;
	}

	@Override
	public void run() {
		while (Render.finishInit == false) {
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		init();

		running = true;

		long lastTime = System.nanoTime();
		double ns = 1000000000.0 / 60.0;
		double delta = 0.0;
		int updates = 0;
		long timer = System.currentTimeMillis();
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1.0) {
				// if network is not initiallized
				if (!MPNetworkManager.getConnectionState()) {
					update();
					updates++;
					delta--;
				}
				// if network is initiallized
				else {
					// if the network is not locked
					if (!MPNetworkManager.SHOULD_LOCK) {
						if (MPNetworkManager.Frame == 0) {
							GameState.SwitchGameState(MPNetworkManager.gameStateAfterInitiation);
						}
						update();
						updates++;
						delta--;
						// after finishing the update loop, time for sending
						// for server, it will be sending to client
						// for client, it will be sending to server
						MPNetworkManager.SHOULD_SEND = true;
						// lock the thread and wait for server/client to finish
						// the loop
						MPNetworkManager.SHOULD_LOCK = true;
						// frame counter
						MPNetworkManager.Frame++;
					}
				}
			}
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				ups = updates;
				updates = 0;
			}
			if (Render.running == false && Render.finishInit == true) {
				running = false;
			}
		}
	}

	public void update() {
		// Physics Rendering
		world.step(1 / 60f, 2, 1);
		// normally (1 / 60f, 8, 3)

		// Update all gameObjects
		for (int i = 0; i < UpdatableObject.AllUpdatableObject.size(); i++) {
			UpdatableObject.AllUpdatableObject.get(i).Update();
		}

		// update the current gameState
		GameState.currentGameState.Update();

		// run all the timers
		for (int i = 0; i < Timer.timerArrays.size(); i++) {
			Timer.timerArrays.get(i).Update();
		}

		if (!world.isLocked()) {

			// destroy all the bodies
			for (int i = 0; i < RigidBody.tobeDestroyBody.size(); i++) {
				Body vbody = RigidBody.tobeDestroyBody.get(i);

				Fixture fixture = vbody.getFixtureList();
				// loop through all the fixtures
				while (fixture != null) {
					vbody.getFixtureList().setUserData(null);
					Fixture originalFixutre = fixture;
					fixture = fixture.getNext();
					vbody.destroyFixture(originalFixutre);
				}
				world.destroyBody(vbody);
			}
			RigidBody.tobeDestroyBody.clear();
		}

		// destroy all the gameobject
		UpdatableObject.DestroyAllDestroyNextFrameObjects();

		// reset Mouse and Keyboard
		InputMouseButton.Reset();
		InputMouseScroll.Reset();
		InputKey.Reset();
	}
}

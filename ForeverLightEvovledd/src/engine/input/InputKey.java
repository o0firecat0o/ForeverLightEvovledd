package engine.input;

import org.lwjgl.glfw.*;

public class InputKey extends GLFWKeyCallback {

	private static boolean[] keysHold = new boolean[65536];

	private static boolean[] keysDown = new boolean[65536];

	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		keysHold[key] = action != GLFW.GLFW_RELEASE;
		if (action == GLFW.GLFW_PRESS) {
			keysDown[key] = true;
		}
	}

	public static boolean OnKeysDown(int Key) {
		return keysDown[Key];
	}

	public static boolean OnKeysHold(int Key) {
		return keysHold[Key];
	}

	public static void Reset() {
		for (int i = 0; i < keysDown.length; i++) {
			keysDown[i] = false;
		}
	}
}

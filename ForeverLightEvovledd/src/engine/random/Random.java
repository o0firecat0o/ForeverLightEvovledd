package engine.random;

import org.joml.Vector2f;

public class Random {

	private static java.util.Random random = new java.util.Random();

	public static Vector2f RandomVector(float Max) {
		return new Vector2f(-Max / 2f + (float) Math.random() * Max, -Max / 2f + (float) Math.random() * Max);
	}

	public static int RandomInt(int min, int max) {
		int randomNum = random.nextInt((max - min) + 1) + min;

		return randomNum;
	}

	public static float RandomFloat(float min, float max) {
		return (float) (min + Math.random() * (max - min));
	}
}

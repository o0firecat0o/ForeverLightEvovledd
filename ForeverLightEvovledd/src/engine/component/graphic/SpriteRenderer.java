package engine.component.graphic;

import java.util.ArrayList;

import org.joml.Matrix4f;

import engine.component.graphic.spriteRendererComponent.SpriteRendererComponent;
import engine.main.Main;

public class SpriteRenderer {

	public static Matrix4f pr_matrix = new Matrix4f().ortho(0, Main.getWidth(), 0, Main.getHeight(), -10, 10);

	public static Matrix4f pr_matrix_enlarged = new Matrix4f().ortho(-Main.getWidth() * 2, Main.getWidth() * 3,
			-Main.getHeight() * 2, Main.getHeight() * 3, -10, 10);

	private static ArrayList<SpriteRendererComponent> allSpriteRendererComponents = new ArrayList<>();

	public static synchronized ArrayList<SpriteRendererComponent> getAllSpriteRendererComponents() {
		return allSpriteRendererComponents;
	}

	public static synchronized void addSpriteRendererComponent(SpriteRendererComponent spriteRendererComponent) {
		allSpriteRendererComponents.add(spriteRendererComponent);
	}

	public static synchronized void removeSpriteRendererComponent(SpriteRendererComponent spriteRendererComponent) {
		allSpriteRendererComponents.remove(spriteRendererComponent);
	}

	public static int getSpriteRendererComponentsCounts() {
		return allSpriteRendererComponents.size();
	}
}

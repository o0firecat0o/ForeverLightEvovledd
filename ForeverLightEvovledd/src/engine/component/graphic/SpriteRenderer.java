package engine.component.graphic;

import java.util.ArrayList;

import org.joml.Matrix4f;

import engine.component.graphic.spriteRendererComponent.SpriteRendererComponent;
import engine.main.Main;

public class SpriteRenderer {

	public static Matrix4f pr_matrix = new Matrix4f().ortho(0, Main.getWidth(), 0, Main.getHeight(), -10, 10);

	public static ArrayList<SpriteRendererComponent> allSpriteRendererComponents = new ArrayList<>();

	public static int getSpriteRendererComponentsCounts() {
		return allSpriteRendererComponents.size();
	}
}

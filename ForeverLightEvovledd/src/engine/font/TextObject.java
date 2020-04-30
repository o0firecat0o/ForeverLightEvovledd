package engine.font;

import org.joml.*;

import engine.math.Maths;

public class TextObject {
	public Matrix4f matrix4f; // includes rotation, position and scale
	public Vector4f Color;

	public TextObject(Matrix4f position, Vector4f Color) {
		this.matrix4f = position;
		this.Color = Color;
	}

	public TextObject(Vector3f position, float rotation, Vector2f scale, Vector4f Color) {
		this.matrix4f = Maths.createTransformationMatrix(position, rotation, scale);
		this.Color = Color;
	}
}
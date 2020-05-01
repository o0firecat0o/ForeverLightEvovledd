package engine.font.newrenderer;

import org.joml.*;

import engine.math.Maths;

public class TextObject {
	public Matrix4f matrix4f; // includes rotation, position and scale
	public Vector4f Color;
	public int charID; // ASCII code

	public TextObject(Matrix4f position, Vector4f Color, int charID) {
		this.matrix4f = position;
		this.Color = Color;
		this.charID = charID;
	}

	public TextObject(Vector3f position, float rotation, Vector2f scale, Vector4f Color, int charID) {
		this.matrix4f = Maths.createTransformationMatrix(position, rotation, scale);
		this.Color = Color;
		this.charID = charID;
	}
}
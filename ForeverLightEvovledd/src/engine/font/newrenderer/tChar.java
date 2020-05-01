package engine.font.newrenderer;

import org.joml.Vector4f;

public class tChar {
	char c;
	boolean bold = false;
	Vector4f color = new Vector4f(0, 0, 0, 1);

	public tChar(char c, boolean bold, Vector4f color) {
		this.c = c;
		this.bold = bold;
		this.color = color;
	}
}
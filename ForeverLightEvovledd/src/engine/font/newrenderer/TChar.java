package engine.font.newrenderer;

import org.joml.Vector4f;

public class TChar {
	public char charID;
	public boolean bold = false;
	public Vector4f color = new Vector4f(0, 0, 0, 1);

	public TChar(char charID, boolean bold, Vector4f color) {
		this.charID = charID;
		this.bold = bold;
		this.color = color;
	}

	public TChar(char charID) {
		this.charID = charID;
	}
}
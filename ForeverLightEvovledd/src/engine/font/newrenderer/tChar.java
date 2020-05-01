package engine.font.newrenderer;

import org.joml.Vector4f;

public class tChar {
	public char charID;
	public boolean bold = false;
	public Vector4f color = new Vector4f(1, 1, 1, 1);

	public tChar(char charID, boolean bold, Vector4f color) {
		this.charID = charID;
		this.bold = bold;
		this.color = color;
	}

	public tChar(char charID) {
		this.charID = charID;
	}
}
package engine.font.newrenderer;

import java.util.ArrayList;

import org.joml.Vector2f;

public class StringObject {
	final ArrayList<TextObject> textObjects = new ArrayList<>();
	public String fontName;
	public String text;
	public Vector2f position;
	public float rotation;
	public int size;
	public float lineLength;
	public float linePadding;

	public void addTextObjects(TextObject textObject) {
		textObjects.add(textObject);
	}

	public ArrayList<TextObject> getTextObjects() {
		return textObjects;
	}
}

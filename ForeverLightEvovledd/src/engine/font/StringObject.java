package engine.font;

import java.awt.List;
import java.util.ArrayList;

public class StringObject {
	final ArrayList<TextObject> textObjects = new ArrayList<>();

	public void addTextObjects(TextObject textObject) {
		textObjects.add(textObject);
	}

	public ArrayList<TextObject> getTextObjects() {
		return textObjects;
	}
}

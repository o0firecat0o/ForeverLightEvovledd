package engine.font.newrenderer;

import java.util.ArrayList;

import org.joml.Vector2f;

public class StringObject {
	final ArrayList<TextObject> textObjects = new ArrayList<>();
	public ArrayList<TString> seperatedString = new ArrayList<>();
	public String fontName;
	public String text;
	public Vector2f position;
	public float rotation;
	public float size;
	public float lineLength;
	public float linePadding;
	public boolean isUIObject = false;

	public void addTextObjects(TextObject textObject) {
		textObjects.add(textObject);
	}

	public ArrayList<TextObject> getTextObjects() {
		return textObjects;
	}

	public StringObject clearTextObjects() {
		textObjects.clear();
		return this;
	}

	public void createProcessedString() {
		//////////////////////////////////////////////////////
		// convert the string from string to TString for better manipulation
		TString tText = new TString(text);
		//////////////////////////////////////////////////////

		//////////////////////////////////////////////////////
		// change char between \b and \r to bolding and different color
		tText.convert_b_ToBold();
		tText.convert_r_ToColor();
		//////////////////////////////////////////////////////

		//////////////////////////////////////////////////////
		// seperate the string into lines
		// for each \n, new line is created
		// if string length > line length, new line is created
		seperatedString = TString.SeperateString(fontName, tText, lineLength);
		//////////////////////////////////////////////////////
	}
}

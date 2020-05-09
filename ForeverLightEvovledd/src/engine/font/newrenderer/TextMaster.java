package engine.font.newrenderer;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector4f;

import engine.font.newrenderer.Atlas.Glyph;
import engine.math.Mathf;

public class TextMaster {

	// TODO: add bolding to text
	// TODO: load automatically via reading files
	public static void LoadDefaultText() {
		Atlas.createAtlas("Utsaah");
		TextRendererMaster.NewTextRenderCage("Utsaah");

	}

	// the render loop will loop through all stringObjects and render the
	// TextObjects on the screen
	public static ArrayList<StringObject> stringOjbects = new ArrayList<>();

	public static StringObject CreateText(String fontName, String text, Vector2f position, float rotation, int size,
			float lineLength, float linePadding) {

		//////////////////////////////////////////////////////
		// convert the string from string to TString for better manipulation
		TString tText = new TString(text);
		//////////////////////////////////////////////////////

		// create the stringObject to store all the information about the string, also
		// the rendering textObjects, for later use of amend, delete, transform and
		// rotate
		StringObject stringObject = new StringObject();

		//////////////////////////////////////////////////////
		// change char between \b and \r to bolding and different color
		tText.convert_b_ToBold();
		tText.convert_r_ToColor();
		//////////////////////////////////////////////////////

		//////////////////////////////////////////////////////
		// seperate the string into lines
		// for each \n, new line is created
		// if string length> line length, new line is created
		ArrayList<TString> seperatedString = TString.SeperateString(fontName, tText, lineLength);
		//////////////////////////////////////////////////////

		// store all information into the stringObject
		stringObject.fontName = fontName;
		stringObject.position = position;
		stringObject.rotation = rotation;
		stringObject.size = size;
		stringObject.text = text;
		stringObject.lineLength = lineLength;
		stringObject.linePadding = linePadding;
		stringObject.seperatedString = seperatedString;

		//////////////////////////////////////////////////////
		// instantiate textobject for each line

		createTextObjects(stringObject, seperatedString);
		//////////////////////////////////////////////////////
		stringOjbects.add(stringObject);

		return stringObject;

		//////////////////////////////////////////////////////
	}

	public static StringObject amendToText(StringObject stringObject, String text) {
		TString tText = new TString(stringObject.text + text);
		tText.convert_b_ToBold();
		tText.convert_r_ToColor();
		ArrayList<TString> seperatedString = TString.SeperateString(stringObject.fontName, tText,
				stringObject.lineLength);
		createTextObjects(stringObject, seperatedString);
		return stringObject;
	}

	public static StringObject translate(StringObject stringObject, Vector2f position, float rotation) {
		stringObject.position = position;
		stringObject.rotation = rotation;
		createTextObjects(stringObject, stringObject.seperatedString);
		return stringObject;
	}

	private static StringObject createTextObjects(StringObject stringObject, ArrayList<TString> seperatedString) {
		// clear the previous textObjects before creating new one
		stringObject.clearTextObjects();

		///////////////////////////////////////////////////////
		// load the textAtlas
		Atlas atlas;
		try {
			atlas = Atlas.getAtlas(stringObject.fontName);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		///////////////////////////////////////////////////////

		for (int i = 0; i < seperatedString.size(); i++) {
			TString singleLine = seperatedString.get(i);
			// ******************************
			// align to left
			float advancepointer = 0;
			for (int j = 0; j < singleLine.length(); j++) {
				TChar tc = singleLine.getChar(j);
				char c = singleLine.getChar(j).charID;
				Glyph g = atlas.getGlyph(c);
				Vector2f addedVector = new Vector2f(
						(g.xoffset + g.width / 2 + advancepointer) * 1.2f * stringObject.size,
						(-g.yoffset - g.height / 2 - i * stringObject.linePadding) * stringObject.size);
				Vector2f rotatedVector = Mathf.rotateVectorAroundPoint(addedVector, stringObject.rotation,
						stringObject.position);
				TextObject textObject = new TextObject(Mathf.addVector(stringObject.position, rotatedVector, 0),
						stringObject.rotation,
						new Vector2f(g.width / 100f * stringObject.size, g.height / 100f * stringObject.size), tc.color,
						c);
				textObject.setBolding(tc.bold);
				stringObject.addTextObjects(textObject);
				advancepointer += g.xadvance;
			}

			// ******************************

			// TODO: align to center
		}

		return stringObject;
	}
}

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
		TextRenderer.NewTextRenderCage("Utsaah");

	}

	public static ArrayList<StringObject> stringOjbects = new ArrayList<>();

	public static StringObject CreateText(String fontName, String text, Vector2f position, float rotation, int size,
			float lineLength, float linePadding) {

		///////////////////////////////////////////////////////
		// load the textAtlas
		Atlas atlas;
		try {
			atlas = Atlas.getAtlas(fontName);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		///////////////////////////////////////////////////////

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
		// if string length> line length, new line is created
		ArrayList<TString> seperatedString = TString.SeperateString(fontName, tText, lineLength);
		//////////////////////////////////////////////////////

		//////////////////////////////////////////////////////
		// instantiate textobject for each line
		StringObject stringObject = new StringObject();

		for (int i = 0; i < seperatedString.size(); i++) {
			TString singleLine = seperatedString.get(i);

			// ******************************
			// align to left
			float advancepointer = 0;
			for (int j = 0; j < singleLine.length(); j++) {
				TChar tc = singleLine.getChar(j);
				char c = singleLine.getChar(j).charID;
				Glyph g = atlas.getGlyph(c);
				Vector2f addedVector = new Vector2f((g.xoffset + g.width / 2 + advancepointer) * 2 * size,
						(-g.yoffset - g.height / 2 - i * linePadding) * size);
				Vector2f rotatedVector = Mathf.rotateVectorAroundPoint(addedVector, rotation, position);
				TextObject textObject = new TextObject(Mathf.addVector(position, rotatedVector, 0), rotation,
						new Vector2f(g.width / 100f * size, g.height / 100f * size), tc.color, c);
				stringObject.addTextObjects(textObject);
				advancepointer += g.xadvance;
			}

			// ******************************

			// TODO: align to center
		}
		//////////////////////////////////////////////////////
		stringObject.fontName = fontName;

		stringOjbects.add(stringObject);

		return stringObject;

		//////////////////////////////////////////////////////
	}

	// TODO: add amend text function
}

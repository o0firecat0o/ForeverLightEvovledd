package engine.font;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector4f;

import engine.math.Mathf;

public class TextMaster {

	// TODO: add color to text
	// TODO: load automatically via reading files
	public static void LoadDefaultText() {
		Atlas.createAtlas("Utsaah");
		TextRenderer.NewTextRenderCage("Utsaah");

	}

	public static ArrayList<StringObject> stringOjbects = new ArrayList<>();

	// TODO: add Vector rotation
	public static StringObject CreateText(String fontName, String text, Vector2f position, float rotation, int size,
			float lineLength, float linePadding) {

		///////////////////////////////////////////////////////
		// load the textAtlas
		Atlas atlas;
		try {
			atlas = Atlas.getAtlas(fontName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		//////////////////////////////////////////////////////

		//////////////////////////////////////////////////////
		// seperate the text into different line
		ArrayList<String> seperatedString = new ArrayList<>();

		lineLength = lineLength * 50f;
		float templength = 0;
		String tempString = "";
		for (int i = 0; i < text.length(); i++) {
			tempString += text.charAt(i);
			templength += atlas.getGlyph(text.charAt(i)).xadvance;
			if ((text.charAt(i) == ' ')) {
				if (templength > lineLength) {
					seperatedString.add(tempString);
					tempString = "";
					templength = 0;
				}
			}
		}
		// add the final line
		if (!tempString.isEmpty()) {
			seperatedString.add(tempString);
		}
		//////////////////////////////////////////////////////

		//////////////////////////////////////////////////////
		// instantiate textobject for each line
		StringObject stringObject = new StringObject();

		for (int i = 0; i < seperatedString.size(); i++) {
			String singleLine = seperatedString.get(i);
			float singleLineLength = 0;
			for (int j = 0; j < singleLine.length(); j++) {
				singleLineLength += atlas.getGlyph(singleLine.charAt(j)).xadvance;
			}

			// ******************************
			// align to left
			float advancepointer = 0;
			for (int j = 0; j < singleLine.length(); j++) {
				char c = singleLine.charAt(j);
				Vector2f addedVector = new Vector2f(
						(atlas.getGlyph(c).xoffset + atlas.getGlyph(c).width / 2 + advancepointer) * 2 * size,
						(-atlas.getGlyph(c).yoffset - atlas.getGlyph(c).height / 2 - i * linePadding) * size);
				Vector2f rotatedVector = Mathf.rotateVectorAroundPoint(addedVector, rotation, position);
				TextObject textObject = new TextObject(Mathf.addVector(position, rotatedVector, 0), rotation,
						new Vector2f(atlas.getGlyph(c).width / 100f * size, atlas.getGlyph(c).height / 100f * size),
						new Vector4f(1, 1, 1, 1), c);
				stringObject.addTextObjects(textObject);
				advancepointer += atlas.getGlyph(c).xadvance;
			}

			// ******************************
		}
		//////////////////////////////////////////////////////
		stringObject.fontName = fontName;

		stringOjbects.add(stringObject);

		return stringObject;

		//////////////////////////////////////////////////////
	}
}

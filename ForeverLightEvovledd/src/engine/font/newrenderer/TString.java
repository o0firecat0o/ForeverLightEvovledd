package engine.font.newrenderer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import engine.math.Mathf;

public class TString {
	ArrayList<TChar> charList = new ArrayList<>();

	public TString(String s) {
		for (int i = 0; i < s.length(); i++) {
			charList.add(new TChar(s.charAt(i)));
		}
	}

	public TChar getChar(int location) {
		return charList.get(location);
	}

	public int length() {
		return charList.size();
	}

	public ArrayList<TChar> getCharList() {
		return charList;
	}

	public float getGlyphLength(String fontName) {
		Atlas atlas;
		try {
			atlas = Atlas.getAtlas(fontName);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

		float singleLineLength = 0;
		for (int j = 0; j < length(); j++) {
			singleLineLength += atlas.getGlyph(getChar(j).charID).xadvance;
		}
		return singleLineLength;
	}

	public TString appendChar(char c) {
		TChar char1 = new TChar(c);
		charList.add(char1);
		return this;
	}

	public TString clear() {
		charList.clear();
		return this;
	}

	public boolean isEmpty() {
		if (charList.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	public TString appendChar(TChar c) {
		charList.add(c);
		return this;
	}

	// change the char inbetween two /b to bold
	public TString convert_b_ToBold() {
		boolean b = false;

		for (Iterator<TChar> iterator = charList.iterator(); iterator.hasNext();) {
			TChar c = iterator.next();
			if (c.charID == '\b') {
				b = !b;
				iterator.remove();
				continue;
			}
			if (b) {
				c.bold = true;
			}
		}
		return this;
	}

	public TString convert_r_ToColor() {
		boolean r = false;
		int counter = 0;
		String colorString = "";

		// remove all /b character
		for (Iterator<TChar> iterator = charList.iterator(); iterator.hasNext();) {
			TChar c = iterator.next();
			if (c.charID == '\r') {
				r = !r;
				iterator.remove();
				continue;
			}
			if (r) {
				// if it is still in the color string
				if (counter < 7) {
					colorString += c.charID;
					counter++;
					// if it has finish reading the color, generate the color Vector4f
					if (counter == 7) {
						try {
							Mathf.ColorToVector4f(Color.decode(colorString));
						} catch (NumberFormatException e) {
							System.err.println("Please ensure that the valid color code is entered, eg #c93131"
									+ ". The color code entered: " + colorString + " is invalid.");
						}

					}
					iterator.remove();
					continue;
				}
				c.color = Mathf.ColorToVector4f(Color.decode(colorString));
			}
		}
		return this;
	}

	public static ArrayList<TString> SeperateString(String fontName, TString string, float lineLength) {
		// load the atlas
		Atlas atlas;
		try {
			atlas = Atlas.getAtlas(fontName);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		ArrayList<TString> returnList = new ArrayList<>();
		lineLength = lineLength * 50f;

		float templength = 0;
		TString tempString = new TString("");
		for (int i = 0; i < string.length(); i++) {
			if ((string.getChar(i).charID == ' ')) {
				if (templength > lineLength) {
					returnList.add(tempString);
					tempString = new TString("");
					templength = 0;
					continue;
				}
			}
			if ((string.getChar(i).charID == '\n')) {
				returnList.add(tempString);
				tempString = new TString("");
				templength = 0;
				continue;
			}
			tempString.appendChar(string.getChar(i));
			templength += atlas.getGlyph(string.getChar(i).charID).xadvance;
		}
		// add the final line
		if (!tempString.isEmpty()) {
			returnList.add(tempString);
		}

		return returnList;
	}
}
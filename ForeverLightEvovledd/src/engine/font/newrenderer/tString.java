package engine.font.newrenderer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import org.joml.Vector4f;

import engine.math.Mathf;

public class tString {
	ArrayList<tChar> charList = new ArrayList<>();

	public tString(String s) {
		for (int i = 0; i < s.length(); i++) {
			charList.add(new tChar(s.charAt(i)));
		}
	}

	public tChar getChar(int location) {
		return charList.get(location);
	}

	public int length() {
		return charList.size();
	}

	public ArrayList<tChar> getCharList() {
		return charList;
	}

	// change the char inbetween two /b to bold
	public tString convert_b_ToBold() {
		boolean b = false;

		for (Iterator<tChar> iterator = charList.iterator(); iterator.hasNext();) {
			tChar c = iterator.next();
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

	public tString convert_r_ToColor() {
		boolean r = false;
		int counter = 0;
		String colorString = "";

		// remove all /b character
		for (Iterator<tChar> iterator = charList.iterator(); iterator.hasNext();) {
			tChar c = iterator.next();
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
}
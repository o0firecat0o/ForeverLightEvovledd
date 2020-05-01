package engine.font;

import java.awt.List;
import java.util.ArrayList;

import org.joml.Vector2f;

public class TextMaster {

	// TODO: load automatically via reading files
	public static void LoadDefaultText() {
		Atlas.createAtlas("Utsaah");
		TextRenderer.NewTextRenderCage("Utsaah");

	}

	public ArrayList<StringObject> stringOjbects = new ArrayList<>();

	public static void CreateText(String fontName, String text, Vector2f position, int size, int lineMax) {

	}
}

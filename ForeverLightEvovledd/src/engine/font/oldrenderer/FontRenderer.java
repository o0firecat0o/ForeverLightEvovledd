package engine.font.oldrenderer;

import java.util.ArrayList;

import engine.component.graphic.*;

//Maybe remove this?
public class FontRenderer {

	public static ArrayList<Font> allFontRenderer = new ArrayList<>();

	public static void render() {
		allFontRenderer.forEach(x -> x.Render());
	}
}

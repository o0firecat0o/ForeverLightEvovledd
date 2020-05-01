package engine.font;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.ArrayList;
import java.util.List;

import engine.component.graphic.instancedRendering.InstancedRenderCage;
import engine.component.graphic.instancedRendering.InstancedRenderObject;

public class TextRenderer {
	public static void Render() {

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		// TODO: add ignore depth

		for (int i = 0; i < textRenderCages.size(); i++) {
			textRenderCages.get(i).Render();
		}

		glDisable(GL_BLEND);
	}

	private static List<TextRenderCage> textRenderCages = new ArrayList<>();

	/**
	 * This function MUST be called in the render thread
	 * 
	 * @param instancedRenderObjects
	 * @param TextureID
	 * @param FrameBufferID
	 */
	public static void Add(List<TextObject> textObjects, String fontname) {
		TextRenderCage trc = getTextRenderCage(fontname);
		if (trc == null) {
			trc = NewTextRenderCage(fontname);
		}
		trc.textObjects.addAll(textObjects);
	}

	public static void Add(TextObject textObject, String fontname) {
		TextRenderCage trc = getTextRenderCage(fontname);
		if (trc == null) {
			trc = NewTextRenderCage(fontname);
		}
		trc.textObjects.add(textObject);
	}

	private static TextRenderCage NewTextRenderCage(String fontname) {
		TextRenderCage textRenderCage = new TextRenderCage(fontname);
		System.out.println("New T Render Cage for Texture:" + fontname);
		textRenderCages.add(textRenderCage);
		return textRenderCage;
	}

	private static TextRenderCage getTextRenderCage(String fontname) {
		for (int i = 0; i < textRenderCages.size(); i++) {
			if (textRenderCages.get(i).FontName == fontname) {
				return textRenderCages.get(i);
			}
		}
		return null;
	}
}

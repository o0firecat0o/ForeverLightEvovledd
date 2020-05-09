package engine.font.newrenderer;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.ArrayList;
import java.util.List;

import engine.component.graphic.Shader;

public class TextRendererMaster {
	public static void Render() {

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		// TODO: add ignore depth

		for (int i = 0; i < textRenderCages.size(); i++) {
			// UIObject and normal gameObject will be using different shader
			// so there will be two draw calls for each fontName, one for normal font and
			// one for UI font
			ArrayList<TextObject> defaultTextList = new ArrayList<>();
			ArrayList<TextObject> uiTextList = new ArrayList<>();
			TextRenderCage textRenderCage = textRenderCages.get(i);

			// loop through all the string, and add the textObjects in the string to the
			// temp list for rendering
			// if it is the same font with the rendering cages
			for (int j = 0; j < TextMaster.stringOjbects.size(); j++) {
				StringObject stringObject = TextMaster.stringOjbects.get(j);

				if (stringObject.fontName != textRenderCage.FontName) {
					continue;

				}

				if (stringObject.isUIObject) {
					uiTextList.addAll(stringObject.getTextObjects());
				} else {
					defaultTextList.addAll(stringObject.getTextObjects());
				}

			}
			// actual rendering for normal text
			Shader shader;
			shader = Shader.getShader("DefaultText");
			shader.enable();
			textRenderCage.Render(defaultTextList);
			shader.disable();

			// actual rendering for UI text
			shader = Shader.getShader("DefaultTextUI");
			shader.enable();
			textRenderCage.Render(uiTextList);
			shader.disable();
		}

		glDisable(GL_BLEND);
	}

	private static List<TextRenderCage> textRenderCages = new ArrayList<>();

	public static TextRenderCage NewTextRenderCage(String fontname) {
		TextRenderCage textRenderCage = new TextRenderCage(fontname);
		System.out.println("New Text Render Cage for Texture:" + fontname);
		textRenderCages.add(textRenderCage);
		return textRenderCage;
	}
}

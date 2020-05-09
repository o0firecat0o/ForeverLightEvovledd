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
import engine.component.graphic.instancedRendering.InstancedRenderCage;
import engine.component.graphic.instancedRendering.InstancedRenderObject;

public class TextRendererMaster {
	public static void Render() {

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		// TODO: add ignore depth

		Shader shader = Shader.getShader("DefaultText");
		shader.enable();

		for (int i = 0; i < textRenderCages.size(); i++) {
			for (int j = 0; j < TextMaster.stringOjbects.size(); j++) {
				if (TextMaster.stringOjbects.get(j).fontName == textRenderCages.get(i).FontName) {
					textRenderCages.get(i).Render(TextMaster.stringOjbects.get(j).getTextObjects());
				}
			}
		}

		shader.disable();

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

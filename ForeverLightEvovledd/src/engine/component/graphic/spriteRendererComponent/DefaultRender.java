package engine.component.graphic.spriteRendererComponent;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

import org.joml.*;
import org.lwjgl.opengl.GL11;

import engine.component.graphic.*;
import engine.main.Render;
import engine.math.Maths;
import engine.object.*;

public class DefaultRender extends SpriteRendererComponent {

	public final Vector3f Color = new Vector3f(1, 1, 1);

	@Override
	public void Render(int FrameBufferObjectID) {

		GL11.glEnable(GL11.GL_BLEND);

		Shader shader;

		GameObject gameObject = spriteRenderer.gameObject;

		if (gameObject instanceof UIObject) {
			shader = Shader.getShader("UI");
		} else {
			shader = Shader.getShader("DEFAULT");
		}

		// if not intended to overide texture
		if (TextureOveride == -1) {
			GL11.glBindTexture(GL_TEXTURE_2D, spriteRenderer.texture);
		} else {
			GL11.glBindTexture(GL_TEXTURE_2D, TextureOveride);
		}

		shader.enable();
		if (gameObject == null || gameObject.transform == null) {
			return;
		}
		shader.setUniformMat4f("ml_matrix",
				Maths.createTransformationMatrix(gameObject.transform.position, gameObject.transform.rotation,
						new Vector2f(gameObject.transform.getScale().x * spriteRenderer.graphicScaleOffset,
								gameObject.transform.getScale().y * spriteRenderer.graphicScaleOffset)));
		shader.setUniform3f("colorTaint", Color);

		SpriteRenderer.mesh_NORAML.render();

		shader.disable();
		glBindTexture(GL_TEXTURE_2D, 0);

		GL11.glDisable(GL11.GL_BLEND);
	}

	@Override
	public void Update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void Start() {
		// TODO Auto-generated method stub
		FrameBufferIDs.add(Render.mainFrameBuffer.FrameBufferID);
	}

}

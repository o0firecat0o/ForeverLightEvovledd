package engine.component.graphic.spriteRendererComponent;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.GL_TEXTURE2;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import engine.component.graphic.Shader;
import engine.component.graphic.Texture;
import engine.component.graphic.VertexArray;
import engine.main.Render;
import engine.math.Mathf;
import engine.math.Maths;

public class DissolveRenderer extends SpriteRendererComponent {

	float dissolveAmount = 0;
	Vector3f color = new Vector3f(1.0f, 0.4f, 0);

	@Override
	public void Render(int FrameBufferObjectID) {
		GL11.glEnable(GL11.GL_BLEND);

		Shader shader;

		shader = Shader.getShader("Dissolve");

		glActiveTexture(GL_TEXTURE1);
		GL11.glBindTexture(GL_TEXTURE_2D, texture);

		glActiveTexture(GL_TEXTURE2);
		GL11.glBindTexture(GL_TEXTURE_2D, Texture.getTexture("clouds_n"));

		shader.enable();

		shader.setUniform1f("dissolveAmount", Mathf.fract(dissolveAmount));
		shader.setUniform3f("dissolveColor", color);

		if (gameObject == null || gameObject.transform == null) {
			return;
		}
		shader.setUniformMat4f("ml_matrix",
				Maths.createTransformationMatrix(gameObject.transform.position, gameObject.transform.rotation,
						new Vector2f(gameObject.transform.getScale().x * graphicScaleOffset,
								gameObject.transform.getScale().y * graphicScaleOffset)));

		VertexArray.mesh_NORAML.render();

		shader.disable();

		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, 0);

		GL11.glDisable(GL11.GL_BLEND);
	}

	@Override
	protected void Update() {
		dissolveAmount += 0.01f;
	}

	@Override
	protected void Start() {
		SetFrameBuffer(Render.mainFrameBuffer);
	}

}

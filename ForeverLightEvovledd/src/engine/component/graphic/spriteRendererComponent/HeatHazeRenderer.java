package engine.component.graphic.spriteRendererComponent;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;

import engine.component.graphic.Shader;
import engine.component.graphic.VertexArray;
import engine.main.Render;
import engine.math.Maths;

public class HeatHazeRenderer extends SpriteRendererComponent {

	@Override
	public void Render(int FrameBufferObjectID) {
		GL11.glEnable(GL11.GL_BLEND);

		Shader shader;

		shader = Shader.getShader("RippleObject");

		GL11.glBindTexture(GL_TEXTURE_2D, texture);

		shader.enable();
		if (gameObject == null || gameObject.transform == null) {
			return;
		}

		shader.setUniformMat4f("ml_matrix",
				Maths.createTransformationMatrix(gameObject.transform.position, gameObject.transform.rotation,
						new Vector2f(gameObject.transform.getScale().x * graphicScaleOffset,
								gameObject.transform.getScale().y * graphicScaleOffset)));

		VertexArray.mesh_NORAML.render();

		shader.disable();
		glBindTexture(GL_TEXTURE_2D, 0);

		GL11.glDisable(GL11.GL_BLEND);
	}

	@Override
	protected void Update() {

	}

	@Override
	protected void Start() {
		SetFrameBuffer(Render.heathazeFrameBuffer);
	}

}

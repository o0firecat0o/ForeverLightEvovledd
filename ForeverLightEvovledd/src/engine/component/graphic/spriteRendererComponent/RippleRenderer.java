package engine.component.graphic.spriteRendererComponent;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;

import engine.component.graphic.Shader;
import engine.component.graphic.VertexArray;
import engine.main.Render;
import engine.math.Mathf;
import engine.math.Maths;

public class RippleRenderer extends SpriteRendererComponent {

	public float alpha = 1f;
	public float age = 0f;
	public float life = 120f; // life in frames;
	public float scale_max = 3;
	public float rotationSpeed = 0;

	@Override
	public void Render(int FrameBufferObjectID) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_DST_COLOR);

		Shader shader;

		shader = Shader.getShader("RippleObject");

		GL11.glBindTexture(GL_TEXTURE_2D, texture);

		shader.enable();
		if (gameObject == null || gameObject.transform == null) {
			return;
		}

		shader.setUniform1f("alpha", alpha);

		shader.setUniformMat4f("ml_matrix",
				Maths.createTransformationMatrix(gameObject.transform.position, gameObject.transform.rotation,
						new Vector2f(gameObject.transform.getScale().x * graphicScaleOffset,
								gameObject.transform.getScale().y * graphicScaleOffset)));

		VertexArray.mesh_NORAML.render();

		shader.disable();
		glBindTexture(GL_TEXTURE_2D, 0);

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	protected void Update() {
		age++;
		float agePercentage = Mathf.concaveDownwardIncreasingInterpolation(0, life, age);
		alpha = 1 - agePercentage;
		if (alpha <= 0) {
			gameObject.InitDestroy();
		}
		float scale = agePercentage * (scale_max - 1) + 1;
		gameObject.transform.setScale(scale);
		gameObject.transform.setRotation(gameObject.transform.getRotation() + rotationSpeed);
	}

	@Override
	protected void Start() {
		SetFrameBuffer(Render.rippleFrameBuffer);
	}

}

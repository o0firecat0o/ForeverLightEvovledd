package engine.font.newrenderer;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import java.io.Console;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import org.joml.Matrix4f;
import org.lwjgl.opengl.*;

import engine.component.graphic.*;
import engine.utils.FileUtils;

public class TextRenderCage {
	public final int TextureID; // the texture of the text atlas
	public final String FontName;

	private VertexArray mesh;
	private int vbo;

	private static final int MAX_INSTANCES = 50000;
	private static final int INSTANCE_DATA_LENGTH = 24;

	private int pointer = 0;

	private final FloatBuffer BUFFER = org.lwjgl.BufferUtils
			.createFloatBuffer(MAX_INSTANCES * INSTANCE_DATA_LENGTH * 4);

	public Atlas atlas;

	public TextRenderCage(String fontname) {
		this.TextureID = Texture.getTexture(fontname);
		this.FontName = fontname;

		try {
			atlas = Atlas.getAtlas(fontname);
		} catch (Exception e) {
			e.printStackTrace();
		}

		float SIZE_X = 100 / 2;
		float SIZE_Y = 100 / 2;
		float[] vertices = new float[] { -SIZE_X, -SIZE_Y, 0f, -SIZE_X, SIZE_Y, 0f, SIZE_X, SIZE_Y, 0f, SIZE_X, -SIZE_Y,
				0f };
		byte[] indices = new byte[] { 0, 1, 2, 2, 3, 0 };
		float[] tcs = new float[] { 0, 1, 0, 0, 1, 0, 1, 1 };

		mesh = new VertexArray(vertices, indices, tcs);

		vbo = mesh.createEmptyVBO(INSTANCE_DATA_LENGTH * MAX_INSTANCES);
		mesh.addInstancedAttribute(vbo, 2, 4, INSTANCE_DATA_LENGTH, 0); // matrix4f
		mesh.addInstancedAttribute(vbo, 3, 4, INSTANCE_DATA_LENGTH, 4);
		mesh.addInstancedAttribute(vbo, 4, 4, INSTANCE_DATA_LENGTH, 8);
		mesh.addInstancedAttribute(vbo, 5, 4, INSTANCE_DATA_LENGTH, 12);
		mesh.addInstancedAttribute(vbo, 6, 4, INSTANCE_DATA_LENGTH, 16); // color
		mesh.addInstancedAttribute(vbo, 7, 2, INSTANCE_DATA_LENGTH, 20); // graphicx and graphicy
		mesh.addInstancedAttribute(vbo, 8, 2, INSTANCE_DATA_LENGTH, 22); // width and height
	}

	public void Render(ArrayList<TextObject> textObjects) {
		if (textObjects.size() > MAX_INSTANCES) {
			throw new RuntimeException(
					"MaxInstances Reached for texture:" + TextureID + "," + Texture.getTexture(TextureID));
		}

		pointer = 0;
		glActiveTexture(GL_TEXTURE1);
		GL11.glBindTexture(GL_TEXTURE_2D, TextureID);

		Shader shader = Shader.getShader("DefaultText");

		shader.enable();

		float[] vboDATA = new float[textObjects.size() * INSTANCE_DATA_LENGTH];
		for (int i = 0; i < textObjects.size(); i++) {
			TextObject object = textObjects.get(i);
			Atlas.Glyph glyph = atlas.getGlyph(object.charID);
			storeMatrixData(object.matrix4f, vboDATA);
			vboDATA[pointer++] = object.Color.x;
			vboDATA[pointer++] = object.Color.y;
			vboDATA[pointer++] = object.Color.z;
			vboDATA[pointer++] = object.Color.w;
			vboDATA[pointer++] = glyph.graphicx / 512f;
			vboDATA[pointer++] = glyph.graphicy / 512f;
			vboDATA[pointer++] = glyph.width / 512f;
			vboDATA[pointer++] = glyph.height / 512f;
		}

		updateVBO(vbo, vboDATA, BUFFER);

		mesh.bind();

		// shader.setUniformMat4f("pr_matrix",
		// SpriteRenderer.pr_matrix).setUniform1i("tex", 1);

		mesh.drawParticle(textObjects.size());

		shader.disable();

		glBindTexture(GL_TEXTURE_2D, 0);
	}

	private void storeMatrixData(Matrix4f matrix, float[] vboData) {
		vboData[pointer++] = matrix.m00();
		vboData[pointer++] = matrix.m01();
		vboData[pointer++] = matrix.m02();
		vboData[pointer++] = matrix.m03();
		vboData[pointer++] = matrix.m10();
		vboData[pointer++] = matrix.m11();
		vboData[pointer++] = matrix.m12();
		vboData[pointer++] = matrix.m13();
		vboData[pointer++] = matrix.m20();
		vboData[pointer++] = matrix.m21();
		vboData[pointer++] = matrix.m22();
		vboData[pointer++] = matrix.m23();
		vboData[pointer++] = matrix.m30();
		vboData[pointer++] = matrix.m31();
		vboData[pointer++] = matrix.m32();
		vboData[pointer++] = matrix.m33();
	}

	public void updateVBO(int vbo, float[] data, FloatBuffer buffer) {
		buffer.clear();
		buffer.put(data);
		buffer.flip();
		if (vbo != -1) {
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer.capacity(), GL15.GL_STREAM_DRAW);
			GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, buffer);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		}
	}
}
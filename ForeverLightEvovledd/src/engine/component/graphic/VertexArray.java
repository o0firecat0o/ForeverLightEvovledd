package engine.component.graphic;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import org.lwjgl.opengl.*;

import engine.utils.BufferUtils;

public class VertexArray {

	// TODO: make this cleaner

	private int count;
	public int vao, vbo, ibo, tbo;

	public static VertexArray mesh_NORAML;
	public static VertexArray mesh_FULSCREEN;

	// mesh for all normal sprite
	public static void loadDefaultMesh_normal() {
		float SIZE_X = 100 / 2;
		float SIZE_Y = 100 / 2;
		float[] vertices = new float[] { -SIZE_X, -SIZE_Y, 0f, -SIZE_X, SIZE_Y, 0f, SIZE_X, SIZE_Y, 0f, SIZE_X, -SIZE_Y,
				0f };
		byte[] indices = new byte[] { 0, 1, 2, 2, 3, 0 };
		float[] tcs = new float[] { 0, 1, 0, 0, 1, 0, 1, 1 };

		mesh_NORAML = new VertexArray(vertices, indices, tcs);
	}

	// full screen mesh is used in FBO rendering
	public static void loadDefaultMesh_FBO() {
		float SIZE_X = 1f;
		float SIZE_Y = 1f;
		float[] vertices = new float[] { 0, 0, 0f, 0, SIZE_Y, 0f, SIZE_X, SIZE_Y, 0f, SIZE_X, 0, 0f };
		byte[] indices = new byte[] { 0, 1, 2, 2, 3, 0 };
		float[] tcs = new float[] { 0, 1, 0, 0, 1, 0, 1, 1 };

		mesh_FULSCREEN = new VertexArray(vertices, indices, tcs);
	}

	public VertexArray(float[] vertices, byte[] indices, float[] textureCoordinates) {
		count = indices.length;

		vao = glGenVertexArrays();
		glBindVertexArray(vao);

		vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(vertices), GL_STATIC_DRAW);
		glVertexAttribPointer(Shader.VERTEX_ATTRIB, 3, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(Shader.VERTEX_ATTRIB);

		tbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, tbo);
		glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(textureCoordinates), GL_STATIC_DRAW);
		glVertexAttribPointer(Shader.TCOORD_ATTRIB, 2, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(Shader.TCOORD_ATTRIB);

		ibo = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferUtils.createByteBuffer(indices), GL_STATIC_DRAW);

		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}

	public int createEmptyVBO(int floatCount) {
		glBindVertexArray(vao);
		int Nvbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, Nvbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, floatCount * 4, GL15.GL_STREAM_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
		return Nvbo;
	}

	public void addInstancedAttribute(int vbo, int attribute, int dataSize, int instancedDataLength, int offset) {
		glBindVertexArray(vao);
		// bind the vbo
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL20.glVertexAttribPointer(attribute, dataSize, GL11.GL_FLOAT, false, instancedDataLength * 4, offset * 4);
		GL33.glVertexAttribDivisor(attribute, 1);

		glEnableVertexAttribArray(attribute);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}

	public void bind() {
		glBindVertexArray(vao);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
	}

	public void unbind() {
		glBindVertexArray(0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	public void draw() {
		glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_BYTE, 0);
	}

	public void drawParticle(int number) {
		GL31.glDrawElementsInstanced(GL_TRIANGLES, count, GL_UNSIGNED_BYTE, 0, number);
	}

	public void render() {
		bind();
		draw();
	}

	public static void cleanMesh(VertexArray mesh) {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(mesh.vbo);

		GL30.glBindVertexArray(0);
		GL30.glDeleteVertexArrays(mesh.vao);
	}
}
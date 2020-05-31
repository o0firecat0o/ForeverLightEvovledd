package engine.component.graphic;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;

import engine.utils.BufferUtils;
import engine.utils.ShaderUtils;

public class Shader {

	public static final int VERTEX_ATTRIB = 0;
	public static final int TCOORD_ATTRIB = 1;

	private boolean enabled = false;

	public final int ID;
	public boolean isUIShader = false;
	private Map<String, Integer> locationCache = new HashMap<String, Integer>();
	// the static dictionary that contains all the NAME and ID of the texture to
	// prevent double instantiation
	private static Map<String, Shader> NAME_SHADER_DICTIONARY = new HashMap<String, Shader>();

	// ArrayList of shaders that will be rendered this frame, used for camera
	public static ArrayList<Shader> rendered_shader = new ArrayList<>();

	public static Object[] returnAllShaders() {
		return NAME_SHADER_DICTIONARY.values().toArray();
	}

	public static void loadDefault() {
		/*
		 * 
		 * NEVER COPY SHADERS DIRECTLY FROM ECLIPSE ITS FORMAT IS DIFFERENT FROM OPENGL
		 * 
		 * 
		 * 
		 * 
		 */

		Shader.createShader("shaders/default.vert", "shaders/default.frag", "DEFAULT")
				.setUniformMat4f("pr_matrix", SpriteRenderer.pr_matrix).setUniform1i("tex", 1);
		Shader.createShader("shaders/default.vert", "shaders/default.frag", "UI")
				.setUniformMat4f("pr_matrix", SpriteRenderer.pr_matrix).setUniform1i("tex", 1).isUIShader();
		Shader.createShader("shaders/BlurHorizontal.vert", "shaders/BlurFragmentShader.frag", "HBlur")
				.setUniformMat4f("pr_matrix", SpriteRenderer.pr_matrix).setUniform1i("tex", 1).isUIShader();
		Shader.createShader("shaders/BlurVertical.vert", "shaders/BlurFragmentShader.frag", "VBlur")
				.setUniformMat4f("pr_matrix", SpriteRenderer.pr_matrix).setUniform1i("tex", 1).isUIShader();
		Shader.createShader("shaders/default.vert", "shaders/bloom.frag", "Bloom")
				.setUniformMat4f("pr_matrix", SpriteRenderer.pr_matrix).setUniform1i("tex", 1).setUniform1i("tex2", 2)
				.isUIShader();

		// ripple shader
		Shader.createShader("shaders/default.vert", "shaders/RippleDistortion.frag", "RippleDistortion")
				.setUniformMat4f("pr_matrix", SpriteRenderer.pr_matrix).setUniform1i("tex", 1).setUniform1i("tex2", 2)
				.isUIShader();
		Shader.createShader("shaders/default.vert", "shaders/RippleDistortionObject.frag", "RippleDistortionObject")
				.setUniformMat4f("pr_matrix", SpriteRenderer.pr_matrix).setUniform1i("tex", 1);

		// heatwave shader
		Shader.createShader("shaders/default.vert", "shaders/HeatHazeDistortion.frag", "HeatHazeDistortion")
				.setUniformMat4f("pr_matrix", SpriteRenderer.pr_matrix).setUniform1i("tex", 1).setUniform1i("tex2", 2)
				.isUIShader();
		Shader.createShader("shaders/default.vert", "shaders/HeatHazeDistortionObject.frag", "HeatHazeDistortionObject")
				.setUniformMat4f("pr_matrix", SpriteRenderer.pr_matrix).setUniform1i("tex", 1).setUniform1i("tex2", 2);

		// swirl shader
		Shader.createShader("shaders/default.vert", "shaders/SwirlDistortion.frag", "SwirlDistortion")
				.setUniformMat4f("pr_matrix", SpriteRenderer.pr_matrix).setUniform1i("tex", 1).isUIShader();

		// dissolve shader
		Shader.createShader("shaders/default.vert", "shaders/Dissolve.frag", "Dissolve")
				.setUniformMat4f("pr_matrix", SpriteRenderer.pr_matrix).setUniform1i("tex", 1).setUniform1i("tex2", 2);

		Shader.createShader("shaders/default.vert", "shaders/Shield.frag", "Shield")
				.setUniformMat4f("pr_matrix", SpriteRenderer.pr_matrix).setUniform1i("tex", 1).setUniform1i("tex2", 2)
				.setUniform1i("tex3", 3).setUniform1i("tex4", 4);
		Shader.createShader("shaders/default.vert", "shaders/Lazer.frag", "Lazer")
				.setUniformMat4f("pr_matrix", SpriteRenderer.pr_matrix).setUniform1i("tex", 1).setUniform1i("tex2", 2)
				.setUniform1i("tex3", 3).setUniform1i("tex4", 4).setUniform1i("tex5", 5).setUniform1i("tex6", 6)
				.setUniform1i("tex7", 7);
		Shader.createShader("shaders/default.vert", "shaders/LazerCurved.frag", "LazerCurved")
				.setUniformMat4f("pr_matrix", SpriteRenderer.pr_matrix);

		Shader.createShader("shaders/defaultParticle.vert", "shaders/defaultParticle.frag", "DefaultParticle")
				.setUniformMat4f("pr_matrix", SpriteRenderer.pr_matrix).setUniform1i("tex", 1);

		//////////////////////////////////////
		// New Font Renderer
		Shader.createShader("shaders/Text.vert", "shaders/Text.frag", "DefaultText")
				.setUniformMat4f("pr_matrix", SpriteRenderer.pr_matrix).setUniform1i("tex", 1);
		Shader.createShader("shaders/Text.vert", "shaders/Text.frag", "DefaultTextUI")
				.setUniformMat4f("pr_matrix", SpriteRenderer.pr_matrix).setUniform1i("tex", 1).isUIShader();
		/////////////////////////////////////

		/////////////////////////////////////
		// Old Font Renderer
		Shader.createShader("shaders/Font.vert", "shaders/Font.frag", "FONT")
				.setUniformMat4f("pr_matrix", SpriteRenderer.pr_matrix).setUniform1i("tex", 1).setUniform1i("tex2", 2);
		Shader.createShader("shaders/Font.vert", "shaders/Font.frag", "UIFONT")
				.setUniformMat4f("pr_matrix", SpriteRenderer.pr_matrix).setUniform1i("tex", 1).setUniform1i("tex2", 2)
				.isUIShader();
		/////////////////////////////////////

		Shader.createShader("shaders/default.vert", "shaders/CircularMeter.frag", "CircularMeter")
				.setUniformMat4f("pr_matrix", SpriteRenderer.pr_matrix);

	}

	public static Shader createShader(String vertex, String fragment, String Name) {
		if (NAME_SHADER_DICTIONARY.containsKey(Name)) {
			System.out.println("Shader already created, it this intended?");
			return NAME_SHADER_DICTIONARY.get(Name);
		} else {
			Shader shader = new Shader(vertex, fragment);
			NAME_SHADER_DICTIONARY.put(Name, shader);
			return shader;
		}
	}

	public static boolean hasShader(String Name) {
		if (NAME_SHADER_DICTIONARY.containsKey(Name)) {
			return true;
		} else {
			return false;
		}
	}

	public static Shader getShader(String Name) throws RuntimeException {
		Shader shader = NAME_SHADER_DICTIONARY.get(Name);
		if (!rendered_shader.contains(shader)) {
			rendered_shader.add(shader);
		}
		return shader;
	}

	public static int getID(String Name) {
		return NAME_SHADER_DICTIONARY.get(Name).ID;
	}

	public Shader isUIShader() {
		this.isUIShader = true;
		return this;
	}

	private Shader(String vertex, String fragment) {
		ID = ShaderUtils.load(vertex, fragment);
	}

	public int getUniform(String name) {
		if (locationCache.containsKey(name)) {
			return locationCache.get(name);
		}
		int result = glGetUniformLocation(ID, name);
		if (result == -1) {
			System.err.println("Could not find uniform variable '" + name
					+ "'! @shader/getUniform, make sure the uniform variables is used in the shaders");
		} else {
			locationCache.put(name, result);
		}
		return result;
	}

	public Shader setUniform1i(String name, int value) {
		if (!enabled)
			enable();
		glUniform1i(getUniform(name), value);
		return this;
	}

	public Shader setUniform1f(String name, float value) {
		if (!enabled)
			enable();
		glUniform1f(getUniform(name), value);
		return this;
	}

	public Shader setUniform2f(String name, float x, float y) {
		if (!enabled)
			enable();
		glUniform2f(getUniform(name), x, y);
		return this;
	}

	public Shader setUniform3f(String name, Vector3f vector3) {
		if (!enabled)
			enable();
		glUniform3f(getUniform(name), vector3.x, vector3.y, vector3.z);
		return this;
	}

	public Shader setUniformMat4f(String name, Matrix4f matrix) {
		if (!enabled)
			enable();
		glUniformMatrix4fv(getUniform(name), false,
				BufferUtils.createFloatBuffer(new float[] { matrix.m00(), matrix.m01(), matrix.m02(), matrix.m03(),
						matrix.m10(), matrix.m11(), matrix.m12(), matrix.m13(), matrix.m20(), matrix.m21(),
						matrix.m22(), matrix.m23(), matrix.m30(), matrix.m31(), matrix.m32(), matrix.m33() }));
		return this;
	}

	public Shader setUniform2fv(String name, ArrayList<Vector2f> arrayList) {
		if (!enabled)
			enable();
		float[] floatList = new float[2 * arrayList.size()];
		for (int i = 0; i < arrayList.size(); i++) {
			floatList[2 * i] = arrayList.get(i).x;
			floatList[2 * i + 1] = arrayList.get(i).y;
		}
		GL20.glUniform2fv(getUniform(name), BufferUtils.createFloatBuffer(floatList));
		return this;
	}

	@Deprecated
	// not yet tested
	public Shader setUniform3fv(String name, ArrayList<Vector3f> arrayList) {
		if (!enabled)
			enable();
		float[] floatList = new float[3 * arrayList.size()];
		for (int i = 0; i < arrayList.size(); i++) {
			floatList[3 * i] = arrayList.get(i).x;
			floatList[3 * i + 1] = arrayList.get(i).y;
			floatList[3 * i + 2] = arrayList.get(i).z;
		}
		GL20.glUniform3fv(getUniform(name), BufferUtils.createFloatBuffer(floatList));
		return this;
	}

	public Shader setUniform4fv(String name, ArrayList<Vector4f> arrayList) {
		if (!enabled)
			enable();
		float[] floatList = new float[4 * arrayList.size()];
		for (int i = 0; i < arrayList.size(); i++) {
			floatList[4 * i] = arrayList.get(i).x;
			floatList[4 * i + 1] = arrayList.get(i).y;
			floatList[4 * i + 2] = arrayList.get(i).z;
			floatList[4 * i + 3] = arrayList.get(i).w;
		}
		GL20.glUniform4fv(getUniform(name), BufferUtils.createFloatBuffer(floatList));
		return this;
	}

	public void enable() {
		glUseProgram(ID);
		enabled = true;
	}

	public void disable() {
		glUseProgram(0);
		enabled = false;
	}
}

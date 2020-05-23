package engine.main;

import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_FRAMEBUFFER_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glBindFramebufferEXT;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glFlush;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.GL_TEXTURE2;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.opengl.GLUtil;

import engine.component.Camera;
import engine.component.graphic.FrameBufferObject;
import engine.component.graphic.Shader;
import engine.component.graphic.SpriteRenderer;
import engine.component.graphic.Texture;
import engine.component.graphic.VertexArray;
import engine.component.graphic.instancedRendering.InstancedRenderer;
import engine.component.graphic.spriteRendererComponent.SpriteRendererComponent;
import engine.component.graphic.spriteRendererComponent.SwirlRenderer;
import engine.font.newrenderer.TextMaster;
import engine.font.newrenderer.TextRendererMaster;
import engine.font.oldrenderer.FontRenderer;
import engine.input.InputKey;
import engine.input.InputMouseButton;
import engine.input.InputMousePos;
import engine.input.InputMouseScroll;
import engine.math.Maths;
import engine.object.Component;
import engine.object.GameObject;
import engine.object.UpdatableObject;

public class Render implements Runnable {

	public static boolean running = false;
	public static long window;
	public static GLCapabilities glCapabilities;

	public static boolean finishInit = false;

	public static long fps;

	private static Vector4f BackGroundColor = new Vector4f(1.0f, 0.8f, 0.8f, 1.0f);

	public static FrameBufferObject heatHazeFrameBuffer;
	public static FrameBufferObject swirlFrameBuffer;
	public static FrameBufferObject rippleFrameBuffer; // For ripple effect
	public static FrameBufferObject bloomFrameBuffer; // For bloom effect
	public static FrameBufferObject glowFrameBuffer; // For glow only, without bloom
	public static FrameBufferObject mainFrameBuffer;

	private static FrameBufferObject blurProcessingBuffer;
	private static FrameBufferObject postBloomBuffer;
	private static FrameBufferObject postRippleBuffer;
	private static FrameBufferObject postHeatHazeBuffer;

	public static FrameBufferObject postProcessingBuffer; // frame buffer after all
	// distortion

	private void init() {
		if (!glfwInit()) {
			throw new IllegalStateException("Failed to init GLFW");
		}
		// set resizeable to false
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

		// TODO: check version

		// make the program use the highest OpenGL version possible
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

		window = glfwCreateWindow(Main.getWidth(), Main.getHeight(), "Tower Defense Evovled", NULL, NULL);

		if (window == NULL) {
			return;
		}

		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (vidmode.width() - Main.getWidth()) / 2, (vidmode.height() - Main.getHeight()) / 2);

		// set input keyboard
		glfwSetKeyCallback(window, new InputKey());
		// set input mouse position
		glfwSetCursorPosCallback(window, new InputMousePos());
		// set input mouse key
		glfwSetMouseButtonCallback(window, new InputMouseButton());
		// set input mouse scroll
		glfwSetScrollCallback(window, new InputMouseScroll());

		glfwSwapInterval(1); // enable vysnc, displaying 60fps

		glfwMakeContextCurrent(window);
		glfwShowWindow(window);
		glCapabilities = GL.createCapabilities();

		glClearDepth(1.0f); // Depth Buffer Setup
		glDepthFunc(GL_LEQUAL); // The Type Of Depth Testing (Less Or Equal)
		glEnable(GL_DEPTH_TEST); // Enable Depth Testing
		System.out.println("OpenGL: " + glGetString(GL_VERSION));

		// enalbe transpatency
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		// The texture is now in channel 1
		glActiveTexture(GL_TEXTURE1);
		// Load all of the default shader
		Shader.loadDefault();

		// Set the Background Color to Black?
		setBackGroundColor(new Vector4f(0f, 0f, 0f, 1.0f));

		// Create a FBO
		bloomFrameBuffer = new FrameBufferObject(Main.getWidth(), Main.getHeight());
		mainFrameBuffer = new FrameBufferObject(Main.getWidth(), Main.getHeight());
		postProcessingBuffer = new FrameBufferObject(Main.getWidth(), Main.getHeight());
		rippleFrameBuffer = new FrameBufferObject(Main.getWidth(), Main.getHeight());
		swirlFrameBuffer = new FrameBufferObject(Main.getWidth(), Main.getHeight());
		heatHazeFrameBuffer = new FrameBufferObject(Main.getWidth(), Main.getHeight());

		postHeatHazeBuffer = new FrameBufferObject(Main.getWidth(), Main.getHeight());
		postBloomBuffer = new FrameBufferObject(Main.getWidth(), Main.getHeight());
		postRippleBuffer = new FrameBufferObject(Main.getWidth(), Main.getHeight());

		// Creating an FBO with smaller size
		// remeber to set the viewport to smaller also
		blurProcessingBuffer = new FrameBufferObject(Main.getHeight() / 4, Main.getWidth() / 4);
		glowFrameBuffer = new FrameBufferObject(Main.getHeight(), Main.getWidth());

		// load default mesh
		VertexArray.loadDefaultMesh_normal();
		VertexArray.loadDefaultMesh_FBO();
		// load default textures
		Texture.LoadDefault(new File("res/Sprites"));
		// load default text atlas, and create the instance renderer for it
		TextMaster.LoadDefaultText();

		// Camera
		GameObject cameraObject = new GameObject();
		Camera.MAIN = cameraObject.AddComponent(new Camera());
		cameraObject.stay_on_stage = true;

	}

	@Override
	public void run() {
		init();

		running = true;
		finishInit = true;
		/*
		 * long lastTime = System.nanoTime(); double ns = 1000000000.0 / 60.0; double
		 * delta = 0.0; int updates = 0; int frames = 0; long timer =
		 * System.currentTimeMillis(); while (running) { long now = System.nanoTime();
		 * delta += (now - lastTime) / ns; lastTime = now; if (delta >= 1.0) { update();
		 * updates++; delta--; } render(); frames++; if (System.currentTimeMillis() -
		 * timer > 1000) { timer += 1000;
		 * 
		 * System.out.println(frames + "fps");
		 * 
		 * updates = 0; frames = 0; } if (glfwWindowShouldClose(window) == true) {
		 * running = false; } }
		 */

		long originalTime = System.currentTimeMillis();
		long accumulator = 0;

		while (running) {

			render();
			accumulator++;
			if (System.currentTimeMillis() - originalTime >= 1000) {
				originalTime = System.currentTimeMillis();
				fps = accumulator;
				accumulator = 0;
			}
			if (glfwWindowShouldClose(window) == true) {
				System.out.println("Application should Close");
				running = false;
			}
		}
		System.out.println("Application is Closing");
		glfwDestroyWindow(window);
		glfwTerminate();
		System.exit(0);
	}

	float timer = 0;

	private void render() {
		glfwPollEvents(); // Inputs

		// Start all gameObjects (Render Loop)
		for (int i = 0; i < Component.toStartComponent.size(); i++) {
			Component.toStartComponent.get(i).StartRender();
		}
		Component.toStartComponent.clear();

		// Update all gameObjects (Render Loop)
		for (int i = 0; i < UpdatableObject.AllUpdatableObject.size(); i++) {
			UpdatableObject updatableObject = UpdatableObject.AllUpdatableObject.get(i);
			if (updatableObject instanceof GameObject) {
				((GameObject) updatableObject).UpdateRender();
			}
		}

		// set the background color back to black before all rendering
		setBackGroundColor(new Vector4f(0, 0, 0, 0));

		////////////////////////////////////////////////////////////////////////////
		////// Rendering Start here

		// FBO rendering: BloomBuffer
		bloomFrameBuffer.bind();
		renderAll(bloomFrameBuffer.FrameBufferID);

		/////////////////////////////////////////////////////////////////////
		// FBO rendering: RippleDistortion

		rippleFrameBuffer.bind();
		// set the background color to lightblue for normal mapping
		fullScreenRender(Shader.getShader("UI"), Texture.getTexture("normalmapbg"), 0);

		renderAll(rippleFrameBuffer.FrameBufferID);
		/////////////////////////////////////////////////////////////////////

		/////////////////////////////////////////////////////////////////////
		// FBO rendering: HeatHazeDistortion

		heatHazeFrameBuffer.bind();
		renderAll(heatHazeFrameBuffer.FrameBufferID);
		/////////////////////////////////////////////////////////////////////

		// FBO rendering: MainBuffer
		mainFrameBuffer.bind();

		// the actual background color is here!
		fullScreenRender(Shader.getShader("UI"), Texture.getTexture("bg"), 0);
		renderAll(mainFrameBuffer.FrameBufferID);

		// Blur horizontally
		blurProcessingBuffer.bind();
		fullScreenRender(Shader.getShader("HBlur"), bloomFrameBuffer.colorTextureID, 0);

		// Blur vertically
		glowFrameBuffer.bind();
		fullScreenRender(Shader.getShader("VBlur"), blurProcessingBuffer.colorTextureID, 0);

		// Render the stuff that does not need blur, hence, only glow
		renderAll(glowFrameBuffer.FrameBufferID);

		/// Bloom result render, added the mainFrame

		postBloomBuffer.bind();
		fullScreenRender(Shader.getShader("Bloom"), mainFrameBuffer.colorTextureID, glowFrameBuffer.colorTextureID);

		// Added ripple distortion into the postbloom buffer
		postRippleBuffer.bind();
		fullScreenRender(Shader.getShader("RippleDistortion"), postBloomBuffer.colorTextureID,
				rippleFrameBuffer.colorTextureID);

		//////////////////////////////////////////////////////////
		// Added swirl distortion into the postripple buffer
		swirlFrameBuffer.bind();

		// Swirl Shader require open GL 4.0 or above to run
		if (glCapabilities.OpenGL40) {
			SwirlRenderer.swirlList.clear();
			for (int i = 0; i < SpriteRenderer.allSpriteRendererComponents.size(); i++) {
				SpriteRenderer.allSpriteRendererComponents.get(i).render(swirlFrameBuffer.FrameBufferID);
			}

			Shader.getShader("SwirlDistortion").enable();

			ArrayList<Vector4f> swirlList = SwirlRenderer.returnALlSwirlinVector4f();
			Shader.getShader("SwirlDistortion").setUniform4fv("veclist", swirlList);
			Shader.getShader("SwirlDistortion").setUniform1f("veccount", swirlList.size());
			Shader.getShader("SwirlDistortion").setUniform1f("scroll", Camera.MAIN.scroll);
			Shader.getShader("SwirlDistortion").setUniform2f("resolution", Main.getWidth(), Main.getHeight());
			Shader.getShader("SwirlDistortion").disable();

			fullScreenRender(Shader.getShader("SwirlDistortion"), postRippleBuffer.colorTextureID, 0);
		}
		// if computer doesnt support open GL 4.0, do the normal fullscreen rendering
		// instead
		else {
			fullScreenRender(Shader.getShader("UI"), postRippleBuffer.colorTextureID, 0);
		}
		///////////////////////////////////////////////////////////

		postHeatHazeBuffer.bind();
		fullScreenRender(Shader.getShader("HeatHazeDistortion"), swirlFrameBuffer.colorTextureID,
				heatHazeFrameBuffer.colorTextureID);

		///////////////////////////////////////////////////////////

		glViewport(0, 0, Main.getWidth(), Main.getHeight());
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		// fullScreenRender(Shader.getShader("UI"), swirlFrameBuffer.colorTextureID, 0);
		fullScreenRender(Shader.getShader("UI"), postHeatHazeBuffer.colorTextureID, 0);

		renderAll(postProcessingBuffer.FrameBufferID);

		// Font rendering
		// new renderer
		TextRendererMaster.Render();
		// old renderer
		FontRenderer.render();

		// Main Render loop end
		//////

		glFlush();

		// check error

		int error = glGetError();
		if (error != GL_NO_ERROR) {
			System.out.println(error);
			GLUtil.setupDebugMessageCallback();
		}

		glfwSwapBuffers(window);
	}

	private static void renderAll(int FrameBufferID) {
		for (Iterator<SpriteRendererComponent> iterator = SpriteRenderer.allSpriteRendererComponents
				.iterator(); iterator.hasNext();) {
			SpriteRendererComponent spriteRenderer = iterator.next();
			spriteRenderer.render(FrameBufferID);
		}

		InstancedRenderer.Render(FrameBufferID);
	}

	private static void fullScreenRender(Shader shader, int textureID, int textureID2) {
		GL11.glDisable(GL11.GL_BLEND);

		glActiveTexture(GL_TEXTURE1);
		GL11.glBindTexture(GL_TEXTURE_2D, textureID);
		// TODO: move to swirl shader
		// used for swirl shader
		GL11.glTexParameterf(GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameterf(GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

		glActiveTexture(GL_TEXTURE2);
		GL11.glBindTexture(GL_TEXTURE_2D, textureID2);

		shader.enable();

		shader.setUniformMat4f("ml_matrix",
				Maths.createTransformationMatrix(new Vector3f(0, 0, -10), 0, new Vector2f(1f, 1f)));
		shader.setUniformMat4f("pr_matrix", new Matrix4f().ortho(0, 1, 1, 0, -10, 10));
		VertexArray.mesh_FULSCREEN.render();

		shader.disable();

		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public static void setWindowSize(Vector2i windowSize) {
		glfwSetWindowSize(window, windowSize.x, windowSize.y);
		Main.setWidth(windowSize.x);
		Main.setHeight(windowSize.y);
		System.out.println("Setting Window Size to x: " + windowSize.x + ",y :" + windowSize.y);
	}

	public static void setBackGroundColor(Vector4f backGroundColor) {
		BackGroundColor = backGroundColor;
		glClearColor(BackGroundColor.x, BackGroundColor.y, BackGroundColor.z, BackGroundColor.w);
	}

	/**
	 * get the screen position from 0 to 1 of X
	 */
	public static float getScreenPosition_X(float positionX) {
		return positionX / Main.getWidth();
	}

	/**
	 * get the screen position from 0 to 1 of Y
	 */
	public static float getScreenPosition_Y(float positionY) {
		return positionY / Main.getHeight();
	}
}

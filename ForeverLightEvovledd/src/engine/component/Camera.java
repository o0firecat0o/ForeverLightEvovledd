package engine.component;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import engine.component.graphic.Shader;
import engine.component.graphic.SpriteRenderer;
import engine.input.InputKey;
import engine.input.InputMousePos;
import engine.input.InputMouseScroll;
import engine.main.Main;
import engine.math.Mathf;
import engine.object.Component;

public class Camera extends Component {

	/**
	 * Mouse scroll that resize the screen
	 */
	public float scroll = 1;

	public static Camera MAIN;

	public boolean enable = true;

	@Override
	public void UpdateRender() {
		// Rendering
		for (int i = 0; i < Shader.returnAllShaders().length; i++) {
			Shader shader = (Shader) Shader.returnAllShaders()[i];
			if (Shader.rendered_shader.contains(shader)) {
				// if it is an UI shader, do not apply scale
				if (shader.isUIShader) {
					shader.setUniformMat4f("vw_matrix", new Matrix4f().translate(0, 0, 0));

					shader.setUniformMat4f("pr_matrix", SpriteRenderer.pr_matrix);
				}
				// else apply scale and translation
				else {
					shader.setUniformMat4f("vw_matrix",
							new Matrix4f()
									.translate(-gameObject.transform.position.x, -gameObject.transform.position.y, 0)
									.scale(scroll));

					shader.setUniformMat4f("pr_matrix", SpriteRenderer.pr_matrix);
				}
			}
		}
		// Clear the rendered shader list
		Shader.rendered_shader.clear();
	}

	@Override
	protected void Update() {
		if (!enable) {
			return;
		}

		// Moving
		if (InputKey.OnKeysHold(GLFW.GLFW_KEY_UP)) {
			gameObject.transform.position.y += 10f;
		}
		if (InputKey.OnKeysHold(GLFW.GLFW_KEY_DOWN)) {
			gameObject.transform.position.y -= 10f;
		}
		if (InputKey.OnKeysHold(GLFW.GLFW_KEY_RIGHT)) {
			gameObject.transform.position.x += 10f;
		}
		if (InputKey.OnKeysHold(GLFW.GLFW_KEY_LEFT)) {
			gameObject.transform.position.x -= 10f;
		}

		// Srolling
		if (InputMouseScroll.scrollUp) {
			scroll *= 1.1;
		} else if (InputMouseScroll.scrollDown) {
			scroll *= 0.9;
		}
		scroll = Mathf.clamp(scroll, 0.02f, 80f);
	}

	@Override
	protected void Start() {
		gameObject.transform.setPosition(0, 0, 0);
	}

	/*
	 * return Mouse position in World Position, considering camera scrolling and
	 * camera movement
	 */
	public Vector2f InputMousePositionV2f() {
		Vector2f screenVector = InputMousePos.ScreenPos;
		Vector2f worldVector = new Vector2f();
		worldVector.x += screenVector.x * 1280 / Main.getWidth();
		worldVector.y += screenVector.y * 720 / Main.getHeight();
		worldVector.x += gameObject.transform.position.x;
		worldVector.y += gameObject.transform.position.y;
		worldVector.x /= scroll;
		worldVector.y /= scroll;
		return worldVector;
	}

	public Vector3f InputMousePositionV3f() {
		return new Vector3f(InputMousePositionV2f().x, InputMousePositionV2f().y, 0);
	}

	// TODO: fix this shit, it is used in button
	public Vector2f ScreenToWorldPositionWITHOUTSCROLL() {
		Vector2f screenVector = InputMousePos.ScreenPos;
		Vector2f worldVector = new Vector2f();
		worldVector.x = screenVector.x * 1280 / Main.getWidth();
		worldVector.y = screenVector.y * 720 / Main.getHeight();
		worldVector.x += gameObject.transform.position.x;
		worldVector.y += gameObject.transform.position.y;
		return worldVector;
	}

	// TODO: fix this shit, it is also used in button
	public Vector2f ScreenToWorldPositionWITHOUTscrollWITHOUTcamera() {
		Vector2f screenVector = InputMousePos.ScreenPos;
		Vector2f worldVector = new Vector2f();
		worldVector.x = screenVector.x * 1280 / Main.getWidth();
		worldVector.y = screenVector.y * 720 / Main.getHeight();
		return worldVector;
	}

	public Vector2f WorldToOpenGLPosition(Vector2f original) {
		Vector2f returnVector = WorldToScreenPosition(original);
		returnVector.x /= Main.getWidth();
		returnVector.y /= Main.getHeight();
		return returnVector;
	}

	public Vector2f WorldToScreenPosition(Vector2f original) {
		Vector2f returnVector = new Vector2f();

		returnVector.x += original.x;
		returnVector.y += original.y;

		returnVector.x *= scroll;
		returnVector.y *= scroll;

		returnVector.x -= gameObject.transform.position.x;
		returnVector.y -= gameObject.transform.position.y;

		returnVector.x = returnVector.x / 1280 * Main.getWidth();
		returnVector.y = returnVector.y / 720 * Main.getHeight();

		return returnVector;
	}
}

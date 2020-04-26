package engine.component.graphic;

import java.util.ArrayList;

import org.joml.Matrix4f;

import engine.component.graphic.spriteRendererComponent.SpriteRendererComponent;
import engine.main.Main;
import engine.object.Component;

public class SpriteRenderer extends Component {

	public static Matrix4f pr_matrix = new Matrix4f().ortho(0, Main.getWidth(), 0, Main.getHeight(), -10, 10);

	public static ArrayList<SpriteRenderer> allSpriteRenderer = new ArrayList<>();

	private ArrayList<SpriteRendererComponent> spriterenderercomponents = new ArrayList<>();

	public int texture;

	// scaling by NOT following the gameobject scale
	public float graphicScaleOffset = 1f;

	public SpriteRenderer() {
		allSpriteRenderer.add(this);
	}

	@Override
	protected void Start() {

	}

	// This function will be called in the render loop
	public void render(int FrameBufferObjectID) {
		for (int i = 0; i < spriterenderercomponents.size(); i++) {
			SpriteRendererComponent component = spriterenderercomponents.get(i);
			if (component.FrameBufferIDs == null) {
				continue;
			}
			if (component.FrameBufferIDs.contains(FrameBufferObjectID)) {
				component.Render(FrameBufferObjectID);
			}
		}
	}

	@Override
	protected void Destroy() {
		// OLD
		/*
		 * clean up the glsl memory if (mesh != null) {
		 * GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); GL15.glDeleteBuffers(mesh.vbo);
		 *
		 * GL30.glBindVertexArray(0); GL30.glDeleteVertexArrays(mesh.vao); } mesh =
		 * null;
		 */
		allSpriteRenderer.remove(this);

		for (int i = 0; i < spriterenderercomponents.size(); i++) {
			spriterenderercomponents.get(i).Destroy();
		}

		super.Destroy();
	}

	public SpriteRenderer SetTexture(int texture) {
		this.texture = texture;
		return this;
	}

	public SpriteRenderer SetGraphicScaleOffset(float graphicScaleOffset) {
		this.graphicScaleOffset = graphicScaleOffset;
		return this;
	}

	@Override
	protected void Update() {
		for (int i = 0; i < spriterenderercomponents.size(); i++) {
			spriterenderercomponents.get(i).Update();
		}
	}

	public <T extends SpriteRendererComponent> T addSpriteRendererComponent(T spriterenderercomponent) {
		spriterenderercomponents.add(spriterenderercomponent);
		spriterenderercomponent.spriteRenderer = this;
		return spriterenderercomponent;
	}

	@SuppressWarnings("unchecked")
	public <T extends SpriteRendererComponent> T getSpriteRendererComponent(Class<T> ComponentClass) {
		for (int i = 0; i < spriterenderercomponents.size(); i++) {
			if (spriterenderercomponents.get(i).getClass().equals(ComponentClass)) {
				return (T) spriterenderercomponents.get(i);
			}
		}
		return null;
	}
}

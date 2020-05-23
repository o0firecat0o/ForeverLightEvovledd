package engine.component.graphic.spriteRendererComponent;

import java.util.ArrayList;

import engine.component.graphic.FrameBufferObject;
import engine.component.graphic.SpriteRenderer;
import engine.object.Component;

public abstract class SpriteRendererComponent extends Component {
	public abstract void Render(int FrameBufferObjectID);

	public int texture = 0;

	// which frame buffer to render to?
	public ArrayList<Integer> FrameBufferIDs = new ArrayList<>();

	protected float graphicScaleOffset = 1f;

	public SpriteRendererComponent() {
		SpriteRenderer.allSpriteRendererComponents.add(this);
	}

	// This function will be called in the render loop
	public void render(int FrameBufferObjectID) {
		if (FrameBufferIDs.contains(FrameBufferObjectID)) {
			Render(FrameBufferObjectID);
		}
	}

	@Override
	public void Destroy() {
		if (FrameBufferIDs != null) {
			FrameBufferIDs.clear();
			FrameBufferIDs = null;
		}
		SpriteRenderer.allSpriteRendererComponents.remove(this);
		super.Destroy();
	}

	public <T extends SpriteRendererComponent> T SetTexture(int texture) {
		this.texture = texture;
		return (T) this;
	}

	public <T extends SpriteRendererComponent> T setGraphicScaleOffset(float f) {
		graphicScaleOffset = f;
		return (T) this;
	}

	public <T extends SpriteRendererComponent> T AddFrameBuffer(FrameBufferObject fbo) {
		FrameBufferIDs.add(fbo.FrameBufferID);
		return (T) this;
	}

	public <T extends SpriteRendererComponent> T SetFrameBuffer(FrameBufferObject fbo) {
		FrameBufferIDs.clear();
		FrameBufferIDs.add(fbo.FrameBufferID);
		return (T) this;
	}

	public <T extends SpriteRendererComponent> T RemoveFrameBuffer(FrameBufferObject fbo) {
		if (FrameBufferIDs.contains(fbo.FrameBufferID)) {
			FrameBufferIDs.remove(fbo.FrameBufferID);
		} else {
			throw new RuntimeException("SpriteRenderer does not contain the frame buffer IDs:" + fbo.FrameBufferID
					+ "that to be removed!");
		}
		return (T) this;
	}
}

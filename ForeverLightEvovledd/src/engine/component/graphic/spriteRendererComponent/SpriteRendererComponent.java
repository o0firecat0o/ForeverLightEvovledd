package engine.component.graphic.spriteRendererComponent;

import java.io.Console;
import java.util.ArrayList;

import engine.component.graphic.*;
import engine.object.Component;

public abstract class SpriteRendererComponent {
	public abstract void Render(int FrameBufferObjectID);

	public abstract void Update();

	public abstract void Start();

	public int texture = 0;

	// which frame buffer to render to?
	public ArrayList<Integer> FrameBufferIDs = new ArrayList<>();

	protected float graphicScaleOffset = 1f;

	public SpriteRendererComponent() {
		Start();
		SpriteRenderer.allSpriteRendererComponents.add(this);
	}

	// This function will be called in the render loop
	public void render(int FrameBufferObjectID) {
		if (FrameBufferIDs.contains(FrameBufferObjectID)) {
			Render(FrameBufferObjectID);
		}
	}

	public SpriteRenderer spriteRenderer;

	public void Destroy() {
		spriteRenderer = null;
		if (FrameBufferIDs != null) {
			FrameBufferIDs.clear();
			FrameBufferIDs = null;
		}
		SpriteRenderer.allSpriteRendererComponents.remove(this);
	}

	public void setGraphicScaleOffset(float f) {
		graphicScaleOffset = f;
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

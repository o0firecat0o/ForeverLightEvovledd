package engine.component.graphic.spriteRendererComponent;

import engine.main.Render;

public class DissolveRenderer extends SpriteRendererComponent {

	@Override
	public void Render(int FrameBufferObjectID) {

	}

	@Override
	protected void Update() {

	}

	@Override
	protected void Start() {
		SetFrameBuffer(Render.mainFrameBuffer);
	}

}

package engine.gui;

import org.joml.Vector2f;

import engine.component.Button;
import engine.component.graphic.SpriteRenderer;
import engine.component.graphic.spriteRendererComponent.DefaultRender;
import engine.font.Font;
import engine.object.UIObject;
import engine.object.UIObject.UIPositions;

public class GUI {
	public static Font Font(String fontstring, int Text_Length, UIPositions uiPositions, Vector2f positionoffset,
			float z_position) {
		UIObject textObject = new UIObject(uiPositions, positionoffset, z_position);
		textObject.AddComponent(new Font(fontstring, Text_Length));

		return textObject.GetComponent(Font.class);
	}

	/**
	 * Fast method of creating a button
	 * 
	 * @param uiPositions     ENUM
	 * @param position_offset
	 * @param z_position
	 * @param texture
	 * @return
	 */
	public static Button Button(UIPositions uiPositions, Vector2f position_offset, float z_position, int texture) {
		UIObject buttonObject = new UIObject(uiPositions, position_offset, z_position);
		buttonObject.AddComponent(new Button(texture));
		buttonObject.AddComponent(new DefaultRender());

		return buttonObject.GetComponent(Button.class);
	}
}

package engine.font.newrenderer;

import org.joml.Vector2f;

import engine.component.graphic.Shader;
import engine.object.Component;
import engine.object.UIObject;

public class TextRenderer extends Component {

	StringObject stringObject;

	@Override
	protected void Update() {

	}

	@Override
	public void UpdateRender() {
		TextMaster.translate(stringObject, gameObject.transform.getPositionVector2f(),
				gameObject.transform.getRotation());
		super.UpdateRender();
	}

	@Override
	protected void Start() {
		// tell the TextRendererMaster class to use the correct shader
		if (gameObject instanceof UIObject) {
			stringObject.isUIObject = true;
		} else {
			stringObject.isUIObject = false;
		}
	}

	public TextRenderer(StringObject stringObject) {
		this.stringObject = stringObject;
	}

	public TextRenderer(String fontName, String text, int size, float lineLength, float linePadding) {
		stringObject = new StringObject();
		stringObject.fontName = fontName;
		stringObject.text = text;
		stringObject.size = size;
		stringObject.lineLength = lineLength;
		stringObject.linePadding = linePadding;
		stringObject.createProcessedString();

		TextMaster.stringOjbects.add(stringObject);
	}

	public TextRenderer(String text) {
		this("Utsaah", text, 1, 20, 100);
	}
}

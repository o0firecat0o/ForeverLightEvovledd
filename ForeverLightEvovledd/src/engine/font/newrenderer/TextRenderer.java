package engine.font.newrenderer;

import org.joml.Vector2f;

import engine.object.Component;

public class TextRenderer extends Component {

	StringObject stringObject;

	// TODO: addUI text

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
}

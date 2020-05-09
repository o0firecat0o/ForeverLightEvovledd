package engine.font.newrenderer;

import engine.object.Component;
import engine.object.UIObject;

public class TextRenderer extends Component {

	private final StringObject stringObject;

	@Override
	protected void Update() {
		TextMaster.translateANDresize(stringObject, gameObject.transform.getPositionVector2f(),
				gameObject.transform.getRotation(), gameObject.transform.getScale().x);
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

	public TextRenderer setText(String text) {
		TextMaster.setText(stringObject, text);
		return this;
	}

	public String getText() {
		return stringObject.text;
	}

	@Override
	public void Destroy() {
		TextMaster.stringOjbects.remove(stringObject);
		super.Destroy();
	}
}

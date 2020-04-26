package org.component;

import org.joml.Vector3f;

import engine.component.graphic.SpriteRenderer;
import engine.component.graphic.spriteRendererComponent.CircularMeterRenderer;
import engine.object.Component;

public class CircularHealthBar extends Component {

	@Override
	protected void Update() {
		circularMeterRenderer.setPrecentage(CurValue / MaxValue);
	}

	@Override
	protected void Start() {
		circularMeterRenderer = gameObject.AddComponent(new SpriteRenderer())
				.addSpriteRendererComponent(new CircularMeterRenderer());
	}

	CircularMeterRenderer circularMeterRenderer;

	public CircularHealthBar(float MaxValue, float CurValue) {
		super();
		this.MaxValue = MaxValue;
		this.CurValue = CurValue;
	}

	private float MaxValue;
	private float CurValue;

	public CircularHealthBar setMaxValue(float MaxValue) {
		this.MaxValue = MaxValue;
		return this;
	}

	public CircularHealthBar setCurValue(float CurValue) {
		this.CurValue = CurValue;
		return this;
	}

	public CircularHealthBar setColor(Vector3f Color) {
		circularMeterRenderer.setColor(Color);
		return this;
	}

	public CircularHealthBar setThickness(float Thickness) {
		circularMeterRenderer.setThickness(Thickness);
		return this;
	}

	public CircularHealthBar setInnerDiamater(float InnerDiamater) {
		circularMeterRenderer.setInnerDiameter(InnerDiamater);
		return this;
	}

}

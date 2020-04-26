package engine.component;

import java.util.ArrayList;

import org.joml.Vector2f;

import engine.font.Font;
import engine.gui.GUI;
import engine.main.*;
import engine.object.Component;
import engine.object.UIObject.UIPositions;

public class DebugScreen extends Component {

	@Override
	protected void Update() {

	}

	@Override
	protected void Start() {

	}

	private ArrayList<String> addedLog = new ArrayList<>();
	private Font debugFont;

	@Override
	public void StartRender() {
		debugFont = GUI.Font("DebugLog", 25, UIPositions.TopRight, new Vector2f(100, -50), -9);
		debugFont.setFontSize(0.1f);
		super.StartRender();
	}

	// TODO: move them to a stable thread
	@Override
	public void UpdateRender() {
		String font = new String();
		font = "fps: " + Render.fps + " /n " + "ups: " + Logic.ups + " /n " + "nps: " + Network.nps;
		font = font + " /n " + "TotalBodies: " + Logic.world.getBodyCount();
		font = font + " /n " + "TotalContacts: " + Logic.world.getContactCount();
		if (debugFont != null) {
			debugFont.setText(font);
		}
		super.UpdateRender();
	}

	public void add(Object object) {

	}

}

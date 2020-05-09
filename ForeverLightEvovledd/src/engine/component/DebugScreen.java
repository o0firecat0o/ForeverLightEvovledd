package engine.component;

import java.util.ArrayList;

import org.joml.Vector2f;

import engine.font.newrenderer.TextMaster;
import engine.font.oldrenderer.Font;
import engine.gui.GUI;
import engine.main.Logic;
import engine.main.Network;
import engine.main.Render;
import engine.object.Component;
import engine.object.UIObject.UIPositions;

public class DebugScreen extends Component {

	@Override
	protected void Update() {

	}

	@Override
	protected void Start() {

	}

	// TODO: finish added log
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
		font = font + " /n " + "TotalFont: " + TextMaster.getTotalFontCount();
		if (debugFont != null) {
			debugFont.setText(font);
		}
		super.UpdateRender();
	}

	public void add(Object object) {

	}

}

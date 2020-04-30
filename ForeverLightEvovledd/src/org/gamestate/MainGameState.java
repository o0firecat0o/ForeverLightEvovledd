package org.gamestate;

import java.awt.List;
import java.util.ArrayList;

import org.jbox2d.dynamics.joints.LimitState;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import engine.component.graphic.Texture;
import engine.component.graphic.instancedRendering.InstancedRenderObject;
import engine.component.graphic.instancedRendering.InstancedRenderer;
import engine.component.graphic.instancedRendering.particle.Particle;
import engine.component.graphic.instancedRendering.particle.ParticleRenderer;
import engine.component.graphic.spriteRendererComponent.DefaultRender;
import engine.component.graphic.spriteRendererComponent.LineRenderer;
import engine.font.TextObject;
import engine.font.TextRenderer;
import engine.gamestate.*;
import engine.main.Render;
import engine.math.Mathf;
import engine.math.Maths;
import engine.network.MPNetworkManager;
import engine.object.GameObject;

public class MainGameState implements IGameState {

	@Override
	public void Update() {

	}

	@Override
	public void Stop() {

	}

	@Override
	public void Init() {

	}
}

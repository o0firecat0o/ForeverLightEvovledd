package engine.component.graphic.instancedRendering.particle;

import java.util.*;

import org.joml.Matrix4f;

import engine.component.graphic.instancedRendering.*;
import engine.component.graphic.spriteRendererComponent.SpriteRendererComponent;
import engine.main.Render;
import engine.math.Maths;

public abstract class ParticleRenderer extends SpriteRendererComponent {

	public ArrayList<Particle> Particles;

	@Override
	public void Render(int FrameBufferObjectID) {
		List<InstancedRenderObject> instancedRenderObjects = new ArrayList<>();

		for (int i = 0; i < Particles.size(); i++) {
			Particle particle = Particles.get(i);
			// if the particle has already been destroyed;
			if (particle == null) {
				continue;
			}
			Matrix4f matrix4f = Maths.createTransformationMatrix(particle.transform.position,
					particle.transform.rotation, particle.transform.scale);
			instancedRenderObjects.add(new InstancedRenderObject(matrix4f, particle.color));

		}

		InstancedRenderer.Add(instancedRenderObjects, texture, FrameBufferObjectID);

	}

	@Override
	public void Update() {

		for (int i = 0; i < Particles.size(); i++) {
			Particles.get(i).Update();
		}
		CustomUpdate();
	}

	public abstract void CustomUpdate();

	@Override
	public void Start() {
		Particles = new ArrayList<>();
		SetFrameBuffer(Render.mainFrameBuffer);
	}

	@Override
	public void Destroy() {
		Particles.clear();
		super.Destroy();
	}
}

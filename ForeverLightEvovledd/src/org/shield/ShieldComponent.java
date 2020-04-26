package org.shield;

import org.bullet.IDamageable;
import org.component.CircularHealthBar;
import org.joml.*;

import engine.component.graphic.SpriteRenderer;
import engine.component.graphic.spriteRendererComponent.ShieldRenderer;
import engine.component.physic.*;
import engine.object.*;

public class ShieldComponent extends Component implements IDamageable {

	public int Team;
	private GameObject CircularHealthBarObject;
	private GameObject RechargeBar;
	private float CurHealth = 250;
	private float MaxHealth = 250;
	private float CurCharge = 0;
	private float MaxCharge = 1000;

	@Override
	protected void Update() {
		CircularHealthBarObject.transform.setPosition(gameObject.transform.position);
		RechargeBar.transform.setPosition(gameObject.transform.position);

		// recharge if shield depleted
		if (CurHealth <= 0) {
			CurCharge++;

			if (CurCharge > MaxCharge) {
				CurHealth = MaxHealth;
				MakeShield();
				CurCharge = 0;
			}
			RechargeBar.GetComponent(CircularHealthBar.class).setCurValue(CurCharge);
		}
	}

	public ShieldComponent(int Team) {
		super();
		this.Team = Team;
	}

	@Override
	protected void Start() {
		MakeShield();

		// add the circular health bar object
		CircularHealthBarObject = new GameObject();
		CircularHealthBarObject.AddComponent(new CircularHealthBar(MaxHealth, CurHealth));
		if (Team == 1) {
			CircularHealthBarObject.GetComponent(CircularHealthBar.class).setColor(new Vector3f(0.2f, 0.3f, 1f));
		} else if (Team == 2) {
			CircularHealthBarObject.GetComponent(CircularHealthBar.class).setColor(new Vector3f(1f, 0.3f, 0.2f));
		}
		CircularHealthBarObject.GetComponent(CircularHealthBar.class).setInnerDiamater(0.6f).setThickness(0.05f);
		CircularHealthBarObject.transform.setScale(gameObject.transform.getScale());

		// add the energy recharge bar
		RechargeBar = new GameObject();
		RechargeBar.AddComponent(new CircularHealthBar(MaxCharge, CurCharge));

		RechargeBar.GetComponent(CircularHealthBar.class).setColor(new Vector3f(0.9f, 0.95f, 1f));

		RechargeBar.GetComponent(CircularHealthBar.class).setInnerDiamater(0.5f).setThickness(0.05f);
		RechargeBar.transform.setScale(gameObject.transform.getScale());

	}

	public void MakeShield() {
		ShieldRenderer shieldRenderer = new ShieldRenderer();
		shieldRenderer.setDistortionValue(0.002f);
		shieldRenderer.setEdgeThickness(2f);
		// set the shield color according to the team
		shieldRenderer.setShieldColor(new Vector3f(1, 1, 1));

		gameObject.AddComponent(new SpriteRenderer()).addSpriteRendererComponent(shieldRenderer);

		gameObject.AddComponent(new RigidBody());

		RigidFixutre rFixutre = gameObject
				.AddComponent(new RigidFixutre(ShapeType.circle, gameObject.transform.getScale().mul(0.5f),
						gameObject.GetComponent(RigidBody.class), new Vector2f(), 0));

		// set the colliding category
		if (Team == 1) {
			gameObject.GetComponent(RigidBody.class).setCategoryBits(EntityCategory.TEAM1SHIELD);
			gameObject.GetComponent(RigidBody.class).setMaskBits(EntityCategory.TEAM2BULLET);
		} else {
			gameObject.GetComponent(RigidBody.class).setCategoryBits(EntityCategory.TEAM2SHIELD);
			gameObject.GetComponent(RigidBody.class).setMaskBits(EntityCategory.TEAM1BULLET);
		}

		gameObject.GetComponent(RigidBody.class).setTrigger(true);
		gameObject.GetComponent(RigidBody.class).setPhysicsDetermineTransform(false);
	}

	/**
	 * Taking damage from any sources
	 */
	@Override
	public void applyDamage(float damageSize) {
		CurHealth -= damageSize;
		if (CurHealth <= 0) {
			gameObject.RemoveComponent(RigidBody.class);
			gameObject.RemoveComponent(RigidFixutre.class);
			gameObject.RemoveComponent(SpriteRenderer.class);
		}
		CircularHealthBarObject.GetComponent(CircularHealthBar.class).setCurValue(CurHealth);
	}

	@Override
	public void Destroy() {
		CircularHealthBarObject.InitDestroy();
		RechargeBar.InitDestroy();
		super.Destroy();
	}
}

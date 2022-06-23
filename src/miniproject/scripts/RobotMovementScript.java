package miniproject.scripts;

import com.mizosoft.Mizo3D.MizoComponent;
import com.mizosoft.Mizo3D.MizoKeys;
import com.mizosoft.Mizo3D.MizoTerrain;
import com.mizosoft.Mizo3D.MizoVector3f;
import com.mizosoft.Mizo3D.components.MizoAnimatedMeshRenderer;
import com.mizosoft.Mizo3D.components.MizoRigidBody;
import com.mizosoft.Mizo3D.components.MizoTerrainRenderer;

import miniproject.MiniProject;

public class RobotMovementScript extends MizoComponent
{
	private MizoRigidBody rb;
	private int jumpCount = 0;
	private float jumpTimer = 0f;
	private static final float TIME_BETWEEN_JUMPS = 0.4f;
	private static final int MAX_JUMPS = 2;
	private boolean moving = false;
	private boolean falling = false;
	private float timeSinceLastJump = 0f;
	public static int animation = 5;
	
	@Override
	public void start()
	{
		rb = owner.getComponent(MizoRigidBody.class);
	}
	
	@Override
	public void update(float deltaTime)
	{
		moveForward(0f);
		boolean move = false;
		if (owner.scene.window.isKeyDown(MizoKeys.MIZOKEY_W) || owner.scene.window.isKeyDown(MizoKeys.MIZOKEY_UP))
		{
			moveForward(50f);
			move = true;
		}
		if (owner.scene.window.isKeyDown(MizoKeys.MIZOKEY_S) || owner.scene.window.isKeyDown(MizoKeys.MIZOKEY_DOWN))
		{
			moveForward(-50f);
			move = true;
		}
		if (owner.scene.window.isKeyDown(MizoKeys.MIZOKEY_A) || owner.scene.window.isKeyDown(MizoKeys.MIZOKEY_LEFT))
		{
			turnRight(deltaTime);
		}
		if (owner.scene.window.isKeyDown(MizoKeys.MIZOKEY_D) || owner.scene.window.isKeyDown(MizoKeys.MIZOKEY_RIGHT))
		{
			turnRight(-deltaTime);
		}
		
		boolean onGround = rb.isOnGround();
		
		int ix = (int) Math.floor(owner.position.x / 800f);
		int iz = (int) Math.floor(owner.position.z / 800f);
		MizoTerrain terrain = MiniProject.terrains[0][0];
		if (ix >= 0 && ix < MiniProject.size && iz >= 0 && iz < MiniProject.size)
			terrain = MiniProject.terrains[iz][ix];
		float height = terrain.GetHeightAtPoint(owner.position.x, owner.position.z);
		if (owner.position.y < height) {
			rb.velocity.y = 0f;
			owner.position.y = height;
			onGround = true;
		}
		
		if (onGround) {
			if (falling) {
				owner.getComponent(MizoAnimatedMeshRenderer.class).animator.PlayAnimation(MiniProject.animations.get("idle"));
				animation = 0;
				falling = false;
				timeSinceLastJump = 0f;
			}
			if (!move && moving) {
				owner.getComponent(MizoAnimatedMeshRenderer.class).animator.PlayAnimation(MiniProject.animations.get("idle"));
				animation = 0;
				moving = false;
			} else if (!moving && move) {
				owner.getComponent(MizoAnimatedMeshRenderer.class).animator.PlayAnimation(MiniProject.animations.get("run"));
				animation = 1;
				moving = true;
			}
		}

		if (onGround) {
			jumpCount = 0;
		} else if (jumpCount == 0) {
			jumpCount = MAX_JUMPS;
		}
		jumpTimer += deltaTime;
		if (jumpTimer >= TIME_BETWEEN_JUMPS && jumpCount < MAX_JUMPS && owner.scene.window.isKeyDown(MizoKeys.MIZOKEY_SPACE)) {
			rb.velocity.y = 175f;
			jumpCount++;
			jumpTimer = 0f;
			owner.getComponent(MizoAnimatedMeshRenderer.class).animator.PlayAnimation(MiniProject.animations.get("jump"));
			animation = 2;
			falling = true;
			moving = false;
			timeSinceLastJump = 0f;
			rb.SetLastOnGround(false);
		}
		
		if (owner.position.y < -1000f) {
			owner.position = new MizoVector3f(400f, 50f, 400f);
			owner.rotation = new MizoVector3f(0f, 0f, 0f);
			rb.velocity = new MizoVector3f();
		}
	}
	
	private void moveForward(float distance)
	{
		
		rb.velocity.x = (float)Math.sin(Math.toRadians(owner.rotation.y)) * distance;
		rb.velocity.z = (float)Math.cos(Math.toRadians(owner.rotation.y)) * distance;
	}
	
	private void turnRight(float deltaTime)
	{
		owner.rotation.y += deltaTime * 150f;
	}
}

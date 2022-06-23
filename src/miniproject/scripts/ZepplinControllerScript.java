package miniproject.scripts;

import com.mizosoft.Mizo3D.MizoComponent;

public class ZepplinControllerScript extends MizoComponent
{
	private float distance;
	private float angle;
	
	@Override
	public void start()
	{
		distance = 900f;
		angle = -110f;
	}
	
	@Override
	public void update(float deltaTime)
	{
		angle += 2f * deltaTime;
		owner.position.x = 800f + (float)Math.cos(Math.toRadians(angle)) * distance;
		owner.position.z = 800f + (float)Math.sin(Math.toRadians(angle)) * distance;
		owner.position.y = 250f;
		owner.rotation.y = -angle - 90f;
	}
}
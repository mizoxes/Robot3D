package miniproject;

import java.util.HashMap;
import java.util.Map;

import com.mizosoft.Mizo3D.MizoAnimatedModel;
import com.mizosoft.Mizo3D.MizoAnimation;
import com.mizosoft.Mizo3D.MizoEntity;
import com.mizosoft.Mizo3D.MizoMeshLoader;
import com.mizosoft.Mizo3D.MizoQuaternion;
import com.mizosoft.Mizo3D.MizoScene;
import com.mizosoft.Mizo3D.MizoTerrain;
import com.mizosoft.Mizo3D.MizoTexture;
import com.mizosoft.Mizo3D.MizoVector3f;
import com.mizosoft.Mizo3D.MizoWindow;
import com.mizosoft.Mizo3D.components.MizoAnimatedMeshRenderer;
import com.mizosoft.Mizo3D.components.MizoMeshCollider;
import com.mizosoft.Mizo3D.components.MizoMeshRenderer;
import com.mizosoft.Mizo3D.components.MizoRigidBody;
import com.mizosoft.Mizo3D.components.MizoSkyRenderer;
import com.mizosoft.Mizo3D.components.MizoTerrainRenderer;

import miniproject.scripts.NetScript;
import miniproject.scripts.RobotMovementScript;
import miniproject.scripts.ZepplinControllerScript;
import com.mizosoft.Mizo3D.MizoMatrix4f;
public class MiniProject
{
	public static Map<String, MizoAnimation> animations = new HashMap<String, MizoAnimation>();
	public static final int size = 2;
	public static MizoTerrain[][] terrains = new MizoTerrain[size][size];
	
	public static void main(String args[]) {
		MizoWindow window = new MizoWindow("mini-project", 1920, 1080);
		
		MizoScene scene = new MizoScene();
		
		MizoEntity robot = new MizoEntity(
				new MizoVector3f(800f, 50f, 800f),  // position
				new MizoVector3f(0f, 0f, 0f), // rotation
				new MizoVector3f(1f, 1f, 1f)      // scale
				);
		MizoAnimatedModel model_robot = new MizoAnimatedModel("assets/robot.dae");
		MizoAnimation animation_robot_idle = new MizoAnimation("assets/robot.dae", model_robot);
		MizoAnimation animation_robot_run = new MizoAnimation("assets/robot_run.dae", model_robot);
		MizoAnimation animation_robot_jump = new MizoAnimation("assets/robot_jump.dae", model_robot);
		animations.put("idle", animation_robot_idle);
		animations.put("run", animation_robot_run);
		animations.put("jump", animation_robot_jump);
		robot.addComponent(new MizoAnimatedMeshRenderer(model_robot, animation_robot_idle));
		
		robot.addComponent(new MizoRigidBody(new MizoVector3f(3.5f, 13f, 3.5f)));
		robot.addComponent(new RobotMovementScript());
		scene.addEntity(robot);
		scene.getMainCamera().attachToEntity(robot);
		
		/*MizoEntity test = new MizoEntity(
				new MizoVector3f(300f, 150f, 200f),   // position
				new MizoVector3f(0f, 0f, 0f),        // rotation
				new MizoVector3f(10f, 10f, 10f)      // scale
				);
		MizoAnimatedModel model = new MizoAnimatedModel("C:/Users/Hamza/eclipse-workspace/mini-project/assets/micro_bat.dae");
		MizoAnimation animation = new MizoAnimation("C:/Users/Hamza/eclipse-workspace/mini-project/assets/micro_bat.dae", model);
		test.addComponent(new MizoAnimatedMeshRenderer(model, animation));
		scene.addEntity(test);*/
		
		MizoEntity airship = new MizoEntity(
				new MizoVector3f(),
				new MizoVector3f(-90f, 0f, 0f),
				new MizoVector3f(0.5f, 0.5f, 0.5f)
				);
		airship.addComponent(new MizoMeshRenderer(MizoMeshLoader.LoadMeshFromOBJ("airship", MizoTexture.loadTexture("airship"))));
		airship.addComponent(new ZepplinControllerScript());
		scene.addEntity(airship);
		
		/*MizoEntity wall0 = new MizoEntity(
				new MizoVector3f(800f, 100f, 0f),
				new MizoVector3f(0f, 0f, 0f),
				new MizoVector3f(1600f, 200f, 30f)
				);
		wall0.addComponent(new MizoMeshRenderer(MizoMeshLoader.LoadMeshFromOBJ("cube", MizoTexture.loadTextureRepeat("floor"))));
		wall0.addComponent(new MizoMeshCollider());
		scene.addEntity(wall0);*/
		
		MizoEntity house = new MizoEntity(
				new MizoVector3f(800f, 40f, 800f),
				new MizoVector3f(0f, 0f, 0f),
				new MizoVector3f(30f, 25f, 30f)
				);
		house.addComponent(new MizoMeshRenderer(MizoMeshLoader.LoadMeshFromOBJ("house", MizoTexture.loadTexture("house-texture"))));
		house.addComponent(new MizoMeshCollider());
		scene.addEntity(house);
		
		/*MizoEntity wall1 = new MizoEntity(
				new MizoVector3f(800f, 100f, 1600f),
				new MizoVector3f(0f, 0f, 0f),
				new MizoVector3f(1600f, 200f, 30f)
				);
		wall1.addComponent(new MizoMeshRenderer(MizoMeshLoader.LoadMeshFromOBJ("cube", MizoTexture.loadTextureRepeat("floor"))));
		wall1.addComponent(new MizoMeshCollider());
		scene.addEntity(wall1);
		
		MizoEntity wall2 = new MizoEntity(
				new MizoVector3f(0f, 100f, 800f),
				new MizoVector3f(0f, 0f, 0f),
				new MizoVector3f(30f, 200f, 1600f)
				);
		wall2.addComponent(new MizoMeshRenderer(MizoMeshLoader.LoadMeshFromOBJ("cube", MizoTexture.loadTextureRepeat("floor"))));
		wall2.addComponent(new MizoMeshCollider());
		scene.addEntity(wall2);
		
		MizoEntity wall4 = new MizoEntity(
				new MizoVector3f(1600f, 100f, 800f),
				new MizoVector3f(0f, 0f, 0f),
				new MizoVector3f(30f, 200f, 1600f)
				);
		wall4.addComponent(new MizoMeshRenderer(MizoMeshLoader.LoadMeshFromOBJ("cube", MizoTexture.loadTextureRepeat("floor"))));
		wall4.addComponent(new MizoMeshCollider());
		scene.addEntity(wall4);*/
		
		/*MizoEntity p0 = new MizoEntity(
				new MizoVector3f(200f, 145f, 300f),
				new MizoVector3f(0f, 0f, 0f),
				new MizoVector3f(60f, 10f, 60f)
				);
		p0.addComponent(new MizoMeshRenderer(MizoMeshLoader.LoadMeshFromOBJ("cube", MizoTexture.loadTexture("floor"))));
		p0.addComponent(new MizoMeshCollider());
		scene.addEntity(p0);
		
		MizoEntity p1 = new MizoEntity(
				new MizoVector3f(300f, 145f, 200f),
				new MizoVector3f(0f, 0f, 0f),
				new MizoVector3f(100f, 10f, 100f)
				);
		p1.addComponent(new MizoMeshRenderer(p0.getComponent(MizoMeshRenderer.class).mesh));
		p1.addComponent(new MizoMeshCollider());
		scene.addEntity(p1);
		
		MizoEntity p2 = new MizoEntity(
				new MizoVector3f(200f, 90f, 430f),
				new MizoVector3f(0f, 0f, 0f),
				new MizoVector3f(100f, 10f, 100f)
				);
		p2.addComponent(new MizoMeshRenderer(p0.getComponent(MizoMeshRenderer.class).mesh));
		p2.addComponent(new MizoMeshCollider());
		scene.addEntity(p2);*/
		
		MizoTexture terrtex1 = MizoTexture.loadTextureRepeat("ground");
		MizoTexture terrtex2 = MizoTexture.loadTextureRepeat("path");
		MizoTexture terrblend = MizoTexture.loadTextureRepeat("blendMap");
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				MizoEntity terrain = new MizoEntity(
						new MizoVector3f(0f, 0f, 0f),
						new MizoVector3f(0f, 0f, 0f),
						new MizoVector3f(1f, 1f, 1f)
						);
				terrains[i][j] = new MizoTerrain(j, i, terrtex1, terrtex2, terrblend, "heightMap");
				terrain.addComponent(new MizoTerrainRenderer(terrains[i][j]));
				scene.addEntity(terrain);
			}
		}
		
		MizoEntity sky = new MizoEntity();
		sky.addComponent(new MizoSkyRenderer("sky"));
		scene.addEntity(sky);
		
		MizoEntity[] players = new MizoEntity[NetScript.MAX_PLAYERS];
		for (int i = 0; i < NetScript.MAX_PLAYERS; i++) {
			players[i] = new MizoEntity();
			players[i].addComponent(new MizoAnimatedMeshRenderer(model_robot, animation_robot_idle));
			players[i].active = false;
		}
		
		MizoEntity net = new MizoEntity();
		String ip = "127.0.0.1";
		if (args.length > 0) ip = args[0];
		net.addComponent(new NetScript(robot, ip, 6666, players));
		scene.addEntity(net);
		
		for (int i = 0; i < 4; i++) {
			scene.addEntity(players[i]);
		}
		
		window.setScene(scene);
		window.Go();
	}
}
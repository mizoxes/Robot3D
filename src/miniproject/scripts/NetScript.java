package miniproject.scripts;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import com.mizosoft.Mizo3D.MizoComponent;
import com.mizosoft.Mizo3D.MizoEntity;
import com.mizosoft.Mizo3D.components.MizoAnimatedMeshRenderer;

import miniproject.MiniProject;

public class NetScript extends MizoComponent
{
	public static final int MAX_PLAYERS = 10;
	
	private int netID;
	private boolean isConnected = false;
	private int requestCount;
	private int numRobots = 0;
	private int ids[] = new int[MAX_PLAYERS];
	private int[] currentAnim = new int[MAX_PLAYERS];
	private DatagramChannel channel;
	private InetSocketAddress svaddr;
	ByteBuffer inbb, oubb;
	private static String[] animations = {"idle", "run", "jump"};
	
	private MizoEntity robot;
	private MizoEntity[] players;
	
	public NetScript(MizoEntity robot, String serverIP, int port, MizoEntity[] players)
	{
		this.robot = robot;
		this.players = players;
		
		inbb = ByteBuffer.allocate(512);
		oubb = ByteBuffer.allocate(512);
		
		svaddr = new InetSocketAddress(serverIP, port);
		
		requestCount = 0;
		
		try {
			channel = DatagramChannel.open();
			channel.configureBlocking(false);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void start()
	{
	}
	
	@Override
	public void update(float deltaTime)
	{
		if (requestCount == 10) {
			return;
		}
		
		try {
			oubb.clear();
			if (!isConnected) {
				oubb.put(5);
				requestCount++;
				if (requestCount == 10) {
					System.out.println("Server not responding");
				}
			} else {
				oubb.put(10);
				oubb.put((byte)netID);
				oubb.putFloat(robot.position.x);
				oubb.putFloat(robot.position.y);
				oubb.putFloat(robot.position.z);
				oubb.putFloat(robot.rotation.x);
				oubb.putFloat(robot.rotation.y);
				oubb.putFloat(robot.rotation.z);
				oubb.putInt(RobotMovementScript.animation);
			}
			oubb.flip();
			channel.send(oubb, svaddr);
			
			while (true) {
				inbb.clear();
				InetSocketAddress from = (InetSocketAddress)channel.receive(inbb);
				if (from == null) {
					break;
				}
				inbb.flip();
				switch ((int)inbb.get())
				{
				case 5:
					if ((int)inbb.get() == 1) {
						System.out.println("connected to server");
						netID = (int)inbb.get();
						isConnected = true;
					} else {
						System.out.println("Server is full");
					}
					break;
				
				case 10:
					numRobots = (int)inbb.get();
					for (int i = 0; i < 4; i++) {
						players[i].active = false;
					}
					for (int i = 0; i < numRobots; i++) {
						ids[i] = inbb.getInt();
						players[ids[i]].position.x = inbb.getFloat();
						players[ids[i]].position.y = inbb.getFloat();
						players[ids[i]].position.z = inbb.getFloat();
						players[ids[i]].rotation.x = inbb.getFloat();
						players[ids[i]].rotation.y = inbb.getFloat();
						players[ids[i]].rotation.z = inbb.getFloat();
						if (ids[i] != netID)
							players[ids[i]].active = true;
						int anim = inbb.getInt();
						if (ids[i] != netID && anim != 5 && currentAnim[ids[i]] != anim) {
							currentAnim[ids[i]] = anim;
							players[ids[i]].getComponent(MizoAnimatedMeshRenderer.class).animator.PlayAnimation(MiniProject.animations.get(animations[anim]));
						}
					}
					break;
					
				default:
					break;
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

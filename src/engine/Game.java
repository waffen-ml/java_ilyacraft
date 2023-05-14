package engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.glfw.GLFW;

public class Game extends Scene {
	
	private static float CAMERA_SPEED = 15f;
	private static int WORLD_SIDE = 50;
	private static int STONE_HEIGHT = 10;
	private static int DIRT_HEIGHT = 5;
	private static int NOISE_HEIGHT = 10;
	private static float TREE_PROB = 0.008f;
	
	public WindowManager window;
	public RenderManager renderer;
	public ObjectLoader loader;
	public Physics physics;
	public World world;
	public Player player;
	private Random rand;
	
	
	public void start() {
		window = engine.getWindowManager();	
		renderer = engine.getRenderManager();
		loader = new ObjectLoader();
		world = new World();
		physics = new Physics(world);
		player = new Player(new Vector3f(10f, 50f, 10f), this);
		rand = new Random(System.currentTimeMillis());
		window.setClearColor(135f / 255, 206f / 255, 235f / 255, 0);
		createTerrain();
	}
	
	private Vector3i endPos(int y) {
		return world.tile(Game.WORLD_SIDE - 1, 
				y, Game.WORLD_SIDE - 1);
	}
	
	private Vector3i startPos(int y) {
		return world.tile(0, y, 0);
	}
	
	private void buildTree(Vector3i tile) {
		int logHeight = rand.nextInt(4, 8);
		Vector3i center = new Vector3i(tile.x, tile.y + logHeight - 1, tile.z);
		world.fill(tile, center, "log");
		
		int w = rand.nextInt(1, 3);
		int hLow = rand.nextInt(1, 3);
		int hHigh = rand.nextInt(2, 4);
		
		world.fill(new Vector3i(center.x - w, center.y - hLow, center.z - w),
				new Vector3i(center.x + w, center.y + hHigh, center.z + w), "leaves");
		
	}
	
	private void createTerrain() {
		world.fill(startPos(0), endPos(STONE_HEIGHT - 1), "stone");
		
		float[][] noiseMatrix = NoiseGenerator.generatePerlinNoise(WORLD_SIDE, WORLD_SIDE, 5, 0.3f, System.currentTimeMillis());

		for (int x = 0; x < WORLD_SIDE; x++)
			for(int z = 0; z < WORLD_SIDE; z++) {
				float noise = noiseMatrix[x][z];
				int height = (int)(noise * NOISE_HEIGHT);
				
				world.fill(new Vector3i(x, STONE_HEIGHT, z), new Vector3i(x, STONE_HEIGHT + height - 1, z), "stone");
				world.fill(new Vector3i(x, STONE_HEIGHT + height, z),
						new Vector3i(x, STONE_HEIGHT + height + DIRT_HEIGHT - 1, z), "dirt");
				world.placeBlock(new Vector3i(x, STONE_HEIGHT + height + DIRT_HEIGHT, z), "grass");
				
				if (rand.nextFloat() < TREE_PROB)
					buildTree(new Vector3i(x, STONE_HEIGHT + height + DIRT_HEIGHT + 1, z));
			}
		
		
		
	}
	
	
	public void update(float dt) {
		player.update(dt);
	}

	public void render() {
		Transform camera = player.getCameraTransform();
		for (Block block : world.getBlocks()) {
			for (Entity entity : block.getActiveEntities())
				renderer.processEntity(entity);
		}
		renderer.render(camera);
	}
	
	public void updateBlocks() {
		for (Block block : world.getBlocks())
			block.update();
	}
	
	public void terminate() {
		System.out.println("Fuck!");
		loader.terminate();
	}

}

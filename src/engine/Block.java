package engine;

import org.joml.Vector3f;
import org.joml.Vector3i;
import java.util.ArrayList;


public class Block {
	private BlockInfo info;
	private Entity[] allEntities;
	private Entity[] activeEntities;
	private World world;
	private Vector3i tile;
	
	public Block(World world, Vector3i tile, BlockInfo info) {
		this.info = info;
		this.world = world;
		this.tile = tile;
		constructSides();
	}
	
	private void constructSides() {
		float offset = World.BLOCK_SIDE / 2;
		Vector3f origin = world.tileToPosition(tile);
		
		allEntities = new Entity[] {
			new Entity(info.getSide(0), new Vector3f(origin.x + offset, origin.y, origin.z),
					new Vector3f(0, Utils.HALF_PI, 0), 1),
			new Entity(info.getSide(1), new Vector3f(origin.x - offset, origin.y, origin.z),
					new Vector3f(0, -Utils.HALF_PI, 0), 1),
			new Entity(info.getSide(2), new Vector3f(origin.x, origin.y + offset, origin.z),
					new Vector3f(-Utils.HALF_PI, 0, 0), 1),
			new Entity(info.getSide(3), new Vector3f(origin.x, origin.y - offset, origin.z),
					new Vector3f(Utils.HALF_PI, 0, 0), 1),
			new Entity(info.getSide(4), new Vector3f(origin.x, origin.y, origin.z + offset),
					new Vector3f(0, 0, 0), 1),
			new Entity(info.getSide(5), new Vector3f(origin.x, origin.y, origin.z - offset),
					new Vector3f(0, Utils.PI, 0), 1),
		};
	}
	
	public Entity[] getActiveEntities() {
		return activeEntities;
	}
	
	public void update() {
		ArrayList<Entity> arr = new ArrayList<>();
		
		for (int i = 0; i < 3; i++) {
			Vector3i near = new Vector3i(tile);
			int b = near.get(i);
			
			if (world.isFree(near.setComponent(i, b + 1)))
				arr.add(allEntities[2 * i]);
			
			if (world.isFree(near.setComponent(i, b - 1)))
				arr.add(allEntities[2 * i + 1]);
		}
		
		activeEntities = arr.toArray(new Entity[arr.size()]);
	}
}

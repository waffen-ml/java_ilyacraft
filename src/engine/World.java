package engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collection;

import org.joml.Vector3f;
import org.joml.Vector3i;

public class World {
	public static float BLOCK_SIDE = 1.5f;
	
	private Map<Vector3i, Block> blockGrid;
	private BlockLoader blockLoader;
	
	public World() {
		blockGrid = new HashMap<>();
		blockLoader = new BlockLoader(this);
	}
	
	public Block[] getBlocks() {
	    Collection<Block> values = blockGrid.values();
		return values.toArray(new Block[values.size()]);
	}
	
	public Block getBlock(Vector3i tile) {
		return blockGrid.get(tile);
	}
	
	public boolean isFree(Vector3i tile) {
		return !blockGrid.containsKey(tile);
	}
	
	public void placeBlock(Vector3i tile, Block block) {
		blockGrid.put(tile, block);
		updateZone(tile);
	}
	
	public void placeBlock(Vector3i tile, String name) {
		placeBlock(tile, blockLoader.createBlock(name, tile));
	}
	
	public void clear() {
		blockGrid.clear();
	}
	
	private void updateZone(Vector3i center) {
		for (int dx = -1; dx <= 1; dx++)
			for (int dy = -1; dy <= 1; dy++)
				for (int dz = -1; dz <= 1; dz++)
					updateIfExists(new Vector3i(
							center.x + dx,
							center.y + dy,
							center.z + dz));
	}
	
	private void updateIfExists(Vector3i tile) {
		Block block = blockGrid.get(tile);
		if (block == null) return;
		block.update();
	}
	
	public void destroyBlock(Vector3i tile) {
		blockGrid.remove(tile);
		updateZone(tile);
	}
	
	public Vector3i tile(int x, int y, int z) {
		return new Vector3i(x, y, z);
	}
	
	public void fill(Vector3i from, Vector3i to, String name) {
		int xMin = Math.min(from.x, to.x);
		int xMax = Math.max(from.x, to.x);
		int yMin = Math.min(from.y, to.y);
		int yMax = Math.max(from.y, to.y);
		int zMin = Math.min(from.z, to.z);
		int zMax = Math.max(from.z, to.z);
		
		for (int x = xMin; x <= xMax; x++)
			for (int y = yMin; y <= yMax; y++)
				for (int z = zMin; z <= zMax; z++)
					placeBlock(tile(x, y, z), name);
	}
	
	public Vector3f tileToPosition(Vector3i v) {
		return new Vector3f(
				v.x * World.BLOCK_SIDE,
				v.y * World.BLOCK_SIDE,
				v.z * World.BLOCK_SIDE);
	}
	
	public Vector3i positionToTile(Vector3f pos) {
		int x = (int)Math.round(pos.x / World.BLOCK_SIDE);
		int y = (int)Math.round(pos.y / World.BLOCK_SIDE);
		int z = (int)Math.round(pos.z / World.BLOCK_SIDE);
		return new Vector3i(x, y, z);
	}

}

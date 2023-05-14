package engine;

import java.util.Map;
import java.util.ArrayList;
import org.joml.Vector3i;
import java.util.HashMap;


class BlockInfo {
	private String name;
	private Model[] models;
	private Map<Integer, Integer> sides;
	
	public BlockInfo(String name) {
		this.name = name;
		sides = new HashMap<>();
	}
	
	public void load(ObjectLoader loader) {
		String folder = BlockLoader.ROOT + name + "\\";
		ArrayList<Model> modelList = new ArrayList<>();
		String text;
		try {
			text = Utils.loadTextResource(folder + "sides.txt");
		} catch(Exception ex) {
			System.out.println("There is an error with loading config: " + ex);
			return;
		}
		for(String line : text.split(System.lineSeparator())) {
			if (line == "") continue;
			String[] p = line.split(" ");
			String sideName = p[0];
			String fileName = p[1];
			
			int sideId = Utils.getSideId(sideName);
			sides.put(sideId, modelList.size());
			modelList.add(ModelBuilder.buildSide(World.BLOCK_SIDE, 
					World.BLOCK_SIDE, loader, folder + fileName));
		}
		this.models = modelList.toArray(new Model[modelList.size()]);
	}
	
	public Model getSide(int sideId) {
		int id = sides.get(sideId);
		return models[id];
	}
}


public class BlockLoader {
	public static String ROOT = "blocks\\";
	
	private World world;
	private ObjectLoader objectLoader = new ObjectLoader();
	private Map<String, BlockInfo> blocks = new HashMap<>();
	
	public BlockLoader(World world) {
		this.world = world;
	}
	
	public Block createBlock(String name, Vector3i tile) {
		BlockInfo info = blocks.get(name);
		if (info == null) {
			info = new BlockInfo(name);
			info.load(objectLoader);
			blocks.put(name, info);
		}
		return new Block(world, tile, info);
	}
	
	public void terminate() {
		objectLoader.terminate();
	}
}

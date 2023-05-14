package engine;

import java.util.ArrayList;

import org.joml.Vector3f;
import org.joml.Vector3i;


public class Physics {
	public static float THETA = 0.001f;
	public static float G = 2.5f * 9.81f;
	
	private World world;
	
	public Physics(World world) {
		this.world = world;
	}
	
	public BlockHitInfo raycastBlock(Vector3f pos, Vector3f d, float maxRayLength) {
		float bs = World.BLOCK_SIDE;
		float l = d.length();
		float w = 0;
		
		Vector3i prevTile = null;
		
		while (w * l < maxRayLength) {
			Vector3f currentPos = new Vector3f(
					pos.x + w * d.x,
					pos.y + w * d.y,
					pos.z + w * d.z);
			Vector3i currentTile = world.positionToTile(currentPos);
			
			Block block = world.getBlock(currentTile);
			
			if (block != null && prevTile == null)
				return new BlockHitInfo(currentTile, currentTile, pos, pos);
			else if (block != null && prevTile != null)
				return new BlockHitInfo(currentTile, prevTile, pos, currentPos);
			
			float borderX = currentTile.x * bs + Math.signum(d.x) * bs / 2;
			float borderY = currentTile.y * bs + Math.signum(d.y) * bs / 2;
			float borderZ = currentTile.z * bs + Math.signum(d.z) * bs / 2;
			
			float alpha = Math.abs((currentPos.x - borderX) / d.x);
			float beta = Math.abs((currentPos.y - borderY) / d.y);
			float gamma = Math.abs((currentPos.z - borderZ) / d.z);
			
			float coef = Math.min(alpha, Math.min(gamma, beta));
			
			w = w + coef + Physics.THETA;
			prevTile = currentTile;
		}
		
		return null;
	}
	
	public BlockHitInfo vectorIntersect(Vector3f origin, Vector3f target) {
		return raycastBlock(origin, target, target.length());
	}
	
	public Vector3f vectorFreeScale(Vector3f origin, Vector3f velocity, float prevent) {
		BlockHitInfo w = vectorIntersect(origin, velocity);
		if (w == null) return velocity;
		return w.prevent(prevent).getRay();
	}
	
	public Vector3f vectorFreeScale(Vector3f origin, Vector3f velocity) {
		return vectorFreeScale(origin, velocity, 0);
	}
	
	public Vector3f vectorFreeScaleMultiple(Vector3f[] origins, Vector3f velocity, float prevent) {
		ArrayList<Vector3f> vectors = ArrayUtils.map(ArrayUtils.toArrayList(origins),
				(origin) -> vectorFreeScale(origin, velocity, prevent));
		return ArrayUtils.getMin(vectors, (v) -> v.length());
	}
	
	public Vector3f vectorFreeScaleMultiple(Vector3f[] origins, Vector3f velocity) {
		return vectorFreeScaleMultiple(origins, velocity, 0);
	}
}

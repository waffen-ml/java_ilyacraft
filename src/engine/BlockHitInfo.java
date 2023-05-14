package engine;

import org.joml.Vector3f;
import org.joml.Vector3i;

public class BlockHitInfo {
	
	private final Vector3i hitTile;
	private final Vector3i hitSide;
	private final Vector3f origin;
	private final Vector3f hitPoint;
	private final Vector3f ray;
	
	
	public BlockHitInfo(Vector3i hitTile, Vector3i prev, Vector3f origin, Vector3f hitPoint) {
		this.origin = origin;
		this.hitTile = hitTile;
		this.hitPoint = hitPoint;
		this.ray = new Vector3f(hitPoint.x - origin.x,
				hitPoint.y - origin.y,
				hitPoint.z - origin.z);
		
		int x = Utils.clamp(prev.x - hitTile.x, -1, 1);
		int y = Utils.clamp(prev.y - hitTile.y, -1, 1);
		int z = Utils.clamp(prev.z - hitTile.z, -1, 1);
		
		if (x != 0 && y != 0) y = 0;
		if (x != 0 && z != 0) z = 0;
		if (y != 0 && z != 0) y = 0;
		
		this.hitSide = new Vector3i(x, y, z);
	}
	
	public boolean innerHit() {
		return hitSide.x == 0 &&
			   hitSide.y == 0 &&
			   hitSide.z == 0;
	}
	
	public Vector3i getHitTile() {
		return hitTile;
	}
	
	public Vector3i getHitSide() {
		return hitSide;
	}
	
	public Vector3f getRay() {
		return ray;
	}
	
	public Vector3f getHitPoint() {
		return hitPoint;
	}
	
	public Vector3f getOrigin() {
		return origin;
	}
	
	public Vector3i getPrevious() {
		return new Vector3i(hitTile.x + hitSide.x,
				hitTile.y + hitSide.y,
				hitTile.z + hitSide.z);
	}
	
	public BlockHitInfo prevent(float delta) {
		if (innerHit()) return this;
		float coef = 1 - delta / ray.length();
		
		Vector3f newHitPoint = new Vector3f(
				origin.x + ray.x * coef,
				origin.y + ray.y * coef,
				origin.z + ray.z * coef);
		return new BlockHitInfo(hitTile, getPrevious(),
				origin, newHitPoint);
	}
	
}

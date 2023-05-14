package engine;

import org.joml.Vector3f;
import org.joml.Vector3i;
import java.util.ArrayList;

public class Collider {
	private Vector3f position;
	private Vector3f size;
	private Vector3i matrixShape;
	private Vector3f matrixGap;
	private Vector3f[][] relMatrix;
	
	public Collider(Vector3f startPos, Vector3f size) {
		this.size = size;
		this.position = startPos;
		
		int hX = (int)(size.x / World.BLOCK_SIDE) + 2;
		int hY = (int)(size.y / World.BLOCK_SIDE) + 2;
		int hZ = (int)(size.z / World.BLOCK_SIDE) + 2;
		
		matrixShape = new Vector3i(hX, hY, hZ);
		matrixGap = new Vector3f(
				size.x / (hX - 1),
				size.y / (hY - 1),
				size.z / (hZ - 1));
		
		ArrayList<Vector3f[]> sides = new ArrayList<>();
		
		for (int a = 2; a > 0; a--)
			for (int b = a - 1; b >= 0; b--)
				for (int s = -1; s < 1; s++) {
					Vector3f[] side = getRelSideMatrix(a, b, s);
					sides.add(side);
				}
		
		relMatrix = sides.toArray(new Vector3f[sides.size()][]);
		
		/* 0, 1 -> (z, y) -> -x, +x
		 * 2, 3 -> (z, x) -> -y, +y
		 * 4, 5 -> (y, x) -> -z, +z
		 */
	}
	
	private Vector3f[] getRelSideMatrix(int a, int b, float w) {
		int c = 3 - a - b;
		ArrayList<Vector3f> points = new ArrayList<>();

		float startA = -size.get(a) / 2;
		float startB = -size.get(b) / 2;
		float constC = normalizeSelector(w) * size.get(c) / 2;
		
		for (int i = 0; i < matrixShape.get(a); i++)
			for (int j = 0; j < matrixShape.get(b); j++) {
				Vector3f point = new Vector3f();
				point.setComponent(a, startA + matrixGap.get(a) * i);
				point.setComponent(b, startB + matrixGap.get(b) * j);
				point.setComponent(c, constC);
				points.add(point);
			}
		
		return points.toArray(new Vector3f[points.size()]);
	}
	
	private int normalizeSelector(float s) {
		int q = (int)Math.signum(s);
		return q >= 0? 1 : q;
	}
	
	public Vector3f[] getSideMatrix(int num) {
		ArrayList<Vector3f> m = ArrayUtils.map(
				ArrayUtils.toArrayList(relMatrix[num]), (v) -> {
					return new Vector3f(v.x + position.x,
							v.y + position.y,
							v.z + position.z);
				});
		return m.toArray(new Vector3f[m.size()]);
	}
	
	private Vector3f[] getNMatrix(float w, int offset) {
		return getSideMatrix((w > 0? 1 : 0) + offset);
	}
	
	public Vector3f[] getXMatrix(float w) {
		return getNMatrix(w, 0);
	}
	
	public Vector3f[] getYMatrix(float w) {
		return getNMatrix(w, 2);
	}
	
	public Vector3f[] getZMatrix(float w) {
		return getNMatrix(w, 4);
	}
	
	public Vector3f getSize() {
		return size;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public void setPosition(Vector3f pos) {
		position.set(pos);
	}
	
	public void move(Vector3f vel) {
		position.add(vel);
	}
	
	public Vector3f getMainPoint(Vector3i pos) {
		float hX = Utils.clamp(pos.x, -1, 1) * size.x / 2;
		float hY = Utils.clamp(pos.y, -1, 1) * size.y / 2;
		float hZ = Utils.clamp(pos.z, -1, 1) * size.z / 2;
		
		return new Vector3f(
				position.x + hX,
				position.y + hY,
				position.z + hZ);
	}
	
}

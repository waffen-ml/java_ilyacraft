package engine;

import org.joml.Vector3f;


class TransformUnit extends Vector3f {
	public TransformUnit(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public TransformUnit() { }
	
	public TransformUnit copy() {
		return new TransformUnit(x, y, z);
	}
	
	public TransformUnit setX(float x) {
		this.x = x;
		return this;
	}
	
	public TransformUnit setY(float y) {
		this.y = y;
		return this;
	}
	
	public TransformUnit setZ(float z) {
		this.z = z;
		return this;
	}
	
	public TransformUnit addX(float x) {
		return setX(this.x + x);
	}
	
	public TransformUnit addY(float y) {
		return setY(this.y + y);
	}
	
	public TransformUnit addZ(float z) {
		return setZ(this.z + z);
	}
	
}

class Position extends TransformUnit {
	
	public Position(float x, float y, float z) {
		super(x, y, z);
	}
	
	public Position() { }

}

class Rotation extends TransformUnit {
	
	public Rotation(float x, float y, float z) {
		super(x, y, z);
	}
	
	public Rotation() { }
	
	public float normX() {
		return Utils.normalizeAngle(x);
	}

	public float normY() {
		return Utils.normalizeAngle(y);
	}
	
	public float normZ() {
		return Utils.normalizeAngle(z);
	}
	
	public Rotation normalize() {
		x = normX();
		y = normY();
		z = normZ();
		return this;
	}
	
	public Vector3f inDegrees() {
		return new Vector3f(
				(float)Math.toDegrees(x),
				(float)Math.toDegrees(y),
				(float)Math.toDegrees(z));
	}
	
}


public class Transform {
	public Position position = new Position();
	public Rotation rotation = new Rotation();
	
	public Transform(Vector3f pos, Vector3f rot) {
		position.set(pos);
		rotation.set(rot);
	}
	
	public Transform() { }

	public void translate(float x, float y, float z) {
		Vector3f velocity = new Vector3f(x, y, z);
		velocity = Utils.rotate3DVector(velocity, rotation);
		move(velocity);
	}
	
	public void translate(Vector3f v) {
		translate(v.x, v.y, v.z);
	}
	
	public void move(float x, float y, float z) {
		position.add(x, y, z);
	}
	
	public void move(Vector3f v) {
		move(v.x, v.y, v.z);
	}
	
	public void rotate(float x, float y, float z) {
		rotation.add(x, y, z);
	}
	
	public void rotate(Vector3f v) {
		rotate(v.x, v.y, v.z);
	}
	
	public void setPosition(float x, float y, float z) {
		position.set(x, y, z);
	}
	
	public void setPosition(Vector3f pos) {
		setPosition(pos.x, pos.y, pos.z);
	}
	
	public void setRotation(float x, float y, float z) {
		rotation.set(x, y, z);
	}
	
	public void setRotation(Vector3f rot) {
		setRotation(rot.x, rot.y, rot.z);
	}
}

package engine;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
	public static float SENSITIVITY = 0.2f;
	
	private Vector3f rotation;
	
	public Camera() {
		rotation = new Vector3f(0, 0, 0);
	}
	
	public void setRotation(Vector3f rot) {
		rotation.set(rot);
	}
	
	public void setRotation(float x, float y, float z) {
		setRotation(new Vector3f(x, y, z));
	}
	
	public Vector3f getRotation() {
		return rotation;
	}
	
	public void rotate(Vector3f rot) {
		rotation.add(rot);
	}
	
	public void rotate(float x, float y, float z) {
		rotate(new Vector3f(x, y, z));
	}
	
	public void applyMouseRotation(Vector2f vel) {
		rotate(0, vel.x * Camera.SENSITIVITY, 0);
		float clampedX = Utils.clamp(
				rotation.x - vel.y * Camera.SENSITIVITY,
				-Utils.HALF_PI, Utils.HALF_PI);
		rotation.x = clampedX;
	}
	
}
package engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {
	
	public static Matrix4f createTransformationMatrix(Entity entity) {
		Matrix4f matrix = new Matrix4f();
		matrix.identity().translate(entity.transform.position).
			rotateX(entity.transform.rotation.x).
			rotateY(entity.transform.rotation.y).
			rotateZ(entity.transform.rotation.z).
			scale(entity.getScale());
		return matrix;
	}
	
	public static Matrix4f getViewMatrix(Transform camera) {
		Vector3f pos = camera.position;
		Vector3f rot = camera.rotation;
		Matrix4f matrix = new Matrix4f();
		matrix.identity();
		matrix.rotate(-rot.x, new Vector3f(1, 0, 0))
			.rotate(rot.y, new Vector3f(0, 1, 0))
			.rotate(rot.z, new Vector3f(0, 0, 1));
		matrix.translate(-pos.x, -pos.y, -pos.z);
		return matrix;
	}
}

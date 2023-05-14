package engine;

import org.joml.Vector3f;

public class Entity {
	private Model model;
	private float scale;
	public final Transform transform;
	
	public Entity(Model model, Vector3f pos, Vector3f rotation, float scale) {
		this.model = model;
		this.scale = scale;
		transform = new Transform(pos, rotation);
	}
	
	public float getScale() {
		return scale;
	}
	
	public Model getModel() {
		return model;
	}
	
}

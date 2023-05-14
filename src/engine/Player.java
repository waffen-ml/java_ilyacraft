package engine;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.glfw.GLFW;

public class Player {
	public static float PLAYER_SIDE = 1f;
	public static float PLAYER_HEIGHT = 2.8f;
	public static float PLAYER_SPEED = 15f;
	public static float RUN_MULTIPLIER = 2.0f;
	public static float CAMERA_POS = 0.9f;
	public static float MAX_CLICK_DISTANCE = 10f;
	public static float JUMP_FORCE = 16f;
	public static float APPLY_FACTOR = 2f;
	
	private final Game game;
	private final Collider collider;
	private final Input input;
	private final Camera camera = new Camera();
	private final Inventory inventory = new Inventory(new String[] {
			"stone", "dirt", "grass", "planks", "log", "leaves"
		});
	
	private float verticalForce = 0f;
	private boolean isGrounded = false;
	private boolean isFlying = false;
	
	public Player(Vector3f startPos, Game game) {
		this.collider = new Collider(startPos, new Vector3f(
				PLAYER_SIDE, PLAYER_HEIGHT, PLAYER_SIDE));	
		this.game = game;
		
		input = new Input(game.window);
		input.mouse.setLocked(true);
		input.mouse.setVisible(false);
	
	}
	
	public Vector3f getCameraPosition() {
		return new Vector3f(collider.getPosition().x,
				collider.getPosition().y + collider.getSize().y * (Player.CAMERA_POS - 0.5f),
				collider.getPosition().z);
	}
	
	public Transform getCameraTransform() {
		return new Transform(getCameraPosition(), camera.getRotation());
	}
	
	private void handleFlying(float dt) {
		
		if (input.isKeyDown(Input.P))
			isFlying = !isFlying;
		
		if (!isFlying) return;
		
		float w = 0;
		
		if (input.isKeyPressed(Input.L_SHIFT))
			w -= 1;
		if (input.isKeyPressed(Input.SPACE))
			w += 1;
		
		float velocity = w * dt * Player.PLAYER_SPEED;
		
		move(new Vector3f(0, velocity, 0));
	}
	
	private void verticalMovement(float dt) {
		
		if (isFlying) {
			return;
		}
		
		verticalForce -= dt * Physics.G * APPLY_FACTOR;

		float v = verticalForce * dt;
		
		float yHat = move(new Vector3f(0, v, 0)).y;
		
		if (v > 0 && yHat < v)
			verticalForce = 0;
		else if (v < 0 && yHat > v) {
			verticalForce = 0;
			isGrounded = true;
		} else isGrounded = false;
		
		if (isGrounded && input.isKeyDown(Input.SPACE))
			verticalForce += Player.JUMP_FORCE;
		
	}
	
	private void handleItemChanging() {
		if (input.isKeyDown(Input.E))
			inventory.next();
		else if (input.isKeyDown(Input.Q))
			inventory.next();
	}
	
	private void handleMoving(float dt) {
		Vector3f vel = new Vector3f(0, 0, 0);
		
		if (input.isKeyPressed(Input.W))
			vel.z = -dt;
		if (input.isKeyPressed(Input.S))
			vel.z = dt;
		
		if (input.isKeyPressed(Input.A))
			vel.x = -dt;
		if (input.isKeyPressed(Input.D))
			vel.x = dt;
		
		float coef = input.isKeyPressed(Input.L_CTRL)?
				Player.RUN_MULTIPLIER : 1;
		
		vel.x *= coef * Player.PLAYER_SPEED;
		vel.y *= coef * Player.PLAYER_SPEED;
		vel.z *= coef * Player.PLAYER_SPEED;
		
		translate(vel);
	}
	
	private void handleMouseRotation(float dt) {
		Vector2f rawVel = input.mouse.getVelocity();
		camera.applyMouseRotation(rawVel.mul(dt));
	}
	
	private boolean doesContainPlayer(Vector3i tile) {
		Vector3i j = new Vector3i(1, 1, 1);
		Vector3f extrem1 = collider.getMainPoint(j);
		Vector3f extrem2 = collider.getMainPoint(j.mul(-1));
		Vector3i t1 = game.world.positionToTile(extrem1);
		Vector3i t2 = game.world.positionToTile(extrem2);
		
		return tile.x <= t1.x && tile.x >= t2.x
				&& tile.y <= t1.y && tile.y >= t2.y
				&& tile.z <= t1.z && tile.z >= t2.z;
	}
	
	private void clickOnBlock(boolean place) {
		Vector3f d = Utils.rotate3DVector(new Vector3f(0, 0, -1), camera.getRotation());
		BlockHitInfo hitInfo = game.physics.raycastBlock(getCameraPosition(), d, Player.MAX_CLICK_DISTANCE);
		
		if (hitInfo == null) return;
		if (!place) {
			game.world.destroyBlock(hitInfo.getHitTile());
			return;
		}
		if (hitInfo.innerHit()) return;
		Vector3i prevTile = hitInfo.getPrevious();
		
		if (game.world.getBlock(prevTile) != null) return;
		if (doesContainPlayer(prevTile)) return;
		
		game.world.placeBlock(prevTile, inventory.item());
	}
	
	private void handleBlockClicking() {
		if (input.mouse.isLeftButtonDown())
			clickOnBlock(false);
		else if (input.mouse.isRightButtonDown())
			clickOnBlock(true);
	}
	
	public Vector3f translate(Vector3f velocity) {
		float yRot = camera.getRotation().y;
		Vector3f rotation = new Vector3f(0, yRot, 0);
		Vector3f absolute = Utils.rotate3DVector(velocity, rotation);
		return move(absolute);
	}
	
	public Vector3f move(Vector3f velocity) {
		
		//if (isFlying) {
		//	collider.move(velocity);
		//	return velocity;
		//}
		
		Vector3f[] xSide = collider.getXMatrix(velocity.x);
		Vector3f[] ySide = collider.getYMatrix(velocity.y);
		Vector3f[] zSide = collider.getZMatrix(velocity.z);
		
		Vector3f xComp = game.physics.vectorFreeScaleMultiple(
				xSide, new Vector3f(velocity.x, 0, 0), Physics.THETA);
		Vector3f yComp = game.physics.vectorFreeScaleMultiple(
				ySide, new Vector3f(0, velocity.y, 0), Physics.THETA);
		Vector3f zComp = game.physics.vectorFreeScaleMultiple(
				zSide, new Vector3f(0, 0, velocity.z), Physics.THETA);
		
		Vector3f result = xComp.add(yComp).add(zComp);
		
		collider.move(result);
		
		return result;
	}
	
	public void update(float dt) {
		handleMouseRotation(dt);
		handleMoving(dt);
		handleItemChanging();
		handleBlockClicking();
		verticalMovement(dt);
		handleFlying(dt);
		
		if (input.isKeyDown(Input.R))
			collider.setPosition(new Vector3f(10f, 40f, 10f));
		
		input.update();
	}
	
}

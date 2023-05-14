package engine;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import java.util.HashMap;
import java.util.Map;


interface CheckInput {
	public boolean update();
}


class InputUnit {
	private CheckInput checkFunction = null;
	private boolean pressed = false;
	private boolean down = false;
	private boolean up = false;
	
	public InputUnit(CheckInput checkFunction) {
		this.checkFunction = checkFunction;
	}
	
	public InputUnit() { }
	
	public void update() {
		if (checkFunction == null) return;
		update(checkFunction.update());
	}
	
	public void update(boolean state) {
		down = state && !pressed;
		up = !state && pressed;
		pressed = state;
	}
	
	public boolean isPressed() {
		return pressed;
	}
	
	public boolean isDown() {
		return down;
	}
	
	public boolean isUp() {
		return up;
	}
}


class Mouse {
	private final WindowManager window;
	
	private Vector2d previousPos = new Vector2d(0, 0);
	private Vector2d currentPos = new Vector2d(0, 0);
	private Vector2f velocity = new Vector2f(0, 0);
	
	private boolean inWindow = false;
	private boolean locked = false;
	private boolean visible = true;
	
	private InputUnit leftButton = new InputUnit();
	private InputUnit rightButton = new InputUnit();
	
	public Mouse(WindowManager window) {
		this.window = window;
		init();
	}
	
	private void init() {
		GLFW.glfwSetCursorPosCallback(window.getWindow(), (window, xpos, ypos) -> {
			currentPos.set(xpos, ypos);
		});
		GLFW.glfwSetCursorEnterCallback(window.getWindow(), (window, entered) -> {
			inWindow = entered;
		});
		GLFW.glfwSetMouseButtonCallback(window.getWindow(), (window, button, action, mods) -> {
			
			if (button == GLFW.GLFW_MOUSE_BUTTON_1)
				leftButton.update(action == GLFW.GLFW_PRESS);
			if (button == GLFW.GLFW_MOUSE_BUTTON_2)
				rightButton.update(action == GLFW.GLFW_PRESS);
		});
	}
	
	private Vector2d getCenter() {
		return new Vector2d(window.width / 2, window.height / 2);
	}
	
	public void update() {
		double diffX = currentPos.x - previousPos.x;
		double diffY = currentPos.y - previousPos.y;
		velocity.set((float)diffX, (float)diffY);
		
		if (locked) {
			Vector2d center = getCenter();
			currentPos.set(center);
			GLFW.glfwSetCursorPos(window.getWindow(), center.x, center.y);
		}
		
		previousPos.set(currentPos);
		
		leftButton.update(leftButton.isPressed());
		rightButton.update(rightButton.isPressed());
	}
	
	public Vector2f getVelocity() {
		return velocity;
	}
	
	public Vector2d getCurrentPos() {
		return currentPos;
	}
	
	public void setLocked(boolean state) {
		locked = state;
	}
	
	public boolean isLocked() {
		return locked;
	}
	
	public void setVisible(boolean state) {
		int h = state? GLFW.GLFW_CURSOR_NORMAL : GLFW.GLFW_CURSOR_HIDDEN;
		GLFW.glfwSetInputMode(window.getWindow(), GLFW.GLFW_CURSOR, h);
		visible = state;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public boolean isLeftButtonPressed() {
		return leftButton.isPressed();
	}
	
	public boolean isRightButtonPressed() {
		return rightButton.isPressed();
	}
	
	public boolean isLeftButtonUp() {
		return leftButton.isUp();
	}
	
	public boolean isRightButtonUp() {
		return rightButton.isUp();
	}
	
	public boolean isLeftButtonDown() {
		return leftButton.isDown();
	}
	
	public boolean isRightButtonDown() {
		return rightButton.isDown();
	}
	
	public boolean isInWindow() {
		return inWindow;
	}
}


public class Input {
	
	public static final int A = GLFW.GLFW_KEY_A;
	public static final int B = GLFW.GLFW_KEY_B;
	public static final int C = GLFW.GLFW_KEY_C;
	public static final int D = GLFW.GLFW_KEY_D;
	public static final int E = GLFW.GLFW_KEY_E;
	public static final int F = GLFW.GLFW_KEY_F;
	public static final int G = GLFW.GLFW_KEY_G;
	public static final int H = GLFW.GLFW_KEY_H;
	public static final int I = GLFW.GLFW_KEY_I;
	public static final int J = GLFW.GLFW_KEY_J;
	public static final int K = GLFW.GLFW_KEY_K;
	public static final int L = GLFW.GLFW_KEY_L;
	public static final int M = GLFW.GLFW_KEY_M;
	public static final int N = GLFW.GLFW_KEY_N;
	public static final int O = GLFW.GLFW_KEY_O;
	public static final int P = GLFW.GLFW_KEY_P;
	public static final int Q = GLFW.GLFW_KEY_Q;
	public static final int R = GLFW.GLFW_KEY_R;
	public static final int S = GLFW.GLFW_KEY_S;
	public static final int T = GLFW.GLFW_KEY_T;
	public static final int U = GLFW.GLFW_KEY_U;
	public static final int V = GLFW.GLFW_KEY_V;
	public static final int W = GLFW.GLFW_KEY_W;
	public static final int X = GLFW.GLFW_KEY_X;
	public static final int Y = GLFW.GLFW_KEY_Y;
	public static final int Z = GLFW.GLFW_KEY_Z;
	
	public static final int SPACE = GLFW.GLFW_KEY_SPACE;
	public static final int L_SHIFT = GLFW.GLFW_KEY_LEFT_SHIFT;
	public static final int R_SHIFT = GLFW.GLFW_KEY_RIGHT_SHIFT;
	public static final int L_ALT = GLFW.GLFW_KEY_LEFT_ALT;
	public static final int R_ALT = GLFW.GLFW_KEY_RIGHT_ALT;
	public static final int L_CTRL = GLFW.GLFW_KEY_LEFT_CONTROL;
	public static final int R_CTRL = GLFW.GLFW_KEY_RIGHT_CONTROL;
	
	public final Mouse mouse;
	private final WindowManager window;
	private Map<Integer, InputUnit> inputMap = new HashMap<>();
	
	public Input(WindowManager window) {
		this.window = window;
		mouse = new Mouse(window);
	}
	
	private InputUnit getUnit(int code) {
		InputUnit unit = inputMap.get(code);
		if (unit == null) {
			unit = new InputUnit(() -> window.isKeyPressed(code));
			inputMap.put(code, unit);
		}
		return unit;
	}

	public boolean isKeyPressed(int code) {
		return getUnit(code).isPressed();
	}
	
	public boolean isKeyDown(int code) {
		return getUnit(code).isDown();
	}
	
	public boolean isKeyUp(int code) {
		return getUnit(code).isUp();
	}
	
	public void update() {
		for (InputUnit unit : inputMap.values())
			unit.update();
		mouse.update();
	}
}

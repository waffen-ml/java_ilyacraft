package engine;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;

public class Launcher {
	public static void main(String[] args) {
		WindowManager window = new WindowManager("Fuck", 1280, 720, true);
		Engine engine = new Engine(window);
		engine.addScene("default", new Game());
		
		try {
			engine.start("default");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

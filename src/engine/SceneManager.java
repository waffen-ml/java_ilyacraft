package engine;

import java.util.HashMap;
import java.util.Map;

public class SceneManager {
	private Map<String, Scene> scenes;
	private Scene currentScene;
	
	public SceneManager() {
		scenes = new HashMap<String, Scene>();
	}
	
	public void addScene(String name, Scene scene) {
		scenes.put(name, scene);
	}
	
	public Scene getScene(String name) {
		return scenes.get(name);
	}
	
	public void setCurrentScene(String name) {
		currentScene = getScene(name);
	}
	
	public Scene getCurrentScene() {
		return currentScene;
	}
}

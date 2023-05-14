package engine;

import org.lwjgl.glfw.*;

public class Engine {
	public static final long NANOSECOND = 1000000000L;
	public static final int FRAMERATE = 60;
	private static float frameTime = 1.0f / FRAMERATE;
	
	private int fps;
	private boolean isRunning;
	
	private final WindowManager window;
	private final SceneManager scenes;
	private final RenderManager renderer;
	private GLFWErrorCallback errorCallback;
	
	
	public Engine(WindowManager window) {
		this.window = window;
		scenes = new SceneManager();
		renderer = new RenderManager(window);
	}
	
	private void init() throws Exception {
		GLFW.glfwSetErrorCallback(errorCallback=GLFWErrorCallback.createPrint(System.err));
		window.init();
		renderer.init();
	}
	
	public void start(String startScene) throws Exception {
		init();
		if(isRunning) return;
		loadScene(startScene);
		run();
	}
	
	private void run() throws Exception {
		isRunning = true;
		long lastTime = System.nanoTime();
		double deltaTime = 0;
		
		while (isRunning) {
			if (window.windowShouldClose())
				stop();
			
			long startTime = System.nanoTime();
			long miniDeltaTime = startTime - lastTime;
			double miniDeltaTimeS = (double)miniDeltaTime / NANOSECOND;
			deltaTime += miniDeltaTimeS;
			lastTime = startTime;
			
			if (deltaTime < frameTime)
				continue;
			
			update((float)deltaTime);
			render();
			
			setFps((int)Math.round(1 / deltaTime));
			deltaTime = 0;
		}
	}
	
	private void stop() {
		isRunning = false;
	}
	
	private void render() {
		renderer.clear();
		getCurrentScene().render();
		window.update();
	}
	
	private void update(float dt) throws Exception {
		getCurrentScene().update(dt);
	}
	
	public void terminate() {
		terminateCurrentScene();
		renderer.terminate();
		window.terminate();
		errorCallback.free();
		GLFW.glfwTerminate();
	}

	public void addScene(String name, Scene scene) {
		scene.init(this);
		scenes.addScene(name, scene);
	}
	
	private void terminateCurrentScene() {
		Scene s = getCurrentScene();
		if (s == null) return;
		s.terminate();
	}
	
	private void initCurrentScene() {
		getCurrentScene().start();
	}
	
	private Scene getCurrentScene() {
		return scenes.getCurrentScene();
	}
	
	public void loadScene(String name) {
		terminateCurrentScene();
		scenes.setCurrentScene(name);
		initCurrentScene();
	}
	
	public int getFps() {
		return fps;
	}

	public void setFps(int fps) {
		this.fps = fps;
	}
	
	public WindowManager getWindowManager() {
		return window;
	}
	
	public RenderManager getRenderManager() {
		return renderer;
	}
}

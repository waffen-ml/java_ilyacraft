package engine;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Scanner;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;


interface AnyFunc {
	public Object oper();
}


public class Utils {
	public static float PI = (float)Math.PI;
	public static float TWO_PI = 2 * Utils.PI;
	public static float HALF_PI = Utils.PI / 2;
	
	public static FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
		buffer.put(data).flip();
		return buffer;
	}
	
	public static IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = MemoryUtil.memAllocInt(data.length);
		buffer.put(data).flip();
		return buffer;
	}
	
	public static <T extends Number & Comparable<T>> T clamp(T v, T a, T b) {
		if (v.compareTo(a) < 0) v = a;
		if (v.compareTo(b) > 0) v = b;
		return v;
	}
	
	public static String getCwd() {
		return System.getProperty("user.dir");
	}
	
	public static String getResourcePath(String suffix) {
		return Utils.getCwd() + "\\resources\\" + suffix;
	}
	
	public static String readFile(String fileName) throws Exception {
		Scanner scanner = new Scanner(Paths.get(fileName), StandardCharsets.UTF_8.name());
		String content = scanner.useDelimiter("\\A").next();
		scanner.close();
		return content;
	}
	
	public static String loadTextResource(String resourceName) throws Exception {
		return Utils.readFile(Utils.getResourcePath(resourceName));
	}
	
	public static float normalizeAngle(float angle) {
		if (angle == 0) return 0;
		int coef = angle > 0? 1 : -1;
		float w = (float)Math.floor(Math.abs(angle) / Utils.TWO_PI);
		angle = angle - coef * w * Utils.TWO_PI;
		if (angle < 0) angle += Utils.TWO_PI;
		return angle;
	}
	
	public static int getAngleQuarter(float angle) {
		angle = normalizeAngle(angle);
		if (0 <= angle && angle < Utils.HALF_PI)
			return 0;
		if (Utils.HALF_PI <= angle && angle < Utils.PI)
			return 1;
		if (Utils.PI <= angle && angle < 3 * Utils.HALF_PI)
			return 2;
		return 3;
	}
	
	public static Vector2f getAngleSign(float angle) {
		int quarter = Utils.getAngleQuarter(angle);
		float xCoef, yCoef;
		
		if (quarter < 2) yCoef = 1;
		else yCoef = -1;
		
		if (quarter == 1 || quarter == 2) xCoef = -1;
		else xCoef = 1;
		
		return new Vector2f(xCoef, yCoef);
	}
	
	
	public static Vector2f rotate2DVector(float a, float b, float beta) {
		Vector2f aVec = new Vector2f(
				(float)Math.cos(beta) * a,
				(float)Math.sin(beta) * a);
		
		Vector2f bVec = new Vector2f(
				(float)Math.cos(beta + Utils.HALF_PI) * b,
				(float)Math.sin(beta + Utils.HALF_PI) * b);
		
		return aVec.add(bVec);
	}
	
	public static Vector2f rotate2DVector(Vector2f v, float beta) {
		return Utils.rotate2DVector(v.x, v.y, beta);
	}
	
	public static Vector3f rotate3DVector(Vector3f vec, Vector3f rot) {
		Vector2f a, b;
		
		a = Utils.rotate2DVector(vec.x, 0, rot.z);
		b = Utils.rotate2DVector(a.x, 0, rot.y);
		Vector3f xNew = new Vector3f(b.x, a.y, b.y);
		
		a = Utils.rotate2DVector(vec.z, 0, -rot.x);
		b = Utils.rotate2DVector(a.x, 0, -rot.y);
		Vector3f zNew = new Vector3f(b.y, a.y, b.x);
		
		a = Utils.rotate2DVector(vec.y, 0, -rot.z);
		b = Utils.rotate2DVector(a.x, 0, rot.x);
		Vector3f yNew = new Vector3f(a.y, b.x, b.y);
		
		return xNew.add(zNew).add(yNew);
	}
	
	public static Object runSafely(AnyFunc f) {
		try {
			return f.oper();
		} catch(Exception ex) {
			System.out.println(ex);
			return null;
		}
	}
	
	public static int getSideId(String side) {
		switch(side) {
		case "front":
			return 0;
		case "back":
			return 1;
		case "top":
			return 2;
		case "bottom":
			return 3;
		case "right":
			return 4;
		case "left":
			return 5;
		default:
			return -1;
		}
	}
	
}

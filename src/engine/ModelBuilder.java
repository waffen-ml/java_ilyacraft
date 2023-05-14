package engine;

public class ModelBuilder {
	public static Model buildRect(float a, float b, float c, ObjectLoader loader, String texture) {
		
		float halfA = a / 2;
		float halfB = b / 2;
		float halfC = c / 2;
		
		float[] vertices = new float[] {
			    -halfA, halfC, halfB,
			    -halfA, -halfC, halfB,
			    halfA, -halfC, halfB,
			    halfA, halfC, halfB,
			    
			    -halfA, halfC, -halfB,
			    halfA, halfC, -halfB,
			    -halfA, -halfC, -halfB,
			    halfA, -halfC, -halfB,
			    
			    -halfA, halfC, -halfB,
			    halfA, halfC, -halfB,
			    -halfA, halfC, halfB,
			    halfA, halfC, halfB,
			    
			    halfA, halfC, halfB,
			    halfA, -halfC, halfB,
			    -halfA, halfC, halfB,
			    -halfA, -halfC, halfB,
			    
			    -halfA, -halfC, -halfB,
			    halfA, -halfC, -halfB,
			    -halfA, -halfC, halfB,
			    halfA, -halfC, halfB,
			};
			
			float[] textCoords = new float[]{
				0.0f, 0.0f,
				0.0f, 0.5f,
				0.5f, 0.5f,
				0.5f, 0.0f,
				
				0.0f, 0.0f,
				0.5f, 0.0f,
				0.0f, 0.5f,
				0.5f, 0.5f,
				
				0.0f, 0.5f,
				0.5f, 0.5f,
				0.0f, 1.0f,
				0.5f, 1.0f,
				
				0.0f, 0.0f,
				0.0f, 0.5f,
				0.5f, 0.0f,
				0.5f, 0.5f,
				
				0.5f, 0.0f,
				1.0f, 0.0f,
				0.5f, 0.5f,
				1.0f, 0.5f,
			};
			
			int[] indices = new int[]{
				0, 1, 3, 3, 1, 2,
				8, 10, 11, 9, 8, 11,
				12, 13, 7, 5, 12, 7,
				14, 15, 6, 4, 14, 6,
				16, 18, 19, 17, 16, 19,
				4, 6, 7, 5, 4, 7,
			};
		
		Model model = loader.loadModel(vertices, textCoords, indices);
		
		if (texture == "")
			return model;
		
		try {
			model.setTexture(new Texture(loader.loadTexture(texture)));
		} catch(Exception ex) {
			System.out.println(ex);
		}
		
		return model;
	}
	
	public static Model buildSide(float a, float b, ObjectLoader loader, String texture) {
		float halfA = a / 2;
		float halfB = b / 2;
		
		float[] vertices = {
			    -halfA,  halfB, 0f,
			    -halfA, -halfB, 0f,
			     halfA, -halfB, 0f,
			     halfA,  halfB, 0f,
			};
		
		int[] indices = {
			0, 1, 3,
			3, 1, 2
		};
		
		float[] textCoords = {
			0, 0,
			0, 1,
			1, 1,
			1, 0
		};
		
		Model model = loader.loadModel(vertices, textCoords, indices);
		
		if (texture == "")
			return model;
		
		try {
			model.setTexture(new Texture(loader.loadTexture(texture)));
		} catch(Exception ex) {
			System.out.println(ex);
		}
		
		return model;
	}
}

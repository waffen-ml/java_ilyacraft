package engine;

public class Inventory {
	private int index = 0;
	private String[] items;
	
	public Inventory(String[] items) {
		this.items = items;
	}
	
	private void log() {
		System.out.println("Inventory: " + item() + " (" + index + ")");
	}
	
	public void next() {
		index++;
		if (index >= items.length)
			index = 0;
		log();
	}
	
	public void previous() {
		index--;
		if (index < 0)
			index = items.length - 1;
		log();
	}
	
	public String item() {
		return items[index];
	}
}

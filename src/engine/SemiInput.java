package engine;

public class SemiInput {
	private boolean onDown;
	private boolean state;
	
	public SemiInput(boolean onDown) {
		this.state = false;
		this.onDown = onDown;
	}
	
	public boolean check(boolean w) {
		if (w && !state) {
			state = true;
			if (onDown) return true;
		}
		if (!w && state) {
			state = false;
			if (!onDown) return true;
		}
		return false;
	}
}

package isnork.g3;

public class Coord {
	private final int x;
	private final int y;

	public Coord(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
	public Coord move(int x, int y){
		return new Coord(this.x + x, this.y + y);
	}
}

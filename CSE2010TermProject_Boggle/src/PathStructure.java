import java.util.Iterator;

/**
 * Array based structure for keeping track and updating the path of a word on the boggle board.
 * 
 * @author Kyle Stead, Justyn Diaz 
 */
public class PathStructure implements Iterable<PathStructure.Node>{
	public final byte[] path;
	
	public class Node {
		final byte xy;
		
		private Node(byte xy) {
			this.xy = xy;
		}
		
		/*
		 *  Returns the X and Y value.
		 *  X location: 0000XX00
		 *  Y location: 000000YY
		 */
		public int[] getXY() {
			return new int[] {(xy & 0b1100) >>> 2, xy & 0b11};
		}
	}
	
	public PathStructure() {
		this(0);
	}
	
	public PathStructure(int length) {
		path = new byte[length];
	}
	
	/**
	 * Clones the path structure, and adds the passed in x and y integers.
	 * 
	 * @param x, y Coordinates of a given tile.
	 * @return Cloned The updated path.
	 * @throws IllegalArgumentException If the coordinates are out of bounds.
	 */
	public PathStructure cloneAdd(int x, int y) {
		if (x > 3 || x < 0 || y > 3 || y < 0) throw new IllegalArgumentException();
		
		PathStructure cloned = new PathStructure(this.path.length + 1);
		for (int i = 0; i < path.length; i++) {
			cloned.path[i] = this.path[i];
		}
		
		cloned.path[this.path.length] = (byte) ((byte) (x << 2) + (byte)(y));
		return cloned;
	}
	
	// Returns the path.
	public byte[] getPath() {
		return path;
	}
	
	@Override
	// Iterator for traveling through each position in the path.
	public Iterator<Node> iterator() {
		return new Iterator<Node>() {
			int pos = 0;
			byte[] path = getPath();
			
			@Override
			public boolean hasNext() {
				return pos < path.length;
			}

			@Override
			public Node next() {
				return new Node(path[pos++]);
			}
		};
	}
}

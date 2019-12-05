import java.util.Iterator;


/**
 * Linked List for keeping track and updating the path of a word on the boggle board.
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
	public PathStructure cloneAdd(int x, int y) {
		if (x > 3 || x < 0 || y > 3 || y < 0) throw new IllegalArgumentException();
		
		PathStructure cloned = new PathStructure(this.path.length + 1);
		for (int i = 0; i < path.length; i++) {
			cloned.path[i] = this.path[i];
		}
		
		cloned.path[this.path.length] = (byte) ((byte) (x << 2) + (byte)(y));
		//System.out.println(cloned.path[this.path.length]);
		return cloned;
	}
	
	
	public byte[] getPath() {
		return path;
	}
	@Override
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

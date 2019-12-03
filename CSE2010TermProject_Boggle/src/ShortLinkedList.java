import java.util.Arrays;
import java.util.Iterator;
import java.util.StringJoiner;

/**
 * Linked List for keeping track and updating the path of a word on the boggle board.
 * 
 * @author Kyle Stead, Justyn Diaz 
 */
public class ShortLinkedList implements Iterable<ShortLinkedList.Node>{
	public Node head;
	public Node tail;
	public int size = 0;
	
	public class Node {
		public short position;
		public Node next;
		
		public Node(short position) {
			this.position = position;
		}
		
		// Adds a newNode to the linked list.
		public void add(Node newNode) {
			this.next = newNode;
		}
		
		// Returns the X and Y value.
		public int[] getXY() {
			return new int[] {position % 4, position / 4};
		}

		@Override
		public String toString() {
			return Arrays.toString(getXY());
		}
	}
	
	/*
	 * Adds a short to the linked list.
	 * Used in clone() and cloneAdd()
	 */
	private void add(short value) {
		if (tail == null) {
			head = new Node(value);
			tail = head;
		} else {
			tail.next = new Node(value);
			tail = tail.next;
		}
		
		size ++;
	}

	// Clones a linked list, and adds the passed in x and y integers.
	public ShortLinkedList cloneAdd(int x, int y) {
		ShortLinkedList cloned = this.clone();
		cloned.add((short) (y * 4 + x));
		
		return cloned;
	}
	
	@Override
	// Clones a linked list, and returns the cloned version.
	public ShortLinkedList clone() {
		ShortLinkedList cloned = new ShortLinkedList();
		cloned.size = size;
		Node finger = head;
		
		while (finger != null) {
			cloned.add(finger.position);
			finger = finger.next;
		}
		
		return cloned;
	}
	
	@Override
	// toString() method to determine how the linked list is represented as a string.
	public String toString() {
		StringJoiner sj = new StringJoiner(", ");
		Node finger = head;
		
		while (finger != null) {
			sj.add(finger.toString());
			finger = finger.next;
		}
		
		return String.format("[%s]", sj.toString());
	}

	// Returns the size of the linked list.
	public int size() {
		return size;
	}
	
	@Override
	// Iteratator for traveling through each node in the linked list.
	public Iterator<ShortLinkedList.Node> iterator() {
		return new Iterator<ShortLinkedList.Node>() {
			Node finger = head;
			
			@Override
			public boolean hasNext() {
				return finger != null;
			}

			@Override
			public Node next() {
				Node temp = finger;
				finger = finger.next;
				return temp;
			}
		};
	}
}

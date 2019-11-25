import java.util.Arrays;
import java.util.Iterator;
import java.util.StringJoiner;

/**
 * 
 * 
 * @author Kyle Stead, Justyn Diaz 
 */
public class ShortLinkedList implements Iterable<ShortLinkedList.Node>{
	
	public Node head;
	public Node tail;
	public int size = 0;
	
	public class Node {
		public short value;
		public Node next;
		
		public Node(short value) {
			this.value = value;
		}
		
		public void add(Node newNode) {
			this.next = newNode;
		}
		public int[] getXY() {
			return new int[] {value % 4, value / 4};
		}

		@Override
		public String toString() {
			return Arrays.toString(getXY());
		}
	}
	
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

	public ShortLinkedList cloneAdd(int x, int y) {
		ShortLinkedList cloned = this.clone();
		cloned.add((short) (y * 4 + x));
		
		return cloned;
	}
	
	@Override
	public ShortLinkedList clone() {
		ShortLinkedList cloned = new ShortLinkedList();
		cloned.size = size;
		Node finger = head;
		while (finger != null) {
			cloned.add(finger.value);
			finger = finger.next;
		}
		return cloned;
	}
	
	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner(", ");
		Node finger = head;
		while (finger != null) {
			sj.add(finger.toString());
			finger = finger.next;
		}
		return String.format("[%s]", sj.toString());
	}

	public int size() {
		return size;
	}
	
	@Override
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

import java.util.Arrays;
import java.util.StringJoiner;

public class ShortLinkedList {
	
	public Node head;
	public Node tail;
	
	public class Node {
		public short value;
		public Node next;
		
		public Node(short value) {
			this.value = value;
		}
		
		public void add(Node newNode) {
			this.next = newNode;
		}
		private int[] getXY() {
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
		
	}

	public ShortLinkedList cloneAdd(int x, int y) {
		ShortLinkedList cloned = this.clone();
		cloned.add((short) (y * 4 + x));
		return cloned;
	}
	
	@Override
	public ShortLinkedList clone() {
		ShortLinkedList cloned = new ShortLinkedList();
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
}

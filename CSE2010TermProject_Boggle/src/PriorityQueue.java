/*
 * Kyle Stead
 * cse2010
 */
public class PriorityQueue {

	 class PQNode implements Comparable<PQNode> {
		String name;
		ShortLinkedList path;
		int priority;
		
		public PQNode(final String name, final int priority, final ShortLinkedList path) {
			this.name = name;
			this.priority = priority;
			this.path = path;
		}

		@Override
		public String toString() {
			return name;
		}

		@Override
		public int compareTo(PQNode that) {

			return this.priority - that.priority;
		}
	}

	private PQNode[] data;
	private int length, maxLength;

	protected int getLength() {
		return length;
	}

	protected int getMaxLength() {
		return maxLength;
	}

	public PriorityQueue(final int length) {
		data = new PQNode[length];
		this.maxLength = length;
		this.length = 0;

	}

	public void insert(final String name, final int priority, final ShortLinkedList path) {
		if (!(length < maxLength)) {
			return;
		}

		int i = length;
		data[length++] = new PQNode(name, priority, path);

		while (i != 0 && data[parentIndex(i)].compareTo(data[i]) > 0) {
			PQNode temp = data[i];
			data[i] = data[parentIndex(i)];
			data[parentIndex(i)] = temp;

			i = parentIndex(i);
		}

	}

	// did not end up needing
	public static int parentIndex(int i) {
		return (i - 1) / 2;
	}

	public static int leftChildIndex(int i) {
		return (i * 2) + 1;
	}

	public static int rightChildIndex(int i) {
		return (i * 2) + 2;
	}

	public void heapify() {
		heapify(0);
	}

	
	// heapify will find the smallest child and bring it up to the root, recursing each level down log n
	public void heapify(int level) {
		int leftIndex = leftChildIndex(level), rightIndex = rightChildIndex(level);

		int minIndex = level;

		// Swap element with smaller child
		if (leftIndex < length && data[leftIndex].compareTo(data[minIndex]) < 0) {
			minIndex = leftIndex;
		}
		if (rightIndex < length && data[rightIndex].compareTo(data[minIndex]) < 0) {
			minIndex = rightIndex;
		}
		
		// do not swap in place
		if (level != minIndex) {
			// swap parent with smaller node
			PQNode temp = data[minIndex];
			data[minIndex] = data[level];
			data[level] = temp;
			
			heapify(minIndex);
		}

	}

	// pull the smallest node and let the rest heapify, moving the last element up
	public PQNode extractMin() {
		if (length <= 0) {
			return null;
		}
		if (length == 1) {
			return data[--length];
		}

		PQNode onHold = data[0];
		data[0] = data[length - 1];
		data[--length] = null;

		heapify();

		return onHold;
	}
	
	public PQNode peekMin() {
		if (length == 0) {
			return null;
		}
		return new PQNode(data[0].name, data[0].priority, data[0].path);
	}
	
	public boolean contains(String word) {
		for (int i = 0; i < length; i++) {
			if (data[i].name.contentEquals(word)) return true;
		}
		return false;
	}

	public boolean isNEmpty() {
		return length != 0;
	}
	public boolean isEmpty() {
		return length == 0;
	}
	/*public static void main(String[] args) {
		PriorityQueue pq = new PriorityQueue(16);
		for (int i = 0; i < 16; i++) {
			Random r = new Random();
			int ri = r.nextInt(100000);

			pq.insert(ri / 1000 + "k", ri);

			System.out.println(Arrays.toString(pq.data));
		}
		for (int i = 0; i<16; i++) {
			PQNode temp = pq.extractMin();
			System.out.println(temp.priority);
		}
	}*/
}

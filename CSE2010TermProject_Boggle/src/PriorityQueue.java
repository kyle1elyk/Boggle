/**
 * Priority Queue to handle and update a list of words.
 * 
 * @author Kyle Stead, Justyn Diaz
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

	// Returns the current length.
	protected int getLength() {
		return length;
	}

	// Returns the max length.
	protected int getMaxLength() {
		return maxLength;
	}

	public PriorityQueue(final int length) {
		data = new PQNode[length];
		this.maxLength = length;
		this.length = 0;
	}

	/**
	 * Inserts a word (with its path) in a PQ based on priority via points.
	 * 
	 * @param name The word
	 * @param priority Points you can get from the word
	 * @param path The row and column location of each char in a word.
	 */
	public void insert(final String name, final int priority, final ShortLinkedList path) {
		if (!(length < maxLength)) {
			return;
		}

		int i = length;
		data[length++] = new PQNode(name, priority, path);

		// Data is set via the value of priority.
		while (i != 0 && data[parentIndex(i)].compareTo(data[i]) > 0) {
			PQNode temp = data[i];
			data[i] = data[parentIndex(i)];
			data[parentIndex(i)] = temp;

			i = parentIndex(i);
		}
	}

	// Returns index of the parent of i.
	public static int parentIndex(int i) {
		return (i - 1) / 2;
	}

	// Returns index of the left child of i.
	public static int leftChildIndex(int i) {
		return (i * 2) + 1;
	}

	// Returns index of the right child of i.
	public static int rightChildIndex(int i) {
		return (i * 2) + 2;
	}

	public void heapify() {
		heapify(0);
	}

	// Heapify will find the smallest child and bring it up to the root, recursing each level down log n
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
		
		// Do not swap in place.
		if (level != minIndex) {
			// Swap parent with smaller node.
			PQNode temp = data[minIndex];
			data[minIndex] = data[level];
			data[level] = temp;
			
			heapify(minIndex);
		}
	}

	// Pull the smallest node and let the rest heapify, moving the last element up.
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
	
	// Returns the node with minimum priority.
	public PQNode peekMin() {
		if (length == 0) {
			return null;
		}
		
		return new PQNode(data[0].name, data[0].priority, data[0].path);
	}
	
	// Returns a boolean indiciating whether the requested word is present in the PQ.
	public boolean contains(String word) {
		for (int i = 0; i < length; i++) {
			if (data[i].name.contentEquals(word)) return true;
		}
		
		return false;
	}
	
	// Return whether or not the PQ is empty.
	public boolean isEmpty() {
		return length == 0;
	}
}

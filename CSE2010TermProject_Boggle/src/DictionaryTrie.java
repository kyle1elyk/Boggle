import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Stores boggle words in a Trie, accounting for the 'Qu' tile.
 * 
 * @author Kyle Stead, Justyn Diaz
 */
public class DictionaryTrie {
	public Node root;
	
	/**
	 * Adds words to the dictionary. Will not add words that contain non A-Z characters
	 * or words that have a Q not followed by a U. Not case sensitive
	 * 
	 * @param wordRaw The word to be added to the dictionary
	 * @return Returns true if the word is in the dictionary after the operation, regardless
	 * if it was before or not. 
	 */
	public boolean insertWord(final String wordRaw) {
		final String word = wordRaw.toLowerCase();
		
		// Word has non a-z characters or it has a q without a u
		if (! (word.matches("^[a-z]*$") && !word.matches(".*q[^u].*") )) {
			return false;
		}
		
		char[] wordArr = word.toCharArray();
		Node finger = root;
		
		for (int i = 0; i < wordArr.length; i++) {
			
			// Skip over this iteration because this is the u in qu
			if (wordArr[i] == 'u' && i > 0 && wordArr[i - 1] == 'q') {
				continue;
			}
			
			finger = finger.getChild(wordArr[i]);
		}
		
		finger.isLeaf = true;
		return true;
	}
	
	/**
	 * Checks to see if the word has valid spelling
	 * 
	 * @param wordRaw The word to be checked for in the dictionary
	 * @return Whether or not the word is in the dictionary.
	 * Will return false if the word is not in proper format.
	 */
	public boolean isValid(final String wordRaw) {
		final String word = wordRaw.toLowerCase();
		
		// Word has non a-z characters or it has a q without a u
		if (! (word.matches("^[a-z]*$") && !word.matches(".*q[^u].*") )) {
			return false;
		}
		
		char[] wordArr = word.toCharArray();
		Node finger = root;
		
		for (int i = 0; i < wordArr.length; i++) {
			// Skip over this iteration because this is the u in qu
			if (wordArr[i] == 'u' && i > 0 && wordArr[i - 1] == 'q') {
				continue;
			}
			
			if (!finger.hasChild(wordArr[i])) {
				return false;
			}
			
			finger = finger.getChild(wordArr[i]);
		}
		
		return finger.isLeaf;
	}
	
	public DictionaryTrie() {
		root = new Node('\0');
	}

	public class Node {
		public final char character;
		boolean isLeaf;
		
		// TODO: Check if it is worth the memory for instant access
		private Node[] children;
		
		private Node(final char c) {
			character = c;
			children = new Node[26];
		}
		
		/**
		 * Creates a new node if it doesn't exist, otherwise returning the existing one
		 * 
		 * @param c character below this node
		 * @return The node of the character specified, created if does not exist
		 * @throws IllegalArgumentException When the char passed in is not between A and Z
		 */
		public Node getChild(char c) {
			
			// The "- 'a'" lets us set 'a' as 0, and 'z' as 25 when converted from ascii
			int index = Character.toLowerCase(c) - 'a';
			
			// Argument is not a letter A - Z
			if (index < 0 || index > 25 ) throw new IllegalArgumentException();
			
			// First time we have seen c
			if (children[index] == null) {
				children[index] = new Node(c);
			}
			
			return children[index];
		}
		
		/**
		 * Returns if there is a child of this node, representing a word existing down this path
		 * 
		 * @param c character below this node
		 * @return True if the child exists
		 * @throws IllegalArgumentException When the char passed in is not between A and Z
		 */
		public boolean hasChild(char c) {
			// The "- 'a'" lets us set 'a' as 0, and 'z' as 25 when converted from ascii
			int index = Character.toLowerCase(c) - 'a';
			
			// Argument is not a letter A - Z
			if (index < 0 || index > 25 ) throw new IllegalArgumentException();
			
			// First time we have seen c
			return (children[index] != null);
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("Initializing dictionary...");
		DictionaryTrie dict = new DictionaryTrie();
		Scanner dictFileIn = new Scanner (new File("words.3plus.qu.txt"));
		
		String line;
		int invalid = 0;
		while (dictFileIn.hasNextLine()) {
			line = dictFileIn.nextLine();
			if (!dict.insertWord(line)) invalid++;
				
		}
		dictFileIn.close();
		System.out.println("Dictionary initialized! (" + invalid + " invalid words)");
		
		Scanner stdin = new Scanner (System.in);
		System.out.println("Enter words to check:");
		int running = 0;
		while (running++ < 10) {
			System.out.printf("\r\n: ");
			System.out.println(dict.isValid(stdin.nextLine()));
		}
		stdin.close();
		
	}
}
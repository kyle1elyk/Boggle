import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Stores boggle words in a Trie, accounting for the 'Qu' tile.
 * 
 * @author Kyle Stead, Justyn Diaz
 */
public class DictionaryTrie {
	public DNode root;
	
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
		DNode finger = root;
		
		for (int i = 0; i < wordArr.length; i++) {
			
			// Skip over this iteration because this is the u in qu
			if (wordArr[i] == 'u' && i > 0 && wordArr[i - 1] == 'q') {
				continue;
			}
			
			finger = finger.getChild(wordArr[i]);
		}
		
		finger.character = Character.toUpperCase(finger.character);
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
		DNode finger = root;
		
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
		;
		return Character.isUpperCase(finger.character); //finger.isLeaf;
	}
	
	public DictionaryTrie() {
		root = new DNode('\0');
	}

	public class DNode {
		public char character;
		
		
		// TOD: Check if it is worth the memory for instant access
		// TODO: private DNode[] children;
		private ChildHashSet children;
		private DNode(final char c) {
			character = c;
			// TODO: children = new DNode[0];
			children = new ChildHashSet();
		}
		
		
		/**
		 * Creates a new node if it doesn't exist, otherwise returning the existing one
		 * 
		 * @param c character below this node
		 * @return The node of the character specified, created if does not exist
		 * @throws IllegalArgumentException When the char passed in is not between A and Z
		 */
		public DNode getChild(char c) {
			
			// The "- 'a'" lets us set 'a' as 0, and 'z' as 25 when converted from ascii
			int index = Character.toLowerCase(c) - 'a';
			
			// Argument is not a letter A - Z
			if (index < 0 || index > 25 ) throw new IllegalArgumentException();
			/* TODO:
			if (children.length < index + 1) {
				children = Arrays.copyOf(children, index + 1);
			}
			
			// First time we have seen c
			if (children[index] == null) {
				children[index] = new DNode(c);
			}
			
			return children[index];
			*/
			
			DNode child = children.hasChild(c);
			if (child == null) {
				child = children.putChild(c);
				//System.out.print("1");
			} else {
				//System.out.print("2");
			}
			//System.out.println("A");
			return child;
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
			return children.hasChild(c) != null?true:false;
			/*
			if (index >= children.length) return false;
			
			// First time we have seen c
			return (children[index] != null);
			*/
		}

		public boolean isLeaf() {
			return Character.isUpperCase(character);
		}

		
		private class ChildHashSet {

			DNode[] children;
			public ChildHashSet() {
				children = new DNode[0];
			}
			
			DNode hasChild(char c) {
				//System.out.print("hasChild " + c + " ");
				if (children.length == 0) {
					//System.out.println(" n1");
					return null;
				}
				int index = Character.toLowerCase(c) - 'a';
				int searchPosStart = index % children.length;
				for (int i = 0; i < children.length; i++) {
					if (children[(i + searchPosStart) % children.length] == null) {
						//System.out.println(" n2");
						return null;
					}
					if (Character.toLowerCase(children[(i + searchPosStart) % children.length].character) == Character.toLowerCase(c)) {
						//System.out.println(" .");
						return children[(i + searchPosStart) % children.length];
					}
				}
				//System.out.println(" n3");
				return null;
			}
			
			DNode putChild(char c) {
				DNode child = hasChild(c);
				if (child != null) return child;
				DNode[] newChildren = new DNode[children.length + 1];

				int index = Character.toLowerCase(c) - 'a';
				int searchPosStart = index % newChildren.length;
				
				for (DNode childi:children) {
					
					int tryIndex = searchPosStart % newChildren.length;
					
					while (newChildren[tryIndex] != null) {
						tryIndex = (tryIndex + 1) % newChildren.length;
						
					}
					
					newChildren[tryIndex] = childi;
				}
				
				int tryIndex = searchPosStart % newChildren.length;
				
				while (newChildren[tryIndex] != null) {
					tryIndex = (tryIndex + 1) % newChildren.length;
				}
				
				newChildren[tryIndex] = new DNode(c);
				
				children = newChildren;
				return children[tryIndex];
			}
			
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
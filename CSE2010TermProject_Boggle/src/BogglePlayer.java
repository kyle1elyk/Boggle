import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/*

  Authors (group members):  Kyle Stead, Justyn Diaz
  Email addresses of group members: kstead2016@my.fit.edu, diazj2016@my.fit.edu 
  Group name: 

  Course: CSE 2010
  Section: 3 (Java)

  Description of the overall algorithm and key data structures: 
  
*/

public class BogglePlayer {
	// Used to store and update valid words.
    PriorityQueue validWords = new PriorityQueue(20);

    // Trie that stores all words passed in from the input file.
    final DictionaryTrie dict = new DictionaryTrie();
    
	// initialize BogglePlayer with a file of English words
	public BogglePlayer(String wordFile) throws IOException {
		 
		BufferedReader reader = new BufferedReader(new FileReader(wordFile));
		String line = "";
		while (line != null) {
			
			line = reader.readLine();
			
			if (line != null) dict.insertWord(line);
		}
		reader.close();
	}

	
	char[][] board;
	
	// based on the board, find valid words
	//
	// board: 4x4 board, each element is a letter, 'Q' represents "QU",
	// first dimension is row, second dimension is column
	// ie, board[row][col]
	//
	// Return at most 20 valid words in UPPERCASE and
	// their paths of locations on the board in myWords;
	// Use null if fewer than 20 words.
	//
	// See Word.java for details of the Word class and
	// Location.java for details of the Location class
	public Word[] getWords(char[][] board) {
		
		this.board = board;
		// Performs depth-first search on each individual tile.
	    for (int i = 0; i < 16; i++) {
            dfs(board, i % 4, i / 4);
        }
		
	    /*
	     * Initializes myWords with an array of acceptable words to 
	     * pass into EvalBogglePlayer.java. Additionally, the row and
	     * column associated with the words are stored alongside them.
	     * This is used for later comparison in EvalBogglePlayer.java.
	     */
		Word[] myWords = new Word[20];
		int wordCount = 0;
		while (!validWords.isEmpty()) {
			PriorityQueue.PQNode word = validWords.extractMin();
			myWords[wordCount] = new Word(word.name);
			
			for (PathStructure.Node tile: word.path) {
				myWords[wordCount].addLetterRowAndCol(tile.getXY()[1], tile.getXY()[0]);
			}
			
			wordCount++;
		}
		
		return myWords;
	}
	
	protected void dfs(char[][] board, int x, int y) {
		
		dfs(/* board, */x, y, '\0', "", dict.root.getChild(board[y][x]), new PathStructure());
    }

    /**
     * Recursive call to locate the best possible words in the board via partial dfs, additionally terminated by trie
     * @param x current X location in board to search
     * @param y current Y location in board to search
     * @param flags represents bit pattern of spaces visited (16 bits ordered LTR, then top-down)
     * @param currentString current path visited, faster than using parent path
     * @param node Current node in the dictionary trie, children represent possible words
     * @param parentPath utilized for creating path required
     */
	private void dfs(/* char[][] board, */int x, int y, char flags, String currentString,
            DictionaryTrie.Node node, final PathStructure parentPath) {
		// board argument Only needed if this were a static implementation or multithreaded
        if (node == null)
            return;

        /**
         * Houses 16 bits representing visited state of each node in only 16 bits
         */
        char thisFlag = visit(x, y, flags);

        char c = board[y][x];
        
        PathStructure path = parentPath.cloneAdd(x, y);

        currentString += c;

        if (node.isLeaf && (validWords.getLength() < validWords.getMaxLength()
                || currentString.length() > validWords.peekMin().name.length())) {
            if (!validWords.contains(currentString.replace("q", "qu"))) {
                handleWord(currentString, path);
            }
        }
/*
        
        1 2 3 <-- check if (y > 0)
        8 X 4
        7 6 5 <-- check if (y < 3)
        ^   ^
        |   ----- check if (x < 3)
        |
        --------- check if (x > 0)
        
*/
        if (y > 0) {	// Do not check above if on top edge
            if (x > 0 && !wasVisited(x - 1, y - 1, thisFlag))	// Only check if node was not visited yet
                if (node.hasChild(board[y - 1][x - 1]))			// and that there is the possibility of a word at this location
					dfs(/* board, */ x - 1, y - 1, thisFlag, currentString, node.getChild(board[y - 1][x - 1]), path); // 1
            if (!wasVisited(x, y - 1, thisFlag))
                if (node.hasChild(board[y - 1][x]))
					dfs(/* board, */x, y - 1, thisFlag, currentString, node.getChild(board[y - 1][x]), path); // 2
            if (x < 3 && !wasVisited(x + 1, y - 1, thisFlag))
                if (node.hasChild(board[y - 1][x + 1]))
					dfs(/* board, */x + 1, y - 1, thisFlag, currentString, node.getChild(board[y - 1][x + 1]), path); // 3
        }

        if (x < 3 && !wasVisited(x + 1, y, thisFlag))	// Do not check to the right if on the right edge
            if (node.hasChild(board[y][x + 1]))
				dfs(/* board, */x + 1, y, thisFlag, currentString, node.getChild(board[y][x + 1]), path); // 4

        if (y < 3) {	// Do not check below if on bottom edge
            if (x < 3 && !wasVisited(x + 1, y + 1, thisFlag))
                if (node.hasChild(board[y + 1][x + 1]))
					dfs(/* board, */x + 1, y + 1, thisFlag, currentString, node.getChild(board[y + 1][x + 1]), path); // 5
            if (!wasVisited(x, y + 1, thisFlag))
                if (node.hasChild(board[y + 1][x]))
					dfs(/* board, */x, y + 1, thisFlag, currentString, node.getChild(board[y + 1][x]), path); // 6
            if (x > 0 && !wasVisited(x - 1, y + 1, thisFlag))
                if (node.hasChild(board[y + 1][x - 1]))
					dfs(/* board, */x - 1, y + 1, thisFlag, currentString, node.getChild(board[y + 1][x - 1]), path); // 7
        }

        if (x > 0 && !wasVisited(x - 1, y, thisFlag))	// Do not check to the left if on the left edge
            if (node.hasChild(board[y][x - 1]))
				dfs(/* board, */x - 1, y, thisFlag, currentString, node.getChild(board[y][x - 1]), path); // 8

    }
    
	/**
	 * If validWords is full, the minimum is extracted.
	 * Additionally, if q is present in a word, it is replaced with qu.
	 * 
	 * @param word The word that is passed in to handle.
	 * @param path The stored locations of each individual char in that word.
	 */
    private void handleWord(String word, final PathStructure path) {
        if (validWords.getLength() == validWords.getMaxLength()) {
            validWords.extractMin();
        }
        
        validWords.insert(word.replace("q", "qu"),
                (word.replace("q", "qu").length() * 26) + (word.charAt(0) - 'a'),
                path);
    }

	/**
	 * Checks if the requested row and column has been visited.
	 * @param x Column to be checked
	 * @param y Row to be checked
	 * @param flags bit pattern to check if node was visited
	 * @return Boolean based on if the requested location was visited or not.
	 */
    private static boolean wasVisited(final int x, final int y, final char flags) {
        char flipped = (char) (1 << (x + (y * 4)));

        return (flipped & flags) == flipped;
    }

	/**
	 * Visits a tile based on the row and column location on the boggle board.
	 * @param x Column to be visited
	 * @param y Row to be visited
	 * @param flags bit pattern that is used to generate new pattern
	 * @return The char that is located at the specified location.
	 */
    private static char visit(final int x, final int y, final char flags) {
        return (char) (flags | 1 << (x + (y * 4)));
    }

}

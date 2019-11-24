import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/*

  Authors (group members):  Kyle Stead, Justyn Diaz
  Email addresses of group members: kstead2016@my.fit.edu, diazj2016@my.fit.edu 
  Group name: 

  Course: CSE 2010
  Section: 3 (Java)

  Description of the overall algorithm and key data structures:

*/

public class BogglePlayer {
    PriorityQueue validWords = new PriorityQueue(20);

    // static String searchTerm = "sparteries";
    final DictionaryTrie dict = new DictionaryTrie();
    final PriorityQueue topSeeds = new PriorityQueue(50);
    
	// initialize BogglePlayer with a file of English words
	public BogglePlayer(String wordFile) throws FileNotFoundException {
	    Scanner dictFileIn = new Scanner(new File(wordFile));
        
        String line;
        while (dictFileIn.hasNextLine()) {
            line = dictFileIn.nextLine();
            dict.insertWord(line);
        }
      
        dictFileIn.close();        

	}

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
		
		
		Word[] myWords = new Word[20];
		int wordCount = 0;
		while (!validWords.isEmpty()) {
			PriorityQueue.PQNode word = validWords.extractMin();

			myWords[wordCount++] = new Word(word.name);
			
			for (ShortLinkedList.Node tile: word.path) {
				myWords[wordCount].addLetterRowAndCol(tile.getXY()[0], tile.getXY()[1]);
			}
			
		}
		return myWords;
	}
	
	protected void dfs(char[][] board, int x, int y) {
        dfs(board, x, y, '\0', "", dict.root.getChild(board[y][x]), new ShortLinkedList());
    }

    private void dfs(char[][] board, int x, int y, char flags, String currentString,
            DictionaryTrie.Node node, final ShortLinkedList parentPath) {

        if (/* currentString.length() > 10 && */node == null)
            return;

        /**
         * Houses 16 bits representing visited state of each node in only 16 bits
         */
        char thisFlag = visit(x, y, flags);

        char c = board[y][x];
        
        ShortLinkedList path = parentPath.cloneAdd(x, y);

        currentString += c;

        if (node.isLeaf && (validWords.getLength() < validWords.getMaxLength()
                || currentString.length() > validWords.peekMin().name.length())) {
            if (!validWords.contains(currentString.replace("q", "qu"))) {
                /*
                 * if (validWords.getLength() == validWords.getMaxLength()) {
                 * validWords.extractMin(); } validWords.insert(currentString.replace("q",
                 * "qu"), (currentString.replace("q", "qu").length() * 26) +
                 * (currentString.charAt(0) - 'a'), path);
                 * 
                 * validCounter++;
                 */
                
                handleWord(currentString, path);
                // System.out.printf("[%d, %d] = '%c'\r\n",x,y, board[y][x]);
                // System.out.println(String.format("%16s",
                // Integer.toBinaryString(flags)).replace(" ", "0"));
                // System.out.printf("%d: %s\r\n", validCounter, currentString);
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
        if (y > 0) {
            if (x > 0 && !wasVisited(x - 1, y - 1, thisFlag))
                if (node.hasChild(board[y - 1][x - 1]))
                    dfs(board, x - 1, y - 1, thisFlag, currentString, node.getChild(board[y - 1][x - 1]), path); // 1
            if (!wasVisited(x, y - 1, thisFlag))
                if (node.hasChild(board[y - 1][x]))
                    dfs(board, x, y - 1, thisFlag, currentString, node.getChild(board[y - 1][x]), path); // 2
            if (x < 3 && !wasVisited(x + 1, y - 1, thisFlag))
                if (node.hasChild(board[y - 1][x + 1]))
                    dfs(board, x + 1, y - 1, thisFlag, currentString, node.getChild(board[y - 1][x + 1]), path); // 3
        }

        if (x < 3 && !wasVisited(x + 1, y, thisFlag))
            if (node.hasChild(board[y][x + 1]))
                dfs(board, x + 1, y, thisFlag, currentString, node.getChild(board[y][x + 1]), path); // 4

        if (y < 3) {
            if (x < 3 && !wasVisited(x + 1, y + 1, thisFlag))
                if (node.hasChild(board[y + 1][x + 1]))
                    dfs(board, x + 1, y + 1, thisFlag, currentString, node.getChild(board[y + 1][x + 1]), path); // 5
            if (!wasVisited(x, y + 1, thisFlag))
                if (node.hasChild(board[y + 1][x]))
                    dfs(board, x, y + 1, thisFlag, currentString, node.getChild(board[y + 1][x]), path); // 6
            if (x > 0 && !wasVisited(x - 1, y + 1, thisFlag))
                if (node.hasChild(board[y + 1][x - 1]))
                    dfs(board, x - 1, y + 1, thisFlag, currentString, node.getChild(board[y + 1][x - 1]), path); // 7
        }

        if (x > 0 && !wasVisited(x - 1, y, thisFlag))
            if (node.hasChild(board[y][x - 1]))
                dfs(board, x - 1, y, thisFlag, currentString, node.getChild(board[y][x - 1]), path); // 8

    }
    
    private void handleWord(String word, final ShortLinkedList path) {
        if (validWords.getLength() == validWords.getMaxLength()) {
            validWords.extractMin();
        }
        validWords.insert(word.replace("q", "qu"),
                (word.replace("q", "qu").length() * 26) + (word.charAt(0) - 'a'),
                path);

    }

    private static boolean wasVisited(int x, int y, char flags) {
        char flipped = (char) (1 << (x + (y * 4)));

        return (flipped & flags) == flipped;
    }

    private static char visit(int x, int y, char flags) {
        return (char) (flags | 1 << (x + (y * 4)));
    }

}

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class BitwiseTest3 {
	static long counter = 0;
	static long validCounter = 0;
	static PriorityQueue validWords = new PriorityQueue(20);

	// static String searchTerm = "sparteries";
	static final DictionaryTrie dict = new DictionaryTrie();
	static final PriorityQueue topSeeds = new PriorityQueue(50);


	public static void main(String[] args) throws FileNotFoundException {

		
		Runtime runtime = Runtime.getRuntime();
		runtime.gc();

		long iMem = runtime.totalMemory() - runtime.freeMemory();

		System.out.print("Dictionary init...");
		Scanner dictFileIn = new Scanner(new File("words.3plus.qu.txt"));
		
		String line;
		while (dictFileIn.hasNextLine()) {
			line = dictFileIn.nextLine();
			dict.insertWord(line);

		}
		dictFileIn.close();
		System.out.println(" finished\r\n");
		boolean threaded = true;
		int iterationCount = 100000;
		 if(!threaded) {
			 
		 } else {
			final int CORES = 16;
			for (int it = 0; it < CORES; it++) {
				//final int iterLocation = i * (iterationCount / CORES);
				Thread t = new Thread(new Runnable() {
					
					@Override
					public void run() {
						
						
						
						long counterT = 0;
						long validCounterT = 0;
						PriorityQueue validWordsT = new PriorityQueue(20);
						char[][] boardT = new char[4][4];
						
						
						for (int it2 = 0; it2 < iterationCount / CORES; it2++) {
							counter = 0;
							validCounter = 0;
							validWords = new PriorityQueue(20);
							boardT = new char[4][4];
							
							long seed = (long)(Math.random() * Long.MAX_VALUE);
							Random rnd = new Random(seed);
							final ArrayList<String> boggleDices = new ArrayList<>(Arrays.asList(
					            "AAEEGN", "ABBJOO", "ACHOPS", "AFFKPS", "AOOTTW", "CIMOTU", "DEILRX", "DELRVY",
					            "DISTTY", "EEGHNW", "EEINSU", "EHRTVW", "EIOSST", "ELRTTY", "HIMNUQ", "HLNNRZ"));
							int length = boggleDices.size();
							for (int i = 0; i < 4; i++) {
					            for (int j = 0; j < 4; j++) {
					                int diceIndex = rnd.nextInt(length);
					                String dice = boggleDices.get(diceIndex);
					                boardT[i][j] = dice.charAt(rnd.nextInt(6));
					                boggleDices.set(diceIndex, boggleDices.get(length - 1));
					                boggleDices.set(length - 1, dice);
					                length--;
					            }
					        }
							
							long iTime = System.nanoTime();
							
							for (int i = 0; i < 16; i++) {
								dfs(boardT, i % 4, i / 4, validWordsT);
							}
							
							long fTime = System.nanoTime();
							long fMem = runtime.totalMemory() - runtime.freeMemory();
							
							int i = 0;
							int score = 0;
							
							double timeElapse = (fTime - iTime) / 1E9;
							long memUsed = (fMem - iMem);
					
							while (!validWordsT.isEmpty()) {
								PriorityQueue.PQNode word = validWordsT.extractMin();
								String currentString = word.name;
								double iScore = Math.pow(currentString.length() - 2, 2);
								//System.out.printf("%-22s %5s %s\r\n",String.format("%2d: %s", ++i, currentString),  "(" + (int) iScore + ")", word.path);
					
								score += iScore;
							}
							handleSeed(score, seed);
						}

					}
					});
				t.run();
			}
		}

		for (int runCount = 0; runCount < 0; runCount++) {
			
			counter = 0;
			validCounter = 0;
			validWords = new PriorityQueue(20);
			
			final char[][] board = new char[4][4];;
			
			/*board = new char[][] { { 's', 'e', 'r', 's' }, { 'p', 'a', 't', 'g' }, { 'l', 'i', 'n', 'e' },
					{ 's', 'e', 'r', 's' } };*/
			
			// gnomonologically
			/*
			board = new char[][] { { 'g', 'n', 'o', 'm' }, { 'c', 'a', 'l', 'o' }, { 'i', 'y', 'l', 'n' },
					{ 'g', 'o', 'l', 'o' } };
			 */
			/*
			board = new char[][] { {'q','e','e','n'}, {'z','z','z','z'},
			 {'a','a','a','a'}, {'a','a','a','a'} };
			*/ 
					
			// qu i n qu e t u b e r c u l a t e
			
			/*		board = new char[][] {{'q','i','n','q'},
										  {'u','l','a','e'},
										  {'c','e','t','t'},
										  {'r','e','b','u'}};
			*/
			long seed = 123456789;
			seed = (long)(Math.random() * Long.MAX_VALUE);
			if (args.length > 0) {
				seed = Long.parseLong(args[0]);
			}
			final ArrayList<String> boggleDices = new ArrayList<>(Arrays.asList(
	            "AAEEGN", "ABBJOO", "ACHOPS", "AFFKPS", "AOOTTW", "CIMOTU", "DEILRX", "DELRVY",
	            "DISTTY", "EEGHNW", "EEINSU", "EHRTVW", "EIOSST", "ELRTTY", "HIMNUQ", "HLNNRZ"));
			Random rnd = new Random(seed);
			int length = boggleDices.size();
			//Create random board
	        for (int i = 0; i < 4; i++) {
	            for (int j = 0; j < 4; j++) {
	                int diceIndex = rnd.nextInt(length);
	                String dice = boggleDices.get(diceIndex);
	                board[i][j] = dice.charAt(rnd.nextInt(6));
	                boggleDices.set(diceIndex, boggleDices.get(length - 1));
	                boggleDices.set(length - 1, dice);
	                length--;
	            }
	        }
			System.out.printf("SEED:%d\r\n", seed);
			System.out.printf("Searching through:\r\n");
			for (char[] alpha : board) {
				for (char bravo : alpha) {
					System.out.printf("%c ", bravo);
				}
				System.out.println();
			}
	
			System.out.println();
			
			long iTime = System.nanoTime();
			
			for (int i = 0; i < 16; i++) {
				//dfs(board, i % 4, i / 4);
			}
			
			
			long fTime = System.nanoTime();
			long fMem = runtime.totalMemory() - runtime.freeMemory();
	
			int i = 0;
			int score = 0;
	
			double timeElapse = (fTime - iTime) / 1E9;
			long memUsed = (fMem - iMem);
	
			while (!validWords.isEmpty()) {
				PriorityQueue.PQNode word = validWords.extractMin();
				String currentString = word.name;
				double iScore = Math.pow(currentString.length() - 2, 2);
				System.out.printf("%-22s %5s %s\r\n",String.format("%2d: %s", ++i, currentString),  "(" + (int) iScore + ")", word.path);
	
				score += iScore;
			}
	
			System.out.printf("\r\nScore: %d\r\n", score);
			System.out.printf("%d checks in %01.3fs (%fs)\r\n", counter, timeElapse, timeElapse);
			System.out.printf("(%,dKB used) (%dB)\r\n", memUsed / 8192, memUsed);
			double performance = Math.pow(score, 2) / Math.sqrt(timeElapse * memUsed);
			System.out.printf("\r\nPerformance: %2.2f\r\n", performance);
			
			if (topSeeds.getLength() >= topSeeds.getMaxLength() && 5000 - score > topSeeds.peekMin().priority) {
				topSeeds.extractMin();
				topSeeds.insert(String.valueOf(seed), 5000 - score, null);
			} else if(topSeeds.getLength() < topSeeds.getMaxLength()) {
				topSeeds.insert(String.valueOf(seed), 5000 - score, null);
			}
			
			
		}
		File f = new File("report.csv");
		
		PrintWriter out = new PrintWriter(f);
		
		while (!topSeeds.isEmpty()) {
			PriorityQueue.PQNode seedNode = topSeeds.extractMin();
			out.printf("%d, %s\r\n",  seedNode.priority, seedNode.name);
			
		}
		out.flush();
		out.close();
		System.out.printf("Top seeds saved to: %s\r\n", f.getAbsolutePath());
	}

	protected static void dfs(char[][] board, int x, int y, PriorityQueue validWords) {
		dfs(board, x, y, '\0', "", dict.root.getChild(board[y][x]), new PathStructure(), validWords);
	}

	private static void dfs(char[][] board, int x, int y, char flags, String currentString,
			DictionaryTrie.Node node, final PathStructure parentPath, PriorityQueue validWords) {

		if (/* currentString.length() > 10 && */node == null)
			return;

		/**
		 * Houses 16 bits representing visited state of each node in only 16 bits
		 */
		char thisFlag = visit(x, y, flags);

		char c = board[y][x];
		
		PathStructure path = parentPath.cloneAdd(x, y);

		currentString += c;

		//counter++;

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
				
				handleWord(currentString, path, validWords);
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
					dfs(board, x - 1, y - 1, thisFlag, currentString, node.getChild(board[y - 1][x - 1]), path, validWords); // 1
			if (!wasVisited(x, y - 1, thisFlag))
				if (node.hasChild(board[y - 1][x]))
					dfs(board, x, y - 1, thisFlag, currentString, node.getChild(board[y - 1][x]), path, validWords); // 2
			if (x < 3 && !wasVisited(x + 1, y - 1, thisFlag))
				if (node.hasChild(board[y - 1][x + 1]))
					dfs(board, x + 1, y - 1, thisFlag, currentString, node.getChild(board[y - 1][x + 1]), path, validWords); // 3
		}

		if (x < 3 && !wasVisited(x + 1, y, thisFlag))
			if (node.hasChild(board[y][x + 1]))
				dfs(board, x + 1, y, thisFlag, currentString, node.getChild(board[y][x + 1]), path, validWords); // 4

		if (y < 3) {
			if (x < 3 && !wasVisited(x + 1, y + 1, thisFlag))
				if (node.hasChild(board[y + 1][x + 1]))
					dfs(board, x + 1, y + 1, thisFlag, currentString, node.getChild(board[y + 1][x + 1]), path, validWords); // 5
			if (!wasVisited(x, y + 1, thisFlag))
				if (node.hasChild(board[y + 1][x]))
					dfs(board, x, y + 1, thisFlag, currentString, node.getChild(board[y + 1][x]), path, validWords); // 6
			if (x > 0 && !wasVisited(x - 1, y + 1, thisFlag))
				if (node.hasChild(board[y + 1][x - 1]))
					dfs(board, x - 1, y + 1, thisFlag, currentString, node.getChild(board[y + 1][x - 1]), path, validWords); // 7
		}

		if (x > 0 && !wasVisited(x - 1, y, thisFlag))
			if (node.hasChild(board[y][x - 1]))
				dfs(board, x - 1, y, thisFlag, currentString, node.getChild(board[y][x - 1]), path, validWords); // 8

	}
	
	private synchronized static void handleSeed(int score, long seed) {
		System.out.printf("[%05d] %d\r\n", score, seed);
		if (topSeeds.getLength() >= topSeeds.getMaxLength() && score > topSeeds.peekMin().priority) {
			topSeeds.extractMin();
			topSeeds.insert(String.valueOf(seed), score, null);
		} else if(topSeeds.getLength() < topSeeds.getMaxLength()) {
			topSeeds.insert(String.valueOf(seed), score, null);
		}
		
	}
	
	private synchronized static void handleWord(String word, final PathStructure path, PriorityQueue validWords) {
		if (validWords.getLength() == validWords.getMaxLength()) {
			validWords.extractMin();
		}
		validWords.insert(word.replace("q", "qu"),
				(word.replace("q", "qu").length() * 26) + (word.charAt(0) - 'a'),
				path);

		validCounter++;
	}

	private static boolean wasVisited(int x, int y, char flags) {
		char flipped = (char) (1 << (x + (y * 4)));

		return (flipped & flags) == flipped;
	}

	private static char visit(int x, int y, char flags) {
		return (char) (flags | 1 << (x + (y * 4)));
	}

}

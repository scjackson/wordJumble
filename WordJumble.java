/*
 * Wordlist taken from http://www.infochimps.com/datasets/word-list-100000-official-crossword-words-excel-readable
 *
 *
 */

import java.io.*;
import java.util.ArrayList;

public class WordJumble {

	/**
	 * Class to represent string as counts of letters. Since the order of the 
	 * letters in the input string doesnt matter, the only determinant of whether
	 * an input string can form a given word is if it has all of the necessary 
	 * characters
	 *
	 */
	static class WordBucket {

		//ASCII offset, see: http://www.asciitable.com/
		private static final int CHAR_OFFSET = 97;

		//Number of characters in the alphabet we are using
		private static final int ALPHABET_LENGTH = 26;

		//Counts of occurances of each letter ('a' -> 0, 'b' -> 1, ...)
		private int [] letterBuckets = new int [ALPHABET_LENGTH];

		//overall length of the input string
		private int length = 0;

		public WordBucket (String input) {

			//Stripping input down to valid characters (based on what the dictionary uses)
			input = input.toLowerCase().replaceAll("[^a-z]", "");
			this.length = input.length();
			char [] characters = input.toCharArray();
			
			//count individual letter occurances
			for (char c : characters) {
				this.letterBuckets[(int) c - CHAR_OFFSET] ++;
			}
		}

		/**
		 * Test to see if this WordBucket is a jumble of the input string
		 *
		 */
		public boolean canFormString (String input) {
			
			//Quick check to see if the input is longer than WordBucket. If so, dont bother to check character counts
			if (input.length() > this.length) {
				return false;
			}

			WordBucket inputBucket = new WordBucket(input);
			for (int x = 0; x < ALPHABET_LENGTH; x ++) {
				if (this.letterBuckets[x] < inputBucket.letterBuckets[x]) {
					return false;
				}
			}

			//if we have all of the necessary characters, then this WordBucket is a jumble of the input
			return true;
		}
	}


	/**
	 * Given an input string, test to see if it is a jumble of any of the words in the 
	 * "wordlist.txt" dictionary. Returns a list of all words it can form
	 *
	 */
	public static ArrayList <String> testAllWords (String input) {

		WordBucket inputBucket = new WordBucket(input);
		ArrayList <String> validWords = new ArrayList <String> ();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File("wordlist.txt")));
			String currentLine;
			while ((currentLine = reader.readLine()) != null) {
				if (inputBucket.canFormString(currentLine)) {
					validWords.add(currentLine);
				}
			}
		} catch (Exception e) {
			System.out.println("There was an issue reading the dictionary:");
			System.out.println(e.getMessage());
			System.exit(0);
		}
		return validWords;
	}

	/**
	 * Read input from the command line
	 *
	 */
	public static void main (String [] args) {
		if (args.length > 0) {
			System.out.println(testAllWords(args[0]));
		} else {
			System.err.println("Expected usage is 'wordJumble <input string>'");
			System.exit(1);
		}
	}
}
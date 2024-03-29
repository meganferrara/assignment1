package search;

import java.util.ArrayList;

import java.util.Arrays;

import java.util.regex.Pattern;

/**
 * An improved tokenizer class that uses the following tokenization rules: -
 * tokens are delimited by whitespace - Single quotes at the beginning and end
 * of words should be separate tokens - Numbers should stay together. A number
 * can start with a `+' or a `-', can have any number of digits, commas and
 * periods interspersed, but must end in a digit (note this is a more general
 * definition that accepts things like ``192.168.1" and other things like
 * ``-129.,24.34"). - An abbreviation is any single letter followed by a period
 * repeated 2 or more times. In regex terms, ``(\w\.){2,}". For example,
 * ``I.B.M.", ``S.A.T." and ``p.m." are all valid abbreviations, while ``Mr." or
 * ``I.B" are not. All abbreviations should have the periods removed, i.e.
 * ``I.B.M" becomes ``IBM". - Finally, ``. , ? : ; " ` ( ) % $" should all be
 * treated as separate tokens, regardless of where they appear (as long as they
 * don't conflict with the above rules). So ``$10,000" becomes two tokens ``$"
 * and ``10,000" and ``I wondered,is this a test?" becomes 8 tokens, with the
 * ``," and ``?" as separate tokens.
 * 
 * @author Megan and Isabelle
 * @version 2/7/18
 *
 */
public class ImprovedTokenizer implements Tokenizer {
	// Given text, tokenize the text into individual tokens
	// and return an ArrayList with those tokens

	/**
	 * Given text, tokenize the text into individual tokens and return and ArrayList
	 * with those tokens
	 */
	public ArrayList<String> tokenize(String text) {
		// TODO
		// ******************SEPERATE WORDS ON WHITE SPACE*********************//
		// 1: separate words based on white space (look at simpleTokenizer)

		text = text.replaceAll("\\s+", " ");
		if (text.startsWith(" ")) {
			text = text.substring(1);
		}
		if (text.endsWith(" ")) {
			text = text.substring(0, text.length() - 1);
		}
		String[] tempTokens = text.split(" ");

		// **********************************************************************//

		// ************CHECKS FOR SINGLE QUOTES and "." "," ";" ":" "!" "?" ")"******//
		// 2: Check for single quotes at the beginning and end of words and separate
		// from tokens
		// You will probably pass the tempTokens through two checkers to check for at
		// the beginning and at the end
		ArrayList<String> firstPass = new ArrayList<String>();

		// Temp: tempTokens --> this is basically duplicating the tempTokens string to
		// look through
		for (String temp : tempTokens) {

			while (temp.startsWith("'") || temp.startsWith("`")) {
				if (temp.startsWith("'")) {
					firstPass.add("'");
				} else if (temp.startsWith("`")) {
					firstPass.add("`");
				}
				temp = temp.substring(1);
			}

			int endingSymbolsCount = 0;
			String endingSymbol = ""; //This could possibly add a white space after a word with no punct

			while (temp.endsWith("'") || temp.endsWith(".") || temp.endsWith(",") || temp.endsWith(";")
					|| temp.endsWith(":") || temp.endsWith("!") || temp.endsWith("?") || temp.endsWith(")")) {
				endingSymbol = "";
				if (temp.endsWith("'")) {
					endingSymbolsCount++;
					endingSymbol = "'"; 
					firstPass.add(endingSymbol);
					temp = temp.substring(0, temp.length() - 1);
				} else if (temp.endsWith(".")) {
					endingSymbolsCount++;
					endingSymbol = "."; 
					firstPass.add(endingSymbol);
					temp = temp.substring(0, temp.length() - 1);
				} else if (temp.endsWith(",")) {
					endingSymbolsCount++;
					endingSymbol = ","; 
					firstPass.add(endingSymbol);
					temp = temp.substring(0, temp.length() - 1);
				} else if (temp.endsWith(";")) {
					endingSymbolsCount++;
					endingSymbol = ";"; 
					firstPass.add(endingSymbol);
					temp = temp.substring(0, temp.length() - 1);
				} else if (temp.endsWith(":")) {
					endingSymbolsCount++;
					endingSymbol = ":"; 
					firstPass.add(endingSymbol);
					temp = temp.substring(0, temp.length() - 1);
				} else if (temp.endsWith("!")) {
					endingSymbolsCount++;
					endingSymbol = "!"; 
					firstPass.add(endingSymbol);
					temp = temp.substring(0, temp.length() - 1);
				} else if (temp.endsWith("?")) {
					endingSymbolsCount++;
					endingSymbol = "?"; 
					firstPass.add(endingSymbol);
					temp = temp.substring(0, temp.length() - 1);
				} else if (temp.endsWith(")")) {
					endingSymbolsCount++;
					endingSymbol = ")"; 
					firstPass.add(endingSymbol);
					temp = temp.substring(0, temp.length() - 1);
				}

				//temp = temp.substring(0, temp.length() - 1);
			}
			
			//Problem is that if there are different ending symbols it will save the right amount of
			//ending symbols but it will make them all the same symbol. **NEED TO FIX THIS
			firstPass.add(temp);
			for (int i = 0; i < endingSymbolsCount; i++) { 
				//firstPass.add(endingSymbol);
			}
		}

		System.out.println("First Pass: " + firstPass);
		// ************************************************************************//

		// ******************CHECKS FOR NUMBERS WITH +-****************************//
		ArrayList<String> secondPass = new ArrayList<String>();
		// 3: Numbers stay together. Can start with "+" or "-". Can have any number of
		// digits, commas and periods.
		// Must end in a digit
		for (int i = 0; i < firstPass.size(); i++) {
			String temp = firstPass.get(i);
			if (Pattern.matches("[$(]{0,}[+-]{0,}\\d+[,.]+\\d+[.,?!;:%)]{0,}", temp)) {
				while (temp.startsWith("$") || temp.startsWith("(")) {
					if (temp.startsWith("$")) {
						secondPass.add("$");
					} else if (temp.startsWith("(")) {
						secondPass.add("(");
					}
					temp = temp.substring(1);
				}

				int endingSymbols = 0;
				String[] endPunct = { ".", ",", "?", ":", ";", ")", "%", "!" };
				for (int e = 0; e < endPunct.length; e++) {
					if (temp.endsWith(endPunct[e])) {
						endingSymbols++;
						temp = temp.substring(0, temp.length() - 1);
						secondPass.add(temp);
					}

					for (int s = 0; s < endingSymbols; s++) {
						secondPass.add(endPunct[e]);
					}
					endingSymbols = 0;
				}
			}
			secondPass.add(temp);

		}
		System.out.println("Second Pass: " + secondPass);

		// *********************************************************************//

		// *********************CHECKS FOR ABBREVIATIONS************************//

		// 4:Check for a single letter followed by a period, if the period is followed
		// by another single letter and period
		// then this will be counted as an abbreviation and should be checked until
		// there is no more single letters and periods following
		// Once the end of the abbreviation is found you will go through and remove all
		// the periods
		// if not an abbreviation separate these into individual tokens.

		ArrayList<String> thirdPass = new ArrayList<String>();
		// I.B.M. <-- for reference when looking at the for loop and thinking in terms
		// of index
		for (int i = 0; i < secondPass.size(); i++) {
			int k = i; // This will be to check the next position in the token
			while (k + 1 < secondPass.size() && secondPass.get(k).length() == 1 && secondPass.get(k + 1).equals(".")) {
				k += 2;
			}
			if (k - i > 2) {
				// making an empty string that we will add our abbreviation to
				String abbrevTokens = "";
				for (int j = i; j < k; j += 2) {
					abbrevTokens += secondPass.get(j);
				}

				thirdPass.add(abbrevTokens);

				i = k - 1;
			} else {
				thirdPass.add(secondPass.get(i));
			}
		}

		System.out.println("Third Pass: " + thirdPass);

		// **************************************************************************//

		// ********************CHECKS FOR ANY OTHER SYMBOLS**************************//
		// 5: These characters ``. , ? : ; " ` ( ) % $" should be treated as separate
		// tokens
		ArrayList<String> fourthPass = new ArrayList<String>();
		// String[] punct = { ".", ",", "?", ":", ";", "(", ")", "%", "$", "!" };

		for (int i = 0; i < thirdPass.size(); i++) {
			String temp = thirdPass.get(i);
			if (Pattern.matches("[(]{0,}[a-zA-Z]+[?!;:%)-]{0,}", temp)) {
				while (temp.startsWith("(")) {
					if (temp.startsWith("(")) {
						fourthPass.add("(");
					}
					temp = temp.substring(1);
				}

				int endingSymbols = 0;
				String[] endPunct = { "?", ":", ";", ")", "%", "!" };
				for (int e = 0; e < endPunct.length; e++) {
					if (temp.endsWith(endPunct[e])) {
						endingSymbols++;
						temp = temp.substring(0, temp.length() - 1);
						fourthPass.add(temp);
					}
					// fourthPass.add(temp);
					for (int s = 0; s < endingSymbols; s++) {
						fourthPass.add(endPunct[e]);
					}
					endingSymbols = 0;
				}
			}
			fourthPass.add(temp);

		}

		System.out.println("Fourth Pass: " + fourthPass);

		// **************************************************************************//
		return fourthPass;
	}

	/**
	 * This is just here to help you test some examples. You may remove it if you
	 * want, but I encourage you to write similar tests.
	 */
	public void test() {
		String test = "The N.Y.S.E. is up $10,000 or 1%.";
		ArrayList<String> answer = new ArrayList<String>();
		answer.add("The");
		answer.add("NYSE");
		answer.add("is");
		answer.add("up");
		answer.add("$");
		answer.add("10,000");
		answer.add("or");
		answer.add("1");
		answer.add("%");
		answer.add(".");
		runTest(test, answer);

		test = "1,000,000.00";
		answer = new ArrayList<String>();
		answer.add(test);
		runTest(test, answer);

		test = "0.1234";
		answer = new ArrayList<String>();
		answer.add(test);
		runTest(test, answer);
	}

	/**
	 * Test helper. Tokenizes the test string and compares the result to the answer.
	 * Outputs pass or fail.
	 * 
	 * @param test
	 *            test string
	 * @param answer
	 *            the correct tokenization of test
	 */
	private void runTest(String test, ArrayList<String> answer) {
		// tokenize the test string
		ArrayList<String> result = tokenize(test);

		if (!checkResult(result, answer)) {
			System.out.println("Failed: " + test);

			for (String s : result) {
				System.out.print(s + " ");
				System.out.println();
			}
		} else {
			System.out.println("Passed: " + test);
		}
	}

	/**
	 * Compares two ArrayLists of strings to see if they're the same.
	 * 
	 * @param result
	 * @param answer
	 * @return true if the ArrayLists are the same, false otherwise
	 */
	private boolean checkResult(ArrayList<String> result, ArrayList<String> answer) {
		if (result.size() != answer.size()) {
			return false;
		} else {
			for (int i = 0; i < result.size(); i++) {
				if (!result.get(i).equals(answer.get(i))) {
					return false;
				}
			}

			return true;
		}
	}
}
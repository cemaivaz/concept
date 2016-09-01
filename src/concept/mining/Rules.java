/**
 * 
 */
package concept.mining;

import java.util.Arrays;
import java.util.List;

/**
 * @author cemri
 *
 */
public class Rules {

	/**
	 * @param args
	 */
	private String word;
	private List<String> meaning;

	public Rules(String word, List<String> meaning) {
		this.word = word;
		this.meaning = meaning;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public String hypernym() {
		int len = meaning.size();
		String[] lastBlock = meaning.get(len - 1).split(" ");
		List<String> lastBlockLst = Arrays.asList(lastBlock);
		String lastWord = lastBlock[lastBlock.length - 1];

		//Rule 1: Last block is merely one word
		if (len == 1 && lastBlock.length == 1) {
			return new Rules(meaning.get(0), meaning).hypernym();
		}
		//Rule 2: In parens, however, not a latin word
		if (lastWord.contains(Constants.Paren)) {
			if (!lastWord.contains(Constants.Unknown)) {
				return lastWord.substring(0, lastWord.indexOf(Constants.Paren));
			}
		}
		for (int i = len - 1; i >= 0; i--) {
			
		}
		return ""; //null döndürebilir, take it into account
	}
}

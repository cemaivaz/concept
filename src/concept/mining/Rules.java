/**
 * 
 */
package concept.mining;

import java.util.ArrayList;
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

	public int spaceCnt(String s) {
		return s.replaceAll("[^ ]", "").length();
	}
	public String hypernym() {
		int len = meaning.size();
		String lastBlockStr = " " + meaning.get(len - 1);
		List<String> lastBlock = Arrays.asList(meaning.get(len - 1).split(" "));
		String lastWord = lastBlock.get(lastBlock.size() - 1);

		int lastBlSize = lastBlock.size();
		

		//Rule 1: Last block and the whole meaning are merely one word
		if (len == 1 && lastBlSize == 1) {
			return new Rules(meaning.get(0), meaning).hypernym();
		}

		//Rule 2: In parens, however, not a latin word
		if (lastWord.contains(Constants.Paren)) {
			if (!lastWord.contains(Constants.Unknown)) {
				return lastWord.substring(0, lastWord.indexOf(Constants.Paren));
			} else {
				lastBlock = lastBlock.subList(0, lastBlSize--);
				lastWord = lastBlock.get(lastBlSize - 1);
				lastBlockStr = lastBlockStr.substring(0, lastBlock.lastIndexOf(" "));
			}
		}

		//Rule 3: A lengthy last block
		if (lastBlock.size() >= 2) {
			
			if (lastWord.contains(Constants.NOM)) {
				return lastWord.substring(0, lastWord.indexOf("["));
			} 

		}

		//Rule 4: Last block contains "bir"
		if (lastBlock.size() >= 2) {
			int birInd = -1;
			List<String> birRemain = new ArrayList<String>();
			for (int i = lastBlock.size() - 1; i >= 0; i--) {
				if (lastBlock.get(i).substring(0, 4).equals("bir[")) {
					birRemain = lastBlock.subList(i + 1, lastBlock.size());
					birInd = i;
					break;
				}
			}
			if (birRemain.size() == 2) {
				String firstBirRemain = birRemain.get(0);
				String secBirRemain = birRemain.get(1);
				
				//orman ağaç -> DÜZELT!
				return firstBirRemain.substring(0, firstBirRemain.indexOf("[")) + " "
						+ secBirRemain.substring(0, secBirRemain.indexOf("["));
			}
//			String penUltWord = lastBlock.get(lastBlock.size() - 2);
//			String barePenUlt = penUltWord.substring(0, penUltWord.indexOf("["));
//			if (lastBlockStr.contains(Constants.bir)) {
//				int birInd = lastBlockStr.lastIndexOf(Constants.bir);
//				if (barePenUlt.equals("bir") && )
//
//			}
		}
		for (int i = len - 1; i >= 0; i--) {

		}
		return ""; //null döndürebilir, take it into account
	}
}

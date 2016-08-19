package concept.mining;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistical {

	private boolean normalized = true;
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public Map<String, Integer> freq(List<String> l) {
		
		Map<String, Integer> fr = new HashMap<String, Integer>();
		for (int i = 0; i < l.size(); i++) {
			String word = l.get(i);
			int cnt = fr.containsKey(word) ? fr.get(word): 0;
			fr.put(word, ++cnt);
		}
		return fr;
	}
	public Map<String, Double> scope(List<String> l) {
		Map<String, Double> sc = new HashMap<String, Double>();
		int size = l.size();
		Map<String, List<Integer>> tmpSc = new HashMap<String, List<Integer>>();
		for (int i = 0; i < l.size(); i++) {
			String word = l.get(i);
			List<Integer> firstLast = tmpSc.containsKey(word) ? tmpSc.get(word): 
				new ArrayList<Integer>();
			int flSize = firstLast.size();
			if (flSize < 2) {
				firstLast.add(i);
			} else {
				firstLast.set(1, i);
			}
			tmpSc.put(word, firstLast);
		}
		for (Map.Entry<String, List<Integer>> me: tmpSc.entrySet()) {
			List<Integer> lox = me.getValue();
			sc.put(me.getKey(), 1 / (1 + Math.log(((double) size / (double) (lox.get(lox.size() - 1) - lox.get(0) + 1)))));
		}
		return sc;
	}
	
	public Map<String, Double> firstLoc(List<String> l) {
		int size = l.size();
		Map<String, Double> firstL = new HashMap<String, Double>();
		for (int i = 0; i < l.size(); i++) {
			String word = l.get(i);
			if (!firstL.containsKey(word))
				firstL.put(word, 1 / (Math.log((double) i / (normalized ? size: 1) + 1) + 1));
		}
		return firstL;
	}
}

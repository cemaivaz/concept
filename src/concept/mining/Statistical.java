package concept.mining;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Statistical {

	private boolean normalized = true;
	
	private double[] coeffHier = new double[]{1, 1.2, 0.8}; //Perceptron training
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sort(Map<K, V> m, boolean asc) {
		List<Map.Entry<K, V>> tmpList = new ArrayList<Map.Entry<K, V>>(m.entrySet());

		Map<K, V> tmp = new LinkedHashMap<K, V>();
		Collections.sort(tmpList, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> m1, Map.Entry<K, V> m2) {
				return ((asc == true) ? 1 : -1) * m1.getValue().compareTo(m2.getValue());
			}
		});
		for (Map.Entry<K, V> me: tmpList) {
			tmp.put(me.getKey(), me.getValue());
		}
		return tmp;
	}
	
	public Map<String, Double> hierarch(List<String> l) {
		Map<String, Double> hierScore = new HashMap<String, Double>();
		
		Map<String, Double> wordScore = combine(l);
		
		for (String s: l) {
			double sc1 = wordScore.get(s);
			double hSc1 = hierScore.containsKey(s) ? hierScore.get(s): 0;
			hSc1 += sc1 * coeffHier[0];
			hierScore.put(s, hSc1);
			
			for (int i = 0; i < coeffHier.length - 1; i++) {
				String hyp = new Rules().hypernym(s);
				
				if (hyp == null) {
					break;
				}
				
				double tmpSc = sc1 * coeffHier[i + 1];
				double hScTmp = hierScore.containsKey(hyp) ? hierScore.get(hyp): 0;
				hScTmp += tmpSc;
				hierScore.put(hyp, hScTmp);
				
				s = hyp;
			}
			
		}
		
		return hierScore;
	}
	public Map<String, Double> combine(List<String> l) {
		Map<String, Double> score = new HashMap<String, Double>();
		
		Map<String, Integer> fr = freq(l);
		Map<String, Double> fL = firstLoc(l);
		Map<String, Double> sc = scope(l);
		
		for (String word: l) {
			score.put(word, (double) fr.get(word) * fL.get(word) * sc.get(word));
		}
		
		return score;
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

package concept.mining;
//UNSUPERVISED'ı yapacaktın ! ! !
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import net.zemberek.erisim.Zemberek;
import net.zemberek.tr.yapi.TurkiyeTurkcesi;
import net.zemberek.yapi.Kelime;



/**
 * 
 * @author cemri
 *
 */
public class Meanings {
	
	private Map<String, Integer> mostFreq = new HashMap<String, Integer>();

	private String alpha = "a-zA-Z";
	private String turkSpecific = "ğüşıöçĞÜŞİÖÇîâûÎÂÛ";
	private String allChars = alpha + turkSpecific;
	
	private String nonTurkCh = "[^" + allChars + " ]";

	private String splitter = ",";
	
	private String POS = "isim";
	private String specWord = "\'da";
	
	private int contextSize = 30;
	
	public static Zemberek z = new Zemberek(new TurkiyeTurkcesi());
	
	private String rootSplStr = ": ";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

//		System.out.println("î".toUpperCase());
//		new Meanings().readFile();
		
//		System.out.println(Arrays.toString(z.asciiCozumle("yengeç")));
//
		System.out.println(new Meanings().mostFreq("C:\\Users\\cemri\\Downloads\\Turkce TDK Sozluk\\Turkce TDK Sozluk\\Sozluk", 10));
		
		Map<String, List<String>> kk = new HashMap<>();
		List<String> l = Arrays.asList("cem", "alin", "seyit");
		kk.put("c", l);
		List<String> l2 = Arrays.asList("christina", "john");
		kk.put("j", l2);
		Collection<List<String>> cc = kk.values();
		System.out.println(cc);
		
//		System.out.println(new Meanings().wordTokenizer("Merhaba, bugün burada olduğum için çok mutlu; hatta "
//				+ "Dünya'nın (bu Dünya'da) en mutlu insanıyım diyebilirim (happy)."));
		Kelime[] k = z.asciiCozumle("birinden");
		System.out.println(Arrays.toString(k));
		
	}
	public String upperOrLowerTurkChars(boolean upper) {
		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < turkSpecific.length(); i++) {
			char c = turkSpecific.charAt(i);
			if (upper ? Character.isUpperCase(c) : Character.isLowerCase(c))
				sb.append(c);
		}
		return new String(sb);
	}
	public List<String> wordTokenizer(String s) {
		s = s.toLowerCase().replaceAll("[\\(\\)\\.,;:!\\?\\'\"]", "");
		s = s.replaceAll("([" + allChars + "])\'[" + allChars + "]+", "$1");
		return Arrays.asList(s.split(" "));
	}

	public List<String> tokenizer(String s) {
		s = s.replaceAll("([" + allChars + "])\'[" + allChars + "]+", "$1");
		s = s.replaceAll("([\\.&&([^(A-Z" + upperOrLowerTurkChars(true) + "[a-z" + upperOrLowerTurkChars(false) + "])]\\.)]:;!\\?\\))", " $1");
		s = s.replaceAll("(\\()", "$1 ");
		return Arrays.asList(s.split(splitter));
	}
//	public List<String> parser(List<String> l) {
//		for (String block: l) {
//			String[] arr = block.split(" ");
//			for (int i = 0; i < arr.length; i++) {
//				Kelime[] k = z.asciiCozumle(arr[i]);
//				System.out.println(Arrays.toString(k));
//			}
//			
//		}
//		
//	}
	
	
	public void readFile() {
		Map<String, List<String>> m = extractMeanings("C:\\Users\\cemri\\Downloads\\Turkce TDK Sozluk\\Turkce TDK Sozluk\\Sozluk");
		
		Scanner inp = new Scanner(System.in);
		while (true) {
			
			String line = inp.nextLine();
			if (line.equalsIgnoreCase("exit"))
				break;
			if (!m.containsKey(line)) {
				System.out.println("Sorry, the word (" + POS + ") you searched for has not been found in the dictionary!");
				continue;
			}
			int i = 1;
			for (String s: m.get(line)) {
				System.out.println((m.get(line).size() > 1 ? (i++ + ": ") : "") + s.trim());
			}
		}
		inp.close();
		System.exit(1);
	}
	public List<String> disamb(List<List<String>> means, int i, List<String> doc) {
		if (means.size() == 0) {
			return means.get(0);
		}
		int min = Math.max(0, i - contextSize / 2);
		int max = Math.min(doc.size(), i + contextSize / 2);
		
		List<String> subL = doc.subList(min, max);

		double maxMeanVal = Double.MIN_VALUE;
		List<String> maxMean = means.get(0);
		
		for (List<String> mean: means) {
			List<String> common = new ArrayList<String>(subL);
			common.retainAll(mean);
			double commonVal = (double) common.size() / mean.size();
			if (commonVal > maxMeanVal) {
				maxMeanVal = commonVal;
				maxMean = mean;
			}
		}
		
		return maxMean;
	}

	public List<String> mostFreq(String fileName, int perc) {
		if (perc <= 0 || perc >= 100)
			throw new IllegalArgumentException();
		Collection<List<String>> allMeans = extractMeanings(fileName).values();
		
		for (List<String> l: allMeans) {
			for (String s: l) {
				for (String word: wordTokenizer(s)) {
					String rootNotSplt = Arrays.toString(z.asciiCozumle(word)).split(",")[0];
					String root = rootNotSplt.substring(rootNotSplt.indexOf(rootSplStr) + rootSplStr.length(), rootNotSplt.length());
					root = root.trim();
					int cnt = mostFreq.containsKey(root)? mostFreq.get(root): 0;
					mostFreq.put(root, ++cnt);
				}
			}
		}
		List<String> mostFreqWords = new ArrayList<String>(Statistical.sort(mostFreq, false).keySet());
		
		return mostFreqWords.subList(0, mostFreqWords.size() * perc / 100);//, mostFreqWords.size());
	}
	

	
	public Map<String, List<String>> extractMeanings(String fileName) {
		File dir = new File(fileName);

		List<String> meanings = new ArrayList<String>();
		Map<String, List<String>> allMeanings = new HashMap<String, List<String>>();
		for (File subDir: dir.listFiles()) {
			System.out.println(subDir.getAbsolutePath());
			if (subDir.isDirectory())
				for (File xmls: subDir.listFiles()) {
					if (xmls.getName().contains("HARF")) {
						try {
							BufferedReader br = new BufferedReader(new FileReader(xmls));
							String line = "";
							String vocWord = "";
							boolean notNoun = false;
							while ((line = br.readLine()) != null) {
								if (line.contains("<name>")) {
									vocWord = line.substring(line.indexOf("<name>") + ("<name>").length(), 
											line.indexOf("</name>"));
									if (vocWord.contains("(I"))
										vocWord = vocWord.substring(0, vocWord.indexOf("("));
									vocWord = vocWord.trim().replaceAll(nonTurkCh, "");

								}
								if (line.contains("<lex_class>")) {
									String POS = line.substring(line.indexOf("<lex_class>") + "<lex_class>".length(), 
											line.indexOf("</lex_class>"));
									if (!POS.contains(POS)) {
										notNoun = true;
										continue;
									} else
										notNoun = false;
								}
								if (notNoun)
									continue;
								if (line.contains("meaning_text")) {
									if (line.contains(specWord))
									{
										System.out.println(vocWord + " - " + line.replaceAll("<(/)?meaning_text>", ""));
									}
									String meaning = line.substring(line.indexOf(">") + 1, line.lastIndexOf("<"));
									meanings.add(meaning);

								}
								if (line.contains("</entry>")) {
									allMeanings.put(vocWord, meanings);
									meanings = new ArrayList<String>();

									vocWord = "";
								}
							}
							br.close();
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
		}
		return allMeanings;
	}
}

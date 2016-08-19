package concept.mining;
//UNSUPERVISED'ı yapacaktın ! ! !
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;



/**
 * 
 * @author cemri
 *
 */
public class Meanings {
	private static String turkishCh = "[^a-zA-ZğüşıöçĞÜŞİÖÇîâûÎÂÛ ]";

	private static String POS = "isim";
	private static String specWord = "ortak adı";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		new Meanings().readFile();
		

	}

	public void readFile() {
		Map<String, Set<String>> m = extractMeanings("C:\\Users\\cemri\\Downloads\\Turkce TDK Sozluk\\Turkce TDK Sozluk\\Sozluk");
		
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

	public Map<String, Set<String>> extractMeanings(String fileName) {
		File dir = new File(fileName);

		Set<String> meanings = new LinkedHashSet<String>();
		Map<String, Set<String>> allMeanings = new HashMap<String, Set<String>>();
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
									vocWord = vocWord.trim().replaceAll(turkishCh, "");

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
									meanings = new LinkedHashSet<String>();

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

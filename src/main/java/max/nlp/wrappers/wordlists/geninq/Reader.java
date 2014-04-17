package max.nlp.wrappers.wordlists.geninq;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Reader {

	private static final String FILE = "/home/max/resources/wordlists/inqtabs.txt";
	private static Reader self;

	private static Map<Integer, String> columnKeys = new HashMap<Integer, String>();

	private static Map<String, HashMap<String,String>> words = new HashMap<String, HashMap<String,String>>();
	private Reader() {
		loadWords();
	}
	
	public void print(){
		try {
			PrintWriter w = new PrintWriter(new FileWriter(new File("/home/max/haravrdinq.txt")));
			for (Entry<String, HashMap<String, String>> e : words.entrySet()){
				for( Object  a:  e.getValue().values().toArray()){
					if(!a.equals("Othtags") && !a.equals("Source"))
					w.println(e.getKey() +"," + "" + a);
				}
			}
			w.flush();
			w.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Reader getInstance() {
		if (self == null)
			self = new Reader();
		return self;
	}
	
	public Map<String,String>getWordAttributes(String word){
		return words.get(word);
	}

	private static void loadWords() {
		try {
			BufferedReader b = new BufferedReader(
					new FileReader(new File(FILE)));
			String line = "";
			boolean parsedValues = false;
			while ((line = b.readLine()) != null) {
				if (!parsedValues) {
					String[] values = line.split("\t");
					for (int i = 0; i < values.length; i++) {
						String value = values[i];
						columnKeys.put(i, value);
					}
					parsedValues = true;
				} else {
					HashMap<String,String> attributesForWord = new HashMap<String,String>();
					String[] values = line.split("\t");
					String word = values[0];
					for (int i = 1; i < values.length; i++) {
						String value = values[i];
						if (!value.isEmpty()){
							attributesForWord.put(value, columnKeys.get(i));
						}
					}
					words.put(word, attributesForWord);
					
				}

			}
			b.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		Reader r = Reader.getInstance();
		r.print();
		Map<String, String> att = r.getWordAttributes("ABANDONMENT");
		for (Entry<String, String>  e: att.entrySet()) 
			System.out.println(e);
	}
}

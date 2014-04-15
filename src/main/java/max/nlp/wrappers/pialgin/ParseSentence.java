package max.nlp.wrappers.pialgin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ParseSentence {

	public static void main(String[] args) {
		ParseSentence s = new ParseSentence();
		s.parse("/home/max/pialign-0.2.4/out/align.1.samp");
	}

	public void parse(String inputFile) {
		try {
			BufferedReader b = new BufferedReader(new FileReader(new File(
					inputFile)));
			String line = "";
			while ((line = b.readLine()) != null) {
				parseLine(line.replaceAll(" ", ""));
				System.exit(0);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void parseLine(String line) {
		if (line.startsWith("[")) {
			parseLine(line.substring(1));
		} if (line.startsWith("(((")) {
			parseLine(line.substring(1));
		}

	}
}

package max.nlp.wrappers.berkeley;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import max.nlp.dal.generic.GenericDB;
import max.nlp.dal.wiktionary.types.Text;
import max.nlp.dal.wiktionary.types.Translation;

import org.apache.log4j.Logger;

/**
 * @author max.kaufmann
 *
 */
/**
 * @author max.kaufmann
 * 
 */
public class BerkeleyAligner {

	private static final double THRESHOLD = .01;
	private final String L1 = "e";
	private final String L2 = "f";
	private String CONFIG_FILE ;
	private String EXEC_DIR;
	private String TRAIN_DIR;

	public void setConfig(String cONFIG_FILE) {
		CONFIG_FILE = cONFIG_FILE;
	}

	public void setExecDir(String eXEC_DIR) {
		EXEC_DIR = eXEC_DIR;
	}

	public void setTrainDir(String tRAIN_DIR) {
		TRAIN_DIR = tRAIN_DIR;
	}

	public static void setLog(Logger log) {
		BerkeleyAligner.log = log;
	}

	static Logger log = Logger.getLogger(BerkeleyAligner.class);

	public void generateConfigFile() {
		try {
			File out = new File(CONFIG_FILE);
			PrintWriter configOutput = new PrintWriter(new FileWriter(out));
			configOutput.println("forwardModels	MODEL1 HMM");
			configOutput.println("reverseModels	MODEL1 HMM");
			configOutput.println("mode	JOINT JOINT");
			configOutput.println("iters	2 2");
			configOutput.println("execDir   " + EXEC_DIR);
			configOutput.println("create    true");
			configOutput.println("overwriteExecDir  true");
			configOutput.println("saveParams        true");
			configOutput.println("numThreads        3");
			configOutput.println("testSources       " + "");

			configOutput.println("msPerLine 10000");
			configOutput.println("alignTraining");
			// configOutput.println("leaveTrainingOnDisk");
			configOutput.println("foreignSuffix	" + L1);
			configOutput.println("englishSuffix	" + L2);
			configOutput.println("lowercase");
			configOutput.println("trainSources      " + TRAIN_DIR);
			configOutput.println("sentences MAX");
			configOutput.println("maxTestSentences  500");
			configOutput.println("offsetTestSentences       0");
			configOutput.println("saveLexicalWeights");
			configOutput.println("competitiveThresholding");
			configOutput.println("dictionary");
			configOutput.println("aligntraining");
			// configOutput.println("allowedprecisionconflicts 8");
			// configOutput.println("inferenceModeC     VITERBI_ITG");
			// configOutput.println("objMode    NONE");
			// configOutput.println("pruneThresh       1e-4");
			// configOutput.println("prunePredict      true");
			configOutput.flush();
			configOutput.close();
		} catch (IOException e) {
			System.out.println("Can't read config file");
			System.out.println(e.getMessage());
		}
	}

	public void generateWordAlignments() {
		if (!new File(CONFIG_FILE).exists()) {
			generateConfigFile();
			log.info("Config file [" + CONFIG_FILE + "] already exists");
		} else {
			log.info("Generating config file at [" + CONFIG_FILE + "]");
		}
		log.info("Training aligner ");
		String[] args = new String[1];
		args[0] = "++" + CONFIG_FILE;
		edu.berkeley.nlp.wordAlignment.Main.main(args);

	}

	public List<Translation> getTranslations() {
		String L1lexweightFile = EXEC_DIR + "1.lexweights";
		String L2lexweightFile = EXEC_DIR + "2.lexweights";

		ArrayList<Translation> translations = new ArrayList<Translation>();
		translations.addAll(readTranslationsFromLexweightsFile(L1lexweightFile,
				L1));
		translations.addAll(readTranslationsFromLexweightsFile(L2lexweightFile,
				L2));
		return translations;
	}

	/**
	 * Puts all translations above THRESHOLD into mongodb Assumes that there are
	 * only 2 languages in EXEC_DIr, and that the translations are in
	 * 1.lexweights and 2.lexWeights
	 */
	public void loadWordAlignmentsIntoMongo() {
		String L1lexweightFile = EXEC_DIR + "1.lexweights";
		String L2lexweightFile = EXEC_DIR + "2.lexweights";

		ArrayList<Translation> translations = new ArrayList<Translation>();
		translations.addAll(readTranslationsFromLexweightsFile(L1lexweightFile,
				L1));
		translations.addAll(readTranslationsFromLexweightsFile(L2lexweightFile,
				L2));

		GenericDB db = GenericDB.getInstance();
		for (Translation t : translations) {
			db.saveEntry(t);
		}

	}

	public ArrayList<Translation> readTranslationsFromLexweightsFile(
			String file, String sourceLanguage) {
		ArrayList<Translation> translations = new ArrayList<Translation>();

		String targetLanguage = "";
		if (sourceLanguage.equals(L1))
			targetLanguage = L2;
		else
			targetLanguage = L1;

		try {
			BufferedReader wordFile = new BufferedReader(new FileReader(file));
			String line;
			String sourceWord = "";
			double entropy = -1;
			int lineNum = 0;
			while ((line = wordFile.readLine()) != null) {
				lineNum++;
				String[] temp = line.split("\t");
				if (line.contains("nTrans")) {
					sourceWord = temp[0];
					entropy = Double.parseDouble(temp[1].split(" ")[1]);
				} else {
					try {
						
						int delim = line.lastIndexOf(":");
						if (delim == -1)
							System.out.println(lineNum+ " " + line);
						String targetWord = line.substring(0, delim);
						double translationProb =Double.parseDouble(line.substring(delim + 1, line.length() -1));
						
						if (translationProb >= THRESHOLD) {
							Text w1 = new Text(sourceWord, sourceLanguage);
							Text w2 = new Text(targetWord, targetLanguage);
							Translation t = new Translation(w1, w2);
							t.setProb(translationProb);
							if (entropy != -1) {
								t.setEntropy(entropy);
							}
							translations.add(t);
						}
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
			}
			wordFile.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return translations;
	}

	/**
	 * Example usage
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		BerkeleyAligner b = new BerkeleyAligner();
		b.setConfig("/home/max/resources/berkeleyaligner/example_confs/test.conf");
		b.setExecDir("/home/max/resources/berkeleyaligner/output_encs");
		b.setTrainDir("/home/max/resources/berkeleyaligner/example_datasets/encs");
		// generate a config file and word alignments
		b.generateConfigFile();
		b.generateWordAlignments();

		// ArrayList<LineAlignment> a = b.extractAlignments();
		// Integer[] al = { 0, 1, 2 };
		// List<String> r = LineAlignment.alignmentsAsWords(true, a.get(64),
		// al);
		// System.out.println(r);

		// load them into mongo. you can also just get them into memory with
		// readTranslationsFromLexweightsFile
		b.loadWordAlignmentsIntoMongo();

	}

	public ArrayList<LineAlignment> extractAlignments() {
		String trainFile = EXEC_DIR + "/training.align";
		String e = EXEC_DIR + "/training.e";
		String f = EXEC_DIR + "/training.f";

		ArrayList<LineAlignment> allAlignments = new ArrayList<LineAlignment>();
		try {
			BufferedReader alignmentReader = new BufferedReader(new FileReader(
					new File(trainFile)));
			BufferedReader eReader = new BufferedReader(new FileReader(
					new File(e)));
			BufferedReader fReader = new BufferedReader(new FileReader(
					new File(f)));
			String alignmentLine;
			int line = 0;
			while ((alignmentLine = alignmentReader.readLine()) != null) {
				LineAlignment lineAlignment = new LineAlignment();
				String[] eWords = eReader.readLine().split(" ");
				String[] fWords = fReader.readLine().split(" ");
				lineAlignment.seteWords(eWords);
				lineAlignment.setfWords(fWords);

				String[] alignments = alignmentLine.split(" ");
				for (String alignment : alignments) {
					WordAlignment wordAlignment = new WordAlignment();

					String[] parts = alignment.split("-");
					int eIndex = Integer.parseInt(parts[0]);
					int fIndex = Integer.parseInt(parts[1]);
					wordAlignment.seteIndex(eIndex);
					wordAlignment.setfIndex(fIndex);
					wordAlignment.seteWord(eWords[eIndex]);
					wordAlignment.setfWord(fWords[fIndex]);
					lineAlignment.addWordAlignment(wordAlignment);
				}
				lineAlignment.setLineNumber(line);
				allAlignments.add(lineAlignment);
				line++;
			}
			
			alignmentReader.close();
			eReader.close();
			fReader.close();

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}

		return allAlignments;
	}
}

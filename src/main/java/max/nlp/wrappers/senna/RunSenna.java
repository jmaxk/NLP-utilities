package max.nlp.wrappers.senna;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import max.nlp.dal.types.statistics.TaggedText;

public class RunSenna {
	File sennaInstallationDir = new File("/home/max/resources/senna/");
	String[] lineArr;

	public String getSennaOutput(String line, String options) {
		try {
			String cmd = "echo " + line + " | " + sennaInstallationDir
					+ "/senna " + options;
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
			pb.directory(sennaInstallationDir);
			Process shell = pb.start();
			InputStream shellIn = shell.getInputStream();
			int shellExitStatus = shell.waitFor();
			int c;
			StringBuffer s = new StringBuffer();
			while ((c = shellIn.read()) != -1) {
				s.append((char) c);
			}
			return s.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public HashMap<String, SennaVerb> parseSRLResults(String allText,
			String sentence) {
		lineArr = allText.split("\n");
		HashMap<String, SennaVerb> verbsToArgs = new HashMap<String, SennaVerb>();
		ArrayList<SennaVerb> verbs = new ArrayList<SennaVerb>();
		int verbCount = 0;
		for (int i = 0; i < lineArr.length; i++) {
			// String[] line = lineArr[i].trim().split("\t");
			String line = lineArr[i].trim();
			lineArr[i] = line;

			if (!line.isEmpty()
					&& (!line.split("\\s+")[4].trim().equalsIgnoreCase("-"))) {
				// Verb v = getVerbArguments(++verbCount,
				// line.split("\\s+")[0].trim(), sentence);
				SennaVerb v = getVerbArgument(++verbCount,
						line.split("\\s+")[0].trim(), sentence);
				verbs.add(v);
				verbsToArgs.put(v.text, v);
			}
		}
		return verbsToArgs;
	}

	public SennaVerb getVerbArgument(int index, String verb, String sentence) {

		SennaVerb v = new SennaVerb();
		v.text = verb;
		HashMap<String, List<String>> argumentToText = new HashMap<String, List<String>>();
		HashMap<String, List<TaggedText>> argumentToTaggedText = new HashMap<String, List<TaggedText>>();

		index = index + 4;

		for (int i = 0; i < lineArr.length; i++) {
			String[] lineTokens = lineArr[i].trim().split("\\s+");
			String token = lineTokens[0].trim();
			String pos = lineTokens[1].trim();
			String value = lineTokens[index].trim();

			if (value.equals("O")) {
				continue;
			} else if (value.startsWith("S-") && !value.contains("S-V")) {
				String arg = value.split("S-")[1];
				List<TaggedText> taggedTokens = new ArrayList<TaggedText>();
				taggedTokens.add(new TaggedText(token, pos));
				argumentToTaggedText.put(arg, taggedTokens);
			} else if (value.startsWith("B-")) {

				String arg = value.split("B-")[1];
				List<TaggedText> taggedTokens = new ArrayList<TaggedText>();
				taggedTokens.add(new TaggedText(token, pos));
				while (!value.startsWith("E-")) {
					i++;
if (i  >= lineArr.length)
	break;
					lineTokens = lineArr[i].trim().split("\\s+");
					token = lineTokens[0].trim();
					pos = lineTokens[1].trim();
					value = lineTokens[index];

					taggedTokens.add(new TaggedText(token, pos));

					if (!argumentToText.containsKey(arg)) {
						argumentToTaggedText.put(arg, taggedTokens);
					}
				}

				if (value.startsWith("E-") && !argumentToText.containsKey(arg)) {
					argumentToTaggedText.put(arg, taggedTokens);

				}

				if (argumentToText.containsKey(arg))
					arg = arg + "-1";

			}
		}

		v.argumentToNPs = argumentToText;
		v.argumentToTagged = argumentToTaggedText;
		return v;

	}

	public HashMap<String, SennaVerb> parse(String sentence) {
		String srlResults = getSennaOutput(sentence, "");
		HashMap<String, SennaVerb> parsedLines = parseSRLResults(srlResults,
				sentence);
		return parsedLines;
	}

	public HashMap<String, List<String>> extractEntities(String sentence) {
		String nerResults = getSennaOutput(sentence, "-ner");
		HashMap<String, List<String>> parsedLines = parseNERResults(nerResults);
		return parsedLines;
	}
	
	
	public HashMap<String, List<String>> parseTree(String sentence) {
		String nerResults = getSennaOutput(sentence, "-psg");
		System.out.println(nerResults);
		HashMap<String, List<String>> parsedLines = parseNERResults(nerResults);
		return parsedLines;
	}

	public HashMap<String, List<String>> parseNERResults(String parsedText) {
		HashMap<String, List<String>> entities = new HashMap<String, List<String>>();
		String[] lines = parsedText.split("\\n");
		boolean started = false;
		StringBuilder sb = new StringBuilder();
		for (String line : lines) {
			String[] sections = line.split("\\t");
			String word = sections[0].trim();
			String entity = sections[1].trim();
			if (entity.equals("O")) {
				continue;
			} else {
				if (entity.startsWith("B")) {
					started = true;
					sb.append(word + " ");
				} else if (entity.startsWith("E-")) {
					sb.append(word + " ");
					String key = entity.split("-")[1];
					List<String> extractedEntities = entities.get(key);

					if (extractedEntities == null) {
						extractedEntities = new ArrayList<String>();
						extractedEntities.add(sb.toString().trim());
						entities.put(key, extractedEntities);
					} else {
						extractedEntities.add(sb.toString().trim());

					}
					sb = new StringBuilder();
					started = false;
				} else if (started) {
					sb.append(word + " ");
				}

			}
		}
		return entities;
	}

	public static void main(String[] args) {
		RunSenna r = new RunSenna();
		
		
		r.parseTree("He was the editor of the paper");
//		HashMap<String, SennaVerb> verbs = r
//				.parse("As if the hostile takeover weren't enough, to add insult.");

//		for (Entry<String, SennaVerb> e : verbs.entrySet()) {
//			System.out.println(e.getKey());
//			HashMap<String, List<TaggedText>> tagged = e.getValue().argumentToTagged;
//			for (Entry<String, List<TaggedText>> g : tagged.entrySet())
//				System.out.println(g);
//		}
		// HashMap<String, List<String>> results = r
		// .extractEntities("I went to the Golden Gate Bridge with Bill Gates ");
		// for (Entry<String, List<String>> e : results.entrySet()) {
		// System.out.println(e);
		// }
	}
}

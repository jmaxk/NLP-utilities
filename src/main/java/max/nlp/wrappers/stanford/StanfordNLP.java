package max.nlp.wrappers.stanford;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

public class StanfordNLP {

	private StanfordCoreNLP pipeline;

	private static Map<String, StanfordNLP> instances = new HashMap<String, StanfordNLP>();
	private static final String defaultAnnotators = "tokenize, ssplit, pos, lemma";

	private StanfordNLP() {
		Properties props = new Properties();
		props.put("annotators", defaultAnnotators);
		pipeline = new StanfordCoreNLP(props);
	}

	private StanfordNLP(String annotators) {
		Properties props = new Properties();
		props.put("annotators", annotators);
		pipeline = new StanfordCoreNLP(props);
	}

	public static StanfordNLP getInstance() {
		if (instances.isEmpty()) {
			instances
					.put(defaultAnnotators, new StanfordNLP(defaultAnnotators));
		}
		return instances.get(defaultAnnotators);
	}

	public static StanfordNLP getInstance(String annotators) {
		instances.put(annotators, new StanfordNLP(annotators));
		return instances.get(annotators);
	}

	/**
	 * @param text
	 * @return a list of sentences
	 */
	public List<CoreMap> annotate(String text) {
		Annotation document = new Annotation(text);
		pipeline.annotate(document);
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		return sentences;
	}

	public String getParseTreeAsString(CoreMap sentence) {
		Tree tree = sentence.get(TreeAnnotation.class);
		return tree.toString();
	}

	public List<String> getTrees(List<String> sentences) {
		List<CoreMap> annotatedSentences = annotate("He goes to the store");
		List<String> sentenceTrees = new ArrayList<String>();
		for (CoreMap sentence : annotatedSentences) {
			sentenceTrees.add(getParseTreeAsString(sentence));
		}
		return sentenceTrees;

	}

	public String getTree(String sentence) {
		List<CoreMap> annotatedSentences = annotate(sentence);
		return getParseTreeAsString(annotatedSentences.get(0));

	}

	public List<String> convertTextToTokens(String text) {
		List<CoreMap> annotatedText = annotate(text);
		List<String> words = new ArrayList<String>();
		for (CoreMap sentence : annotatedText) {
			LinkedHashMap<String, String> tokenizedSentence = extractTaggedTokensFromLabeledSentence(
					"tokenization", sentence);
			for (Entry<String, String> e : tokenizedSentence.entrySet()) {
				String word = e.getKey();
				words.add(word);
			}
		}
		return words;
	}

	public List<String> convertTextToSentence(String text) {
		List<CoreMap> annotatedText = annotate(text);
		List<String> sentences = new ArrayList<String>();
		for (CoreMap sentence : annotatedText) {

			// StringBuilder sentenceBuilder = new StringBuilder();
			// LinkedHashMap<String, String> tokenizedSentence =
			// extractAnnotationFromLabeledSentence(
			// "tokenization", sentence);
			// for (Entry<String, String> e : tokenizedSentence.entrySet()) {
			// String word = e.getKey();
			// sentenceBuilder.append(word + " ");
			// }
			sentences.add(sentence.toString());
		}
		return sentences;
	}

	/**
	 * Supported annotation types: "token", "pos,"lemma
	 * 
	 * @param annotation
	 * @param sentence
	 * @return
	 */
	public LinkedHashMap<String, String> extractTaggedTokensFromLabeledSentence(
			String annotation, CoreMap sentence) {

		List<CoreLabel> annotations = new ArrayList<CoreLabel>();

		// TODO: make this fail when an unsupported annotation is input. this is
		// broken atm
		if (annotation.equals("tokenization"))
			annotations = sentence.get(TokensAnnotation.class);
		// else if (annotation.equals("lemma"))
		// annotations = sentence.get(LemmaAnnotation.class);
		// else if (annotation.equals("pos"))
		// annotations = sentence.get(PartOfSpeechAnnotation.class);
		//

		LinkedHashMap<String, String> taggedTokens = new LinkedHashMap<String, String>();
		for (CoreLabel token : annotations) {
			String word = token.get(TextAnnotation.class);
			String pos = token.get(PartOfSpeechAnnotation.class);
			taggedTokens.put(word, pos);
		}

		return taggedTokens;
	}

	public List<String> extractSentences(String annotation, String text) {

		List<CoreMap> sentences = annotate(text);
		List<String> cleandSentences = new ArrayList<String>();
		if (annotation.equals("ssplit")) {
			for (CoreMap sentence : sentences) {
				@SuppressWarnings("unused")
				List<CoreMap> annotations = sentence
						.get(SentencesAnnotation.class);
				StringBuilder sb = new StringBuilder();
				for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
					// this is the text of the token
					String word = token.get(TextAnnotation.class);
					sb.append(word + " ");
				}
				cleandSentences.add(sb.toString());
			}
		}

		return cleandSentences;
	}

	public static void main(String[] args) {
//		StanfordNLP nlp = StanfordNLP
//				.getInstance("tokenize, ssplit, pos, lemma, parse");
		//
		// for (CoreMap sentence : sentences) {
		//
		// for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
		// // this is the text of the token
		// String word = token.get(TextAnnotation.class);
		// // this is the POS tag of the token
		// String pos = token.get(PartOfSpeechAnnotation.class);
		// System.out.println("[" + word + "/" + pos + "]");
		// ;
		// }
		// }
		// HashMap<String, List<String>> res =
		// extractEntities("Mr. Smith went to The Red Sox with his brother James and Max and John");
		// System.out.println("**********");
		//
		// for ( Entry<String, List<String>> e: res.entrySet()){
		// System.out.print(e.getKey() + ": " );
		// for (String f: e.getValue()){
		// System.out.print("\t" + f);
		// }
		// System.out.println();
		// }
	}

}

package max.nlp.wrappers.stanford;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import max.nlp.wrappers.WrappingConfiguration;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;

public class StanfordNER {

	private static StanfordNER self;

	@SuppressWarnings("rawtypes")
	private CRFClassifier classifier;
	private StanfordNER(){
		File f  = new File("/home/max/workspace/util/english.all.3class.nodistsim.crf.ser.gz");
		 try {
			classifier = CRFClassifier.getClassifier(f);
		} catch (ClassCastException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static StanfordNER getInstance(){
		if (self == null)
			self = new StanfordNER();
		return self;
	}
	
	/**
	 * @param text 
	 * @return The key of the hashmap is either ORGANIZATION,PERSON, or LOCATION. The values are the entities with their counts.
	 */
	public  HashMap<String, HashMap<String, Integer>> extractEntitiesWithCounts(String text) {
	
		HashMap<String, HashMap<String, Integer>> entities = new HashMap<String, HashMap<String, Integer>>();
		@SuppressWarnings("unchecked")
		List<List<CoreLabel>> sentences = classifier.classify(text);
		
		for (List<CoreLabel> lcl : sentences) {

	        Iterator<CoreLabel> iterator = lcl.iterator();

	        if (!iterator.hasNext())
	            continue;

	        CoreLabel cl = iterator.next();

	        while (iterator.hasNext()) {
	            String answer =
	                    cl.getString(CoreAnnotations.AnswerAnnotation.class);
	            if (answer.equals("O")) {
	                cl = iterator.next();
	                continue;
	            }

	            if (!entities.containsKey(answer))
	                entities.put(answer, new HashMap<String, Integer>());

	            String value = cl.getString(CoreAnnotations.ValueAnnotation.class);

	            while (iterator.hasNext()) {
	                cl = iterator.next();
	                if (answer.equals(
	                        cl.getString(CoreAnnotations.AnswerAnnotation.class))){
	                    value = value + " " +
	                           cl.getString(CoreAnnotations.ValueAnnotation.class);
	                }
	                else {
	                    if (!entities.get(answer).containsKey(value))
	                        entities.get(answer).put(value, 0);

	                    entities.get(answer).put(value,
	                            entities.get(answer).get(value) + 1);

	                    break;
	                }
	            }

	            if (!iterator.hasNext())
	                break;
	        }
	    }

	    return entities;
	}
	
	public  HashMap<String, List<String>> extractEntities(String text) {
		HashMap<String, List<String>> entities = new HashMap<String, List<String>>();

			@SuppressWarnings("unchecked")
			List<List<CoreLabel>> sentences = classifier.classify(text);
			
			for (List<CoreLabel> lcl : sentences) {

			    Iterator<CoreLabel> iterator = lcl.iterator();

			    if (!iterator.hasNext())
			        continue;

			    CoreLabel cl = iterator.next();

			    while (iterator.hasNext()) {
			        String answer =
			                cl.getString(CoreAnnotations.AnswerAnnotation.class);
			        if (answer.equals("O")) {
			            cl = iterator.next();
			            continue;
			        }

			        if (!entities.containsKey(answer))
			            entities.put(answer, new ArrayList<String>());

			        String value = cl.getString(CoreAnnotations.ValueAnnotation.class);

			        while (iterator.hasNext()) {
			            cl = iterator.next();
			            if (answer.equals(
			                    cl.getString(CoreAnnotations.AnswerAnnotation.class))){
			                value = value + " " +
			                       cl.getString(CoreAnnotations.ValueAnnotation.class);
			            }
			            else {
			                if (!entities.get(answer).contains(value))
			                    entities.get(answer).add(value);

			                break;
			            }
			        }

			        if (!iterator.hasNext())
			            break;
			    }
			}
	
	    return entities;
	}
	
	public HashMap<String,List<String>>  filterEntities(HashMap<String, List<String>> entities,List<String> allowedTypes){
		HashMap<String,List<String>> filteredEntities = new HashMap<String,List<String>>();
 		for ( Entry<String, List<String>>  e: entities.entrySet()){
 			String entityType = e.getKey();
 			if (allowedTypes.contains(entityType)){
 				filteredEntities.put(entityType, e.getValue());
 			}
		}
 		return filteredEntities;
	}
	public static void main(String[] args) {
		StanfordNLP nllp = StanfordNLP.getInstance();
	}
}

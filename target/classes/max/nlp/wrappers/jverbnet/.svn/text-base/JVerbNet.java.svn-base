package max.nlp.wrappers.jverbnet;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import max.nlp.wrappers.WrappingConfiguration;

import org.apache.log4j.Logger;


import com.google.common.collect.Maps;

import edu.mit.jverbnet.data.IMember;
import edu.mit.jverbnet.data.IVerbClass;
import edu.mit.jverbnet.index.IVerbIndex;
import edu.mit.jverbnet.index.VerbIndex;

/**
 * @author max.kaufmann
 *
 */
public class JVerbNet {

	private static WrappingConfiguration config = WrappingConfiguration.getInstance();
	private IVerbIndex index;
	private static JVerbNet self;
	private Map<String, String> verbsIndexedByClassID = Maps.newLinkedHashMap();
	private Map<String, String> verbsIndexedByClassName = Maps.newLinkedHashMap();

	private static Logger log = Logger.getLogger(JVerbNet.class);

	public static JVerbNet getInstance() {
		if (self == null) {
			self = new JVerbNet();
		}
		return self;
	}

	private JVerbNet() {
		try {
			URL url = new URL("file", null, config.getVerbNetIndexDir());
			index = new VerbIndex(url);
			index.open();
			makeVerbNetMappings();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Requres a verb formatted like "hit-18.1"
	 * @param verbString the formatted string
	 * @return 
	 */
	public IVerbClass lookupVerb(String verbString) {
		IVerbClass verb = index.getVerb(verbString);
		return verb;
	}
	
	public IVerbClass lookupVerbByName(String verbString) {
		return lookupVerb(verbsIndexedByClassName.get(verbString));
	}

	
	public IVerbClass lookupVerbByID(String verbString) {
		return lookupVerb(verbsIndexedByClassID.get(verbString));

	}


	/**
	 * Turns a string that represents the member of a verbclass into a reference
	 * to the member of that class
	 * 
	 * @param verb
	 *            A verb class
	 * @param member
	 *            A string representing the member of that verb class
	 * @return
	 */
	public IMember lookupMemberByName(IVerbClass verb, String member) {
		for (IMember m : verb.getMembers()) {
			if (m.getName().equals(member))
				return m;
		}
		return null;
	}



		// IVerbClass verb = ind.getRootVerb("hit-18.1");
		// System.out.println(ind.getVerb("18.1"));
		// IMember member = verb . getMembers ().get (0) ;
		//
		// Set < IWordnetKey > keys = member . getWordnetTypes (). keySet ();
		// IFrame frame = verb . getFrames ().get (0) ;
		// FrameType type = frame . getPrimaryType ();
		// String example = frame . getExamples ().get (0) ;
		// System .out . println ("id: " + verb . getID ());
		// System .out . println (" first wordnet keys : " + keys );
		// System .out . println (" first frame type : " + type . getID ());
		// System .out . println (" first example : " + example );

		// Set<IMember> cla = ind.getMembers("hesitate.02");
		// for ( IMember m :cla){
		// System.out.println("\t" + m.getName());
		// }
		//
	

	public IVerbIndex getIndex() {
		return index;
	}

	public void setIndex(IVerbIndex index) {
		this.index = index;
	}

	
	/**
	 * SemLink's class field refers to verb classes by their id's (e.g., 18.1),
	 * but JVerbNet refers to classes by their id's concatenated with the class
	 * name (e.g., hit-18.1). This makes a map of id's to class names (18.1,
	 * hit-18.1)
	 * 
	 * @return map of ids to class names
	 */
	public void makeVerbNetMappings() {
		Iterator<IVerbClass> itr = index.iterator();
		while (itr.hasNext()) {
			IVerbClass verb = itr.next();
			String id = verb.getID();
			String[] parts = id.split("-", 2);
			if (parts.length == 2) {
				verbsIndexedByClassName.put(parts[0], id);
				verbsIndexedByClassID.put(parts[1], id);
			} else {
				log.error("Couldn't make a verbnet mapping for" + verb.getID());
			}
		}
	}

}

package max.nlp.wrappers.semlink;

import java.io.File;

import max.nlp.util.xml.XMLUtils;
import max.nlp.wrappers.WrappingConfiguration;
import max.nlp.wrappers.jverbnet.JVerbNet;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import edu.mit.jverbnet.data.IMember;
import edu.mit.jverbnet.data.IVerbClass;

/**
 * @author max.kaufmann
 * 
 */
@SuppressWarnings("unused")
public class SemLink {

	static Logger log = Logger.getLogger(SemLink.class);

	private WrappingConfiguration config;
	private String semlinkDir;

	public SemLink() {
		config = WrappingConfiguration.getInstance();
		semlinkDir = config.getSemLinkDir();
	}

	public static void main(String[] args) {
		SemLink s = new SemLink();
//		s.extractVNFNMappings();
//		s.extractVNFNRoleMappings();
		s.extractVNPBMappings();
	}

	public void extractVNFNRoleMappings() {
		String mappingsFileString = semlinkDir + "vn-fn" + File.separator + "VN-FNRoleMapping.txt";
		log.info("parsing file");
		Document doc = XMLUtils.parseXMLFile(mappingsFileString);
		NodeList entries = doc.getElementsByTagName("vncls");

		JVerbNet verbnet = JVerbNet.getInstance();
		for (int i = 0; i < entries.getLength(); i++) {
			Element node = (Element) entries.item(i);
			String classID = node.getAttribute("class");
			IVerbClass verb = verbnet.lookupVerbByID(classID);
			String fnFrame = node.getAttribute("fnframe");
			//
			//
			NodeList roles = node.getElementsByTagName("roles");
			for (int j = 0; j < roles.getLength(); j++) {
				NodeList allRoles = node.getElementsByTagName("role");
				for (int k = 0; k < allRoles.getLength(); k++) {
					//
					Element role = (Element) allRoles.item(k);
					String fnrole = role.getAttribute("fnrole");
					String vnrole = role.getAttribute("fnrole");
				}
			}

		}
	}

	public void extractVNFNMappings() {
		String mappingsFileString = semlinkDir + "vn-fn" + File.separator + "VNC-FNF.s";
		JVerbNet verbnet = JVerbNet.getInstance();

		Document vnfn = XMLUtils.parseXMLFile(mappingsFileString);
		NodeList entries = vnfn.getElementsByTagName("vncls");
		for (int i = 0; i < entries.getLength(); i++) {
			Element node = (Element) entries.item(i);

			// Get the VN verb
			// Semlink's <code> class </code> attribute refers to the ID of the
			// class, we need to concatenate it with the name for JVerbNet
			String classID = node.getAttribute("class");
			IVerbClass verb = verbnet.lookupVerbByID(classID);
			;
			String vnmember = node.getAttribute("vnmember");
			// get the VN member as an object
			IMember member = verbnet.lookupMemberByName(verb, vnmember);

			// TODO support framenet
			String fnFrame = node.getAttribute("fnframe");
			String fnlexentry = node.getAttribute("fnlexent");

		}
	}
	
	public void extractVNPBMappings(){
		String mappingsFileString = semlinkDir + "vn-pb" + File.separator + "vnpbMappings";
		JVerbNet verbnet = JVerbNet.getInstance();
		Document vnpb = XMLUtils.parseXMLFile(mappingsFileString);
		NodeList predicates = vnpb.getElementsByTagName("predicate");
		for (int i = 0; i < predicates.getLength(); i++) {
			Element predicate = (Element) predicates.item(i);

			String lemma = predicate.getAttribute("lemma");
//			System.out.println(lemma);

			NodeList argmaps = predicate.getElementsByTagName("argmap");
			for (int j = 0; j < argmaps.getLength(); j++) {
				Element argmap = (Element) argmaps.item(j);
				String pbRoleset = argmap.getAttribute("pb-roleset");
				String vnClass = argmap.getAttribute("vn-class");


				NodeList roles = argmap.getElementsByTagName("role");
				for (int k = 0; k < roles.getLength(); k++) {
					Element role = (Element) roles.item(k);
					String pbArg = role.getAttribute("pb-arg");
					String vnTheta = role.getAttribute("vnThetha");
					System.out.println(pbArg);
					System.out.println(vnTheta);

				}
			}

		}

	}
	
	

}

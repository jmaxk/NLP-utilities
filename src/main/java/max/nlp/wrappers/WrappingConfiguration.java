package max.nlp.wrappers;

import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.yaml.snakeyaml.Yaml;

public class WrappingConfiguration {

	public static final String CONFIG_FILE = "config/WrappingConfig.yaml";

	private static WrappingConfiguration conf;

	private static Hashtable<Object, Object> table;

	@SuppressWarnings("static-access")
	public static WrappingConfiguration getInstance() {
		if (conf == null) {
			conf = new WrappingConfiguration();
			table = conf.table;
		}
		return conf;
	}

	@SuppressWarnings("unchecked")
	private WrappingConfiguration() {
		table = new Hashtable<Object, Object>();
		InputStream io = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(CONFIG_FILE);
		Yaml yaml = new Yaml();
		Map<String, Object> yamlProps = (Map<String, Object>) yaml.load(io);
		for (Entry<String, Object> e : yamlProps.entrySet()) {
			table.put(e.getKey(), e.getValue());
		}

		// Close the stream and ignore exceptions
		IOUtils.closeQuietly(io);
	}

	public void setProperty(String k, Object v) {
		table.put(k, v);
	}

	public Object getProperty(String key) {
		return table.get(key);
	}

	public String getString(String key) {
		return (String) table.get(key);
	}

	public String getUWNDir() {
		return (String) table.get("uwn.dir");
	}

	public String getDefaultWordNetDir() {
		return (String) table.get("wordnet.defaultDir");
	}

	public String getJWMEIndexFile() {
		return (String) table.get("jwme.indexFile");
	}

	public String getVerbNetIndexDir() {
		return (String) table.get("verbnet.indexDir");
	}

	public String getSemLinkDir() {
		return (String) table.get("semlink.dir");
	}

	public String getStanfordNER() {
		return (String) table.get("stanford.ner");
	}
	

	public String getSennaDir() {
		return (String) table.get("senna.dir");
	}
	
	


}

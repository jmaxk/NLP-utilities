package max.nlp.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

public class Experiment {
	
	private static String log4JPropertyFile = "/home/max/workspace/lyrics/src/main/resources/config/log4j.properties";
	static{
		configureLogger();
	}
	public static void configureLogger(){
		try {
			Properties p = new Properties();
			p.load(new FileInputStream(log4JPropertyFile));
			PropertyConfigurator.configure(p);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	
}

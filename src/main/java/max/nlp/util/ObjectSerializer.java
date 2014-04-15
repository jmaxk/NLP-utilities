package max.nlp.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectSerializer<T> {
	
	public  void save(String file, T myObject) {
		// Write to disk with FileOutputStream
		try {
			FileOutputStream f_out = new FileOutputStream(file);

			// Write object with ObjectOutputStream
			ObjectOutputStream obj_out = new ObjectOutputStream(f_out);

			// Write object out to disk
			obj_out.writeObject(myObject);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public  T load(String file) {
		try {
			// Read from disk using FileInputStream
			FileInputStream f_in = new FileInputStream(file);

			// Read object using ObjectInputStream
			ObjectInputStream obj_in = new ObjectInputStream(f_in);

			// Read an object
			Object obj = obj_in.readObject();

			//Should probably do some type checking at some point 
			@SuppressWarnings("unchecked")
			T vec = (T) obj;
			return vec;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}

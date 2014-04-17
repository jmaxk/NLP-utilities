package max.nlp.util.basic;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

	public static List<File> findFilesInDirWithExt(String folder, String ext) {

		List<File> matchingFiles = new ArrayList<File>();
		GenericExtFilter filter = new GenericExtFilter(ext);

		File dir = new File(folder);

		if (dir.isDirectory() == false) {
			System.out.println("Directory does not exists : " + dir);
			return null;
		}

		// list out all the file name and filter by the extension
		File[] list = dir.listFiles(filter);

		if (list.length == 0) {
			System.out.println("no files end with : " + ext);
			return null;
		}

		for (File file : list) {
			matchingFiles.add(file);
		}
		return matchingFiles;
	}

	// inner class, generic extension filter
	public static class GenericExtFilter implements FilenameFilter {

		private String ext;

		public GenericExtFilter(String ext) {
			this.ext = ext;
		}

		public boolean accept(File dir, String name) {
			return (name.endsWith(ext));
		}
	}

	private static final String FILE_DIR = "/home/max/temp/fbtest/";
	private static final String FILE_TEXT_EXT = ".vecs";

	public static void main(String args[]) {
		List<File> r = FileUtils.findFilesInDirWithExt(FILE_DIR, FILE_TEXT_EXT);
		System.out.println(r);
	}
}

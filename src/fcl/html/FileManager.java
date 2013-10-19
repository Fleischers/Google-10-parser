package fcl.html;

import java.io.*;

public class FileManager {

	
	public static String read (String filename) throws FileNotFoundException {
		StringBuilder sb = new StringBuilder();
		File file = new File (filename);
		exists(filename);
		try {
		
		BufferedReader in = new BufferedReader(new FileReader(file.getAbsoluteFile()));
		try {
			String s;
			while ((s = in.readLine()) != null) {
				sb.append(s);
				sb.append("\n");
			}
		} finally {
			in.close();
		}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		
		return sb.toString();
	}
	
	private static void exists (String filename) throws FileNotFoundException {
		File file = new File (filename);
		if (!file.exists()) {
			throw new FileNotFoundException(file.getName());
		}
	}
}

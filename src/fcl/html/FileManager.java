package fcl.html;

import java.io.*;
import java.util.Date;

public class FileManager {

	
	public static String read (String filename) throws FileNotFoundException {
		StringBuilder sb = new StringBuilder();
		File file = new File (filename);
		exists(filename);
		//System.out.println(file.getParent());
		
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
	
	public static Date getDate (String filename) {
		File file = new File (filename);
		file.exists();
		Date date = new Date(file.lastModified());
		return date;
	}
	
	public static void write (String fileName, String text) {
		
		    File file = new File(fileName);
		 
		    try {
		        if(!file.exists()){
		            file.createNewFile();
		        }
		        PrintWriter out = new PrintWriter(file.getAbsoluteFile());
		 
		        try {
		            out.print(text);
		        } finally {
		            out.close();
		        }
		    } catch(IOException e) {
		        throw new RuntimeException(e);
		    }
		
	}
	
	private static void exists (String filename) throws FileNotFoundException {
		File file = new File (filename);
		if (!file.exists()) {
			throw new FileNotFoundException(file.getName());
		}
	}
}

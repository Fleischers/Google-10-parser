package fcl.html;

import java.io.FileNotFoundException;
import java.util.List;

import org.htmlcleaner.*;

public class GoogleTopTenParser {
	private static String path = "/home/fleischer/workspace/Google-10-parser/resources/";
	private static String fileName = "java-Google.html";

	public static void main(String[] args) throws FileNotFoundException {
		String htmlFromFile = FileManager.read(path + fileName);
		//System.out.println(htmlFromFile);
		
		HtmlCleaner cleaner = new HtmlCleaner();
		TagNode node = cleaner.clean(htmlFromFile);
		
		TagNode[] r = node.getElementsByAttValue("class", "r", true, true);
		StringBuilder domains = new StringBuilder();
		for (int i=0; i < r.length; i++) {
			List <?> elements = r[i].getAllElementsList(true);
			System.out.println(elements);
			TagNode[] a = r[i].getElementsByName("a", true);
			for (int j=0; j < a.length; j++) {
				String site = a[j].getAttributeByName("href");
				domains.append(findDomain(site) + "\n");
			}
		}
		System.out.println(domains);
		
	}
	
	static String findDomain(String s) {
		return s;
	}

}

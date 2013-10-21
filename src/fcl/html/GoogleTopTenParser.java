package fcl.html;

import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.htmlcleaner.*;

public class GoogleTopTenParser {
	private static String path = "/home/fleischer/workspace/Google-10-parser/resources/";
	private static String fileName = "java-Google.html";
	private static String fileName2 = "java2Google.html";
	private static String fileName3 = "GorlifSense-Google.html";
	private static String fileName4 = "Jexer-Google.html";

	public static void main(String[] args) throws FileNotFoundException, URISyntaxException {
		String htmlFromFile = FileManager.read(path + fileName4);
		//System.out.println(htmlFromFile);
		
		HtmlCleaner cleaner = new HtmlCleaner();
		TagNode node = cleaner.clean(htmlFromFile);
		//find <h3 class="r">
		TagNode[] r;
		r = node.getElementsByAttValue("class", "r", true, false); 
		
		StringBuilder domains = new StringBuilder();
		int counter = 0;
		for (int i=0; i < r.length; i++) {
			
			List <?> elements = r[i].getAllElementsList(true);
			System.out.println(elements);
			TagNode a = r[i].findElementByName("a", true);
			if (!a.hasAttribute("class")) {
				counter++;
				String site = a.getAttributeByName("href");
				domains.append(counter + " " + findDomain(site) + "\n");
			}
		}
		System.out.println(domains);
		
	}
	
	static String findDomain(String url) throws URISyntaxException {
		URI uri = new URI(url);
		String domain = uri.getHost();
		if (domain.contains("google")) {
			//TODO google url GET resend
			domain = uri.getQuery();
		}
		if (domain.startsWith("www.")) {
			domain = domain.substring(4);
		}
		return domain;
	}

}

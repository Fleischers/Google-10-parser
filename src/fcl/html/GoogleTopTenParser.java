package fcl.html;

import java.io.*;
import java.net.*;
//import java.nio.file.*;
import java.util.*;

import org.htmlcleaner.*;

public class GoogleTopTenParser {
	private static String path = "/home/fleischer/workspace/Google-10-parser/resources/";
	private static String fileName = "java-Google.html";
	private static String fileName2 = "java2Google.html";
	private static String fileName3 = "GorlifSense-Google.html";
	private static String fileName4 = "Jexer-Google.html";
	private static String fileName5 = "apple-Google.html";
	
	private static List <List <TopSite>> topsites = new ArrayList <List <TopSite>> ();

	public static void main(String[] args) throws FileNotFoundException, URISyntaxException  {
		/*Path paths = ; TODO relative path
		System.out.println(paths);*/
		if (args.length > 0) {
			path = args[0];
		}
		File folder = new File(path);
		topsites = listFilesInFolder(folder);
		FileManager.write("target/parseResult" + ".txt", topsites.toString());
		
		//TODO delelte
		List <List <TopSite>> topsites2 = new ArrayList <List <TopSite>> ();
		topsites2.add(parse(path + fileName));
		topsites2.add(parse(path + fileName2));
		topsites2.add(parse(path + fileName3));
		topsites2.add(parse(path + fileName4));
		topsites2.add(parse(path + fileName5));
		//System.out.println(topsites);
		
	}
	
	private static List<TopSite> parse(String filepath) throws FileNotFoundException, URISyntaxException {
		String htmlFromFile = FileManager.read(filepath);
		Date date = FileManager.getDate(filepath);
		List <TopSite> topsites = new ArrayList<TopSite>();
		
		System.out.println(date);
		
		HtmlCleaner cleaner = new HtmlCleaner();
		TagNode node = cleaner.clean(htmlFromFile);
		TagNode[] r = node.getElementsByAttValue("class", "r", true, false); //find <h3 class="r">
		int counter = 0;
		int aClassL = 3;
		for (int i=0; i < r.length; i++) {
			TagNode a = r[i].findElementByName("a", true);
			if (!a.hasAttribute("class")) {
				counter++;
				String site = a.getAttributeByName("href");
				topsites.add(new TopSite (counter, findDomain(site), date));
				
			} else {
				if (aClassL > 0) {
					aClassL--;
					counter++;
					String site = a.getAttributeByName("href");
					topsites.add(new TopSite (counter, findDomain(site), date));
				}
			}
		}
		return topsites;
	}

	private static String findDomain(String url) throws URISyntaxException {
		URI uri = new URI(url);
		String domain = uri.getHost();
		if (domain.contains("google")) {
			//TODO google url GET resend
			if (uri.getPath().contains("url") ) {
				domain = uri.getQuery();
				
				if (domain.contains("&")) {
					String[] tokens = domain.split("&");
					for (String t: tokens) {
						if (t.contains("url=")) {
							URI guri = new URI (t.substring(4));
							domain = guri.getHost();
							break;
						}
					}
				}
			}
		}
		/*if (domain.startsWith("www.")) {
			domain = domain.substring(4);
		}*/
		return domain;
	}
	
	public static List<List <TopSite>> listFilesInFolder(File folder) throws FileNotFoundException, URISyntaxException {
		List <List <TopSite>> topsites = new ArrayList <List <TopSite>> ();
	    for (File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesInFolder(fileEntry);
	        } else {
	            System.out.println(fileEntry.getName());
	            String pathF = fileEntry.getPath();
	            
	    		topsites.add(parse(pathF));
	    		System.out.println (parse(pathF)); //TODO delete this
	        }
	    }
	    return topsites;
	}

}

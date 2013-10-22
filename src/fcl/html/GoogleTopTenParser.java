package fcl.html;

import java.io.*;
import java.net.*;
import java.security.AccessController;
//import java.nio.file.*;
import java.util.*;

import org.htmlcleaner.*;

import sun.security.action.GetPropertyAction;

public class GoogleTopTenParser {
	private static String fileName = "java-Google.html";
	private static String path = "/home/fleischer/workspace/Google-10-parser/resources/" + fileName;
	
	final static char SLASH = AccessController.doPrivileged(new GetPropertyAction("file.separator")).charAt(0);


	private static List <TopSite> topsites = new ArrayList <TopSite> ();

	public static void main(String[] args) throws FileNotFoundException, URISyntaxException  {
		if (args.length > 0) {
			path = args[0];
		}
		topsites = parse(path);
		
		StringBuilder sb = new StringBuilder();
		
		for (TopSite itr: topsites) {
			System.out.print(itr);
			sb.append(itr);
		}
		String name = new String();
		String[] folders = path.split(String.valueOf(SLASH));
		name = folders[folders.length-1];
		
		FileManager.write("target" + SLASH + name + ".txt", sb.toString());
		
	}
	
	private static List<TopSite> parse(String filepath) throws FileNotFoundException, URISyntaxException {
		String htmlFromFile = FileManager.read(filepath);
		Date date = FileManager.getDate(filepath);
		List <TopSite> topsites = new ArrayList<TopSite>();		
		HtmlCleaner cleaner = new HtmlCleaner();
		TagNode node = cleaner.clean(htmlFromFile);
		TagNode[] r = node.getElementsByAttValue("class", "r", true, false); //find <h3 class="r">
		int counter = 0;
		boolean notBeenHere = true;
		int notBeenHereCounter = 0;
		for (int i=0; i < r.length; i++) {
			TagNode ahref = r[i].findElementByName("a", true);
			if (!ahref.hasAttribute("class")) {
				counter++;
				String site = ahref.getAttributeByName("href");
				topsites.add(new TopSite (counter, findDomain(site), date));
				System.out.println("[a] i: " + i);
				System.out.println(topsites.get(counter-1));
				
				
			} else {
				System.out.println("I am here, and I (" + notBeenHere + ") here so many times " + notBeenHereCounter++ );
				if (notBeenHere) {
					List <TagNode> aClassNode = new ArrayList <TagNode> ();
					int tempI = i;
					int aCounter = 0;
					do {
						aClassNode.add(r[tempI].findElementByName("a", true));
						System.out.print("[tempI] tempI: " + tempI + " ");
						System.out.println("[contains] " + aClassNode.get(aCounter) + " " + aClassNode.get(aCounter).getAttributeByName("href"));
						tempI++;
					} while (aClassNode.get(aCounter++).hasAttribute("class"));
					int aClassL = aClassNode.size() /2;
					TagNode aL;
					System.out.println("[aClass] size: " + aClassNode.size());
					i = tempI-2;
					tempI = 0;
					System.out.println("[aL final] i: " + i);
					System.out.println("aClassL: " + aClassL);
					while (aClassL > 0) {
						aClassL--;
						System.out.println("aClassL inside: " + aClassL);
						counter++;
						aL = aClassNode.get(tempI++);
						String site = aL.getAttributeByName("href");
						topsites.add(new TopSite (counter, findDomain(site), date));
						System.out.println(topsites.get(counter-1));
					}
					notBeenHere = false;
				}
				
			}
		}
		return topsites;
	}

	private static String findDomain(String url) throws URISyntaxException {
		URI uri = new URI(url);
		String domain = uri.getHost();
		if (domain.contains("google")) {
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
	        }
	    }
	    return topsites;
	}

}

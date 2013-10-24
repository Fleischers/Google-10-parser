package fcl.html;
/*
 * @version v1.1.*
*/

import java.io.*;
import java.net.*;
import java.util.*;

import org.htmlcleaner.*;

import java.security.AccessController;
import sun.security.action.GetPropertyAction;

public class GoogleTopTenParser {
	private static String path;
	final static char SLASH = AccessController.doPrivileged(new GetPropertyAction("file.separator")).charAt(0);
	private static List <TopSite> topsites = new ArrayList <TopSite> ();

	public static void main(String[] args) throws FileNotFoundException, URISyntaxException  {
		if (args.length > 0) {
			path = args[0];
		}
		topsites = parse(path);
		
		StringBuilder sb = new StringBuilder();
		String result;
		
		for (TopSite itr: topsites) {
			System.out.print(itr);
			sb.append(itr);
		}
		String name = new String();
		String[] folders = path.split(String.valueOf(SLASH));
		name = folders[folders.length-1];
		result = sb.toString();
		if (result.isEmpty() || !name.contains("html")) {
			System.err.print ("[HTML Warning] Only valid html files is accepted");
			System.err.println(" - resulting filename is marked with [XHTMLwarning]");
			name = "[HTMLwarning]" + name;
		}
		FileManager.folderExists("target");
		FileManager.write("target" + SLASH + name + ".txt", result);
		
	}
	
	private static List<TopSite> parse(String filepath) throws FileNotFoundException, URISyntaxException {
		String htmlFromFile = FileManager.read(filepath);
		Date date = FileManager.getDate(filepath);
		List <TopSite> topsites = new ArrayList<TopSite>();		
		HtmlCleaner cleaner = new HtmlCleaner();
		TagNode node = cleaner.clean(htmlFromFile);
		//find <h3 class="r">
		TagNode[] r = node.getElementsByAttValue("class", "r", true, false);
		int counter = 0;
		boolean notBeenHere = true;
		for (int i=0; i < r.length; i++) {
			TagNode ahref = r[i].findElementByName("a", true);
			if (!ahref.hasAttribute("class")) {
				counter++;
				String site = ahref.getAttributeByName("href");
				topsites.add(new TopSite (counter, findDomain(site), date));
			} else {
				if (notBeenHere) {
					List <TagNode> aClassNode = new ArrayList <TagNode> ();
					int tempI = i;
					int aCounter = 0;
					do {
						aClassNode.add(r[tempI].findElementByName("a", true));
						tempI++;
					} while (aClassNode.get(aCounter++).hasAttribute("class"));
					int aClassL = aClassNode.size() - 1;
					TagNode aL;
					i = tempI-2;
					tempI = 0;
					while (aClassL > 0) {
						aClassL--;
						counter++;
						aL = aClassNode.get(tempI);
						if (tempI + 2 > aClassNode.size()) {
							break;
						} else {
							tempI+=2;
						}
						String site = aL.getAttributeByName("href");
						topsites.add(new TopSite (counter, findDomain(site), date));
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
}

package fcl.html;

import java.util.Date;

public class TopSite {
	int position;
	private String domain;
	Date date;
	
	TopSite (int pos, String dom, Date dat) {
		position = pos;
		domain = dom;
		date = dat;
	}
	
	@Override
	public String toString () {
		return domain + "\n";
	}

}

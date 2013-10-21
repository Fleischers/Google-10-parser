package fcl.html;

import java.util.Date;

public class TopSite {
	private int position;
	private String domain;
	private Date date;
	
	TopSite (int pos, String dom, Date dat) {
		position = pos;
		domain = dom;
		date = dat;
	}
	
	@Override
	public String toString () {
		return position + " " + domain + " - " + date + "\n";
	}

}

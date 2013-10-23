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
	
	public int getPosition() {
		return position;
	}

	public Date getDate() {
		return date;
	}
	
	@Override
	public String toString () {
		return domain + "\n";
	}

}

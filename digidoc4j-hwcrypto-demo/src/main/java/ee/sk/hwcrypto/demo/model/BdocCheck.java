package ee.sk.hwcrypto.demo.model;

import java.util.ArrayList;

/*
 BDOC faili konteineri sisu koos valideerimise tulemusena
 */
public class BdocCheck {
	
	private boolean check;  //kas fail valideerus
	private ArrayList<BdocFailid> failid; //konteineri failid (nimi ja sisu)
	private String commonname;  //esimsese signatuuri CommonName
	
	public BdocCheck() {}
	
	public BdocCheck(boolean check) {  //uue klassi tegemisel, sea check=false
		this.check=check;
	}
	
	public boolean isCheck() {
		return check;
	}
	public void setCheck(boolean check) {
		this.check = check;
	}
	
	public String getCommonname() {
		return this.commonname;
	}
	
	public void setCommonname(String commonname) {
		this.commonname = commonname;
	}
	
	public ArrayList<BdocFailid> getFailid() {
		return this.failid;
	}
	
	public void setFailid(ArrayList<BdocFailid> failid) {		
		this.failid = new ArrayList<BdocFailid>();
		this.failid.addAll(failid);
	}

}

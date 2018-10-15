package ee.sk.hwcrypto.demo.model;

import org.bouncycastle.util.Arrays;


/*
 BDOC konteinerist loetud failide nimekiri koos sisuga
 */
public class BdocFailid {
	
	private String filename;  //faili nimi
	private byte[] filebytes;  //faili sisu, bytearray

	public BdocFailid() {}

	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	

	public byte[] getFilebytes() {
		return Arrays.copyOf(filebytes,filebytes.length);
	}

	public void setFilebytes(byte[] filebytes) {
		this.filebytes = Arrays.copyOf(filebytes, filebytes.length);
	}
	
}

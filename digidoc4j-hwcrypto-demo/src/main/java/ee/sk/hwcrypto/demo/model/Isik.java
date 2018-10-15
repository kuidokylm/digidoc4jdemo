package ee.sk.hwcrypto.demo.model;


//Kuido test
public class Isik {
	
	private String name;
	private String city;

    public Isik() {
    }

    public Isik(String name, String city) {
        this.name = name;
        this.city=city;
    }

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}

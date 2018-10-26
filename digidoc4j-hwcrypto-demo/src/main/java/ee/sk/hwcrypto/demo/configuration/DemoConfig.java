package ee.sk.hwcrypto.demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class DemoConfig {

	@Bean
    public org.digidoc4j.Configuration configuration() {
        // System.setProperty("digidoc4j.mode", this.environment.getProperty("digidoc4j.mode"));
		org.digidoc4j.Configuration cf = new org.digidoc4j.Configuration(org.digidoc4j.Configuration.Mode.TEST); 
        return new org.digidoc4j.Configuration(org.digidoc4j.Configuration.Mode.TEST); 
}
}

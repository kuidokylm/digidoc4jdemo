package ee.sk.hwcrypto.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Created by Janar Rahumeel (CGI Estonia)
 */

@Configuration
public class Demo {

    @Autowired
    private Environment environment;

    @Bean
    public org.digidoc4j.Configuration configuration() {
        // System.setProperty("digidoc4j.mode", this.environment.getProperty("digidoc4j.mode"));
        return new org.digidoc4j.Configuration(org.digidoc4j.Configuration.Mode.TEST);
    }

}

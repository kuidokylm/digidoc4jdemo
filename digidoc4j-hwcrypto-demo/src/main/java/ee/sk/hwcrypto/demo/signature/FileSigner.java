/**
 * DigiDoc4j Hwcrypto Demo
 * <p>
 * The MIT License (MIT)
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package ee.sk.hwcrypto.demo.signature;

import org.digidoc4j.*;
import org.digidoc4j.impl.asic.asice.bdoc.BDocContainerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import eu.europa.esig.dss.x509.CertificateToken;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

@Service
public class FileSigner {

    private static final Logger log = LoggerFactory.getLogger(FileSigner.class);
    private static final DigestAlgorithm DIGEST_ALGORITHM = DigestAlgorithm.SHA256;
    private Configuration configuration = new Configuration(Configuration.Mode.TEST);  //http://demo.sk.ee/ocsp

    
    
    public Container createContainer(DataFile dataFile) {    	
    	//Configuration configuration = Configuration.getInstance(); //see annab alati välja PROD keskkonna andmed   	    	
    	configuration.setTrustedTerritories("EE");
    	
    	/*
    	org.digidoc4j.TSLCertificateSource tsl = configuration.getTSL();
    	List<CertificateToken> ts = tsl.getCertificates();
    	String ssd ;
    	for (CertificateToken ct : ts)
    	{
    		ssd = ct.getEncryptionAlgorithm().getName();
    		System.out.println("EncryptionAlgorithm "+ssd);
    		ssd=ct.getDigestAlgorithm().getName();
    		System.out.println("DigestAlgorithm "+ssd);
    		ssd=ct.getDigestAlgorithm().getJavaName();
    		System.out.println("JavaName "+ssd);
    	}  */
    	
    	/*
        String ger =  configuration.getOcspSource();
        System.out.println("OCSP aadress: "+ger);
        ger=configuration.getTspSource();
        System.out.println("TspSource: "+ger);
        ger=configuration.getTslKeyStoreLocation();
        System.out.println("TslKeyStoreLocation: "+ger);
        ger = configuration.getTslLocation();
        System.out.println("TslLocation: "+ger);
        */
//        ger=configuration.getTslKeyStorePassword();
//        System.out.println("TslKeyStorePassword: "+ger);  //digidoc4j-password
        
        
        configuration.setOcspSource("http://ocsp.sk.ee/");
        configuration.setTspSource("http://tsa.sk.ee");
        configuration.setTslKeyStoreLocation("keystore/keystore.jks");        
        configuration.setTslLocation("https://ec.europa.eu/information_society/policy/esignature/trusted-list/tl-mp.xml");
                
        String ger =  configuration.getOcspSource();
        System.out.println("OCSP uus aadress: "+ger);
        ger=configuration.getTspSource();
        System.out.println("Uus TspSource: "+ger);
        ger=configuration.getTslKeyStoreLocation();
        System.out.println("Uus TslKeyStoreLocation: "+ger);
        ger=configuration.getTslLocation();
        System.out.println("Uus TslLocation: "+ger);
        
        Container container = BDocContainerBuilder.
                aContainer().
                withDataFile(dataFile).
                withConfiguration(configuration).
                build();
        return container;
    }

    public DataToSign getDataToSign(Container containerToSign, String certificateInHex) {
        X509Certificate certificate = getCertificate(certificateInHex);
        DataToSign dataToSign = SignatureBuilder.
                aSignature(containerToSign).
                withSigningCertificate(certificate).
                withSignatureDigestAlgorithm(DIGEST_ALGORITHM).
                withSignatureProfile(SignatureProfile.LT_TM).
                //withSignatureProfile(SignatureProfile.B_BES).
                buildDataToSign();
        return dataToSign;
    }

    public void signContainer(Container container, DataToSign dataToSign, String signatureInHex) {
        byte[] signatureBytes = DatatypeConverter.parseHexBinary(signatureInHex);
        Signature signature = dataToSign.finalize(signatureBytes);
        container.addSignature(signature);
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    private X509Certificate getCertificate(String certificateInHex) {
        byte[] certificateBytes = DatatypeConverter.parseHexBinary(certificateInHex);
        try (InputStream inStream = new ByteArrayInputStream(certificateBytes)) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate certificate = (X509Certificate)cf.generateCertificate(inStream);
            return certificate;
        } catch (CertificateException | IOException e) {
            log.error("Error reading certificate: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

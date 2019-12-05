package cn.boommanpro;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;
import sun.security.tools.keytool.CertAndKeyGen;
import sun.security.x509.X500Name;

/**
 * 主要用于创建keyStore文件，保存起来
 *
 * @author Xia
 */
@Slf4j
public class KeyStoreCreate2 {

    public static String filePath = "new_KeyStore.keystore";

    private static final int keysize = 1024;

    private static final String commonName = "cas.example.org";

    private static final String organizationalUnit = "IT";

    private static final String organization = "test";

    private static final String city = "beijing";

    private static final String state = "beijing";

    private static final String country = "beijing";

    private static final long validity = 1096; // 3 years

    private static final String alias = "tomcat";

    private static final char[] keyPassword = "changeit".toCharArray();

    public static void main(String[] args) throws GeneralSecurityException {
        try {

            KeyStore ks = KeyStore.getInstance("pkcs12");
//            char[] password = "123456".toCharArray();
            ks.load(null, null);

            CertAndKeyGen keypair = new CertAndKeyGen("RSA", "SHA1WithRSA", null);
            X500Name x500Name = new X500Name(commonName, organizationalUnit, organization, city, state, country);
            keypair.generate(keysize);

            PrivateKey privateKey = keypair.getPrivateKey();
            X509Certificate[] chain = new X509Certificate[1];
            chain[0] = keypair.getSelfCertificate(x500Name, new Date(), (long) validity * 24 * 60 * 60);

            // store away the key store
            FileOutputStream fos = new FileOutputStream(filePath);
            ks.setKeyEntry(alias, privateKey, keyPassword, chain);
            ks.store(fos, keyPassword);
            fos.close();
            System.out.println("create Success");
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            System.out.println(e.getCause());
        }
    }

}
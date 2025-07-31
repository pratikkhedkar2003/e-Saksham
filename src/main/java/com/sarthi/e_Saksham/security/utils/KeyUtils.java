package com.sarthi.e_Saksham.security.utils;

import com.nimbusds.jose.jwk.RSAKey;
import com.sarthi.e_Saksham.exception.ESakshamApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Component
public class KeyUtils {
    private static final Logger log = LoggerFactory.getLogger(KeyUtils.class);

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${keys.id}")
    private String keyId;

    @Value("${keys.algorithm}")
    private String keyAlgorithm;

    @Value("${keys.private}")
    private String privateKey;

    @Value("${keys.public}")
    private String publicKey;

    public RSAKey getOrGenerateRSAKeyPair() {
        return getOrGenerateRSAKeyPair(privateKey, publicKey);
    }

    private RSAKey getOrGenerateRSAKeyPair(String privateKeyName, String publicKeyName) {
        log.info("Inside getOrGenerateRSAKeyPair {}, {}", privateKeyName, publicKeyName);

        Path keysDirectory = Paths.get("src", "main", "resources", "keys");
        verifyKeysDirectory(keysDirectory);

        if (Files.exists(keysDirectory.resolve(privateKeyName)) && Files.exists(keysDirectory.resolve(publicKeyName))) {
            log.info("Inside getOrGenerateRSAKeyPair, RSA keys already exists. Loading keys from file paths: {}, {}", privateKeyName, publicKeyName);

            try {
                File privateKeyFile = keysDirectory.resolve(privateKeyName).toFile();
                File publicKeyFile = keysDirectory.resolve(publicKeyName).toFile();

                log.info("Inside getOrGenerateRSAKeyPair, Creating RSA Key pair from key files with KeyID: {}, KeyAlgorithm {}", keyId, keyAlgorithm);

                KeyFactory keyFactory = KeyFactory.getInstance(keyAlgorithm);

                byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
                EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
                RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);

                byte[] privateKeyBytes = Files.readAllBytes(privateKeyFile.toPath());
                PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
                RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);

                return new RSAKey.Builder(publicKey).privateKey(privateKey).keyID(keyId).build();
            } catch (Exception exception) {
                log.error("Exception while reading Key Files and Generating RSA Key Pair, {}", exception.getMessage());
                throw new ESakshamApiException(exception.getMessage());
            }
        } else {
            if (activeProfile.equalsIgnoreCase("prod")) {
                throw new ESakshamApiException("Public and Private key don't exist in prod environment");
            }
            log.info("Inside getOrGenerateRSAKeyPair, Generating RSA private key and public keys: {}, {}", privateKeyName, publicKeyName);

            try {
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(keyAlgorithm);
                keyPairGenerator.initialize(2048);
                KeyPair keyPair = keyPairGenerator.generateKeyPair();

                RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
                RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

                try(FileOutputStream fos = new FileOutputStream(keysDirectory.resolve(privateKeyName).toFile())) {
                    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
                    fos.write(keySpec.getEncoded());
                }

                try(FileOutputStream fos = new FileOutputStream(keysDirectory.resolve(publicKeyName).toFile())) {
                    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey.getEncoded());
                    fos.write(keySpec.getEncoded());
                }
                log.info("Inside getOrGenerateRSAKeyPair, Successfully created private key and public key Files : {}, {}", privateKeyName, publicKeyName);

                return new RSAKey.Builder(publicKey).privateKey(privateKey).keyID(keyId).build();
            } catch (Exception exception) {
                log.error("Exception while Generating key Files and creating RSA Key Pair: {}", exception.getMessage());
                throw new ESakshamApiException(exception.getMessage());
            }
        }
        
    }

    private void verifyKeysDirectory(Path keysDirectory) {
        if (!Files.exists(keysDirectory)) {
            log.info("Inside verifyKeysDirectory, creating Keys Directory");
            try {
                Files.createDirectories(keysDirectory);
            } catch (Exception exception) {
                log.error("Exception while creating Keys Directory, {}", exception.getMessage());
                throw new ESakshamApiException(exception.getMessage());
            }
            log.info("Keys directory created: {}", keysDirectory);
        }
    }

}

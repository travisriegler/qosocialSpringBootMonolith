package com.qosocial.v1api.auth.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import java.io.IOException;
import java.io.StringReader;
import java.security.Key;
import java.security.KeyPair;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

@Configuration
@Profile({"staging", "prod"})
public class StagingProdJwtKeyConfig {

    private static final Logger logger = LoggerFactory.getLogger(StagingProdJwtKeyConfig.class);

    private final SecretsManagerClient secretsManagerClient;

    @Value("${aws.secrets.accessToken.keyPair}")
    private String accessTokenKeyPairSecret;

    @Value("${aws.secrets.refreshToken.keyPair}")
    private String refreshTokenKeyPairSecret;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Autowired
    public StagingProdJwtKeyConfig(SecretsManagerClient secretsManagerClient) {
        this.secretsManagerClient = secretsManagerClient;
    }

    @Bean
    @Qualifier("accessTokenKeyPair")
    public KeyPair accessTokenKeyPair() throws IOException {

        //Get the secret string
        String secretString = getSecretString(accessTokenKeyPairSecret);

        //Convert keysString into JsonNode
        JsonNode secretJson = parseSecretStringToJson(secretString);

        // Extract the values from the JSON
        String base64PrivateKeyString = secretJson.get("privateKey").asText();
        String base64PublicKeyString = secretJson.get("publicKey").asText();

        Key privateKey = convertStringPEMToKey(base64PrivateKeyString, false);
        Key publicKey = convertStringPEMToKey(base64PublicKeyString, true);

        return new KeyPair((RSAPublicKey) publicKey, (RSAPrivateKey) privateKey);

    }

    @Bean
    @Qualifier("refreshTokenKeyPair")
    public KeyPair refreshTokenKeyPair() throws IOException {

        //Get the secret string
        String secretString = getSecretString(refreshTokenKeyPairSecret);

        //Convert keysString into JsonNode
        JsonNode secretJson = parseSecretStringToJson(secretString);

        // Extract the values from the JSON
        String base64PrivateKeyString = secretJson.get("privateKey").asText();
        String base64PublicKeyString = secretJson.get("publicKey").asText();

        Key privateKey = convertStringPEMToKey(base64PrivateKeyString, false);
        Key publicKey = convertStringPEMToKey(base64PublicKeyString, true);

        return new KeyPair((RSAPublicKey) publicKey, (RSAPrivateKey) privateKey);

    }

    private String getSecretString(String secretId) {
        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretId)
                .build();

        GetSecretValueResponse getSecretValueResponse = secretsManagerClient.getSecretValue(getSecretValueRequest);

        return getSecretValueResponse.secretString();
    }

    private JsonNode parseSecretStringToJson(String secretId) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(secretId);
    }

    private Key convertStringPEMToKey(String base64PemKey, boolean isPublicKey) throws IOException {

        String cleanedBase64PemKey = base64PemKey.trim().replaceAll(" ", "");

        byte[] decodedBytes = Base64.getDecoder().decode(cleanedBase64PemKey);
        String pemKey = new String(decodedBytes);

        try (PEMParser pemParser = new PEMParser(new StringReader(pemKey))) {

            Object parsedObj = pemParser.readObject();

            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");

            return isPublicKey ? converter.getPublicKey((SubjectPublicKeyInfo) parsedObj) : converter.getPrivateKey((PrivateKeyInfo) parsedObj);
        }
    }

}
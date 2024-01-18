package com.qosocial.v1api.common.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import javax.sql.DataSource;

@Configuration
@Profile({"staging", "prod"})
public class DataSourceConfig {

    private final SecretsManagerClient secretsManagerClient;
    @Value("${aws.secretName}")
    private String secretName;
    private static final Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

    @Autowired
    public DataSourceConfig(SecretsManagerClient secretsManagerClient) {
        this.secretsManagerClient = secretsManagerClient;
    }

    @Bean
    public DataSource dataSource() {

        try {
            GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                    .secretId(secretName)
                    .build();

            GetSecretValueResponse getSecretValueResponse = secretsManagerClient.getSecretValue(getSecretValueRequest);

            String secret = getSecretValueResponse.secretString();

            // Parse the secret JSON string
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode secretJson = objectMapper.readTree(secret);

            // Extract the values from the JSON
            String username = secretJson.get("username").asText();
            String password = secretJson.get("password").asText();
            String host = secretJson.get("host").asText();
            String port = secretJson.get("port").asText();
            String dbName = secretJson.get("dbname").asText();

            // Construct the JDBC URL
            String jdbcUrl = String.format("jdbc:mysql://%s:%s/%s", host, port, dbName);

            // Create and return the data source
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setUrl(jdbcUrl);
            dataSource.setUsername(username);
            dataSource.setPassword(password);

            return dataSource;

        } catch (Exception ex) {
            logger.error("DataSourceConfig encountered an unexpected error while trying to connect with AWS", ex);
            throw new DataAccessResourceFailureException("DataSourceConfig encountered an unexpected error while trying to connect with AWS", ex);
        }
    }
}


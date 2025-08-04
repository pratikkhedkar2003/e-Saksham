package com.sarthi.e_Saksham.repository.client.implementation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sarthi.e_Saksham.repository.client.ESakshamRegisteredClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.ConfigurationSettingNames;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class ESakshamRegisteredClientRepositoryImpl implements ESakshamRegisteredClientRepository {
    private static final Logger log = LoggerFactory.getLogger(ESakshamRegisteredClientRepositoryImpl.class);

    // @formatter:off
    private static final String COLUMN_NAMES = "id, "
            + "client_id, "
            + "client_id_issued_at, "
            + "client_secret, "
            + "client_secret_expires_at, "
            + "client_name, "
            + "client_authentication_methods, "
            + "authorization_grant_types, "
            + "redirect_uris, "
            + "post_logout_redirect_uris, "
            + "scopes, "
            + "client_settings,"
            + "token_settings";
    // @formatter:on

    private static final String TABLE_NAME = "oauth2_registered_client";

    private static final String PK_FILTER = "id = ?";

    private static final String LOAD_REGISTERED_CLIENT_SQL = "SELECT " + COLUMN_NAMES + " FROM " + TABLE_NAME
            + " WHERE ";

    // @formatter:off
    private static final String INSERT_REGISTERED_CLIENT_SQL = "INSERT INTO " + TABLE_NAME
            + "(" + COLUMN_NAMES + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    // @formatter:on

    // @formatter:off
    private static final String UPDATE_REGISTERED_CLIENT_SQL = "UPDATE " + TABLE_NAME
            + " SET client_secret = ?, client_secret_expires_at = ?, client_name = ?, client_authentication_methods = ?,"
            + " authorization_grant_types = ?, redirect_uris = ?, post_logout_redirect_uris = ?, scopes = ?,"
            + " client_settings = ?, token_settings = ?"
            + " WHERE " + PK_FILTER;
    // @formatter:on

    private static final String COUNT_REGISTERED_CLIENT_SQL = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE ";

    private final JdbcOperations jdbcOperations;

    private RowMapper<RegisteredClient> registeredClientRowMapper;

    private Function<RegisteredClient, List<SqlParameterValue>> registeredClientParametersMapper;

    public ESakshamRegisteredClientRepositoryImpl(JdbcOperations jdbcOperations) {
        Assert.notNull(jdbcOperations, "jdbcOperations cannot be null");
        this.jdbcOperations = jdbcOperations;
        this.registeredClientRowMapper = new RegisteredClientRowMapper();
        this.registeredClientParametersMapper = new RegisteredClientParametersMapper();
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        log.info("Inside save Method, trying to save or update the given client: {}", registeredClient.getClientId());

        Assert.notNull(registeredClient, "registeredClient cannot be null");
        RegisteredClient existingRegisteredClient = findBy(PK_FILTER, registeredClient.getId());
        if (existingRegisteredClient != null) {
            updateRegisteredClient(registeredClient);
        }
        else {
            insertRegisteredClient(registeredClient);
        }
    }

    private void updateRegisteredClient(RegisteredClient registeredClient) {
        log.info("Inside updateRegisteredClient Method, trying to update the given client: {}", registeredClient.getClientId());
        List<SqlParameterValue> parameters = new ArrayList<>(
                this.registeredClientParametersMapper.apply(registeredClient));
        SqlParameterValue id = parameters.removeFirst();
        parameters.removeFirst(); // remove client_id
        parameters.removeFirst(); // remove client_id_issued_at
        parameters.add(id);
        PreparedStatementSetter pss = new ArgumentPreparedStatementSetter(parameters.toArray());
        this.jdbcOperations.update(UPDATE_REGISTERED_CLIENT_SQL, pss);
        log.info("Inside updateRegisteredClient Method, Successfully updated the given client: {}", registeredClient.getClientId());
    }

    private void insertRegisteredClient(RegisteredClient registeredClient) {
        log.info("Inside insertRegisteredClient Method, trying to insert the given client: {}", registeredClient.getClientId());
        assertUniqueIdentifiers(registeredClient);
        List<SqlParameterValue> parameters = this.registeredClientParametersMapper.apply(registeredClient);
        PreparedStatementSetter pss = new ArgumentPreparedStatementSetter(parameters.toArray());
        this.jdbcOperations.update(INSERT_REGISTERED_CLIENT_SQL, pss);
        log.info("Inside insertRegisteredClient Method, Successfully inserted the given client: {}", registeredClient.getClientId());
    }

    private void assertUniqueIdentifiers(RegisteredClient registeredClient) {
        Integer count = this.jdbcOperations.queryForObject(COUNT_REGISTERED_CLIENT_SQL + "client_id = ?", Integer.class,
                registeredClient.getClientId());
        if (count != null && count > 0) {
            throw new IllegalArgumentException("Registered client must be unique. "
                    + "Found duplicate client identifier: " + registeredClient.getClientId());
        }
        if (StringUtils.hasText(registeredClient.getClientSecret())) {
            count = this.jdbcOperations.queryForObject(COUNT_REGISTERED_CLIENT_SQL + "client_secret = ?", Integer.class,
                    registeredClient.getClientSecret());
            if (count != null && count > 0) {
                throw new IllegalArgumentException("Registered client must be unique. "
                        + "Found duplicate client secret for identifier: " + registeredClient.getId());
            }
        }
    }

    @Override
    public RegisteredClient findById(String id) {
        log.info("Inside findById Method, trying to Fetch for the given Id: {}", id);
        Assert.hasText(id, "id cannot be empty");
        return findBy("id = ?", id);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        log.info("Inside findByClientId Method, trying to Fetch for the given Client Id: {}", clientId);
        Assert.hasText(clientId, "clientId cannot be empty");
        return findBy("client_id = ?", clientId);
    }

    @Override
    public List<String> getAllClientDomainNames() {
        log.info("Inside getAllClientDomainNames Method, trying to Fetch all Client Domain names");
        List<Set<String>> domainNameSetList = jdbcOperations.query("SELECT client_domain_names FROM " + TABLE_NAME,
                (rs, rowNum) -> StringUtils.commaDelimitedListToSet(rs.getString("client_domain_names"))
        );

        Set<String> allDomainNames = new HashSet<>();
        for (Set<String> domainSet : domainNameSetList) {
            allDomainNames.addAll(domainSet);
        }
        return new ArrayList<>(allDomainNames);
    }

    private RegisteredClient findBy(String filter, Object... args) {
        List<RegisteredClient> result = this.jdbcOperations.query(LOAD_REGISTERED_CLIENT_SQL + filter,
                this.registeredClientRowMapper, args);
        return !result.isEmpty() ? result.getFirst() : null;
    }

    public final void setRegisteredClientRowMapper(RowMapper<RegisteredClient> registeredClientRowMapper) {
        Assert.notNull(registeredClientRowMapper, "registeredClientRowMapper cannot be null");
        this.registeredClientRowMapper = registeredClientRowMapper;
    }

    public final void setRegisteredClientParametersMapper(
            Function<RegisteredClient, List<SqlParameterValue>> registeredClientParametersMapper) {
        Assert.notNull(registeredClientParametersMapper, "registeredClientParametersMapper cannot be null");
        this.registeredClientParametersMapper = registeredClientParametersMapper;
    }

    protected final JdbcOperations getJdbcOperations() {
        return this.jdbcOperations;
    }

    protected final RowMapper<RegisteredClient> getRegisteredClientRowMapper() {
        return this.registeredClientRowMapper;
    }

    protected final Function<RegisteredClient, List<SqlParameterValue>> getRegisteredClientParametersMapper() {
        return this.registeredClientParametersMapper;
    }

    public static class RegisteredClientRowMapper implements RowMapper<RegisteredClient> {

        private ObjectMapper objectMapper = new ObjectMapper();

        public RegisteredClientRowMapper() {
            ClassLoader classLoader = ESakshamRegisteredClientRepositoryImpl.class.getClassLoader();
            List<com.fasterxml.jackson.databind.Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
            this.objectMapper.registerModules(securityModules);
            this.objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
        }

        @Override
        public RegisteredClient mapRow(ResultSet rs, int rowNum) throws SQLException {
            Timestamp clientIdIssuedAt = rs.getTimestamp("client_id_issued_at");
            Timestamp clientSecretExpiresAt = rs.getTimestamp("client_secret_expires_at");
            Set<String> clientAuthenticationMethods = StringUtils
                    .commaDelimitedListToSet(rs.getString("client_authentication_methods"));
            Set<String> authorizationGrantTypes = StringUtils
                    .commaDelimitedListToSet(rs.getString("authorization_grant_types"));
            Set<String> redirectUris = StringUtils.commaDelimitedListToSet(rs.getString("redirect_uris"));
            Set<String> postLogoutRedirectUris = StringUtils
                    .commaDelimitedListToSet(rs.getString("post_logout_redirect_uris"));
            Set<String> clientScopes = StringUtils.commaDelimitedListToSet(rs.getString("scopes"));

            // @formatter:off
            RegisteredClient.Builder builder = RegisteredClient.withId(rs.getString("id"))
                    .clientId(rs.getString("client_id"))
                    .clientIdIssuedAt((clientIdIssuedAt != null) ? clientIdIssuedAt.toInstant() : null)
                    .clientSecret(rs.getString("client_secret"))
                    .clientSecretExpiresAt((clientSecretExpiresAt != null) ? clientSecretExpiresAt.toInstant() : null)
                    .clientName(rs.getString("client_name"))
                    .clientAuthenticationMethods((authenticationMethods) ->
                            clientAuthenticationMethods.forEach((authenticationMethod) ->
                                    authenticationMethods.add(resolveClientAuthenticationMethod(authenticationMethod))))
                    .authorizationGrantTypes((grantTypes) ->
                            authorizationGrantTypes.forEach((grantType) ->
                                    grantTypes.add(resolveAuthorizationGrantType(grantType))))
                    .redirectUris((uris) -> uris.addAll(redirectUris))
                    .postLogoutRedirectUris((uris) -> uris.addAll(postLogoutRedirectUris))
                    .scopes((scopes) -> scopes.addAll(clientScopes));
            // @formatter:on

            Map<String, Object> clientSettingsMap = parseMap(rs.getString("client_settings"));
            builder.clientSettings(ClientSettings.withSettings(clientSettingsMap).build());

            Map<String, Object> tokenSettingsMap = parseMap(rs.getString("token_settings"));
            TokenSettings.Builder tokenSettingsBuilder = TokenSettings.withSettings(tokenSettingsMap);
            if (!tokenSettingsMap.containsKey(ConfigurationSettingNames.Token.ACCESS_TOKEN_FORMAT)) {
                tokenSettingsBuilder.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED);
            }
            builder.tokenSettings(tokenSettingsBuilder.build());

            return builder.build();
        }

        public final void setObjectMapper(ObjectMapper objectMapper) {
            Assert.notNull(objectMapper, "objectMapper cannot be null");
            this.objectMapper = objectMapper;
        }

        protected final ObjectMapper getObjectMapper() {
            return this.objectMapper;
        }

        private Map<String, Object> parseMap(String data) {
            try {
                return this.objectMapper.readValue(data, new TypeReference<>() {
                });
            }
            catch (Exception ex) {
                throw new IllegalArgumentException(ex.getMessage(), ex);
            }
        }

        private static AuthorizationGrantType resolveAuthorizationGrantType(String authorizationGrantType) {
            if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(authorizationGrantType)) {
                return AuthorizationGrantType.AUTHORIZATION_CODE;
            }
            else if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(authorizationGrantType)) {
                return AuthorizationGrantType.CLIENT_CREDENTIALS;
            }
            else if (AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(authorizationGrantType)) {
                return AuthorizationGrantType.REFRESH_TOKEN;
            }
            // Custom authorization grant type
            return new AuthorizationGrantType(authorizationGrantType);
        }

        private static ClientAuthenticationMethod resolveClientAuthenticationMethod(String clientAuthenticationMethod) {
            if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue().equals(clientAuthenticationMethod)) {
                return ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
            }
            else if (ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue().equals(clientAuthenticationMethod)) {
                return ClientAuthenticationMethod.CLIENT_SECRET_POST;
            }
            else if (ClientAuthenticationMethod.NONE.getValue().equals(clientAuthenticationMethod)) {
                return ClientAuthenticationMethod.NONE;
            }
            // Custom client authentication method
            return new ClientAuthenticationMethod(clientAuthenticationMethod);
        }

    }

    public static class RegisteredClientParametersMapper implements Function<RegisteredClient, List<SqlParameterValue>> {

        private ObjectMapper objectMapper = new ObjectMapper();

        public RegisteredClientParametersMapper() {
            ClassLoader classLoader = ESakshamRegisteredClientRepositoryImpl.class.getClassLoader();
            List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
            this.objectMapper.registerModules(securityModules);
            this.objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
        }

        @Override
        public List<SqlParameterValue> apply(RegisteredClient registeredClient) {
            Timestamp clientIdIssuedAt = (registeredClient.getClientIdIssuedAt() != null)
                    ? Timestamp.from(registeredClient.getClientIdIssuedAt()) : Timestamp.from(Instant.now());

            Timestamp clientSecretExpiresAt = (registeredClient.getClientSecretExpiresAt() != null)
                    ? Timestamp.from(registeredClient.getClientSecretExpiresAt()) : null;

            List<String> clientAuthenticationMethods = new ArrayList<>(
                    registeredClient.getClientAuthenticationMethods().size());
            registeredClient.getClientAuthenticationMethods()
                    .forEach((clientAuthenticationMethod) -> clientAuthenticationMethods
                            .add(clientAuthenticationMethod.getValue()));

            List<String> authorizationGrantTypes = new ArrayList<>(
                    registeredClient.getAuthorizationGrantTypes().size());
            registeredClient.getAuthorizationGrantTypes()
                    .forEach((authorizationGrantType) -> authorizationGrantTypes.add(authorizationGrantType.getValue()));

            return Arrays.asList(new SqlParameterValue(Types.VARCHAR, registeredClient.getId()),
                    new SqlParameterValue(Types.VARCHAR, registeredClient.getClientId()),
                    new SqlParameterValue(Types.TIMESTAMP, clientIdIssuedAt),
                    new SqlParameterValue(Types.VARCHAR, registeredClient.getClientSecret()),
                    new SqlParameterValue(Types.TIMESTAMP, clientSecretExpiresAt),
                    new SqlParameterValue(Types.VARCHAR, registeredClient.getClientName()),
                    new SqlParameterValue(Types.VARCHAR,
                            StringUtils.collectionToCommaDelimitedString(clientAuthenticationMethods)),
                    new SqlParameterValue(Types.VARCHAR,
                            StringUtils.collectionToCommaDelimitedString(authorizationGrantTypes)),
                    new SqlParameterValue(Types.VARCHAR,
                            StringUtils.collectionToCommaDelimitedString(registeredClient.getRedirectUris())),
                    new SqlParameterValue(Types.VARCHAR,
                            StringUtils.collectionToCommaDelimitedString(registeredClient.getPostLogoutRedirectUris())),
                    new SqlParameterValue(Types.VARCHAR,
                            StringUtils.collectionToCommaDelimitedString(registeredClient.getScopes())),
                    new SqlParameterValue(Types.VARCHAR, writeMap(registeredClient.getClientSettings().getSettings())),
                    new SqlParameterValue(Types.VARCHAR, writeMap(registeredClient.getTokenSettings().getSettings())));
        }

        public final void setObjectMapper(ObjectMapper objectMapper) {
            Assert.notNull(objectMapper, "objectMapper cannot be null");
            this.objectMapper = objectMapper;
        }

        protected final ObjectMapper getObjectMapper() {
            return this.objectMapper;
        }

        private String writeMap(Map<String, Object> data) {
            try {
                return this.objectMapper.writeValueAsString(data);
            }
            catch (Exception ex) {
                throw new IllegalArgumentException(ex.getMessage(), ex);
            }
        }

    }

}

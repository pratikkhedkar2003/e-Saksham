package com.sarthi.e_Saksham.model.client;

import com.sarthi.e_Saksham.utils.ESakshamAuthorizationServerVersion;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

@SuppressWarnings("LombokGetterMayBeUsed")
public class ESakshamRegisteredClient implements Serializable {

    @Serial
    private static final long serialVersionUID = ESakshamAuthorizationServerVersion.SERIAL_VERSION_UID;

    private String id;

    private String clientId;

    private Instant clientIdIssuedAt;

    private String clientSecret;

    private Instant clientSecretExpiresAt;

    private String clientName;

    private Set<ClientAuthenticationMethod> clientAuthenticationMethods;

    private Set<AuthorizationGrantType> authorizationGrantTypes;

    private Set<String> redirectUris;

    private Set<String> postLogoutRedirectUris;

    private Set<String> scopes;

    private ClientSettings clientSettings;

    private TokenSettings tokenSettings;

    protected ESakshamRegisteredClient() {
    }

    public String getId() {
        return this.id;
    }

    public String getClientId() {
        return this.clientId;
    }

    @Nullable
    public Instant getClientIdIssuedAt() {
        return this.clientIdIssuedAt;
    }

    @Nullable
    public String getClientSecret() {
        return this.clientSecret;
    }

    @Nullable
    public Instant getClientSecretExpiresAt() {
        return this.clientSecretExpiresAt;
    }

    public String getClientName() {
        return this.clientName;
    }


    public Set<ClientAuthenticationMethod> getClientAuthenticationMethods() {
        return this.clientAuthenticationMethods;
    }

    public Set<AuthorizationGrantType> getAuthorizationGrantTypes() {
        return this.authorizationGrantTypes;
    }

    public Set<String> getRedirectUris() {
        return this.redirectUris;
    }

    public Set<String> getPostLogoutRedirectUris() {
        return this.postLogoutRedirectUris;
    }

    public Set<String> getScopes() {
        return this.scopes;
    }

    public ClientSettings getClientSettings() {
        return this.clientSettings;
    }

    public TokenSettings getTokenSettings() {
        return this.tokenSettings;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ESakshamRegisteredClient that = (ESakshamRegisteredClient) obj;
        return Objects.equals(this.id, that.id) && Objects.equals(this.clientId, that.clientId)
                && Objects.equals(this.clientIdIssuedAt, that.clientIdIssuedAt)
                && Objects.equals(this.clientSecret, that.clientSecret)
                && Objects.equals(this.clientSecretExpiresAt, that.clientSecretExpiresAt)
                && Objects.equals(this.clientName, that.clientName)
                && Objects.equals(this.clientAuthenticationMethods, that.clientAuthenticationMethods)
                && Objects.equals(this.authorizationGrantTypes, that.authorizationGrantTypes)
                && Objects.equals(this.redirectUris, that.redirectUris)
                && Objects.equals(this.postLogoutRedirectUris, that.postLogoutRedirectUris)
                && Objects.equals(this.scopes, that.scopes) && Objects.equals(this.clientSettings, that.clientSettings)
                && Objects.equals(this.tokenSettings, that.tokenSettings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.clientId, this.clientIdIssuedAt, this.clientSecret,
                this.clientSecretExpiresAt, this.clientName, this.clientAuthenticationMethods,
                this.authorizationGrantTypes, this.redirectUris, this.postLogoutRedirectUris, this.scopes,
                this.clientSettings, this.tokenSettings);
    }

    @Override
    public String toString() {
        return "ESakshamRegisteredClient {" + "id='" + this.id + '\'' + ", clientId='" + this.clientId + '\'' + ", clientName='"
                + this.clientName + '\'' + ", clientAuthenticationMethods=" + this.clientAuthenticationMethods
                + ", authorizationGrantTypes=" + this.authorizationGrantTypes + ", redirectUris=" + this.redirectUris
                + ", postLogoutRedirectUris=" + this.postLogoutRedirectUris + ", scopes=" + this.scopes
                + ", clientSettings=" + this.clientSettings + ", tokenSettings=" + this.tokenSettings + '}';
    }

    public static ESakshamRegisteredClient.Builder withId(String id) {
        Assert.hasText(id, "id cannot be empty");
        return new ESakshamRegisteredClient.Builder(id);
    }

    public static ESakshamRegisteredClient.Builder from(ESakshamRegisteredClient registeredClient) {
        Assert.notNull(registeredClient, "registeredClient cannot be null");
        return new ESakshamRegisteredClient.Builder(registeredClient);
    }

    public static class Builder implements Serializable {

        @Serial
        private static final long serialVersionUID = ESakshamAuthorizationServerVersion.SERIAL_VERSION_UID;

        private String id;

        private String clientId;

        private Instant clientIdIssuedAt;

        private String clientSecret;

        private Instant clientSecretExpiresAt;

        private String clientName;

        private final Set<ClientAuthenticationMethod> clientAuthenticationMethods = new HashSet<>();

        private final Set<AuthorizationGrantType> authorizationGrantTypes = new HashSet<>();

        private final Set<String> redirectUris = new HashSet<>();

        private final Set<String> postLogoutRedirectUris = new HashSet<>();

        private final Set<String> scopes = new HashSet<>();

        private ClientSettings clientSettings;

        private TokenSettings tokenSettings;

        protected Builder(String id) {
            this.id = id;
        }

        protected Builder(ESakshamRegisteredClient registeredClient) {
            this.id = registeredClient.getId();
            this.clientId = registeredClient.getClientId();
            this.clientIdIssuedAt = registeredClient.getClientIdIssuedAt();
            this.clientSecret = registeredClient.getClientSecret();
            this.clientSecretExpiresAt = registeredClient.getClientSecretExpiresAt();
            this.clientName = registeredClient.getClientName();
            if (!CollectionUtils.isEmpty(registeredClient.getClientAuthenticationMethods())) {
                this.clientAuthenticationMethods.addAll(registeredClient.getClientAuthenticationMethods());
            }
            if (!CollectionUtils.isEmpty(registeredClient.getAuthorizationGrantTypes())) {
                this.authorizationGrantTypes.addAll(registeredClient.getAuthorizationGrantTypes());
            }
            if (!CollectionUtils.isEmpty(registeredClient.getRedirectUris())) {
                this.redirectUris.addAll(registeredClient.getRedirectUris());
            }
            if (!CollectionUtils.isEmpty(registeredClient.getPostLogoutRedirectUris())) {
                this.postLogoutRedirectUris.addAll(registeredClient.getPostLogoutRedirectUris());
            }
            if (!CollectionUtils.isEmpty(registeredClient.getScopes())) {
                this.scopes.addAll(registeredClient.getScopes());
            }
            this.clientSettings = ClientSettings.withSettings(registeredClient.getClientSettings().getSettings())
                    .build();
            this.tokenSettings = TokenSettings.withSettings(registeredClient.getTokenSettings().getSettings()).build();
        }

        public ESakshamRegisteredClient.Builder id(String id) {
            this.id = id;
            return this;
        }

        public ESakshamRegisteredClient.Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public ESakshamRegisteredClient.Builder clientIdIssuedAt(Instant clientIdIssuedAt) {
            this.clientIdIssuedAt = clientIdIssuedAt;
            return this;
        }

        public ESakshamRegisteredClient.Builder clientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public ESakshamRegisteredClient.Builder clientSecretExpiresAt(Instant clientSecretExpiresAt) {
            this.clientSecretExpiresAt = clientSecretExpiresAt;
            return this;
        }

        public ESakshamRegisteredClient.Builder clientName(String clientName) {
            this.clientName = clientName;
            return this;
        }

        public ESakshamRegisteredClient.Builder clientAuthenticationMethod(ClientAuthenticationMethod clientAuthenticationMethod) {
            this.clientAuthenticationMethods.add(clientAuthenticationMethod);
            return this;
        }

        public ESakshamRegisteredClient.Builder clientAuthenticationMethods(
                Consumer<Set<ClientAuthenticationMethod>> clientAuthenticationMethodsConsumer) {
            clientAuthenticationMethodsConsumer.accept(this.clientAuthenticationMethods);
            return this;
        }

        public ESakshamRegisteredClient.Builder authorizationGrantType(AuthorizationGrantType authorizationGrantType) {
            this.authorizationGrantTypes.add(authorizationGrantType);
            return this;
        }

        public ESakshamRegisteredClient.Builder authorizationGrantTypes(Consumer<Set<AuthorizationGrantType>> authorizationGrantTypesConsumer) {
            authorizationGrantTypesConsumer.accept(this.authorizationGrantTypes);
            return this;
        }

        public ESakshamRegisteredClient.Builder redirectUri(String redirectUri) {
            this.redirectUris.add(redirectUri);
            return this;
        }

        public ESakshamRegisteredClient.Builder redirectUris(Consumer<Set<String>> redirectUrisConsumer) {
            redirectUrisConsumer.accept(this.redirectUris);
            return this;
        }

        public ESakshamRegisteredClient.Builder postLogoutRedirectUri(String postLogoutRedirectUri) {
            this.postLogoutRedirectUris.add(postLogoutRedirectUri);
            return this;
        }

        public ESakshamRegisteredClient.Builder postLogoutRedirectUris(Consumer<Set<String>> postLogoutRedirectUrisConsumer) {
            postLogoutRedirectUrisConsumer.accept(this.postLogoutRedirectUris);
            return this;
        }

        public ESakshamRegisteredClient.Builder scope(String scope) {
            this.scopes.add(scope);
            return this;
        }

        public ESakshamRegisteredClient.Builder scopes(Consumer<Set<String>> scopesConsumer) {
            scopesConsumer.accept(this.scopes);
            return this;
        }

        public ESakshamRegisteredClient.Builder clientSettings(ClientSettings clientSettings) {
            this.clientSettings = clientSettings;
            return this;
        }

        public ESakshamRegisteredClient.Builder tokenSettings(TokenSettings tokenSettings) {
            this.tokenSettings = tokenSettings;
            return this;
        }

        public ESakshamRegisteredClient build() {
            Assert.hasText(this.clientId, "clientId cannot be empty");
            Assert.notEmpty(this.authorizationGrantTypes, "authorizationGrantTypes cannot be empty");
            if (this.authorizationGrantTypes.contains(AuthorizationGrantType.AUTHORIZATION_CODE)) {
                Assert.notEmpty(this.redirectUris, "redirectUris cannot be empty");
            }
            if (!StringUtils.hasText(this.clientName)) {
                this.clientName = this.id;
            }
            if (CollectionUtils.isEmpty(this.clientAuthenticationMethods)) {
                this.clientAuthenticationMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
            }
            if (this.clientSettings == null) {
                ClientSettings.Builder builder = ClientSettings.builder();
                if (isPublicClientType()) {
                    // @formatter:off
                    builder
                            .requireProofKey(true)
                            .requireAuthorizationConsent(true);
                    // @formatter:on
                }
                this.clientSettings = builder.build();
            }
            if (this.tokenSettings == null) {
                this.tokenSettings = TokenSettings.builder().build();
            }
            validateScopes();
            validateRedirectUris();
            validatePostLogoutRedirectUris();
            return create();
        }

        private boolean isPublicClientType() {
            return this.authorizationGrantTypes.contains(AuthorizationGrantType.AUTHORIZATION_CODE)
                    && this.clientAuthenticationMethods.size() == 1
                    && this.clientAuthenticationMethods.contains(ClientAuthenticationMethod.NONE);
        }

        private ESakshamRegisteredClient create() {
            ESakshamRegisteredClient registeredClient = new ESakshamRegisteredClient();

            registeredClient.id = this.id;
            registeredClient.clientId = this.clientId;
            registeredClient.clientIdIssuedAt = this.clientIdIssuedAt;
            registeredClient.clientSecret = this.clientSecret;
            registeredClient.clientSecretExpiresAt = this.clientSecretExpiresAt;
            registeredClient.clientName = this.clientName;
            registeredClient.clientAuthenticationMethods = Collections
                    .unmodifiableSet(new HashSet<>(this.clientAuthenticationMethods));
            registeredClient.authorizationGrantTypes = Collections
                    .unmodifiableSet(new HashSet<>(this.authorizationGrantTypes));
            registeredClient.redirectUris = Collections.unmodifiableSet(new HashSet<>(this.redirectUris));
            registeredClient.postLogoutRedirectUris = Collections
                    .unmodifiableSet(new HashSet<>(this.postLogoutRedirectUris));
            registeredClient.scopes = Collections.unmodifiableSet(new HashSet<>(this.scopes));
            registeredClient.clientSettings = this.clientSettings;
            registeredClient.tokenSettings = this.tokenSettings;

            return registeredClient;
        }

        private void validateScopes() {
            if (CollectionUtils.isEmpty(this.scopes)) {
                return;
            }

            for (String scope : this.scopes) {
                Assert.isTrue(validateScope(scope), "scope \"" + scope + "\" contains invalid characters");
            }
        }

        private static boolean validateScope(String scope) {
            return scope == null || scope.chars()
                    .allMatch((c) -> withinTheRangeOf(c, 0x21, 0x21) || withinTheRangeOf(c, 0x23, 0x5B)
                            || withinTheRangeOf(c, 0x5D, 0x7E));
        }

        private static boolean withinTheRangeOf(int c, int min, int max) {
            return c >= min && c <= max;
        }

        private void validateRedirectUris() {
            if (CollectionUtils.isEmpty(this.redirectUris)) {
                return;
            }

            for (String redirectUri : this.redirectUris) {
                Assert.isTrue(validateRedirectUri(redirectUri),
                        "redirect_uri \"" + redirectUri + "\" is not a valid redirect URI or contains fragment");
            }
        }

        private void validatePostLogoutRedirectUris() {
            if (CollectionUtils.isEmpty(this.postLogoutRedirectUris)) {
                return;
            }

            for (String postLogoutRedirectUri : this.postLogoutRedirectUris) {
                Assert.isTrue(validateRedirectUri(postLogoutRedirectUri), "post_logout_redirect_uri \""
                        + postLogoutRedirectUri + "\" is not a valid post logout redirect URI or contains fragment");
            }
        }

        private static boolean validateRedirectUri(String redirectUri) {
            try {
                URI validRedirectUri = new URI(redirectUri);
                return validRedirectUri.getFragment() == null;
            }
            catch (URISyntaxException ex) {
                return false;
            }
        }

    }

}

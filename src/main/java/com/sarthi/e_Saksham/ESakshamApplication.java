package com.sarthi.e_Saksham;

import com.sarthi.e_Saksham.model.client.ESakshamRegisteredClient;
import com.sarthi.e_Saksham.repository.client.ESakshamRegisteredClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@EnableJpaAuditing
@SpringBootApplication
public class ESakshamApplication {
	private static final Logger log = LoggerFactory.getLogger(ESakshamApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ESakshamApplication.class, args);
	}

	@Bean
	public ApplicationRunner init(ESakshamRegisteredClientRepository eSakshamRegisteredClientRepository) {
		return args -> {
			if (eSakshamRegisteredClientRepository.findByClientId("e-Saksham-client") == null) {
				log.info("Inside ApplicationRunner Registering  e-Saksham-client into DB");
				try {
					ESakshamRegisteredClient eSakshamClient = ESakshamRegisteredClient.withId(UUID.randomUUID().toString())
							.clientId("e-Saksham-client")
							.clientSecret("e-Saksham-client-secret")
							.clientName("e-Saksham client")
							.clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
							.authorizationGrantTypes(authorizationGrantTypes ->
									authorizationGrantTypes.add(AuthorizationGrantType.AUTHORIZATION_CODE)
							)
							.scopes(authorizationScopes -> {
								authorizationScopes.add(OidcScopes.OPENID);
								authorizationScopes.add(OidcScopes.PROFILE);
								authorizationScopes.add(OidcScopes.EMAIL);
								authorizationScopes.add(OidcScopes.PHONE);
								authorizationScopes.add(OidcScopes.ADDRESS);
							})
							.redirectUri("http://localhost:8081/auth-success")
							.clientDomainName("http://localhost:8081")
							.postLogoutRedirectUri("http://localhost:8081/")
							.clientSettings(ClientSettings.builder()
									.requireAuthorizationConsent(false)
									.requireProofKey(true)
									.build()
							)
							.tokenSettings(TokenSettings.builder()
									.accessTokenTimeToLive(Duration.ofHours(1))
									.build()
							)
							.createdBy(0L)
							.updatedBy(0L)
							.createdAt(Instant.now())
							.updatedAt(Instant.now())
							.build();
					eSakshamRegisteredClientRepository.save(eSakshamClient);
					log.info("Registered client successfully {}", eSakshamClient.getClientId());
				} catch (Exception exception) {
					log.error("Exception while registering client INIT method {}", exception.getMessage());
				}
			}
		};
	}

}

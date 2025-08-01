package com.sarthi.e_Saksham.security.config;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.sarthi.e_Saksham.security.utils.KeyUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
public class JwtConfiguration {
    private final KeyUtils keyUtils;

    public JwtConfiguration(KeyUtils keyUtils) {
        this.keyUtils = keyUtils;
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = keyUtils.getOrGenerateRSAKeyPair();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, context) ->
                jwkSelector.select(jwkSet);
    }

    @Bean
    public JwtDecoder jwtDecoder() throws JOSEException {
        return NimbusJwtDecoder.withPublicKey(keyUtils.getOrGenerateRSAKeyPair().toRSAPublicKey()).build();
    }

}

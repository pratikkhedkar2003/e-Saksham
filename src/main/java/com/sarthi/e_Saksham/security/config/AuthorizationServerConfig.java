package com.sarthi.e_Saksham.security.config;

import com.sarthi.e_Saksham.security.constant.ESakshamAuthConstant;
import com.sarthi.e_Saksham.security.filter.LoggedInUserFilter;
import com.sarthi.e_Saksham.security.generator.ESakshamJwtGenerator;
import com.sarthi.e_Saksham.security.handler.LoginFailureHandler;
import com.sarthi.e_Saksham.security.handler.MfaAuthenticationSuccessHandler;
import com.sarthi.e_Saksham.security.utils.ESakshamAuthUtils;
import com.sarthi.e_Saksham.service.client.ESakshamRegisteredClientService;
import com.sarthi.e_Saksham.service.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static java.util.Arrays.asList;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.ORIGIN;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

@Configuration
@EnableWebSecurity
public class AuthorizationServerConfig {
    private final UserService userService;
    private final JwtConfiguration jwtConfiguration;
    private final LoggedInUserFilter loggedInUserFilter;
    private final ESakshamRegisteredClientService eSakshamRegisteredClientService;

    public AuthorizationServerConfig(UserService userService, JwtConfiguration jwtConfiguration, LoggedInUserFilter loggedInUserFilter, ESakshamRegisteredClientService eSakshamRegisteredClientService) {
        this.userService = userService;
        this.jwtConfiguration = jwtConfiguration;
        this.loggedInUserFilter = loggedInUserFilter;
        this.eSakshamRegisteredClientService = eSakshamRegisteredClientService;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity httpSecurity, RegisteredClientRepository registeredClientRepository) throws Exception {

        httpSecurity.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()));
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = OAuth2AuthorizationServerConfigurer.authorizationServer();

        httpSecurity
                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .with(authorizationServerConfigurer, (authorizationServerConfig) -> authorizationServerConfig
                        .tokenGenerator(tokenGenerator())
                        .oidc(Customizer.withDefaults()) // Enable OpenID Connect 1.0
                )
                .authorizeHttpRequests((authorizeHttpRequest) -> authorizeHttpRequest
                        .anyRequest().authenticated()
                )
                .addFilterAfter(loggedInUserFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling((httpSecurityExceptionHandlingConfigurer) -> httpSecurityExceptionHandlingConfigurer
                        .accessDeniedPage("/access-denied")
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                )
        ;

        return httpSecurity.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()));

        httpSecurity
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login", "/logout", "/css/**", "/images/**").permitAll()
                        .requestMatchers(POST, "/logout").permitAll()
                        .requestMatchers("/mfa").hasAuthority(ESakshamAuthConstant.MFA_AUTHORITY)
                        .anyRequest().authenticated()
                )
                .addFilterAfter(loggedInUserFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
                        .loginPage("/login")
                        .successHandler(new MfaAuthenticationSuccessHandler("/mfa", ESakshamAuthConstant.MFA_AUTHORITY, userService))
                        .failureHandler(new LoginFailureHandler("/login?error"))
                )
                .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
                        .logoutUrl("/logout")
                        .logoutSuccessUrl(ESakshamAuthConstant.APPLICATION_BASE_URL)
                        .addLogoutHandler(new CookieClearingLogoutHandler(ESakshamAuthConstant.JSESSIONID))
                        .invalidateHttpSession(true)
                        .deleteCookies(ESakshamAuthConstant.JSESSIONID)
                        .clearAuthentication(true)
                )
        ;

        return httpSecurity.build();
    }

    @Bean
    public OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator() {
        ESakshamJwtGenerator eSakshamJwtGenerator = ESakshamJwtGenerator.init(new NimbusJwtEncoder(this.jwtConfiguration.jwkSource()));
        eSakshamJwtGenerator.setJwtCustomizer(tokenCustomizer());
        return new DelegatingOAuth2TokenGenerator(eSakshamJwtGenerator);
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return context -> {
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                context.getClaims().claims(claims -> {
                        claims.putIfAbsent(ESakshamAuthConstant.AUTHORITIES_KEY, ESakshamAuthUtils.getUserAuthorities(context));
                        // claims.putIfAbsent(ESakshamAuthConstant.CLIENT_ID, context.getRegisteredClient().getClientId());
                    }
                );
            }
        };
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().issuer(ESakshamAuthConstant.ISSUER).build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(this.eSakshamRegisteredClientService.getAllClientDomainNames());
        corsConfiguration.setAllowedHeaders(asList(ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN, CONTENT_TYPE, ACCEPT, AUTHORIZATION, "X_REQUESTED_WITH", ACCESS_CONTROL_REQUEST_METHOD, ACCESS_CONTROL_REQUEST_HEADERS, ACCESS_CONTROL_ALLOW_CREDENTIALS, "File-Name"));
        corsConfiguration.setExposedHeaders(asList(ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN, CONTENT_TYPE, ACCEPT, AUTHORIZATION, "X_REQUESTED_WITH", ACCESS_CONTROL_REQUEST_METHOD, ACCESS_CONTROL_REQUEST_HEADERS, ACCESS_CONTROL_ALLOW_CREDENTIALS, "File-Name"));
        corsConfiguration.setAllowedMethods(asList(GET.name(), POST.name(), PUT.name(), PATCH.name(), DELETE.name(), OPTIONS.name()));
        corsConfiguration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

}

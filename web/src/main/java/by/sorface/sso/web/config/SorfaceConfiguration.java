package by.sorface.sso.web.config;

import by.sorface.sso.web.config.properties.CookieOptions;
import by.sorface.sso.web.config.properties.CorsOptions;
import by.sorface.sso.web.config.resolvers.HttpI18LocaleResolver;
import by.sorface.sso.web.utils.json.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.LocaleResolver;

import java.nio.charset.StandardCharsets;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties
public class SorfaceConfiguration {

    public static final String I18_BUNDLE_LOCATION = "language/messages";

    private final CorsOptions corsOptions;

    private final CookieOptions cookieOptions;

    @Bean
    public FilterRegistrationBean<CorsFilter> corsConfigurationSource() {
        log.debug("cors sorface filter");

        final var source = new UrlBasedCorsConfigurationSource();

        corsOptions.getOptions().forEach(configProps -> {
            final var config = new CorsConfiguration();

            config.setAllowCredentials(configProps.isAllowCredentials());
            config.addAllowedOrigin(configProps.getAllowedOrigins());
            config.addAllowedOriginPattern(configProps.getAllowedOriginPatterns());
            config.addAllowedHeader(configProps.getAllowedHeaders());
            config.addExposedHeader(configProps.getExposedHeaders());
            config.addAllowedMethod(configProps.getAllowedMethods());

            log.debug("{}{}", System.lineSeparator(), Json.lazyStringify(config));

            source.registerCorsConfiguration(configProps.getPattern(), config);
        });

        final var bean = new FilterRegistrationBean<>(new CorsFilter(source));
        {
            bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        }

        return bean;
    }

    @Bean
    public CookieSerializer cookieSerializer() {
        final var serializer = new DefaultCookieSerializer();
        {
            serializer.setCookieName(cookieOptions.getSession().getName());
            serializer.setCookiePath(cookieOptions.getSession().getPath());
            serializer.setDomainNamePattern(cookieOptions.getSession().getDomainPattern());
            serializer.setUseHttpOnlyCookie(cookieOptions.getSession().isHttpOnly());
        }

        return serializer;
    }

    /**
     * Creating a password encoding component
     *
     * @return the password encoding component
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public ResourceBundleMessageSource resourceBundleMessageSource() {
        final var source = new ResourceBundleMessageSource();
        {
            source.setBasename(I18_BUNDLE_LOCATION);
            source.setDefaultEncoding(StandardCharsets.UTF_8.name());
        }

        return source;
    }

    @Bean
    public UserAgentAnalyzer userAgentAnalyzer() {
        return UserAgentAnalyzer.newBuilder().hideMatcherLoadStats().withCache(10000).build();
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new HttpI18LocaleResolver();
    }
}

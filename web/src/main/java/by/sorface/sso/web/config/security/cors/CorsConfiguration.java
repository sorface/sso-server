package by.sorface.sso.web.config.security.cors;

import by.sorface.sso.web.config.options.CorsOptions;
import by.sorface.sso.web.utils.json.Json;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Slf4j
@Configuration
public class CorsConfiguration {

    @Bean
    public FilterRegistrationBean<CorsFilter> corsConfigurationSource(final CorsOptions corsOptions) {
        log.debug("start configure cors filter");

        final var source = new UrlBasedCorsConfigurationSource();

        corsOptions.getOptions().forEach(configProps -> {
            final var config = new org.springframework.web.cors.CorsConfiguration();

            config.setAllowCredentials(configProps.isAllowCredentials());
            config.addAllowedOrigin(configProps.getAllowedOrigins());
            config.addAllowedOriginPattern(configProps.getAllowedOriginPatterns());
            config.addAllowedHeader(configProps.getAllowedHeaders());
            config.addExposedHeader(configProps.getExposedHeaders());
            config.addAllowedMethod(configProps.getAllowedMethods());

            log.debug("config cors -> {}{}", System.lineSeparator(), Json.lazyStringify(config));

            source.registerCorsConfiguration(configProps.getPattern(), config);
        });

        final var bean = new FilterRegistrationBean<>(new CorsFilter(source));
        {
            bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        }

        return bean;
    }


}

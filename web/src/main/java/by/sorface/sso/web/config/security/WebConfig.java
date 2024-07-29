package by.sorface.sso.web.config.security;

import by.sorface.sso.web.config.options.CorsOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final CorsOptions corsOptions;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        corsOptions.getOptions().forEach(it -> {
            registry.addMapping(it.getPattern())
                    .allowedOrigins(it.getAllowedOrigins())
                    .allowedMethods(it.getAllowedMethods())
                    .allowedHeaders(it.getAllowedHeaders())
                    .exposedHeaders(it.getExposedHeaders())
                    .allowCredentials(it.isAllowCredentials())
                    .maxAge(it.getMaxAge());
        });

    }


}

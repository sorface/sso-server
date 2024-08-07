package by.sorface.sso.web.config;

import brave.sampler.Sampler;
import by.sorface.sso.web.config.locale.resolvers.HttpI18LocaleResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;

import java.nio.charset.StandardCharsets;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties
public class SorfaceConfiguration {

    public static final String I18_BUNDLE_LOCATION = "language/messages";

    /**
     * The resourceBundleMessageSource function creates a ResourceBundleMessageSource bean.
     * This bean is used to resolve messages in the application, such as error messages.
     * The resource bundle location is set to &quot;i18n/messages&quot;.
     *
     * @return A resourcebundlemessagesource object
     */
    @Bean
    public ResourceBundleMessageSource resourceBundleMessageSource() {
        final var source = new ResourceBundleMessageSource();
        {
            source.setBasename(I18_BUNDLE_LOCATION);
            source.setDefaultEncoding(StandardCharsets.UTF_8.name());
        }

        return source;
    }

    /**
     * The userAgentAnalyzer function creates a new UserAgentAnalyzer object.
     * The UserAgentAnalyzer class is part of the ua-parser library, which parses user agent strings into their component parts.
     * <p>
     * This function uses the builder pattern to create a new instance of this class with some custom settings:
     * - hideMatcherLoadStats() hides log messages that are printed when the parser loads its data files (which happens on startup).
     * - withCache(10000) sets up an in-memory cache for parsed user agents, so that parsing doesn't have to happen every time.
     *
     * @return A useragentanalyzer object
     */
    @Bean
    public UserAgentAnalyzer userAgentAnalyzer() {
        return UserAgentAnalyzer.newBuilder().hideMatcherLoadStats().withCache(10000).build();
    }

    /**
     * The localeResolver function is a bean that returns an instance of the HttpI18LocaleResolver class.
     * This class implements the LocaleResolver interface, which provides methods for resolving a locale from
     * a request and setting it in response. The HttpI18LocaleResolver class resolves locales based on HTTP requests,
     * using either cookies or session attributes to store them between requests. It also supports changing the locale
     * by adding a parameter to URLs (e.g., <a href="http://localhost:8080/index?lang=en">...</a>). In this case, we are not configuring
     * any
     *
     * @return A httpi18localeresolver object
     */
    @Bean
    public LocaleResolver localeResolver() {
        return new HttpI18LocaleResolver();
    }

    @Bean
    public Sampler defaultSampler() {
        return Sampler.ALWAYS_SAMPLE;
    }
}

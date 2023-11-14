package by.sorface.ssoserver.config.handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

import java.io.IOException;

public interface IntrospectionWriter {

    void write(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException;

}

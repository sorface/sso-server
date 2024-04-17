package by.sorface.sso.web.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class CsrfControllerTest {

    @Mock
    private CsrfTokenRepository csrfTokenRepository;

    @InjectMocks
    private CsrfController csrfController;

    @Test
    public void setupCsrf() {
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = Mockito.mock(HttpServletResponse.class);

        final CsrfToken defaultCsrfToken = new DefaultCsrfToken("XSRF-TOKEN", "_csrf", UUID.randomUUID().toString());

        Mockito.when(csrfTokenRepository.generateToken(httpServletRequest)).thenReturn(defaultCsrfToken);
        Mockito.doNothing().when(csrfTokenRepository).saveToken(defaultCsrfToken, httpServletRequest, httpServletResponse);

        final CsrfToken csrfToken = csrfController.setupCsrf(httpServletRequest, httpServletResponse);

        Mockito.verify(csrfTokenRepository, Mockito.times(1)).generateToken(httpServletRequest);
        Mockito.verify(csrfTokenRepository, Mockito.times(1)).saveToken(Mockito.eq(defaultCsrfToken), Mockito.eq(httpServletRequest), Mockito.eq(httpServletResponse));

        Assertions.assertEquals(defaultCsrfToken.getToken(), csrfToken.getToken());
        Assertions.assertEquals(defaultCsrfToken.getHeaderName(), csrfToken.getHeaderName());
        Assertions.assertEquals(defaultCsrfToken.getParameterName(), csrfToken.getParameterName());
    }
}
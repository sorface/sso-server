package by.sorface.sso.web.constants;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OAuthProviderTest {

    @Test
    void findByName() {
        OAuthProvider github = OAuthProvider.findByName("GITHUB");

        Assertions.assertEquals(OAuthProvider.GITHUB, github);
    }
}
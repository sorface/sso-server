package by.sorface.sso.web.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FrontendUrlPattern {

    PAGE_SIGNIN("/account/signin"),

    PAGE_SIGNUP("/account/signup"),

    PAGE_PROFILE("/account"),

    ;

    private final String endpoint;

}

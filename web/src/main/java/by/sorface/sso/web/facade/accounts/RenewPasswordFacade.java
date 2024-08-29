package by.sorface.sso.web.facade.accounts;

import by.sorface.sso.web.records.tokens.ApplyNewPasswordRequest;

public interface RenewPasswordFacade {

    void forgetPassword(final String email);

    void applyNewPassword(final ApplyNewPasswordRequest request);

    void checkRenewPasswordToken(final String hash);

}

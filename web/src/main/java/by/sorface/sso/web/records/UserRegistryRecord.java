package by.sorface.sso.web.records;

import lombok.Data;

/**
 * application for user registration
 */
@Data
public class UserRegistryRecord {

    /**
     * user firstname
     */
    private String firstName;

    /**
     * user's lastname
     */
    private String lastName;

    /**
     * user's middleName
     */
    private String middleName;

    /**
     * user's email
     */
    private String email;

    /**
     * user's login in the system
     */
    private String username;

    /**
     * user's password in the system
     */
    private String password;

}

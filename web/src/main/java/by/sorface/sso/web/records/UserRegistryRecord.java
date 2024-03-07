package by.sorface.sso.web.records;

/**
 * application for user registration
 */
public record UserRegistryRecord(
        /*
          user firstname
         */
        String firstName,

        /*
          user's lastname
         */
        String lastName,

        /*
          user's middleName
         */
        String middleName,

        /*
          user's email
         */
        String email,

        /*
          user's login in the system
         */
        String username,

        /*
          user's password in the system
         */
        String password) {
}

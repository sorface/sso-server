package by.sorface.ssoserver.records;

import lombok.Data;

@Data
public class UserRecord {

    private String firstName;

    private String lastName;

    private String middleName;

    private String email;

    private String username;

    private String password;

}
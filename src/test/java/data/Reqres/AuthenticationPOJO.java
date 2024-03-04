package data.Reqres;

import lombok.Data;

@Data
public class AuthenticationPOJO {
    String email;
    String password;

    public AuthenticationPOJO(String email, String password) {
        this.email = email;
        this.password = password;
    }
}

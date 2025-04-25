package auth.application.port.in.request;

import com.jnulocker.auth.application.port.in.request.LoginRequest;

public class LoginRequestTestDataBuilder {
    private String email = "222222@jnu.ac.kr";
    private String password = "abcde12345!";

    private LoginRequestTestDataBuilder() {}

    public static LoginRequestTestDataBuilder loginRequestBuilder() {
        return new LoginRequestTestDataBuilder();
    }

    public LoginRequestTestDataBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public LoginRequestTestDataBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public LoginRequest build() {
        return new LoginRequest(email, password);
    }
}

package auth.application.port.in.request;

import com.jnulocker.auth.application.port.in.request.UserSignupRequest;

public class UserSignupRequestTestDataBuilder {
    private String name = "서영우";
    private String email = "222222@jnu.ac.kr";
    private String password = "abcde12345!";
    private Long departmentId = 1L;
    private String phoneNumber = "010-1234-1234";

    private UserSignupRequestTestDataBuilder() {}

    public static UserSignupRequestTestDataBuilder userSignupRequestBuilder() {
        return new UserSignupRequestTestDataBuilder();
    }

    public UserSignupRequestTestDataBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public UserSignupRequestTestDataBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserSignupRequestTestDataBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserSignupRequestTestDataBuilder withDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
        return this;
    }

    public UserSignupRequestTestDataBuilder withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public UserSignupRequest build() {
        return new UserSignupRequest(name, email, password, departmentId, phoneNumber);
    }
}

package auth.application.port.in.request;

import com.jnulocker.auth.application.port.in.request.ManagerSignupRequest;

public class ManagerSignupRequestTestDataBuilder {
    private String nickName = "디자인학과 학생회 아트";
    private String email = "design@gmail.com";
    private String password = "abcde12345!";
    private Long departmentId = 1L;
    private String phoneNumber = "010-1234-1234";

    private ManagerSignupRequestTestDataBuilder() {}

    public static ManagerSignupRequestTestDataBuilder managerSignupRequestBuilder() {
        return new ManagerSignupRequestTestDataBuilder();
    }

    public ManagerSignupRequestTestDataBuilder withNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public ManagerSignupRequestTestDataBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public ManagerSignupRequestTestDataBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public ManagerSignupRequestTestDataBuilder withDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
        return this;
    }

    public ManagerSignupRequestTestDataBuilder withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public ManagerSignupRequest build() {
        return new ManagerSignupRequest(nickName, email, password, departmentId, phoneNumber);
    }
}

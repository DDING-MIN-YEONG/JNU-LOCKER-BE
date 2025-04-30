package auth.application.port.in.request;

import com.jnulocker.auth.application.port.in.request.VerifyCodeRequest;

public class VerifyCodeRequestTestDataBuilder {
    private String email = "test@example.com";
    private String code = "123456";

    private VerifyCodeRequestTestDataBuilder() {}

    public static VerifyCodeRequestTestDataBuilder verifyCodeRequestBuilder() {
        return new VerifyCodeRequestTestDataBuilder();
    }

    public VerifyCodeRequestTestDataBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public VerifyCodeRequestTestDataBuilder withCode(String code) {
        this.code = code;
        return this;
    }

    public VerifyCodeRequest build() {
        return new VerifyCodeRequest(email, code);
    }
}

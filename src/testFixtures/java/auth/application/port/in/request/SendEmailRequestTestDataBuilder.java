package auth.application.port.in.request;

import com.jnulocker.auth.application.port.in.request.SendEmailRequest;

public class SendEmailRequestTestDataBuilder {
    private String email = "test@example.com";

    private SendEmailRequestTestDataBuilder() {}

    public static SendEmailRequestTestDataBuilder sendEmailRequestBuilder() {
        return new SendEmailRequestTestDataBuilder();
    }

    public SendEmailRequestTestDataBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public SendEmailRequest build() {
        return new SendEmailRequest(email);
    }
}

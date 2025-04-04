package organization.builder;

import com.jnulocker.organization.domain.Organization;

public class OrganizationTestDataBuilder {

    private String email = "test@example.com";
    private String phoneNumber = "010-1234-5678";
    private String affiliation = "테스트 소속";
    private String department = "테스트 학과";
    private String name = "테스트 조직명";

    private OrganizationTestDataBuilder() {}

    public static OrganizationTestDataBuilder builder() {
        return new OrganizationTestDataBuilder();
    }

    public OrganizationTestDataBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public OrganizationTestDataBuilder withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public OrganizationTestDataBuilder withAffiliation(String affiliation) {
        this.affiliation = affiliation;
        return this;
    }

    public OrganizationTestDataBuilder withDepartment(String department) {
        this.department = department;
        return this;
    }

    public OrganizationTestDataBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public Organization build() {
        return Organization.create(email, phoneNumber, affiliation, department, name);
    }
}

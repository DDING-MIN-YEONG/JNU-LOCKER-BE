package organization.builder;

import com.jnulocker.organization.domain.Department;
import com.jnulocker.organization.domain.Organization;

public class DepartmentTestDataBuilder {

    private Organization organization = OrganizationTestDataBuilder.builder().build();
    private String name = "테스트 학과명";
    private String nickname = "테스트 학과 학생회 별칭";
    private String email = "test@test.com";
    private String phoneNumber = "010-1234-5678";

    private DepartmentTestDataBuilder() {}

    public static DepartmentTestDataBuilder builder() {
        return new DepartmentTestDataBuilder();
    }

    public DepartmentTestDataBuilder withOrganization(Organization organization) {
        this.organization = organization;
        return this;
    }

    public DepartmentTestDataBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public DepartmentTestDataBuilder withNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public DepartmentTestDataBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public DepartmentTestDataBuilder withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public Department build() {
        return Department.create(name, nickname, email, phoneNumber, organization);
    }
}

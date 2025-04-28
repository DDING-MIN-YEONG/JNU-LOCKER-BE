package organization.domain;

import com.jnulocker.organization.domain.Organization;
import com.jnulocker.organization.domain.OrganizationType;

public class OrganizationTestDataBuilder {

    private String name = "테스트 조직명";
    private OrganizationType type = OrganizationType.COUNCIL;

    private OrganizationTestDataBuilder() {}

    public static OrganizationTestDataBuilder organizationBuilder() {
        return new OrganizationTestDataBuilder();
    }

    public OrganizationTestDataBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public OrganizationTestDataBuilder withType(OrganizationType type) {
        this.type = type;
        return this;
    }

    public Organization build() {
        return Organization.create(name, type);
    }
}

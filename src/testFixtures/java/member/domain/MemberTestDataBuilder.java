package member.domain;

import static organization.domain.DepartmentTestDataBuilder.departmentBuilder;

import com.jnulocker.member.domain.Member;
import com.jnulocker.organization.domain.Department;

public class MemberTestDataBuilder {

    private String name = "테스트 이름";
    private String email = "test@email.com";
    private String phoneNumber = "010-1234-5678";
    private String password = "password123";
    private String studentNumber = "221965";
    private Department department = departmentBuilder().build();

    private MemberTestDataBuilder() {}

    public static MemberTestDataBuilder memberBuilder() {
        return new MemberTestDataBuilder();
    }

    public MemberTestDataBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public MemberTestDataBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public MemberTestDataBuilder withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public MemberTestDataBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public MemberTestDataBuilder withDepartment(Department department) {
        this.department = department;
        return this;
    }

    public Member buildUser() {
        return Member.createUser(name, email, phoneNumber, password, studentNumber, department);
    }

    public Member buildManager() {
        return Member.createManager(name, email, phoneNumber, password, department);
    }
}

package announce.domain;

import static organization.domain.DepartmentTestDataBuilder.departmentBuilder;

import com.jnulocker.announce.domain.Announce;
import com.jnulocker.organization.domain.Department;

public class AnnounceTestDataBuilder {

    private String title = "테스트 공지사항";
    private String content = "테스트 공지사항 내용";
    private Department department = departmentBuilder().build();
    private String writer = department.getNickname();

    private AnnounceTestDataBuilder() {}

    public static AnnounceTestDataBuilder announceBuilder() {
        return new AnnounceTestDataBuilder();
    }

    public AnnounceTestDataBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public AnnounceTestDataBuilder withContent(String content) {
        this.content = content;
        return this;
    }

    public AnnounceTestDataBuilder withDepartment(Department department) {
        this.department = department;
        return this;
    }

    public AnnounceTestDataBuilder withWriter(String writer) {
        this.writer = writer;
        return this;
    }

    public Announce build() {
        return Announce.create(title, content, writer, department);
    }
}

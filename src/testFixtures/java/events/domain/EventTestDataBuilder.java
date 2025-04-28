package events.domain;

import static organization.domain.DepartmentTestDataBuilder.*;

import com.jnulocker.events.domain.Event;
import com.jnulocker.organization.domain.Department;
import java.time.LocalDateTime;

public class EventTestDataBuilder {

    private String title = "테스트 이벤트";
    private Department department = departmentBuilder().build();
    private LocalDateTime startAt = LocalDateTime.now();
    private LocalDateTime endAt = LocalDateTime.now().plusHours(1);

    private EventTestDataBuilder() {}

    public static EventTestDataBuilder eventBuilder() {
        return new EventTestDataBuilder();
    }

    public EventTestDataBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public EventTestDataBuilder withDepartment(Department department) {
        this.department = department;
        return this;
    }

    public EventTestDataBuilder withStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
        return this;
    }

    public EventTestDataBuilder withEndAt(LocalDateTime endAt) {
        this.endAt = endAt;
        return this;
    }

    public Event build() {
        return Event.create(title, department, startAt, endAt);
    }
}

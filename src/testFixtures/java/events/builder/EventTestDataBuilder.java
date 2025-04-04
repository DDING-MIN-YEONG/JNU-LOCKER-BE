package events.builder;

import com.jnulocker.events.domain.Event;
import com.jnulocker.events.domain.EventStatus;
import com.jnulocker.organization.domain.Organization;
import java.time.LocalDateTime;
import organization.builder.OrganizationTestDataBuilder;

public class EventTestDataBuilder {

    private String title = "테스트 이벤트";
    private Organization organization = OrganizationTestDataBuilder.builder().build();
    private LocalDateTime startAt = LocalDateTime.now();
    private LocalDateTime endAt = LocalDateTime.now().plusHours(1);
    private EventStatus eventStatus = EventStatus.OPEN;
    private Boolean publish = true;

    public static EventTestDataBuilder builder() {
        return new EventTestDataBuilder();
    }

    public EventTestDataBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public EventTestDataBuilder withOrganization(Organization organization) {
        this.organization = organization;
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

    public EventTestDataBuilder withEventStatus(EventStatus eventStatus) {
        this.eventStatus = eventStatus;
        return this;
    }

    public EventTestDataBuilder withPublish(Boolean publish) {
        this.publish = publish;
        return this;
    }

    public Event build() {
        return Event.create(title, organization, startAt, endAt, eventStatus, publish);
    }
}

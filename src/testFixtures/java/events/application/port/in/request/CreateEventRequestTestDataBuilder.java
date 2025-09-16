package events.application.port.in.request;

import com.jnulocker.events.application.port.in.request.CreateEventRequest;
import com.jnulocker.events.application.port.in.request.FloorInfo;
import java.time.LocalDateTime;
import java.util.List;

public class CreateEventRequestTestDataBuilder {
    private String title = "전자컴퓨터공학부 2025-2 사물함 신청";
    private LocalDateTime startAt = LocalDateTime.now().plusHours(1);
    private LocalDateTime endAt = LocalDateTime.now().plusHours(2);
    private List<Long> participationDepartmentIds = List.of(1L, 2L, 3L);
    private List<FloorInfo> floors = List.of(FloorInfoTestDataBuilder.floorInfoBuilder().build());

    private CreateEventRequestTestDataBuilder() {}

    public static CreateEventRequestTestDataBuilder createEventRequestBuilder() {
        return new CreateEventRequestTestDataBuilder();
    }

    public CreateEventRequestTestDataBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public CreateEventRequestTestDataBuilder withStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
        return this;
    }

    public CreateEventRequestTestDataBuilder withEndAt(LocalDateTime endAt) {
        this.endAt = endAt;
        return this;
    }

    public CreateEventRequestTestDataBuilder withParticipationDepartmentIds(
            List<Long> participationDepartmentIds) {
        this.participationDepartmentIds = participationDepartmentIds;
        return this;
    }

    public CreateEventRequestTestDataBuilder withFloors(List<FloorInfo> floors) {
        this.floors = floors;
        return this;
    }

    public CreateEventRequest build() {
        return new CreateEventRequest(title, startAt, endAt, participationDepartmentIds, floors);
    }
}

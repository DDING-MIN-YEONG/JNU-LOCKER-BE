package events.application.port.in.request;

import com.jnulocker.events.application.port.in.request.FloorInfo;
import com.jnulocker.events.application.port.in.request.UpdateEventRequest;
import java.time.LocalDateTime;
import java.util.List;

public class UpdateEventRequestTestDataBuilder {
    private String title = "전자컴퓨터공학부 2025-2 사물함 신청 (수정됨)";
    private LocalDateTime startAt = LocalDateTime.of(2025, 8, 2, 15, 0);
    private LocalDateTime endAt = LocalDateTime.of(2025, 8, 2, 16, 0);
    private List<Long> participationDepartmentIds = List.of(1L, 2L, 3L);
    private List<FloorInfo> floors = List.of(FloorInfoTestDataBuilder.floorInfoBuilder().build());

    private UpdateEventRequestTestDataBuilder() {}

    public static UpdateEventRequestTestDataBuilder updateEventRequestBuilder() {
        return new UpdateEventRequestTestDataBuilder();
    }

    public UpdateEventRequestTestDataBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public UpdateEventRequestTestDataBuilder withStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
        return this;
    }

    public UpdateEventRequestTestDataBuilder withEndAt(LocalDateTime endAt) {
        this.endAt = endAt;
        return this;
    }

    public UpdateEventRequestTestDataBuilder withParticipationDepartmentIds(
            List<Long> participationDepartmentIds) {
        this.participationDepartmentIds = participationDepartmentIds;
        return this;
    }

    public UpdateEventRequestTestDataBuilder withFloors(List<FloorInfo> floors) {
        this.floors = floors;
        return this;
    }

    public UpdateEventRequest build() {
        return new UpdateEventRequest(title, startAt, endAt, participationDepartmentIds, floors);
    }
}

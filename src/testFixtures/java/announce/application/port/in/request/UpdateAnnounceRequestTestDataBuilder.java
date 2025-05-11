package announce.application.port.in.request;

import com.jnulocker.announce.application.port.in.request.UpdateAnnounceRequest;
import java.util.List;

public class UpdateAnnounceRequestTestDataBuilder {
    private String title = "전자컴퓨터공학부 사물함 신청 안내";
    private String content = "수정된 공지사항 내용입니다.";
    private List<Long> participationDepartmentIds = List.of(4L, 5L);

    private UpdateAnnounceRequestTestDataBuilder() {}

    public static UpdateAnnounceRequestTestDataBuilder updateAnnounceRequestBuilder() {
        return new UpdateAnnounceRequestTestDataBuilder();
    }

    public UpdateAnnounceRequestTestDataBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public UpdateAnnounceRequestTestDataBuilder withContent(String content) {
        this.content = content;
        return this;
    }

    public UpdateAnnounceRequestTestDataBuilder withParticipationDepartmentIds(
            List<Long> participationDepartmentIds) {
        this.participationDepartmentIds = participationDepartmentIds;
        return this;
    }

    public UpdateAnnounceRequest build() {
        return new UpdateAnnounceRequest(title, content, participationDepartmentIds);
    }
}

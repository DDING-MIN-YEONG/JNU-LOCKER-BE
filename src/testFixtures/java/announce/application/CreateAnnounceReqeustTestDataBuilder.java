package announce.application;

import com.jnulocker.announce.application.port.in.request.CreateAnnounceRequest;
import java.util.List;

public class CreateAnnounceReqeustTestDataBuilder {
    private String title = "백도 사물함 신청 안내";
    private String content = "공지사항 내용입니다.";
    private List<Long> participationDepartmentIds = List.of(1L, 2L, 3L);

    private CreateAnnounceReqeustTestDataBuilder() {}

    public static CreateAnnounceReqeustTestDataBuilder createAnnounceReqeustBuilder() {
        return new CreateAnnounceReqeustTestDataBuilder();
    }

    public CreateAnnounceReqeustTestDataBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public CreateAnnounceReqeustTestDataBuilder withContent(String content) {
        this.content = content;
        return this;
    }

    public CreateAnnounceReqeustTestDataBuilder withParticipationDepartmentIds(
            List<Long> participationDepartmentIds) {
        this.participationDepartmentIds = participationDepartmentIds;
        return this;
    }

    public CreateAnnounceRequest build() {
        return new CreateAnnounceRequest(title, content, participationDepartmentIds);
    }
}

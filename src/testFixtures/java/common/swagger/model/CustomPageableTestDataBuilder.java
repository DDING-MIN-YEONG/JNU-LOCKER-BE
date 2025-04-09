package common.swagger.model;

import com.jnulocker.common.swagger.model.AbstractPageable;
import com.jnulocker.events.application.port.in.response.EventPageable;

public class CustomPageableTestDataBuilder {
    private Integer page = 0;
    private Integer size = 10;
    private String direction = "asc";
    private String sort = "createdAt";

    private CustomPageableTestDataBuilder() {}

    public static CustomPageableTestDataBuilder pageableBuilder() {
        return new CustomPageableTestDataBuilder();
    }

    public CustomPageableTestDataBuilder withPage(Integer page) {
        this.page = page;
        return this;
    }

    public CustomPageableTestDataBuilder withSize(Integer size) {
        this.size = size;
        return this;
    }

    public CustomPageableTestDataBuilder withDirection(String direction) {
        this.direction = direction;
        return this;
    }

    public CustomPageableTestDataBuilder withSort(String sort) {
        this.sort = sort;
        return this;
    }

    // AbstractPageable을 상속받은 테스트용 Pageable 객체를 생성하는 메서드
    public AbstractPageable buildTestPageable() {
        return new AbstractPageable(page, size, direction, sort) {
            @Override
            protected boolean isValidSort() {
                return true;
            }
        };
    }

    // Event 도메인에 맞는 Pageable 객체를 생성하는 메서드
    public EventPageable buildEventPageable() {
        return new EventPageable(page, size, direction, sort);
    }
}

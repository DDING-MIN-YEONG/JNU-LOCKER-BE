package com.jnulocker.events.infrastructure.querydsl;

import static com.jnulocker.events.domain.QEvent.event;

import com.jnulocker.common.persistence.querydsl.PathProvider;
import com.querydsl.core.types.Path;
import java.util.HashMap;
import java.util.Map;

/** Event 엔티티의 Path 매핑을 제공하는 클래스 싱글톤 패턴으로 구현하여 인스턴스 생성 비용을 줄입니다. */
public class EventPathMap implements PathProvider {

    private static final EventPathMap INSTANCE = new EventPathMap();
    private static final Map<String, Path<?>> pathMap = new HashMap<>();

    static {
        pathMap.put("id", event.id);
        pathMap.put("title", event.title);
        pathMap.put("publish", event.publish);
        pathMap.put("eventStatus", event.eventStatus);
        pathMap.put("eventSchedule.startAt", event.eventSchedule.startAt);
        pathMap.put("eventSchedule.endAt", event.eventSchedule.endAt);
        pathMap.put("createdAt", event.createdAt);
        pathMap.put("updatedAt", event.updatedAt);
    }

    private EventPathMap() {
        // 싱글톤 패턴을 위한 private 생성자
    }

    /**
     * EventPathMap의 싱글톤 인스턴스를 반환합니다.
     *
     * @return EventPathMap 인스턴스
     */
    public static EventPathMap getInstance() {
        return INSTANCE;
    }

    @Override
    public Map<String, Path<?>> getPathMap() {
        return pathMap;
    }

    @Override
    public Path<?> getDefaultPath() {
        return event.eventSchedule.startAt;
    }
}

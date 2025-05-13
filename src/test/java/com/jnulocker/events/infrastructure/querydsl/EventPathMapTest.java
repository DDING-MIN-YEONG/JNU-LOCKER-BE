package com.jnulocker.events.infrastructure.querydsl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.jnulocker.events.domain.QEvent;
import com.querydsl.core.types.Path;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EventPathMapTest {

    @Test
    @DisplayName("EventPathMap은 싱글톤 인스턴스를 제공해야 한다")
    void getInstance() {
        // when
        EventPathMap instance1 = EventPathMap.getInstance();
        EventPathMap instance2 = EventPathMap.getInstance();

        // then
        assertNotNull(instance1);
        assertSame(instance1, instance2);
    }

    @Test
    @DisplayName("EventPathMap은 필요한 모든 Path 매핑을 제공해야 한다")
    void getPathMap() {
        // given
        EventPathMap instance = EventPathMap.getInstance();
        QEvent event = QEvent.event;

        // when
        Map<String, Path<?>> pathMap = instance.getPathMap();

        // then
        assertNotNull(pathMap);
        assertEquals(8, pathMap.size());

        assertTrue(pathMap.containsKey("id"));
        assertTrue(pathMap.containsKey("title"));
        assertTrue(pathMap.containsKey("publish"));
        assertTrue(pathMap.containsKey("eventStatus"));
        assertTrue(pathMap.containsKey("eventSchedule.startAt"));
        assertTrue(pathMap.containsKey("eventSchedule.endAt"));
        assertTrue(pathMap.containsKey("createdAt"));
        assertTrue(pathMap.containsKey("updatedAt"));

        assertEquals(event.id, pathMap.get("id"));
        assertEquals(event.title, pathMap.get("title"));
        assertEquals(event.publish, pathMap.get("publish"));
        assertEquals(event.eventStatus, pathMap.get("eventStatus"));
        assertEquals(event.eventSchedule.startAt, pathMap.get("eventSchedule.startAt"));
        assertEquals(event.eventSchedule.endAt, pathMap.get("eventSchedule.endAt"));
        assertEquals(event.createdAt, pathMap.get("createdAt"));
        assertEquals(event.updatedAt, pathMap.get("updatedAt"));
    }

    @Test
    @DisplayName("EventPathMap은 기본 정렬 Path를 제공해야 한다")
    void getDefaultPath() {
        // given
        EventPathMap instance = EventPathMap.getInstance();
        QEvent event = QEvent.event;

        // when
        Path<?> defaultPath = instance.getDefaultPath();

        // then
        assertNotNull(defaultPath);
        assertEquals(event.eventSchedule.startAt, defaultPath);
    }
}

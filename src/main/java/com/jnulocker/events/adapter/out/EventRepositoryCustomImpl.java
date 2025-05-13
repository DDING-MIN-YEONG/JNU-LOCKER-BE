package com.jnulocker.events.adapter.out;

import static com.jnulocker.events.domain.QEvent.event;
import static com.jnulocker.events.domain.QEventParticipation.eventParticipation;
import static com.jnulocker.events.domain.QLocker.locker;

import com.jnulocker.common.persistence.querydsl.QueryDslOrderUtils;
import com.jnulocker.events.application.port.in.response.MyEventCustomPage;
import com.jnulocker.events.application.port.in.response.MyEventListItem;
import com.jnulocker.events.application.port.in.response.QMyEventListItem;
import com.jnulocker.events.infrastructure.querydsl.EventPathMap;
import com.jnulocker.organization.domain.Department;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class EventRepositoryCustomImpl implements EventRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public MyEventCustomPage findEventsByParticipationDepartment(
            Department department, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        long offset = pageable.getOffset();

        Long totalElements =
                queryFactory
                        .select(event.count())
                        .from(event)
                        .join(event.eventParticipations, eventParticipation)
                        .where(eventParticipation.department.eq(department), event.publish.isTrue())
                        .fetchOne();

        if (totalElements == null) {
            totalElements = 0L;
        }

        List<MyEventListItem> content =
                queryFactory
                        .select(
                                new QMyEventListItem(
                                        event.id,
                                        event.title,
                                        event.department.nickname,
                                        event.eventSchedule.startAt,
                                        event.eventSchedule.endAt,
                                        // availableLockerCount
                                        JPAExpressions.select(locker.count())
                                                .from(locker)
                                                .where(
                                                        locker.available.isTrue(),
                                                        locker.floor.event.id.eq(event.id))))
                        .from(event)
                        .join(event.eventParticipations, eventParticipation)
                        .where(eventParticipation.department.eq(department), event.publish.isTrue())
                        .orderBy(
                                QueryDslOrderUtils.getOrderSpecifiers(
                                        pageable, EventPathMap.getInstance()))
                        .offset(offset)
                        .limit(pageSize)
                        .fetch();

        long totalPage = (totalElements + pageSize - 1) / pageSize;
        boolean last = pageNumber >= totalPage - 1;

        return MyEventCustomPage.of(content, totalElements, last);
    }
}

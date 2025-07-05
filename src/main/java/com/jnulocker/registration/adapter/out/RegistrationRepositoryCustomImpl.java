package com.jnulocker.registration.adapter.out;

import static com.jnulocker.events.domain.QFloor.floor;
import static com.jnulocker.events.domain.QLocker.locker;
import static com.jnulocker.registration.domain.QRegistration.*;

import com.jnulocker.registration.domain.Registration;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RegistrationRepositoryCustomImpl implements RegistrationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Registration> findAllByEventId(UUID eventId) {
        return queryFactory
                .selectFrom(registration)
                .where(floor.event.id.eq(eventId))
                .join(locker.floor)
                // 층 번호 오름차순, 사물함 코드 오름차순
                .orderBy(floor.floorNumber.asc(), locker.code.asc())
                .fetch();
    }
}

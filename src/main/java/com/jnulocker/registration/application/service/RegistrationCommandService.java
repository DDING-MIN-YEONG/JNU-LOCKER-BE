package com.jnulocker.registration.application.service;

import com.jnulocker.auth.security.SecurityUtils;
import com.jnulocker.common.util.RedisLockManager;
import com.jnulocker.events.application.port.in.EventQuery;
import com.jnulocker.events.domain.Event;
import com.jnulocker.events.domain.Locker;
import com.jnulocker.events.exception.InvalidLockerForEventException;
import com.jnulocker.member.application.port.in.MemberQuery;
import com.jnulocker.member.domain.Member;
import com.jnulocker.registration.application.port.in.RegistrationCommand;
import com.jnulocker.registration.application.port.in.request.RegisterForEventRequest;
import com.jnulocker.registration.application.port.out.RegistrationLoadPort;
import com.jnulocker.registration.application.port.out.RegistrationRecordPort;
import com.jnulocker.registration.domain.Registration;
import com.jnulocker.registration.exception.RegistrationAlreadyExistsException;
import com.jnulocker.registration.exception.RegistrationNotFoundException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrationCommandService implements RegistrationCommand {

    private final MemberQuery memberQuery;
    private final EventQuery eventQuery;
    private final RegistrationLoadPort registrationLoadPort;
    private final RegistrationRecordPort registrationRecordPort;
    private final RedisLockManager redisLockManager;

    @Override
    @Transactional
    public void registerForEvent(UUID eventId, RegisterForEventRequest request) {
        Long memberId = SecurityUtils.getCurrentMemberId();
        UUID lockerId = request.lockerId();

        redisLockManager.lock(
                lockerId.toString(),
                5L,
                v -> {
                    Member member = memberQuery.findByIdOrThrow(memberId);

                    Event event = eventQuery.getByIdOrThrow(eventId);
                    Locker locker = eventQuery.getLockerByIdOrThrow(request.lockerId());

                    if (!event.equals(locker.getFloor().getEvent())) {
                        throw InvalidLockerForEventException.EXCEPTION;
                    }

                    // 이미 해당 이벤트에서 사물함 신청을 완료한 경우 예외 발생
                    if (registrationLoadPort.existsByMemberIdAndEventId(memberId, eventId)) {
                        throw RegistrationAlreadyExistsException.EXCEPTION;
                    }

                    Registration registration = Registration.create(member, locker);
                    registrationRecordPort.save(registration);
                });
    }

    @Override
    @Transactional
    public void cancelMyRegistration(UUID eventId) {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Event event = eventQuery.getByIdOrThrow(eventId);
        Registration registration =
                registrationLoadPort
                        .getRegistrationByMemberIdAndEventId(memberId, event.getId())
                        .orElseThrow(() -> RegistrationNotFoundException.EXCEPTION);

        Locker locker = registration.getLocker();
        locker.markAsAvailable();

        registrationRecordPort.delete(registration);
    }

    @Override
    @Transactional
    public void deleteAllByMember(Member member) {
        // 해당 회원의 모든 등록 정보 삭제
        List<Registration> registrations = registrationLoadPort.getAllByMember(member);

        for (Registration registration : registrations) {
            Locker locker = registration.getLocker();
            locker.markAsAvailable(); // 사물함을 사용 가능 상태로 변경
        }

        registrationRecordPort.deleteAllByMember(member);
    }
}

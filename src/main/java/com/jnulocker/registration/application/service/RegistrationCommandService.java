package com.jnulocker.registration.application.service;

import com.jnulocker.auth.security.SecurityUtils;
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

    @Override
    @Transactional
    public void registerForEvent(Long eventId, RegisterForEventRequest request) {
        Long memberId = SecurityUtils.getCurrentMemberId();
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
    }

    @Override
    @Transactional
    public void cancelMyRegistration(Long eventId) {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Event event = eventQuery.getByIdOrThrow(eventId);
        Registration registration =
                registrationLoadPort
                        .getRegistrationByMemberIdAndEventId(memberId, event.getId())
                        .orElseThrow(() -> RegistrationNotFoundException.EXCEPTION);
        registrationRecordPort.delete(registration);
    }
}

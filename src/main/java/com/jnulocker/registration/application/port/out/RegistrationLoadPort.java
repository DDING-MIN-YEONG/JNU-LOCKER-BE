package com.jnulocker.registration.application.port.out;

import com.jnulocker.member.domain.Member;
import com.jnulocker.registration.domain.Registration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RegistrationLoadPort {

    boolean existsByMemberIdAndEventId(Long memberId, UUID eventId);

    Page<Registration> getRegistrationsByEventId(UUID eventId, Pageable pageable);

    Optional<Registration> getRegistrationByMemberIdAndEventId(Long memberId, UUID eventId);

    List<Registration> getAllByMember(Member member);
}

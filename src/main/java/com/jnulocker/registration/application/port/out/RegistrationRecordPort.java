package com.jnulocker.registration.application.port.out;

import com.jnulocker.member.domain.Member;
import com.jnulocker.registration.domain.Registration;

public interface RegistrationRecordPort {

    void save(Registration registration);

    void delete(Registration registration);

    void deleteAllByMember(Member member);
}

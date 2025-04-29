package com.jnulocker.registration.utils;

import com.jnulocker.registration.adapter.out.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegistrationTestUtil {

    @Autowired private RegistrationRepository registrationRepository;

    public void deleteAll() {
        registrationRepository.deleteAll();
    }
}

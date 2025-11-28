package com.jnulocker.config;

import com.jnulocker.auth.application.port.out.ManagerEmailSender;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
@Import({RedisTestConfig.class, TestVectorStoreConfig.class})
public abstract class RedisTest extends RedisTestContainer {

    @MockitoBean protected ManagerEmailSender managerEmailSender;
}

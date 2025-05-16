package ru.vinogradiya.utils;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import ru.vinogradiya.config.EntityManagerTestConfig;

@ContextConfiguration(classes = EntityManagerTestConfig.class)
public class IntegrationMvcTest extends BaseMvcTest {

    @Autowired
    protected EntityManager entityManager;
}
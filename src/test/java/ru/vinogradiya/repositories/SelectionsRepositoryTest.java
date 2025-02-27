package ru.vinogradiya.repositories;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import ru.vinogradiya.utils.JpaRepositoryBasedTest;

@ExtendWith(MockitoExtension.class)
public class SelectionsRepositoryTest extends JpaRepositoryBasedTest {

    @Autowired
    SelectionsRepository repository;
}

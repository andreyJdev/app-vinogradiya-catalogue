package ru.vinogradiya.utils;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ComponentScan(basePackages = "ru.vinogradiya")
public class BaseApplicationTest {

}
package ru.vinogradiya.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest
public class BaseMvcTest extends BaseApplicationTest {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper mapper;
}
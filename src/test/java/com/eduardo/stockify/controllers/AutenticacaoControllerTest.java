package com.eduardo.stockify.controllers;

import com.eduardo.stockify.config.AutenticacaoControllerTestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(AutenticacaoController.class)
@RequiredArgsConstructor
@Import(AutenticacaoControllerTestConfig.class)
class AutenticacaoControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

}
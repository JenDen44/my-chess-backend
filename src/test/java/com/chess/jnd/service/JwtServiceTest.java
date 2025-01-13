package com.chess.jnd.service;

import com.chess.jnd.ParameterFabric;
import com.chess.jnd.entity.Color;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@TestPropertySource
class JwtServiceTest {

    @InjectMocks
    JwtService jwtService;

    @Mock
    ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "secretKey", "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
    }

    @Test
    void generateCustomToken() throws JsonProcessingException {
        String color = "black";

        given(mapper.writeValueAsString(any(Color.class))).willReturn(color);

        String token = jwtService.generateCustomToken(1, Color.BLACK);

        verify(mapper).writeValueAsString(any(Color.class));

        assertThat(token).isNotNull();
    }

    @Test
    void getGameId() throws JsonProcessingException {
        String id  = "1";
        Integer returnedId = 1;

        given(mapper.readValue(id, Integer.class)).willReturn(returnedId);

        Integer idFromService = jwtService.getGameId(ParameterFabric.getBlackToken());

        verify(mapper).readValue(id, Integer.class);

        assertThat(idFromService).isNotNull();
        assertEquals(idFromService, returnedId);
    }

    @Test
    void getColor() throws JsonProcessingException {
        String color  = "white";
        Color returnedColor = Color.WHITE;

        given(mapper.readValue(color, Color.class)).willReturn(returnedColor);

        Color colorFromService = jwtService.getColor(ParameterFabric.getWhiteToken());

        verify(mapper).readValue(color, Color.class);

        assertThat(colorFromService).isNotNull();
        assertEquals(colorFromService, returnedColor);
    }

    @Test
    void resolveToken() {
        MockHttpServletRequest mockRequest = ParameterFabric.createMockRequest();

        String token = jwtService.resolveToken(mockRequest);

        assertThat(token).isNotNull();
        assertEquals(token, ParameterFabric.getWhiteToken());
    }
}
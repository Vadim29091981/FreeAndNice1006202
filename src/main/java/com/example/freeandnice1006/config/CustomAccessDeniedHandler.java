package com.example.freeandnice1006.config;

import com.example.freeandnice1006.handlers.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

/**
 * Компонент для обработки случаев отказа в доступе (403 Forbidden).
 * Логирует ошибку и возвращает клиенту информацию об ошибке в формате JSON.
 */
@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * Обрабатывает отказ в доступе.
     *
     * @param request HTTP запрос.
     * @param response HTTP ответ.
     * @param accessDeniedException Исключение, указывающее на отказ в доступе.
     * @throws IOException Если возникает ошибка ввода-вывода.
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        // Логируем ошибку отказа в доступе с сообщением исключения.
        log.error("Access denied error: {}", accessDeniedException.getMessage());

        // Устанавливаем тип контента ответа как JSON.
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // Устанавливаем статус ответа как 403 Forbidden.
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        // Создаем тело ответа с информацией об ошибке.
        ErrorResponse body = ErrorResponse.builder()
                .status(HttpServletResponse.SC_FORBIDDEN)
                .error("Forbidden")
                .timestamp(Instant.now())
                .message(accessDeniedException.getMessage())
                .path(request.getServletPath())
                .build();

        // Создаем объект ObjectMapper для сериализации Java объектов в JSON.
        final ObjectMapper mapper = new ObjectMapper();
        // Регистрируем модуль JavaTimeModule, чтобы Jackson поддерживал типы даты и времени Java 8 и выше.
        mapper.registerModule(new JavaTimeModule());
        // Настраиваем Jackson на сериализацию дат как строк в формате ISO 8601.
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // Записываем тело ответа в выходной поток ответа в формате JSON.
        mapper.writeValue(response.getOutputStream(), body);
    }
}

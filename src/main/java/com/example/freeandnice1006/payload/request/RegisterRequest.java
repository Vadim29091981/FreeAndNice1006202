package com.example.freeandnice1006.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Запрос на регистрацию нового пользователя.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    /**
     * Имя пользователя.
     */
    //@NotBlank(message = "firstname is required")
    private String firstname;

    /**
     * Фамилия пользователя.
     */
    //@NotBlank(message = "lastname is required")
    private String lastname;

    /**
     * Электронная почта пользователя.
     */
    //@NotBlank(message = "email is required")
    //@Email(message = "email format is not valid")
    private String email;

    /**
     * Пароль пользователя.
     */
    //@NotBlank(message = "password is required")
    //@StrongPassword
    private String password;


    /**
     * Дата рождения пользователя в формате строки.
     */
    private String dateOfBirth;

    /**
     * Номер телефона пользователя.
     */
    private String phoneNumber;

}

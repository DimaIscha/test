package com.innoseti.innosetitestprojec.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public static String getCustomMessage(String message) {
        return "Объект не существует в базе данных: ".concat(message).concat(" Запрос отклонен.");
    }
}

package com.innoseti.innosetitestprojec.exception;

public class ExistsInDataBaseException extends RuntimeException {

    public ExistsInDataBaseException(String message) {
        super(message);
    }

    public static String getCustomMessage(String message) {
        return "Объект существует в базе данных: ".concat(message).concat(" Запрос отклонен.");
    }
}

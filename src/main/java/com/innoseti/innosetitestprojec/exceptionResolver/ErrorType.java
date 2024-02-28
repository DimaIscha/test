package com.innoseti.innosetitestprojec.exceptionResolver;

import graphql.ErrorClassification;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public enum ErrorType implements ErrorClassification {
    SAVE_EXCEPTION,
    BAD_REQUEST,
    UNAUTHORIZED,
    FORBIDDEN,
    NOT_FOUND,
    INTERNAL_ERROR;

}

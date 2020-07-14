package com.vodafone.devicetracker.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "ERROR: A technical exception occurred")
public class TechnicalException extends RuntimeException {
    private static final long serialVersionUID = 8805058765486631703L;

    public TechnicalException(String string) {
        super(string);
    }
}

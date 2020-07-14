package com.vodafone.devicetracker.exceptions;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.vodafone.devicetracker.util.StringConstants;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "ERROR: no data file found")
public class DataLoadException extends Exception {
    private static final long serialVersionUID = 1427850963552811833L;

    private String additionalInformation = StringConstants.EMPTY_STRING;

    public DataLoadException addAdditionalInformation(String additionalInformation) {
        Objects.requireNonNull(additionalInformation);
        this.additionalInformation = additionalInformation;
        return this;
    }

    public String getAdditionalInformation() {
        return this.additionalInformation;
    }
}

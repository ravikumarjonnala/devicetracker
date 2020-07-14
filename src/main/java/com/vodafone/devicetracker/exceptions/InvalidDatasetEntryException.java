package com.vodafone.devicetracker.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "Invalid data in dataset")
public class InvalidDatasetEntryException extends Exception {

    private static final long serialVersionUID = 8225051098323386914L;
    public static final String INCORRECT_NUMBER_OF_FIELDS = "Incorrect number of fields";
    public static final String INVALID_DATA_IN_FIELD = "Invalid data in a field";

    public InvalidDatasetEntryException(String message) {
        super(message);
    }
}

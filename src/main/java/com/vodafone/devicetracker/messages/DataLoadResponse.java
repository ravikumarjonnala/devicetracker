package com.vodafone.devicetracker.messages;

/**
 * Response object for data load request.
 */
public class DataLoadResponse {
    public static final String DATA_REFRESHED = "data refreshed";
    public static final String NO_DATA_FILE_FOUND = "ERROR: No data file found";
    public static final String INVALID_DATA_IN_DATASET = "ERROR: Invalid data in dataset";
    public static final String TECHNICAL_ERROR = "ERROR: A technical exception occurred";

    /**
     * For holding additional description.
     */
    private String description;

    /**
     * Returns the additional description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the additional description.
     */
    public void setDescription(String description) {
        this.description = description;
    }
}

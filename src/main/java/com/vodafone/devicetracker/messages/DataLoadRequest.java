package com.vodafone.devicetracker.messages;

/**
 * Request for a data load.
 */
public class DataLoadRequest {

    private String filepath;

    /**
     * Returns the filepath.
     */
    public String getFilepath() {
        return filepath;
    }

    /**
     * Sets the filepath.
     */
    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
}

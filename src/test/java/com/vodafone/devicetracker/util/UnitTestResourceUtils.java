package com.vodafone.devicetracker.util;

import java.io.File;

public class UnitTestResourceUtils {
    public static final String TEST_DATASET_LOCATION = "resources";
    public static final String VALID_DATASET_FILENAME = "validDataset.xlsm";
    /**
     * Template for invalid dataset file name. Use with string formatting to include
     * a number in the file name.
     */
    public static final String INVALID_DATASET_FILENAME = "invalidDataset%d.xlsm";
    /**
     * Template for non-existent file name. Use with string formatting to include a
     * number in the file name.
     */
    public static final String NONEXISTENT_FILENAME = "nonExistentFile%d.xlsm";
    public static final int DATASET_COLUMN_COUNT = 8;

    public static final String VALID_PRODUCT_ID = "WG11155638";
    public static final String NON_EXISTING_PRODUCT_ID = "WG11155638X";
    public static final String VALID_PRODUCT_ID_PAST_TIMESTAMP = "1582605136000"; // 1582605137000 is the most recent
                                                                                  // for WG11155638
    public static final String VALID_PRODUCT_ID_LAT_AT_PAST_TIMESTAMP = "51.5185";
    public static final String VALID_PRODUCT_ID_LONG_AT_PAST_TIMESTAMP = "-0.1736";

    public static final String PRODUCT_ID_BATTERY_FULL = "WG11155639";
    public static final String PRODUCT_ID_BATTERY_HIGH = "WG11155640";
    public static final String PRODUCT_ID_BATTERY_MEDIUM = "WG11155641";
    public static final String PRODUCT_ID_BATTERY_LOW = "WG11155642";
    public static final String PRODUCT_ID_BATTERY_CRITICAL = "WG11155643";

    public static final String PRODUCT_ID_CYCLE_PLUS_TRACKER = "WG11155643";
    public static final String PRODUCT_ID_GENERAL_TRACKER = "6900001001";

    public static final String PRODUCT_ID_AIRPLANE_ON = "WG11155642";
    public static final String PRODUCT_ID_AIRPLANE_OFF = "WG11155643";

    public static final String PRODUCT_ID_GENERAL_TRACKER_AIRPLANE_ON = "6900233113";
    public static final String PRODUCT_ID_GENERAL_TRACKER_AIRPLANE_OFF = "6900233112";
    public static final String PRODUCT_ID_CYCLEPLUS_TRACKER_AIRPLANE_ON = "WG11155644";
    public static final String PRODUCT_ID_CYCLEPLUS_TRACKER_AIRPLANE_OFF = "WG11155643";
    public static final String PRODUCT_ID_CYCLEPLUS_TRACKER_AIRPLANE_OFF_LOCATION_UNCHANGED = "WG11155645";
    public static final String PRODUCT_ID_CYCLEPLUS_TRACKER_STARTING_UP = "WG11155646";

    /**
     * Returns the absolute path to a non-existing file within the test resources
     * directory.
     * <p>
     * Uses the template file name {@link #NONEXISTENT_FILENAME} with a number in
     * the file name to determine a file that does not exist in the test resources.
     * 
     * @return Absolute path to the non existing file name
     */
    public static String getPathForNonExistingFile() {
        String nonExistentFileName = null;
        boolean nonExistentFileFound = false;
        File testDataLocation = new File(
                UnitTestResourceUtils.class.getClassLoader().getResource(TEST_DATASET_LOCATION).getFile());
        for (int i = 0; !nonExistentFileFound; i++) {
            nonExistentFileName = testDataLocation.getAbsolutePath() + File.separator
                    + String.format(NONEXISTENT_FILENAME, i);
            File nonExistentFile = new File(nonExistentFileName);
            if (!nonExistentFile.exists()) {
                nonExistentFileFound = true;
            }
        }

        return nonExistentFileName;
    }

    /**
     * Returns an array of paths to invalid dataset files.
     * <p>
     * Each element in the array at index i, corresponds to a dataset file with
     * invalid data in column i of the dataset.
     * 
     * @return array of paths to invalid dataset files
     */
    public static String[] getPathsToInvalidDatasetFiles() {
        File testDataLocation = new File(
                UnitTestResourceUtils.class.getClassLoader().getResource(TEST_DATASET_LOCATION).getFile());
        String[] paths = new String[DATASET_COLUMN_COUNT];
        for (int i = 0; i < DATASET_COLUMN_COUNT; i++) {
            paths[i] = testDataLocation.getAbsolutePath() + File.separator + String.format(INVALID_DATASET_FILENAME, i);
        }
        return paths;
    }

    /**
     * Returns absolute path to a valid dataset file within the test resources.
     * 
     * @return valid dataset path
     */
    public static String getPathForValidDataset() {
        File testDataLocation = new File(
                UnitTestResourceUtils.class.getClassLoader().getResource(TEST_DATASET_LOCATION).getFile());
        return testDataLocation.getAbsolutePath() + File.separator + VALID_DATASET_FILENAME;
    }

}

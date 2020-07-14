package com.vodafone.devicetracker;

import static com.vodafone.devicetracker.util.UnitTestResourceUtils.NON_EXISTING_PRODUCT_ID;
import static com.vodafone.devicetracker.util.UnitTestResourceUtils.TEST_DATASET_LOCATION;
import static com.vodafone.devicetracker.util.UnitTestResourceUtils.VALID_DATASET_FILENAME;
import static com.vodafone.devicetracker.util.UnitTestResourceUtils.VALID_PRODUCT_ID;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.vodafone.devicetracker.exceptions.DataLoadException;
import com.vodafone.devicetracker.exceptions.InvalidDatasetEntryException;
import com.vodafone.devicetracker.exceptions.InvalidProductIdException;
import com.vodafone.devicetracker.exceptions.TechnicalException;
import com.vodafone.devicetracker.service.DeviceInformationServiceImpl;
import com.vodafone.devicetracker.util.UnitTestResourceUtils;

@DisplayName("Test DeviceInformationService implementation")
public class DeviceInformationServiceImplTests {

    @Nested
    class Test_forceRefresh {

        @Test
        public void testNonExistentFile() {
            final String fileName = UnitTestResourceUtils.getPathForNonExistingFile();
            DeviceInformationServiceImpl serviceImpl = new DeviceInformationServiceImpl();
            assertThrows(DataLoadException.class, () -> serviceImpl.forceRefresh(fileName),
                    "DataLoadException should have been thrown for a non-existing dataset file");
        }

        @Test
        public void testInvalidDataInColumns() {
            DeviceInformationServiceImpl serviceImpl = new DeviceInformationServiceImpl();
            String[] invalidDatasets = UnitTestResourceUtils.getPathsToInvalidDatasetFiles();
            for (String invalidDatasetPath : invalidDatasets) {
                File invalidFile = new File(invalidDatasetPath);
                assertThrows(InvalidDatasetEntryException.class,
                        () -> serviceImpl.forceRefresh(invalidFile.getAbsolutePath()),
                        "InvalidDatasetEntryException should have been thrown for invalid data in column");
            }
        }

        @Test
        public void testValidDataLoad() {
            DeviceInformationServiceImpl serviceImpl = new DeviceInformationServiceImpl();
            String validFileName = UnitTestResourceUtils.getPathForValidDataset();
            File validFile = new File(validFileName);
            assertEquals(true, validFile.exists(), "File for valid dataset should have been existing");
            assertDoesNotThrow(() -> serviceImpl.forceRefresh(validFileName),
                    "No exception should have been thrown when existing valid dataset is loaded");
        }

    }

    @Nested
    class Test_getTracker {
        @Test
        public void testRetrievalBeforeLoad() {
            DeviceInformationServiceImpl serviceImpl = new DeviceInformationServiceImpl();
            assertThrows(TechnicalException.class, () -> serviceImpl.getTracker(VALID_PRODUCT_ID),
                    "TechnicalException should have been thrown when a retrieval is performed prior to loading data");
        }

        @Test
        public void testRetrievalAfterLoad() {
            DeviceInformationServiceImpl serviceImpl = new DeviceInformationServiceImpl();
            File testDataLocation = new File(getClass().getClassLoader().getResource(TEST_DATASET_LOCATION).getFile());
            String validFileName = testDataLocation.getAbsolutePath() + File.separator + VALID_DATASET_FILENAME;
            try {
                serviceImpl.forceRefresh(validFileName);
            } catch (Exception e) {
                fail("No exception should have been thrown for loading valid dataset");
            }
            assertDoesNotThrow(() -> serviceImpl.getTracker(VALID_PRODUCT_ID),
                    "No Exception should have been thrown for a valid product Id");
            try {
                assertNotNull(serviceImpl.getTracker(VALID_PRODUCT_ID),
                        "A non-null tracker should have been returned for a valid product id");
            } catch (TechnicalException e) {
                fail("TechnicalException should not have been thrown for a valid dataset and valid product id");
            } catch (InvalidProductIdException e) {
                fail("InvalidProductIdException should not have been thrown for a valid product id");
            }

            try {
                assertThrows(InvalidProductIdException.class, () -> serviceImpl.getTracker(NON_EXISTING_PRODUCT_ID),
                        "InvalidProductIdException should have been thrown for an invalid product id");
            } catch (TechnicalException e) {
                fail("TechnicalException should not have been thrown for a valid dataset and invalid product id");
            }
        }
    }
}

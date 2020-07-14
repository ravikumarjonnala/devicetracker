package com.vodafone.devicetracker.controller;

import static com.vodafone.devicetracker.util.UnitTestDefaults.VALID_LOCATION_RETRIEVAL_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.util.UriComponentsBuilder;

import com.vodafone.devicetracker.dataobjects.BaseTrackerVO;
import com.vodafone.devicetracker.dataobjects.SuccessTrackerVO;
import com.vodafone.devicetracker.messages.DeviceLocationRequest;
import com.vodafone.devicetracker.messages.DeviceLocationResponse;
import com.vodafone.devicetracker.service.DeviceInformationService;
import com.vodafone.devicetracker.util.StringConstants;
import com.vodafone.devicetracker.util.UnitTestResourceUtils;
import com.vodafone.devicetracker.util.Utils;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HTTPDeviceLocationRetrievalTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DeviceInformationService deviceLocationService;

    @Test
    public void retrieveUsingValidProductIdBeforeLoad() {
        String url = String.format(VALID_LOCATION_RETRIEVAL_URL, port);
        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam(DeviceLocationRequest.PRODUCT_ID, UnitTestResourceUtils.VALID_PRODUCT_ID);
        ResponseEntity<? extends BaseTrackerVO> response = restTemplate
                .getForEntity(urlBuilder.build(true).toUriString(), BaseTrackerVO.class, new HashMap<String, String>());
        assertEquals(500, response.getStatusCodeValue(),
                "HTTP response should have been 500 for a device location request prior to data load");
        assertNotNull(response.getBody(), "Response body should have included a BaseTrackerVO ");
        assertEquals(true, response.getBody() instanceof BaseTrackerVO);
    }

    @Nested
    @DirtiesContext
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class TestPostDataLoadScenarios {
        private boolean dataLoadSuccessful = false;

        @BeforeAll
        private void loadDataset() {
            String validFileName = UnitTestResourceUtils.getPathForValidDataset();
            try {
                deviceLocationService.forceRefresh(validFileName);
                dataLoadSuccessful = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @BeforeEach
        private void checkIfDataLoadWasSuccessful() {
            Assumptions.assumeTrue(dataLoadSuccessful);
        }

        @Test
        public void retrieveUsingInvalidProductIdAfterLoad() {
            String url = String.format(VALID_LOCATION_RETRIEVAL_URL, port);
            UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParam(DeviceLocationRequest.PRODUCT_ID, UnitTestResourceUtils.NON_EXISTING_PRODUCT_ID);
            ResponseEntity<? extends BaseTrackerVO> response = restTemplate.getForEntity(
                    urlBuilder.build(true).toUriString(), BaseTrackerVO.class, new HashMap<String, String>());
            assertEquals(404, response.getStatusCodeValue(),
                    "HTTP response should have been 200 for a device location request after data load");
            assertNotNull(response.getBody(), "Response body should have included a BaseTrackerVO");

        }

        @Test
        public void retrieveUsingValidProductIdAfterLoad() {
            String url = String.format(VALID_LOCATION_RETRIEVAL_URL, port);
            UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParam(DeviceLocationRequest.PRODUCT_ID, UnitTestResourceUtils.VALID_PRODUCT_ID);
            ResponseEntity<SuccessTrackerVO> response = restTemplate.getForEntity(urlBuilder.build(true).toUriString(),
                    SuccessTrackerVO.class, new HashMap<String, String>());
            assertEquals(200, response.getStatusCodeValue(),
                    "HTTP response should have been 200 for a device location request after data load");
            assertNotNull(response.getBody(), "Response body should have included a SuccessTrackerVO ");
            assertEquals(UnitTestResourceUtils.VALID_PRODUCT_ID, response.getBody().getId(),
                    "Queried productId should match return productId");
        }

        @Test
        public void ensureLocationIsReturnedBasedOnTimestamp() {
            String pastTimestamp = UnitTestResourceUtils.VALID_PRODUCT_ID_PAST_TIMESTAMP;
            String expectedLat = UnitTestResourceUtils.VALID_PRODUCT_ID_LAT_AT_PAST_TIMESTAMP;
            String expectedLong = UnitTestResourceUtils.VALID_PRODUCT_ID_LONG_AT_PAST_TIMESTAMP;

            String url = String.format(VALID_LOCATION_RETRIEVAL_URL, port);
            UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParam(DeviceLocationRequest.PRODUCT_ID, UnitTestResourceUtils.VALID_PRODUCT_ID)
                    .queryParam(DeviceLocationRequest.TIMESTAMP, pastTimestamp);
            ResponseEntity<SuccessTrackerVO> response = restTemplate.getForEntity(urlBuilder.build(true).toUriString(),
                    SuccessTrackerVO.class, new HashMap<String, String>());
            assertEquals(200, response.getStatusCodeValue(),
                    "HTTP response should have been 200 for a device location request after data load");
            assertEquals(UnitTestResourceUtils.VALID_PRODUCT_ID, response.getBody().getId(),
                    "Queried productId should match return productId");
            assertEquals(expectedLat, response.getBody().getLatitude(), "Actual and expected latitudes do not match");
            assertEquals(expectedLong, response.getBody().getLongitude(),
                    "Actual and expected longitudes do not match");
        }

        @Test
        public void testBatteryLevelIndicators() {
            String[] productIds = new String[] { UnitTestResourceUtils.PRODUCT_ID_BATTERY_FULL,
                    UnitTestResourceUtils.PRODUCT_ID_BATTERY_HIGH, UnitTestResourceUtils.PRODUCT_ID_BATTERY_MEDIUM,
                    UnitTestResourceUtils.PRODUCT_ID_BATTERY_LOW, UnitTestResourceUtils.PRODUCT_ID_BATTERY_CRITICAL };
            String[] expectedBatterylevel = new String[] { StringConstants.BATTERY_FULL, StringConstants.BATTERY_HIGH,
                    StringConstants.BATTERY_MEDIUM, StringConstants.BATTERY_LOW, StringConstants.BATTERY_CRITICAL };

            String url = String.format(VALID_LOCATION_RETRIEVAL_URL, port);
            for (int i = 0; i < productIds.length; i++) {
                UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(url)
                        .queryParam(DeviceLocationRequest.PRODUCT_ID, productIds[i]);
                ResponseEntity<SuccessTrackerVO> response = restTemplate.getForEntity(
                        urlBuilder.build(true).toUriString(), SuccessTrackerVO.class, new HashMap<String, String>());
                assertEquals(200, response.getStatusCodeValue(),
                        "HTTP response should have been 200 for a device location request after data load");
                assertEquals(expectedBatterylevel[i], response.getBody().getBattery(),
                        "Battery level " + Utils.encloseInSingleQuotes(expectedBatterylevel[i]) + " not returned");
            }
        }

        @Test
        public void testGeneralVsCycleTracker() {
            String[] productIds = new String[] { UnitTestResourceUtils.PRODUCT_ID_CYCLE_PLUS_TRACKER,
                    UnitTestResourceUtils.PRODUCT_ID_GENERAL_TRACKER };
            String[] expectedName = new String[] { StringConstants.CYCLE_PLUS_TRACKER,
                    StringConstants.GENERAL_TRACKER };

            String url = String.format(VALID_LOCATION_RETRIEVAL_URL, port);
            for (int i = 0; i < productIds.length; i++) {
                UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(url)
                        .queryParam(DeviceLocationRequest.PRODUCT_ID, productIds[i]);
                ResponseEntity<SuccessTrackerVO> response = restTemplate.getForEntity(
                        urlBuilder.build(true).toUriString(), SuccessTrackerVO.class, new HashMap<String, String>());
                assertEquals(200, response.getStatusCodeValue(),
                        "HTTP response should have been 200 for a device location request after data load");
                assertNotNull(response.getBody(), "Response body should have included a SuccessTrackerVO ");
                assertEquals(expectedName[i], response.getBody().getName(),
                        "Name " + Utils.encloseInSingleQuotes(expectedName[i]) + " not returned");
            }
        }

        @Test
        public void testAirplaneModeDescription() {
            String[] productIds = new String[] { UnitTestResourceUtils.PRODUCT_ID_AIRPLANE_ON,
                    UnitTestResourceUtils.PRODUCT_ID_AIRPLANE_OFF };
            String[] expectedDesc = new String[] { DeviceLocationResponse.AIRPLANE_MODE_ON_LOCATION_UNAVAILABLE,
                    DeviceLocationResponse.LOCATION_IDENTIFIED };

            String url = String.format(VALID_LOCATION_RETRIEVAL_URL, port);
            for (int i = 0; i < productIds.length; i++) {
                UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(url)
                        .queryParam(DeviceLocationRequest.PRODUCT_ID, productIds[i]);
                ResponseEntity<SuccessTrackerVO> response = restTemplate.getForEntity(
                        urlBuilder.build(true).toUriString(), SuccessTrackerVO.class, new HashMap<String, String>());
                assertEquals(200, response.getStatusCodeValue(),
                        "HTTP response should have been 200 for a device location request after data load");
                assertNotNull(response.getBody(), "Response body should have included a SuccessTrackerVO ");
                assertEquals(expectedDesc[i], response.getBody().getDescription(),
                        "Expected description " + Utils.encloseInSingleQuotes(expectedDesc[i]) + " not returned");
            }
        }

        @RepeatedTest(3)
        public void testStatusActiveness(RepetitionInfo repetitionInfo) {
            String[] productIds_Active = new String[] { UnitTestResourceUtils.PRODUCT_ID_GENERAL_TRACKER_AIRPLANE_OFF,
                    UnitTestResourceUtils.PRODUCT_ID_CYCLEPLUS_TRACKER_AIRPLANE_OFF };
            String[] productIds_Inactive = new String[] { UnitTestResourceUtils.PRODUCT_ID_GENERAL_TRACKER_AIRPLANE_ON,
                    UnitTestResourceUtils.PRODUCT_ID_CYCLEPLUS_TRACKER_AIRPLANE_ON,
                    UnitTestResourceUtils.PRODUCT_ID_CYCLEPLUS_TRACKER_AIRPLANE_OFF_LOCATION_UNCHANGED };
            String[] productIds_NA = new String[] { UnitTestResourceUtils.PRODUCT_ID_CYCLEPLUS_TRACKER_STARTING_UP };

            String[][] productIds = new String[][] { productIds_Active, productIds_Inactive, productIds_NA };
            String[] expectedStatuses = new String[] { StringConstants.STATUS_ACTIVE, StringConstants.STATUS_INACTIVE,
                    StringConstants.STATUS_NA };

            String url = String.format(VALID_LOCATION_RETRIEVAL_URL, port);
            String[] productIdsToTest = productIds[repetitionInfo.getCurrentRepetition() - 1];
            String expectedStatus = expectedStatuses[repetitionInfo.getCurrentRepetition() - 1];

            for (String productId : productIdsToTest) {
                UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(url)
                        .queryParam(DeviceLocationRequest.PRODUCT_ID, productId);
                ResponseEntity<SuccessTrackerVO> response = restTemplate.getForEntity(
                        urlBuilder.build(true).toUriString(), SuccessTrackerVO.class, new HashMap<String, String>());
                assertEquals(200, response.getStatusCodeValue(),
                        "HTTP response should have been 200 for a device location request after data load");
                assertNotNull(response.getBody(), "Response body should have included a SuccessTrackerVO ");
                assertEquals(expectedStatus, response.getBody().getStatus(), "Expected status not returned");
            }
        }

    }
}

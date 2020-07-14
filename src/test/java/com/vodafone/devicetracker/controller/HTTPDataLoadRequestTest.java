package com.vodafone.devicetracker.controller;

import static com.vodafone.devicetracker.util.UnitTestDefaults.VALID_DATASET_LOAD_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.vodafone.devicetracker.messages.DataLoadRequest;
import com.vodafone.devicetracker.messages.DataLoadResponse;
import com.vodafone.devicetracker.util.UnitTestResourceUtils;

@DirtiesContext
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HTTPDataLoadRequestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void nonExistentDatasetLoadRequest() {
        DataLoadRequest dataLoadRequest = new DataLoadRequest();
        String nonExistentFileName = UnitTestResourceUtils.getPathForNonExistingFile();
        dataLoadRequest.setFilepath(nonExistentFileName);
        HttpEntity<DataLoadRequest> request = new HttpEntity<>(dataLoadRequest);
        ResponseEntity<DataLoadResponse> response = restTemplate
                .postForEntity(String.format(VALID_DATASET_LOAD_URL, port), request, DataLoadResponse.class);
        assertEquals(404, response.getStatusCodeValue(),
                "Non existent dataset load should have returned HTTP Status code of 404");
        assertNotNull(response.getBody(), "Response body should have been included DataLoadResponse instance");
        assertEquals(DataLoadResponse.NO_DATA_FILE_FOUND, response.getBody().getDescription(),
                "'No data file found'message is incorrect");
    }

    @Test
    public void incorrectDataInDatasetLoadRequest() {
        DataLoadRequest dataLoadRequest = new DataLoadRequest();
        String[] invalidDatasetPaths = UnitTestResourceUtils.getPathsToInvalidDatasetFiles();
        for (String invalidDatasetPath : invalidDatasetPaths) {
            dataLoadRequest.setFilepath(invalidDatasetPath);
            HttpEntity<DataLoadRequest> request = new HttpEntity<>(dataLoadRequest);
            ResponseEntity<DataLoadResponse> response = restTemplate
                    .postForEntity(String.format(VALID_DATASET_LOAD_URL, port), request, DataLoadResponse.class);
            assertEquals(500, response.getStatusCodeValue(),
                    "Invalid data in dataset file should have returned HTTP Status code of 500");
            assertNotNull(response.getBody(), "Response body should have been included DataLoadResponse instance");
            assertEquals(DataLoadResponse.INVALID_DATA_IN_DATASET, response.getBody().getDescription(),
                    "'Invalid data in dataset'message is incorrect");
        }
    }

    @Test
    public void validDatasetLoadRequest() {
        DataLoadRequest dataLoadRequest = new DataLoadRequest();
        String validFileName = UnitTestResourceUtils.getPathForValidDataset();
        dataLoadRequest.setFilepath(validFileName);
        HttpEntity<DataLoadRequest> request = new HttpEntity<>(dataLoadRequest);
        ResponseEntity<DataLoadResponse> response = restTemplate
                .postForEntity(String.format(VALID_DATASET_LOAD_URL, port), request, DataLoadResponse.class);
        assertEquals(200, response.getStatusCodeValue(),
                "Valid dataset load should have returned HTTP Status code of 200");
        assertNotNull(response.getBody(), "Response body should have been included DataLoadResponse instance");
        assertEquals(DataLoadResponse.DATA_REFRESHED, response.getBody().getDescription(),
                "Data refreshed message is incorrect");
    }
}

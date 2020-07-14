package com.vodafone.devicetracker.controller;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vodafone.devicetracker.dataobjects.BaseTrackerVO;
import com.vodafone.devicetracker.dataobjects.DeviceStatus;
import com.vodafone.devicetracker.dataobjects.SuccessTrackerVO;
import com.vodafone.devicetracker.dataobjects.Tracker;
import com.vodafone.devicetracker.dataobjects.util.VOConverter;
import com.vodafone.devicetracker.exceptions.DataLoadException;
import com.vodafone.devicetracker.exceptions.DeviceNotLocatedException;
import com.vodafone.devicetracker.exceptions.InvalidDatasetEntryException;
import com.vodafone.devicetracker.exceptions.InvalidProductIdException;
import com.vodafone.devicetracker.exceptions.TechnicalException;
import com.vodafone.devicetracker.messages.DataLoadRequest;
import com.vodafone.devicetracker.messages.DataLoadResponse;
import com.vodafone.devicetracker.messages.DeviceLocationResponse;
import com.vodafone.devicetracker.service.DeviceInformationService;
import com.vodafone.devicetracker.util.Defaults;
import com.vodafone.devicetracker.util.Utils;

import io.swagger.annotations.ApiOperation;

/**
 * Controller class to handle requests for loading data and retrieving device
 * information.
 */
@RestController
public class DeviceInformationController {

    private static final Logger logger = Logger.getLogger(DeviceInformationController.class.getName());

    @Autowired
    private DeviceInformationService deviceInformationService;

    @PostMapping(value = "/iot/event/v1/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Loads the provided dataset for use by other API", notes = "Provide the excel filepath for loading the dataset from", response = DataLoadResponse.class)
    public ResponseEntity<DataLoadResponse> loadDataset(@RequestBody DataLoadRequest payLoad) {
        DataLoadResponse response = new DataLoadResponse();
        try {
            deviceInformationService.forceRefresh(payLoad.getFilepath());
        } catch (DataLoadException dle) {
            logger.log(Level.WARNING, "Exception occurred while loading data. ", dle);
            response.setDescription(DataLoadResponse.NO_DATA_FILE_FOUND);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (InvalidDatasetEntryException idee) {
            logger.log(Level.WARNING, "Exception occurred while loading data. ", idee);
            response.setDescription(DataLoadResponse.INVALID_DATA_IN_DATASET);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (TechnicalException e) {
            logger.log(Level.WARNING, "Technical exception occurred while loading data. ", e);
            response.setDescription(DataLoadResponse.TECHNICAL_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.setDescription(DataLoadResponse.DATA_REFRESHED);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "iot/event/v1", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Returns the last known device location", notes = "Defaults for timestamp and timeZone are current time and UTC respectively", response = SuccessTrackerVO.class)
    public ResponseEntity<? extends BaseTrackerVO> getDeviceLocation(@RequestParam String productId,
            @RequestParam(required = false) Long timestamp,
            @RequestParam(required = false, defaultValue = Defaults.DEFAULT_TIMEZONE_UTC) String timeZone) {

        Tracker tracker = null;
        DeviceStatus deviceStatus = null;

        BaseTrackerVO baseVo = new BaseTrackerVO();

        try {
            tracker = deviceInformationService.getTracker(productId);
            timestamp = timestamp == null || timestamp.longValue() <= 0 ? System.currentTimeMillis() : timestamp;

            deviceStatus = tracker.getLastKnownStatus(new Date(timestamp));
            if (!deviceStatus.getAirplaneMode().booleanValue()
                    && !deviceStatus.getDeviceLocation().isLocationAvailable()) {
                throw new DeviceNotLocatedException();
            }
        } catch (InvalidProductIdException ipe) {
            logger.info("Product not found for id " + productId);
            baseVo.setDescription(String.format(DeviceLocationResponse.ID_NOT_FOUND, productId));
            return new ResponseEntity<>(baseVo, HttpStatus.NOT_FOUND);
        } catch (DeviceNotLocatedException dnle) {
            logger.info("Airplane mode is off and device location is unavailable");
            baseVo.setDescription(DeviceLocationResponse.DEVICE_COULD_NOT_BE_LOCATED);
            return new ResponseEntity<>(baseVo, HttpStatus.BAD_REQUEST);
        } catch (TechnicalException e) {
            logger.log(Level.WARNING, "Technical exception occurred while retrieving device location", e);
            baseVo.setDescription(DeviceLocationResponse.TECHNICAL_ERROR + Utils.encloseInParenthesis(e.getMessage()));
            return new ResponseEntity<>(baseVo, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(VOConverter.convert(tracker, deviceStatus), HttpStatus.OK);
    }
}

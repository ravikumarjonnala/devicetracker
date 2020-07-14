package com.vodafone.devicetracker.dataobjects.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import com.vodafone.devicetracker.dataobjects.CyclePlusTracker;
import com.vodafone.devicetracker.dataobjects.DeviceLocation;
import com.vodafone.devicetracker.dataobjects.DeviceStatus;
import com.vodafone.devicetracker.util.BusinessRules;
import com.vodafone.devicetracker.util.StringConstants;
import com.vodafone.devicetracker.util.Utils;

/**
 * Parses a given row or line of text into device related objects and fields.
 * <p>
 * This parser is not thread safe. This parser holds on to the results from last
 * parsed row/textual line. Therefore if a single instance of this parser is
 * used/referenced from multiple threads, then appropriate synchronization be
 * used. Alternatively, multiple instances of the parser may be used.
 * <p>
 * Parsing does not throw an exception, rather sets the flag
 * {@link #isSuccessfullyParsed} appropriately. So, after a parse, check (using
 * {@link #isSuccessfullyParsed()} if the parsing has been successful.
 */
public class EventParser {
    /**
     * Is the last parsing successfully complete.
     */
    private boolean isSuccessfullyParsed = false;
    /**
     * Last parsed {@link DeviceStatus} object.
     */
    private DeviceStatus parsedDeviceStatus = null;
    /**
     * Last parsed {@link DeviceLocation} object.
     */
    private DeviceLocation parsedDeviceLocation = null;
    /**
     * Was the last parsed device corresponding to a {@link CyclePlusTracker}.
     */
    private boolean isCyclePlusTracker = false;
    /**
     * Last parsed productId.
     */
    private String parsedProductId = null;

    private static final Logger logger = Logger.getLogger(EventParser.class.getName());

    private void reset() {
        isSuccessfullyParsed = false;
        parsedDeviceStatus = null;
        parsedDeviceLocation = null;
        isCyclePlusTracker = false;
        parsedProductId = null;
    }

    /**
     * Parses the given row into the fields used by the device/tracker/event object.
     * <p>
     * Throws {@link }
     * 
     * @param row row from spreadsheet corresponding to an event
     */
    public void parse(Row row) {
        reset();
        try {
            Date dateTime = row.getCell(0) == null ? null
                    : new Date(new BigDecimal(row.getCell(0).getNumericCellValue()).longValue());
            if (dateTime == null) {
                throw new IllegalStateException("Invalid dateTime in column 0");
            }

            Integer eventId = row.getCell(1) == null ? null
                    : new BigDecimal(row.getCell(1).getNumericCellValue()).intValue();
            if (eventId == null) {
                throw new IllegalStateException("Invalid eventId in column 1");
            }

            parsedProductId = row.getCell(2) == null ? null : parseCellForString(row.getCell(2));
            if (Utils.isNullOrEmptyString(parsedProductId)) {
                throw new IllegalStateException("Invalid ProductId in column 2");
            }

            Double latitude = row.getCell(3) == null ? null
                    : new BigDecimal(row.getCell(3).getNumericCellValue()).doubleValue();
            Double longitude = row.getCell(4) == null ? null
                    : new BigDecimal(row.getCell(4).getNumericCellValue()).doubleValue();

            Integer batteryPercentage = row.getCell(5) == null ? null
                    : new BigDecimal(row.getCell(5).getNumericCellValue()).setScale(2, RoundingMode.UP)
                            .multiply(new BigDecimal(100)).intValue();
            if (batteryPercentage == null) {
                throw new IllegalStateException("Invalid batteryPercentage in column 5");
            }

            String light = row.getCell(6) == null ? StringConstants.EMPTY_STRING : row.getCell(6).getStringCellValue();
            String airplaneMode = row.getCell(7) == null ? StringConstants.EMPTY_STRING
                    : row.getCell(7).getStringCellValue();

            parsedDeviceLocation = new DeviceLocation(latitude, longitude);
            parsedDeviceStatus = new DeviceStatus(eventId, dateTime, parsedDeviceLocation, batteryPercentage, light,
                    DeviceStatus.determineAirplaneMode(airplaneMode));
            isCyclePlusTracker = BusinessRules.isCycleTracker(parsedProductId);
            isSuccessfullyParsed = true;
        } catch (IllegalStateException | NumberFormatException e) {
            logger.log(Level.WARNING, e.getMessage());
            isSuccessfullyParsed = false;
        }
    }

    /**
     * Indicates if the last parsing was successful.
     * 
     * @return true, if successful
     */
    public boolean isSuccessfullyParsed() {
        return this.isSuccessfullyParsed;
    }

    /**
     * Returns the last device status information encapsulated in
     * {@link DeviceStatus}.
     * 
     * @return device status
     */
    public DeviceStatus getParsedDeviceStatus() {
        return this.parsedDeviceStatus;
    }

    /**
     * Returns the last device location information encapsulated in
     * {@link DeviceLocation}.
     * 
     * @return device location
     */
    public DeviceLocation getParsedDeviceLocation() {
        return this.parsedDeviceLocation;
    }

    /**
     * Indicates if the last parse result was a {@link #CyclePlusTracker}.
     * 
     * @return true, if it was a cycle plus tracker
     */
    public boolean isCyclePlusTracker() {
        return this.isCyclePlusTracker;
    }

    /**
     * Returns the parsed product id.
     * 
     * @return product id
     */
    public String getParsedProductId() {
        return parsedProductId;
    }

    private static String parseCellForString(Cell cell) {
        Objects.requireNonNull(cell, "Cannot parse a null cell");
        if (cell.getCellType() == CellType.NUMERIC) {
            long val = new BigDecimal(cell.getNumericCellValue()).longValue();
            return StringConstants.EMPTY_STRING + val;
        } else if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        }

        return StringConstants.EMPTY_STRING;
    }

}

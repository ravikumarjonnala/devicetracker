package com.vodafone.devicetracker.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.vodafone.devicetracker.data.DeviceData;
import com.vodafone.devicetracker.dataobjects.CyclePlusTracker;
import com.vodafone.devicetracker.dataobjects.GeneralTracker;
import com.vodafone.devicetracker.dataobjects.Tracker;
import com.vodafone.devicetracker.dataobjects.util.EventParser;
import com.vodafone.devicetracker.exceptions.DataLoadException;
import com.vodafone.devicetracker.exceptions.InvalidDatasetEntryException;
import com.vodafone.devicetracker.exceptions.InvalidProductIdException;
import com.vodafone.devicetracker.exceptions.TechnicalException;
import com.vodafone.devicetracker.util.Defaults;

/**
 * Service class for providing the device information services.
 * <p>
 * Services currently provided by this service:
 * <ol>
 * <li>{@link #forceRefresh(String)}, to load the device data from the provided
 * dataset file</li>
 * <li>{@link #getTracker(String)}, to return the tracker corresponding to a
 * given productId</li>
 * </ol>
 *
 */
@Service
public class DeviceInformationServiceImpl implements DeviceInformationService {
    /**
     * Holds the device data parsed from the file.
     */
    private DeviceData deviceData = null;

    @Override
    public void forceRefresh(String datasetPath)
            throws DataLoadException, InvalidDatasetEntryException, TechnicalException {
        File datasetFile = new File(datasetPath);
        if (datasetFile == null || !datasetFile.exists() || !datasetFile.isFile()) {
            throw new DataLoadException().addAdditionalInformation("Specified dataset does not point to a file");
        }

        loadExcelDataset(datasetFile);
    }

    private void loadExcelDataset(File datasetFile) throws TechnicalException, InvalidDatasetEntryException {
        DeviceData deviceData = new DeviceData();
        AtomicInteger incorrectEntries = new AtomicInteger(0);

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(new BufferedInputStream(new FileInputStream(datasetFile)));
            XSSFSheet dataSheet = workbook.getSheetAt(0);
            Iterator<Row> rowIt = dataSheet.rowIterator();

            // Ignore the top most header & comment rows
            for (int i = 0; i < Defaults.NUMBER_OF_HEADER_ROWS_IN_DATASET_FILE; i++) {
                if (rowIt.hasNext()) {
                    rowIt.next();
                } else {
                    throw new InvalidDatasetEntryException("Incorrect number of header rows in dataset");
                }
            }

            EventParser parser = new EventParser();
            while (rowIt.hasNext()) {
                Row row = rowIt.next();
                parser.parse(row);
                if (parser.isSuccessfullyParsed()) {
                    Tracker tracker = deviceData.getDevice(parser.getParsedProductId());
                    if (tracker == null) {
                        if (parser.isCyclePlusTracker()) {
                            tracker = new CyclePlusTracker(parser.getParsedProductId());
                        } else {
                            tracker = new GeneralTracker(parser.getParsedProductId());
                        }
                    }
                    tracker.addStatus(parser.getParsedDeviceStatus());
                    deviceData.addDevice(parser.getParsedProductId(), tracker);
                } else {
                    incorrectEntries.incrementAndGet();
                }
            }
        } catch (IOException e) {
            throw new TechnicalException(e.getMessage());
        }

        if (incorrectEntries.intValue() > 0) {
            throw new InvalidDatasetEntryException(incorrectEntries.intValue() + " incorrect entries in dataset");
        }

        if (this.deviceData != null) {
            this.deviceData.reset();
        }
        this.deviceData = deviceData;
    }

    @Override
    public Tracker getTracker(String productId) throws InvalidProductIdException, TechnicalException {
        if (deviceData == null) {
            throw new TechnicalException("Data not loaded yet");
        }

        Objects.requireNonNull(productId, "productId cannot be null");
        Tracker tracker = deviceData.getDevice(productId);
        if (tracker == null) {
            throw new InvalidProductIdException(productId);
        }

        return tracker;
    }
}

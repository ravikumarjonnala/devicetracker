package com.vodafone.devicetracker.service;

import com.vodafone.devicetracker.data.DeviceData;
import com.vodafone.devicetracker.dataobjects.Tracker;
import com.vodafone.devicetracker.exceptions.DataLoadException;
import com.vodafone.devicetracker.exceptions.InvalidDatasetEntryException;
import com.vodafone.devicetracker.exceptions.InvalidProductIdException;
import com.vodafone.devicetracker.exceptions.TechnicalException;

/**
 * Service interface for device information.
 */
public interface DeviceInformationService {
    /**
     * Reads the dataset file and loads them into a {@link DeviceData} instance for
     * subsequent retrieval.
     * 
     * @param datasetPath absolute path to the dataset file
     * @throws DataLoadException            if the provided path does not exist or
     *                                      does not correspond to a file
     * @throws InvalidDatasetEntryException if the data in the file is not in the
     *                                      expected format
     * @throws TechnicalException           in case of any other errors
     */
    public void forceRefresh(String datasetPath)
            throws DataLoadException, InvalidDatasetEntryException, TechnicalException;

    /**
     * Returns the tracker corresponding to a given product id.
     * 
     * @param productId product id
     * @return tracker corresponding to the product
     * @throws InvalidProductIdException in case no tracker could be located for the
     *                                   product id
     * @throws TechnicalException        if an attempt has been made to retrieve
     *                                   product prior to loading the data
     */
    public Tracker getTracker(String productId) throws InvalidProductIdException, TechnicalException;
}

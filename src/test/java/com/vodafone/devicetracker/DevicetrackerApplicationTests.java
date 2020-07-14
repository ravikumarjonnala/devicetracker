package com.vodafone.devicetracker;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.vodafone.devicetracker.controller.DeviceInformationController;
import com.vodafone.devicetracker.service.DeviceInformationService;

@SpringBootTest
@DisplayName("Ensuring that required beans are created")
class DevicetrackerApplicationTests {
    @Autowired
    public DeviceInformationController deviceInformationController;
    @Autowired
    public DeviceInformationService deviceInformationService;

    @Test
    void areBeansInPlace() {
        assertNotNull(deviceInformationController, "DeviceInformationController failed to be created");
        assertNotNull(deviceInformationService, "DeviceInformationService failed to be created");
    }

}

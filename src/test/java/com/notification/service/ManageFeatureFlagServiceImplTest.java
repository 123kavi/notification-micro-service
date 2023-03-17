package com.notification.service;

import com.notification.dto.FeatureFlagsDto;
import com.notification.exception.SystemWarningException;
import com.notification.util.Utility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ManageFeatureFlagServiceImplTest {

    @Autowired
    private ManageFeatureFlagService featureFlagService;

    @Test
    void isAllExistingFeatureFlagsReturnsSuccessfully() {
        Assertions.assertDoesNotThrow(() -> featureFlagService.getAllExistingFeatureFlags());
    }

    @Test
    void isFeatureFlagsSavesSuccessfullyForValidDataInput() {
        // Prerequisite parameters needed
        int i = new Random().nextInt(10) + 1;
        String featureFlagName = "Test Feature Flag Name" + i;
        String featureFlagDescription = "Test feature Flag Description" + i;
        boolean status = Utility.ACTIVE_STATUS_TRUE;
        String userName = "System";

        // Feature flag dto initialization
        FeatureFlagsDto featureFlagsDto = new FeatureFlagsDto();
        featureFlagsDto.setFeatureFlagName(featureFlagName);
        featureFlagsDto.setFeatureFlagDescription(featureFlagDescription);
        featureFlagsDto.setStatus(status);

        Assertions.assertDoesNotThrow(() -> featureFlagService.saveNewFeatureFlagDetails(featureFlagsDto, userName));
    }

    @ParameterizedTest
    @CsvSource({"order_confirmation_email_to_customer," + Utility.EX_FEATURE_FLAG_NAME_ALREADY_TAKEN})
    void isFeatureFlagsSaveThrowsFeatureFlagNameAlreadyTakenExceptionForDuplicatedFlagName(String inputFeatureFlagName,
                                                                                           String expectedMessage) {
        // Prerequisite parameters needed
        String featureFlagDescription = "Test feature Flag Description";
        boolean status = Utility.ACTIVE_STATUS_TRUE;
        String userName = "System";

        // Feature flag dto initialization
        FeatureFlagsDto featureFlagsDto = new FeatureFlagsDto();
        featureFlagsDto.setFeatureFlagName(inputFeatureFlagName);
        featureFlagsDto.setFeatureFlagDescription(featureFlagDescription);
        featureFlagsDto.setStatus(status);

        Exception exception = assertThrows(SystemWarningException.class, () -> {
            featureFlagService.saveNewFeatureFlagDetails(featureFlagsDto, userName);
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void isFeatureFlagsUpdatesSuccessfullyForValidDataInput() {
        // Prerequisite parameters needed
        long featureFlagId = 1;
        String featureFlagName = "Test Feature Flag Name";
        String featureFlagDescription = "Test feature Flag Description";
        boolean status = Utility.ACTIVE_STATUS_TRUE;
        String userName = "System";

        // Feature flag dto initialization
        FeatureFlagsDto featureFlagsDto = new FeatureFlagsDto();
        featureFlagsDto.setFeatureFlagId(featureFlagId);
        featureFlagsDto.setFeatureFlagName(featureFlagName);
        featureFlagsDto.setFeatureFlagDescription(featureFlagDescription);
        featureFlagsDto.setStatus(status);

        Assertions.assertDoesNotThrow(() -> featureFlagService.updateFeatureFlagDetails(featureFlagsDto, userName));
    }

    @ParameterizedTest
    @CsvSource({"0," + Utility.EX_FEATURE_FLAG_ID_NOT_FOUND})
        // passing a not existing id
    void isFeatureFlagsUpdatesThrowsIncorrectFeatureFlagIdProvidedExceptionForInvalidFeatureFlagId(long inputFeatureFlagId,
                                                                                                   String expectedMessage) {
        // Prerequisite parameters needed
        String featureFlagName = "Test Feature Flag Name";
        String featureFlagDescription = "Test feature Flag Description";
        boolean status = Utility.ACTIVE_STATUS_TRUE;
        String userName = "System";

        // Feature flag dto initialization
        FeatureFlagsDto featureFlagsDto = new FeatureFlagsDto();
        featureFlagsDto.setFeatureFlagId(inputFeatureFlagId);
        featureFlagsDto.setFeatureFlagName(featureFlagName);
        featureFlagsDto.setFeatureFlagDescription(featureFlagDescription);
        featureFlagsDto.setStatus(status);

        Exception exception = assertThrows(SystemWarningException.class, () -> {
            featureFlagService.updateFeatureFlagDetails(featureFlagsDto, userName);
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @ParameterizedTest
    @CsvSource({"," + Utility.EX_ALL_FIELDS_REQUIRED})
    void isFeatureFlagsUpdatesThrowsAllFieldsRequiredExceptionForPassingNullForFeatureFlagsDto(FeatureFlagsDto featureFlagsDto,
                                                                                               String expectedMessage) {
        // Prerequisite parameters needed
        String userName = "System";

        Exception exception = assertThrows(SystemWarningException.class, () -> {
            featureFlagService.updateFeatureFlagDetails(featureFlagsDto, userName);
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @ParameterizedTest
    @CsvSource({"," + Utility.EX_FEATURE_FLAG_NAME_REQUIRED})
    void isFeatureFlagsUpdatesThrowsFeatureFlagNameRequiredExceptionForForFeatureFlagNameNull(String featureFlagName,
                                                                                              String expectedMessage) {
        // Prerequisite parameters needed
        long featureFlagId = 1;
        String featureFlagDescription = "Test feature Flag Description";
        boolean status = Utility.ACTIVE_STATUS_TRUE;
        String userName = "System";

        // Feature flag dto initialization
        FeatureFlagsDto featureFlagsDto = new FeatureFlagsDto();
        featureFlagsDto.setFeatureFlagId(featureFlagId);
        featureFlagsDto.setFeatureFlagName(featureFlagName);
        featureFlagsDto.setFeatureFlagDescription(featureFlagDescription);
        featureFlagsDto.setStatus(status);

        Exception exception = assertThrows(SystemWarningException.class, () -> {
            featureFlagService.updateFeatureFlagDetails(featureFlagsDto, userName);
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @ParameterizedTest
    @CsvSource({" ," + Utility.EX_FEATURE_FLAG_NAME_REQUIRED})
    void isFeatureFlagsUpdatesThrowsFeatureFlagNameRequiredExceptionForEmptyFeatureFlagName(String featureFlagName,
                                                                                            String expectedMessage) {
        // Prerequisite parameters needed
        long featureFlagId = 1;
        String featureFlagDescription = "Test feature Flag Description";
        boolean status = Utility.ACTIVE_STATUS_TRUE;
        String userName = "System";

        // Feature flag dto initialization
        FeatureFlagsDto featureFlagsDto = new FeatureFlagsDto();
        featureFlagsDto.setFeatureFlagId(featureFlagId);
        featureFlagsDto.setFeatureFlagName(featureFlagName);
        featureFlagsDto.setFeatureFlagDescription(featureFlagDescription);
        featureFlagsDto.setStatus(status);

        Exception exception = assertThrows(SystemWarningException.class, () -> {
            featureFlagService.updateFeatureFlagDetails(featureFlagsDto, userName);
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @ParameterizedTest
    @CsvSource({"," + Utility.EX_FEATURE_FLAG_DESCRIPTION_REQUIRED})
    void isFeatureFlagsUpdatesThrowsFeatureFlagDescRequiredExceptionForForFeatureFlagDescNull(String featureFlagDesc,
                                                                                              String expectedMessage) {
        // Prerequisite parameters needed
        long featureFlagId = 1;
        String featureFlagName = "Test feature Flag Name";
        boolean status = Utility.ACTIVE_STATUS_TRUE;
        String userName = "System";

        // Feature flag dto initialization
        FeatureFlagsDto featureFlagsDto = new FeatureFlagsDto();
        featureFlagsDto.setFeatureFlagId(featureFlagId);
        featureFlagsDto.setFeatureFlagName(featureFlagName);
        featureFlagsDto.setFeatureFlagDescription(featureFlagDesc);
        featureFlagsDto.setStatus(status);

        Exception exception = assertThrows(SystemWarningException.class, () -> {
            featureFlagService.updateFeatureFlagDetails(featureFlagsDto, userName);
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @ParameterizedTest
    @CsvSource({" ," + Utility.EX_FEATURE_FLAG_DESCRIPTION_REQUIRED})
    void isFeatureFlagsUpdatesThrowsFeatureFlagDescRequiredExceptionForEmptyFeatureFlagDesc(String featureFlagDesc,
                                                                                            String expectedMessage) {
        // Prerequisite parameters needed
        long featureFlagId = 1;
        String featureFlagName = "Test feature Flag Name";
        boolean status = Utility.ACTIVE_STATUS_TRUE;
        String userName = "System";

        // Feature flag dto initialization
        FeatureFlagsDto featureFlagsDto = new FeatureFlagsDto();
        featureFlagsDto.setFeatureFlagId(featureFlagId);
        featureFlagsDto.setFeatureFlagName(featureFlagName);
        featureFlagsDto.setFeatureFlagDescription(featureFlagDesc);
        featureFlagsDto.setStatus(status);

        Exception exception = assertThrows(SystemWarningException.class, () -> {
            featureFlagService.updateFeatureFlagDetails(featureFlagsDto, userName);
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @ParameterizedTest
    @CsvSource({"," + Utility.EX_USER_NAME_REQUIRED})
    void isFeatureFlagsUpdatesThrowsUserNameRequiredExceptionForForFeatureUserNameNull(String userName,
                                                                                       String expectedMessage) {
        // Prerequisite parameters needed
        long featureFlagId = 1;
        String featureFlagName = "Test feature Flag Name";
        String featureFlagDescription = "Test feature Flag Description";
        boolean status = Utility.ACTIVE_STATUS_TRUE;

        // Feature flag dto initialization
        FeatureFlagsDto featureFlagsDto = new FeatureFlagsDto();
        featureFlagsDto.setFeatureFlagId(featureFlagId);
        featureFlagsDto.setFeatureFlagName(featureFlagName);
        featureFlagsDto.setFeatureFlagDescription(featureFlagDescription);
        featureFlagsDto.setStatus(status);

        Exception exception = assertThrows(SystemWarningException.class, () -> {
            featureFlagService.updateFeatureFlagDetails(featureFlagsDto, userName);
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @ParameterizedTest
    @CsvSource({" ," + Utility.EX_USER_NAME_REQUIRED})
    void isFeatureFlagsUpdatesThrowsUserNameRequiredExceptionForEmptyUserName(String userName,
                                                                              String expectedMessage) {
        // Prerequisite parameters needed
        long featureFlagId = 1;
        String featureFlagName = "Test feature Flag Name";
        String featureFlagDescription = "Test feature Flag Description";
        boolean status = Utility.ACTIVE_STATUS_TRUE;

        // Feature flag dto initialization
        FeatureFlagsDto featureFlagsDto = new FeatureFlagsDto();
        featureFlagsDto.setFeatureFlagId(featureFlagId);
        featureFlagsDto.setFeatureFlagName(featureFlagName);
        featureFlagsDto.setFeatureFlagDescription(featureFlagDescription);
        featureFlagsDto.setStatus(status);

        Exception exception = assertThrows(SystemWarningException.class, () -> {
            featureFlagService.updateFeatureFlagDetails(featureFlagsDto, userName);
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}

package com.notification.util;

import com.notification.exception.SystemWarningException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

@UtilityClass
public class Utility {

    /**
     * Common System Specific Constant Values
     */
    public static final boolean ACTIVE_STATUS_TRUE = true;
    public static final String ENCODING_FORMAT_UTF_8 = "UTF-8";
    public static final String EMPTY_STR = "";
    public static final String CRON_EXP_CHAR_ALL = "*";
    public static final String CRON_EXP_CHAR_ANY = "?";
    public static final String DOUBLE_NEW_LINE_BREAK = "\n\n";
    public static final String CURLY_BRAZE_END = "}";
    public static final String ERROR_JSON_REGEX = "\"error\":";
    public static final String PDF_FILE_FORMAT_REGEX = "^.+\\.(([pP][dD][fF]))$";
    public static final String NOT_FOUND = "NOT_FOUND";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String SWAGGER_API_KEY_NAME_JWT = "JWT";
    public static final String UTC_TIME_ZONE_ALIAS = "UTC";
    public static final String HEADER = "header";
    public static final String GLOBAL_SCOPE = "global";
    public static final String CRON_EXPRESSION = "0 %s %s %s %s %s"; // Sec Min Hour Day Month DayOfWeek

    public static final String PLIVO_SMS_SERVICE_QUALIFIER = "plivoSmsService";
    public static final String TWILIO_SMS_SERVICE_QUALIFIER = "twilioSmsService";

    /**
     * System Calender Date Constants
     */
    public static final int MAX_CALENDER_MONTH_COUNT = 12;
    public static final int MAX_CALENDER_DAY_OF_MONTH_COUNT = 31;
    public static final int MAX_CALENDER_HOUR_COUNT = 24;
    public static final int MAX_CALENDER_MIN_COUNT = 59;
    public static final int MAX_CALENDER_DAY_OF_WEEK_COUNT = 7;

    /**
     * Freemarker Template Attribute Constants
     */
    public static final String ORDERS_LIST_MAP_KEY = "ordersList";
    public static final String IMAGE_URL_MAP_KEY = "topBannerAdUrl";
    public static final String IMAGE_URL_IS_NOTNULL_AND_NOT_EMPTY_MAP_KEY = "isNotNullAndNotEmpty";

    /**
     * Email HTML Content Attribute Constants
     */
    public static final String ITEM_DETAILS_SECTION_IDENTIFIER = "<item-details-section>";
    public static final String TOP_BANNER_AD_SECTION_IDENTIFIER = "<top-banner-advertisement>";
    public static final String ITEM_DETAILS_SECTION_CONTENT_PATTERN = "%<item-details-section>%";
    public static final String TOP_BANNER_AD_SECTION_CONTENT_PATTERN = "%<top-banner-advertisement>%";
    public static final String NON_EDITABLE_IDENTIFIER_START = "<non-editable>";
    public static final String NON_EDITABLE_IDENTIFIER_END = "</non-editable>";

    /**
     * System cache values
     */
    public static final String REDIS_CACHE_MAP_KEY_SEND_EMAIL = "sendEmailDto";
    public static final String CACHE_VAL_FIRST_BY_TEMPLATE_NAME = "firstByTemplateName";
    public static final String CACHE_VAL_EXISTS_BY_TEMPLATE_NAME_AND_CONTENT_LIKE = "existsByTemplateNameAndContentLike";
    public static final String CACHE_VAL_FIND_ALL_FEATURE_FLAGS = "findAllFeatureFlags";
    public static final String CACHE_VAL_FIND_TOKENS_BY_USER_ID = "findTokensByUserId";
    public static final String CACHE_EXISTS_BY_NAME = "existsByName";
    public static final String CACHE_EXISTS_BY_NAME_AND_STATUS = "existsByNameAndStatus";
    public static final String CACHE_EXISTS_BY_JOB_ID = "existsByJobId";
    public static final String CACHE_FIND_FIRST_BY_JOB_ID = "findFirstByJobId";
    public static final String CACHE_FIND_ALL_BY_SCHEDULED_STATUS_AND_ACTIVE_STATUS = "findAllByScheduledStatusAndActiveStatus";
    public static final String CACHE_GET_MULTI_SCHEDULER_DETAILS = "getMultiSchedulerDetails";
    public static final String CACHE_EXTRACT_SINGLE_EMAIL_TEMPLATE_DATA = "extractSingleEmailTemplateData";
    public static final String CACHE_SET_HTML_TOP_BANNER_SECTION = "setHtmlTopBannerAdSection";
    public static final String CACHE_SET_ITEM_DETAILS_SECTION = "setItemDetailsSection";
    public static final String CACHE_VERIFY_TOKEN_VALIDITY = "verifyTokenValidity";

    /**
     * System Exception Message Responses
     */
    public static final String EX_ROOT = "Something went wrong! Please contact administrator.";
    public static final String EX_ALL_FIELDS_REQUIRED = "All fields are required!";
    public static final String EX_UNSUPPORTED_OPERATION = "This functionality is unsupported in current version of the application!";
    public static final String EX_NON_EDITABLE_ATTRIBUTE_MODIFIED = "Non editable data has been modified! Please reset them and try again..";
    public static final String EX_MISMATCH_NON_EDITABLE_ATTRIBUTE_COUNT = "Non editable data not allowed to be newly added nor remove existing!";
    public static final String EX_INVALID_EMAIL_SUBJECT = "Email subject can't be empty!";
    public static final String EX_INVALID_NO_OF_CONTENT_DATA_MAP_PROVIDED = "Provided no of content data is not sufficient!";
    public static final String EX_FEATURE_FLAG_ID_NOT_FOUND = "No data found for feature flag id!";
    public static final String EX_FEATURE_FLAG_NAME_REQUIRED = "Feature-Flag Name is required!";
    public static final String EX_FEATURE_FLAG_DESCRIPTION_REQUIRED = "Feature-Flag Description is required!";
    public static final String EX_FIREBASE_CLIENT_TOKEN_REQUIRED = "Firebase client token is required!";
    public static final String EX_DEVICE_PLATFORM_REQUIRED = "Device platform is required!";
    public static final String EX_FILE_NAME_REQUIRED = "File name is required!";
    public static final String EX_FILE_DATA_REQUIRED = "File data is required!";
    public static final String EX_TEMPLATE_NAME_REQUIRED = "Template name is required!";
    public static final String EX_MOBILE_NO_REQUIRED = "Mobile Number is required!";
    public static final String EX_MAIL_DETAILS_REQUIRED = "Mail details are required!";
    public static final String EX_NOTIFICATION_DETAILS_REQUIRED = "Notification details are required!";
    public static final String EX_USER_NAME_REQUIRED = "User name required!";
    public static final String EX_INVALID_TEMPLATE_NAME = "Unable to find data for the template name provided!";
    public static final String EX_UNABLE_TO_SUBSCRIBE_CHANNELS_WITH_REASON = "Unable to subscribe channels to send notifications due to %s for user id: %s!";
    public static final String EX_DATA_NOT_FOUND_FOR_USER_ID = "Data not found for user Id!";
    public static final String EX_FIREBASE_TOKEN_VERIFICATION_FAILED = "Firebase token verification failed!";
    public static final String EX_FIREBASE_TOKEN_NOT_FOUND = "Requested token not found for the given user!";
    public static final String EX_INVALID_MESSAGE_TITLE = "Message title can't be empty!";
    public static final String EX_INVALID_MESSAGE_BODY = "Message body can't be empty!";
    public static final String EX_FEATURE_FLAG_STATUS_DISABLED = "Feature flag status is disabled for this template name! Please enable it to proceed";
    public static final String EX_NO_RESULTS_FOUND_FOR_TEMPLATE_NAME = "No results found for the provided templateName: %s";
    public static final String EX_INVALID_MOBILE_NUMBER_FORMAT = "'%s' is not a valid phone number! Please provide a valid mobile number with country code";
    public static final String EX_FEATURE_FLAG_NAME_ALREADY_TAKEN = "Feature flag name is already taken! Please try different name";
    public static final String EX_INVALID_FILE_EXTENSION = "Invalid file extension! PDF file format is only supported";
    public static final String EX_JOB_ID_ALREADY_TAKEN = "Provided JobId: \"%s\" is already taken! Please provide a different JobId to proceed";
    public static final String EX_JOB_ID_NOT_FOUND = "Data not found for job id %s!";
    public static final String EX_SINGLE_EMAIL_SCHEDULER_FAILS = "Single Email Template Scheduler running failed due to invalid job id: \"%s\" provided!";
    public static final String EX_MULTI_EMAIL_SCHEDULER_FAILS = "Multi Email Template Scheduler running failed due to invalid job id: \"%s\" provided!";
    public static final String EX_REQUEST_DATA_REQUIRED = "Request data is required!";
    public static final String EX_INVALID_MONTH = "Invalid month provided!";
    public static final String EX_INVALID_DAY = "Invalid day of month provided!";
    public static final String EX_INVALID_HOUR = "Invalid hour provided!";
    public static final String EX_INVALID_MINUTE = "Invalid minute provided!";
    public static final String EX_INVALID_WEEK = "Invalid day of week provided!";

    /**
     * System Success Message Responses
     */
    public static final String MSG_EMAIL_SENT_SUCCESSFULLY = "User successfully notified via email, sms and push notification!";
    public static final String MSG_FEATURE_FLAG_DETAILS_UPDATED_SUCCESSFULLY = "Feature flag details updated successfully";
    public static final String MSG_EMAIL_CONTENT_UPDATED_SUCCESSFULLY = "Email content updated successfully";
    public static final String MSG_SUCCESSFULLY_SENT_NOTIFICATIONS = "Message was successfully send to %d subscribers with %d failures!";
    public static final String MSG_MESSAGE_CONTENT_UPDATED_SUCCESSFULLY = "Message content updated successfully!";
    public static final String MSG_FEATURE_FLAG_DETAILS_SAVED_SUCCESSFULLY = "Feature flag details saved successfully!";
    public static final String MSG_EMAIL_GROUP_SCHEDULED_SUCCESSFULLY = "Email Group Scheduled Successfully!";
    public static final String MSG_EMAIL_GROUP_SCHEDULE_TERMINATED_SUCCESSFULLY = "Schedule with jobId: %s was successfully terminated!";

    /**
     * System Log Messages
     */
    public static final String LOG_EMAIL_SENT_SUCCESSFULLY = "Email sent successfully to email: %s for user id: %d";
    public static final String LOG_SENT_EMAIL_DETAILS_SAVED_SUCCESSFULLY = "Sent email details saved successfully in the database for user id: %s";
    public static final String LOG_EMAIL_CONTENT_UPDATED_SUCCESSFULLY = "Email subject/content details updated successfully for templateName: %s";
    public static final String LOG_BACKEND_CACHE_CLEAN_FOR_TEMPLATE_NAME = "Backend cache cleaned successfully for templateName: %s with key: %s | value: %s";
    public static final String LOG_BACKEND_CACHE_CLEAN_FOR_USER_ID = "Backend cache cleaned successfully for user id: %d with key: %s | value: %s";
    public static final String LOG_BACKEND_CACHE_CLEAN_FOR_FEATURE_FLAG_ID = "Backend cache cleaned successfully for feature flag id: %d with value: %s";
    public static final String LOG_SMS_CONTENT_UPDATED_SUCCESSFULLY = "SMS message title/body details updated successfully for templateName: %s";
    public static final String LOG_PUSH_NOTIFICATION_DETAILS_SAVED_SUCCESSFULLY = "Push notification details saved successfully for user id: %s with platform: %s";
    public static final String LOG_USER_FIREBASE_FCM_TOKENS_SUBSCRIBED_SUCCESSFULLY = "Subscribed Firebase FCM tokens: %s for user id: %s | SuccessAttempts=%d, FailedAttempts=%d, TotalAttempts=%d";
    public static final String LOG_FIREBASE_PUSH_NOTIFICATIONS_SUCCESSFULLY_SENT = "Push Notifications sent successfully for %d subscribed devices of user id: %d";
    public static final String LOG_USER_FIREBASE_FCM_TOKENS_SUCCESSFULLY_UPDATED = "Firebase FCM token updated successfully for user: %d";
    public static final String LOG_USER_REQUESTED_FIREBASE_FCM_REFRESH_TOKEN = "Requested firebase FCM refresh token for user id: %d with old token: %s";
    public static final String LOG_USER_REQUESTED_OLD_FCM_TOKEN_NOT_FOUND = "Requested firebase FCM token: %s not found for user id: %d";
    public static final String LOG_SENT_PUSH_NOTIFICATIONS_SUCCESSFULLY_SAVED = "Sent push notifications successfully saved in the database for user id: %d";
    public static final String LOG_FEATURE_FLAG_DETAILS_SUCCESSFULLY_UPDATED = "Feature flag details updated successfully for featureFlagId=%d with status=%s!";
    public static final String LOG_SMS_NOTIFICATIONS_SUCCESSFULLY_SENT = "SMS notification successfully sent to user id: %d for mobileNo: %s";
    public static final String LOG_SENT_SMS_DETAILS_SUCCESSFULLY_SAVED = "Sent SMS details successfully saved in the database for user id: %d for sms response: %s";
    public static final String LOG_FEATURE_FLAG_DETAILS_SUCCESSFULLY_SAVED = "Feature flag details successfully saved in the database with featureFlagId: %d, featureFlagName: %s, status: %s";
    public static final String LOG_SCHEDULED_EMAIL_GROUP = "Scheduled email group with job id: %s and cron expression: %s";
    public static final String LOG_SINGLE_EMAIL_TEMPLATE_SCHEDULER_STARTED = "Single Email Template Scheduler started for JobId: %s with data: { Minute: %s, Hour: %s, DayOfMonth: %s, Month: %s, DayOfWeek: %s, noOfEmailsToSend: %s}";
    public static final String LOG_SINGLE_EMAIL_TEMPLATE_SCHEDULER_RUNNING = "Single Email Template Scheduler Running with job id: %s, cronExp: %s";
    public static final String LOG_MULTIPLE_EMAIL_TEMPLATE_SCHEDULER_STARTED = "Multiple Email Template Scheduler started for JobId: %s with data: { Minute: %s, Hour: %s, DayOfMonth: %s, Month: %s, DayOfWeek: %s, noOfEmailsToSend: %s}";
    public static final String LOG_TOTAL_NO_OF_SCHEDULES_TO_RESTART = "Total no of schedules detected to be Restarted: %d";
    public static final String LOG_SCHEDULER_RESTARTING = "Restarting scheduler with job id: %s, cronExp: %s";
    public static final String LOG_SCHEDULER_RESTARTED = "Scheduler restarted successfully for job id: %s, cronExp: %s";


    /**
     * Validates whether a given String value to determine it's not null and not empty
     * @param text : java.lang.String
     * @return boolean
     */
    public static boolean isNotNullAndNotEmpty(String text) {
        return null != text && !text.isBlank();
    }

    /**
     * Validates whether a given String value to determine it's null or empty
     * @param text : java.lang.String
     * @return boolean
     */
    public static boolean isNullOrEmpty(String text) {
        return null == text || text.isBlank();
    }

    /**
     * Validates a given Mobile Number validates with country code accordingly
     * @param mobileNo : java.lang.String
     * @return boolean
     */
    public static boolean isMobileNoValidatesWithCountryCode(String mobileNo) {
        return isNotNullAndNotEmpty(mobileNo)
                && mobileNo.matches("\\+\\d(\\d{3}){2}\\d{4}");
    }

    /**
     * @param val : java.lang.Integer
     * @param max : int
     * @param errorMessage : java.lang.String
     * @return boolean
     * @throws SystemWarningException
     */
    public static boolean isNotNullAndAndLessThan(Integer val, int max, String errorMessage) throws SystemWarningException {
        if (null == val) {
            return false;
        } else if (0 < val && val <= max) {
            return true;
        } else {
            throw new SystemWarningException(errorMessage);
        }
    }

    /**
     * @param val : java.lang.Integer
     * @param max : int
     * @param errorMessage : java.lang.String
     * @return boolean
     * @throws SystemWarningException
     */
    public static boolean isNotNullAndInBetween(Integer val, int max, String errorMessage) throws SystemWarningException {
        if (null == val) {
            return false;
        } else if (0 <= val && val <= max) {
            return true;
        } else {
            throw new SystemWarningException(errorMessage);
        }
    }

    /**
     * @param actualMessageContent : java.lang.String
     * @return java.util.List<String>
     */
    public static List<String> extractNonEditableContents(String actualMessageContent) {
        if (actualMessageContent.contains(Utility.NON_EDITABLE_IDENTIFIER_START)
                && actualMessageContent.contains(Utility.NON_EDITABLE_IDENTIFIER_END)) {
            return Arrays.asList(StringUtils.substringsBetween(
                    actualMessageContent, Utility.NON_EDITABLE_IDENTIFIER_START, Utility.NON_EDITABLE_IDENTIFIER_END));
        }
        return new ArrayList<>();
    }

    /**
     * @param objectMap : java.util.List<?>
     * @return java.lang.String
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    public static String convertListIntoJsonString(List<?> objectMap) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(objectMap);
    }

    /**
     * @param objectMap : java.util.Map<?, Object>
     * @return java.lang.String
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    public static String convertMapIntoJsonString(Map<?, Object> objectMap) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(objectMap);
    }

    /**
     * @param jsonStr : java.lang.String
     * @return java.util.Map<?, ?>
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    public static Map<?, ?> convertJsonStringToMap(String jsonStr) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonStr, Map.class);
    }

    /**
     * @param jsonStr : java.lang.String
     * @return java.util.List<?>
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    public static List<?> convertJsonStringToList(String jsonStr) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonStr, List.class);
    }

    /**
     * @return java.util.TimeZone
     */
    public TimeZone getSystemCurrentUtcTimeZone() {


        return TimeZone.getTimeZone(Utility.UTC_TIME_ZONE_ALIAS);
    }



//    public TimeZone getSystemCurrentUtcTimeZone() {
//        // Get the current UTC time zone
//        TimeZone utcTimeZone = TimeZone.getTimeZone(Utility.UTC_TIME_ZONE_ALIAS);
//
//        // Create a date object with the current UTC time
//        Date date = new Date();
//
//        // Create a date formatter with the desired output format and time zone
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
//
//        // Format the date in the desired time zone
//        String formattedDate = formatter.format(date);
//
//        // Return the time zone object for the formatted date
//        return TimeZone.getTimeZone("Asia/Kolkata");
//    }
//









    /**
     * @param bytes : byte[]
     * @return java.lang.String
     */
    public static String getByteArrayEncodedString(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * @param encodedString : java.lang.String
     * @return byte[]
     */
    public static byte[] getDecodedByteArray(String encodedString) {
        return Base64.getDecoder().decode(encodedString);
    }
}

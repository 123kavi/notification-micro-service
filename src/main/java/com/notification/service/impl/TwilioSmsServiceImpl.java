package com.notification.service.impl;

import com.notification.dto.request.NotificationRequestDto;
import com.notification.repository.MailContentRepository;
import com.notification.util.Utility;
import com.twilio.rest.api.v2010.account.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Slf4j
@Service(Utility.TWILIO_SMS_SERVICE_QUALIFIER)
public class TwilioSmsServiceImpl extends CogSmsServiceImpl {

    /**
     * @param mailContentRepository : com.cloudofgoods.notification.repository.MailContentRepository
     * @param cacheManager : org.springframework.cache.CacheManager
     */
    public TwilioSmsServiceImpl(MailContentRepository mailContentRepository, CacheManager cacheManager) {
        super(mailContentRepository, cacheManager);
    }

    /**
     * @param userId : long
     * @param templateName : java.lang.String
     * @param mobileNo : java.lang.String
     * @param notification : com.cloudofgoods.notification.dto.request.NotificationRequestDto
     * @return com.twilio.rest.api.v2010.account.Message
     * @throws java.lang.UnsupportedOperationException
     */
    @Override
    public Message sendSms(long userId, String templateName, String mobileNo,
                           NotificationRequestDto notification) throws UnsupportedOperationException {
        log.error(Utility.EX_UNSUPPORTED_OPERATION);
        throw new UnsupportedOperationException(Utility.EX_UNSUPPORTED_OPERATION);
    }
}

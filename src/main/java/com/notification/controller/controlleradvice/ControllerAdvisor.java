package com.notification.controller.controlleradvice;

import com.notification.exception.SystemRootException;
import com.notification.exception.SystemInfoException;
import com.notification.exception.SystemWarningException;
import com.notification.util.Utility;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    /**
     * @param ex : java.lang.Exception
     * @return : org.springframework.http.ResponseEntity<String>
     * System Info Exception Handler
     */
    @ExceptionHandler(value = SystemInfoException.class)
    protected ResponseEntity<String> handleCogInfoException(Exception ex) {
        logger.info(ex.getMessage());
        return ResponseEntity.status(599).body(ex.getMessage());
    }

    /**
     * @param ex : java.lang.Exception
     * @return : org.springframework.http.ResponseEntity<String>
     * System Warning Exception Handler
     */
    @ExceptionHandler(value = SystemWarningException.class)
    protected ResponseEntity<String> handleCogWarningException(Exception ex) {
        logger.warn(ex.getMessage());
        return ResponseEntity.status(598).body(ex.getMessage());
    }

    /**
     * @param ex : java.lang.Exception
     * @return : org.springframework.http.ResponseEntity<String>
     * System Internal Server Exception Handler
     */
    @ExceptionHandler(value = {SystemRootException.class, Exception.class})
    protected ResponseEntity<String> handleCogInternalServerException(Exception ex) {
        logger.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Utility.EX_ROOT);
    }
}

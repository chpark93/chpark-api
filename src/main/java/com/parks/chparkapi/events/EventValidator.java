package com.parks.chparkapi.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {

    public void validate(EventDTO eventDTO, Errors errors) {

        //price validate
        if( eventDTO.getBasePrice() > eventDTO.getMaxPrice() && eventDTO.getMaxPrice() > 0 ) {
            errors.rejectValue("basePrice", "wrongValue", "BasePrice is wrong.");
            errors.rejectValue("maxPrice", "wrongValue", "MaxPrice is wrong.");
        }

        //date validate
        LocalDateTime endEventDateTime = eventDTO.getEndEventDateTime();
        if( endEventDateTime.isBefore(eventDTO.getBeginEventDateTime())
            || endEventDateTime.isBefore(eventDTO.getCloseEnrollmentDateTime())
            || endEventDateTime.isBefore(eventDTO.getBeginEnrollmentDateTime()) ) {

            errors.rejectValue("endEventDateTime", "wrongValue", "EndEventDateTime is Wrong.");
        }

        // TODO beginEventDateTime
        // TODO CloseEnrollmentDateTime
    }
}

package com.eventostec.api.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.eventostec.api.domain.event.EventRequestDTO;

@Component
public class EventRequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return EventRequestDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EventRequestDTO dto = (EventRequestDTO) target;

        if (dto.title() == null || dto.title().isBlank()) {
            errors.rejectValue("title", "title.blank", "O título é obrigatório");
        }

        if (dto.date() == null || dto.date() < System.currentTimeMillis()) {
            errors.rejectValue("date", "date.invalid", "A data deve ser hoje ou no futuro");
        }

        if (dto.image() == null || dto.image().isEmpty()) {
            errors.rejectValue("image", "image.empty", "A imagem é obrigatória");
        }
    }
}

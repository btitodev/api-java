package com.eventostec.api.domain.event;

import java.util.Date;
import java.util.List;
import java.util.UUID;


public record EventDetailsDTO(
        UUID id,
        String title,
        String description,
        String imageUrl,
        String eventUrl,
        Boolean remote,
        Date date,
        String city,
        String state,
        List<CouponDTO> coupons) {

    public record CouponDTO(
            String code,
            Integer discount,
            Date valid) {
    }
}

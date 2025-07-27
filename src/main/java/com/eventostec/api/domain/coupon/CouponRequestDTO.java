package com.eventostec.api.domain.coupon;

import java.util.Date;
import java.util.UUID;

public record CouponRequestDTO (
    String code,
    Integer discount,
    Date valid,
    UUID eventId
) {

}

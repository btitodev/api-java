package com.eventostec.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import com.eventostec.api.domain.coupon.Coupon;
import com.eventostec.api.domain.coupon.CouponRequestDTO;
import com.eventostec.api.domain.event.Event;
import com.eventostec.api.repositories.CouponRepository;

@Service
public class CouponService {

    @Autowired
    private CouponRepository repository;

    @Autowired
    private EventService eventService; 

    @PostMapping
    public Coupon createCoupon(CouponRequestDTO couponRequest) {
        Event event = eventService.getById(couponRequest.eventId());
        if (event == null) {
            throw new IllegalArgumentException("Event not found for ID: " + couponRequest.eventId());
        }

        
        Coupon newCoupon = new Coupon();
        newCoupon.setCode(couponRequest.code());
        newCoupon.setDiscount(couponRequest.discount());
        newCoupon.setValid(couponRequest.valid());
        newCoupon.setEvent(event);

        repository.save(newCoupon);

        return newCoupon;
    }

}

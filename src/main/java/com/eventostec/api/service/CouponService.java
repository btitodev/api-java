package com.eventostec.api.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import com.eventostec.api.domain.coupon.Coupon;
import com.eventostec.api.domain.coupon.CouponRequestDTO;
import com.eventostec.api.domain.event.Event;
import com.eventostec.api.domain.event.EventResponseDTO;
import com.eventostec.api.repositories.CouponRepository;
import com.eventostec.api.repositories.EventRepository;

@Service
public class CouponService {

    @Autowired
    private CouponRepository repository;

    @Autowired
    private EventRepository eventRepository; 

    public Coupon createCoupon(CouponRequestDTO couponRequest) {
        Optional<Event> event = eventRepository.findById(couponRequest.eventId());
        if (event == null) {
            throw new IllegalArgumentException("Event not found for ID: " + couponRequest.eventId());
        }

        
        Coupon newCoupon = new Coupon();
        newCoupon.setCode(couponRequest.code());
        newCoupon.setDiscount(couponRequest.discount());
        newCoupon.setValid(couponRequest.valid());
        newCoupon.setEvent(event.get());

        repository.save(newCoupon);

        return newCoupon;
    }


    public List<Coupon> getCouponsByEventId(UUID eventId) {
        return repository.findByEventId(eventId);
    }
}

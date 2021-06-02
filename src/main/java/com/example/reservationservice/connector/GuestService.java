package com.example.reservationservice.connector;

import com.example.reservationservice.model.dto.GuestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(value = "${guest-service.name}", url = "${guest-service.host}", path = "${guest-service.path}")
public interface GuestService {
  @GetMapping("/{guestId}")
  GuestDto getGuestById(@PathVariable String guestId);
}

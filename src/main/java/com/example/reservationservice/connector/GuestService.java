package com.example.reservationservice.connector;

import com.example.reservationservice.model.dto.RoomDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "${guest-service.name}", path = "${guest-service.path}")
public interface GuestService {
  @GetMapping("/{guestId}")
  List<RoomDto> getGuestById(@PathVariable String guestId);
}

package com.example.reservationservice.connector;

import com.example.reservationservice.model.dto.RoomDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "${hotel-service.name}", url = "${hotel-service.host}", path = "${hotel-service.path-hotel}")
public interface HotelService {
  @GetMapping("/{hotelId}/room")
  List<RoomDto> getRoomsByHotelId(@PathVariable String hotelId);
}

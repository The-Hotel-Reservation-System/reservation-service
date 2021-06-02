package com.example.reservationservice;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.reservationservice.connector.HotelService;
import com.example.reservationservice.controller.ReservationController;
import com.example.reservationservice.controller.ReservationControllerImpl;
import com.example.reservationservice.model.ReservationStatus;
import com.example.reservationservice.model.dto.ReservationDto;
import com.example.reservationservice.model.dto.RoomDto;
import com.example.reservationservice.model.entity.Reservation;
import com.example.reservationservice.model.request.CreateReservationRequest;
import com.example.reservationservice.repository.ReservationRepository;
import com.example.reservationservice.service.ReservationService;
import com.example.reservationservice.service.ReservationServiceImpl;
import feign.FeignException;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL,
    ids = "com.example:hotel:+:stubs:8082")
public class HotelContractTesting {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private HotelService hotelService;

  @Test
  public void validate_ListRoomFromHotel() throws Exception {
    var list = hotelService.getRoomsByHotelId("1");
    Assertions.assertEquals("ROOM_TYPE", list.get(0).getType());
    Assertions.assertEquals(BigInteger.ONE.toString(), list.get(0).getId());
    Assertions.assertEquals("1300", list.get(0).getPrice());
  }

  @Test
  public void validate_ListRoomFromHotel_HotelNotFound_ShouldThrowException() throws Exception {
    Assertions.assertThrows(FeignException.class, () -> hotelService.getRoomsByHotelId("2"));
  }

}

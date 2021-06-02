package com.example.reservationservice;

import com.example.reservationservice.connector.GuestService;
import com.example.reservationservice.connector.HotelService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigInteger;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL,
    ids = "com.example:guest:+:stubs:8081")
public class GuestContractTesting {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private GuestService guestService;

  @Test
  public void validate_GuestDtoModel() throws Exception {
    var guest = guestService.getGuestById("1");
    Assertions.assertEquals("NAME", guest.getName());
    Assertions.assertEquals(BigInteger.ONE, guest.getId());
    Assertions.assertEquals("PHONE", guest.getPhone());
    Assertions.assertEquals("EMAIL", guest.getEmail());
  }
}

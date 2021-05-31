package com.example.reservationservice.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class ReservationServiceConverter {
  private final String ERROR_CODE = "errorCode";
  private final String ERROR_MESSAGE = "errorMessage";
  private ObjectMapper mapper = new ObjectMapper();

  public JsonNode toJsonNode(ReservationExceptionResponse reservationExceptionResponse, String extraError) {
    var objectNode = mapper.createObjectNode();
    objectNode.put(ERROR_CODE, reservationExceptionResponse.getErrorCode());
    objectNode.put(ERROR_MESSAGE, reservationExceptionResponse.getErrorMessage() + extraError);
    return objectNode;
  }

  public JsonNode toJsonNode(String errorDetail) throws JsonProcessingException {
    return (this.mapper.readTree(errorDetail));
  }
}

package com.example.reservationservice.exception;

import brave.Tracer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

@Component
public class ReservationServiceConverter {
  private final String ERROR_CODE = "errorCode";
  private final String ERROR_MESSAGE = "errorMessage";
  private final String TRACE_ID = "traceId";
  private ObjectMapper mapper = new ObjectMapper();

  public JsonNode toJsonNode(ReservationExceptionResponse reservationExceptionResponse, String extraError , Tracer tracer) {
    var objectNode = mapper.createObjectNode();
    objectNode.put(ERROR_CODE, reservationExceptionResponse.getErrorCode());
    objectNode.put(ERROR_MESSAGE, reservationExceptionResponse.getErrorMessage() + extraError);
    objectNode.put(TRACE_ID, tracer.currentSpan().context().traceIdString());
    return objectNode;
  }

  public JsonNode toJsonNode(String errorDetail, Tracer tracer) throws JsonProcessingException {
    return ((ObjectNode) this.mapper.readTree(errorDetail)).put(TRACE_ID, tracer.currentSpan().context().traceIdString());
  }

}

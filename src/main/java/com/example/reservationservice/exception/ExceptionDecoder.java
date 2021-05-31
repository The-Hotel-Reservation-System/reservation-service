package com.example.reservationservice.exception;

import com.google.common.io.CharStreams;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ExceptionDecoder extends ErrorDecoder.Default {
  @Override
  public Exception decode(String methodKey, Response response) {
    try {
      var messageBody = CharStreams.toString(response.body().asReader());
      return new FeignException.FeignClientException(
          response.status(), messageBody, response.request(), messageBody.getBytes());

    } catch (Exception ex) {
      log.error("Error when read response.body", ex);
    }
    return super.decode(methodKey, response);
  }
}

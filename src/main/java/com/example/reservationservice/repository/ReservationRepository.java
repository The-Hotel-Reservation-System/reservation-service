package com.example.reservationservice.repository;

import com.example.reservationservice.model.entity.Reservation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.time.Instant;
import java.util.List;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, BigInteger> {
  @Query(value = "select room_id from reservation r where (:start, :end)  overlaps (r.start_date, r.end_date) AND r.room_Type = :roomType AND r.hotel_id = :hotelId", nativeQuery = true)
  List<String> getRoomBookedFromStartDateToEndDate(@Param("start") Instant startDate,
                                                   @Param("end") Instant endDate,
                                                   @Param("roomType") String roomType,
                                                   @Param("hotelId") BigInteger hotelId);

  List<Reservation> findByGuestId(String guestId);
}

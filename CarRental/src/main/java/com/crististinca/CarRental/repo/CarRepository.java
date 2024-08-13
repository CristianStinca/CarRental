package com.crististinca.CarRental.repo;

import com.crististinca.CarRental.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    @Query("SELECT c FROM Car c WHERE c.id NOT IN " +
            "(SELECT r.car.id FROM Rents r WHERE (r.rentalDateStart BETWEEN :startDate AND :endDate) OR (r.rentalDateEnd BETWEEN :startDate AND :endDate) OR (r.rentalDateStart < :startDate AND r.rentalDateEnd > :endDate))")
    List<Car> findAvailableCars(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}

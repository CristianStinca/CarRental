package com.crististinca.CarRentalAPI.repo;

import com.crististinca.CarRentalAPI.model.Rents;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentsRepository extends JpaRepository<Rents, Long> {
}

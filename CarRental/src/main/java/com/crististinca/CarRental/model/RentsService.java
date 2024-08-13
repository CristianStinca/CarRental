package com.crististinca.CarRental.model;

import com.crististinca.CarRental.repo.RentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RentsService {

    @Autowired
    private RentsRepository rentalRepository;

    @Autowired
    private CarService carService;

    public List<Rents> getAllRentals() {
        return rentalRepository.findAll();
    }

    public Rents rentCar(Long carId, LocalDate rentalDateStart, LocalDate rentalDateEnd, Client client) {
        Car car = carService.getCarById(carId);

        if (car == null) { throw new IllegalArgumentException("Car not found"); };

        Rents rental = new Rents();
        rental.setCar(car);
        rental.setClient(client);
        rental.setRentalDateStart(rentalDateStart);
        rental.setRentalDateEnd(rentalDateEnd);

        return rentalRepository.save(rental);
    }

}

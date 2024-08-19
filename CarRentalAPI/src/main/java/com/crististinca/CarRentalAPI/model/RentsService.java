package com.crististinca.CarRentalAPI.model;

import com.crististinca.CarRentalAPI.repo.RentsRepository;
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

    public Rents rentCar(Rents rent) {
        return this.rentCar(rent.getCar(), rent.getRentalDateStart(), rent.getRentalDateEnd(), rent.getClient());
    }

    public Rents rentCar(Car car, LocalDate rentalDateStart, LocalDate rentalDateEnd, Client client) {
//        Car car = carService.getCarById(carId);

        if (car == null) { throw new IllegalArgumentException("Car not found"); };

        Rents rental = new Rents();
        rental.setCar(car);
        rental.setClient(client);
        rental.setRentalDateStart(rentalDateStart);
        rental.setRentalDateEnd(rentalDateEnd);

        return rentalRepository.save(rental);
    }

}

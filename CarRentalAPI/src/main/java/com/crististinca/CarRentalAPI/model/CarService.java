package com.crististinca.CarRentalAPI.model;

import com.crististinca.CarRentalAPI.repo.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public void addCar(Car car) {
        carRepository.save(car);
    }

    public Car getCarById(Long id) {
        return carRepository.findById(id).orElse(null);
    }

    public List<Car> getAvailableCars(LocalDate startDate, LocalDate endDate) {
        return carRepository.findAvailableCars(startDate, endDate);
    }

    public boolean isCarAvailable(Car car, LocalDate startDate, LocalDate endDate) {
        return carRepository.findAvailableCars(startDate, endDate).contains(car);
    }

    public Car update(Car car) {
        return carRepository.save(car);
    }
}

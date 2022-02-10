package com.parkingcontrol.service;

import com.parkingcontrol.model.ParkingSpot;
import com.parkingcontrol.repository.ParkingSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParkingSpotService {

    private final ParkingSpotRepository repository;

    @Transactional
    public ParkingSpot save(ParkingSpot parkingSpotModel) {
        return repository.save(parkingSpotModel);
    }

    public boolean existsByApartmentAndBlock(String apartment, String block) {
        return repository.existsByApartmentAndBlock(apartment, block);
    }

    public boolean existsByParkingSpotNumber(String parkingSpotNumber) {
        return  repository.existsByParkingSpotNumber(parkingSpotNumber);
    }

    public boolean existsByLicensePlateCar(String licensePlateCar) {
        return repository.existsByLicensePlateCar(licensePlateCar);
    }

//    public List<ParkingSpot> findAll() {
//        return repository.findAll();
//    }

    public Page<ParkingSpot> findAlls(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<ParkingSpot> findById(UUID id) {
        return repository.findById(id);
    }

    @Transactional
    public void delete(ParkingSpot parkingSpot) {
        repository.delete(parkingSpot);
    }
}

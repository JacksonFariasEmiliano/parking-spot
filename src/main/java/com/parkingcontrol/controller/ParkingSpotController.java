package com.parkingcontrol.controller;

import com.parkingcontrol.model.ParkingSpot;
import com.parkingcontrol.model.dto.ParkingSpotDTO;
import com.parkingcontrol.service.ParkingSpotService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "parking-spot")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ParkingSpotController {

    private final ParkingSpotService service;

    @PostMapping
    public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDTO dto){

        if(service.existsByLicensePlateCar(dto.getLicensePlateCar())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: License Plate Car is Already in use!");
        }
        if(service.existsByParkingSpotNumber(dto.getParkingSpotNumber())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: parking Spot is already in use!");
        }
        if(service.existsByApartmentAndBlock(dto.getApartment(),dto.getBlock())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot already registered for this apartment/block!");
        }

        var parkingSpotModel = new ParkingSpot();
        BeanUtils.copyProperties(dto,parkingSpotModel);
        parkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(parkingSpotModel));
    }

    @GetMapping
    public ResponseEntity<Page<ParkingSpot>> getAllParkingSpots(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC)Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(service.findAlls(pageable));
    }

//    @GetMapping
//    public ResponseEntity<List<ParkingSpot>> getAllParkingSpot(){
//        return ResponseEntity.status(HttpStatus.OK).body(service.findAll());
//    }

    @GetMapping(value = "/{id}")
    public ResponseEntity <Object> getOneParkingSpot(@PathVariable(value = "id")UUID id) {
        Optional<ParkingSpot> parkingSpotOptional = service.findById(id);
        if (!parkingSpotOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotOptional.get());
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteParkingSpot(@PathVariable(value = "id")UUID id){
        Optional<ParkingSpot> parkingSpotOptional = service.findById(id);
        if (!parkingSpotOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found");
        }
        service.delete(parkingSpotOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Parking Spot deleted successfully");
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> updateParkingSpot(@PathVariable(value = "id")UUID id, @RequestBody @Valid ParkingSpotDTO dto){
        Optional<ParkingSpot> parkingSpotOptional = service.findById(id);
        if (!parkingSpotOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found");
        }

//        var parkingSpotModel = parkingSpotOptional.get();
//        parkingSpotModel.setParkingSpotNumber(dto.getParkingSpotNumber());
//        parkingSpotModel.setLicensePlateCar(dto.getLicensePlateCar());
//        parkingSpotModel.setModelCar(dto.getModelCar());
//        parkingSpotModel.setBrandCar(dto.getBrandCar());
//        parkingSpotModel.setColorCar(dto.getColorCar());
//        parkingSpotModel.setResponsibleName(dto.getResponsibleName());
//        parkingSpotModel.setApartment(dto.getApartment());
//        parkingSpotModel.setBlock(dto.getBlock());
//        return ResponseEntity.status(HttpStatus.OK).body(service.save(parkingSpotModel));

        var parkingSpotModel = new ParkingSpot();
        BeanUtils.copyProperties(dto, parkingSpotModel);
        parkingSpotModel.setId(parkingSpotOptional.get().getId());
        parkingSpotModel.setRegistrationDate(parkingSpotOptional.get().getRegistrationDate());
        return ResponseEntity.status(HttpStatus.OK).body(service.save(parkingSpotModel));
    }

}

package pl.edu.dik.rest.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.dik.domain.model.rent.Rent;
import pl.edu.dik.ports.exception.business.RentNotFoundException;
import pl.edu.dik.ports._interface.InactiveRentService;
import pl.edu.dik.rest.model.rent.RentResponse;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inactive-rents")
@RequiredArgsConstructor
public class InactiveRentController {

    private final InactiveRentService inactiveRentService;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<RentResponse>> getAllInactiveRents() {
        List<Rent> rents = inactiveRentService.getAllInactiveRents();
        return ResponseEntity.ok(rents.stream().map(rent -> modelMapper.map(rent, RentResponse.class)).collect(Collectors.toList()));
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<List<RentResponse>> getInactiveRentsByClientId(@PathVariable UUID id) {
        List<Rent> rents = inactiveRentService.getInactiveRentsByClientId(id);
        return ResponseEntity.ok(rents.stream().map(rent -> modelMapper.map(rent, RentResponse.class)).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentResponse> getInactiveRentById(@PathVariable UUID id) throws RentNotFoundException {
        Rent rent = inactiveRentService.findInactiveRentById(id);
        return ResponseEntity.ok(modelMapper.map(rent, RentResponse.class));
    }
}

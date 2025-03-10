package pl.edu.dik.tks.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.edu.dik.tks.dto.rent.CreateRentRequest;
import pl.edu.dik.tks.dto.rent.RentResponse;
import pl.edu.dik.tks.exception.business.*;
import pl.edu.dik.tks.model.rent.Rent;
import pl.edu.dik.tks.service.RentService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rents")
@RequiredArgsConstructor
public class RentController {

    private final RentService rentService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<RentResponse> createRent(@Valid @RequestBody CreateRentRequest dto) throws
            StartDateBeforeEndDateException,
            GameNotAvailableForRentException,
            ClientNotAvailableForRentException,
            AccountNotActiveException {
        Rent rent = rentService.createRent(dto.getStartDate(), dto.getEndDate(), SecurityContextHolder.getContext().getAuthentication().getName(), dto.getGameId());
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(rent, RentResponse.class));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentResponse> getRentById(@PathVariable UUID id) throws RentNotFoundException {
        Rent rent = rentService.findRentById(id);
        return ResponseEntity.ok(modelMapper.map(rent, RentResponse.class));
    }

    @GetMapping
    public ResponseEntity<List<RentResponse>> getAllRents() {
        List<Rent> rents = rentService.getAllRents();
        return ResponseEntity.ok(rents.stream().map(rent -> modelMapper.map(rent, RentResponse.class)).collect(Collectors.toList()));
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<List<RentResponse>> getActiveRentsByClientId(@PathVariable UUID id) {
        List<Rent> rents = rentService.getActiveRentsByClientId(id);
        return ResponseEntity.ok(rents.stream().map(rent -> modelMapper.map(rent, RentResponse.class)).collect(Collectors.toList()));
    }

    @PostMapping("/end/{id}")
    public ResponseEntity<Void> endRent(@PathVariable UUID id) throws GameNotAvailableForRentException, ClientNotAvailableForRentException, RentNotFoundException {
        rentService.endRent(id);
        return ResponseEntity.ok().build();
    }
}

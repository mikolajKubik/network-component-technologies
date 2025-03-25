package pl.edu.dik.rest.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.edu.dik.domain.model.rent.Rent;
import pl.edu.dik.ports._interface.InactiveRentService;
import pl.edu.dik.ports.exception.business.RentNotFoundException;
import pl.edu.dik.rest.exception.AppExceptionHandler;
import pl.edu.dik.rest.model.rent.RentResponse;


import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class InactiveRentControllerMockTest {
    @Mock
    private InactiveRentService inactiveRentService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private InactiveRentController inactiveRentController;

    private MockMvc mockMvc;

    private UUID rentId;
    private UUID clientId;
    private Rent rent;
    private RentResponse rentResponse;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.standaloneSetup(inactiveRentController).setControllerAdvice(new AppExceptionHandler()).build();
        rentId = UUID.randomUUID();
        clientId = UUID.randomUUID();

        rent = new Rent();
        rent.setId(rentId);

        rentResponse = new RentResponse();
        rentResponse.setId(rentId.toString());
    }

    @SneakyThrows
    @Test
    void returnAllInactiveRents() {

        when(inactiveRentService.getAllInactiveRents()).thenReturn(List.of(rent));
        when(modelMapper.map(any(Rent.class), eq(RentResponse.class))).thenReturn(rentResponse);

        mockMvc.perform(get("/api/inactive-rents")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(rentId.toString()));
     }

    @SneakyThrows
    @Test
    void returnInactiveRentsByClientId() {

        when(inactiveRentService.getInactiveRentsByClientId(clientId)).thenReturn(List.of(rent));
        when(modelMapper.map(any(Rent.class), eq(RentResponse.class))).thenReturn(rentResponse);

        mockMvc.perform(get("/api/inactive-rents/client/{id}", clientId)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(rentId.toString()));
    }

    @SneakyThrows
    @Test
    void returnInactiveRentById() {

        when(inactiveRentService.findInactiveRentById(rentId)).thenReturn(rent);
        when(modelMapper.map(any(Rent.class), eq(RentResponse.class))).thenReturn(rentResponse);

        mockMvc.perform(get("/api/inactive-rents/{id}", rentId)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(rentId.toString()));
    }

    @SneakyThrows
    @Test
    void returnInactiveRentByIdNotFound() {
        when(inactiveRentService.findInactiveRentById(rentId)).thenThrow(new RentNotFoundException("Rent not found"));

        mockMvc.perform(get("/api/inactive-rents/{id}", rentId)
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

}

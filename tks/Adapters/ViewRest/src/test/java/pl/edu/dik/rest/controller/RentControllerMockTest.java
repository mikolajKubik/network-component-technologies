package pl.edu.dik.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.edu.dik.domain.model.account.Account;
import pl.edu.dik.domain.model.game.Game;
import pl.edu.dik.domain.model.rent.Rent;
import pl.edu.dik.ports._interface.RentService;
import pl.edu.dik.ports.exception.business.RentNotFoundException;
import pl.edu.dik.rest.exception.AppExceptionHandler;
import pl.edu.dik.rest.model.rent.CreateRentRequest;
import pl.edu.dik.rest.model.rent.RentResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@ExtendWith(SpringExtension.class)
class RentControllerMockTest {
    // no ale mam robić reszte kontrolerów tak samo czy chcenie to
    @InjectMocks
    private RentController rentController;

    @Mock
    private RentService rentService;

    @Mock
    private ModelMapper modelMapper;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private UUID rentId;
    private Rent rent;
    private UUID gameId;
    private UUID accountId;
    private Game game;
    private Account account;
    private RentResponse rentResponse;
    private CreateRentRequest createRentRequest;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());

        new UsernamePasswordAuthenticationToken("test_user", null, List.of(new SimpleGrantedAuthority("ROLE_CLIENT")));

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        when(authentication.getName()).thenReturn("test_user");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        mockMvc = MockMvcBuilders
                .standaloneSetup(rentController)
                .setControllerAdvice(new AppExceptionHandler())
                .build();
        gameId = UUID.randomUUID();
        accountId = UUID.randomUUID();

        game = new Game();
        game.setId(gameId);
        game.setName("Chess");
        game.setPricePerDay(20);

        account = new Account();
        account.setId(accountId);
        account.setLogin("test_user");

        rentId = UUID.randomUUID();
        rent = new Rent(rentId, LocalDate.now(), LocalDate.now().plusDays(1), account, game, 100);
        rentResponse = new RentResponse();
        rentResponse.setId(rentId.toString());
        rentResponse.setStartDate(LocalDate.now());
        rentResponse.setEndDate(LocalDate.now().plusDays(1));
        rentResponse.setGameId(gameId);
        rentResponse.setAccountId(accountId);
        rentResponse.setRentalPrice(20);

        createRentRequest = new CreateRentRequest();
        createRentRequest.setStartDate(LocalDate.now());
        createRentRequest.setEndDate(LocalDate.now().plusDays(1));
        createRentRequest.setGameId(gameId);
    }

    @SneakyThrows
    @Test
    void createRent() {
        when(modelMapper.map(any(CreateRentRequest.class), any())).thenReturn(rent);
        when(rentService.createRent(any(), any(), any(), any())).thenReturn(rent);
        when(modelMapper.map(any(Rent.class), any())).thenReturn(rentResponse);

        mockMvc.perform(post("/api/rents")
                        .with(user("test_tser"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRentRequest)))
                .andExpect(status().isCreated());
    }

    @SneakyThrows
    @Test
    void getRentById() {
        when(rentService.findRentById(rentId)).thenReturn(rent);
        when(modelMapper.map(any(Rent.class), any())).thenReturn(rentResponse);

        mockMvc.perform(get("/api/rents/{id}", rentId))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void returnNotFoundForNonExistingRent() {
        when(rentService.findRentById(rentId)).thenThrow(new RentNotFoundException("Rent not found"));

        mockMvc.perform(get("/api/rents/{id}", rentId))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void getAllRents() {
        when(rentService.getAllRents()).thenReturn(List.of(rent));
        when(modelMapper.map(any(Rent.class), any())).thenReturn(rentResponse);

        mockMvc.perform(get("/api/rents"))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void endRent() {
        mockMvc.perform(post("/api/rents/end/{id}", rentId))
                .andExpect(status().isOk());
    }
}

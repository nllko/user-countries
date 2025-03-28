package lv.visma.consulting.usercountriesapi.services;

import lv.visma.consulting.usercountriesapi.controllers.dto.CountryDto;
import lv.visma.consulting.usercountriesapi.controllers.dto.UserDto;
import lv.visma.consulting.usercountriesapi.db.entities.CountryEntity;
import lv.visma.consulting.usercountriesapi.db.entities.UserEntity;
import lv.visma.consulting.usercountriesapi.db.repositories.CountryRepository;
import lv.visma.consulting.usercountriesapi.db.repositories.UserRepository;
import lv.visma.consulting.usercountriesapi.web.client.WebclientService;
import lv.visma.consulting.usercountriesapi.web.client.dto.WebClientCountryDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private CountryRepository countryRepository;
    @Mock
    private WebclientService webclientService;

    @Test
    void getAllUsers_WhenUsersExist_ReturnsListOfUserDto() {
        List<UserEntity> users =  mockUsers();
        when(userRepository.findAll()).thenReturn(users);

        List<UserDto> result = userService.getAllUsers();

        assertThat(result).hasSize(2);
        verify(userRepository).findAll();
    }

    @Test
    void getAllUsers_WhenNoUsersExist_ReturnsEmptyList() {
        when(userRepository.findAll()).thenReturn(List.of());

        List<UserDto> result = userService.getAllUsers();

        assertThat(result).isEmpty();
    }

    @Test
    void getFavoriteCountries_WhenApiSucceeds_ReturnsConvertedWebClientDtos() {
        Long userId = 1L;
        Set<CountryEntity> mockCountries = mockCountryEntities();

        List<WebClientCountryDto> mockApiResponse = mockWebClientCountryDtos();

        when(countryRepository.findAllByUserId(userId)).thenReturn(mockCountries);
        when(webclientService.getList("LV,EE")).thenReturn(mockApiResponse);

        List<CountryDto> result = userService.getFavoriteCountriesByUserId(userId);

        assertThat(result).hasSize(2);
        verify(webclientService).getList("LV,EE");
    }

    @Test
    void getFavoriteCountries_WhenApiFails_ReturnsConvertedEntities() {
        Long userId = 1L;

        CountryEntity country1 = new CountryEntity();
        country1.setId(1L);
        country1.setName("Latvija");
        country1.setCode("LV");

        Set<CountryEntity> mockCountries = Set.of(country1);

        when(countryRepository.findAllByUserId(userId)).thenReturn(mockCountries);
        when(webclientService.getList("LV")).thenThrow(new WebClientResponseException(500, "", null, null, null));

        List<CountryDto> result = userService.getFavoriteCountriesByUserId(userId);

        assertThat(result).hasSize(1)
                .extracting(CountryDto::getCode)
                .containsExactly("LV");
    }

    @Test
    void getFavoriteCountries_WhenNoCountriesExist_ReturnsEmptyList() {
        Long userId = 1L;
        when(countryRepository.findAllByUserId(userId)).thenReturn(Set.of());
        when(webclientService.getList("")).thenReturn(List.of());

        List<CountryDto> result = userService.getFavoriteCountriesByUserId(userId);

        assertThat(result).isEmpty();
    }

    private List<UserEntity> mockUsers() {
        List<UserEntity> users = new ArrayList<>();

        UserEntity user1 = new UserEntity();
        user1.setId(1L);
        user1.setUsername("user1");
        users.add(user1);

        UserEntity user2 = new UserEntity();
        user2.setId(2L);
        user2.setUsername("user2");
        users.add(user2);

        return users;
    }

    private Set<CountryEntity> mockCountryEntities() {
        Set<CountryEntity> countries = new HashSet<>();

        CountryEntity country1 = new CountryEntity();
        country1.setId(1L);
        country1.setName("Latvija");
        country1.setCode("LV");
        countries.add(country1);

        CountryEntity country2 = new CountryEntity();
        country2.setId(2L);
        country2.setName("Estonia");
        country2.setCode("EE");
        countries.add(country2);

        return countries;
    }

    private List<WebClientCountryDto> mockWebClientCountryDtos() {
        List<WebClientCountryDto> countries = new ArrayList<>();

        WebClientCountryDto country1 = new WebClientCountryDto();
        WebClientCountryDto.Name name1 = new WebClientCountryDto.Name();
        name1.setOfficial("Latvija");
        country1.setCode("LV");
        country1.setName(name1);
        countries.add(country1);

        WebClientCountryDto country2 = new WebClientCountryDto();
        WebClientCountryDto.Name name2 = new WebClientCountryDto.Name();
        name1.setOfficial("Estonia");
        country2.setCode("EE");
        country2.setName(name2);
        countries.add(country2);

        return countries;
    }
}
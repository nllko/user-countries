package lv.visma.consulting.usercountriesapi.services;

import lombok.AllArgsConstructor;
import lv.visma.consulting.usercountriesapi.controllers.dto.CountryDto;
import lv.visma.consulting.usercountriesapi.controllers.dto.UserDto;
import lv.visma.consulting.usercountriesapi.converters.CountryConverter;
import lv.visma.consulting.usercountriesapi.converters.UserConverter;
import lv.visma.consulting.usercountriesapi.db.entities.CountryEntity;
import lv.visma.consulting.usercountriesapi.db.repositories.CountryRepository;
import lv.visma.consulting.usercountriesapi.db.repositories.UserRepository;
import lv.visma.consulting.usercountriesapi.web.client.WebclientService;
import lv.visma.consulting.usercountriesapi.web.client.dto.WebClientCountryDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService {

    static Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final CountryRepository countryRepository;
    private final WebclientService webclientService;

    public List<UserDto> getAllUsers() {
        var entityUsers = userRepository.findAll();
        return UserConverter.toDTOList(entityUsers);
    }

    public List<CountryDto> getFavoriteCountriesByUserId(Long id) {
        var entityCountries = countryRepository.findAllByUserId(id);
        try {
            String codes = getCountryCodes(entityCountries);
            List<WebClientCountryDto> countries = webclientService.getList(codes);
            return CountryConverter.webClientDtoToDTOList(countries);
        } catch (WebClientResponseException e) {
            logger.error("Error while fetching country data from the external API . Reason: {} ", e.getMessage());
            return CountryConverter.entityToDTOList(entityCountries);
        }
    }

    private String getCountryCodes(Set<CountryEntity> countries) {
        return countries.stream().map(CountryEntity::getCode).collect(Collectors.joining(","));
    }
}

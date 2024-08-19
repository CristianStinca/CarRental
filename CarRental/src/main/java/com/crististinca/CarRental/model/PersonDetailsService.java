package com.crististinca.CarRental.model;

import com.crististinca.CarRental.Utils.WClient;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {

    public PersonDetailsService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl(WClient.url).build();
    }

    private final RestClient restClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<Person> user = personRepository.findByUsername(username);

        Optional<Person> user = Optional.empty();
        try {
            user = Optional.ofNullable(this.restClient.get()
                    .uri("/users/by/username?username={username}", username)
                    .retrieve()
                    .body(Person.class));
        } catch (HttpClientErrorException.NotFound e) {
            //TODO: User not found.
            throw new UsernameNotFoundException(username);
        } catch (HttpClientErrorException e) {
            //TODO: Unknown user.
            throw new UsernameNotFoundException(username);
        }

        if (user.isPresent()) {
            var userObj = user.get();
            return User.builder()
                    .username(userObj.getUsername())
                    .password(userObj.getPassword())
                    .roles(analyzeRoles(userObj))
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    private String[] analyzeRoles(Person person) {
        if (person.getRole() == null) {
            return new String[] { "USER" };
        }
        return person.getRole().split(",");
    }
}

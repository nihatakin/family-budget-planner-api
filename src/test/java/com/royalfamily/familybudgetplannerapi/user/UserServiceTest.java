package com.royalfamily.familybudgetplannerapi.user;

import com.github.javafaker.Faker;
import com.royalfamily.familybudgetplannerapi.domain.User;
import com.royalfamily.familybudgetplannerapi.exceptions.AuthException;
import com.royalfamily.familybudgetplannerapi.repositories.UserRepository;
import com.royalfamily.familybudgetplannerapi.services.UserServiceImpl;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl sampleImpl;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    private final Faker faker = new Faker();

    @Test
    void canValidateUser() {
        String name = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        var user = new User(1,name,lastName,email,password);
        given(userRepository.findByEmailAndPassword(email,password))
                .willReturn(user);

        // when
       var result = sampleImpl.validateUser(email,password);
        // then
        assertThat(result.getEmail()).isEqualTo(email);
    }

    @Test
    void canRegisterUser() {
        String name = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        var user = new User(1,name,lastName,email,password);
        given(userRepository.getCountByEmail(email))
                .willReturn(0);
        given(userRepository.create(name,lastName,email,password))
                .willReturn(user.getUserId());

        given(userRepository.findById(user.getUserId()))
                .willReturn(user);

        // when
        var result = sampleImpl.registerUser(name,lastName,email,password);
        // then
        assertThat(result.getFirstName()).isEqualTo(name);
        assertThat(result.getLastName()).isEqualTo(lastName);
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getPassword()).isEqualTo(password);
        assertThat(result).isEqualTo(user);
    }

    @Test
    void willThrowWhenEmailFormatWrong() {
        String name = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = "wrongemailaddress";
        String password = faker.internet().password();

        // when
        assertThatThrownBy(() -> sampleImpl.registerUser(name,lastName,email,password))
                .isInstanceOf(AuthException.class)
                .hasMessageContaining("Invalid email format");
        // then
        verify(userRepository, never()).getCountByEmail(any());
        verify(userRepository, never()).create(any(),any(),any(),any());
        verify(userRepository, never()).findById(any());
    }

    @Test
    void willThrowWhenEmailIsTaken() {
        String name = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();

        given(userRepository.getCountByEmail(email))
                .willReturn(1);

        // when
        assertThatThrownBy(() -> sampleImpl.registerUser(name,lastName,email,password))
                .isInstanceOf(AuthException.class)
                .hasMessageContaining("Email already in use");
        // then
        verify(userRepository, never()).create(any(),any(),any(),any());
        verify(userRepository, never()).findById(any());
    }
}
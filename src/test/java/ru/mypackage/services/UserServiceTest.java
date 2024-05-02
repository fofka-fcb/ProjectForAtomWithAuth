package ru.mypackage.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import ru.mypackage.dto.user.UserDTO;
import ru.mypackage.models.ApplicationUser;
import ru.mypackage.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserService userService;

    private ApplicationUser appUser_1;
    private ApplicationUser appUser_2;
    private ApplicationUser appUser_3;

    private UserDTO user_1;
    private UserDTO user_2;
    private UserDTO user_3;

    private List<ApplicationUser> users;

    @BeforeEach
    void init() {
        appUser_1 = new ApplicationUser();
        appUser_2 = new ApplicationUser();
        appUser_3 = new ApplicationUser();

        user_1 = new UserDTO();
        user_2 = new UserDTO();
        user_3 = new UserDTO();

        users = new ArrayList<>(List.of(appUser_1, appUser_2, appUser_3));
    }

    @Test
    void shouldHaveCorrectFindAll() {
        when(userRepository.findAll()).thenReturn(users);
        when(modelMapper.map(users.get(0), UserDTO.class)).thenReturn(user_1);
        when(modelMapper.map(users.get(1), UserDTO.class)).thenReturn(user_2);
        when(modelMapper.map(users.get(2), UserDTO.class)).thenReturn(user_3);

        List<UserDTO> result = userService.findAll();

        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    void shouldHaveCorrectFindOneById() {
        int idOfUser_1 = 1;

        when(userRepository.findById(idOfUser_1)).thenReturn(Optional.of(appUser_1));
        when(modelMapper.map(appUser_1, UserDTO.class)).thenReturn(user_1);

        UserDTO userFromUserService = userService.findOneById(idOfUser_1);

        assertThat(userFromUserService).isEqualTo(user_1);
    }

}

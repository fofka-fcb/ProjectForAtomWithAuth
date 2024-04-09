package ru.mypackage.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mypackage.dto.user.UserDTO;
import ru.mypackage.models.ApplicationUser;
import ru.mypackage.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository,
                       ModelMapper modelMapper
    ) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    public List<UserDTO> findAll() {
        List<UserDTO> userDTOList = new ArrayList<>();

        userRepository.findAll().stream().forEach(u -> {
            userDTOList.add(convertToUserDTO(u));
        });

        return userDTOList;
    }

    @Transactional(readOnly = true)
    public UserDTO findOneById(Integer idOfUser) {
        return convertToUserDTO(userRepository.findById(idOfUser)
                .orElseThrow()
        );
    }


    private UserDTO convertToUserDTO(ApplicationUser applicationUser) {
        return modelMapper.map(applicationUser, UserDTO.class);
    }


}

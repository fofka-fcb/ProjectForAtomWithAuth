package ru.mypackage.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mypackage.dto.LoginResponseDTO;
import ru.mypackage.dto.RegisterResponseDTO;
import ru.mypackage.models.ApplicationUser;
import ru.mypackage.models.Role;
import ru.mypackage.models.Token;
import ru.mypackage.models.enums.TokenType;
import ru.mypackage.repository.RoleRepository;
import ru.mypackage.repository.TokenRepository;
import ru.mypackage.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Autowired
    public AuthenticationService(UserRepository userRepository, RoleRepository roleRepository,
                                 TokenRepository tokenRepository, PasswordEncoder passwordEncoder,
                                 ModelMapper modelMapper, AuthenticationManager authenticationManager,
                                 TokenService tokenService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @Transactional
    public RegisterResponseDTO registerUser(String username, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        Role userRole = roleRepository.findByAuthority("USER").get();

        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);

        ApplicationUser applicationUser = new ApplicationUser(username, encodedPassword, authorities);

        userRepository.save(applicationUser);

        return convertToRegisterResponseDTO(applicationUser);
    }

    @Transactional
    public LoginResponseDTO loginUser(String username, String password) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        ApplicationUser user = (ApplicationUser) auth.getPrincipal();
        String access_token = tokenService.generateAccessToken(user);

        revokeAllUserTokens(user);
        saveUserToken(user, access_token);

        return new LoginResponseDTO(user.getUsername(), (Set<Role>) user.getAuthorities(), access_token);
    }

    private void saveUserToken(ApplicationUser user, String jwtToken) {
        Token token = new Token();
        token.setUsername(user.getUsername());
        token.setTokenType(TokenType.BEARER);
        token.setToken(jwtToken);
        token.setExpired(false);

        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(ApplicationUser user) {
        List<Token> tokenList = tokenRepository.findAllValidToken(user.getUsername(), false);

        if (tokenList.isEmpty())
            return;

        for (Token token : tokenList) {
            token.setExpired(true);
        }

        tokenRepository.saveAllAndFlush(tokenList);
    }

    private RegisterResponseDTO convertToRegisterResponseDTO(ApplicationUser user) {
        return modelMapper.map(user, RegisterResponseDTO.class);
    }

    private ApplicationUser convertToApplicationUser(RegisterResponseDTO reg) {
        return modelMapper.map(reg, ApplicationUser.class);
    }

}

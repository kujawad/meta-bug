package com.metabug.service;

import com.metabug.persistence.dao.UserRepository;
import com.metabug.persistence.model.Role;
import com.metabug.persistence.model.User;
import com.metabug.web.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(final UserRepository userRepository,
                       final BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User findUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    public User findUserByLogin(final String login) {
        return userRepository.findByLogin(login);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void save(final UserDto userDto) {
        final User user = new User();
        user.setLogin(userDto.getLogin());
        user.setEmail(userDto.getEmail());
        user.setRole(Role.USER);
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
    }

    public boolean isMatchingPassword(final String givenPassword, final String encodedPassword) {
        return bCryptPasswordEncoder.matches(givenPassword, encodedPassword);
    }
}

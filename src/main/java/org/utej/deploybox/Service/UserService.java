package org.utej.deploybox.Service;

import org.springframework.stereotype.Service;
import org.utej.deploybox.Model.User;
import org.utej.deploybox.Repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public User registerUser(String username, String email, String password){
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        return userRepository.save(user);
    }
    public Optional<User> findbyUsername(String username){
        return userRepository.findByUsername(username);
    }
}

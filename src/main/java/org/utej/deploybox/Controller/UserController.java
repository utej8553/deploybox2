package org.utej.deploybox.Controller;
import org.springframework.web.bind.annotation.*;
import org.utej.deploybox.Model.User;
import org.utej.deploybox.Service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/signup")
    public User signup(@RequestParam String username,@RequestParam String email,@RequestParam String password){
        return userService.registerUser(username,email,password);
    }
}

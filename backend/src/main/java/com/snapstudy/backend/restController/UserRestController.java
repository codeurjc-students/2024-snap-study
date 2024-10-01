package com.snapstudy.backend.restController;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snapstudy.backend.model.User;
import com.snapstudy.backend.security.jwt.AuthResponse;
import com.snapstudy.backend.security.jwt.AuthResponse.Status;
import com.snapstudy.backend.service.UserService;
import com.snapstudy.backend.security.jwt.LoginRequest;
import com.snapstudy.backend.security.jwt.UserLoginService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

    @Autowired
    UserService userService;
    
    @Operation(summary = "Register a new student")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "User created", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@PostMapping("/")
	public ResponseEntity<User> register(@RequestBody User post) throws IOException, SQLException {
		User user = new User(post.getFirstName(), post.getLastName(), post.getEmail(), post.getPassword());
		userService.setUser(user);

		URI location = fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
		return ResponseEntity.created(location).body(user);
	}
}

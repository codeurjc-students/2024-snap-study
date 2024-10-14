package com.snapstudy.backend.restController;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.sql.SQLException;
import java.util.Optional;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snapstudy.backend.model.Student;
import com.snapstudy.backend.model.User;
import com.snapstudy.backend.model.DTO.UserDTO;
import com.snapstudy.backend.service.AdminService;
import com.snapstudy.backend.service.StudentService;
import com.snapstudy.backend.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    UserService userService;

	@Autowired
	private AdminService adminService;

	@Autowired
	private StudentService studentService;

    
    @Operation(summary = "Register a new student")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "User created", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@PostMapping("/")
	public ResponseEntity<User> register(@RequestBody UserDTO post) throws IOException, SQLException {
		User user = new Student(post.getFirstName(), post.getLastName(), post.getEmail(), post.getPassword());
		userService.setUser(user);

		URI location = fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
		return ResponseEntity.created(location).body(user);
	}


	@Operation(summary = "Get user") // registered
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the user", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
			@ApiResponse(responseCode = "404", description = "User not found ", content = @Content),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content) })
	@GetMapping("/me")
	public ResponseEntity<?> profile(HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		if (principal == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		Optional<User> currentUser = userService.getByEmail(principal.getName());
		if (currentUser.isPresent()) {
			return new ResponseEntity<>(this.getPrincipalUser(currentUser.get(), request), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}


	private User getPrincipalUser(User user, HttpServletRequest request) {
		System.out.println("---------------------------------");
		System.out.println(user.getRoles());
		System.out.println("---------------------------------");
		if (user.getRoles().contains("STUDENT")) {
			user = studentService.getStudentByEmail(request.getUserPrincipal().getName());
		} else if (user.getRoles().contains("ADMIN")) {
			user = adminService.getAdminByEmail(request.getUserPrincipal().getName());
		}

		return user;
	}
}

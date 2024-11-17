package com.snapstudy.backend.restController;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.sql.SQLException;
import java.util.Optional;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
        Optional<User> checkUser = userService.getByEmail(post.getEmail());
        if (checkUser.isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
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
        if (user.getRoles().contains("STUDENT")) {
            user = studentService.getStudentByEmail(request.getUserPrincipal().getName());
        } else if (user.getRoles().contains("ADMIN")) {
            user = adminService.getAdminByEmail(request.getUserPrincipal().getName());
        }

        return user;
    }

    @Operation(summary = "Update the user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Profile not found ", content = @Content),
            @ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
    @PutMapping("/")
    public ResponseEntity<User> editProfile(HttpServletRequest request, @RequestBody UserDTO post)
            throws IOException, SQLException {

        Optional<User> currentUser = userService.getByEmail(request.getUserPrincipal().getName());

        if (currentUser.isPresent()) {
            User user = this.getPrincipalUser(currentUser.get(), request);

            if (user != null) {
                user.setFirstName(post.getFirstName());
                user.setLastName(post.getLastName());
                user.setEmail(post.getEmail());
                user.setPassword(post.getPassword());

                userService.setUser(user);

                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Set the image profile of the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the image profile", content = {
                    @Content(mediaType = "image/jpg") }),
            @ApiResponse(responseCode = "404", description = "image profile not found", content = @Content),
            @ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
    @PutMapping("/image")
    public ResponseEntity<Object> updateFile(HttpServletRequest request, @RequestBody MultipartFile file)
            throws IOException {
        byte[] foto;
        Optional<User> currentUser = userService.getByEmail(request.getUserPrincipal().getName());

        if (currentUser.isPresent()) {
            foto = file.getBytes();
            User user = this.getPrincipalUser(currentUser.get(), request);

            user.setProfilePicture(foto);
            userService.setUser(user);

            URI location = fromCurrentRequest().build().toUri();

            return ResponseEntity.created(location).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get the image profile of the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the image profile", content = {
                    @Content(mediaType = "image/jpg") }),
            @ApiResponse(responseCode = "404", description = "image profile not found", content = @Content),
            @ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
    @GetMapping("/image")
    public ResponseEntity<byte[]> downloadFile(HttpServletRequest request) {

        Optional<User> currentUser = userService.getByEmail(request.getUserPrincipal().getName());

        if (currentUser.isPresent()) {
            byte[] imageData = currentUser.get().getProfilePicture();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "image/png")
                    .contentLength(imageData.length)
                    .body(imageData);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

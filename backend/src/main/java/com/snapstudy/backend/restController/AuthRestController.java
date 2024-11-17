package com.snapstudy.backend.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snapstudy.backend.security.jwt.AuthResponse;
import com.snapstudy.backend.security.jwt.AuthResponse.Status;
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
@RequestMapping("/api/auth")
public class AuthRestController {

	@Autowired
	private UserLoginService userLoginService;

	@Operation(summary = "Log in")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Login successful", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class)) }) })
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(
			@CookieValue(name = "accessToken", required = false) String accessToken,
			@CookieValue(name = "refreshToken", required = false) String refreshToken,
			@RequestBody LoginRequest loginRequest) {

		return userLoginService.login(loginRequest, accessToken, refreshToken);
	}

	@PostMapping("/refresh")
	public ResponseEntity<AuthResponse> refreshToken(
			@CookieValue(name = "refreshToken", required = false) String refreshToken) {

		return userLoginService.refresh(refreshToken);
	}

	@Operation(summary = "Log out ")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Logout successful", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class)) }) })
	@PostMapping("/logout")
	public ResponseEntity<AuthResponse> logOut(HttpServletRequest request, HttpServletResponse response) {

		return ResponseEntity.ok(new AuthResponse(Status.SUCCESS, userLoginService.logout(request, response)));
	}
}

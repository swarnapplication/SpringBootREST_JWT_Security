package com.swarn.securitycontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swarn.jwt.JwtTokenUtil;
import com.swarn.user.User;
import com.swarn.user.api.AuthRequest;
import com.swarn.user.api.AuthResponse;



@RestController
@RequestMapping("/security")
public class SecurityController {
	
	@Autowired
	AuthenticationManager authManager;
	
	@Autowired
	JwtTokenUtil jwtUtil;

	
	@PostMapping(value = "/auth/token", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> generateSecurityToken(
			@RequestBody AuthRequest authRequest)
	{
		System.out.println("INSIDE Auth-token");
		System.out.println("Data: "+authRequest.getEmail()+" / "+authRequest.getPassword());
		
		
		try {
			Authentication authentication = authManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							authRequest.getEmail(), authRequest.getPassword())
			);
			//System.out.println("Data2: "+authRequest.getEmail()+" / "+authRequest.getPassword());
			User user = (User) authentication.getPrincipal();
			//System.out.println("Data2.1: "+user.getEmail()+" / "+user.getPassword());
			String accessToken = jwtUtil.generateAccessToken(user);
			//System.out.println("Data3: "+request.getEmail()+" / "+request.getPassword());
			AuthResponse response = new AuthResponse(user.getEmail(), accessToken);
			
			return ResponseEntity.ok().body(response);
			
		} catch (BadCredentialsException ex) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
		
	}
}

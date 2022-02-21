package com.zebrands.authorization.controller;

import com.zebrands.authorization.annotations.Authorization;
import com.zebrands.authorization.constants.Constants;
import com.zebrands.authorization.dto.ProfileDto;
import com.zebrands.authorization.service.ProfileService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/profile")
@Log4j2
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/type/{user}")
    public ResponseEntity<String> findProfileByUser(@PathVariable(value = "user") String user) {
        log.info("find profile by user: {}", user);
        return ResponseEntity.ok(profileService.findProfileByUser(user));
    }

    @GetMapping("/emails/{user}")
    public ResponseEntity<List<String>> findEmailsWithOutUser(@PathVariable(value = "user") String user) {
        log.info("find emails without user: {}", user);
        return ResponseEntity.ok(profileService.findEmailsWithOutUser(user));
    }

    @PostMapping()
    @Authorization(userType = {Constants.ADMIN})
    public ResponseEntity<ProfileDto> saveProfile(@RequestBody ProfileDto profileDto, HttpServletRequest httpServletRequest) {
        var user = httpServletRequest.getHeader(Constants.AUTH_USER);
        log.info("save profile user: {} profile: {}", user, profileDto.toString());
        return ResponseEntity.ok(profileService.save(profileDto));
    }

    @PutMapping("/{id}")
    @Authorization(userType = {Constants.ADMIN})
    public ResponseEntity<ProfileDto> updateProfile(@PathVariable(value = "id") int id, @RequestBody ProfileDto profileDto, HttpServletRequest httpServletRequest) {
        var user = httpServletRequest.getHeader(Constants.AUTH_USER);
        log.info("update profile user: {} profile: {}", user, profileDto.toString());
        return ResponseEntity.ok(profileService.update(id, profileDto));
    }

    @DeleteMapping("/{id}")
    @Authorization(userType = {Constants.ADMIN})
    public ResponseEntity<Boolean> deleteProfile(@PathVariable(value = "id") int id, HttpServletRequest httpServletRequest) {
        var user = httpServletRequest.getHeader(Constants.AUTH_USER);
        log.info("delete profile user: {} id: {}", user, id);
        return ResponseEntity.ok(profileService.delete(id));
    }
}

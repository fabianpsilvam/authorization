package com.zebrands.authorization.service.impl;

import com.zebrands.authorization.constants.Constants;
import com.zebrands.authorization.dto.ProfileDto;
import com.zebrands.authorization.entity.ProfileEntity;
import com.zebrands.authorization.repository.ProfileRepository;
import com.zebrands.authorization.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    public String findProfileByUser(String user) {
        var profile = profileRepository.findByUserId(user);
        var type = "";
        if (profile != null) {
            type = profile.getType();
        }
        return type;
    }

    public List<String> findEmailsWithOutUser(String user) {
        var profiles = profileRepository.findAdminsWithoutUser(user, Constants.ADMIN);
        return profiles.stream().map(ProfileEntity::getEmail).collect(Collectors.toList());
    }

    public ProfileDto save(ProfileDto profileDto) {
        var newProfile = profileRepository.save(ProfileEntity.builder()
                .type(profileDto.getType())
                .userId(profileDto.getUser())
                .email(profileDto.getEmail())
                .build());
        profileDto.setId(newProfile.getId());
        return profileDto;
    }

    public ProfileDto update(int id, ProfileDto profileDto) {
        var profileFound = findById(id);
        profileFound.setEmail(profileDto.getEmail());
        profileFound.setType(profileDto.getType());

        profileRepository.save(profileFound);

        return new ProfileDto(profileFound.getId(), profileFound.getUserId(), profileFound.getType(), profileFound.getEmail());
    }

    public Boolean delete(int sku) {
        var profileFound = findById(sku);
        profileRepository.delete(profileFound);
        return Boolean.TRUE;
    }

    private ProfileEntity findById(int id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new InvalidParameterException("Profile not found on: " + id));
    }
}

package com.zebrands.authorization.service;

import com.zebrands.authorization.dto.ProfileDto;

import java.util.List;

public interface ProfileService {

    String findProfileByUser(String user);

    List<String> findEmailsWithOutUser(String user);

    ProfileDto save(ProfileDto profileDto);

    ProfileDto update(int id, ProfileDto profileDto);

    Boolean delete(int sku);
}

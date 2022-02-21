package com.zebrands.authorization.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zebrands.authorization.CommonTests;
import com.zebrands.authorization.constants.Constants;
import com.zebrands.authorization.dto.ProfileDto;
import com.zebrands.authorization.entity.ProfileEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class ProfileControllerTest extends CommonTests {

    @Test
    public void findDefaultProfile_Type_Ok() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/profile/type/zebrands");

        var response = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assertions.assertEquals(response, Constants.ADMIN);
    }

    @Test
    public void findProfile_Not_Found() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/profile/type/no-user");

        var response = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        Assertions.assertEquals(response, "");
    }

    @Test
    @Sql(scripts = {"/db/query/add_admin_profile.sql"})
    public void findEmailsWithOutUser_Type_Ok() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/profile/emails/zebrands");

        var response = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        var emails = objectMapper.readValue(response, new TypeReference<List<String>>() {
        });

        Assertions.assertEquals(emails.size(), 2);
        Assertions.assertEquals(emails.get(0), "test-user@mail.com");
        Assertions.assertEquals(emails.get(1), "test-user-2@mail.com");
    }

    @Test
    public void findEmailsWithOutUser_Not_Found() throws Exception {

        var request = MockMvcRequestBuilders
                .get("/profile/email/no-user");

        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void saveProfile_Admin() throws Exception {

        var profile = ProfileDto.builder().email("test-user@mail.com").type(Constants.ADMIN).user("test-user").build();
        var request = MockMvcRequestBuilders
                .post("/profile")
                .content(objectMapper.writeValueAsString(profile))
                .contentType(MediaType.APPLICATION_JSON)
                .header("AUTH_USER", "zebrands");

        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        var profiles = profileRepository.findAll();

        Assertions.assertEquals(profiles.size(), 2);
        Assertions.assertEquals(profiles.get(1).getEmail(), profile.getEmail());
        Assertions.assertEquals(profiles.get(1).getType(), profile.getType());
        Assertions.assertEquals(profiles.get(1).getUserId(), profile.getUser());
    }

    @Test
    public void saveProfile_Unauthorized() throws Exception {

        var profile = ProfileDto.builder().email("test-user@mail.com").type(Constants.ADMIN).user("test-user").build();
        var request = MockMvcRequestBuilders
                .post("/profile")
                .content(objectMapper.writeValueAsString(profile))
                .contentType(MediaType.APPLICATION_JSON)
                .header("AUTH_USER", "no-user");

        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void updateProfile_Admin() throws Exception {

        var profile = ProfileEntity.builder().email("test-user@mail.com").type(Constants.ADMIN).userId("test-user").build();
        var newProfile = profileRepository.save(profile);

        var profileDto = ProfileDto.builder().email("test-user-2@mail.com").type(Constants.ADMIN).user("test-user").build();
        var request = MockMvcRequestBuilders
                .put("/profile/" + newProfile.getId())
                .content(objectMapper.writeValueAsString(profileDto))
                .contentType(MediaType.APPLICATION_JSON)
                .header("AUTH_USER", "zebrands");

        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        var profileEntities = profileRepository.findAll();

        Assertions.assertEquals(profileEntities.size(), 2);
        Assertions.assertEquals(profileEntities.get(1).getEmail(), profileDto.getEmail());
        Assertions.assertEquals(profileEntities.get(1).getType(), profileDto.getType());
        Assertions.assertEquals(profileEntities.get(1).getUserId(), profileDto.getUser());
    }

    @Test
    public void updateProfile_Unauthorized() throws Exception {

        var profileEntity = ProfileEntity.builder().email("test-user@mail.com").type(Constants.ADMIN).userId("test-user").build();
        var newProfile = profileRepository.save(profileEntity);

        var profileDto = ProfileDto.builder().email("test-user-2@mail.com").type(Constants.ADMIN).user("test-user").build();
        var request = MockMvcRequestBuilders
                .put("/profile/" + newProfile.getId())
                .content(objectMapper.writeValueAsString(profileDto))
                .contentType(MediaType.APPLICATION_JSON)
                .header("AUTH_USER", "no-user");

        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void updateProfile_NotExistProfile() throws Exception {

        var profileDto = ProfileDto.builder().email("test-user-2@mail.com").type(Constants.ADMIN).user("test-user").build();
        var request = MockMvcRequestBuilders
                .put("/profile/22")
                .content(objectMapper.writeValueAsString(profileDto))
                .contentType(MediaType.APPLICATION_JSON)
                .header("AUTH_USER", "zebrands");

        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void deleteProfile_Admin() throws Exception {

        var profileEntity = ProfileEntity.builder().email("test-user@mail.com").type(Constants.ADMIN).userId("test-user").build();
        var newProfile = profileRepository.save(profileEntity);

        var request = MockMvcRequestBuilders
                .delete("/profile/" + newProfile.getId())
                .header("AUTH_USER", "zebrands");

        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        var profiles = profileRepository.findAll();

        Assertions.assertEquals(profiles.size(), 1);
    }

    @Test
    public void deleteProfile_Unauthorized() throws Exception {

        var profileEntity = ProfileEntity.builder().email("test-user@mail.com").type(Constants.ADMIN).userId("test-user").build();
        var newProfile = profileRepository.save(profileEntity);

        var request = MockMvcRequestBuilders
                .delete("/profile/" + newProfile.getId())
                .header("AUTH_USER", "no-user");

        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void deleteProfile_NotExistProfile() throws Exception {

        var request = MockMvcRequestBuilders
                .delete("/profile/123")
                .header("AUTH_USER", "zebrands");

        mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}

package com.zebrands.authorization.repository;

import com.zebrands.authorization.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, Integer> {

    ProfileEntity findByUserId(String userId);

    @Query(value = "select * from profile p where user_id <> ?1 and type = ?2", nativeQuery = true)
    List<ProfileEntity> findAdminsWithoutUser(@Param("user_id") String userId, @Param("type") String type);
}

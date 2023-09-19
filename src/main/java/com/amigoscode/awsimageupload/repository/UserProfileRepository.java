package com.amigoscode.awsimageupload.repository;

import com.amigoscode.awsimageupload.domain.UserProfile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserProfileRepository {

    private final FakeUserProfileDataStore fakeUserProfileDataStore;

    public UserProfileRepository(FakeUserProfileDataStore fakeUserProfileDataStore) {
        this.fakeUserProfileDataStore = fakeUserProfileDataStore;
    }

    public List<UserProfile> getUserProfiles() {
        return this.fakeUserProfileDataStore.getUserProfiles();
    }
}

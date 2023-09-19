package com.amigoscode.awsimageupload.repository;

import com.amigoscode.awsimageupload.domain.UserProfile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class FakeUserProfileDataStore {

    private static List<UserProfile> USER_PROFILES = new ArrayList<>();

    static {
        USER_PROFILES.add(
                new UserProfile(UUID.fromString("46ef3a87-c741-4051-abeb-2804aaecf76e"), "janetjones", null));
        USER_PROFILES.add(
                new UserProfile(UUID.fromString("d9dc716a-a333-41e9-92c9-c8b3c5597af6"), "antoniojunior", null));
    }

    public List<UserProfile> getUserProfiles() {
        return USER_PROFILES;
    }
}

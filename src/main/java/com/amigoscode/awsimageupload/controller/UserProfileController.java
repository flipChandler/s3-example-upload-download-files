package com.amigoscode.awsimageupload.controller;

import com.amigoscode.awsimageupload.domain.UserProfile;
import com.amigoscode.awsimageupload.service.UserProfileService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/user-profiles")
@CrossOrigin("*")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping
    public List<UserProfile> getUserProfiles() {
        return this.userProfileService.getUserProfiles();
    }

    @PutMapping(
            path = "{userProfileId}/image/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void uploadUserProfileImage(@PathVariable UUID userProfileId,
                                       @PathParam("file") MultipartFile file) {
        this.userProfileService.uploadUserProfileImage(userProfileId, file);
    }

    @GetMapping(path = "{userProfileId}/image/download")
    public byte[] downloadUserProfileImage(@PathVariable UUID userProfileId) {
        return this.userProfileService.downloadUserProfileImage(userProfileId);
    }
}

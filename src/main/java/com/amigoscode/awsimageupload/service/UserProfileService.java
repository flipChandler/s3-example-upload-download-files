package com.amigoscode.awsimageupload.service;

import com.amigoscode.awsimageupload.bucket.BucketName;
import com.amigoscode.awsimageupload.domain.UserProfile;
import com.amigoscode.awsimageupload.repository.UserProfileRepository;

import org.apache.http.entity.ContentType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.apache.http.entity.ContentType.IMAGE_PNG;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final FileStoreService fileStoreService;

    public UserProfileService(UserProfileRepository userProfileRepository,
                              FileStoreService fileStoreService) {
        this.userProfileRepository = userProfileRepository;
        this.fileStoreService = fileStoreService;
    }

    public List<UserProfile> getUserProfiles() {
        return this.userProfileRepository.getUserProfiles();
    }

    public void uploadUserProfileImage(UUID userProfileId, MultipartFile file) {
        // 1. Check if image is not empty
        isFileEmpty(file);

        // 2. If file is an image
        isAnImage(file);

        // 3. The user exists in our database
        UserProfile user = getUserProfileByIdOrThrow(userProfileId);

        // 4. Grab some metadata from file if any
        Map<String, String> metadata = extractMetadata(file);

        // 5. Store the image in S3 and update database (userProfileImageLink) with S3 image link
        String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), user.getUserProfileId());
        String fileName = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());

        try {
            fileStoreService.save(path, fileName, Optional.of(metadata), file.getInputStream());
            user.setUserProfileImageLink(fileName);
        } catch(IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public byte[] downloadUserProfileImage(UUID userProfileId) {
        UserProfile user = getUserProfileByIdOrThrow(userProfileId);
        String path = String.format("%s/%s",
                BucketName.PROFILE_IMAGE.getBucketName(),
                user.getUserProfileId());
        return user.getUserProfileImageLink()
                .map(key -> fileStoreService.download(path, key))
                .orElse(new byte[0]);
    }

    private Map<String, String> extractMetadata(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        return metadata;
    }

    private UserProfile getUserProfileByIdOrThrow(UUID userProfileId) {
        UserProfile user = userProfileRepository.getUserProfiles()
                .stream()
                .filter(userProfile -> userProfile.getUserProfileId().equals(userProfileId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("user profile %s not found!", userProfileId)));
        return user;
    }

    private void isFileEmpty(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file [" + file.getSize() + "]");
        }
    }

    private void isAnImage(MultipartFile file) {
        if (!Arrays.asList(
                ContentType.IMAGE_JPEG.getMimeType(), // MimeType = image/jpeg
                ContentType.IMAGE_PNG.getMimeType(),
                ContentType.IMAGE_GIF.getMimeType()).contains(file.getContentType())) {
            throw new IllegalStateException("file must be an image [" + file.getContentType() + "]");
        }
    }
}

package com.amigoscode.awsimageupload.bucket;

import org.springframework.beans.factory.annotation.Value;

public enum BucketName {

    PROFILE_IMAGE("felipe-amigoscode");

    private final String bucketName;

    BucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}

package com.taesan.tikkle.domain.file.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FileService {

	@Value("${file.upload.image-dir}")
	private String imageUploadDir;

	public byte[] getProfileImage(UUID memberId) {
		String profileImagePath = imageUploadDir + "/profiles-" + memberId.toString() + ".png";
		byte[] profileImage;
		try {
			Path path = Paths.get(profileImagePath);
			profileImage = Files.readAllBytes(path);
		} catch (IOException e) {
			profileImage = null;
		}
		return profileImage;
	}
}
package com.taesan.tikkle.domain.file.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taesan.tikkle.global.errors.ErrorCode;
import com.taesan.tikkle.global.exceptions.CustomException;

@Service
public class FileService {

	@Value("${file.upload.image-dir}")
	private String imageUploadDir;

	public byte[] getProfileImage(UUID memberId) {
		String profileImagePath = imageUploadDir + "/profile-" + memberId.toString() + ".png";
		byte[] profileImage;
		try {
			Path path = Paths.get(profileImagePath);
			profileImage = Files.readAllBytes(path);
		} catch (IOException e) {
			throw new CustomException(ErrorCode.MEMBER_IMAGE_NOT_FOUND);
		}
		return profileImage;
	}
}
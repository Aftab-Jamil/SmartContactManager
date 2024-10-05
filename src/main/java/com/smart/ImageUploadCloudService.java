package com.smart;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import javax.swing.plaf.basic.BasicToolTipUI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
@Service
public class ImageUploadCloudService {
	
	@Autowired
	private Cloudinary cloudinary;
	
	public String uploadImage(MultipartFile image) {
		
		
		try {
			String imageUuid=UUID.randomUUID().toString();
			byte [] data=new byte[image.getInputStream().available()];
			image.getInputStream().read(data);
			Map params1 = ObjectUtils.asMap(
					"public_id",imageUuid
				);
			cloudinary.uploader().upload(data,params1);
			return this.getUrlFromPublicId(imageUuid);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public void deleteImage(String id) throws IOException {
		if(!id.contains("user")) {
		cloudinary.uploader().destroy(id,ObjectUtils.emptyMap());
		}
		
	}
public String updateImage(MultipartFile image,String fileName) {
		try {
			deleteImage(fileName);
			String imageUuid=UUID.randomUUID().toString();
			byte [] data=new byte[image.getInputStream().available()];
			image.getInputStream().read(data);
			Map params1 = ObjectUtils.asMap(
					"public_id",imageUuid
				);
			cloudinary.uploader().upload(data,params1);
			return this.getUrlFromPublicId(imageUuid);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private String getUrlFromPublicId(String imageUuid) {
		return cloudinary.url().transformation(new Transformation<>().crop("fill")).generate(imageUuid);
	}
	

}

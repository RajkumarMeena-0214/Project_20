package com.rays.ctl;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rays.common.BaseCtl;
import com.rays.common.ORSResponse;
import com.rays.common.attachment.AttachmentDTO;
import com.rays.common.attachment.AttachmentServiceInt;
import com.rays.dto.UserDTO;
import com.rays.form.UserForm;
import com.rays.service.UserServiceInt;

@RestController
@RequestMapping(value = "User")
public class UserCtl extends BaseCtl<UserForm, UserDTO, UserServiceInt> {

	@Autowired
	private AttachmentServiceInt attachmentService;

	@PostMapping("/profilePic/{userId}")
	public ORSResponse uploadPic(@PathVariable Long userId, @RequestParam("file") MultipartFile file,
			HttpServletRequest req) {

		UserDTO dto = baseService.findById(userId);
		AttachmentDTO doc = new AttachmentDTO(file);
		doc.setDescription("profile pic");
		doc.setUserId(userId);

		if (dto.getImageId() != null) {
			doc.setId(dto.getImageId());
		}
		Long imageId = attachmentService.save(doc);
		if (dto.getImageId() == null) {
			dto.setImageId(imageId);
			baseService.update(dto);
		}
		ORSResponse res = new ORSResponse(true);
		res.addResult("imageId", imageId);
		return res;
	}

	@GetMapping("/profilePic/{userId}")
	public @ResponseBody void downloadPic(@PathVariable Long userId, HttpServletResponse response) {
		
		UserDTO dto = baseService.findById(userId);
		AttachmentDTO attachmentDto = null;
		if (dto != null && dto.getImageId() > 0) {
			attachmentDto = attachmentService.findById(dto.getImageId());
		}
		try {
			if (attachmentDto != null) {
				response.setContentType(attachmentDto.getType());
				OutputStream out = response.getOutputStream();
				out.write(attachmentDto.getDoc());
				out.close();
			} else {
				response.getWriter().write("ERROR : File not found");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

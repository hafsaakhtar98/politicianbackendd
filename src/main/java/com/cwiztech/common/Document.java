package com.cwiztech.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class Document {

	private static final Logger log = LoggerFactory.getLogger(Document.class);
	private static String student;
	private static String employee;
	private static String library;

	@Autowired
	public Document(Environment env) {
		Document.student = env.getRequiredProperty("file_path.student");
		Document.employee = env.getRequiredProperty("file_path.employee");
		Document.library = env.getRequiredProperty("file_path.library");
	}

	public static String save(long id, MultipartFile file, boolean isEmployeeDocument, boolean isStudentDocument, boolean islibraryDocument ) {

		String employeeDocument = employee;
		String studentDocument = student;
		String libraryDocument = library;

		SimpleDateFormat dateFormat1 = new SimpleDateFormat("ddMMyy-hhmmss");
		Date date = new Date();
		String fileName = "";

		if (!file.isEmpty()) {
			try {
				String[] split = file.getOriginalFilename().split("\\.");
				String ext = split[split.length - 1];
				fileName = "File-" + dateFormat1.format(date) + "." + ext;
				
				log.info(file.getName());
				log.info(file.getContentType());
				log.info(file.getOriginalFilename());
				log.info(fileName);

				if (isEmployeeDocument == true) {
					if (Files.notExists(Paths.get(employeeDocument + "/" + id), LinkOption.NOFOLLOW_LINKS))
						Files.createDirectory(Paths.get(employeeDocument + "/" + id));
					Files.copy(file.getInputStream(), Paths.get(employeeDocument + "/" + id, fileName));
				} else if(isStudentDocument==true) {
					if (Files.notExists(Paths.get(studentDocument + "/" + id), LinkOption.NOFOLLOW_LINKS))
						Files.createDirectory(Paths.get(studentDocument + "/" + id));
					Files.copy(file.getInputStream(), Paths.get(studentDocument + "/" + id, fileName));
				}else if(islibraryDocument==true)
				{
					if (Files.notExists(Paths.get(libraryDocument + "/" + id), LinkOption.NOFOLLOW_LINKS))
						Files.createDirectory(Paths.get(libraryDocument + "/" + id));
					Files.copy(file.getInputStream(), Paths.get(libraryDocument + "/" + id, fileName));
				}
			} catch (IOException e) {
				return "";
			}
		} else {
			return "";
		}

		return fileName;
	}

}

package com.leggett.glorious;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.leggett.glorious.photo.Photo;
import com.leggett.glorious.photo.PhotoRepository;

import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GloriousApplication {
	private static final Logger logger = LoggerFactory.getLogger(GloriousApplication.class);
	public static String PHOTO_ROOT = "";
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
	PhotoRepository photoRepository;
	public static void main(String[] args) throws IOException {
		PHOTO_ROOT = args[0];
		SpringApplication.run(GloriousApplication.class, args);
	}
	@Bean
	public CommandLineRunner demo(PhotoRepository photoRepository) {
		return (args) -> {
			Set<String> photos = getFiles(PHOTO_ROOT);
			for (String photo : photos) {
				if (photo.toLowerCase().endsWith(".jpg")) {
					Date dateTaken = null;
					try {
						// Get the original date taken
						File file = new File(photo);
						final ImageMetadata metadata = Imaging.getMetadata(file);
						if (metadata instanceof JpegImageMetadata) {
							final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
							final TiffField field = jpegMetadata
									.findEXIFValueWithExactMatch(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
							dateTaken = formatter.parse(field.getValueDescription().replace("'", ""));
						}
						
					} catch (Exception e) {
						logger.error(e.getMessage());
					}finally{
						photoRepository.save(new Photo(photo, dateTaken));
					}
					
				}
			}
		};
	};

	/**
	 * Retrieve all files recursivley from the specified directory
	 * 
	 * @param dir
	 * @return
	 * @throws IOException
	 */
	public static Set<String> getFiles(String dir) throws IOException {
		Set<String> fileList = new HashSet<>();
		Files.walkFileTree(Paths.get(dir), new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				if (!Files.isDirectory(file)) {
					fileList.add(file.toString());
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException e) throws IOException {
				System.err.printf("Visiting failed for %s\n", file);

				return FileVisitResult.SKIP_SUBTREE;
			}

			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				System.out.printf("About to visit directory %s\n", dir);

				return FileVisitResult.CONTINUE;
			}
		});
		return fileList;
	}
}

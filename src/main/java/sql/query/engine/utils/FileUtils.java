package sql.query.engine.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileUtils {
	
	public static Path createQueryFile(String query, String path) throws IOException, URISyntaxException {
		return Files.write(Paths.get(path), query.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE_NEW);
	}
	
	public static boolean deleteQueryFile(Path path) throws IOException {
		return Files.deleteIfExists(path);
	}
}

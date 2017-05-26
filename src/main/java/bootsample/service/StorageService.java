package bootsample.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

public interface StorageService {

    void init();

    String store(Path path,MultipartFile file);
    String store(String path,InputStream inStream, String key) throws FileNotFoundException;

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

}

package ee.sk.hwcrypto.demo.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import ee.sk.hwcrypto.demo.model.BdocFailid;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    void store(MultipartFile file);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);
    
    ArrayList<BdocFailid> getFailid();

    void deleteAll();

}

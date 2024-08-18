package programar.app.services.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
@Service
public class FileServiceImpl {
    @Value("${file.storage.location:img}") // Asume 'img' como subdirectorio de resources/static
    private String fileStorageLocation;

    // Método para eliminar un archivo
    public boolean deleteFile(String fileName) {
        Path filePath = Paths.get(fileStorageLocation, fileName);

        // Usar la ruta actual del directorio de trabajo
        File file = new File(filePath.toUri());

        try {
            if (file.exists() && file.delete()) {
                return true;
            } else {
                log.info("El archivo no existe o no se pudo eliminar.");
                return false;
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            return false;
        }
    }
}

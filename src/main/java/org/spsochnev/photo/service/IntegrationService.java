package org.spsochnev.photo.service;


import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.ftp.session.FtpRemoteFileTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
class IntegrationService {

    @Value("${photos.root.directory}")
    private String rootDir;

    @Value("${photos.folder.name}")
    private String folderName;

    @Value("${photos.file.mask}")
    private String fileMask;

    @Autowired
    private FtpRemoteFileTemplate template;

    public byte[] getPhoto(String path) {
        return template.execute(session -> {
            if (!session.exists(path)) {
                throw new FileNotFoundException();
            }
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            session.read(path, stream);
            return stream.toByteArray();
        });
    }

    public List<PhotoDTO> listPhotos() {
        Map<String, FTPFile> directories = getDirectoriesByName(rootDir, folderName);
        Map<String, FTPFile> pictures = new HashMap<>();
        for (String path: directories.keySet()) {
            pictures.putAll(getFilesByRegexMatch(path, fileMask));
        }
        return pictures.entrySet().stream()
                .map(e -> new PhotoDTO(e.getKey(), e.getValue().getTimestamp(), e.getValue().getSize()))
                .collect(Collectors.toList());
    }

    private Map<String, FTPFile> getFilesByRegexMatch(String path, String expression) {
        return template.execute(session -> {
            FTPClient client = (FTPClient) session.getClientInstance();
            client.cwd(path);
            FTPFile[] files = client.listFiles();
            return Arrays.stream(files)
                    .filter(FTPFile::isFile)
                    .filter(f -> f.getName().matches(expression))
                    .collect(Collectors.toMap(f -> path + "/" + f.getName(), Function.identity()));
        });
    }

    private Map<String, FTPFile> getDirectoriesByName(String rootPath, String name) {
        return template.execute(session -> {
            FTPClient client = (FTPClient) session.getClientInstance();
            client.cwd(rootPath);
            FTPFile[] dirs = client.listDirectories();
            Map<String, FTPFile> result = new HashMap<>();
            for (FTPFile dir: dirs) {
                if (!dir.getName().equals(".") && !dir.getName().equals("..")) {
                    String dirPath = rootPath + "/" + dir.getName();
                    if (dir.getName().equalsIgnoreCase(name)) {
                        result.put(dirPath, dir);
                    }
                    result.putAll(getDirectoriesByName(dirPath, name));
                }
            }
            return result;
        });
    }
}
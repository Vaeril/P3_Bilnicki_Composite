package edu.io;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileNode implements IFileComposite{

    private final Path path;
    private long lineLength;

    FolderNode parent;

    public FileNode(Path path, FolderNode parent) throws IOException {
        this.path = path;
        this.parent = parent;
    }

    public String fileName(){
        return path.subpath(path.getNameCount() - 1, path.getNameCount()).toString();
    }

    public void scanLength() throws IOException {
        FileReader fileReader = new FileReader(path.toFile());
        BufferedReader reader = new BufferedReader(fileReader);
        lineLength = 0;
        while (reader.readLine() != null) lineLength++;
        reader.close();
        fileReader.close();
        if(parent != null)
            parent.addLineLength(lineLength);
    }

    @Override
    public Path path() {
        return path;
    }

    @Override
    public long lineLength() {
        return lineLength;
    }
}

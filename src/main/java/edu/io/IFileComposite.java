package edu.io;

import java.nio.file.Path;

public interface IFileComposite {

    public Path path();
    public long lineLength();
    public String fileName();

    public enum FileType {FOLDER, JAVA}
}

package edu.io;

import java.io.IOException;
import java.nio.file.Path;

public interface IFileComposite {

    public Path path();
    public long lineLength();
    public String fileName();

    public void accept(IVisitor visitor) throws IOException;

    public enum FileType {FOLDER, JAVA}
}

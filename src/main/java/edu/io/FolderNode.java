package edu.io;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class FolderNode implements IFileComposite{

    private final Path path;
    private long lineLength;

    private ArrayList<IFileComposite> children = new ArrayList<IFileComposite>();
    FolderNode parent;

    public FolderNode(Path path, FolderNode parent){

        this.path = path;
        this.parent = parent;
    }

    public String fileName(){
        return path.subpath(path.getNameCount() - 1, path.getNameCount()).toString();
    }

    public void add(IFileComposite child){
        children.add(child);
    }

    @Override
    public Path path() {
        return path;
    }

    @Override
    public long lineLength() {
        return lineLength;
    }

    public void addLineLength(long value){
        lineLength += value;
        if(parent != null)
            parent.addLineLength(value);
    }

    public int getChildCount() {
        return children.size();
    }

    public IFileComposite getChild(int index){
        return children.get(index);
    }

    public IFileComposite getChild(Path path){
        for (int i = 0; i < getChildCount(); i++) {
            if(getChild(i).path().subpath(0, getChild(i).path().getNameCount()).equals(path)) {
                return getChild(i);
            }
        }
        return null;
    }

    @Override
    public void accept(IVisitor visitor) throws IOException {
        visitor.visitNode(this);
    }
}

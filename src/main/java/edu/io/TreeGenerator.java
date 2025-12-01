package edu.io;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TreeGenerator {

    private IFileComposite root;

    private String[] ignoredFileRegexes;

    public TreeGenerator() {
        ignoredFileRegexes = new String[]{
                ".git",
        };
    }
    // creating tree
    public void createTree(Path rootPath) throws IOException {
        ArrayList<Path> files = getFiles(rootPath);

        root = new FolderNode(rootPath, null);
        for (int i = 1; i < files.size(); i++) {
            if(!shouldBeIgnored(files.get(i)))
                createNode(files.get(i));
        }
    }

    // creating nodes
    private void createNode(Path path) throws IOException{
        Path last = path.subpath(path.getNameCount() - 1, path.getNameCount());

        if(!Files.isDirectory(path)){
            createFileNode(path);
        } else {
            createFolderNode(path);
        }
    }

    private void createFileNode(Path path) throws IOException{
        FolderNode parent = getParent(path);
        IFileComposite newFile = new FileNode(path, parent);
        parent.add(newFile);
    }

    private void createFolderNode(Path path){
        FolderNode parent = getParent(path);
        parent.add(new FolderNode(path, parent));
    }

    // auxiliary
    private FolderNode getParent(Path path){
        path = path.subpath(0, path.getNameCount() - 1);
        FolderNode current = (FolderNode)root;
        for (int i = root.path().getNameCount(); i < path.getNameCount(); i++) {
            Path folderPath = path.subpath(0, i + 1);
            current = (FolderNode)current.getChild(folderPath);
        }
        return current;
    }

    // getting files
    private ArrayList<Path> getFiles(Path rootPath) throws IOException {
        System.out.println("Getting files...");

        try {
            ArrayList<Path> files = new ArrayList<Path>();
            Files.walk(rootPath)
                    .forEach(files::add);
            return files;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // visiting tree
    public void visitTree(IVisitor visitor) throws IOException{
        visitor.startVisitingTree(root);

        ArrayList<IFileComposite> currentPath = new ArrayList<IFileComposite>();
        currentPath.add(root);
        visitChildren(currentPath, visitor);

        visitor.endVisitingTree(root);
    }

    private void visitChildren(ArrayList<IFileComposite> currentPath, IVisitor visitor) throws IOException{
        IFileComposite last = currentPath.get(currentPath.size() - 1);

        last.accept(visitor);

        if(last instanceof  FolderNode && ((FolderNode)last).getChildCount() > 0){
            for (int i = 0; i < ((FolderNode)last).getChildCount(); i++) {
                currentPath.add(((FolderNode)last).getChild(i));
                visitChildren(currentPath, visitor);
                currentPath.remove(currentPath.size() - 1);
            }
        }
    }

    private boolean shouldBeIgnored(Path path){
        for (int i = 0; i < ignoredFileRegexes.length; i++) {
            Pattern pattern = Pattern.compile(ignoredFileRegexes[i], Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(path.toString());
            if(matcher.find()) {
                return true;
            }
        }
        return false;
    }
}

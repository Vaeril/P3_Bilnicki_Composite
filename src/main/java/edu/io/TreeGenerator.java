package edu.io;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class TreeGenerator {

    private IFileComposite root;
    private FileCounter fileCounter = new FileCounter();

    // creating tree
    public void createTree(Path rootPath) throws IOException {
        ArrayList<Path> files = getFiles(rootPath);

        root = new FolderNode(rootPath, null);
        for (int i = 1; i < files.size(); i++) {
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
        if(fileCounter.matchesAnyType(newFile))
            ((FileNode)newFile).scanLength();
        fileCounter.checkAndCountFile(newFile);
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

    // displaying
    public void displayTree(){
        ArrayList<IFileComposite> currentPath = new ArrayList<IFileComposite>();

        currentPath.add(root);

        displayChildren(currentPath);
    }

    private void displayChildren(ArrayList<IFileComposite> currentPath){
        IFileComposite last = currentPath.get(currentPath.size() - 1);

        System.out.println("\t");
        for (int i = 0; i < currentPath.size(); i++) {
            System.out.print("\t");
        }
        System.out.print(last.fileName() + (last.lineLength() > 0 ? " " + last.lineLength() : ""));

        if(last instanceof  FolderNode && ((FolderNode)last).getChildCount() > 0){
            for (int i = 0; i < ((FolderNode)last).getChildCount(); i++) {
                currentPath.add(((FolderNode)last).getChild(i));
                displayChildren(currentPath);
                currentPath.remove(currentPath.size() - 1);
            }
        }
    }

    public void displayStats() {
        for (int i = 0; i < fileCounter.countedTypes(); i++) {
            System.out.println(fileCounter.getDisplay(i));
            ArrayList<String> files = fileCounter.getFilesOfType(i);

            for (int j = 0; j < files.size(); j++) {
                System.out.println("\t" + files.get(j));
            }
        }
    }
}

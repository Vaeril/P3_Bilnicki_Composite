package edu.io;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConsoleManager {

    Path currentPath;
    boolean endProgram;
    TreeGenerator lastTree;

    DisplayVisitor displayVisitor = new DisplayVisitor();
    ExportVisitor exportVisitor = new ExportVisitor();

    public void Start() throws IOException {

        currentPath = Paths.get(System.getProperty("user.dir"));
        System.out.println("Available commands are: move [dir], scan, showTree, exportTree, end");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        do {
            System.out.print(currentPath + ": ");
            String s = br.readLine();
            int spaceIndex = s.indexOf(' ');
            if(spaceIndex == -1)
                spaceIndex = s.length();

            switch(s.substring(0, spaceIndex)) {
                case "move":
                    if(spaceIndex < s.length())
                        changeCurrentPath(s.substring(spaceIndex + 1, s.length()));
                    else
                        System.out.println("Needs directory to move");
                    break;
                case "scan":
                    scanCurrentDirectory();
                    System.out.println("Scanned current directory.");
                    break;
                case "showTree":
                    showCurrentDirectory();
                    System.out.println("");
                    break;
                case "exportTree":
                    exportTree();
                    System.out.println("Exported scanned tree.");
                    break;
                case "end":
                    endProgram = true;
                    System.out.println("Ending program");
                    break;
            }
        } while (!endProgram);
    }

    void scanCurrentDirectory() throws IOException {
        lastTree = new TreeGenerator();
        lastTree.createTree(currentPath);
    }

    void showCurrentDirectory() throws IOException{
        lastTree.visitTree(displayVisitor);
    }

    void exportTree() throws IOException {
        lastTree.visitTree(exportVisitor);
    }

    void changeCurrentPath(String change) {
        switch(change){
            case "..":
                currentPath = currentPath.getRoot().resolve(
                        currentPath.subpath(0, currentPath.getNameCount() - 1));
                break;
            default:
                String last = currentPath.subpath(currentPath.getNameCount() - 1, currentPath.getNameCount())
                        .toString();
                File f = new File(currentPath.toAbsolutePath().toString());
                File[] children = f.listFiles();
                for (int i = 0; i < children.length; i++) {
                    Path childPath = children[i].toPath();
                    if(children[i].isDirectory() &&
                            childPath.subpath(childPath.getNameCount() - 1, childPath.getNameCount())
                            .toString().equals(change)){
                        currentPath = currentPath.getRoot().resolve(childPath);
                        return;
                    }
                }

                System.out.println("There is no such directory");
                break;
        }
    }
}

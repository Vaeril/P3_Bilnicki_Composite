package edu.io;

import java.io.IOException;
import java.util.ArrayList;

public class DisplayVisitor implements IVisitor {

    private FileCounter fileCounter = new FileCounter();

    @Override
    public void startVisitingTree(IFileComposite root) {
        // Do nothing here
    }

    @Override
    public void visitNode(IFileComposite node) throws IOException {
        displayNode(node);
        addToFileCounter(node);
    }

    private void displayNode(IFileComposite node){
        System.out.println("\t");
        for (int i = 0; i < node.path().getNameCount(); i++) {
            System.out.print("\t");
        }
        System.out.print(node.fileName() + (node.lineLength() > 0 ? " " + node.lineLength() : ""));
    }

    private void addToFileCounter(IFileComposite node) throws IOException {
        if(fileCounter.matchesAnyType(node) && node.path().toFile().isFile())
            ((FileNode)node).scanLength();
        fileCounter.checkAndCountFile(node);
    }

    private void displayStats() {
        System.out.println("");
        for (int i = 0; i < fileCounter.countedTypes(); i++) {
            System.out.println(fileCounter.getDisplay(i) +
                    ": " + fileCounter.getFileCount(i) + ", lines: " + fileCounter.getLineCount(i));
            ArrayList<String> files = fileCounter.getFilesOfType(i);

            for (int j = 0; j < files.size(); j++) {
                System.out.println("\t" + files.get(j));
            }
        }
    }

    @Override
    public void endVisitingTree(IFileComposite root) {
        displayStats();
    }
}

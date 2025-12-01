package edu.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ExportVisitor implements IVisitor{
    private FileCounter fileCounter = new FileCounter();
    private IFileComposite root;

    FileWriter currentReport;

    public void startVisitingTree(IFileComposite root) throws IOException {
        this.root = root;
        currentReport = new FileWriter(new File("raport.md"), false);

        int subdirectoryCount = 0, totalFileCount = 0;
        currentReport.write("# General info\n");
        currentReport.write("- path: " + root.path() + "\n");
        currentReport.write("- subdirectory count: " + subdirectoryCount + "\n");
        currentReport.write("- total file count: " + totalFileCount + "\n\n");
        currentReport.write("# Tree\n");
        currentReport.write(root.path() + "\n");
    }

    @Override
    public void visitNode(IFileComposite node) throws IOException {
        writeNode(node);
        addToFileCounter(node);
    }

    private void writeNode(IFileComposite node) throws IOException{
        for (int i = 0; i < node.path().getNameCount() - root.path().getNameCount(); i++) {
            currentReport.write("\t");
        }
        currentReport.write("- " + node.fileName() + "\n");
    }

    private void addToFileCounter(IFileComposite node) throws IOException {
        if(fileCounter.matchesAnyType(node) && node.path().toFile().isFile())
            ((FileNode)node).scanLength();
        fileCounter.checkAndCountFile(node);
    }

    private void writeStats() throws IOException {
        currentReport.write("\n");
        currentReport.write("# Stats\n");
        currentReport.write("## Files by type\n");
        for (int i = 0; i < fileCounter.countedTypes(); i++) {
            currentReport.write("- " + fileCounter.getDisplay(i) + ":\n");
            ArrayList<String> files = fileCounter.getFilesOfType(i);

            for (int j = 0; j < files.size(); j++) {
                currentReport.write("\t- " + files.get(j) + "\n");
            }
        }

        currentReport.write("\n## Line count\n");
        for (int i = 0; i < fileCounter.countedTypes(); i++) {
            currentReport.write("- " + fileCounter.getDisplay(i) + ": " + fileCounter.getLineCount(i) + "\n");
        }
    }

    @Override
    public void endVisitingTree(IFileComposite root) throws IOException {
        writeStats();
        currentReport.close();
    }
}

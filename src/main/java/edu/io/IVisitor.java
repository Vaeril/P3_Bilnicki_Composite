package edu.io;

import java.io.IOException;

public interface IVisitor {

    public void startVisitingTree(IFileComposite root) throws IOException;
    public void visitNode(IFileComposite node) throws IOException;
    public void endVisitingTree(IFileComposite root) throws IOException;
}

package com.example.kkadaba.selfpositioningview;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

/**
 * @author Kushal Kadaba(Fuzz).
 */

public class BFSTraversal {
    public Queue<Element> queue = new ArrayDeque<>();
    public Visitor visitor;

    public BFSTraversal(Element rootNode, Visitor visitor) {
        this.visitor = visitor;
        queue.offer(rootNode);
    }

    public void traverse() {
        while (!queue.isEmpty()) {
            Element element = queue.poll();
            if (element != null) {
                element.accept(visitor);
                List<ViewGroupWrapper> elements = visitor.getElements();
                for (ViewGroupWrapper viewGroupWrapper : elements) {
                    queue.offer(viewGroupWrapper);
                }
            }
        }
    }

    public interface Visitor {
        void visit(ViewWrapper element);
        void visit(ViewGroupWrapper element);
        void addElement(ViewGroupWrapper element);
        List<ViewGroupWrapper> getElements();
    }
}

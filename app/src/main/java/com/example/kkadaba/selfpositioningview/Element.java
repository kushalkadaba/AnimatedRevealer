package com.example.kkadaba.selfpositioningview;

/**
 * @author Kushal Kadaba(Fuzz).
 */
public interface Element {
    void accept(BFSTraversal.Visitor visitor);
}

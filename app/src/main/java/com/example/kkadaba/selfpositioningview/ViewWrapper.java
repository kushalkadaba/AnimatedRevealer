package com.example.kkadaba.selfpositioningview;

import android.view.View;

/**
 * @author Kushal Kadaba(Fuzz).
 */
public class ViewWrapper implements Element{
    View view;

    public ViewWrapper(View view) {
        this.view = view;
    }

    @Override
    public void accept(BFSTraversal.Visitor visitor) {
        visitor.visit(this);
    }
}

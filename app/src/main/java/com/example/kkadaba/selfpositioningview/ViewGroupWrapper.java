package com.example.kkadaba.selfpositioningview;

import android.view.View;
import android.view.ViewGroup;

/**
 * @author Kushal Kadaba(Fuzz).
 */
public class ViewGroupWrapper implements Element{
    ViewGroup viewGroup;

    public ViewGroupWrapper(ViewGroup viewGroup) {
        this.viewGroup = viewGroup;
    }

    @Override
    public void accept(BFSTraversal.Visitor visitor) {
        visitor.visit(this);
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof ViewGroup) {
                visitor.addElement(new ViewGroupWrapper((ViewGroup) child));
            }
        }
    }

    public ViewGroup getContainedView() {
        return viewGroup;
    }
}

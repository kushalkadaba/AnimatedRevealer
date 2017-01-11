package com.example.kkadaba.selfpositioningview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created to explore transitions. Won't work at all in our situation.
 *
 * @author Kushal Kadaba(Fuzz).
 */

public class BitmapCutTransition extends Transition {

    private static final String PROPNAME_VALUE = "selfpositioning:bitmapcut:value";
    private View animatedRevealer;

    @Override
    public void captureEndValues(@NonNull TransitionValues transitionValues) {
        Map<String, Object> values = transitionValues.values;
        values.put(PROPNAME_VALUE, 1);
    }

    @Override
    public void captureStartValues(@NonNull TransitionValues transitionValues) {
        Map<String, Object> values = transitionValues.values;
        ViewGroup rootView = (ViewGroup) transitionValues.view.getRootView();
        findViewByType(rootView);
        values.put(PROPNAME_VALUE, 0);
    }

    void findViewByType(ViewGroup root) {
        BFSTraversal bfsTraversal = new BFSTraversal(new ViewGroupWrapper(root),
                new BFSTraversal.Visitor() {
                    List<ViewGroupWrapper> viewGroupList = new ArrayList<>();
                    @Override
                    public void visit(ViewWrapper element) {

                    }

                    @Override
                    public void visit(ViewGroupWrapper element) {
                        if (element.getContainedView() instanceof AnimatedRevealer) {
                            animatedRevealer = element.getContainedView();
                        }
                    }

                    @Override
                    public void addElement(ViewGroupWrapper element) {
                        viewGroupList.add(element);
                    }

                    @Override
                    public List<ViewGroupWrapper> getElements() {
                        List<ViewGroupWrapper> templist = new ArrayList<>(viewGroupList);
                        viewGroupList.clear();
                        return templist;
                    }
                });
        bfsTraversal.traverse();
    }

    @NonNull
    @Override
    public Transition excludeChildren(@NonNull Class type, boolean exclude) {
        return super.excludeChildren(AnimatedRevealer.class, exclude);
    }

    @Nullable
    @Override
    public Animator createAnimator(@NonNull ViewGroup sceneRoot, @Nullable TransitionValues startValues, @Nullable TransitionValues endValues) {
        Animator animator = null;
        if (animatedRevealer instanceof AnimatedRevealer && startValues != null && endValues != null) {
            Map<String, Object> startValueMap = startValues.values;
            Map<String, Object> endValueMap = endValues.values;
            animator = ObjectAnimator.ofFloat(animatedRevealer, "xTranslation", (float) startValueMap.get(PROPNAME_VALUE),
                    (float) endValueMap.get(PROPNAME_VALUE));
        }
        return animator;
    }
}

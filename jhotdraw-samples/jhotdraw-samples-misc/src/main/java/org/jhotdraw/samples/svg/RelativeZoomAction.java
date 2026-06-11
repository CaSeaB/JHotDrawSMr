/*
 * @(#)RelativeZoomAction.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.samples.svg;

import javax.swing.Action;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.action.AbstractDrawingViewAction;

/**
 * Changes the zoom factor of a drawing view relative to its current value.
 */
public class RelativeZoomAction extends AbstractDrawingViewAction {

    private static final long serialVersionUID = 1L;
    public static final String ZOOM_IN_ID = "view.zoomIn";
    public static final String ZOOM_OUT_ID = "view.zoomOut";
    public static final double DEFAULT_ZOOM_STEP = 1.25d;
    public static final double MIN_SCALE_FACTOR = 0.01d;
    public static final double MAX_SCALE_FACTOR = 50d;
    private final double zoomFactor;

    public RelativeZoomAction(DrawingView view, double zoomFactor, String name) {
        super(view);
        this.zoomFactor = zoomFactor;
        putValue(Action.NAME, name);
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        DrawingView view = getView();
        view.setScaleFactor(computeScaleFactor(view.getScaleFactor(), zoomFactor));
    }

    public static double computeScaleFactor(double currentScaleFactor, double zoomFactor) {
        return Math.max(MIN_SCALE_FACTOR, Math.min(MAX_SCALE_FACTOR, currentScaleFactor * zoomFactor));
    }
}

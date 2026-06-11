package org.jhotdraw.draw.action;

import java.awt.Color;
import java.awt.event.ActionEvent;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.util.ResourceBundleUtil;

public class InvertColorsAction extends AbstractSelectedAction {
    private static final long serialVersionUID = 1L;
    public static final String ID = "edit.invertColors";

    public InvertColorsAction(DrawingEditor editor) {
        super(editor);
        ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        labels.configureAction(this, ID);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (getView() == null || getView().getSelectedFigures().isEmpty()) {
            return;
        }

        for (Figure figure : getView().getSelectedFigures()) {
            invertFigureColors(figure);
        }
    }

    private void invertFigureColors(Figure figure) {
        //Calculate and reassign Fill Color
        Color currentFill = figure.get(AttributeKeys.FILL_COLOR);
        if (currentFill != null) {
            Color invertedFill = new Color(
                    255 - currentFill.getRed(),
                    255 - currentFill.getGreen(),
                    255 - currentFill.getBlue(),
                    currentFill.getAlpha()
            );
            figure.set(AttributeKeys.FILL_COLOR, invertedFill);
        }

        //Calculate and reassign Stroke Color
        Color currentStroke = figure.get(AttributeKeys.STROKE_COLOR);
        if (currentStroke != null) {
            Color invertedStroke = new Color(
                    255 - currentStroke.getRed(),
                    255 - currentStroke.getGreen(),
                    255 - currentStroke.getBlue(),
                    currentStroke.getAlpha()
            );
            figure.set(AttributeKeys.STROKE_COLOR, invertedStroke);
        }
    }
}

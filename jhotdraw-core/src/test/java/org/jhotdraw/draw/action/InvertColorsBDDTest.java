package org.jhotdraw.draw.action;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.junit.ScenarioTest;
import java.awt.Color;
import java.util.HashSet;
import java.util.Set;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.Figure;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvertColorsBDDTest extends ScenarioTest<InvertColorsBDDTest.GivenCanvasState, InvertColorsBDDTest.WhenToolbarAction, InvertColorsBDDTest.ThenCanvasOutcome> {

    @Test
    public void inverting_primary_colors_on_a_shape() {
        given().a_shape_is_selected_on_the_canvas_with_fill_color_$_and_stroke_color_(Color.BLACK, Color.WHITE);
        when().the_user_clicks_the_invert_colors_toolbar_action();
        then().the_shapes_fill_color_must_become_$_and_stroke_color_must_become__(Color.WHITE, Color.BLACK);
    }

    // GIVEN STAGE: Preconditions & Initial Context

    public static class GivenCanvasState extends Stage<GivenCanvasState> {
        @ProvidedScenarioState
        private DrawingEditor mockEditor;
        @ProvidedScenarioState
        private Figure mockFigure;

        public GivenCanvasState a_shape_is_selected_on_the_canvas_with_fill_color_$_and_stroke_color_(Color fill, Color stroke) {
            mockEditor = mock(DrawingEditor.class);
            DrawingView mockView = mock(DrawingView.class);
            mockFigure = mock(Figure.class);

            Set<Figure> selectedFigures = new HashSet<>();
            selectedFigures.add(mockFigure);

            Mockito.when(mockEditor.getActiveView()).thenReturn(mockView);
            Mockito.when(mockView.getSelectedFigures()).thenReturn(selectedFigures);

            Mockito.when(mockFigure.get(AttributeKeys.FILL_COLOR)).thenReturn(fill);
            Mockito.when(mockFigure.get(AttributeKeys.STROKE_COLOR)).thenReturn(stroke);

            return self();
        }
    }

    // WHEN STAGE: The Interaction Trigger

    public static class WhenToolbarAction extends Stage<WhenToolbarAction> {
        @ExpectedScenarioState
        private DrawingEditor mockEditor;

        public WhenToolbarAction the_user_clicks_the_invert_colors_toolbar_action() {
            InvertColorsAction action = new InvertColorsAction(mockEditor);
            action.actionPerformed(null);
            return self();
        }
    }

    // THEN STAGE: Verifiable Side-Effects & Business Assertions

    public static class ThenCanvasOutcome extends Stage<ThenCanvasOutcome> {
        @ExpectedScenarioState
        private Figure mockFigure;

        public ThenCanvasOutcome the_shapes_fill_color_must_become_$_and_stroke_color_must_become__(Color expectedFill, Color expectedStroke) {
            verify(mockFigure).set(AttributeKeys.FILL_COLOR, expectedFill);
            verify(mockFigure).set(AttributeKeys.STROKE_COLOR, expectedStroke);
            return self();
        }
    }
}

package org.jhotdraw.draw.action;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.Figure;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
public class InvertColorsActionTest {

    private DrawingEditor mockEditor;
    private DrawingView mockView;
    private Figure mockFigure;
    private InvertColorsAction action;
    private Set<Figure> selectedFigures;

    @Before
    public void setUp() {
        // Initialize Mock objects to isolate the unit test from GUI components
        mockEditor = mock(DrawingEditor.class);
        mockView = mock(DrawingView.class);
        mockFigure = mock(Figure.class);

        selectedFigures = new HashSet<>();
        selectedFigures.add(mockFigure);

        // Configure the cascading mock framework behavior
        when(mockEditor.getActiveView()).thenReturn(mockView);
        when(mockView.getSelectedFigures()).thenReturn(selectedFigures);

        // Instantiate the controller under test
        action = new InvertColorsAction(mockEditor);
    }

    @Test
    public void testInvertColors_BestCaseScenario() {
        // Arrange: Setup a standard solid color (RGB: 100, 150, 200)
        Color testColor = new Color(100, 150, 200, 255);
        when(mockFigure.get(AttributeKeys.FILL_COLOR)).thenReturn(testColor);
        when(mockFigure.get(AttributeKeys.STROKE_COLOR)).thenReturn(null);

        // Act: Invoke the action logic directly
        action.actionPerformed(null);

        // Assert: Verify the mathematical inversion calculation (255 - original)
        Color expectedColor = new Color(155, 105, 55, 255);

        // Capture that the updated attribute was sent back to the model container
        verify(mockFigure).set(AttributeKeys.FILL_COLOR, expectedColor);
    }

    @Test
    public void testInvertColors_BoundaryCase_NullColor() {
        // Arrange: Emulate a shape that has no fill color
        when(mockFigure.get(AttributeKeys.FILL_COLOR)).thenReturn(null);
        when(mockFigure.get(AttributeKeys.STROKE_COLOR)).thenReturn(null);

        // Act
        action.actionPerformed(null);

        // Assert: The system must handle the null gracefully without throwing a NullPointerException
        verify(mockFigure, never()).set(any(), any());
    }
}

package org.jhotdraw.undoRedo;

import org.jhotdraw.undo.UndoRedoManager;
import org.jhotdraw.util.ResourceBundleUtil;
import javax.swing.JButton;
import javax.swing.undo.AbstractUndoableEdit;
import java.lang.reflect.Field;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class UndoRedoStage {
    private UndoRedoManager manager;
    private JButton undoButton;
    private JButton redoButton;

    public UndoRedoStage() {
        try {
            ResourceBundleUtil mockLabels = mock(ResourceBundleUtil.class);
            Field labelsField = UndoRedoManager.class.getDeclaredField("labels");
            labelsField.setAccessible(true);
            labelsField.set(null, mockLabels);
            
            this.manager = new UndoRedoManager();
            this.undoButton = new JButton(manager.getUndoAction());
            this.redoButton = new JButton(manager.getRedoAction());
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize UndoRedoManager with mocked labels", e);
        }
    }

    public UndoRedoStage an_edit_is_made() {
        manager.addEdit(new TestEdit());
        return this;
    }

    public UndoRedoStage five_edits_are_made() {
        for (int i = 0; i < 5; i++) an_edit_is_made();
        return this;
    }

    public UndoRedoStage the_edit_is_undone() {
        undoButton.doClick();
        return this;
    }

    public UndoRedoStage five_edits_are_undone() {
        for (int i = 0; i < 5; i++) the_edit_is_undone();
        return this;
    }

    public UndoRedoStage i_click_undo() {
        undoButton.doClick();
        return this;
    }

    public UndoRedoStage i_click_undo_five_times() {
        for (int i = 0; i < 5; i++) i_click_undo();
        return this;
    }

    public UndoRedoStage i_click_redo() {
        redoButton.doClick();
        return this;
    }

    public UndoRedoStage i_click_redo_five_times() {
        for (int i = 0; i < 5; i++) i_click_redo();
        return this;
    }

    public void the_undo_button_should_be_disabled() {
        assertThat(undoButton.isEnabled()).isFalse();
    }

    public void the_redo_button_should_be_disabled() {
        assertThat(redoButton.isEnabled()).isFalse();
    }

    private static class TestEdit extends AbstractUndoableEdit {
        private static final long serialVersionUID = 1L;
        @Override public boolean isSignificant() { return true; }
    }
}
package org.jhotdraw.undoRedo;

import org.junit.Before;
import org.junit.Test;

public class UndoRedoManagerBDDTest {
    private UndoRedoStage stage;

    @Before
    public void setUp() {
        stage = new UndoRedoStage();
    }

    private UndoRedoStage given() { return stage; }
    private UndoRedoStage when() { return stage; }
    private UndoRedoStage then() { return stage; }

    @Test
    public void undo_a_single_edit_disables_button() {
        given().an_edit_is_made();
        when().i_click_undo();
        then().the_undo_button_should_be_disabled();
    }

    @Test
    public void undo_five_edits_disables_button() {
        given().five_edits_are_made();
        when().i_click_undo_five_times();
        then().the_undo_button_should_be_disabled();
    }

    @Test
    public void redo_a_single_edit_disables_button() {
        given().an_edit_is_made();
        given().the_edit_is_undone();
        when().i_click_redo();
        then().the_redo_button_should_be_disabled();
    }

    @Test
    public void redo_five_edits_disables_button() {
        given().five_edits_are_made();
        given().five_edits_are_undone();
        when().i_click_redo_five_times();
        then().the_redo_button_should_be_disabled();
    }
}
package org.jhotdraw.undoRedo;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import org.jhotdraw.util.ResourceBundleUtil;
import java.lang.reflect.Field;

import javax.swing.undo.UndoableEdit;
import org.junit.Before;
import org.junit.Test;
import org.jhotdraw.undo.UndoRedoManager;

public class UndoRedoManagerTest {

    private static class CountingEdit extends javax.swing.undo.AbstractUndoableEdit {
        private static final long serialVersionUID = 1L;
        int undoCount = 0;
        int redoCount = 0;

        @Override
        public boolean isSignificant() {
            return true;
        }

        @Override
        public void undo() {
            super.undo();
            undoCount++;
        }

        @Override
        public void redo() {
            super.redo();
            redoCount++;
        }
    }
    private UndoRedoManager manager;
    private UndoableEdit mockEdit;

    @Before
    public void setUp() {
        // Provide a mocked ResourceBundleUtil to avoid missing resource exception
        ResourceBundleUtil mockLabels = mock(ResourceBundleUtil.class);
        when(mockLabels.getString("edit.undo.text")).thenReturn("Undo");
        when(mockLabels.getString("edit.redo.text")).thenReturn("Redo");
        doNothing().when(mockLabels).configureAction(any(javax.swing.Action.class), anyString());
        try {
            Field labelsField = UndoRedoManager.class.getDeclaredField("labels");
            labelsField.setAccessible(true);
            labelsField.set(null, mockLabels);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        manager = new UndoRedoManager();

        mockEdit = mock(UndoableEdit.class);
        when(mockEdit.isSignificant()).thenReturn(true);
    }

    @Test
    public void testAddEdit_SuccessfulInsertion() {
        boolean result = manager.addEdit(mockEdit);
        assertTrue("The manager must successfully accept a unique operational record.", result);
    }

    @Test
    public void testAddEdit_SuccessfulMultipleInsertion() {
        UndoRedoManager manager = new UndoRedoManager();
        CountingEdit edit = new CountingEdit();
        CountingEdit edit2 = new CountingEdit();
        
        boolean firstResult = manager.addEdit(edit);
        boolean secondResult = manager.addEdit(edit2);
        
        assertTrue("First unique edit should be accepted.", firstResult);
        assertTrue("Second unique edit should be accepted.", secondResult);
        
        assertTrue("Manager should be able to undo the second edit.", manager.canUndo());
        
        manager.undo();
        assertEquals("Second edit should have been undone.", 1, edit2.undoCount);
        assertEquals("First edit should not have been undone yet.", 0, edit.undoCount);
    }

    @Test
    public void testUndo_DuplicateReference_DoesNotCorruptStack() {
        UndoRedoManager manager = new UndoRedoManager();
        CountingEdit edit = new CountingEdit();
        
        manager.addEdit(edit);
        manager.addEdit(edit); 
        
        assertTrue("Manager should be able to undo", manager.canUndo());
        
        manager.undo();
        
        assertEquals("Should only undo the edit once", 1, edit.undoCount);
        assertFalse("Stack should be empty after one undo", manager.canUndo());
    }

    @Test
    public void testUndo_DuplicateReference_LeavesStackIntact() {
        UndoRedoManager manager = new UndoRedoManager();
        CountingEdit edit1 = new CountingEdit();
        CountingEdit edit2 = new CountingEdit();
        
        manager.addEdit(edit1);
        manager.addEdit(edit2);
        manager.addEdit(edit2); 
        
        assertTrue("Manager should be able to undo the top edit", manager.canUndo());
        
        manager.undo();
        assertEquals("edit2 should have been undone exactly once", 1, edit2.undoCount);
        
        assertTrue("Stack should still contain edit1 after undoing edit2", manager.canUndo());
        
        manager.undo();
        assertEquals("edit1 should have been undone exactly once", 1, edit1.undoCount);
        
        assertFalse("Stack should be empty after undoing both unique edits", manager.canUndo());
    }

    @Test
    public void testRedo_BasicFunctionality() {
        UndoRedoManager manager = new UndoRedoManager();
        CountingEdit edit = new CountingEdit();
        
        manager.addEdit(edit);
        manager.undo();
        
        assertTrue("Manager should allow redo after undo", manager.canRedo());
        
        manager.redo();
        assertEquals("Edit should have been redone exactly once", 1, edit.redoCount);
        assertFalse("Redo stack should be empty after redo", manager.canRedo());
    }

    @Test
    public void testRedo_ComplexSequenceWithDuplicates() {
        UndoRedoManager manager = new UndoRedoManager();
        CountingEdit edit1 = new CountingEdit();
        CountingEdit edit2 = new CountingEdit();
        
        manager.addEdit(edit1);
        manager.addEdit(edit2);
        manager.addEdit(edit2); 
        
        manager.undo();
        manager.undo();
        
        manager.redo();
        assertEquals("edit1 should be redone first", 1, edit1.redoCount);
        
        manager.redo();
        assertEquals("edit2 should be redone second", 1, edit2.redoCount);
        
        assertFalse("No more items to redo", manager.canRedo());
    }
}
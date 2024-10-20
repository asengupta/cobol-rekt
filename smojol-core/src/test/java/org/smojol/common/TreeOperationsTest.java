package org.smojol.common;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;
import org.smojol.common.transpiler.*;
import org.smojol.common.vm.type.TypedRecord;

import static org.junit.jupiter.api.Assertions.*;

public class TreeOperationsTest {
    @Test
    public void canReplaceExistingNodeWithMultipleNodes() {
        TranspilerNode set1 = set("ABC", 30);
        TranspilerNode set2 = set("DEF", 40);
        TranspilerNode set3 = set("PQR", 50);
        TranspilerNode set4 = set("XYZ", 60);
        TranspilerNode parent = new TranspilerCodeBlockNode(ImmutableList.of(set1, set2));
        assertTrue(parent.replace(set1, ImmutableList.of(set3, set4)));
        assertEquals(ImmutableList.of(set3, set4, set2), parent.astChildren());
    }

    @Test
    public void cannotReplaceNonExistentNodes() {
        TranspilerNode set1 = set("ABC", 30);
        TranspilerNode set2 = set("DEF", 40);
        TranspilerNode set3 = set("PQR", 50);
        TranspilerNode parent = new TranspilerCodeBlockNode(ImmutableList.of(set1));
        assertFalse(parent.replace(set2, ImmutableList.of(set3)));
        assertEquals(ImmutableList.of(set1), parent.astChildren());
    }

    @Test
    public void cannotTouchEmptyChildrenList() {
        TranspilerNode set1 = set("ABC", 30);
        TranspilerNode set2 = set("DEF", 40);
        TranspilerNode parent = new TranspilerCodeBlockNode(ImmutableList.of());
        assertFalse(parent.replace(set1, ImmutableList.of(set2)));
        assertEquals(ImmutableList.of(), parent.astChildren());
    }

    @Test
    public void cannotInsertAfterSpecifiedChildIfNoChildren() {
        TranspilerNode set1 = set("ABC", 30);
        TranspilerNode set2 = set("DEF", 40);
        TranspilerNode parent = new TranspilerCodeBlockNode(ImmutableList.of());
        assertFalse(parent.addAfter(set1, ImmutableList.of(set2)));
        assertEquals(ImmutableList.of(), parent.astChildren());
    }

    @Test
    public void canInsertAfterSpecifiedChild() {
        TranspilerNode set1 = set("ABC", 30);
        TranspilerNode set2 = set("DEF", 40);
        TranspilerNode set3 = set("PQR", 50);
        TranspilerNode set4 = set("XYZ", 60);
        TranspilerNode parent = new TranspilerCodeBlockNode(ImmutableList.of(set1, set2));
        assertTrue(parent.addAfter(set1, ImmutableList.of(set3, set4)));
        assertEquals(ImmutableList.of(set1, set3, set4, set2), parent.astChildren());
    }

    @Test
    public void canInsertAfterSpecifiedChildIfChildIsLastElement() {
        TranspilerNode set1 = set("ABC", 30);
        TranspilerNode set2 = set("DEF", 40);
        TranspilerNode set3 = set("PQR", 50);
        TranspilerNode set4 = set("XYZ", 60);
        TranspilerNode parent = new TranspilerCodeBlockNode(ImmutableList.of(set1, set2));
        assertTrue(parent.addAfter(set2, ImmutableList.of(set3, set4)));
        assertEquals(ImmutableList.of(set1, set2, set3, set4), parent.astChildren());
    }

    private static SetTranspilerNode set(String variable, int value) {
        return new SetTranspilerNode(new SymbolReferenceNode(variable), new PrimitiveValueTranspilerNode(TypedRecord.typedNumber(value)));
    }
}

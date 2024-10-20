package org.smojol.common.transpiler;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import org.smojol.common.ast.SemanticCategory;
import org.smojol.common.id.Identifiable;

import java.util.*;

public abstract class TranspilerNode implements Identifiable {
    protected final Map<String, Object> properties;
    @Getter private final List<SemanticCategory> categories;
    protected final List<TranspilerNode> childTranspilerNodes = new ArrayList<>();
    protected final String id;

    public TranspilerNode(List<SemanticCategory> categories) {
        this(ImmutableList.of(), ImmutableMap.of(), categories);
    }

    protected TranspilerNode(List<TranspilerNode> childTranspilerNodes, Map<String, Object> additionalAttributes, List<SemanticCategory> categories) {
        this.childTranspilerNodes.addAll(childTranspilerNodes);
        this.properties = additionalAttributes;
        this.categories = categories;
        this.id = UUID.randomUUID().toString();
    }

    public <E> TranspilerNode(List<TranspilerNode> childTranspilerNodes, List<SemanticCategory> categories) {
        this(childTranspilerNodes, ImmutableMap.of(), categories);
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String label() {
        return description();
    }

    @Override
    public String toString() {
        return description();
    }

    public abstract String description();

    public <T> T getProperty(String key) {
        if (!properties.containsKey(key)) return null;
        return (T) properties.get(key);
    }

    public Collection<TranspilerNode> astChildren() {
        return childTranspilerNodes;
    }

    public Collection<TranspilerNode> internalElements() {
        return ImmutableList.of();
    }

    public String shortDescription() {
        return description();
    }

    public boolean replace(TranspilerNode replacedNode, List<TranspilerNode> replacementNodes) {
        int i = childTranspilerNodes.indexOf(replacedNode);
        if (i == -1) return false;
        childTranspilerNodes.remove(replacedNode);
        childTranspilerNodes.addAll(i, replacementNodes);
        return true;
    }

    public boolean addAfter(TranspilerNode targetNode, ImmutableList<TranspilerNode> successors) {
        int i = childTranspilerNodes.indexOf(targetNode);
        if (i == -1) return false;
        childTranspilerNodes.addAll(i + 1, successors);
        return true;
    }
}

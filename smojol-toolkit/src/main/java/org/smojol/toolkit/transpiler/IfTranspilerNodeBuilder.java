package org.smojol.toolkit.transpiler;

import org.smojol.common.transpiler.DetachedTranspilerCodeBlock;
import org.smojol.common.transpiler.IfTranspilerNode;
import org.smojol.common.transpiler.TranspilerCodeBlock;
import org.smojol.common.transpiler.TranspilerNode;
import org.smojol.common.vm.expression.CobolExpression;
import org.smojol.common.vm.structure.CobolDataStructure;
import org.smojol.toolkit.ast.ConditionalStatementFlowNode;
import org.smojol.toolkit.ast.IfFlowNode;

public class IfTranspilerNodeBuilder {
    public static TranspilerNode build(IfFlowNode n, CobolDataStructure dataStructures) {
        CobolExpression condition = n.getConditionExpression();
        TranspilerExpressionBuilder nodeBuilder = new TranspilerExpressionBuilder(dataStructures);
        TranspilerNode transpilerCondition = nodeBuilder.build(condition);
        TranspilerCodeBlock ifThenBlock = new DetachedTranspilerCodeBlock(n.getIfThenBlock().astChildren().stream().filter(c -> c instanceof ConditionalStatementFlowNode).map(stmt -> TranspilerTreeBuilder.flowToTranspiler(stmt, dataStructures)).toList());
        TranspilerCodeBlock ifElseBlock = n.getIfElseBlock() != null ? new DetachedTranspilerCodeBlock(n.getIfElseBlock().astChildren().stream().filter(c -> c instanceof ConditionalStatementFlowNode).map(stmt -> TranspilerTreeBuilder.flowToTranspiler(stmt, dataStructures)).toList()) : new TranspilerCodeBlock();
        return new IfTranspilerNode(transpilerCondition, ifThenBlock, ifElseBlock);
    }
}

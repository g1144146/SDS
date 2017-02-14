package sds.decompile.cond_expr;

import java.util.Arrays;
import sds.assemble.controlflow.CFEdge;
import sds.assemble.controlflow.CFNode;
import sds.classfile.bytecode.BranchOpcode;
import sds.classfile.bytecode.OpcodeInfo;

import static sds.assemble.controlflow.CFEdgeType.FalseBranch;
import static sds.assemble.controlflow.CFEdgeType.TrueBranch;

/**
 * This class is for conditional expression.
 * @author inagaki
 */
public class Expression {
	String expr;
	int[] range;
	int jumpPoint;
	Expression trueExpr;
	Expression falseExpr;
	Child child;

	public enum Child {
		TRUE, FALSE, OWN;
	}

	public Expression(int number, String expr, CFNode node) {
		this.expr  = expr;
		this.range = new int[]{ -1, 0 };
		int ifCount = 0;	
		for(OpcodeInfo op : node.getOpcodes().getAll()) {
			if((ifCount == number)) {
				if(range[0] == -1) {
					range[0] = op.getPc();
				} else if(isIf(op)) {
					BranchOpcode branch = (BranchOpcode)op;
					range[1] = branch.getPc();
					this.jumpPoint = branch.getBranch() + range[1];
					break;
				}
			}
			if(isIf(op)) {
				ifCount++;
				if(ifCount > number) {
					break;
				}
			}
		}
		for(CFEdge edge : node.getChildren()) {
			if(edge.getDest().isInPcRange(jumpPoint)) {
				if(edge.getType() == TrueBranch) {
					this.child = Child.TRUE;
				} else if(edge.getType() == FalseBranch) {
					reverse();
					this.child = Child.FALSE;
				}
				break;
			}
		}
		if(child == null) {
			this.child = Child.OWN;
		}
	}

	/**
	 * combines specified expression and this.<br>
	 * @param newExpr conditional expression
	 * @param logical logical operator
	 */
	public void combine(Expression newExpr, String logical) {
		this.expr = "(" + expr + logical + newExpr.expr + ")";
		this.trueExpr = newExpr.trueExpr;
		this.child = newExpr.child;
		range[1] = newExpr.range[1];
	}

	/**
	 * returns whether destination is this expression.
	 * @param pc index of code array.
	 * @return
	 * if destination is this expression, this method returns true.<br>
	 * otherwise, it returns false.
	 */
	public boolean isDestination(int pc) {
		return (range[0] <= pc) && (pc <= range[1]);
	}

	/**
	 * reverses comparing operator of this expression.
	 */
	public void reverse() {
		expr = changeOperator(expr);
	}

	private String changeOperator(String target) {
		if(target.contains("=="))   return target.replace("==", "!=");
		if(target.contains("!="))   return target.replace("!=", "==");
		if(target.contains(">="))   return target.replace(">=", "<");
		if(target.contains("<="))   return target.replace("<=", ">");
		if(target.contains(">"))    return target.replace(">" , "<=");
		if(target.contains(">"))    return target.replace("<" , ">=");
		if(target.startsWith("(!")) return target.replace("(!", "(");
		return "(! " + target + ")";
	}

	private boolean isIf(OpcodeInfo opcode) {
		return (opcode instanceof BranchOpcode) && ((BranchOpcode)opcode).isIf();
	}

	@Override
	public boolean equals(Object obj) {
		if(! (obj instanceof Expression)) {
			return false;
		}
		Expression target = (Expression)obj;
		boolean equal = true;
		equal &= expr.equals(target.expr);
		equal &= Arrays.equals(range, target.range);
		equal &= (jumpPoint == target.jumpPoint);
		equal &= (child     == target.child);
		return equal;
	}

	@Override
	public String toString() {
		return expr + "{" + child + ","+jumpPoint+"}";
	}
}
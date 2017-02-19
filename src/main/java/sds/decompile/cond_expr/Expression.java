package sds.decompile.cond_expr;

import java.util.Arrays;
import java.util.StringJoiner;
import sds.assemble.controlflow.CFEdge;
import sds.assemble.controlflow.CFNode;
import sds.classfile.bytecode.BranchOpcode;
import sds.classfile.bytecode.OpcodeInfo;

import static java.util.Objects.isNull;
import static sds.assemble.controlflow.CFEdgeType.FalseBranch;
import static sds.assemble.controlflow.CFEdgeType.TrueBranch;
import static sds.util.Printer.println;
/**
 * This class is for conditional expression.
 * @author inagaki
 */
public class Expression {
	String[] exprs;
	String logical;
	int jumpPoint;
	Expression trueExpr;
	Expression falseExpr;
	Child child;
	private int[] range;
	private int[] ownDest;

	public enum Child {
		TRUE, FALSE, OWN;
	}

	public Expression(int number, String expr, CFNode node) {
		this.range = new int[]{ -1, 0 };
		setRange(number, node);
		this.exprs = new String[]{expr};
		setChild(node);
	}

	private void setRange(int number, CFNode node) {
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
	}

	private boolean isIf(OpcodeInfo opcode) {
		return (opcode instanceof BranchOpcode) && ((BranchOpcode)opcode).isIf();
	}

	private void setChild(CFNode node) {
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
		if(isNull(child)) {
			this.child = Child.OWN;
			this.ownDest = new int[]{ jumpPoint };
		}
	}

	/**
	 * combines specified expression and this.<br>
	 * @param newExpr conditional expression
	 * @param logical logical operator
	 */
	public void combine(Expression newExpr, String logical) {
		println("\t" + toString());

		this.exprs = new String[]{ getExpr(), newExpr.getExpr() };
		this.logical = logical;	
		this.trueExpr = newExpr.trueExpr;
		this.falseExpr = newExpr.falseExpr;
		this.child = newExpr.child;
		if(child == Child.OWN) {
			int[] replaced = new int[ownDest.length + 1];
			System.arraycopy(ownDest, 0, replaced, 0, ownDest.length);
			replaced[replaced.length - 1] = newExpr.jumpPoint;
			this.ownDest = replaced;
		}
		range[1] = newExpr.range[1];
		this.jumpPoint = newExpr.jumpPoint;

		println("\t" + toString());
	}

	/**
	 * returns this conditional expression.
	 * @return conditional expression
	 */
	public String getExpr() {
		if(exprs.length < 2) {
			return exprs[0];
		}
		String result = exprs[0] + logical + exprs[1];
		return logical.contains("&&") ? result : "(" + result + ")";
	}

	/**
	 * returns other jump points to own.<br>
	 * this method is able to call only this expression child is OWN.
	 * @return jump points
	 */
	public int[] getOwnDest() {
		if(child != Child.OWN) {
			throw new IllegalStateException(child + " type expression must not call this method.");
		}
		return ownDest;
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
		for(int i = 0; i < exprs.length; i++) {
			String expr = exprs[i];
			if(expr.contains("||")) {
				exprs[i] = changeOperator(expr, " || ");
			} else if(expr.contains("&&")) {
				exprs[i] = changeOperator(expr, " && ");
			} else {
				exprs[i] = changeOperator(expr);
			}
		}
	}

	private String changeOperator(String target, String separator) {
		StringJoiner sj = new StringJoiner(separator);
		for(String boolExpr : target.split(separator.replace("|", "\\|"))) {
			if(boolExpr.contains("||")) {
				sj.add(changeOperator(boolExpr, " && "));
			} else if(boolExpr.contains("&&")) {
				sj.add(changeOperator(boolExpr, " || "));
			} else {
				sj.add(changeOperator(boolExpr));
			}
		}
		return sj.toString();
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

	@Override
	public boolean equals(Object obj) {
		if(isNull(obj) || (! (obj instanceof Expression))) {
			return false;
		}
		Expression target = (Expression)obj;
		boolean equal = true;
		equal &= Arrays.equals(exprs, target.exprs);
		equal &= Arrays.equals(range, target.range);
		equal &= (jumpPoint == target.jumpPoint);
		equal &= (child     == target.child);
		return equal;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(getExpr());
		return sb.append("{").append(child).append(",").append(jumpPoint)
				 .append(",[").append(range[0]).append("-").append(range[1]).append("]}")
				.toString();
	}
}
package sds.decompile;

import java.util.ArrayList;
import java.util.List;
import sds.assemble.controlflow.CFEdge;
import sds.assemble.controlflow.CFNode;
import sds.classfile.bytecode.BranchOpcode;
import sds.classfile.bytecode.OpcodeInfo;
import sds.classfile.bytecode.MnemonicTable;

import static sds.assemble.controlflow.CFNodeType.Entry;
import static sds.assemble.controlflow.CFNodeType.LoopEntry;
import static sds.assemble.controlflow.CFEdgeType.FalseBranch;
import static sds.assemble.controlflow.CFEdgeType.TrueBranch;
import static sds.assemble.controlflow.NodeTypeChecker.check;

/**
 * This builder class is for conditional expression.
 * @author inagaki
 */
public class ConditionalExprBuilder {
	private List<String> exprs;
	private CFNode node;
	private StringBuilder result;

	public ConditionalExprBuilder(CFNode node) {
		this.node = node;
		this.exprs = new ArrayList<>();
		this.result = new StringBuilder("");
		if(check(node, Entry)) {
			result.append("if(");
			return;
		}
		result.append("while(");
	}

	/**
	 * adds part of expression.
	 * @param expr expression
	 * @param type expression type
	 * @param cmpOperator comparing operator
	 */
	public void addExpr(String expr, String type, String cmpOperator) {
		if(expr.contains("OPERATOR") || type.equals("boolean")) {
			addExpr(expr.replace("OPERATOR", cmpOperator));
			return;
		}
		// in case of comparing int value and 0, no opcode push 0 onto stack.
		// so, it is necessary to append comparing operator and 0 to popped element.
		addExpr("(" + expr + cmpOperator + "0)");
	}

	/**
	 * adds part of expression.
	 * @param expr expression
	 */
	public void addExpr(String expr) {
		exprs.add(expr);
	}

	/**
	 * returns built conditional expression.
	 * @return conditional expression
	 */
	public String build() {
		// processing
		addLogicalOperator();
		result.append(") ");
		return check(node, Entry, LoopEntry) ? result.append("{").toString() : result.toString();
	}

	private void addLogicalOperator() {
		if(exprs.size() < 2) {
			return;
		}
		BranchOpcode[] branches = getBranches();
		String[] logics = new String[exprs.size() - 1];
		StringBuilder ifLine = new StringBuilder();
		for(int i = 0; i < branches.length; i++) {
			if(i < branches.length - 1) {
				int jump = branches[i].getBranch();
				for(CFEdge edge : node.getChildren()) {
					if((edge.getType() == TrueBranch) && edge.getDest().isInPcRange(jump)) {
						ifLine.append(changeToNegative(branches[i].getOpcodeType(), exprs.get(i)));
					} else if((edge.getType() == FalseBranch) && edge.getDest().isInPcRange(jump)) {
						logics[i] = "&&";
					}
				}
			} else {
				result.append(exprs.get(i));
			}
		}
	}

	private BranchOpcode[] getBranches() {
		int index = 0;
		BranchOpcode[] branches = new BranchOpcode[node.getJumpPoints().length];
		for(OpcodeInfo opcode : node.getOpcodes().getAll()) {
			if((opcode instanceof BranchOpcode)) {
				BranchOpcode branch = (BranchOpcode)opcode;
				if(branch.isIf()) {
					branches[index] = branch;
					index++;
				}
			}
		}
		return branches;
	}

	private String changeToNegative(MnemonicTable type, String condition) {
		switch(type) {
			case if_icmpeq:
			case if_acmpeq:
			case ifnull:
			case ifeq:
				if(condition.contains("==")) {
					return condition.replace("==", "!=");
				}
				return "(! " + condition + ")";
			case if_icmpne:
			case if_acmpne:
			case ifnonnull:
			case ifne:      return condition.replace("!=", "==");
			case if_icmplt:
			case iflt:      return condition.replace("<" , ">=");
			case if_icmpgt:
			case ifgt:      return condition.replace(">" , "<=");
			case if_icmple:
			case ifle:      return condition.replace("<=", ">");
			case if_icmpge:
			case ifge:      return condition.replace(">=", "<");
			default:
				throw new IllegalArgumentException(type + " opcode is not if_xx opcode.");
		}
	}
}
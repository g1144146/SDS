package sds.decompile.cond_expr;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import sds.assemble.controlflow.CFNode;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static sds.assemble.controlflow.CFNodeType.Entry;
import static sds.assemble.controlflow.CFNodeType.OneLineEntry;
import static sds.assemble.controlflow.CFNodeType.LoopEntry;
import static sds.assemble.controlflow.NodeTypeChecker.check;
import static sds.decompile.cond_expr.Expression.Child.TRUE;
import static sds.decompile.cond_expr.Expression.Child.FALSE;
import static sds.decompile.cond_expr.Expression.Child.OWN;
import static sds.util.Printer.println;

/**
 * This builder class is for conditional expression of "if" and "while" statement.
 * @author inagaki
 */
public class ConditionalExprBuilder {
	private List<Expression> exprs;
	private CFNode node;
	private StringBuilder result;

	public ConditionalExprBuilder(CFNode node) {
		this.node = node;
		this.exprs = new ArrayList<>();
		this.result = new StringBuilder("");
		if(check(node, Entry, OneLineEntry)) {
			result.append("if(");
			return;
		}
		result.append("while(");
	}

	/**
	 * append part of expression.
	 * @param expr expression
	 * @param type expression type
	 * @param cmpOperator comparing operator
	 */
	public void append(String expr, String type, String cmpOperator) {
		if(type.equals("boolean")) {
			if(expr.contains("OPERATOR")) {
				append(expr.replace("OPERATOR", cmpOperator));
			} else {
				// for example, "str.equals(another)"
				// in this case, restores reversed expression.
				if(cmpOperator.equals(" == ")) {
					append("(! " + expr + ")");
				} else {
					// cmpOperator is " != ".
					append(expr);
				}
			}
			return;
		}
		// in case of comparing int value and 0, no opcode push 0 onto stack.
		// so, it is necessary to append comparing operator and 0 to popped element.
		append("(" + expr + cmpOperator + "0)");
	}

	/**
	 * appends part of expression.
	 * @param expr expression
	 */
	public void append(String expr) {
		exprs.add(new Expression(exprs.size(), expr, node));
	}

	/**
	 * returns built conditional expression.
	 * @return conditional expression
	 */
	public String build() {
		// setting true and false expression of that.
		setExprBranch();
		println("[initialized]: " + exprs);

		int len = exprs.size();
		combineExpr(TRUE, " || ");
		output(len, "combine TRUE");
		
		len = exprs.size();
		combineExpr(OWN, "");
		output(len, "combine OWN");

		// compine expression in case of child type of the expression is OWN. 
		len = exprs.size();
		combineExpr();
		output(len, "combine BOTH");

		len = exprs.size();
		combineExpr(FALSE, " && ");
		output(len, "FALSE");

		len = exprs.size();
		combineCommonFactor();
		output(len, "combine Last");

		addLogicalOperator();
		result.append(") ");
		println("*** " + result + " ***");
		return check(node, Entry, LoopEntry) ? result.append("{").toString() : result.toString();
	}

	private void output(int len, String phase) {
		if(len != exprs.size()) println("[after " + phase + "]: " + exprs);
	}

	private void setExprBranch() {
		for(int i = 0; i < exprs.size() - 1; i++) {
			exprs.get(i).trueExpr = exprs.get(i + 1);
			int jump = exprs.get(i).jumpPoint;
			for(int j = i; j < exprs.size(); j++) {
				if(exprs.get(j).isDestination(jump)) {
					exprs.get(i).falseExpr = exprs.get(j);
				}
			}
		}
	}

	/**
	 * combines each expression in case of expressions have same destination.
	 * for example,
	 * in case of there are expr_a and expr_b,
	 * when child type of the exprs is FALSE, the exprs are combinable with OR.
	 * on the other hand, when the type is TRUE, the exprs are combinable with AND.
	 */
	private void combineExpr(Expression.Child type, String logical) {
		if(exprs.size() < 2) {
			return;
		}
		Set<Expression> removeSet = new HashSet<>();
		if(type == OWN) {
			int index = 0;
			for(Expression ex : exprs) {
				Expression next = ex.trueExpr;
				if(nonNull(next) &&(ex.jumpPoint == next.jumpPoint) && (next.child == type) && (0 < index)) {
					Expression.Child beforeChild = exprs.get(index - 1).child;
					Expression.Child nextTrue = next.trueExpr.child;
					if((beforeChild == OWN) && nonNull(nextTrue) && (nextTrue == OWN)) {
						removeSet.addAll(combineExpr(index - 1, index, new HashSet<>()));
						break;
					}
				}
				index++;
			}
			updateExprs(removeSet);
			removeSet.clear();
			println("[in the middle]: " + exprs);
		}
		// combining expressions
		// if type == TRUE, with 'OR'.
		// if type == FALSE, with 'AND'.
		for(Expression ex : exprs) {
			if(removeSet.contains(ex) || ex.child != type) {
				continue;
			}
			Expression next = ex.trueExpr;
			while(nonNull(next)) {
				if((ex.jumpPoint == next.jumpPoint) && (next.child == type)) {
					if(type == OWN) {
						if((searchDestinationChild(next.falseExpr) == TRUE)) {
							ex.combine(next, " || ");
						} else {
							ex.combine(next, " && ");
						}
					} else {
						ex.combine(next, logical);
					}
					removeSet.add(next);
					next = next.trueExpr;
					continue;
				}
				break;
			}
		}
		updateExprs(removeSet);
	}


	/**
	 * this method for child type of expression is OWN.
	 * for example,
	 * there are expr_a, expr_b and expr_c, and these expressions is next:
	 *  - expr_a: child type of this expression is OWN.
	 *  - expr_b: this expression is destination in case of expr_a is FALSE.
	 *  - expr_c: this expression is destination in case of expr_a is TRUE.
	 * then, expr_b and destination expression in case of expr_c are equal,
	 * expr_a and expr_c is combinable.
	 * when child type of expr_c is TRUE, the expressions combine with AND.
	 * on the other hand, when the type is FALSE, the expressions combine with OR.
	 */
	private void combineExpr() {
		updateExprs(combineExpr(0, exprs.size(), new HashSet<>()));
	}

	private Set<Expression> combineExpr(int from, int until, Set<Expression> removeSet) {
		if((exprs.size() < 2) || (from == until)) {
			return removeSet;
		}
		Expression ex = exprs.get(from);
		if((! removeSet.contains(ex)) && (ex.child == OWN) && (ex.falseExpr != null)) {
			Expression destination = ex.falseExpr;
			Expression next = ex.trueExpr;
			int index = from + 1;
			while(nonNull(destination)) {
				if(destination.equals(next.trueExpr)) {
					if(searchDestinationChild(next) == TRUE) {
						ex.reverse();
						ex.combine(next, " && ");
					} else {
						if(next.child == OWN) {
							next.reverse();
						}
						ex.combine(next, " || ");
					}
					removeSet.add(next);
				}
				destination = next.falseExpr;
				next = next.trueExpr;
				index++;
				if(isNull(next) || (next.child != OWN) || (index >= until)) {
					break;
				}
			}
		}
		return combineExpr(from + 1, until, removeSet);
	}

	private Expression.Child searchDestinationChild(Expression expr) {
		while(expr.child == OWN) {
			expr = expr.falseExpr;
		}
		return expr.child;
	}

	/**
	 * for example, it defines opcode sequence to next:<br>
	 *   1. if_a N<br>
	 *   2. if_b X<br>
	 *   3. if_c M<br>
	 *   4. if_d X<br>
	 * in this case, if_a and if_b are combinable.<br>
	 * in the same way, if_c and if_d is too.<br>
	 * if destination of N or M is TRUE, each if_x combines with 'OR'.
	 * otherwise, if the destination is FALSE, it combines with 'AND'.
	 */
	private void combineCommonFactor() {
		if(exprs.size() < 2) {
			return;
		}
		int[] counter = new int[exprs.get(exprs.size() - 1).jumpPoint];
		for(Expression ex : exprs) {
			if(ex.child == FALSE) {
				counter[ex.jumpPoint - 1]++;
			}
		}
		Set<Expression> removeSet = new HashSet<>();
		for(Expression ex : exprs) {
			Expression next = ex.trueExpr;
			if(removeSet.contains(ex) || isNull(next) || next.child != FALSE) {
				continue;
			}
			if((ex.jumpPoint != next.jumpPoint) && (counter[next.jumpPoint - 1] > 1)) {
				if(ex.child == TRUE) {
					ex.combine(next, " || ");
					removeSet.add(next);
				} else if(ex.child == FALSE) {
					ex.combine(next, " && ");
					removeSet.add(next);
				}
			}
		}
		updateExprs(removeSet);
	}

	/**
	 * makes new List except for elements that removeSet has.
	 */
	private void updateExprs(Set<Expression> removeSet) {
		if(removeSet.isEmpty()) {
			return;
		}
		List<Expression> newList = new ArrayList<>();
		for(Expression ex : exprs) {
			if(! removeSet.contains(ex)) {
				newList.add(ex);
			}
		}
		this.exprs = newList;
	}

	private void addLogicalOperator() {
		if(exprs.size() < 2) {
			result.append(exprs.get(0).getExpr());
			return;
		}
		String[] logicals = new String[exprs.size() - 1];
		int index = 0;
		for(Expression ex : exprs) {
			if(index >= logicals.length) {
				break;
			}
			if(ex.child == TRUE) {
				logicals[index] = isNull(logicals[index]) ? " || " : logicals[index];
			} else if(ex.child == FALSE) {
				logicals[index] = isNull(logicals[index]) ? " && " : logicals[index];
			} else { // ex.child == OWN
				Expression next = ex.trueExpr;
				int logicIndex = index;
				while(nonNull(next) && (logicIndex < logicals.length)) {
					for(int jump : ex.getOwnDest()) {
						if(next.isDestination(jump)) {
							logicals[logicIndex] = (exprs.get(logicIndex).child == TRUE) ? " || " : " && ";
							break;
						}
					}
					logicIndex++;
					next = next.trueExpr;
				}
			}
			index++;
		}
		for(int i = 0; i < logicals.length; i++) {
			result.append(exprs.get(i).getExpr()).append(logicals[i]);
		}
		result.append(exprs.get(exprs.size() - 1).getExpr());
	}
}
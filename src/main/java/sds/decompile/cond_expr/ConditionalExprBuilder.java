package sds.decompile.cond_expr;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import sds.assemble.controlflow.CFNode;

import static sds.assemble.controlflow.CFNodeType.Entry;
import static sds.assemble.controlflow.CFNodeType.OneLineEntry;
import static sds.assemble.controlflow.CFNodeType.LoopEntry;
import static sds.assemble.controlflow.NodeTypeChecker.check;
import static sds.decompile.cond_expr.Expression.Child.TRUE;
import static sds.decompile.cond_expr.Expression.Child.FALSE;
import static sds.decompile.cond_expr.Expression.Child.OWN;

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
		// processing
		setExprBranch();
		// compine expression which same destination has with OR.
		combineExpr(TRUE, " || ");
		// compine expression in case of child type of the expression is OWN. 
		combineExpr();
		// compine expression which same destination has with AND.
		combineExpr(FALSE, " && ");

		addLogicalOperator();
		result.append(") ");
		return check(node, Entry, LoopEntry) ? result.append("{").toString() : result.toString();
	}

	private void setExprBranch() {
		for(int i = 0; i < exprs.size() - 1; i++) {
			int jump = exprs.get(i).jumpPoint;
			for(int j = i + 1; j < exprs.size(); j++) {
				if(exprs.get(j).isDestination(jump)) {
					exprs.get(i).falseExpr = exprs.get(j);
				}
			}
			if(i < exprs.size() - 1) {
				exprs.get(i).trueExpr = exprs.get(i + 1);
			}
		}
	}

	/**
	 * combines expression in case of combinable condition expressions exist.
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
		// combining expressions
		// if type == TRUE, with 'OR'.
		// if type == FALSE, with 'AND'.
		int index = 0;
		for(Expression ex : exprs) {
			if(removeSet.contains(ex) || ex.child != type) {
				continue;
			}
			Expression next = ex.trueExpr;
			while(next != null) {
				if((ex.jumpPoint == next.jumpPoint) && (next.child == type)) {
					ex.combine(next, logical);
					removeSet.add(next);
					next = next.trueExpr;
					continue;
				}
				break;
			}
			index++;
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
		if(exprs.size() < 2) {
			return;
		}
		Set<Expression> removeSet = new HashSet<>();
		for(Expression ex : exprs) {
			if(removeSet.contains(ex) || ex.child != OWN) {
				continue;
			}
			Expression destination = ex.falseExpr;
			Expression next = ex.trueExpr;
			if(destination == null || next == null) {
				continue;
			}
			if(destination.equals(next.trueExpr)) {
				if(next.child == TRUE) {
					ex.reverse();
					ex.combine(next, " && ");
				} else {
					ex.combine(next, " || ");
				}
				removeSet.add(next);
			}
		}
		updateExprs(removeSet);
	}

	/**
	 * makes new List except for elements that removeSet has.
	 */
	private void updateExprs(Set<Expression> removeSet) {
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
			result.append(exprs.get(0).expr);
			return;
		}
		String[] logicals = new String[exprs.size() - 1];
		int index = 0;
		for(Expression ex : exprs) {
			if(index >= logicals.length) {
				break;
			}
			if(ex.child == TRUE) {
				logicals[index] = (logicals[index] == null) ? " || " : logicals[index];
			} else if(ex.child == FALSE) {
				logicals[index] = (logicals[index] == null) ? " && " : logicals[index];
			} else { // ex.child == OWN
				for(int i = index; i < exprs.size(); i++) {
					Expression next = exprs.get(i);
					// the front of destination of this expression is OR operator.
					if(next.isDestination(ex.jumpPoint) && (! next.equals(ex))) {
						logicals[i] = " || ";
						break;
					}
				}
			}
			index++;
		}
		for(int i = 0; i < logicals.length; i++) {
			result.append(exprs.get(i).expr).append(logicals[i]);
		}
		result.append(exprs.get(exprs.size() - 1).expr);
	}
}
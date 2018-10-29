package sds.decompile.cond_expr;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import sds.assemble.controlflow.CFNode;

import static sds.decompile.cond_expr.Expression.Child.TRUE;
import static sds.decompile.cond_expr.Expression.Child.FALSE;
import static sds.decompile.cond_expr.Expression.Child.OWN;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static sds.util.Printer.println;

/**
 * This builder class is for conditional expression of "if" and "while" statement.
 * @author inagaki
 */
public class ConditionalExprBuilder {
    private List<Expression> exprs;
    private StringBuilder result;

    public ConditionalExprBuilder() {
        this.exprs = new ArrayList<>();
        this.result = new StringBuilder();
    }

    /**
     * append part of expression.
     * @param expr expression
     * @param type expression type
     * @param cmpOperator comparing operator
     * @param node node has expression
     */
    public void append(String expr, String type, String cmpOperator, CFNode node) {
        if(type.equals("boolean")) {
            if(expr.contains("OPERATOR")) {
                append(expr.replace("OPERATOR", cmpOperator), node);
            } else {
                // for example, "str.equals(another)"
                // in this case, restores reversed expression.
                if(cmpOperator.equals(" == ")) {
                    append("(! " + expr + ")", node);
                } else {
                    // cmpOperator is " != ".
                    append("(" + expr + ")", node);
                }
            }
            return;
        }
        // in case of comparing int value and 0, no opcode push 0 onto stack.
        // so, it is necessary to append comparing operator and 0 to popped element.
        append("(" + expr + cmpOperator + "0)", node);
    }

    /**
     * appends part of expression.
     * @param expr expression
     * @param node node has expression
     */
    public void append(String expr, CFNode node) {
        exprs.add(new Expression(exprs.size(), expr, node));
    }

    /**
     * returns built conditional expression.
     * @return conditional expression
     */
    public String build() {
        result.append("(");
        
        // setting true and false expression of that.
        setExprBranch();
//        println("[initialized]: " + exprs);

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
        result.append(")");
//        println("*** " + result + " ***");

        return result.toString();
    }

    private void output(int len, String phase) {
//        if(len != exprs.size()) println("[after " + phase + "]: " + exprs);
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
        if(type.is(OWN)) {
            int len = exprs.size();
            int index = 0;
            for(Expression ex : exprs) {
                Expression next = ex.trueExpr;
                if(nonNull(next) && ex.equalsDest(next) && next.equalsChild(type) && (0 < index)) {
                    Expression.Child beforeChild = exprs.get(index - 1).child;
                    Expression.Child nextTrue = next.trueExpr.child;
                    if(beforeChild.is(OWN) && nonNull(nextTrue) && nextTrue.is(OWN)) {
                        removeSet.addAll(combineExpr(index - 1, index, new HashSet<>()));
                        break;
                    }
                }
                index++;
            }
            updateExprs(removeSet);
            removeSet.clear();
            output(len, "in the middle");
        }
        // combining expressions
        // if type == TRUE, with 'OR'.
        // if type == FALSE, with 'AND'.
        for(Expression ex : exprs) {
            if(removeSet.contains(ex) || type.isNot(ex.child)) {
                continue;
            }
            Expression next = ex.trueExpr;
            while(nonNull(next)) {
                if(ex.equalsDest(next) && type.is(next.child)) {
                    if(type.is(OWN)) {
                        if(searchDestinationChild(next.falseExpr).is(TRUE)) {
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
        if((! removeSet.contains(ex)) && ex.equalsChild(OWN) && nonNull(ex.falseExpr)) {
            Expression destination = ex.falseExpr;
            Expression next = ex.trueExpr;
            int index = from + 1;
            while(nonNull(destination)) {
                if(destination.equals(next.trueExpr)) {
                    if(searchDestinationChild(next).is(TRUE)) {
                        ex.reverse();
                        ex.combine(next, " && ");
                    } else {
                        if(next.equalsChild(OWN)) {
                            next.reverse();
                        }
                        ex.combine(next, " || ");
                    }
                    removeSet.add(next);
                }
                destination = next.falseExpr;
                next = next.trueExpr;
                index++;
                if(isNull(next) || next.child.isNot(OWN) || (index >= until)) {
                    break;
                }
            }
        }
        return combineExpr(from + 1, until, removeSet);
    }

    private Expression.Child searchDestinationChild(Expression expr) {
        while(expr.equalsChild(OWN)) {
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
        int max = 0;
        for(Expression ex : exprs) {
            if(max < ex.jumpPoint) {
                max = ex.jumpPoint;
            }
        }
        int[] counter = new int[max];
        for(Expression ex : exprs) {
            if(ex.equalsChild(FALSE)) {
                counter[ex.jumpPoint - 1]++;
            }
        }
        Set<Expression> removeSet = new HashSet<>();
        for(Expression ex : exprs) {
            Expression next = ex.trueExpr;
            if(removeSet.contains(ex) || isNull(next) || next.child.isNot(FALSE)) {
                continue;
            }
            if((! ex.equalsDest(next)) && (counter[next.jumpPoint - 1] > 1)) {
                if(ex.equalsChild(TRUE)) {
                    ex.combine(next, " || ");
                    removeSet.add(next);
                } else if(ex.equalsChild(FALSE)) {
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
            if(ex.equalsChild(TRUE)) {
                logicals[index] = isNull(logicals[index]) ? " || " : logicals[index];
            } else if(ex.equalsChild(FALSE)) {
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
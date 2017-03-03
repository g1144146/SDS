package sds.decompile;

import sds.assemble.controlflow.CFNode;
import sds.decompile.cond_expr.ConditionalExprBuilder;

/**
 * This builder class is for loop statement ("for" and "while").
 * @author inagaki
 */
public class LoopStatementBuilder {
	private final String declaringPattern = ".+ .+ = .+";
	private final String arrayDeclaringPattern = ".+\\[\\] .+ = .+";
	private final String comparingPattern = "\\(.+ (<|>|<=|>=|==|!=) .+\\)";
	private final String hasNextPattern = ".+\\.hasNext\\(\\)";
	private String declared;
	private ConditionalExprBuilder builder;
	private Type type;

	private enum Type {
		WHILE, FOR, FOR_EACH_ARRAY;
	}

	public LoopStatementBuilder() {
		this.builder = new ConditionalExprBuilder();
	}

	/**
	 * return wheter loop type is FOR_EACH_ARRAY.
	 * @return if the type is FOR_EACH_ARRAY, this method returns true.<br>
	 * otherwise, it returns false.
	 */
	public boolean isForEachArray() {
		return type == Type.FOR_EACH_ARRAY;
	}

	/**
	 * adds comparing context.
	 * @param context comparing context.
	 * @param node node has the context
	 */
	public void accept(String expr, String type, String cmpOperator, CFNode node) {
		builder.append(expr, type, cmpOperator, node);
	}

	/**
	 * adds declaring.
	 * @param context declaring(0)
	 * @param node node has the context
	 */
	public void accept(String[] context, CFNode node) {
		accept(context[0], node);
	}

	/**
	 * adds declaring, storing or comparing context.
	 * @param context declaring, storing or comparing context
	 * @param node node has the context
	 */
	public void accept(String context, CFNode node) {
		if(type == null) {
			if(context.matches(arrayDeclaringPattern)) {
				this.type = Type.FOR_EACH_ARRAY;
				String[] sepDeclare = context.split(" ");
				this.declared = sepDeclare[0].replace("[]", "") + " #VAL# : " + sepDeclare[sepDeclare.length - 1];
			} else if(context.matches(declaringPattern)) {
				this.type = Type.FOR;
				declared = context;
			} else {
				this.type = Type.WHILE;
			}
		} else {
			if(context.matches(comparingPattern + "|" + hasNextPattern)) {
				builder.append(context, node);
			}
		}
	}

	/**
	 * returns built loop statement.
	 * @return loop statement.
	 */
	public String build() {
		StringBuilder sb = new StringBuilder();
		if(type == Type.WHILE) {
			sb.append("while").append(builder.build()).append(" {");
		} else {
			sb.append("for(");
			if(type == Type.FOR) {
				sb.append(declared).append("; ").append(builder.build()).append(";) {");
			} else if(type == Type.FOR_EACH_ARRAY) {
				sb.append(declared).append(") {");
			}
		}
		return sb.toString();
	}

	/**
	 * returns built loop statement for for-each.
	 * @param forEachDeclare declaring for for-each
	 * @return loop statement
	 */
	public String build(String forEachDeclare) {
		if(forEachDeclare.matches(".+ .+ = .+\\.next\\(\\).*")) {
			String[] sep = forEachDeclare.split(" ");
			String left =  sep[0] + " " + sep[1];

			String[] separated = declared.split("java\\.util\\.Iterator .+ = ");
			String right = separated[1];
			right = right.substring(0, right.length() - ".iterator()".length());

			return "for(" + left + " : " + right + ") {";
		}
		return build();
	}
}
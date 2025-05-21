// Generated from c:/Users/carlo/Documents/AntlrProjects/dalia-grammatic/src/main/antlr4/com/nuestrolenguaje/manbel.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link manbelParser}.
 */
public interface manbelListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link manbelParser#programa}.
	 * @param ctx the parse tree
	 */
	void enterPrograma(manbelParser.ProgramaContext ctx);
	/**
	 * Exit a parse tree produced by {@link manbelParser#programa}.
	 * @param ctx the parse tree
	 */
	void exitPrograma(manbelParser.ProgramaContext ctx);
	/**
	 * Enter a parse tree produced by {@link manbelParser#instruccion}.
	 * @param ctx the parse tree
	 */
	void enterInstruccion(manbelParser.InstruccionContext ctx);
	/**
	 * Exit a parse tree produced by {@link manbelParser#instruccion}.
	 * @param ctx the parse tree
	 */
	void exitInstruccion(manbelParser.InstruccionContext ctx);
	/**
	 * Enter a parse tree produced by {@link manbelParser#asig}.
	 * @param ctx the parse tree
	 */
	void enterAsig(manbelParser.AsigContext ctx);
	/**
	 * Exit a parse tree produced by {@link manbelParser#asig}.
	 * @param ctx the parse tree
	 */
	void exitAsig(manbelParser.AsigContext ctx);
	/**
	 * Enter a parse tree produced by {@link manbelParser#declaracionVariable}.
	 * @param ctx the parse tree
	 */
	void enterDeclaracionVariable(manbelParser.DeclaracionVariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link manbelParser#declaracionVariable}.
	 * @param ctx the parse tree
	 */
	void exitDeclaracionVariable(manbelParser.DeclaracionVariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link manbelParser#tipo}.
	 * @param ctx the parse tree
	 */
	void enterTipo(manbelParser.TipoContext ctx);
	/**
	 * Exit a parse tree produced by {@link manbelParser#tipo}.
	 * @param ctx the parse tree
	 */
	void exitTipo(manbelParser.TipoContext ctx);
	/**
	 * Enter a parse tree produced by {@link manbelParser#def}.
	 * @param ctx the parse tree
	 */
	void enterDef(manbelParser.DefContext ctx);
	/**
	 * Exit a parse tree produced by {@link manbelParser#def}.
	 * @param ctx the parse tree
	 */
	void exitDef(manbelParser.DefContext ctx);
	/**
	 * Enter a parse tree produced by {@link manbelParser#condicional}.
	 * @param ctx the parse tree
	 */
	void enterCondicional(manbelParser.CondicionalContext ctx);
	/**
	 * Exit a parse tree produced by {@link manbelParser#condicional}.
	 * @param ctx the parse tree
	 */
	void exitCondicional(manbelParser.CondicionalContext ctx);
	/**
	 * Enter a parse tree produced by {@link manbelParser#ciclo}.
	 * @param ctx the parse tree
	 */
	void enterCiclo(manbelParser.CicloContext ctx);
	/**
	 * Exit a parse tree produced by {@link manbelParser#ciclo}.
	 * @param ctx the parse tree
	 */
	void exitCiclo(manbelParser.CicloContext ctx);
	/**
	 * Enter a parse tree produced by {@link manbelParser#bloque}.
	 * @param ctx the parse tree
	 */
	void enterBloque(manbelParser.BloqueContext ctx);
	/**
	 * Exit a parse tree produced by {@link manbelParser#bloque}.
	 * @param ctx the parse tree
	 */
	void exitBloque(manbelParser.BloqueContext ctx);
	/**
	 * Enter a parse tree produced by {@link manbelParser#print}.
	 * @param ctx the parse tree
	 */
	void enterPrint(manbelParser.PrintContext ctx);
	/**
	 * Exit a parse tree produced by {@link manbelParser#print}.
	 * @param ctx the parse tree
	 */
	void exitPrint(manbelParser.PrintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Numero}
	 * labeled alternative in {@link manbelParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterNumero(manbelParser.NumeroContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Numero}
	 * labeled alternative in {@link manbelParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitNumero(manbelParser.NumeroContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BinOpComp}
	 * labeled alternative in {@link manbelParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBinOpComp(manbelParser.BinOpCompContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BinOpComp}
	 * labeled alternative in {@link manbelParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBinOpComp(manbelParser.BinOpCompContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Variable}
	 * labeled alternative in {@link manbelParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterVariable(manbelParser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Variable}
	 * labeled alternative in {@link manbelParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitVariable(manbelParser.VariableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StringLiteral}
	 * labeled alternative in {@link manbelParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterStringLiteral(manbelParser.StringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StringLiteral}
	 * labeled alternative in {@link manbelParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitStringLiteral(manbelParser.StringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Parens}
	 * labeled alternative in {@link manbelParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterParens(manbelParser.ParensContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Parens}
	 * labeled alternative in {@link manbelParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitParens(manbelParser.ParensContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BooleanLiteral}
	 * labeled alternative in {@link manbelParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBooleanLiteral(manbelParser.BooleanLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BooleanLiteral}
	 * labeled alternative in {@link manbelParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBooleanLiteral(manbelParser.BooleanLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UnaryOpNot}
	 * labeled alternative in {@link manbelParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterUnaryOpNot(manbelParser.UnaryOpNotContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UnaryOpNot}
	 * labeled alternative in {@link manbelParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitUnaryOpNot(manbelParser.UnaryOpNotContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BinOpAddSub}
	 * labeled alternative in {@link manbelParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBinOpAddSub(manbelParser.BinOpAddSubContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BinOpAddSub}
	 * labeled alternative in {@link manbelParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBinOpAddSub(manbelParser.BinOpAddSubContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BinOpLogical}
	 * labeled alternative in {@link manbelParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBinOpLogical(manbelParser.BinOpLogicalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BinOpLogical}
	 * labeled alternative in {@link manbelParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBinOpLogical(manbelParser.BinOpLogicalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BinOpMulDiv}
	 * labeled alternative in {@link manbelParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBinOpMulDiv(manbelParser.BinOpMulDivContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BinOpMulDiv}
	 * labeled alternative in {@link manbelParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBinOpMulDiv(manbelParser.BinOpMulDivContext ctx);
}
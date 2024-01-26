package semantic;

import interpreter.TipoToken;
import interpreter.Token;
import parser.*;
import java.util.List;
public class Semantic extends TablaSimbolos{
    private final List<Statement>tree;

    public Semantic(List<Statement> tree){
        this.tree=tree;
    }

    public void InitAnalizer() throws SemanticException {
        analizerStatement(tree);
    }

    private void analizerStatement(List<Statement> treeR) throws SemanticException {
        for(Statement statement:treeR){
            if(statement instanceof StmtVar){
                StmtVar stmtVar= (StmtVar) statement;
                ExprLiteral exprLiteral= (ExprLiteral) stmtVar.initializer;
                if(!existeIdentificador((String) stmtVar.name.literal)) {
                    asignar((String) stmtVar.name.literal,exprLiteral.value);
                }
                else throw new SemanticException(new StringBuilder("Variable ya declarada"));

            }
            if(statement instanceof StmtPrint){
                StmtPrint stmtPrint=(StmtPrint) statement;
                if(stmtPrint.expression instanceof ExprBinary){
                    System.out.println(ExprBinaryPrint("",(ExprBinary) stmtPrint.expression));

                }
                if(stmtPrint.expression instanceof ExprVariable){
                    String variable=((ExprVariable) stmtPrint.expression).name;
                    if(existeIdentificador(variable)) {
                        System.out.println(obtener(variable));
                    }
                    else throw new SemanticException(new StringBuilder("Variable "+ variable+" no declarada"));
                }
                if(stmtPrint.expression instanceof ExprLiteral){
                    System.out.println(""+((ExprLiteral) stmtPrint.expression).value);
                }
            }
            if(statement instanceof StmtIf){
                if(ExprLogical((ExprLogical) ((StmtIf) statement).condition)){
                    System.out.println("true");
                }
                else{
                    System.out.println("false");
                }
            }
        }
    }
    private String ExprBinaryPrint(String string, ExprBinary exprBinary) throws SemanticException {
        Token token=exprBinary.operator;
        if(token.tipo== TipoToken.PLUS){
            if(exprBinary.left instanceof ExprBinary){
                string += ExprBinaryPrint(string, (ExprBinary) exprBinary.left)+string;
            }
            else if(exprBinary.left instanceof ExprLiteral){
                string += ((ExprLiteral) exprBinary.left).value+string;
            }
            else if(exprBinary.left instanceof ExprVariable){
                String variable=((ExprVariable) exprBinary.left).name;
                if(existeIdentificador(variable)) {
                    string += obtener(variable)+string;
                }
                else throw new SemanticException(new StringBuilder("Variable "+ variable+" no declarada"));
            }

            if(exprBinary.right instanceof ExprBinary){
                string += ExprBinaryPrint(string, (ExprBinary) exprBinary.right)+string;
            }
            else if(exprBinary.right instanceof ExprLiteral){
                string += ((ExprLiteral) exprBinary.right).value;
            }
            else if(exprBinary.right instanceof ExprVariable){
                String variable=((ExprVariable) exprBinary.right).name;
                if(existeIdentificador(variable)) {
                    string +=obtener(variable);
                }
                else throw new SemanticException(new StringBuilder("Variable "+ variable+" no declarada"));
            }
        }
        return string;
    }

    private boolean ExprLogical(ExprLogical exprLogical) throws SemanticException {
        Token token=exprLogical.operator;
        boolean boolI = true;
        
        if(exprLogical.left instanceof ExprBinary){
            boolI = ExprBinary(boolI, (ExprBinary) exprLogical.left);

        }
        else if(exprLogical.left instanceof ExprLiteral){
            if(!(((ExprLiteral) exprLogical.left).value==(Object) true)){
                boolI = false ;
            }
        }

        else if(exprLogical.left instanceof ExprVariable){
            String variable= ((ExprVariable) exprLogical.left).name;
            if(existeIdentificador(variable)) {
                boolI = (boolean) obtener(variable);
            }
            else throw new SemanticException(new StringBuilder("Variable "+ variable+" no declarada"));
        }
        boolean boolR = true;
        if(exprLogical.right instanceof ExprBinary){
            boolR = ExprBinary(boolR, (ExprBinary) exprLogical.right);

        }
        else if(exprLogical.right instanceof ExprLiteral){
            if(!(((ExprLiteral) exprLogical.right).value==(Object) true)){
                boolR = false ;
            }
        }

        else if(exprLogical.right instanceof ExprVariable){
            String variable= ((ExprVariable) exprLogical.right).name;
            if(existeIdentificador(variable)) {
                boolR = (boolean) obtener(variable);
            }
            else throw new SemanticException(new StringBuilder("Variable "+ variable+" no declarada"));
        }
        if(token.tipo==TipoToken.AND){
            return boolI && boolR;
        }
        else if(token.tipo==TipoToken.OR){
            return boolI || boolR;
        }
        else{
            throw new SemanticException(new StringBuilder("Formatos incorrectos"));
        }

    }

    private boolean ExprBinary(boolean bool, ExprBinary exprBinary) throws SemanticException {
        /*Token token=exprBinary.operator;
        if(token.tipo== TipoToken.PLUS){
            if(exprBinary.left instanceof ExprBinary){
                string += ExprBinary(bool, (ExprBinary) exprBinary.left)+string;
            }
            else if(exprBinary.left instanceof ExprLiteral){
                string += ((ExprLiteral) exprBinary.left).value+string;
            }
            else if(exprBinary.left instanceof ExprVariable){
                String variable=((ExprVariable) exprBinary.left).name;
                if(existeIdentificador(variable)) {
                    string += obtener(variable)+string;
                }
                else throw new SemanticException(new StringBuilder("Variable "+ variable+" no declarada"));
            }
////////////////////////
            if(exprBinary.right instanceof ExprBinary){
                string += ExprBinaryPrint(string, (ExprBinary) exprBinary.right)+string;
            }
            else if(exprBinary.right instanceof ExprLiteral){
                string += ((ExprLiteral) exprBinary.right).value;
            }
            else if(exprBinary.right instanceof ExprVariable){
                String variable=((ExprVariable) exprBinary.right).name;
                if(existeIdentificador(variable)) {
                    string +=obtener(variable);
                }
                else throw new SemanticException(new StringBuilder("Variable "+ variable+" no declarada"));
            }
        }*/
        return true;
    }
}

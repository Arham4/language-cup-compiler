package tokens.stmts;

import tokens.id.Name;
import tokens.lexeme.Type;
import tokens.lexeme.Types;
import type_checking.TypeCheckException;

import java.util.Map;

import static type_checking.TypeCheckException.undeclaredError;
import static utils.SymbolTableHelper.getClosestScopeType;
import static utils.SymbolTableHelper.isScopeTooHigh;

public final class IncrementStmt implements Stmt {
    public static IncrementStmt withName(Name name) {
        return new IncrementStmt(name);
    }

    private final Name name;

    private IncrementStmt(Name name) {
        this.name = name;
    }

    @Override
    public String asString(String prefix, int tabs) {
        return prefix + name.asString(tabs) + "++;";
    }

    @Override
    public Void typeCheck(int scope, Map<String, Map<Integer, Type>> variableSymbolTable, Map<String, Type> methodSymbolTable) throws TypeCheckException {
        if (!variableSymbolTable.containsKey(name.getId()) || isScopeTooHigh(scope, variableSymbolTable.get(name.getId()))) {
            throw undeclaredError(name.getId());
        }
        // todo check for final
        Type varType = getClosestScopeType(scope, variableSymbolTable.get(name.getId()));
        if (varType != Types.INTLIT && varType != Types.FLOATLIT) {
            throw TypeCheckException.withFault("Error: Only ints or floats can be incremented, but " + name.getId() + " is not of those types.");
        }
        return null;
    }
}

package com.telenav.tdk.graph.query.program.statements;

import com.telenav.tdk.graph.query.program.*;

/**
 * A select statement evaluating to a boolean value
 *
 * @author jonathanl (shibo)
 */
public class Select extends Node implements Statement
{
    /** The query expression to evaluate */
    private final BooleanExpression query;

    public Select(final BooleanExpression query)
    {
        this.query = query;
    }

    @Override
    public boolean evaluate()
    {
        if (query.evaluate())
        {
            stack().matched();
            return true;
        }
        return false;
    }

    @Override
    public String toString()
    {
        return "select " + query;
    }

    @Override
    public void visit(final Visitor visitor)
    {
        super.visit(visitor);
        query.visit(visitor);
    }
}

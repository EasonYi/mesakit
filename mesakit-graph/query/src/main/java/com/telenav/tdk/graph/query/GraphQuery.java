package com.telenav.tdk.graph.query;

import com.telenav.tdk.core.application.TdkApplication;
import com.telenav.tdk.core.filesystem.File;
import com.telenav.tdk.core.kernel.interfaces.code.Callback;
import com.telenav.tdk.core.kernel.language.collections.set.Sets;
import com.telenav.tdk.core.kernel.messaging.Message;
import com.telenav.tdk.core.kernel.operation.progress.ProgressReporter;
import com.telenav.tdk.core.kernel.operation.progress.reporters.Progress;
import com.telenav.tdk.core.kernel.scalars.counts.Maximum;
import com.telenav.tdk.graph.Route;
import com.telenav.tdk.graph.collections.EdgeSequence;
import com.telenav.tdk.graph.io.load.SmartGraphLoader;
import com.telenav.tdk.graph.project.TdkGraphCore;
import com.telenav.tdk.graph.query.compiler.*;
import org.antlr.v4.runtime.*;

import java.util.*;

/**
 * Evaluates a sequence of candidate edges against a query, return up to the given maximum number of matches.
 *
 * @author jonathanl (shibo)
 */
public class GraphQuery extends TdkApplication
{
    /**
     * Entrypoint for debugging and testing purposes only (see {@link #onRun()})
     */
    public static void main(final String[] arguments)
    {
        new GraphQuery().run(arguments);
    }

    private volatile boolean stop;

    public GraphQuery()
    {
        super(TdkGraphCore.get());
    }

    /**
     * Selects from a sequence of edges those that match the query.
     *
     * @param candidates The candidate edges to match against the query
     * @param query The query string
     * @param maximumMatches The maximum number of matching edges to return
     * @param errorHandler Callback for receiving error messages
     * @return The set of candidate edges matching the query
     */
    public Set<Route> execute(final ProgressReporter reporter, final EdgeSequence candidates, final String query, final Maximum maximumMatches, final Callback<String> errorHandler)
    {
        // Start the progress reporter,
        reporter.steps(candidates.count().asMaximum());
        reporter.start();

        // create a lexer for query,
        final var lexer = new com.telenav.tdk.graph.query.GraphQueryLexer(CharStreams.fromString(query));

        // parse the lexer's token stream,
        final var parser = new com.telenav.tdk.graph.query.GraphQueryParser(new CommonTokenStream(lexer));
        final var listener = new GraphQueryErrorListener(errorHandler);
        parser.addErrorListener(listener);
        final var queryParseTree = parser.select();

        // and if an error was reported
        if (listener.error())
        {
            // return nothing
            return Sets.empty();
        }

        // or if the parser ran out of input
        if (!parser.isMatchedEOF())
        {
            // report that
            errorHandler.callback("Parser did not match all input in query expression");
            return Sets.empty();
        }

        // or if there's a syntax error
        if (parser.getNumberOfSyntaxErrors() > 0)
        {
            // return an empty set
            errorHandler.callback("Syntax error");
            return Sets.empty();
        }

        // otherwise, create a query compiler,
        final var compiler = new GraphQueryCompiler();

        // build the query program to run,
        final var program = compiler.compile(queryParseTree, Maximum._8);

        // then go through all the candidate edges
        final var matches = new HashSet<Route>();
        for (final var candidate : candidates)
        {
            if (stop)
            {
                break;
            }

            // and if running the program on the candidate results in a match
            final var match = program.run(candidate);
            if (match != null)
            {
                // then add the route to the set of matches
                matches.add(match);

                // and if we have found enough matches already,
                if (matches.size() == maximumMatches.asInt())
                {
                    // then stop looking and return them
                    return matches;
                }
            }
            reporter.next();
        }
        reporter.end();

        return matches;
    }

    public void stop()
    {
        stop = true;
    }

    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    @Override
    protected void onRun()
    {
        final var query = "select (roadType = 'freeway' and !isRamp) then isRamp+ then (roadType = 'freeway' and !isRamp)";
        final var graphFile = new File("/Users/Shared/tdk-8-data/OSM/new-mexico/new-mexico-latest.graph");
        final var graph = new SmartGraphLoader(graphFile).load();
        final var candidates = graph.edges();
        // candidates = new EdgeSequence(List.of(graph.newEdge(new EdgeIdentifier(319565312000000L))));
        for (final var route : execute(Progress.create(this), candidates, query, Maximum._1_000, System.out::println))
        {
            Message.println("route: $", route);
        }
    }
}

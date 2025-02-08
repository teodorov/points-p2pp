import fr.ensta.points.PointsAbstractSyntaxBuilder;
import fr.ensta.points.PointsLexer;
import fr.ensta.points.PointsParser;
import fr.ensta.points.model.PointList;
import fr.ensta.points.p2pp.PointListP2PP;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.jupiter.api.Test;
import org.openflexo.p2pp.DefaultPrettyPrintContext;
import org.openflexo.p2pp.P2PPNode;
import org.openflexo.p2pp.PrettyPrintContext;
import org.openflexo.p2pp.RawSource;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestParsingAndPrinting {
    PrettyPrintContext pCtx = new DefaultPrettyPrintContext(PrettyPrintContext.Indentation.DoNotIndent);

    ParseTree antlr4tree(String syntax) {
        var lexer = new PointsLexer(CharStreams.fromString(syntax));
        var tokenStream = new CommonTokenStream(lexer);
        var parser = new PointsParser(tokenStream);
        return parser.points();
    }

    P2PPNode<PointListP2PP, PointList> parse(String syntax) throws IOException {
        var rawSource = new RawSource(new StringReader(syntax));

        var builder = new PointsAbstractSyntaxBuilder(rawSource);
        var walker = new ParseTreeWalker();
        var parseTree = antlr4tree(syntax);

        walker.walk(builder, parseTree);

        //noinspection unchecked
        return (P2PPNode<PointListP2PP, PointList>) builder.getEntry(parseTree);
    }

    @Test
    void testOnePoint() throws IOException {
        var syntax = "P [  1, 2]";
        var pp = parse(syntax);

        pp.initializePrettyPrint(pp, pCtx);
        var actual = pp.getTextualRepresentation(pCtx);
        assertEquals(syntax, actual);

        actual = pp.getNormalizedTextualRepresentation(pCtx);
        var expected = "P[1, 2]\n";
        assertEquals(expected, actual);
    }

    @Test
    void testOnePointNoSpaceBeforeY() throws IOException {
        var syntax = "P [  1,2]";
        var pp = parse(syntax);

        pp.initializePrettyPrint(pp, pCtx);
        var actual = pp.getTextualRepresentation(pCtx);
        assertEquals(syntax, actual);
    }

    @Test
    void testTwoPoints() throws IOException {
        var syntax = "P [  1, 2] P \n [ 2,     3]";
        var pp = parse(syntax);

        pp.initializePrettyPrint(pp, pCtx);
        var actual = pp.getTextualRepresentation(pCtx);
        assertEquals(syntax, actual);

        actual = pp.getNormalizedTextualRepresentation(pCtx);
        var expected = "P[1, 2]\nP[2, 3]\n";
        assertEquals(expected, actual);
    }

    @Test
    void testTwoPointsModelChange() throws IOException {
        var syntax = "P [  1, 2] P \n [ 2,     3]";
        var pp = parse(syntax);

        pp.getModelObject().points().forEach(point -> {
            point.x +=100; point.y += 100;});

        pp.initializePrettyPrint(pp, pCtx);
        var expected = "P [  101, 102] P \n [ 102,     103]";
        var actual = pp.getTextualRepresentation(pCtx);
        assertEquals(expected, actual);

        actual = pp.getNormalizedTextualRepresentation(pCtx);
        expected = "P[101, 102]\nP[102, 103]\n";
        assertEquals(expected, actual);
    }

}

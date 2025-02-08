package fr.ensta.points;

import fr.ensta.points.model.Point;
import fr.ensta.points.model.PointList;
import fr.ensta.points.p2pp.PointListP2PP;
import fr.ensta.points.p2pp.PointP2PP;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.openflexo.p2pp.DefaultPrettyPrintContext;
import org.openflexo.p2pp.P2PPNode;
import org.openflexo.p2pp.PrettyPrintContext;
import org.openflexo.p2pp.RawSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.Arrays;

public class Main {

    static ParseTree antlr4tree(URL syntaxURL) throws IOException {
        var lexer = new PointsLexer(CharStreams.fromStream(syntaxURL.openStream()));
        var tokenStream = new CommonTokenStream(lexer);
        var parser = new PointsParser(tokenStream);
        return parser.points();
    }

    static P2PPNode<PointListP2PP, PointList> parse(URL syntaxURL) throws IOException {
        var rawSource = new RawSource(new InputStreamReader(syntaxURL.openStream()));

        var builder = new PointsAbstractSyntaxBuilder(rawSource);
        var walker = new ParseTreeWalker();
        var parseTree = antlr4tree(syntaxURL);

        walker.walk(builder, parseTree);

        //noinspection unchecked
        return (P2PPNode<PointListP2PP, PointList>) builder.getEntry(parseTree);
    }

    public static void main(String[] args) throws IOException {
        URL url = Main.class.getResource("example1.points");
        assert url != null;
        var p2pp = parse(url);

        System.out.println("Reprint just after parsing...");

        var pCtx = new DefaultPrettyPrintContext(PrettyPrintContext.Indentation.DoNotIndent);
        p2pp.initializePrettyPrint(p2pp, pCtx);

        var preserving = p2pp.getTextualRepresentation(pCtx);
        var normalized = p2pp.getNormalizedTextualRepresentation(pCtx);

        System.out.println("preserving  = \n" + preserving);
        System.out.println("normalized  = \n" + normalized);

        //Modify the underlying object
        System.out.println("Object structured modified after parsing...");
        p2pp.getModelObject().points().forEach(p -> {
            p.x++;
            p.y++;
        });

        preserving = p2pp.getTextualRepresentation(pCtx);
        normalized = p2pp.getNormalizedTextualRepresentation(pCtx);

        System.out.println("preserving  = \n" + preserving);
        System.out.println("normalized  = \n" + normalized);

        //increase the number of characters
        System.out.println("Object structured more modified after parsing...");
        p2pp.getModelObject().points().forEach(p -> {
            p.x += 100;
            p.y += 1000;
        });

        preserving = p2pp.getTextualRepresentation(pCtx);
        normalized = p2pp.getNormalizedTextualRepresentation(pCtx);

        System.out.println("preserving  = \n" + preserving);
        System.out.println("normalized  = \n" + normalized);


        System.out.println("Now just print a new Point (no parsing)...");
        var p = new Point(2, 32);
        var pp = new PointP2PP(p);

        pp.initializePrettyPrint(null, pCtx);
        System.out.println("preserving  = \n" + pp.getTextualRepresentation(pCtx));
        System.out.println("normalized  = \n" + pp.getNormalizedTextualRepresentation(pCtx));

        System.out.println("Now let's print a new PointList (no parsing)...");
        var p1 = new Point(3, 33);

        var pL = new PointList(Arrays.asList(p, p1));
        var ppL = new PointListP2PP(pL);
        ppL.initializePrettyPrint(null, pCtx);
        System.out.println("preserving  = \n" + ppL.getTextualRepresentation(pCtx));
        System.out.println("normalized  = \n" + ppL.getNormalizedTextualRepresentation(pCtx));
    }
}
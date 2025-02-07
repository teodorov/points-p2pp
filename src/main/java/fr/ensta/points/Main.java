package fr.ensta.points;

import fr.ensta.points.p2pp.PointListP2PP;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.openflexo.p2pp.DefaultPrettyPrintContext;
import org.openflexo.p2pp.P2PPNode;
import org.openflexo.p2pp.PrettyPrintContext;
import org.openflexo.p2pp.RawSource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("start");
        URL url = Main.class.getResource("example1.points");

        assert url != null;
        RawSource rawSource = new RawSource(new InputStreamReader(url.openStream()));

        Lexer lexer = new PointsLexer(CharStreams.fromStream(url.openStream()));
        TokenStream tokenStream = new CommonTokenStream(lexer);
        PointsParser parser = new PointsParser(tokenStream);
        PointsParser.PointsContext antlr4tree = parser.points();

        System.out.println("result  = " + antlr4tree.getText());

        PointsSyntaxBuilder builder = new PointsSyntaxBuilder(rawSource);
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(builder, antlr4tree);

        var result = builder.getEntry(antlr4tree);

        ((PointListP2PP)result).getModelObject().points().forEach(p -> {p.x++; p.y++;} );

        var pCtx = new DefaultPrettyPrintContext(PrettyPrintContext.Indentation.DoNotIndent);
        ((PointListP2PP) result).initializePrettyPrint((P2PPNode<?, ?>) result, pCtx);

        String textual = ((PointListP2PP) result).getTextualRepresentation(pCtx);
        String ntextual = ((PointListP2PP) result).getNormalizedTextualRepresentation(pCtx);

        System.out.println("preserving  = \n" + textual);
        System.out.println("normalized  = \n" + ntextual);

        ((PointListP2PP)result).getModelObject().points().forEach(p -> {p.x+=100; p.y+=100;} );

         textual = ((PointListP2PP) result).getTextualRepresentation(pCtx);
         ntextual = ((PointListP2PP) result).getNormalizedTextualRepresentation(pCtx);

        System.out.println("preserving  = \n" + textual);
        System.out.println("normalized  = \n" + ntextual);

//        PointsFragmentRetriever retriever = new PointsFragmentRetriever(rawSource);
//        RawSource.RawSourceFragment fragment = retriever.retrieveFragment(antlr4tree.point(0));
//        System.out.println("fragment = " + fragment);
    }
}
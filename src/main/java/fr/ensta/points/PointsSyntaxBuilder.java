package fr.ensta.points;

import fr.ensta.points.p2pp.PointListP2PP;
import fr.ensta.points.p2pp.PointP2PP;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.openflexo.p2pp.RawSource;

public class PointsSyntaxBuilder extends PointsBaseListener {
    ParseTreeProperty<Object> values = new ParseTreeProperty<>();
    RawSource rawSource;
    public PointsSyntaxBuilder(RawSource rawSource) {
        this.rawSource = rawSource;
    }

    Object getEntry(ParseTree ctx) {
        return values.get(ctx);
    }

    @Override
    public void exitPoint(PointsParser.PointContext ctx) {
        values.put(ctx, new PointP2PP(values, ctx, new ANTLR4FragmentRetriever(rawSource)));
    }

    @Override
    public void exitPoints(PointsParser.PointsContext ctx) {
        values.put(ctx, new PointListP2PP(values, ctx, new ANTLR4FragmentRetriever(rawSource)));
    }
}

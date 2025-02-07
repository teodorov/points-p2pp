package fr.ensta.points.p2pp;

import fr.ensta.points.ANTLR4FragmentRetriever;
import fr.ensta.points.PointsParser;
import fr.ensta.points.model.Point;
import fr.ensta.points.model.PointList;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.openflexo.p2pp.P2PPNode;
import org.openflexo.p2pp.PrettyPrintContext;
import org.openflexo.p2pp.RawSource;

public class PointListP2PP extends P2PPNode<PointsParser.PointsContext, PointList> {
    ParseTreeProperty<Object> antlr2pp;
    ANTLR4FragmentRetriever fragmentRetriever;

    public PointListP2PP(ParseTreeProperty<Object> antlr2pp, PointsParser.PointsContext astNode, ANTLR4FragmentRetriever fragmentRetriever) {
        super(null, astNode, fragmentRetriever);
        this.fragmentRetriever = fragmentRetriever;
        this.antlr2pp = antlr2pp;

        modelObject = buildModelObjectFromAST(astNode);
    }

    @Override
    public PointList buildModelObjectFromAST(PointsParser.PointsContext pointsContext) {
        var points = pointsContext.point().stream().map((pc) -> {

            var pp = ((PointP2PP) antlr2pp.get(pc));
            addToChildren(pp);
            return pp.getModelObject();
        });

        return new PointList(points.toList());
    }

    @Override
    protected void preparePrettyPrint(boolean hasParsedVersion) {
        super.preparePrettyPrint(hasParsedVersion);
        append(childrenContents("", () -> getModelObject().points(), "\n", PrettyPrintContext.Indentation.DoNotIndent, Point.class));
    }

    @Override
    public P2PPNode<PointsParser.PointsContext, PointList> deserialize() {
        return this;
    }

    @Override
    public RawSource getRawSource() {
        return this.fragmentRetriever.rawSource;
    }

    @Override
    public <C> P2PPNode<?, C> makeObjectNode(C c) {
//        if (c instanceof PointsParser.PointsContext) {
//            //noinspection unchecked
//            return (P2PPNode<?, C>) new PointListP2PP(antlr2pp, (PointsParser.PointsContext) c, this.fragmentRetriever);
//        }
//        if (c instanceof PointsParser.PointContext) {
//            //noinspection unchecked
//            return (P2PPNode<?, C>) new PointP2PP(antlr2pp, (PointsParser.PointContext) c, this.fragmentRetriever);
//        }
        System.err.println("Not supported: " + c);
        Thread.dumpStack();
        return null;
    }
}

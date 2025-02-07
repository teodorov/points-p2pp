package fr.ensta.points.p2pp;

import fr.ensta.points.ANTLR4FragmentRetriever;
import fr.ensta.points.PointsParser;
import fr.ensta.points.model.Point;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.openflexo.p2pp.P2PPNode;
import org.openflexo.p2pp.RawSource;

public class PointP2PP extends P2PPNode<PointsParser.PointContext, Point> {
    ParseTreeProperty<Object> antlr2pp;
    ANTLR4FragmentRetriever fragmentRetriever;
    public PointP2PP(ParseTreeProperty<Object> antlr2pp,PointsParser.PointContext astNode, ANTLR4FragmentRetriever fragmentRetriever) {
        super(null, astNode, fragmentRetriever);
        this.fragmentRetriever = fragmentRetriever;
        this.antlr2pp = antlr2pp;

        modelObject = buildModelObjectFromAST(astNode);
    }

    @Override
    public Point buildModelObjectFromAST(PointsParser.PointContext pointContext) {
        int x = Integer.parseInt(pointContext.NUMBER(0).getText());
        int y = Integer.parseInt(pointContext.NUMBER(1).getText());
        return new Point(x, y);
    }

    @Override
    protected void preparePrettyPrint(boolean hasParsedVersion) {
        super.preparePrettyPrint(hasParsedVersion);
        append(staticContents("P"), this.fragmentRetriever.retrieveFragment( getASTNode().getChild(0)) );
        append(staticContents("["), this.fragmentRetriever.retrieveFragment( getASTNode().getChild(1)) );
        append(dynamicContents(() -> String.valueOf(getModelObject().x)), this.fragmentRetriever.retrieveFragment(getASTNode().getChild(2)) );
        append(staticContents(","), this.fragmentRetriever.retrieveFragment( getASTNode().getChild(3)) );
        append(dynamicContents(() -> String.valueOf(getModelObject().y)), this.fragmentRetriever.retrieveFragment(getASTNode().getChild(4)) );
        append(staticContents("]"), this.fragmentRetriever.retrieveFragment( getASTNode().getChild(5)) );
    }

    @Override
    public P2PPNode<PointsParser.PointContext, Point> deserialize() {
        return this;
    }

    @Override
    public RawSource getRawSource() {
        return this.fragmentRetriever.rawSource;
    }

    //TODO: Why do we need this method ?
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

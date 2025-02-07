package fr.ensta.points;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.openflexo.p2pp.FragmentRetriever;
import org.openflexo.p2pp.RawSource;
import org.openflexo.p2pp.RawSource.RawSourcePosition;

public class ANTLR4FragmentRetriever implements FragmentRetriever<ParseTree> {
    public RawSource rawSource;
    public ANTLR4FragmentRetriever(RawSource rawSource) {
        this.rawSource = rawSource;
    }

    @Override
    public RawSource.RawSourceFragment retrieveFragment(ParseTree parseTree) {
        if (parseTree instanceof TerminalNode) {
            return retrieveFragmentFromToken((TerminalNode) parseTree);
        }
        return retrieveFragmentFromParseRuleContext((ParserRuleContext) parseTree);
    }

    public RawSource.RawSourceFragment retrieveFragmentFromParseRuleContext(ParserRuleContext parseTree) {

        RawSourcePosition start = rawSource.makePositionAfterChar(parseTree.start.getLine(), parseTree.start.getCharPositionInLine());
        RawSourcePosition end = rawSource.makePositionAfterChar(parseTree.stop.getLine(), parseTree.stop.getCharPositionInLine()+ parseTree.stop.getText().length());

        return rawSource.makeFragment(start, end);
    }

    public RawSource.RawSourceFragment retrieveFragmentFromToken(TerminalNode p) {
        RawSourcePosition start = rawSource.makePositionAfterChar(p.getSymbol().getLine(), p.getSymbol().getCharPositionInLine());
        RawSourcePosition end = rawSource.makePositionAfterChar(p.getSymbol().getLine(), p.getSymbol().getCharPositionInLine() + p.getText().length());
        return rawSource.makeFragment(start, end);
    }
}

import fr.ensta.points.model.Point;
import fr.ensta.points.model.PointList;
import fr.ensta.points.p2pp.PointListP2PP;
import fr.ensta.points.p2pp.PointP2PP;
import org.junit.jupiter.api.Test;
import org.openflexo.p2pp.DefaultPrettyPrintContext;
import org.openflexo.p2pp.PrettyPrintContext;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPrintWithoutParsing {
    PrettyPrintContext pCtx = new DefaultPrettyPrintContext(PrettyPrintContext.Indentation.DoNotIndent);

    PointListP2PP makePointListPP(Point... ps) {
        var pointList = new PointList(Arrays.asList(ps));
        return new PointListP2PP(pointList);
    }
    @Test
    void testDuplicateObjectInContainer() {
        var expected = """
                P[1, 2]
                P[1, 2]
                """;

        var p1 = new Point(1, 2);
        var ppL = makePointListPP(p1, p1);
        ppL.initializePrettyPrint(null, pCtx);
        var actual = ppL.getTextualRepresentation(pCtx);
        assertEquals(expected, actual);
    }

    @Test
    void testDifferentIdentityObjectInContainer() {
        var expected = """
               P[1, 2]
               P[1, 2]
               """;

        var ppL = makePointListPP(new Point(1, 2), new Point(1, 2));
        ppL.initializePrettyPrint(null, pCtx);
        var actual = ppL.getTextualRepresentation(pCtx);
        assertEquals(expected, actual);
    }


    @Test
    void testTwoPointsInContainer() {
        var expected = """
               P[1, 2]
               P[2, 3]
               """;

        var ppL = makePointListPP(new Point(1, 2), new Point(2, 3));
        ppL.initializePrettyPrint(null, pCtx);
        var actual = ppL.getTextualRepresentation(pCtx);
        assertEquals(expected, actual);
    }

    @Test
    void testTwoPointsInContainerChange() {
        var expected = """
               P[2, 3]
               P[3, 4]
               """;

        var ppL = makePointListPP(new Point(1, 2), new Point(2, 3));
        ppL.initializePrettyPrint(null, pCtx);
        ppL.getTextualRepresentation(pCtx);

        ppL.getModelObject().points().forEach(point -> { point.x++; point.y++; });

        var actual = ppL.getTextualRepresentation(pCtx);
        assertEquals(expected, actual);
    }

    @Test
    void testOnePointInContainer() {
        var expected = """
               P[1, 2]
               """;

        var ppL = makePointListPP(new Point(1, 2));
        ppL.initializePrettyPrint(null, pCtx);
        var actual = ppL.getTextualRepresentation(pCtx);
        assertEquals(expected, actual);
    }

    @Test
    void testPoint() {
        var expected = "P[1, 2]";

        var pp = new PointP2PP(new Point(1, 2));
        pp.initializePrettyPrint(null, pCtx);
        var actual = pp.getTextualRepresentation(pCtx);
        assertEquals(expected, actual);
    }


}

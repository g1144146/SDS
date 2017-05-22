package sds.assemble.controlflow;

/**
 * This class is for edge of control flow graph.
 * @author inagaki
 */
public class CFEdge {
    private CFNode dest;
    private CFNode source;
    private CFEdgeType type;

    CFEdge(CFNode source, CFNode dest, CFEdgeType type) {
        this.source = source;
        this.dest = dest;
        this.type = type;
    }

    /**
     * returns destination node of this edge.
     * @return destination node
     */
    public CFNode getDest() {
        return dest;
    }

    /**
     * returns source node of this edge.
     * @return source node
     */
    public CFNode getSource() {
        return source;
    }

    /**
     * returns this edge type.
     * @return edge type
     */
    public CFEdgeType getType() {
        return type;
    }

    @Override
    public int hashCode() {
        return source.hashCode() + dest.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof CFEdge)) {
            return false;
        }
        CFEdge edge = (CFEdge)obj;
        CFNode s = edge.getSource();
        CFNode d = edge.getDest();
        return source.getStart().pc == s.getStart().pc
            && source.getEnd().pc   == s.getEnd().pc
            && dest.getStart().pc   == d.getStart().pc
            && dest.getEnd().pc     == d.getEnd().pc;
    }

    @Override
    public String toString() {
        return dest.getStart().pc + "-" + dest.getEnd().pc + "(" + type + ")";
    }
}
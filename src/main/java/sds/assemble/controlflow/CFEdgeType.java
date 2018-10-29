package sds.assemble.controlflow;

/**
 *
 * @author inagaki
 */
public enum CFEdgeType {
    Normal,
    JumpToCatch,
    JumpToFinally,
    TrueBranch,
    FalseBranch;
}

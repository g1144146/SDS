package sds.assemble.controlflow;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import sds.assemble.LineInstructions;
import sds.classfile.bytecode.BranchOpcode;
import sds.classfile.bytecode.OpcodeInfo;
import sds.classfile.bytecode.LookupSwitch;
import sds.classfile.bytecode.TableSwitch;
import sds.util.SDSStringBuilder;

import static sds.assemble.controlflow.NodeTypeChecker.check;
import static sds.assemble.controlflow.CFNodeType.Entry;
import static sds.assemble.controlflow.CFNodeType.OneLineEntry;
import static sds.assemble.controlflow.CFNodeType.OneLineEntryBreak;
import static sds.assemble.controlflow.CFNodeType.Exit;
import static sds.assemble.controlflow.CFNodeType.LoopExit;
import static sds.assemble.controlflow.CFNodeType.StringSwitch;
import static sds.assemble.controlflow.CFNodeType.SynchronizedExit;
import static sds.assemble.controlflow.CFNodeType.Switch;
import static sds.classfile.bytecode.MnemonicTable._goto;
import static sds.classfile.bytecode.MnemonicTable.goto_w;

/**
 * This class is for node of control flow graph.
 * @author inagaki
 */
public class CFNode {
    private Set<CFEdge> parents;
    private Set<CFEdge> children;
    private OpcodeInfo start;
    private OpcodeInfo end;
    private OpcodeInfo[] opcodes;
    private int[] jumpPoints = new int[0];
    private int gotoPoint = -1;
    private int[] switchJump = new int[0];
    private int hash;
    private String instStr;
    // package-private fields.
    CFNode dominator;
    CFNode  immediateDominator;
    CFNodeType nodeType;
    boolean inTry      = false;
    boolean inCatch    = false;
    boolean inFinally  = false;
    int synchIndent = 0;

    /**
     * constructor.
     * @param inst instructions of a line.
     */
    public CFNode(LineInstructions inst) {
        this.opcodes = inst.getOpcodes();
        int size = opcodes.length;
        this.start = opcodes[0];
        if(size == 1) {
            this.end = start;
        } else {
            this.end = opcodes[size - 1];
        }
        StringBuilder sb = new StringBuilder();
        for(OpcodeInfo info : opcodes) {
            sb.append(info.getType()).append(" ");
        }
        this.instStr = sb.toString();

        this.nodeType = CFNodeType.getType(inst, end);
        if(check(this, Entry, OneLineEntry, OneLineEntryBreak)) { // if_xx
            int[] points = new int[opcodes.length];
            int ifCount = 0;
            for(OpcodeInfo op : opcodes) {
                if(op instanceof BranchOpcode) {
                    int branch = ((BranchOpcode)op).getBranch() + op.getPc();
                    if(op.getType() == _goto || op.getType() == goto_w) {
                        this.gotoPoint = branch;
                        continue;
                    }
                    points[ifCount] = branch;
                    ifCount++;
                }
            }
            jumpPoints = Arrays.copyOf(points, ifCount);
        } else if(check(this, Exit, LoopExit)) { // goto
            this.gotoPoint = ((BranchOpcode)end).getBranch() + end.getPc();
        } else if(check(this, Switch)) { // switch
            for(OpcodeInfo op : opcodes) {
                if(op instanceof LookupSwitch) {
                    LookupSwitch look = (LookupSwitch)op;
                    this.switchJump = new int[look.getMatch().length + 1];
                    int[] offsets = look.getOffset();
                    for(int i = 0; i < switchJump.length-1; i++) {
                        switchJump[i] = offsets[i] + look.getPc();
                    }
                    switchJump[switchJump.length - 1] = look.getDefault() + look.getPc();
                    break;
                } else if(op instanceof TableSwitch) {
                    TableSwitch table = (TableSwitch)op;
                    this.switchJump = new int[table.getJumpOffsets().length + 1];
                    int[] offsets = table.getJumpOffsets();
                    for(int i = 0; i < switchJump.length-1; i++) {
                        switchJump[i] = offsets[i] + table.getPc();
                    }
                    switchJump[switchJump.length - 1] = table.getDefault() + table.getPc();
                    break;
                }
            }
         } else if(check(this, StringSwitch)) { // switch statement with string
            if(end instanceof LookupSwitch) {
                LookupSwitch look = (LookupSwitch)end;
                this.switchJump = new int[look.getMatch().length + 1];
                int[] offsets = look.getOffset();
                for(int i = 0; i < switchJump.length-1; i++) {
                    switchJump[i] = offsets[i] + look.getPc();
                }
                switchJump[switchJump.length - 1] = look.getDefault() + look.getPc();
            } else if(end instanceof TableSwitch) {
                TableSwitch table = (TableSwitch)end;
                this.switchJump = new int[table.getJumpOffsets().length + 1];
                int[] offsets = table.getJumpOffsets();
                for(int i = 0; i < switchJump.length-1; i++) {
                    switchJump[i] = offsets[i] + table.getPc();
                }
                switchJump[switchJump.length - 1] = table.getDefault() + table.getPc();
            }
        } else if(check(this, SynchronizedExit)) { // end of synchronized
            for(OpcodeInfo info : opcodes) {
                if(info instanceof BranchOpcode) {
                    this.gotoPoint = ((BranchOpcode)info).getBranch() + info.getPc();
                    break;
                }
            }
        }

        // calc hash
        char[] val1 = String.valueOf(start.getPc()).toCharArray();
        char[] val2 = String.valueOf(end.getPc()).toCharArray();
        char[] val3 = start.getType().toString().toCharArray();
        char[] val4 = end.getType().toString().toCharArray();
        for(int i = 0; i < val1.length; i++) {
            hash = 31 * hash + val1[i];
        }
        for(int i = 0; i < val2.length; i++) {
            hash = 31 * hash + val2[i];
        }
        for(int i = 0; i < val3.length; i++) {
            hash = 31 * hash + val3[i];
        }
        for(int i = 0; i < val4.length; i++) {
            hash = 31 * hash + val4[i];
        }

        this.parents    = new LinkedHashSet<>();
        this.children   = new LinkedHashSet<>();
    }

    /**
     * returns try-catch-finally flag.<br>
     * the flag is 3bit, and each bit is "finally", "catch" and "try".
     * @return try-catch-finally flag
     */
    public int isTryCatchFinally() {
        int flag = 0;
        if(inTry)     flag |= 0x01;
        if(inCatch)   flag |= 0x02;
        if(inFinally) flag |= 0x04;
        return flag;
    }

    /**
     * returns opcodes which this node has.
     * @return opcodes
     */
    public OpcodeInfo[] getOpcodes() {
        return opcodes;
    }

    /**
     * returns indexes into code array of jump point.
     * @return jump point indexes
     */
    public int[] getJumpPoints() {
        return jumpPoints;
    }

    /**
     * returns index into code array of jump point.
     * @return jump point index
     */
    public int getGotoPoint() {
        return gotoPoint;
    }

    /**
     * returns indexes into code array of jump point.
     * @return jump point indexes
     */
    public int[] getSwitchJump() {
        return switchJump;
    }

    /**
     * returns node type.
     * @return node type
     */
    public CFNodeType getType() {
        return nodeType;
    }

    /**
     * returns parent nodes.
     * @return parent nodes
     */
    public Set<CFEdge> getParents() {
        return parents;
    }

    /**
     * returns child nodes.
     * @return child nodes
     */
    public Set<CFEdge> getChildren() {
        return children;
    }

    /**
     * returns immediate dominator node.
     * @return immediate dominator node
     */
    public CFNode getImmediateDominator() {
        return immediateDominator;
    }

    /**
     * returns dominator node.
     * @return dominator node
     */
    public CFNode getDominator() {
        return dominator;
    }

    /**
     * returns whether specified pc is in range of opcode pc of this opcodes.
     * @param pc index into code array
     * @return if specified pc is in range of opcode pc, this method returns true.<br>
     * Otherwise, this method returns false.
     */
    public boolean isInPcRange(int pc) {
        return start.getPc() <= pc && pc <= end.getPc();
    }

    /**
     * returns start opcode of instructions of this node.
     * @return start opcode
     */
    public OpcodeInfo getStart() {
        return start;
    }

    /**
     * returns end opcode of instructions of this node.
     * @return end opcode
     */
    public OpcodeInfo getEnd() {
        return end;
    }

    void addParent(CFNode parent, CFEdgeType type) {
        if(equals(parent)) {
            return;
        }
        if(!isRoot()) {
            CFEdge edge = new CFEdge(this, parent, type);
            if(parents.isEmpty()) {
                this.immediateDominator = parent;
                this.parents.add(edge);
            } else {
                if(!parents.contains(edge)) {
                    parents.add(edge);
                }
            }
        }
    }

    void addChild(CFNode child) {
        addChild(child, CFEdgeType.Normal);
    }

    void addChild(CFNode child, CFEdgeType type) {
        if(equals(child)) {
            return;
        }
        CFEdge edge = new CFEdge(this, child, type);
        if(check(this, OneLineEntry, OneLineEntryBreak)) {
            children.remove(edge);
            children.add(edge);
        } else {
            if(!children.contains(edge)) {
                children.add(edge);
            }
        }
    }

    boolean isRoot() {
        return start.getPc() == 0;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof CFNode)) {
            return false;
        }
        CFNode node = (CFNode)obj;
        return start.getPc() == node.getStart().getPc()
            && end.getPc() == node.getEnd().getPc();
    }

    @Override
    public String toString() {
        SDSStringBuilder sb = new SDSStringBuilder();
        sb.append("#", start.getPc(), "-", end.getPc(), " [", nodeType, "]", "\n");
        if(inTry)           sb.append("  in try\n");
        if(inCatch)         sb.append("  in catch\n");
        if(inFinally)       sb.append("  in finally\n");

        sb.append("  opcodes: ", instStr, "\n");
        if(parents.size() == 1) {
            sb.append("  immediate dominator: ", parents.iterator().next());
        } else if((parents.size() > 1) && (dominator != null)) {
            sb.append("  dominator: ", dominator.start.getPc(), "-", dominator.end.getPc(), "\n  parents: ");
            for(CFEdge edge : parents) {
                sb.append(edge.toString(), " ");
            }
        } else {
            sb.append("  parents: not exist");
        }
        sb.append("\n  children: ");
        for(CFEdge edge : children) {
            sb.append(edge.toString(), " ");
        }
        return sb.toString();
    }
}
package sds.classfile.bytecode;

/**
 * This enum class is for mnemonic table of JVM instructions.
 * @author inagaki
 */
public enum MnemonicTable {
	nop(0x00),
	aconst_null(0x01),
	iconst_m1(0x02),
	iconst_0(0x03),
	iconst_1(0x04),
	iconst_2(0x05),
	iconst_3(0x06),
	iconst_4(0x07),
	iconst_5(0x08),
	lconst_0(0x09),
	lconst_1(0x0a),
	fconst_0(0x0b),
	fconst_1(0x0c),
	fconst_2(0x0d),
	dconst_0(0x0e),
	dconst_1(0x0f),
	bipush(0x10, 1),
	sipush(0x11, 2),
	ldc(0x12, 1),
	ldc_w(0x13, 2),
	ldc2_w(0x14, 2),
	iload(0x15),
	lload(0x16, 1),
	fload(0x17, 1),
	dload(0x18, 1),
	// index
	aload(0x19, 1),
	iload_0(0x1a),
	iload_1(0x1b),
	iload_2(0x1c),
	iload_3(0x1d),
	lload_0(0x1e),
	lload_1(0x1f),
	lload_2(0x20),
	lload_3(0x21),
	fload_0(0x22),
	fload_1(0x23),
	fload_2(0x24),
	fload_3(0x25),
	dload_0(0x26),
	dload_1(0x27),
	dload_2(0x28),
	dload_3(0x29),
	aload_0(0x2a),
	aload_1(0x2b),
	aload_2(0x2c),
	aload_3(0x2d),
	iaload(0x2e),
	laload(0x2f),
	faload(0x30),
	daload(0x31),
	aaload(0x32),
	baload(0x33),
	caload(0x34),
	saload(0x35),
	istore(0x36, 1),
	lstore(0x37, 1),
	fstore(0x38, 1),
	dstore(0x39, 1),
	astore(0x3a, 1),
	istore_0(0x3b),
	istore_1(0x3c),
	istore_2(0x3d),
	istore_3(0x3e),
	lstore_0(0x3f),
	lstore_1(0x40),
	lstore_2(0x41),
	lstore_3(0x42),
	fstore_0(0x43),
	fstore_1(0x44),
	fstore_2(0x45),
	fstore_3(0x46),
	dstore_0(0x47),
	dstore_1(0x48),
	dstore_2(0x49),
	dstore_3(0x4a),
	astore_0(0x4b),
	astore_1(0x4c),
	astore_2(0x4d),
	astore_3(0x4e),
	iastore(0x4f),
	lastore(0x50),
	fastore(0x51),
	dastore(0x52),
	aastore(0x53),
	bastore(0x54),
	castore(0x55),
	sastore(0x56),
	pop(0x57),
	pop2(0x58),
	dup(0x59),
	dup_x1(0x5a),
	dup_x2(0x5b),
	dup2(0x5c),
	dup2_x1(0x5d),
	dup2_x2(0x5e),
	swap(0x5f),
	iadd(0x60),
	ladd(0x61),
	fadd(0x62),
	dadd(0x63),
	isub(0x64),
	lsub(0x65),
	fsub(0x66),
	dsub(0x67),
	imul(0x68),
	lmul(0x69),
	fmul(0x6a),
	dmul(0x6b),
	idiv(0x6c),
	ldiv(0x6d),
	fdiv(0x6e),
	ddiv(0x6f),
	irem(0x70),
	lrem(0x71),
	frem(0x72),
	drem(0x73),
	ineg(0x74),
	lneg(0x75),
	fneg(0x76),
	dneg(0x77),
	ishl(0x78),
	lshl(0x79),
	ishr(0x7a),
	lshr(0x7b),
	iushr(0x7c),
	lushr(0x7d),
	iand(0x7e),
	land(0x7f),
	ior(0x80),
	lor(0x81),
	ixor(0x82),
	lxor(0x83),
	iinc(0x84, 2),
	i2l(0x85),
	i2f(0x86),
	i2d(0x87),
	l2i(0x88),
	l2f(0x89),
	l2d(0x8a),
	f2i(0x8b),
	f2l(0x8c),
	f2d(0x8d),
	d2i(0x8e),
	d2l(0x8f),
	d2f(0x90),
	i2b(0x91),
	i2c(0x92),
	i2s(0x93),
	lcmp(0x94),
	fcmpl(0x95),
	fcmpg(0x96),
	dcmpl(0x97),
	dcmpg(0x98),
	ifeq(0x99, 2),
	ifne(0x9a, 2),
	iflt(0x9b, 2),
	ifge(0x9c, 2),
	ifgt(0x9d, 2),
	ifle(0x9e, 2),
	if_icmpeq(0x9f, 2),
	if_icmpne(0xa0, 2),
	if_icmplt(0xa1, 2),
	if_icmpge(0xa2, 2),
	if_icmogt(0xa3, 2),
	if_icmple(0xa4, 2),
	if_acmpeq(0xa5, 2),
	if_acmpne(0xa6, 2),
	_goto(0xa7, 2),
	jsr(0xa8, 2),
	ret(0xa9, 1),
	tableswitch(0xaa, -1),
	lookupswitch(0xab, -1),
	ireturn(0xac),
	lreturn(0xad),
	freturn(0xae),
	dreturn(0xaf),
	areturn(0xb0),
	_return(0xb1),
	getstatic(0xb2, 2),
	putstatic(0xb3, 2),
	getfield(0xb4, 2),
	putfield(0xb5, 2),
	invokevirtual(0xb6, 2),
	invokespecial(0xb7, 2),
	invokestatic(0xb8, 2),
	invokeinterface(0xb9, 4),
	inovokedynamic(0xba, 4),
	_new(0xbb, 2),
	newarray(0xbc, 1),
	anewarray(0xbd, 2),
	arraylength(0xbe),
	athrow(0xbf),
	checkcast(0xc0, 2),
	_instanceof(0xc1, 2),
	monitorenter(0xc2),
	monitorexit(0xc3),
	wide(0xc4, -1),
	multianewarray(0xc5, 3),
	ifnull(0xc6, 2),
	ifnonnull(0xc7, 2),
	goto_w(0xc8, 4),
	jsr_w(0xc9, 4),
	breakpoint(0xca),
	impdep1(0xfe),
	impdep2(0xff);
	
	private int opcode;
	private int operand;

	MnemonicTable(int opcode) {
		this(opcode, 0);
	}

	MnemonicTable(int opcode, int operand) {
		this.opcode = opcode;
		this.operand = operand;
	}

	/**
	 * returns operand count.
	 * @return operand count
	 */
	public int getOperand() {
		return operand;
	}

	/**
	 * returns hex of opcode.
	 * @return opcode
	 */
	public int getOpcode() {
		return opcode;
	}

	/**
	 * returns opcode info.
	 * @param opcode hex of opcode
	 * @param pc index into the code array
	 * @return opcode info
	 * @throws UndefinedOpcodeException
	 */
	public static OpcodeInfo get(int opcode, int pc) throws UndefinedOpcodeException {
		// has no operand
		if( (0x00 <= opcode && opcode <= 0x0f) || (0x1a <= opcode && opcode <= 0x35) ||
			(0x3b <= opcode && opcode <= 0x83) || (0x85 <= opcode && opcode <= 0x98) ||
			(0xac <= opcode && opcode <= 0xb1) || (opcode == 0xbe || opcode == 0xbf) || 
			(opcode == 0xc2 || opcode == 0xc3  ||  opcode == 0xca)) {
			return new OpcodeInfo(values()[opcode], pc);
		}
		// has operand or undefined opcode
		switch(opcode) {
			case 0x10: /** bipush **/
			case 0x11: /** sipush **/
				return new PushOpcode(values()[opcode], pc);
			case 0x12: /** ldc **/
			case 0x13: /** ldc_w **/
			case 0x14: /** ldc2_w **/
			case 0xb2: /** getstatic **/
			case 0xb3: /** putstatic **/
			case 0xb4: /** getfield **/
			case 0xb5: /** putfield **/
			case 0xb6: /** invokevirtual **/
			case 0xb7: /** invokespecial **/
			case 0xb8: /** invokestatic **/
			case 0xbb: /** new **/
			case 0xc0: /** checkcast **/
			case 0xbd: /** anewarray **/
			case 0xc1: /** instanceof **/
				return new CpRefOpcode(values()[opcode], pc);
			case 0x15: /** iload **/
			case 0x16: /** lload **/
			case 0x17: /** fload **/
			case 0x18: /** dload **/
			case 0x19: /** aload **/
			case 0x36: /** istore **/
			case 0x37: /** lstore **/
			case 0x38: /** fstore **/
			case 0x39: /** dstore **/
			case 0x3a: /** astore **/
			case 0xa9: /** ret **/
				return new IndexOpcode(values()[opcode], pc);
			case 0x84: return new Iinc(pc);
			case 0x99: /** ifeq **/
			case 0x9a: /** ifne **/
			case 0x9b: /** iflt **/
			case 0x9c: /** ifge **/
			case 0x9d: /** ifgt **/
			case 0x9e: /** ifle **/
			case 0x9f: /** if_icmpeq **/
			case 0xa0: /** if_icmpne **/
			case 0xa1: /** if_icmplt **/
			case 0xa2: /** if_icmpge **/
			case 0xa3: /** if_icmpgt **/
			case 0xa4: /** if_icmple **/
			case 0xa5: /** if_acmpeq **/
			case 0xa6: /** if_acmpne **/
			case 0xa7: /** goto **/
			case 0xa8: /** jsr **/	
			case 0xc6: /** ifnull **/
			case 0xc7: /** ifnonnull **/
				return new BranchOpcode(values()[opcode], pc);
			case 0xaa: return new TableSwitch(pc);
			case 0xab: return new LookupSwitch(pc);
			case 0xb9: return new InvokeInterface(pc);
			case 0xba: return new InvokeDynamic(pc);
			case 0xbc: return new NewArray(pc);
			case 0xc4: return new Wide(pc);
			case 0xc5: return new MultiANewArray(pc);
			case 0xc8: /** goto_w **/
			case 0xc9: /** jsr_w **/
				return new BranchWideOpcode(values()[opcode], pc);
			case 0xfe: return new OpcodeInfo(values()[0xcb], pc); 
			case 0xff: return new OpcodeInfo(values()[0xcc], pc);
			default:   throw  new UndefinedOpcodeException(opcode);
		}
	}
}
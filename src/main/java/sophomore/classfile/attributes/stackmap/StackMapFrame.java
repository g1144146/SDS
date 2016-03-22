package sophomore.classfile.attributes.stackmap;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class StackMapFrame {
	/**
	 * 
	 */
	SameFrame sameFrame;
	/**
	 * 
	 */
	SameLocalsStackItemFrame slsif;
	/**
	 * 
	 */
	SameLocalsStackItemFrameExtended slsife;
	/**
	 * 
	 */
	ChopFrame chop;
	/**
	 * 
	 */
	SameFrameExtended sfe;
	/**
	 * 
	 */
	AppendFrame af;
	/**
	 * 
	 */
	FullFrame ff;

	StackMapFrame(RandomAccessFile raf) throws IOException {
		this.sameFrame = new SameFrame(raf.readByte());
		this.slsif = new SameLocalsStackItemFrame(raf);
		this.slsife = new SameLocalsStackItemFrameExtended(raf);
		this.chop = new ChopFrame(raf);
		this.sfe = new SameFrameExtended(raf);
		this.af = new AppendFrame(raf);
		this.ff = new FullFrame(raf);
	}

	public SameFrame getSameFrame() {
		return sameFrame;
	}

	public SameLocalsStackItemFrame getSLSIF() {
		return slsif;
	}

	public SameLocalsStackItemFrameExtended getSLSIFE() {
		return slsife;
	}

	public ChopFrame getChop() {
		return chop;
	}

	public SameFrameExtended getSFE() {
		return sfe;
	}

	public AppendFrame getAppend() {
		return af;
	}

	public FullFrame getFullFrame() {
		return ff;
	}
}

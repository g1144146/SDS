package sds;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertThat;

import static org.hamcrest.Matchers.is;

public class ClassFileReaderTest {
	private ClassFile cf;
	
	@Before
	public void startUp() {
		String filePath = generateFilePath("build" , "resources"
									, "test", "resources", "Hello.class");
		ClassFileReader reader = new ClassFileReader(filePath);
		reader.read();
		this.cf = reader.getClassFile();
	}
	
	private String generateFilePath(String... paths) {
		StringBuilder sb = new StringBuilder(paths.length * 2);
		String sep = File.separator;
		for(int i = 0; i < paths.length - 1; i++) {
			sb.append(paths[i]).append(sep);
		}
		sb.append(paths[paths.length-1]);
		return sb.toString();
	}

	@Test
	public void testNumbers() throws Exception {
		assertThat(Integer.toHexString(cf.magicNumber), is("cafebabe"));
		assertThat(cf.majorVersion, is(52));
		assertThat(cf.minorVersion, is(0));
	}
}
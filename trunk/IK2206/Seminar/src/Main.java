import StegoFileImpl.ReadStegoFileImpl;
import StegoFileImpl.WriteStegoFileImpl;
import StegoStrategyImpl.SimpleLSB;
import common.IContainerFile;
import common.IReadStegoFile;
import common.IStegoStrategy;
import common.IWriteStegoFile;
import container.BmpFileContainer;


public class Main {
	public static void main(String args[]) {
		Main m = new Main();
		m.test1();
		m.test2();

	}

	public void test1() {
		System.out.println("test1");
		byte[] test = "some one is going out tonight and will not work tomorow".getBytes();
		IReadStegoFile r = new ReadStegoFileImpl(test);
		IWriteStegoFile w = new WriteStegoFileImpl();

		while(r.hasMoreBits()) {
			w.setNextBits(r.getNextBits(2), 2);
		}

		System.out.println("Result");
		System.out.println(new String(w.getMessage()));		
	}

	public void test2() {
		System.out.println("test2");
		IContainerFile cover = new BmpFileContainer();
		cover.loadFile("src/resources/test.bmp");
		String secretMessage = "this is a secret message";
		IReadStegoFile readStego = new ReadStegoFileImpl(secretMessage.getBytes());
		IStegoStrategy lsb = new SimpleLSB(2);
		lsb.encode(cover, readStego);
		cover.saveFile("src/resources/testStego.bmp");
		IContainerFile modifiedCover = new BmpFileContainer();
		modifiedCover.loadFile("src/resources/testStego.bmp");
		IWriteStegoFile writeStego = new WriteStegoFileImpl(secretMessage.length());
		lsb.decode(modifiedCover, writeStego);
		System.out.println(new String(writeStego.getMessage()));

	}
}

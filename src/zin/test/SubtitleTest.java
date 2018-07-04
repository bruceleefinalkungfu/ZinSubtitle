package zin.test;

import zin.sub.util.SubtitleUtil;
import zin.tools.ZIO;

public class SubtitleTest {
	public static void main(String[] args) throws Exception {
		for(;;) {
			String subtract =  ZIO.input("How much to subtract");
			SubtitleUtil.subtractFromAss("D:\\zMovie\\zin.ass", subtract);
		}
	}
}

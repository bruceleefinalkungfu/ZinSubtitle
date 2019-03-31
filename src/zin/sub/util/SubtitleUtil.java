package zin.sub.util;

import zin.z.file.ZinFile;
import zin.z.string.ZinRegEx;
import zin.sub.bo.Subtitle;
import zin.sub.bo.SubtitleAssImpl;
import zin.sub.bo.SubtitleSentence.Prefix;
import zin.sub.bo.SubtitleSentence.Suffix;
import zin.sub.bo.SubtitleSrtImpl;

public class SubtitleUtil {
	
	public static ZinFile file = new ZinFile();
	
	/**
	 * <pre>
	 * values of subtract and their meaning
	 * 12		=> 0:00:12:00
	 * 2m3		=> 0:02:03:00
	 * 1m2s11	=> 0:01:02:11
	 * subtracting an hour is currently not supported, just subtract 30 twice 
	 * </pre>
	 * @param fileName
	 * @param subtract
	 * @throws Exception
	 */
	public static void subtractFromAss(String fileName, String subtract) throws Exception {
		String str = file.getStringFromFile(fileName);
		String splitter = "MarginL, MarginR, MarginV, Effect, Text\r\n";
		String[] arr = str.split(splitter);
		SubtitleAssImpl subtitle = new SubtitleAssImpl(new Prefix(arr[0] + splitter), arr[1], new Suffix(""));
		String s[] = subtract.split(ZinRegEx.literal("m").or(ZinRegEx.literal("s")).getRegEx());
		if(s.length == 1) {
			subtract = "0:0:"+subtract+":0";
			
		} else if(s.length == 2){
			subtract = "0:"+s[0] + ":" + s[1]+":0";
		} else {
			subtract = "0:" + s[0] + ":" + s[1] + ":" + s[2];
		}
		file.write(fileName, subtitle.subtract(subtract).toString());
	}
	
	/**
	 * <pre>
	 * values of subtract and their meaning
	 * 12		=> 0:00:12:00
	 * 2m3		=> 0:02:03:00
	 * 1m2s11	=> 0:01:02:11
	 * subtracting an hour is currently not supported, just subtract 30 twice 
	 * </pre>
	 * @param fileName
	 * @param subtract
	 * @throws Exception
	 */
	public static void addToAss(String fileName, String subtract) throws Exception {
		String str = file.getStringFromFile(fileName);
		subtract = subtract.replaceAll("-", "");
		String splitter = "MarginL, MarginR, MarginV, Effect, Text\r\n";
		String[] arr = str.split(splitter);
		SubtitleAssImpl subtitle = new SubtitleAssImpl(new Prefix(arr[0] + splitter), arr[1], new Suffix(""));
		String s[] = subtract.split(ZinRegEx.literal("m").or(ZinRegEx.literal("s")).getRegEx());
		if(s.length == 1) {
			subtract = "0:0:-"+subtract+":0";
			
		} else if(s.length == 2){
			subtract = "0:-"+s[0] + ":-" + s[1]+":0";
		} else {
			subtract = "0:-" + s[0] + ":-" + s[1] + ":-" + s[2];
		}
		file.write(fileName, subtitle.subtract(subtract).toString());
	}


	/**
	 * <pre>
	 * values of subtract and their meaning
	 * 12		=> 0:00:12:00
	 * 2m3		=> 0:02:03:00
	 * 1m2s11	=> 0:01:02:11
	 * subtracting an hour is currently not supported, just subtract 30 twice 
	 * </pre>
	 * @param fileName
	 * @param subtract
	 * @throws Exception
	 */
	public static void subtractFromSrt(String fileName, String subtract) throws Exception {
		String str = file.getStringFromFile(fileName);
		Subtitle subtitle = new SubtitleSrtImpl(str);
		String s[] = subtract.split(ZinRegEx.literal("m").or(ZinRegEx.literal("s")).getRegEx());
		if(s.length == 1) {
			subtract = "0:0:"+subtract+":0";
			
		} else if(s.length == 2){
			subtract = "0:"+s[0] + ":" + s[1]+":0";
		} else {
			subtract = "0:" + s[0] + ":" + s[1] + ":" + s[2];
		}
		file.write(fileName, subtitle.subtract(subtract).toString());
	}


	/**
	 * <pre>
	 * values of subtract and their meaning
	 * 12		=> 0:00:12:00
	 * 2m3		=> 0:02:03:00
	 * 1m2s11	=> 0:01:02:11
	 * subtracting an hour is currently not supported, just subtract 30 twice 
	 * </pre>
	 * @param fileName
	 * @param subtract
	 * @throws Exception
	 */
	public static void addToSrt(String fileName, String subtract) throws Exception {
		String str = file.getStringFromFile(fileName);
		subtract = subtract.replaceAll("-", "");
		Subtitle subtitle = new SubtitleSrtImpl(str);
		String s[] = subtract.split(ZinRegEx.literal("m").or(ZinRegEx.literal("s")).getRegEx());
		if(s.length == 1) {
			subtract = "0:0:-"+subtract+":0";
			
		} else if(s.length == 2){
			subtract = "0:-"+s[0] + ":-" + s[1]+":0";
		} else {
			subtract = "0:-" + s[0] + ":-" + s[1] + ":-" + s[2];
		}
		file.write(fileName, subtitle.subtract(subtract).toString());
	}
	
}
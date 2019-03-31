package zin.sub.bo;

import zin.z.string.ZinRegEx;
import zin.z.constant.ZinConstant;

public class SubtitleSentence {
	public static final String 		FROM_TO_SEPARATOR				= Constant.FROM_TO_SEPARATOR;
	public static final String 		HOUR_MIN_SEC_SEPARATOR			= Constant.HOUR_MIN_SEC_SEPARATOR;
	public static final String 		NEW_LINE						= Constant.NEW_LINE;
	
	public final Prefix prefix;
	public final Suffix suffix;
	
	public final SubtitleTime fromTime;
	public final SubtitleTime toTime;
	
	public final SubtitleText subtitleText;

	
	/**
	 * Used for Ass files
	 * @param prefix
	 * @param suffix
	 * @param wholeSentenceStr
	 */
	public SubtitleSentence(Prefix prefix, Suffix suffix, String wholeSentenceStr ) {
		this.prefix = prefix;
		this.suffix = suffix;
		String[] twoStr = wholeSentenceStr.split(ZinRegEx.literal(Constant.TEMP_SUBTITLE_SENTENCE_PREFIX).getRegEx());
		this.subtitleText = new SubtitleText(twoStr[1], Constant.TEMP_SUBTITLE_TEXT_PREFIX, new Suffix(""));
		String fromTo [] = twoStr[0].split(ZinRegEx.literal(Constant.TEMP_SENTENCE_PREFIX_STR).getRegEx())[1].split(",");
		this.fromTime = new SubtitleTime(fromTo[0]);
		this.toTime = new SubtitleTime(fromTo[1]);
	}
	
	/**
	 * Used for Srt files
	 * @param wholeSentenceStr
	 */
	public SubtitleSentence(String wholeSentenceStr) {
		this.prefix = null;
		this.suffix = null;
		String[] twoStr = wholeSentenceStr.trim().split(ZinRegEx.NEW_LINE.getRegEx());
		String fromToStr = twoStr[1];
		String subStr = "";
		for(int i=2 ; i < twoStr.length ; i++) {
			subStr += twoStr[i];
			subStr += NEW_LINE;
		}
		this.subtitleText = new SubtitleText(subStr, null, null);
		String fromTo [] = fromToStr.split(" --> ");
		this.fromTime = new SubtitleTime(fromTo[0]);
		this.toTime = new SubtitleTime(fromTo[1]);
	}
	
	private SubtitleSentence(Prefix prefix, Suffix suffix, SubtitleTime fromTime, SubtitleTime toTime, SubtitleText subtitleText) {
		super();
		this.prefix = prefix;
		this.suffix = suffix;
		this.fromTime = fromTime;
		this.toTime = toTime;
		this.subtitleText = subtitleText;
	}

	public SubtitleSentence subtract(String hhMMSSMs) {
		return new SubtitleSentence(this.prefix, this.suffix, fromTime.subtract(hhMMSSMs), toTime.subtract(hhMMSSMs), this.subtitleText);
	}

	public SubtitleSentence subtractSrt(String hhMMSSMs) {
		return new SubtitleSentence(this.prefix, this.suffix, fromTime.subtractSrt(hhMMSSMs), toTime.subtractSrt(hhMMSSMs), this.subtitleText);
	}
	
	/**
	 * If null it should set empty string
	 *
	 */
	public static class Prefix { public final String str; public Prefix(String str) { this.str = str; } }

	/**
	 * If null it should set empty string
	 *
	 */
	public static class Suffix { public final String str; public Suffix(String str) { this.str = str; } }
	public static class SubtitleText {
		public final String str; public final Prefix prefix; public final Suffix suffix;
		public SubtitleText(String str, Prefix prefix, Suffix suffix) {
			this.str = str; this.prefix = prefix; this.suffix = suffix;
		}
		public String getStringValue() {
			return prefix.str + str + suffix.str;
		}
		public String getSrtValue() {
			return str;
		}
	}
	public String getStringValue() {
		if(fromTime.getStringValue().equals(""))
			return "";
		return fromTime.hour < 0 ? "" : prefix.str + fromTime.getStringValue() + FROM_TO_SEPARATOR + toTime.getStringValue() + subtitleText.getStringValue() + ZinConstant.NEW_LINE;
	}
	
	public String getSrtValue() {
		if(fromTime.getSrtValue().equals(""))
			return "";
		return fromTime.getSrtValue() + " --> " + toTime.getSrtValue() + NEW_LINE
				+ subtitleText.getSrtValue() + NEW_LINE; 
	}
}

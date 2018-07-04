package zin.sub.bo;

import zin.sub.bo.SubtitleSentence.Prefix;

public class Constant {
	public static final String 		HOUR_MIN_SEC_SEPARATOR			= ":";
	public static final String 		SEC_MILLI_SEPARATOR				= ".";
	public static final String 		FROM_TO_SEPARATOR				= ",";
	public static final short		MAX_POSSIBLE_MILLI_SEC_VALUE	= 99;
	
	// DELETE
	public static final String		TEMP_SENTENCE_PREFIX_STR		= "Dialogue: 0,";
	public static final String		TEMP_SUBTITLE_SENTENCE_PREFIX	= ",Default,,0,0,0,,";
	public static final Prefix 		TEMP_SENTENCE_PREFIX			= new Prefix(Constant.TEMP_SENTENCE_PREFIX_STR);
	public static final Prefix		TEMP_SUBTITLE_TEXT_PREFIX		= new Prefix(Constant.TEMP_SUBTITLE_SENTENCE_PREFIX);
}

package zin.sub.bo;

import java.util.ArrayList;

import zin.z.string.ZinRegEx;
import zin.sub.bo.SubtitleSentence.Prefix;
import zin.sub.bo.SubtitleSentence.Suffix;
import zin.z.constant.ZinConstant;

public class SubtitleAssImpl implements Subtitle {
	public final Prefix prefix;
	public final ArrayList<SubtitleSentence> sentences;
	public final Suffix suffix;
	private SubtitleAssImpl(Prefix prefix, ArrayList<SubtitleSentence> sentences, Suffix suffix) {
		this.prefix = prefix;
		this.sentences = sentences;
		this.suffix = suffix;
	}
	public SubtitleAssImpl(Prefix prefix, String middleStuff, Suffix suffix) {
		this.prefix = prefix;
		this.suffix = suffix;
		this.sentences = new ArrayList<SubtitleSentence>();
		String arr[] = middleStuff.split(ZinRegEx.NEW_LINE.getRegEx());
		for (String str : arr) {
			sentences.add(new SubtitleSentence(Constant.TEMP_SENTENCE_PREFIX, new Suffix(""), str));
		}
	}
	
	public Subtitle subtract (String hhMMSSMs) {
		ArrayList<SubtitleSentence> newSentences = new ArrayList<>();
		SubtitleAssImpl subtitle = new SubtitleAssImpl(this.prefix, newSentences, this.suffix);
		for ( SubtitleSentence sentence : sentences) {
			newSentences.add(sentence.subtract(hhMMSSMs));
		}
		return subtitle;
	}
	
	public String toString () {
		StringBuilder sb = new StringBuilder();
		sb.append(prefix.str);
		for(SubtitleSentence s : sentences)
			sb.append(s.getStringValue());
		sb.append(suffix.str);
		return sb.toString();
	}
}

package zin.sub.bo;

import java.util.ArrayList;

import zin.string.ZinRegEx;
import zin.sub.bo.SubtitleSentence.Prefix;
import zin.sub.bo.SubtitleSentence.Suffix;

public class SubtitleSrtImpl implements Subtitle {

	public final ArrayList<SubtitleSentence> sentences;
	
	public SubtitleSrtImpl(ArrayList<SubtitleSentence> a) {
		this.sentences = a;
	}
	
	public SubtitleSrtImpl(String wholeText) {
		this.sentences = new ArrayList<SubtitleSentence>();
		String arr[] = wholeText.split("\\r\\n\\r\\n");
		for (String str : arr) {
			sentences.add(new SubtitleSentence(str));
		}
	}
	
	
	@Override
	public Subtitle subtract(String hhMMSSMs) {
		ArrayList<SubtitleSentence> newSentences = new ArrayList<>();
		Subtitle subtitle = new SubtitleSrtImpl(newSentences);
		for ( SubtitleSentence sentence : sentences) {
			newSentences.add(sentence.subtractSrt(hhMMSSMs));
		}
		return subtitle;
	}

	public String toString () {
		StringBuilder sb = new StringBuilder();
		for(SubtitleSentence s : sentences) {
			if(s.getSrtValue().equals(""))
				continue;
			sb.append("1" + Constant.NEW_LINE);
			sb.append(s.getSrtValue());
		}
		return sb.toString();
	}
	
}

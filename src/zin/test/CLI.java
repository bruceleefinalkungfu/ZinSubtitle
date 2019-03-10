package zin.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import zin.exception.ZinException;
import zin.exception.ZinFileNotFoundException;
import zin.file.ZinFile;
import zin.string.ZinRegEx;
import zin.string.ZinRegEx.Repeat;
import zin.sub.bo.ProcessSubtitle;
import zin.sub.util.SubtitleUtil;
import zin.tools.ZIO;
import zin.tools.ZinConstant;

public class CLI {
	static ZinFile file = new ZinFile();
	static List<String> list = null;
	public static String getFileData(String fileName) throws Exception {
		String fData = file.getStringFromFile(fileName);
		if(fileName.endsWith(".srt"))
		{
			while(!fData.startsWith("1"))
				fData = fData.substring(1);
		}
		return fData;
	}
	public static void main(String[] args) throws Exception {
		outerMostLoop:
		for(;;) {
			String fileName = getFileName();
			fileName = fileName.toLowerCase();
			file.write(ZinConstant.ZIN_DATA_DIR+"dumped_subs"+ZinConstant.SLASH+ fileName.substring(fileName.lastIndexOf("\\")+1), getFileData(fileName));
			outerLoop:
			for(;;) {
				String subtract =  ZIO.input("How much to subtract? or press P to process or C to choose another file");
				if(subtract.equalsIgnoreCase("p")) {
					ProcessSubtitle processSubtitle = new ProcessSubtitle(fileName);
					for(;;) {
						String fromTime = ZIO.input("From time? Leave empty for 0");
						subtract =  ZIO.input("How much to subtract? Press 'D' for when you're done and wanna begin subtraction");
						if(subtract.equalsIgnoreCase("d")) {
							for(Map.Entry<String, String> entry : processSubtitle.getEntries()) {
								SubtitleUtil.subtractFromSrt(fileName, "0");
								String fileFullData = getFileData(fileName).trim();
								fromTime = entry.getKey();
								subtract = entry.getValue();
								if(fileName.endsWith(".srt")) {
									SubtitleUtil.subtractFromSrt(fileName, fromTime);
									SubtitleUtil.addToSrt(fileName, fromTime);
									String dataToBeSubtracted = getFileData(fileName).trim();
									String dataToBeNotSubtracted = fileFullData.replaceAll("(\\Q"+dataToBeSubtracted+"\\E)", "");
									SubtitleUtil.subtractFromSrt(fileName, subtract);
									String subtractedData = getFileData(fileName);
									String dataToWrite = dataToBeNotSubtracted + subtractedData;
									file.write(fileName, dataToWrite);
								}
								else if(fileName.endsWith(".ass")) {
									SubtitleUtil.subtractFromAss(fileName, subtract);
									SubtitleUtil.addToAss(fileName, subtract);
									String dataToBeSubtracted = getFileData(fileName).trim();
									String dataToBeNotSubtracted = fileFullData.replaceAll("(\\Q"+dataToBeSubtracted+"\\E)", "");
									SubtitleUtil.subtractFromAss(fileName, subtract);
									String subtractedData = getFileData(fileName);
									String dataToWrite = dataToBeNotSubtracted + subtractedData;
									file.write(fileName, dataToWrite);
								}
								else
								{
									System.out.println("File "+fileName+" is not a subtitle file");
									break;
								}	
							}
							break outerLoop;
						}
						processSubtitle.mark(fromTime, subtract);
					}
				} else if(subtract.equalsIgnoreCase("c")) {
					break;
				} else {
					if(fileName.endsWith(".srt"))
						SubtitleUtil.subtractFromSrt(fileName, subtract);
					else if(fileName.endsWith(".ass"))
						SubtitleUtil.subtractFromAss(fileName, subtract);
					else
					{
						System.out.println("File "+fileName+" is not a subtitle file");
						break;
					}
				}
			}
		}
	}
	
	public static String getFileName() throws Exception {
		if(list == null)
			list = getFiles(".");
		for(int i=0 ; i<list.size() ; i++) {
			System.out.println((i+1) +" => " + list.get(i).substring(list.get(i).lastIndexOf("\\")+1));
		}
		String fileName = ZIO.input("Enter file index/name or leave it blank if it to be treated like zin.srt");
		if(fileName == null || fileName.trim().isEmpty())
			fileName = "zin.srt";
		else if(fileName.length() <= 2)
		{
			int index = Integer.parseInt(fileName.trim());
			fileName = list.get(index-1);
		}
		else {
			File f = file.getFileFromFileName(fileName);
			if(f.isDirectory()) {
				list = getFiles(fileName);
				for(int i=0 ; i<list.size() ; i++) {
					System.out.println((i+1) +" => " + list.get(i).substring(list.get(i).lastIndexOf("\\")+1));
				}
				fileName = ZIO.input("Enter file index/name or leave it blank if it to be treated like zin.srt");
				if(fileName == null || fileName.trim().isEmpty())
					fileName = "zin.srt";
				else if(fileName.length() <= 2)
				{
					int index = Integer.parseInt(fileName.trim());
					fileName = list.get(index-1);
				}
				else if( ! file.isFilePathAbsolute(fileName))
				{
					fileName = f.getAbsolutePath() + fileName;
				}
			}
		}
		return fileName;
	}
	
	public static List<String> getFiles(String dir) throws Exception {
		return new ArrayList<>( file.getAllFileNamesSet(dir, ".srt", ".ass") );		
	}
}

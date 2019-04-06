package zin.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import zin.z.exception.ZinException;
import zin.z.exception.ZinFileNotFoundException;
import zin.z.file.ZinFile;
import zin.z.string.ZinRegEx;
import zin.z.string.ZinRegEx.Repeat;
import zin.sub.bo.ProcessSubtitle;
import zin.sub.util.SubtitleUtil;
import zin.z.tools.ZIO;
import zin.z.constant.ZinConstant;

public class CLI {
	static ZinFile file = new ZinFile();
	static List<String> list = null;
	static String separator = "\"";
	public static String getFileData(String fileName) throws Exception {
		String fData = file.getStringFromFile(fileName);
		if(fileName.endsWith(".srt"))
		{
			while(!fData.startsWith("1"))
				fData = fData.substring(1);
		}
		return fData;
	}
	
	public static void createBackup(String files) throws Exception {
		String [] fileNames = files.split(separator);
		for(String fileName : fileNames)
			file.write(ZinConstant.ZIN_DATA_DIR+"dumped_subs"+ZinConstant.SLASH+ fileName.substring(fileName.lastIndexOf("\\")+1)+"."+new Date().getTime(), getFileData(fileName));
	}
	
	public static void processSub(String files, String fromTime, String subtract) throws Exception {
		String [] fileNames = files.split(separator);
		for(String fileName : fileNames) {
			SubtitleUtil.subtractFromSrt(fileName, "0");
			String fileFullData = getFileData(fileName).trim();
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
				throw new Exception("File "+fileName+" is not a subtitle file");
			}
		}
	}
	
	public static void subtractSub(String files, String subtract) throws Exception {
		String [] fileNames = files.split(separator);
		for(String fileName : fileNames) {
			if(fileName.endsWith(".srt"))
				SubtitleUtil.subtractFromSrt(fileName, subtract);
			else if(fileName.endsWith(".ass"))
				SubtitleUtil.subtractFromAss(fileName, subtract);
			else
			{
				throw new Exception("File "+fileName+" is not a subtitle file");
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		outerMostLoop:
		for(;;) {
			String fileName = getFileName();
			fileName = fileName.toLowerCase();
			createBackup(fileName);
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
								fromTime = entry.getKey();
								subtract = entry.getValue();
								processSub(fileName, fromTime, subtract);	
							}
							break outerLoop;
						}
						processSubtitle.mark(fromTime, subtract);
					}
				} else if(subtract.equalsIgnoreCase("c")) {
					break;
				} else {
					subtractSub(fileName, subtract);
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
		String fileName = ZIO.input("Enter file index/name");
		if(fileName == null || fileName.trim().isEmpty())
			fileName = "zin.srt";
		else if(fileName.length() <= 2)
		{
			int index = Integer.parseInt(fileName.trim());
			fileName = list.get(index-1);
		}
		else if (fileName.equalsIgnoreCase("all") || fileName.equalsIgnoreCase("bulk")) {
			fileName = getAllFileNames();
		}
		else {
			File f = file.getFileFromFileName(fileName);
			if(f.isDirectory()) {
				list = getFiles(fileName);
				for(int i=0 ; i<list.size() ; i++) {
					System.out.println((i+1) +" => " + list.get(i).substring(list.get(i).lastIndexOf("\\")+1));
				}
				fileName = ZIO.input("Enter file index/name");
				if(fileName == null || fileName.trim().isEmpty())
					fileName = "zin.srt";
				else if(fileName.length() <= 2)
				{
					int index = Integer.parseInt(fileName.trim());
					fileName = list.get(index-1);
				}
				else if (fileName.equalsIgnoreCase("all") || fileName.equalsIgnoreCase("bulk")) {
					fileName = getAllFileNames();
				}
				else if( ! file.isFilePathAbsolute(fileName))
				{
					fileName = f.getAbsolutePath() + fileName;
				}
			}
		}
		return fileName;
	}
	
	public static String getAllFileNames() {
		StringBuffer sf = new StringBuffer();
		for(String file : list) {
			if(! file.equalsIgnoreCase(list.get(0)))
				sf.append(separator);
			sf.append(file);
		}
		return new String(sf);
	}
	
	public static List<String> getFiles(String dir) throws Exception {
		return file.getAllFileNamesSet(dir, ".srt", ".ass").stream().sorted().collect(Collectors.toList());
	}
}

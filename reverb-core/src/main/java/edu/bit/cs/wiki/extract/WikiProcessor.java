package edu.bit.cs.wiki.extract;

import edu.washington.cs.knowitall.nlp.ChunkedSentence;
import util.IO;
import java.util.regex.*;
import edu.washington.cs.knowitall.nlp.OpenNlpSentenceChunker;
import java.io.IOException;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;
import edu.washington.cs.knowitall.extractor.ReVerbExtractor;
import edu.washington.cs.knowitall.extractor.conf.ConfidenceFunction;
import edu.washington.cs.knowitall.extractor.conf.ReVerbOpenNlpConfFunction;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedBinaryExtraction;
import edu.washington.cs.knowitall.normalization.BinaryExtractionNormalizer;
import edu.washington.cs.knowitall.normalization.NormalizedBinaryExtraction;

public class WikiProcessor {
	private OpenNlpSentenceChunker chunker;
	private BinaryExtractionNormalizer norm;
	private ReVerbExtractor reverb;
	private ConfidenceFunction confFunc; 
	private Pattern r;
	
	public WikiProcessor() throws IOException{
		chunker = new OpenNlpSentenceChunker();
		norm = new BinaryExtractionNormalizer();
        reverb = new ReVerbExtractor();
        confFunc = new ReVerbOpenNlpConfFunction();
        r = Pattern.compile(".+?(?=\\.\\n)");
        System.out.println("Initialized...");
	}
	
	public void processSent(String sentStr, Hashtable<String, String> form2Id){
		System.out.println("processingSent");
		ChunkedSentence sent = chunker.chunkSentence(sentStr);
		for (ChunkedBinaryExtraction extr : reverb.extract(sent)) {
            double conf = confFunc.getConf(extr);
            
            if(form2Id.contains(extr.getArgument1().toString()) && form2Id.contains(extr.getArgument2().toString())){
              System.out.println("Arg1=" + extr.getArgument1());
              System.out.println("Rel=" + extr.getRelation());
              System.out.println("Arg2=" + extr.getArgument2());
              System.out.println("Conf=" + conf);
            }
            
//            System.out.println("Arg1=" + extr.getArgument1());
//            System.out.println("Rel=" + extr.getRelation());
//            System.out.println("Arg2=" + extr.getArgument2());
//            System.out.println("Conf=" + conf);
//            NormalizedBinaryExtraction extrNorm = norm.normalize(extr);
//            System.out.println("Arg1=" + extrNorm.getArgument1Norm());
//            System.out.println("Rel=" + extrNorm.getRelationNorm());
//            System.out.println("Arg2=" + extrNorm.getArgument2Norm());
//            System.out.println("Conf=" + conf);
//            System.out.println();
        }
	}
	
	public String getTitle(String sent){
		String title = "";
		Matcher m = r.matcher(sent);
		if(m.find()){
			title = m.group(0);
		}
		
		return title;
	}
	
	public void Process(String file) throws IOException{
		ArrayList<String> content = IO.FileRead(file);
		Hashtable<String, String> form2Id = new Hashtable<String, String>();
		for(int i=0;i<content.size();i++){
			String line = content.get(i).toLowerCase();
			JSONObject jobj = new JSONObject(line);
			String url = (String) jobj.get("url");
			String id = url.replace("http://en.wikipedia.org/wiki/", "");
			String text = (String) jobj.get("text");
			String title = getTitle(text);
			
			form2Id.put(title, id);
			
			JSONArray annoJarr = (JSONArray) jobj.get("annotations");
			for(int j=0;j<annoJarr.length();j++){
				JSONObject obj = annoJarr.getJSONObject(j);
				String surfaceForm = obj.getString("surface_form");
				String uri = obj.getString("uri");
				form2Id.put(surfaceForm, uri);
			}
			processSent(text, form2Id);
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		WikiProcessor wp = new WikiProcessor();
		wp.Process("D:/workdata/WikiExtract/AA/wiki01");
		
//		String sentStr = "Bananas are an excellent source of potassium.";
//        
//        wp.processSent(sentStr);
//        wp.processSent(sentStr);
//        wp.processSent(sentStr);
    }
	
	
}

package laretlernejo.moleanalysis;


import java.io.*;
import java.util.*;
import android.view.*;
import android.content.*;
import android.util.*;
import org.xmlpull.v1.*;

import laretlernejo.moleanalysis.Word;

public class DataHandler
{
	List<Word> words;
	private Word word;
	private String text;
	
	public DataHandler(){
		words = new ArrayList<Word>();
	}
	public List<Word> getWords(){
		return words;
	}
	public List<Word> parse(InputStream is){
		XmlPullParserFactory factory = null;
		XmlPullParser parser = null;
		try{
			factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			parser = factory.newPullParser();
			parser.setInput(is, null);
			
			int eventType = parser.getEventType();
			while(eventType != XmlPullParser.END_DOCUMENT){
				String tagname = parser.getName();
				switch(eventType){
					case XmlPullParser.START_TAG:
					if(tagname.equalsIgnoreCase("word")){
						word = new Word();
					
					}
					break;
					case XmlPullParser.TEXT:
						text = parser.getText();
						break;
					case XmlPullParser.END_TAG:
					if(tagname.equalsIgnoreCase("word")){
						words.add(word);
						word.setWord(text);
					}
					break;
				default:
					break;
				}
				eventType = parser.next();
			}
			
		}catch(XmlPullParserException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		return words;
	}
			
}

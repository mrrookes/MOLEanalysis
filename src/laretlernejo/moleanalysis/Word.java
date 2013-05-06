package laretlernejo.moleanalysis;

import java.io.Serializable;
import java.util.*;


public class Word implements Serializable
{
	private String word;
	private int id = 0;

	public String getWord(){
		return word;
	}
	public void setWord(String word){
		this.word = word;
	}
	@Override
	public String toString(){
		return word;
	}
	@Override
	public int hashCode(){
		int result = 17;
		result = 37*result+id;
		return result;
	}
	@Override
	public boolean equals(Object obj){
		
		if(this==obj)
			return true;
		if(!(obj instanceof Word))
			return false;
		Word lhs = (Word) obj;
		
		return word.equals(lhs.getWord()); 
	}
}


package laretlernejo.moleanalysis;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import java.io.*;
import java.util.*;

public class MainActivity extends Activity
{
	public String message;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
	public void onClick(View view){
		EditText text = (EditText)findViewById(R.id.edit_box);
		message = text.getText().toString();
		String resultado = MOLEanalysis(message, "MOLE.xml");
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setMessage(resultado);
		dialogBuilder.setCancelable(true).setTitle("Analysis");
		dialogBuilder.create().show();
	}
	private String MOLEanalysis(String texto, String filename){
		
		List<Word> words=null;//Lista de palabras del diccionario
		String resultado = null;//Cadena de salida
		List<Word>palabras_conocidas = new ArrayList<Word>();
		List<Word>palabras_desconocidas= new ArrayList<Word>();
		List<Word>words_para_analizar = new ArrayList<Word>();//palabras del texto
		List<Word>texto_analisis_arreglado= new ArrayList<Word>();//palabras del texto en minúscula y sin signos
		
		
		//primeiro abrimos o archivo de dicionario
		try{
			DataHandler parser = new DataHandler();
			words = parser.parse(getAssets().open(filename));
			
		}catch (IOException e){
			e.printStackTrace();
		}
		
		//partimos en palabras o texto a analizar
		String []texto_analisis = texto.split("\\s+");
		for(int i = 0; i<texto_analisis.length; i++)
		{
			Word newWord = new Word();
			newWord.setWord(texto_analisis[i]);
			words_para_analizar.add(newWord);
			//enchemos una clase List con obxectos word coas palabras obtidas na vetana de texto
		}
	
		//Recorremos las palabras para analizar y para cada una miramos si está en el diccionario
		//Tambien las ponemos en minúscula ye les quitamos los símbolos
		//Si están en el diccionario las recogemos en la lista de palabras conocidas
		for(int i=0; i< words_para_analizar.size(); i++)
		{
			for(int j=0; j<words.size(); j++)
			{
				String []cosas_para_quitar = {".",",",";","=",":","(",")","\\{","\\}","?","!","\\","/","-"};	
				//poner en minúsculas, tanto la palabra del texto como la del diccionario
				
				String elemento1 = words_para_analizar.get(i).getWord().toLowerCase();
				String elemento2 = words.get(j).getWord().toLowerCase();

				//quitar los signos
				for(int x = 0; x<cosas_para_quitar.length;x++){
					elemento1 = elemento1.replace(cosas_para_quitar[x], "");
					elemento2 = elemento2.replace(cosas_para_quitar[x], "");
				}
				
				Word elemento1_word = new Word();
				Word elemento2_word = new Word();
				elemento1_word.setWord(elemento1);
				elemento2_word.setWord(elemento2);
				
				//Comprobar si la palabra ya está guardada para analizar
				if(!texto_analisis_arreglado.contains(elemento1_word))
				{
					//En caso de que no lo esté, guardarla
					texto_analisis_arreglado.add(elemento1_word);
				}
				//Comprobar si la palabra a analizar coincide con el diccionario y si ya está en la lista de palabras conocidas
				if(elemento1.equals(elemento2) && !palabras_conocidas.contains(elemento1_word))
				{	
					palabras_conocidas.add(elemento1_word);
				}
			}
		}
		
		//Calcular el porcentaje de palabas conocidas
		int porcentaje = palabras_conocidas.size()*100/words_para_analizar.size();
		
		//Vamos metiendo datos en la cadena de salida
		resultado = "El texto tiene " + texto_analisis_arreglado.size() + " palabras.";
		resultado = resultado +" "+ palabras_conocidas.size() + " fueron encontradas en MOLE ("+ porcentaje + "%). "; 
		
		
		//Retocamos el texto para dejar solo las palabras desconocidas, despues recorremos el texto obtenido y llenamos la lista de las desconocidas
		texto_analisis_arreglado.removeAll(palabras_conocidas);
		for(int i = 0; i < texto_analisis_arreglado.size(); i++){
			if(!palabras_desconocidas.contains(texto_analisis_arreglado.get(i))){		
				Word newWord = new Word();
				newWord.setWord(texto_analisis_arreglado.get(i).getWord());
				palabras_desconocidas.add(newWord);
			}
		}
		
		//Añadimos las palabras desconocidas a la cadena de salida sólo en caso de que existan
		if(palabras_desconocidas.size()>0)
			resultado = resultado + "No se han reconocido las siguientes palabras: ";
		
		//Vamos poniendo los datos de las palabras desconocidas en la cadena de salida
		for(int i=0; i<palabras_desconocidas.size(); i++){
			//Con estos if separamos el último elemento, para ponerle punto final en lugar de coma
			if(i<palabras_desconocidas.size()-1){
				resultado = resultado + palabras_desconocidas.get(i).getWord()+ ", ";
			}
			if(i == palabras_desconocidas.size()-1){
				resultado = resultado + palabras_desconocidas.get(i).getWord()+ ".";
			}
		}
		if(words_para_analizar.size()==0){
			resultado = "Por favor, introduce texo para analizar";
		}			
		return resultado;
			
	}
}

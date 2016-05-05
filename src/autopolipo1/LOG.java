/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autopolipo1;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class LOG{
//String Logs = "";  
String  Logs = " nome Inercio";





    
public LOG() {
    
   
    
    File arquivo = new File("C:\\Users\\Mr. Belton\\Desktop\\INSTRUCOES.txt");
 String linha="";
 linha = "skdhgbchkzfxjhcgskhcbgkjasgdaskhdbaskjdgaskhgkhsbnasgc";
try {
 
if (!arquivo.exists()) {

arquivo.createNewFile();
}
 

File[] arquivos = arquivo.listFiles();

//Escrever no arquivo

FileWriter fw = new FileWriter(arquivo, true);
 
BufferedWriter bw = new BufferedWriter(fw);
 

bw.write(Logs+linha);
bw.newLine();
 
bw.close();
fw.close();
 




//faz a leitura do arquivo
FileReader fr = new FileReader(arquivo);
 
BufferedReader br = new BufferedReader(fr);
 
//equanto houver mais linhas
while (br.ready()) {
//
 linha = linha+"\n"+ br.readLine();
 
//faz algo com a linha;
//System.out.println(linha);
 
}
JOptionPane.showMessageDialog(null,linha);
br.close();
fr.close();
 
} catch (IOException ex) {
ex.printStackTrace();
}
}

public static void main(String[] args) {
 
new LOG();
 
}
}

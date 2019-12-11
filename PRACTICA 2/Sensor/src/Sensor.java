import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.*;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
*  Clase de las sondas
*  Práctica 2 de SD - Comunicación y encriptación
*  @author ivanmmurciaua
*/
public class Sensor{ 
	
	private final String characterEncoding = "UTF-8";
    private final String cipherTransformation = "AES/CBC/PKCS5Padding";
    private final String aesEncryptionAlgorithm = "AES";
	
    /**
     * Método para iniciar (no usado)
     * @param id
     * @param usuario
     * @param contrasenya
     * @param ip
     * @return 1
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
	public int iniciar(String id, String usuario, String contrasenya, String ip) throws IOException,UnsupportedEncodingException {
		
		id=decrypt(id);
		ip=decrypt(ip);
		String directorio= "";

		File fichero;
		String volumen;
		String fecha;
		String led;
		
		//Creamos el TXT de la sonda si no existe
		directorio="./sensor"+id+".txt";
		fichero = new File(directorio);
		if (!fichero.exists()) {
		    volumen="0";
		    fecha=this.fecha();
		    led="0";
		    FileWriter escribir=new FileWriter(fichero,true);      
		    escribir.write("Volumen="+volumen+"\r\nUltimaFecha="+fecha+"\r\nLed="+led);
		    escribir.close();
		}
		return 1;
	}
	
	/**
	 * Método usado para leer de fichero los datos
	 * @param busca
	 * @param id
	 * @return cadena encontrada
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	private String leer(String busca, String id) throws IOException,UnsupportedEncodingException{
		String sloots="";
		
		try(BufferedReader br = new BufferedReader(new FileReader("./sensor"+id+".txt"))){
			String fich = br.readLine();
			while(fich != null){
				if(fich.contains(busca)){
					sloots = fich.split("=")[1];
				}
				else {}
				fich = br.readLine();
			}
		}
		catch(Exception ex) { System.out.println(ex); return ""; }
		
		return sloots;
	}
	
	/**
	 * Getter de la fecha del sistema
	 * @return fecha del sistema
	 */
	public String fecha() {
		Date fechaActual = new Date();
        DateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
       	DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        return formatoFecha.format(fechaActual)+" "+formatoHora.format(fechaActual);
	}

	/**
	* Getter del volumen
	* @return Volumen del sensor
	*/
	public String volumen(String id, String usu, String pwd, String ip) throws IOException,UnsupportedEncodingException{
		if(checkUser(usu,pwd)){
			this.escribirLog("COMUNICACIÓN ENTRANTE DESDE "+ip+" EL USUARIO "+usu+"CON LA CONTRASEÑA "+pwd+"NECESITA LEER EL VOLUMEN DE LA SONDA "+id);

			this.escribirLog("DESENCRIPTANDO COMUNICACIONES");
			id = this.decrypt(id);
			usu = this.decrypt(usu);
			ip = this.decrypt(ip);

			this.escribirLog("COMUNICACIÓN ENTRANTE DESDE "+ip+" EL USUARIO "+usu+"CON LA CONTRASEÑA "+pwd+"NECESITA LEER EL VOLUMEN DE LA SONDA "+id);

			String dev = this.encrypt(leer("Volumen=",id));
			this.escribirLog("DEVOLVIENDO VALOR "+dev+" EN PLANO : "+this.decrypt(dev));

			return dev;
		}
		else return "Fallo en el usuario";
	}
	
	/**
	* Getter del led
	* @return Valor del led del sensor
	*/
	public String luz(String id, String usu, String pwd, String ip) throws IOException,UnsupportedEncodingException{
		if(checkUser(usu,pwd)){
			this.escribirLog("COMUNICACIÓN ENTRANTE DESDE "+ip+" EL USUARIO "+usu+" CON LA CONTRASEÑA "+pwd+" NECESITA LEER EL VALOR DEL LED DE LA SONDA "+id);
	
			this.escribirLog("DESENCRIPTANDO COMUNICACIONES");
			id = this.decrypt(id);
			usu = this.decrypt(usu);
			ip = this.decrypt(ip);
	
			this.escribirLog("COMUNICACIÓN ENTRANTE DESDE "+ip+" EL USUARIO "+usu+" CON LA CONTRASEÑA "+pwd+" NECESITA LEER EL VALOR DEL LED DE LA SONDA "+id);
	
			String dev = this.encrypt(leer("Led=",id));
			this.escribirLog("DEVOLVIENDO VALOR "+dev+" EN PLANO : "+this.decrypt(dev));
	
			return dev;
		}
		else return "Fallo en el usuario";
	}
	
	/**
	* Getter de la ultima fecha de modificacion del sensor
	* @return Ultima fecha de modificacion del sensor
	*/
	public String ultimaFecha(String id, String usu, String pwd, String ip) throws IOException,UnsupportedEncodingException{
		if(checkUser(usu,pwd)){
			this.escribirLog("COMUNICACIÓN ENTRANTE DESDE "+ip+" EL USUARIO "+usu+" CON LA CONTRASEÑA "+pwd+" NECESITA LEER EL VALOR DE LA ULTIMA FECHA DE LA SONDA "+id);
	
			this.escribirLog("DESENCRIPTANDO COMUNICACIONES");
			id = this.decrypt(id);
			usu = this.decrypt(usu);
			ip = this.decrypt(ip);
	
			this.escribirLog("COMUNICACIÓN ENTRANTE DESDE "+ip+" EL USUARIO "+usu+" CON LA CONTRASEÑA "+pwd+" NECESITA LEER EL VALOR DE LA ULTIMA FECHA DE LA SONDA "+id);
	
			String dev = this.encrypt(leer("UltimaFecha=",id));
			this.escribirLog("DEVOLVIENDO VALOR "+dev+" EN PLANO : "+this.decrypt(dev));
	
			return dev;
		}
		else return "Fallo en el usuario";
	}
	
	/**
	* Setter de la fecha, utilizado por otros metodos
	* @return String para saber si ha cambiado
	*/
	private String setfecha(int sonda) throws UnsupportedEncodingException, IOException{
		Date fechaActual = new Date();
    	DateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
   	 	DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
    	String val = formatoFecha.format(fechaActual)+" "+formatoHora.format(fechaActual);
    	if(writeSetters("UltimaFecha=",val,sonda)) return "Variable cambiada correctamente";
    	else return "Variable no cambiada";
	}
	
	/**
	* @param sonda - id de la sonda(no se utiliza, solo para la respuesta)
	* @param val - valor a cambiar
	* @return String para saber si ha cambiado
	* Setter del led de la sonda
	*/
	public String setled(int sonda, String val, String usu, String c, String ip) throws UnsupportedEncodingException, IOException {
		
		String res="Variable no cambiada";
		
		this.escribirLog("COMUNICACIÓN ENTRANTE DESDE "+ip+" EL USUARIO "+usu+" CON LA CONTRASEÑA "+c+" NECESITA CAMBIAR EL VALOR DEL LED DE LA SONDA "+this.encrypt(String.valueOf(sonda)));
		
		this.escribirLog("DESENCRIPTANDO COMUNICACIONES");

		usu = this.decrypt(usu);
		ip = this.decrypt(ip);
			
		this.escribirLog("COMUNICACIÓN ENTRANTE DESDE "+ip+" EL USUARIO "+usu+" CON LA CONTRASEÑA "+c+" NECESITA CAMBIAR EL VALOR DEL LED DE LA SONDA "+sonda);
			
		if(writeSetters("Led=",val,sonda)) res = this.encrypt("Luz led cambiada de la sonda "+sonda+" a "+val);

		this.escribirLog("DEVOLVIENDO VALOR "+res+" EN PLANO : "+this.decrypt(res));
		
		return res;
		
		//if(writeSetters("Led=",val,sonda)) return "Luz led cambiada de la sonda "+sonda+" a "+val;
		//else return "Variable no cambiada";
	}
	
	public String setledi(int sonda, String val) throws UnsupportedEncodingException, IOException {

		if(writeSetters("Led=",val,sonda)) return "Luz led cambiada de la sonda "+sonda+" a "+val;
		else return "Variable no cambiada";
	}
	
	/**
	*  @param sonda - id de la sonda(no se usa, solo para la respuesta)
	*  @param val - valor a cambiar
	*  @return String para saber si ha cambiado
	*  Setter del volumen de la sonda
	*/
	public String setvolumen(int sonda, String val, String usu, String c, String ip) throws UnsupportedEncodingException, IOException {
		
		if(Integer.parseInt(val)>100) {   val = "100";   }
		if(Integer.parseInt(val)<0) {   val = "0";   }
		setfecha(sonda);
		if(Integer.parseInt(val)>=30) {  setledi(sonda,"50000");   }
		else {  setledi(sonda,"0");   }
		
		String res="Variable no cambiada";
		
		this.escribirLog("COMUNICACIÓN ENTRANTE DESDE "+ip+" EL USUARIO "+usu+" CON LA CONTRASEÑA "+c+" NECESITA CAMBIAR EL VOLUMEN DE LA SONDA "+this.encrypt(String.valueOf(sonda)));
		
		this.escribirLog("DESENCRIPTANDO COMUNICACIONES");

		usu = this.decrypt(usu);
		ip = this.decrypt(ip);
			
		this.escribirLog("COMUNICACIÓN ENTRANTE DESDE "+ip+" EL USUARIO "+usu+" CON LA CONTRASEÑA "+c+" NECESITA CAMBIAR EL VOLUMEN DE LA SONDA "+sonda);
			
		if(writeSetters("Volumen=",val,sonda)) res = this.encrypt("Volumen cambiado de la sonda "+sonda+" a "+val);

		this.escribirLog("DEVOLVIENDO VALOR "+res+" EN PLANO : "+this.decrypt(res));
		
		return res;
	}
	
	/**
	*  @param busca - String a buscar en el fichero
	*  @param valor - Valor a cambiar en el fichero
	*  @return Boolean para saber si se ha escrito bien
	*  Metodo para escribir en el TXT
	*/
	private boolean writeSetters(String busca, String valor, int ids) throws UnsupportedEncodingException, IOException{
		String directorio="./sensor"+ids+".txt";      
        String directorioaux="./s"+ids+".txt";   
        String texto;
        
        String fecha=this.fecha();
        String textoFecha="UltimaFecha=";
        
        try {
        File fichero = new File(directorio);
        File ficheroaux = new File(directorioaux);
        FileReader lector=new FileReader(directorio);
        BufferedReader contenido=new BufferedReader(lector);
        FileWriter escribir=new FileWriter(ficheroaux,true);   
        
        
            if(fichero.exists()){
                BufferedReader Flee= new BufferedReader(new FileReader(directorio));
                while((texto=contenido.readLine())!=null) { 
                    if (texto.contains(busca)) {
                        escribir.write(busca+valor+"\r\n");
                    }
                    else if(texto.contains(textoFecha)){
                    	escribir.write(textoFecha+fecha+"\r\n");
                    }else{
                        /*Escribo la linea antigua*/
                        escribir.write(texto+"\r\n");
                    }             
                }
                Flee.close();
                escribir.close();
                contenido.close();
                lector.close();
                fichero.delete();
                ficheroaux.renameTo(fichero);
                /*Cierro el flujo de lectura*/
                return true;

            }else{
                System.out.println("Fichero No Existe");
                escribir.close();
                contenido.close();
                return false;
            }
        } catch (Exception ex) {
            /*Captura un posible error y le imprime en pantalla*/ 
             System.out.println(ex.getMessage());
             return false;
        }
	}
	
	/**
	 * Método usado para chequear usuario y contraseña en el sistema
	 * @param usuario
	 * @param contrasena
	 * @return si existe
	 */
	private boolean checkUser(String usuario, String contrasena) {
		System.out.println(usuario);
		System.out.println(contrasena);
		boolean ok=false;
		usuario = this.decrypt(usuario);
		try(BufferedReader br = new BufferedReader(new FileReader("./datos.txt"))){
			String fich = br.readLine();
			while(fich != null && !ok){
				System.out.println(fich);
				ok = (fich.split(" ")[0].equals(usuario) && fich.split(" ")[1].equals(contrasena));
				System.out.println(ok);
				fich = br.readLine();
			}
			br.close();
		}
		catch(Exception ex) { System.out.println(ex); }
		return ok;
	}
    
	/**
	 * Método para leer la clave AES generada para la conversación
	 * @return clave
	 */
    private String readKey() {
    	String k="1";
    	try(BufferedReader br = new BufferedReader(new FileReader("./clave.txt"))){
			k = br.readLine();
		}
		catch(Exception ex) { System.out.println(ex);}
    	return k;
    }
 
    /**
     * Decrypter
     * @param cipherText
     * @param key
     * @param initialVector
     * @return decrypt
     */
    private  byte[] decrypt(byte[] cipherText, byte[] key, byte [] initialVector){
    	try {
	        Cipher cipher = Cipher.getInstance(cipherTransformation);
	        SecretKeySpec secretKeySpecy = new SecretKeySpec(key, aesEncryptionAlgorithm);
	        IvParameterSpec ivParameterSpec = new IvParameterSpec(initialVector);
	        cipher.init(Cipher.DECRYPT_MODE, secretKeySpecy, ivParameterSpec);
	        cipherText = cipher.doFinal(cipherText);
	        return cipherText;
    	}catch(Exception e){ System.out.println(e+"5"); return null;}
    }
 
    /**
     * Encrypt
     * @param plainText
     * @param key
     * @param initialVector
     * @return encrypt
     */
    private byte[] encrypt(byte[] plainText, byte[] key, byte [] initialVector) {
    	try {
	        Cipher cipher = Cipher.getInstance(cipherTransformation);
	        SecretKeySpec secretKeySpec = new SecretKeySpec(key, aesEncryptionAlgorithm);
	        IvParameterSpec ivParameterSpec = new IvParameterSpec(initialVector);
	        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
	        plainText = cipher.doFinal(plainText);
	        return plainText;
    	}catch(Exception e){ System.out.println(e+"4"); return null;}
    }
 
    /**
     * getKeyBytes de la clave
     * @param key
     * @return la clave en bytes
     */
    private byte[] getKeyBytes(String key) {
    	try {
	        byte[] keyBytes= new byte[16];
	        byte[] parameterKeyBytes= key.getBytes(characterEncoding);
	        System.arraycopy(parameterKeyBytes, 0, keyBytes, 0, Math.min(parameterKeyBytes.length, keyBytes.length));
	        return keyBytes;
    	}catch(Exception e){ System.out.println(e+"3"); return null;}
    }
 
    /**
     * Encrypt
     * @param plainText
     * @return encrypted text
     */
    private String encrypt(String plainText) {    	
    	String key=readKey();
    	try {
	    	byte[] plainTextbytes = plainText.getBytes(characterEncoding);
	        byte[] keyBytes = getKeyBytes(key);
	        return Base64.getEncoder().encodeToString(encrypt(plainTextbytes,keyBytes, keyBytes));
		}catch(Exception e){ System.out.println(e+"2"); return null;}
    }
 
    /**
     * Decrypt
     * @param encryptedText
     * @return decrypt
     */
    private String decrypt(String encryptedText) {
    	String key=readKey();
    	try{
	    	byte[] cipheredBytes = Base64.getDecoder().decode(encryptedText);
	        byte[] keyBytes = getKeyBytes(key);
	        return new String(decrypt(cipheredBytes, keyBytes, keyBytes), characterEncoding);
    	}catch(Exception e){ System.out.println(e+"1"); return null;}
    }
    
    /**
     * Escribe el log
     * @param linea
     */
    private void escribirLog(String linea) {
        String directorio="./log.txt";      
        String texto;
               
        texto= this.fecha() + "\t = " + linea;
        
        try {
        	File fichero = new File(directorio);
        	FileWriter escribir=new FileWriter(directorio,true);   
            if(fichero.exists()){
            	escribir.write(texto+"\r\n");
                escribir.close();
            }else{
                System.out.println("Fichero No Existe");
            }
        } catch (Exception ex) {
            //Captura un posible error y le imprime en pantalla
             System.out.println(ex.getMessage());
        }
    }

}


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyPairGenerator;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.PrivateKey;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;


public class Cifrado{

   private static void escribirData(InputStream is, OutputStream os) throws IOException {

        byte[] buf = new byte[1024];
        int i = 0;

        while ((i = is.read(buf)) >= 0) {
            os.write(buf, 0, i);
        }

        os.close();
        is.close();
    }
    
  private static void cifrado(InputStream is, OutputStream os, PublicKey llave) throws Exception {
    Cipher encriptado = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    encriptado.init(Cipher.ENCRYPT_MODE, llave);
    os = new CipherOutputStream(os, encriptado);
    escribirData(is, os);            
  }
  
  private static void descifrado(InputStream is, OutputStream os, PrivateKey llave) throws Exception{
    Cipher desencriptado = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    desencriptado.init(Cipher.DECRYPT_MODE, llave);
    is = new CipherInputStream(is, desencriptado);    
    escribirData(is, os);
  }

//   public static void main(String[] args) throws Exception {
//     
//       // Generate a key-pair
//       KeyPairGenerator parGenerado = KeyPairGenerator.getInstance("RSA");
//       parGenerado.initialize(1024); // Llave de 1024
//       KeyPair par = parGenerado.generateKeyPair();
//       PublicKey publica = par.getPublic();
//       PrivateKey privada = par.getPrivate();
//       
//       String entrada = "/home/caponte/Desktop/test.txt";
//       String archCifrado = "/home/caponte/Desktop/test-cifrado.txt";
//       String salida = "/home/caponte/Desktop/test-descifrado.txt";
//       
//       cifrado(new FileInputStream(entrada), new FileOutputStream(archCifrado), publica);
//       descifrado(new FileInputStream(archCifrado), new FileOutputStream(salida), privada);
//     }
 }
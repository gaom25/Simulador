package prueba;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class Cifrado{

    private static final byte[] iv = {11, 22, 33, 44, 99, 88, 77, 66};
   
    
    private static void encrypt(SecretKey llave, InputStream is, OutputStream os) throws IOException {

	AlgorithmParameterSpec  paramSpec = new IvParameterSpec(iv);
        Cipher encriptado;
        try {
            encriptado = Cipher.getInstance("DES/CBC/PKCS5Padding");

            encriptado.init(Cipher.ENCRYPT_MODE, llave, paramSpec);

            //Stream para cifrar la data.
            os = new CipherOutputStream(os, encriptado);
            escribirData(is, os);
        } catch (NoSuchAlgorithmException e) {
        } catch (NoSuchPaddingException e) {
        } catch (InvalidKeyException e) {
        } catch (InvalidAlgorithmParameterException e) {
        }
    }

    private static void descifrar(SecretKey llave, InputStream is, OutputStream os) throws IOException {
 
	AlgorithmParameterSpec  paramSpec = new IvParameterSpec(iv);
        Cipher desencriptado;

        try {
        
            desencriptado = Cipher.getInstance("DES/CBC/PKCS5Padding");
            desencriptado.init(Cipher.DECRYPT_MODE, llave, paramSpec);

            //Stream para descifrar la data.
            is = new CipherInputStream(is, desencriptado);
            escribirData(is, os);
            
        } catch (NoSuchAlgorithmException e) {
        } catch (NoSuchPaddingException e) {
        } catch (InvalidKeyException e) {
        } catch (InvalidAlgorithmParameterException e) {
        }
    }

    //Escribe la data en el destino
    private static void escribirData(InputStream is, OutputStream os) throws IOException {

        byte[] buf = new byte[1024];
        int i = 0;

        while ((i = is.read(buf)) >= 0) {
            os.write(buf, 0, i);
        }

        os.close();
        is.close();
    }

//     public static void main(String[] args) {
//         String entrada = "/home/caponte/Desktop/test.txt";
//         String salida = "/home/caponte/Desktop/test-cifrado.txt";
//         String salidaDescifrada = "/home/caponte/Desktop/test-new.txt";
// 
//         try {
// 
//             SecretKey llave = KeyGenerator.getInstance("DES").generateKey();
// 
//             //method to encrypt clear text file to encrypted file
//             encrypt(llave, new FileInputStream(entrada), new FileOutputStream(salida));
//             //method to decrypt encrypted file to clear text file
//             descifrar(llave, new FileInputStream(salida), new FileOutputStream(salidaDescifrada));
// 
//         } catch (NoSuchAlgorithmException e) {
//         } catch (IOException e) {
//         }
//     }

}

public class Hola{
  public static void main(String[]args){
    Hilo t = new Hilo();
    Thread un = new Thread(t);
    Thread dos = new Thread(t);
    un.setName("Uno");
    dos.setName("Dos");
    
    un.start();
    dos.start();
    
    System.out.println("Voy a morir");

  }

}
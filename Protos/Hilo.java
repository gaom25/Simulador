class Hilo extends java.lang.Thread {
  private int varComp = 100000;
  public void run(){
      while(varComp > 0){
        decrementar();
        System.out.println("Durmiendo hilo "+Thread.currentThread().getName()+
                           " varComp= "+varComp);
	try{
	  Thread.sleep(2000);
	}catch(InterruptedException ex){
	  ex.printStackTrace();
	}

      }
  }
  
  private synchronized void decrementar(){
	System.out.println("intentando acceder a la var compartida");
	varComp = varComp / 2;	
      }
}
  


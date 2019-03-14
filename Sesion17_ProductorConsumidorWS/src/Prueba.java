// Fig. 23.9: Buffer.java
// Buffer interface specifies methods called by Producer and Consumer.
//La interfaz de búfer especifica métodos llamados por el Productor y el Consumidor.
import java.security.SecureRandom;

interface Buffer{
// place int value into Buffer
//colocar valor int en Buffer
	public void blockingPut(int value) throws InterruptedException;
// return int value from Buffer
//devuelve el valor int desde Buffer
	public int blockingGet() throws InterruptedException;
} // end interface Buffer

//Producer with a run method that inserts the values 1 to 10 in buffer.
//Productor con un método de ejecución que inserta los valores 1 a 10 en el búfer.

class Producer implements Runnable{
	
	private static final SecureRandom generator = new SecureRandom();
	private final Buffer sharedLocation; // reference to shared object
	                                     //referencia a objeto compartido
	//constructor
	public Producer(Buffer sharedLocation){
	this.sharedLocation = sharedLocation;
	}
	
	// store values from 1 to 10 in sharedLocation
	//almacena valores de 1 a 10 en sharedLocation
	public void run() {
		int sum = 0;
		for (int count = 1; count <= 10; count++) {
			try {// sleep 0 to 3 seconds, then place value in Buffer
				 //duerme de 0 a 3 segundos, luego coloca el valor en Buffer
				Thread.sleep(generator.nextInt(3000)); // random sleep
				sharedLocation.blockingPut(count); // set value in buffer/ valor en el buffer
				sum += count; // increment sum of values/ incrementar la suma de valores
				System.out.printf("\t%2d%n", sum);	
			}
			catch (InterruptedException exception) {
				Thread.currentThread().interrupt();
			}
		}
		System.out.printf("Producer done producing%nTerminating Producer%n");
	}
} // end class Producer






public class Prueba {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

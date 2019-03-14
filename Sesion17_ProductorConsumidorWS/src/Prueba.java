// Fig. 23.9: Buffer.java
// Buffer interface specifies methods called by Producer and Consumer.
//La interfaz de búfer especifica métodos llamados por el Productor y el Consumidor.
import java.security.SecureRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
		//System.out.printf("Producer done producing%nTerminating Producer%n");
		System.out.printf("Produccion terminada del Productor%n Terminando Productor%n");
	}
} // end class Producer

class Consumer implements Runnable{
	private static final SecureRandom generator = new SecureRandom();
	private final Buffer sharedLocation; // reference to shared object
	                                     //referencia a objeto compartido
	// constructor
	public Consumer(Buffer sharedLocation){
	this.sharedLocation = sharedLocation;
	}
	
	// read sharedLocation's value 10 times and sum the values
	//ea el valor de sharedLocation 10 veces y suma los valores
	public void run() {
		int sum = 0;
		for (int count = 1; count <= 10; count++){
			// sleep 0 to 3 seconds, read value from buffer and add to sum
			//duerme de 0 a 3 segundos, lee el valor del búfer y agrega a la suma
			try{
				Thread.sleep(generator.nextInt(3000));
				sum += sharedLocation.blockingGet();
				System.out.printf("\t\t\t%2d%n", sum);
			}
			catch (InterruptedException exception) {
				Thread.currentThread().interrupt();
			}
		}
		//System.out.printf("%n%s %d%n%s%n","Consumer read values totaling", sum, "Terminating Consumer");
		System.out.printf("%n%s %d%n%s%n","El total de valores que consumidor leyo", sum, "Terminando consumidor");
	}
} // end class Consumer

//UnsynchronizedBuffer maintains the shared integer that is accessed by
// a producer thread and a consumer thread.

//UnsynchronizedBuffer mantiene el número entero compartido al que se accede 
//mediante un hilo productor y un hilo consumidor.

class UnsynchronizedBuffer implements Buffer{
	private int buffer = -1; // shared by producer and consumer threads
	                         //compartido por los hilos productor y consumidor
	// place value into buffer
	//Pociciona un valor en el buffer
	public void blockingPut(int value) throws InterruptedException{
	//System.out.printf("Producer writes\t%2d", value);
		System.out.printf("El Productor escribio\t%2d", value);
	    buffer = value;
	}
	
	// return value from buffer
	//regresa un valor del buffer
	public int blockingGet() throws InterruptedException{
		//System.out.printf("Consumer reads\t%2d", buffer);
		System.out.printf("El consumidor leyo\t%2d", buffer);
		return buffer;
	}
}// end class UnsynchronizedBuffer


//Application with two threads manipulating an unsynchronized buffer.
//Aplicación con dos hilos manipulando un búfer no sincronizado.
             //SharedBufferTest
public class Prueba {

	public static void main(String[] args)throws InterruptedException {
		
		// create new thread pool with two threads
		// Crear un nuevo grupo de hilos con dos hilos.
		ExecutorService executorService = Executors.newCachedThreadPool();
		
		// create UnsynchronizedBuffer to store ints
		//crear un UnsynchronizedBuffer para almacenar entradas
		Buffer sharedLocation = new UnsynchronizedBuffer();
		
		//System.out.println( "Action\t\tValue\tSum of Produced\tSum of Consumed");
		System.out.println( "Accion       \t\tValor\tSum Producto\tSum Consumidor");
		System.out.printf("-------------\t\t------\t-------------\t---------------%n%n");
		// execute the Producer and Consumer, giving each
		//Ejecutar el productor y consumidor
		// access to the sharedLocation
		//accesa a la locacion compartida sharedLocation
		executorService.execute(new Producer(sharedLocation));
		executorService.execute(new Consumer(sharedLocation));
		executorService.shutdown(); // terminate app when tasks complete
		                            //termina la aplicacion cuando la tarea se completo
		executorService.awaitTermination(1, TimeUnit.MINUTES);
	}

}

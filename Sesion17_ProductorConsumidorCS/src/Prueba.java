import java.security.SecureRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
// Synchronizing access to shared mutable data using Object
// methods wait and notifyAll.
//Sincronización de acceso a datos mutables compartidos utilizando Object
//los métodos esperan y notifican a todos.

interface Buffer{
// place int value into Buffer
//colocar valor int en Buffer
	public void blockingPut(int value) throws InterruptedException;
// return int value from Buffer
//devuelve el valor int desde Buffer
	public int blockingGet() throws InterruptedException;
} // end interface Buffer

class SynchronizedBuffer implements Buffer{
	private int buffer = -1; // shared by producer and consumer threads
	private boolean occupied = false;
	
	// place value into buffer
	// colocar valor en el búfer
	
	public synchronized void blockingPut(int value) throws InterruptedException{
	// while there are no empty locations, place thread in waiting state
	// Si bien no hay ubicaciones vacías, coloque el hilo en estado de espera
		while (occupied) {
			// output thread information and buffer information, then wait
			// información de hilo de salida e información de búfer, luego espera
			System.out.println("Producer tries to write."); // for demo only
			displayState("Buffer full. Producer waits."); // for demo only
			wait();
		}
		buffer = value; // set new buffer value
		                //nuevo valor de buffer
		// indicate producer cannot store another value
		//indicar productor no puede almacenar otro valor
		// until consumer retrieves current buffer value
		// hasta que el consumidor recupere el valor actual del búfer
		occupied = true;
		
		displayState("Producer writes " + buffer); // for demo only
		notifyAll(); // tell waiting thread(s) to enter runnable state
		             // Dice a los hilos en espera que ingresen al estado ejecutable	
	} // end method blockingPut; releases lock on SynchronizedBuffer
	  // método blockingPut; libera bloqueo en SynchronizedBuffer
	
	// return value from buffer
	//retorna valor del buffer
	public synchronized int blockingGet() throws InterruptedException{
		// while no data to read, place thread in waiting state
		//Si bien no hay datos para leer, coloca el hilo en estado de espera
		
		while (!occupied) {
			// output thread information and buffer information, then wait
			//información de hilo de salida e información de búfer, luego espera
			System.out.println("Consumer tries to read."); // for demo only
			displayState("Buffer empty. Consumer waits."); // for demo only
			wait();
		}
		
		// indicate that producer can store another value
		// Indicar que productor puede almacenar otro valor.
		// because consumer just retrieved buffer value
		// porque el consumidor acaba de recuperar el valor del búfer
		occupied = false;
		displayState("Consumer reads " + buffer); // for demo only
		notifyAll(); // tell waiting thread(s) to enter runnable state
		             //Dile a los hilos en espera que ingresen al estado ejecutable
		return buffer;
	} // end method blockingGet; releases lock on SynchronizedBuffer
	  // end method blockingGet; libera bloqueo en SynchronizedBuffer
	
	// display current operation and buffer state; for demo only
	//mostrar la operación actual y el estado del búfer; solo para demostración
	
	private synchronized void displayState(String operation) {
		System.out.printf("%-40s%d\t\t%b%n%n", operation, buffer,occupied);
	}
}// end class SynchronizedBuffer

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
				//System.out.printf("\t\t\t%2d%n", sum);
			}
			catch (InterruptedException exception) {
				Thread.currentThread().interrupt();
			}
		}
		//System.out.printf("%n%s %d%n%s%n","Consumer read values totaling", sum, "Terminating Consumer");
		System.out.printf("%n%s %d%n%s%n","El total de valores que consumidor leyo", sum, "Terminando consumidor");
	}
} // end class Consumer


//PAGINA 990
public class Prueba {
	public static void main(String[] args) throws InterruptedException{
		// create a newCachedThreadPool
		//Crea un nuevo grupo de hilos en caché
		ExecutorService executorService = Executors.newCachedThreadPool();
		
		// create SynchronizedBuffer to store ints
		//Crear Búfer Sincronizado para almacenar entradas
		Buffer sharedLocation = new SynchronizedBuffer();
		
		System.out.printf("%-40s%s\t\t%s%n%-40s%s%n%n", "Operation","Buffer", "Occupied", "---------", "------\t\t--------");
		// execute the Producer and Consumer tasks
		// Ejecutar las tareas del productor y consumidor.
		executorService.execute(new Producer(sharedLocation));
		executorService.execute(new Consumer(sharedLocation));
		
		executorService.shutdown();
		executorService.awaitTermination(1, TimeUnit.MINUTES);
		
	}

}

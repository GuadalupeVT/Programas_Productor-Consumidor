import java.util.concurrent.ArrayBlockingQueue;
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
//Productor con un m�todo de ejecuci�n que inserta los valores 1 a 10 en el b�fer.

class BlockingBuffer implements Buffer{
	private final ArrayBlockingQueue<Integer> buffer; // shared buffer
	                                                  //buffer compartido
	public BlockingBuffer(){
		buffer = new ArrayBlockingQueue<Integer>(1);
	}
	
	// place value into buffer
	//pone el valor en el buffer
	public void blockingPut(int value) throws InterruptedException{
		buffer.put(value); // place value in buffer
		                   //pone el valor en el buffer 
		//System.out.printf("%s%2d\t%s%d%n", "Producer writes ", value,"Buffer cells occupied: ", buffer.size());
		System.out.printf("%s%2d\t%s%d%n", "Productor escribio", value,"Buffer cells ocupada: ", buffer.size());
	}
	
	// return value from buffer
	//regresa un valor del buffer
	public int blockingGet() throws InterruptedException{
		int readValue = buffer.take(); // remove value from buffer
		                               // remover valores del buffer
		//System.out.printf("%s %2d\t%s%d%n", "Consumer reads ",readValue, "Buffer cells occupied: ", buffer.size());
		System.out.printf("%s %2d\t%s%d%n", "Consumidor leyo",readValue, "Buffer cells ocupada: ", buffer.size());
		return readValue;
	}
} // end class BlockingBuffer


public class Prueba {
	public static void main(String[] args) {
		// create new thread pool with two threads
		//Crear un nuevo grupo de hilos con dos hilos.
		ExecutorService executorService = Executors.newCachedThreadPool();
		
		// create BlockingBuffer to store ints
		//crear BlockingBuffer para almacenar entradas
		Buffer sharedLocation = new BlockingBuffer();
		
		executorService.execute(new Producer(sharedLocation));
		executorService.execute(new Consumer(sharedLocation));
		
		executorService.shutdown();
		executorService.awaitTermination(1, TimeUnit.MINUTES);
	}
} // end class BlockingBufferTest

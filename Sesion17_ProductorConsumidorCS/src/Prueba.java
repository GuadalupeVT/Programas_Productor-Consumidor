
// Synchronizing access to shared mutable data using Object
// methods wait and notifyAll.
//Sincronizaci�n de acceso a datos mutables compartidos utilizando Object
//los m�todos esperan y notifican a todos.

class SynchronizedBuffer implements Buffer{
	private int buffer = -1; // shared by producer and consumer threads
	private boolean occupied = false;
	
	// place value into buffer
	// colocar valor en el b�fer
	
	public synchronized void blockingPut(int value) throws InterruptedException{
	// while there are no empty locations, place thread in waiting state
	// Si bien no hay ubicaciones vac�as, coloque el hilo en estado de espera
		while (occupied) {
			// output thread information and buffer information, then wait
			// informaci�n de hilo de salida e informaci�n de b�fer, luego espera
			System.out.println("Producer tries to write."); // for demo only
			displayState("Buffer full. Producer waits."); // for demo only
			wait();
		}
		buffer = value; // set new buffer value
		                //nuevo valor de buffer
		// indicate producer cannot store another value
		//indicar productor no puede almacenar otro valor
		// until consumer retrieves current buffer value
		// hasta que el consumidor recupere el valor actual del b�fer
		occupied = true;
		
		displayState("Producer writes " + buffer); // for demo only
		notifyAll(); // tell waiting thread(s) to enter runnable state
		             // Dice a los hilos en espera que ingresen al estado ejecutable	
	} // end method blockingPut; releases lock on SynchronizedBuffer
	  // m�todo blockingPut; libera bloqueo en SynchronizedBuffer
	
	// return value from buffer
	//retorna valor del buffer
	public synchronized int blockingGet() throws InterruptedException{
		// while no data to read, place thread in waiting state
		//Si bien no hay datos para leer, coloca el hilo en estado de espera
		
		while (!occupied) {
			// output thread information and buffer information, then wait
			//informaci�n de hilo de salida e informaci�n de b�fer, luego espera
			System.out.println("Consumer tries to read."); // for demo only
			displayState("Buffer empty. Consumer waits."); // for demo only
			wait();
		}
		
		// indicate that producer can store another value
		// Indicar que productor puede almacenar otro valor.
		// because consumer just retrieved buffer value
		// porque el consumidor acaba de recuperar el valor del b�fer
		occupied = false;
		displayState("Consumer reads " + buffer); // for demo only
		notifyAll(); // tell waiting thread(s) to enter runnable state
		             //Dile a los hilos en espera que ingresen al estado ejecutable
		return buffer;
	} // end method blockingGet; releases lock on SynchronizedBuffer
	  // end method blockingGet; libera bloqueo en SynchronizedBuffer
	
	// display current operation and buffer state; for demo only
	//mostrar la operaci�n actual y el estado del b�fer; solo para demostraci�n
	
	private synchronized void displayState(String operation) {
		System.out.printf("%-40s%d\t\t%b%n%n", operation, buffer,occupied);
	}
}// end class SynchronizedBuffer


public class Prueba {

}

// Fig. 23.9: Buffer.java
// Buffer interface specifies methods called by Producer and Consumer.
//La interfaz de búfer especifica métodos llamados por el Productor y el Consumidor.
interface Buffer{
// place int value into Buffer
//colocar valor int en Buffer
	public void blockingPut(int value) throws InterruptedException;
// return int value from Buffer
//devuelve el valor int desde Buffer
	public int blockingGet() throws InterruptedException;
} // end interface Buffer






public class Prueba {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

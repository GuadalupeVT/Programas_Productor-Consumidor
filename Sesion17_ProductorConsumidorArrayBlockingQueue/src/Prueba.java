
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




public class Prueba {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

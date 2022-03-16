import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class tcp1cli {

	private static String IP_ADDR;
	private static int puerto;

	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			System.err.println("Error, el formato debe ser:\njava tcp1cli <ip_adress> <port_numer>");
			return;
		}
		IP_ADDR = args[0];
		puerto = Integer.parseInt(args[1]);
		Scanner sc = new Scanner(System.in);
		Socket socket = new Socket(IP_ADDR, puerto);
		boolean loop = true;
		while (loop) {
			System.out.println("Introduzca una cadena de enteros separados por espacios:");
			String linea = sc.nextLine();
			String cadena = "" + linea;
			if (cadena.startsWith("0")) {
				socket.close();
				sc.close();
				loop = false;
			} else {

				try {
					DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
					dos.writeUTF(cadena);
					DataInputStream dis = new DataInputStream(socket.getInputStream());
					int acum = dis.readInt();
					System.out.println("Acumulador= " + acum);
				} catch (IOException e) {

				}
			}
		}
		return;
	}
}

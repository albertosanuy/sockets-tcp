import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class tcp1ser {

	private static int acul;
	private static int puerto;

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.err.println("Error, el formato debe ser:\njava tcp1ser <port_numer>");
			return;
		}
		puerto = Integer.parseInt(args[0]);
		ServerSocket socket = new ServerSocket(puerto);
		while (true) {
			Socket saux = socket.accept();
			acul = 0;
			try {
			while (true) {
				DataInputStream dis = new DataInputStream(saux.getInputStream());
				String cadena = dis.readUTF();
				String[] palabras = cadena.split(" ");
				int[] result = new int[palabras.length];
				for (int i = 0; i < palabras.length; i++) {
					result[i] = Integer.parseInt(palabras[i].trim());
					acul = acul + result[i];
					System.out.println("Acumulador= " + acul);
				}
				DataOutputStream dos = new DataOutputStream(saux.getOutputStream());
				dos.writeInt(acul);
			}
			} catch (IOException e) {
				continue;
			}
		}
	}
}

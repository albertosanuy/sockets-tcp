import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class tcp2ser {

	private static int puerto;
	

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.err.println("Error, el formato debe ser:\njava tcp2ser <port_numer>");
			return;
		}
		puerto = Integer.parseInt(args[0]);
		ServerSocket socket = new ServerSocket(puerto);
		while (true) {
			Socket saux = socket.accept();
			ProcesoHijo pH = new ProcesoHijo(saux);
			pH.start();
		}
	}
	
	public static class ProcesoHijo extends Thread {
		Socket saux2;
		ProcesoHijo (Socket saux) {
			saux2 = saux;
		}
		public void run () {
			int acul = 0;
			while (true) {
				try {
				DataInputStream dis = new DataInputStream(saux2.getInputStream());
				String cadena = dis.readUTF();
				String[] palabras = cadena.split(" ");
				int[] result = new int[palabras.length];
				for (int i = 0; i < palabras.length; i++) {
					result[i] = Integer.parseInt(palabras[i].trim());
					acul = acul + result[i];
					System.out.println("Acumulador= " + acul);
				}
				DataOutputStream dos = new DataOutputStream(saux2.getOutputStream());
				dos.writeInt(acul);
				} catch (IOException e) {
					continue;
				}
			}
		}
	}
}

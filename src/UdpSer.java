
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpSer {

	private static int acul = 0;
	private static int puerto;
	public static void main (String[] args) throws IOException {
		if (args.length != 1) {
			System.err.println("Error, el formato debe ser:\njava UdpSer <port_numer>");
			return;
		}
		puerto = Integer.parseInt(args[0]);
		DatagramSocket socket = new DatagramSocket(puerto);
		while (true) {
			byte[] solicitud = new byte[128];
			DatagramPacket responsePacket = new DatagramPacket(solicitud,solicitud.length);
			socket.receive(responsePacket);
			InetAddress direccion_cliente = responsePacket.getAddress();
			int puerto_cliente = responsePacket.getPort();
			String cadena = new String (solicitud);
			String[] palabras = cadena.split(" ");
			int[] result = new int[palabras.length];
			   for (int i = 0; i < palabras.length; i++) {
			      result[i] = Integer.parseInt(palabras[i].trim());
			      acul = acul + result[i];
			      System.out.println("Acumulador= " +acul);
			   }
			String acul_respuesta = String.valueOf(acul);
			byte[] mensaje = acul_respuesta.getBytes();
			DatagramPacket packet = new DatagramPacket(mensaje, mensaje.length, direccion_cliente, puerto_cliente);
			socket.send(packet);
		}
	}
}

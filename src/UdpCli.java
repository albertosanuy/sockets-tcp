
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class UdpCli {
	private static String IP_ADDR;
	private static int puerto;

	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			System.err.println("Error, el formato debe ser:\njava UdpCli <ip_adress> <port_numer>");
			return;
		}
		IP_ADDR = args[0];
		puerto = Integer.parseInt(args[1]);
		DatagramSocket socket = new DatagramSocket();
		System.out.println("Introduzca una cadena de enteros separados por espacios:");
		Scanner sc = new Scanner(System.in);
		String linea = sc.nextLine();
		String cadena = "" + linea;
		byte[] mensaje = cadena.trim().getBytes();
		socket.setSoTimeout(10000);
		try {
		DatagramPacket packet = new DatagramPacket(mensaje, mensaje.length, InetAddress.getByName(IP_ADDR), puerto);
		socket.send(packet);
		byte[] respuesta = new byte[128];
		DatagramPacket responsePacket = new DatagramPacket(respuesta, respuesta.length);
		socket.receive(responsePacket);
		String resp = new String(respuesta);
		System.out.println("Acumulador= " + Integer.parseInt(resp.trim()));
		} catch (SocketTimeoutException s) {
	        System.out.println("No ha habido respuesta del servidor en 10 segundos");
			socket.close();
			sc.close();
	        return;
	      }
		socket.close();
		sc.close();
		return;
	}
}
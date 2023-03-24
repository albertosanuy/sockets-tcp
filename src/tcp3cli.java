import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.util.Scanner;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;

public class tcp3cli {

	private static String IP_ADDR;
	private static int puerto;

	public static void main(String[] args) throws IOException {
		if (args.length == 3) {
			if (!(args[2].equals("-u"))) {
				System.err.println("Error, el formato debe ser:\njava tcp3cli <ip_adress> <port_numer> [-u]");
				return;
			}
			IP_ADDR = args[0];
			puerto = Integer.parseInt(args[1]);
			DatagramChannel dc = DatagramChannel.open();
			InetSocketAddress dir = new InetSocketAddress(InetAddress.getByName(IP_ADDR), puerto);
			System.out.println("Introduzca un número:");
			Scanner sc = new Scanner(System.in);
			String linea = sc.nextLine();
			String cadena = "" + linea;
			int num = Integer.parseInt(cadena.trim());
			try {
				ByteBuffer buff = ByteBuffer.allocate(48);
				ByteBuffer buffout = ByteBuffer.allocate(48);
				buff.order(ByteOrder.BIG_ENDIAN);
				buff.clear();
				buff.putInt(num);
				buff.flip();
				dc.send(buff, dir);
				buff.clear();
				dc.receive(buffout);
				buffout.flip();
				while (buffout.hasRemaining()) {
					int acum = buffout.getInt();
					System.out.println("Acumulador= " + acum);
				}
			} catch (SocketTimeoutException s) {
				System.out.println("No ha habido respuesta del servidor en 10 segundos");
				dc.close();
				sc.close();
				return;
			}
			dc.close();
			sc.close();
			return;
		}
		if (args.length == 2) {
			IP_ADDR = args[0];
			puerto = Integer.parseInt(args[1]);
			Scanner sc = new Scanner(System.in);
			SocketChannel sch = SocketChannel.open(new InetSocketAddress(InetAddress.getByName(IP_ADDR), puerto));
			boolean loop = true;
			while (loop) {
				System.out.println("Introduzca un número:");
				String linea = sc.nextLine();
				String cadena = "" + linea;
				if (cadena.startsWith("0")) {
					sch.close();
					sc.close();
					loop = false;
				} else {
					try {
						ByteBuffer buff = ByteBuffer.allocate(48);
						int num = Integer.parseInt(cadena.trim());
						buff.clear();
						buff.putInt(num);
						buff.flip();
						sch.write(buff);
						buff.clear();
						sch.read(buff);
						buff.flip();
						int acul = buff.getInt();
						System.out.println("Acumulador= " + acul);
					} catch (IOException e) {
					}
				}
			}
			return;
		} else {
			System.err.println("Error, el formato debe ser:\njava tcp3cli <ip_adress> <port_numer> [-u]");
			return;
		}
	}
}
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class tcp3ser {

	private static int puerto;
	private static int aculudp = 0;

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.err.println("Error, el formato debe ser:\njava tcp3ser <port_numer>");
			return;
		}
		puerto = Integer.parseInt(args[0]);
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.socket().bind(new InetSocketAddress(puerto));
		DatagramChannel dc = DatagramChannel.open();
		dc.socket().bind(new InetSocketAddress(puerto));
		Selector selector = Selector.open();
		ssc.configureBlocking(false);
		dc.configureBlocking(false);
		ssc.register(selector, SelectionKey.OP_ACCEPT);
		dc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
		SocketChannel sc = null;

		while (true) {
			try {
				int readychannels = selector.select();
				if (readychannels == 0) {
					continue;
				}
				Set<SelectionKey> selectedKeys = selector.selectedKeys();

				Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

				while (keyIterator.hasNext()) {

					SelectionKey key = keyIterator.next();
					keyIterator.remove();
					if (!key.isValid()) {
						continue;
					}
					if (key.isAcceptable()) {
						ssc = (ServerSocketChannel) key.channel();
						sc = ssc.accept();
						sc.configureBlocking(false);
						sc.register(selector, SelectionKey.OP_READ, 0);
					} else if (key.isReadable()) {
						try {
							dc = (DatagramChannel) key.channel();
							if (dc != null) {
								ByteBuffer buffudp = ByteBuffer.allocate(48);
								ByteBuffer buffudpout = ByteBuffer.allocate(48);
								buffudp.clear();
								SocketAddress origen = dc.receive(buffudp);
								buffudp.flip();
								int leidoudp = buffudp.getInt();
								buffudp.clear();
								buffudp.flip();
								aculudp += leidoudp;
								System.out.println("Acumulador= " + aculudp);
								buffudp.clear();
								buffudpout.clear();
								buffudpout.putInt(aculudp);
								buffudpout.flip();
								while (buffudpout.hasRemaining()) {
									dc.send(buffudpout, origen);
								}
								continue;
							}
						} catch (Exception e) {

						}

						sc = (SocketChannel) key.channel();
						if (sc != null) {
							int attach = (Integer) key.attachment();
							// while (true) {
							ByteBuffer buff = ByteBuffer.allocate(48);
							int numread = sc.read(buff);
							if (numread < 0) {
								continue;
							}
							buff.flip();
							int leido = buff.getInt();
							buff.clear();
							attach += leido;
							System.out.println("Acumulador= " + attach);

							buff.clear();
							buff.putInt(attach);
							buff.flip();
							sc.write(buff);
							key.attach(attach);
						}
					}

				}
			} catch (IOException e) {
				sc.close();
				continue;
			}
		}

	}
}
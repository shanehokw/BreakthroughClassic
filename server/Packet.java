import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Packet
{
	private int type;
	private ArrayList<Object> values = new ArrayList<Object>();
	
	/**
	 * Creates a minimal packet with no content, only a type
	 * @param packetType The ID of the packet to send
	 */
	public Packet(int packetType) {
		this.type = packetType;
	}
	
	/**
	 * Creates a simple Packet object with a typeID and a UTF message.
	 * @param packetType The ID of the packet to send
	 * @param value The value of the packet content 
	 */
	public Packet(int packetType, String value) {
		this.type = packetType;
		this.values.add(value);
	}
	
	/**
	 * Creates a complex Packet object with potentially multiple properties, possibly
	 * of varying data types.
	 * @param packetType The ID of the packet to send
	 * @param values An object-generic mixed-type values ArrayList of packet data to send
	 */
	public Packet(int packetType, ArrayList<Object> values) {
		this.type = packetType;
		this.values = values;
	}
	
	/**
	 * Sends the contents of the packet to the client using the client's DataOutputStream.
	 * @throws IOException
	 */
	public void send(DataOutputStream clientOut) throws IOException {
		
		/* Synchronize on the DataOutputStream of the client-handling thread
		This will prevent packets being written concurrently and confusing the client */
		synchronized(clientOut) {
			
			// Send the type
			clientOut.write(type);
			
			// Send each packet property value to the client
			for (Object value: values) {
				
				System.out.println(value);
				
				// Use the appropriate "writeX" method depending on the dataType
				if(value instanceof String) {
					clientOut.writeUTF((String) value);
				}
				else if(value instanceof Integer) {
					clientOut.writeInt((Integer) value);
				}
				else if(value instanceof Double) {
					clientOut.writeDouble((Double) value);
				}
				
				// Flush the output stream, sending the packet
				clientOut.flush();
			}
		}
	}
	
	/**
	 * Sends the packet to multiple clients.
	 * @param clientOut An array of the DataOutputStreams for every recipient of this packet.
	 * @throws IOException
	 */
	public void send(DataOutputStream[] clientOut) throws IOException {
		
		// Send the packet to each person provided
		for(DataOutputStream recipient: clientOut) {
			send(recipient);
		}
	}
}

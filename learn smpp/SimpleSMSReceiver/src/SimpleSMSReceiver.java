import org.smpp.Data;
import org.smpp.Session;
import org.smpp.TCPIPConnection;
import org.smpp.pdu.BindReceiver;
import org.smpp.pdu.BindRequest;
import org.smpp.pdu.BindResponse;
import org.smpp.pdu.DeliverSM;
import org.smpp.pdu.PDU;
import org.smpp.pdu.tlv.TLV;
import org.smpp.util.ByteBuffer;

public class SimpleSMSReceiver {
	
	/** 
	 * Parameters used for connecting to SMSC (or SMPPSim)
	 */
	private Session session = null;
	private String ipAddress = "10.70.105.89"; //dia chi IP cua may chu
//	private String ipAddress = "10.80.231.147";
	private String systemId = "smppclient1"; //user name
	private String password = "password"; //pass
	private int port = 2775; //cong ket noi nt sms
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Sms receiver starts");
		
		SimpleSMSReceiver objSimpleSMSReceiver = new SimpleSMSReceiver();
		objSimpleSMSReceiver.bindToSmsc();
		
		while(true) {
			objSimpleSMSReceiver.receiveSms();
		}
	}

	private void bindToSmsc() {
		try {
			// setup connection
			TCPIPConnection connection = new TCPIPConnection(ipAddress, port);
			connection.setReceiveTimeout(20 * 1000);
			session = new Session(connection);

			// set request parameters
			BindRequest request = new BindReceiver();
			request.setSystemId(systemId);
			request.setPassword(password);

			// send request to bind
			BindResponse response = session.bind(request);
			if (response.getCommandStatus() == Data.ESME_ROK) {
				System.out.println("Sms receiver is connected to Server.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void receiveSms() {
		try {

			PDU pdu = session.receive(1500);
			
			if ((pdu != null) && (pdu instanceof DeliverSM))  {
				DeliverSM sms = (DeliverSM) pdu;
//				byte[] smsPart = sms.getShortMessageData().getBuffer();
//				ByteBuffer byteBuffer = new ByteBuffer();
//				byteBuffer.appendBytes(sms.getShortMessage().getBytes());
				
				if ((int)sms.getDataCoding() == 0 ) {
					//message content is English
					System.out.println("***** New Message Received *****");
					System.out.println("From: " + sms.getSourceAddr().getAddress());
					System.out.println("To: " + sms.getDestAddr().getAddress());
					System.out.println("Content: " + sms.getShortMessage(Data.ENC_UTF8));
//					System.out.println("content: "+ new String(sms.getShortMessage()));
				
				} else if((int)sms.getDataCoding() == 8) {
					// non English
					System.out.println("***** New Message non English Received *****");
					System.out.println("From: " + sms.getSourceAddr().getAddress());
					System.out.println("To: " + sms.getDestAddr().getAddress());
					System.out.println("Content: " + sms.getShortMessage(Data.ENC_UTF16));
//					System.out.println("Content: "+ sms.getShortMessageData().getBuffer());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

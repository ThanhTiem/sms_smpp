import org.smpp.Connection;
import org.smpp.Data;
import org.smpp.Session;
import org.smpp.TCPIPConnection;
import org.smpp.pdu.Address;
import org.smpp.pdu.BindRequest;
import org.smpp.pdu.BindTransmitter;
import org.smpp.pdu.BindTransmitterResp;
import org.smpp.pdu.SubmitSM;
import org.smpp.pdu.SubmitSMResp;
import org.smpp.util.ByteBuffer;

public class SimpleSMSTransmitter {

	/**
	 * @param args
	 */
	private Session session = null;
//	private String ipAddress = "127.0.0.1";
	private String ipAddress = "10.70.105.89";
//	private String ipAddress = "10.80.231.147";
	private String systemId = "smppclient1";
	private String password = "password";
	private int port = 2775;
//	private String shortMessage = "le thanh tiem";
	private String shortMessage = "dabdabdjabjdbajdajdbjabdnabchjnc dsacbnad cdavchdnac hadbc nasmbcjhansmbc mabdcnab chadbc nambchdsc nd chdvcha jahjdabdabdjabjdbajdajdbjabdnabchjnc dsacbnad cdavchdnac hadbc nasmbcjhansmbc mabdcnab chadbc nambchdsc nd chdvcha kjaskjaskjaksjaskkjajbhvba1234";
//	private String shortMessage = "dabdabdjabjdbajdajdbjabdnabchjnc dsacbnad cdavchdnac hadbc nasmbcjhansmbc mabdcnab chadbc nambchdsc nd chdvcha jahjdabdabdjabjdbajdajdbjabdnabchjnc dsacbnad cdavchdnac hadbc nasmbcjhansmbc mabdcnab chadbc nambchdsc nd chdvcha kjaskjaskjaksjaskkjajbhvba1234 dabdabdjabjdbajdajdbjabdnabchjnc dsacbnad cdavchdnac hadbc nasmbcjhansmbc mabdcnab chadbc nambchdsc nd chdvcha jahjdabdabdjabjdbajdajdbjabdnabchjnc dsacbnad cdavchdnac hadbc nasmbcjhansmbc mabdcnab chadbc nambchdsc nd chdvcha kjaskjaskjaksjaskkjajbhvba1234";
	private String sourceAddress = "121212";
	private String destinationAddress = "12345678900";

	public static void main(String[] args) {

		SimpleSMSTransmitter objSimpleSMSTransmitter = new SimpleSMSTransmitter();
		objSimpleSMSTransmitter.bindToSMSC();
		objSimpleSMSTransmitter.sendSingleSMS();

		System.out.println("Program terminated");
		System.exit(0);
	}

	public void bindToSMSC() {
		try {
			Connection conn = new TCPIPConnection(ipAddress, port);
			session = new Session(conn);

			BindRequest breq = new BindTransmitter();
			breq.setSystemId(systemId);
			breq.setPassword(password);
			BindTransmitterResp bresp = (BindTransmitterResp) session.bind(breq);
			
			if(bresp.getCommandStatus() == Data.ESME_ROK) {
				System.out.println("Connected to SMSC");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendSingleSMS() {
		try {
			SubmitSM request = new SubmitSM();
			Address srcAddr = new Address();
			Address desAddr = new Address();
			ByteBuffer msg = new ByteBuffer();
			msg.setBuffer(shortMessage.getBytes());

			// set values
			srcAddr.setTon((byte) 1);
			srcAddr.setNpi((byte) 1);
			srcAddr.setAddress(sourceAddress);
			
			desAddr.setTon((byte) 1);
			desAddr.setNpi((byte) 1);
			desAddr.setAddress(destinationAddress);
			
			request.setSourceAddr(srcAddr);
			request.setDestAddr(desAddr);
//			request.setShortMessage(shortMessage);
			request.setExtraOptional((short) 0x0424, msg);
			request.setMessagePayload(msg);
			// send the request
			SubmitSMResp resp = session.submit(request);

			if (resp.getCommandStatus() == Data.ESME_ROK) {
				System.out.println("Message submitted....");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to submit message....");
		}
	}
}

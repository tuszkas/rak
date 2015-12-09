//SMSProcessor.java
package main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import com.ericsson.hosasdk.api.TpAddress;
import com.ericsson.hosasdk.api.TpAddressPlan;
import com.ericsson.hosasdk.api.TpAddressRange;
import com.ericsson.hosasdk.api.TpAddressPresentation;
import com.ericsson.hosasdk.api.TpAddressScreening;
import com.ericsson.hosasdk.api.TpHosaDeliveryTime;
import com.ericsson.hosasdk.api.TpHosaMessage;
import com.ericsson.hosasdk.api.TpHosaSendMessageError;
import com.ericsson.hosasdk.api.TpHosaSendMessageReport;
import com.ericsson.hosasdk.api.TpHosaTerminatingAddressList;
import com.ericsson.hosasdk.api.TpHosaUIMessageDeliveryType;
import com.ericsson.hosasdk.api.hui.IpAppHosaUIManager;
import com.ericsson.hosasdk.api.hui.IpAppHosaUIManagerAdapter;
import com.ericsson.hosasdk.api.hui.IpHosaUIManager;
import com.ericsson.hosasdk.api.ui.IpAppUI;
import com.ericsson.hosasdk.api.ui.P_UI_RESPONSE_REQUIRED;
import com.ericsson.hosasdk.api.ui.TpUIEventCriteria;
import com.ericsson.hosasdk.api.ui.TpUIEventInfo;
import com.ericsson.hosasdk.api.ui.TpUIEventNotificationInfo;
import com.ericsson.hosasdk.api.ui.TpUIIdentifier;

/**
 * Klasa odpowiedzialna za
 * <ul>
 * <li>Wysyłanie SMSów.</li>
 * <li>Obsługę przekazanie przychodzących SMSów do klasy Informer</li>
 * </ul>
 */
public class SMSProcessor extends IpAppHosaUIManagerAdapter implements
		IpAppHosaUIManager {
	private static final String WORKER_NUMBER = "11111";
	private IpHosaUIManager itsHosaUIManager;
	private ConfigurationLoader parent;
	private OrderManager orderManager = new OrderManager();

	/**
	 * @param aHosaUIManager
	 *            menadŜer słuŜacy do komunikacji z Ericsson Network Resource
	 *            Gateway
	 * @param aParent
	 *            Referencja do klasy-rodzica Informer, która przekazuje klasie
	 *            SMSProcessor wiadomości do wysłania i przetwarza odbierane
	 *            wiadomości
	 */
	public SMSProcessor(IpHosaUIManager aHosaUIManager, ConfigurationLoader aParent) {
		itsHosaUIManager = aHosaUIManager;
		this.parent = parent;
	}

	/**
	 * Funkcja dodaje nasłuch przychodzacych wiadomości dla podanego numeru
	 * (uŜytkownik o tym numerze nie musi istnieć, moŜe to być numer usługi)
	 * 
	 * @param sDest
	 *            Adres (numer) uŜytkownika, dla którego utworzona jest
	 *            notyfikacja
	 * @return przydzielone ID notyfikacji
	 */
	public int startNotifications(String sDest) {
		// interfejs obsługujący zdarzenia pochodzące od bramki, w tym wypadku
		// implementuje go klasa SMSProcessor
		IpAppHosaUIManager appHosaUIManager = this;
		// obiekt reprezentujący adres nadawcy wiadomości - w tym wypadku brak
		// nadawcy
		TpAddressRange originatingAddress = new TpAddressRange(
				TpAddressPlan.P_ADDRESS_PLAN_UNDEFINED, "*", "", "");

		// obiekt reprezentujący adres nadawcy wiadomości - w tym wypadku
		// dowolny nadawca
		TpAddressRange destinationAddress = new TpAddressRange(
				TpAddressPlan.P_ADDRESS_PLAN_E164, sDest, "", "");
		// kod usługi - 00 oznacza wiadomości SMS (np. dla MMS kod - 01)
		String serviceCode = "00";

		// kryterium uruchomienia obsługi zdarzenia - przyszła wiadomość SMS na
		// numer destinationAddress od dowolnego numeru
		TpUIEventCriteria criteria = new TpUIEventCriteria(originatingAddress,
				destinationAddress, serviceCode);

		// utworzenie powiadamiania - przekazanie kryteriów i klasy
		// implementującej interfejs obsługi zdarzeń
		int assignmentId = itsHosaUIManager.createNotification(
				appHosaUIManager, criteria);
		return assignmentId;
	}

	/**
	 * Usunięcie notyfikacji
	 * 
	 * @param anAssignmentId
	 *            ID notyfikacji do usunięcia (zwróceone przez
	 * @see messaging.MMSProcessor#startNotifications(String) startNotifications
	 *      )
	 */
	public void stopNotifications(int anAssignmentId) {
		itsHosaUIManager.destroyNotification(anAssignmentId);
	}

	/**
	 * Wywoływana przez Ericsson Network Resource Gateway w chwili otrzymania
	 * wiadomości
	 *
	 * @see com.ericsson.hosasdk.api.hui.IpAppHosaUIManager
	 */
	public IpAppUI reportEventNotification(TpUIIdentifier anUserInteraction,
			TpUIEventNotificationInfo anEventInfo, int anAssignmentID) {

		String sender = anEventInfo.OriginatingAddress.AddrString;
		String receiver = anEventInfo.DestinationAddress.AddrString;
		String messageContent = new String(anEventInfo.UIEventData);
		// odesłanie wiadomości ,,Witaj świecie'' do nadawcy
		this.sendSMS(receiver, sender, "Witaj swiecie");
		return null;
	}

	/**
	 * Wywoływana przez Ericsson Network Resource Gateway w chwili otrzymania
	 * wiadomości
	 *
	 * @see com.ericsson.hosasdk.api.hui.IpAppHosaUIManager
	 * @deprecated Od wersji standardu 6.0 funkcja nie będzie wspierana, zamiast
	 *             niej naleŜy uŜywać * @see
	 *             messaging.MMSProcessor#reportEventNotification
	 *             (TpUIIdentifier, TpUIEventNotificationInfo,int)
	 *             reportEventNotification . Dla zapewnienia zdgoności wstecznej
	 *             została zaimplementowana.
	 */
	public IpAppUI reportNotification(TpUIIdentifier anUserInteraction,
			TpUIEventInfo anEventInfo, int anAssignmentID) {
		// pobranie danych zdarzenia - nadawcy, odbiorcy i treści wiadomości
		String sender = anEventInfo.OriginatingAddress.AddrString;
		String receiver = anEventInfo.DestinationAddress.AddrString;
		String messageContent = anEventInfo.DataString;
		if (sender.equals(WORKER_NUMBER)) {
			if (messageContent.startsWith("dodaj")) {
				Menu.addItem(messageContent);
				this.sendSMS(receiver, sender, "dodano");
			} else if(messageContent.equals("lista")) {
				StringBuilder builder = new StringBuilder("");
				List<Order> orders = orderManager.getOrders();
				for(Order order : orders) {
					builder.append(order);
					builder.append(" ");
				}
				this.sendSMS(receiver, sender, builder.toString());
			}
		}
		else if (messageContent.equals("menu")) {
			String menu = Menu.getMenu();
			this.sendSMS(receiver, sender, menu);
		} else if (messageContent.startsWith("zamow")) {
			orderManager.addOrder(messageContent, sender);
			this.sendSMS(receiver, sender, "Zarejestrowano");
			this.sendSMS(WORKER_NUMBER, WORKER_NUMBER, "Nowe zamowienie");

		} else {
			this.sendSMS(receiver, sender, "nieznane polecenie");
		}
		return null;
	}

	

	/**
	 * Wysłanie SMSa
	 *
	 * @param aSender
	 *            nadawca
	 * @param aReceiver
	 *            odbiorca
	 * @param aMessageContent
	 *            zawartość
	 */
	public void sendSMS(String aSender, String aReceiver, String aMessageContent) {
		IpAppHosaUIManager appHosaUIManager = this;
		// typ wiadomości: SMS
		TpHosaUIMessageDeliveryType deliveryType = TpHosaUIMessageDeliveryType.P_HUI_SMS;
		// Bez opŜónienia
		TpHosaDeliveryTime deliveryTime = new TpHosaDeliveryTime();
		deliveryTime.Dummy((short) 0);
		// adres nadawcy
		TpAddress originatingAddress = createTpAddress(aSender);
		// adres odbiorcy
		TpAddress destinationAddress = createTpAddress(aReceiver);
		// lista odbiorców, w tym wypadku zawierająca tylko jeden adres
		TpHosaTerminatingAddressList recipients = new TpHosaTerminatingAddressList();
		recipients.ToAddressList = new TpAddress[] { destinationAddress };
		// treść wiadomości
		TpHosaMessage message = new TpHosaMessage();
		message.Text(aMessageContent);
		// zaŜądanie wysłania wiadomości
		itsHosaUIManager.hosaSendMessageReq(appHosaUIManager, // callback
				originatingAddress, // nadawca
				recipients, // odbiorcy
				null, // bez tematu
				message, // treść wiadomości
				deliveryType, // rodzaj wiadomości (SMS)
				null, P_UI_RESPONSE_REQUIRED.value, false, deliveryTime, "");
	}

	/**
	 * Wywoływana przez Ericsson Network Resource Gateway, gdy wystąpił bład
	 * przy wysyłaniu wiadomości
	 *
	 * @see com.ericsson.hosasdk.api.hui.IpAppHosaUIManager
	 */
	public void hosaSendMessageErr(int anAssignmentID,
			TpHosaSendMessageError[] anErrorList) {
		System.out.println("\nError sending the SMS to "
				+ anErrorList[0].UserAddress.AddrString + "(ErrorCode "
				+ anErrorList[0].Error.value() + ")");
	}

	/**
	 * Wywoływana przez Ericsson Network Resource Gateway, gdy wysyłanie
	 * wiadomości zakończyło się poprawnie
	 *
	 * @see com.ericsson.hosasdk.api.hui.IpAppHosaUIManager
	 */
	public void hosaSendMessageRes(int anAssignmentID,
			TpHosaSendMessageReport[] aResponseList) {
		System.out.println("\nSMS Message sent to "
				+ aResponseList[0].UserAddress.AddrString);
	}

	public static TpAddress createTpAddress(String aNumber) {
		return new TpAddress(TpAddressPlan.P_ADDRESS_PLAN_E164,
				aNumber, // addres
				"", // name
				TpAddressPresentation.P_ADDRESS_PRESENTATION_UNDEFINED,
				TpAddressScreening.P_ADDRESS_SCREENING_UNDEFINED, "");// podadres
	}
}
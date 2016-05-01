package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients;

import com.bitdubai.fermat_api.layer.osa_android.location_system.Location;
import com.bitdubai.fermat_api.layer.osa_android.location_system.LocationSource;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.request.NearNodeListMsgRequest;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.MessageTransmitRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.MsgRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.NearNodeListMsgRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.ns.Message;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.util.DistanceCalculator;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.util.GsonProvider;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.HeadersAttName;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.MessageContentType;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.caches.ClientsSessionMemoryCache;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.NodesCatalog;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.websocket.EncodeException;
import javax.websocket.Session;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.MessageTransmitProcessor</code>
 * process all packages received the type <code>PackageType.MESSAGE_TRANSMIT</code><p/>
 *
 * Created by Roberto Requena - (rart3001@gmail.com) on 30/04/16.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class MessageTransmitProcessor extends PackageProcessor {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(MessageTransmitProcessor.class));

    /**
     * Represent the clientsSessionMemoryCache instance
     */
    private ClientsSessionMemoryCache clientsSessionMemoryCache;

    /**
     * Constructor whit parameter
     *
     * @param fermatWebSocketChannelEndpoint register
     */
    public MessageTransmitProcessor(FermatWebSocketChannelEndpoint fermatWebSocketChannelEndpoint) {
        super(fermatWebSocketChannelEndpoint, PackageType.MESSAGE_TRANSMIT);
        this.clientsSessionMemoryCache = ClientsSessionMemoryCache.getInstance();
    }

    /**
     * (non-javadoc)
     * @see PackageProcessor#processingPackage(Session, Package)
     */
    @Override
    public void processingPackage(Session session, Package packageReceived) {

        LOG.info("Processing new package received");

        String channelIdentityPrivateKey = getChannel().getChannelIdentity().getPrivateKey();
        String senderIdentityPublicKey = (String) session.getUserProperties().get(HeadersAttName.CPKI_ATT_HEADER_NAME);
        MessageTransmitRespond messageTransmitRespond = null;
        Message messageContent = null;

        try {

            /*
             * Get the content
             */
            messageContent = GsonProvider.getGson().fromJson(packageReceived.getContent(), Message.class);

            /*
             * Create the method call history
             */
            methodCallsHistory(getGson().toJson(messageContent), senderIdentityPublicKey);

            /*
             * Get the destination
             */
            String destinationIdentityPublicKey = messageContent.getReceiver();

            /*
             * Get the connection to the destination
             */
            Session clientDestination =  clientsSessionMemoryCache.get(destinationIdentityPublicKey);

            if (clientDestination != null){

                /*
                 * Repackage the content and send
                 */
                Package packageToRedirect = Package.createInstance(messageContent.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.MESSAGE_TRANSMIT, channelIdentityPrivateKey, destinationIdentityPublicKey);
                clientDestination.getBasicRemote().sendObject(packageToRedirect);

                /*
                 * Notify to de sender the message was transmitted
                 */
                messageTransmitRespond = new MessageTransmitRespond(MsgRespond.STATUS.SUCCESS, MsgRespond.STATUS.SUCCESS.toString(), messageContent.getId());
                Package packageRespond = Package.createInstance(messageTransmitRespond.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.MESSAGE_TRANSMIT_RESPOND, channelIdentityPrivateKey, senderIdentityPublicKey);
                session.getBasicRemote().sendObject(packageRespond);

            }else {

                /*
                 * Notify to de sender the message can not transmit
                 */
                messageTransmitRespond = new MessageTransmitRespond(MsgRespond.STATUS.FAIL, "The destination is not more available", messageContent.getId());
                Package packageRespond = Package.createInstance(messageTransmitRespond.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.MESSAGE_TRANSMIT_RESPOND, channelIdentityPrivateKey, senderIdentityPublicKey);
                session.getBasicRemote().sendObject(packageRespond);
            }


        }catch (Exception exception){

            try {

                LOG.error(exception.getMessage());

                messageTransmitRespond = new MessageTransmitRespond(MsgRespond.STATUS.FAIL, exception.getMessage(), messageContent.getId());
                Package packageRespond = Package.createInstance(messageTransmitRespond.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.MESSAGE_TRANSMIT_RESPOND, channelIdentityPrivateKey, senderIdentityPublicKey);

                /*
                 * Send the respond
                 */
                session.getBasicRemote().sendObject(packageRespond);

            } catch (IOException iOException) {
                LOG.error(iOException.getMessage());
            } catch (EncodeException encodeException) {
                LOG.error(encodeException.getMessage());
            }

        }

    }

}

package com.bitdubai.fermat_core.layer._12_transaction.incoming_extra_user.developer.bitdubai.version_1.event_handlers;

import com.bitdubai.fermat_api.Service;
import com.bitdubai.fermat_api.layer._12_transaction.TransactionNotStartedException;
import com.bitdubai.fermat_api.layer._12_transaction.incoming_extra_user.IncomingExtraUserManager;
import com.bitdubai.fermat_api.layer._1_definition.enums.ServiceStatus;
import com.bitdubai.fermat_api.layer._1_definition.event.PlatformEvent;
import com.bitdubai.fermat_api.layer._2_platform_service.event_manager.EventHandler;
import com.bitdubai.fermat_api.layer._8_crypto.address_book.exceptions.ExampleException;

/**
 * Created by loui on 23/02/15.
 */
public class IncomingCryptoReversedFromExtraUserEventHandler implements EventHandler {
    
    IncomingExtraUserManager incomingExtraUserManager;
    
    public void setIncomingExtraUserManager(IncomingExtraUserManager incomingExtraUserManager){
        this.incomingExtraUserManager = incomingExtraUserManager;
        
    }

    @Override
    public void handleEvent(PlatformEvent platformEvent) throws Exception {
        if (((Service) this.incomingExtraUserManager).getStatus() == ServiceStatus.STARTED){

            try
            {
                this.incomingExtraUserManager.exampleMethod();
            }
            catch (ExampleException exampleException)
            {
                /**
                 * The main module could not handle this exception. Me neither. Will throw it again.
                 */
                System.err.println("CantCreateCryptoWalletException: "+ exampleException.getMessage());
                exampleException.printStackTrace();

                throw  exampleException;
            }
        }
        else
        {
            throw new TransactionNotStartedException();
        }
    }
}

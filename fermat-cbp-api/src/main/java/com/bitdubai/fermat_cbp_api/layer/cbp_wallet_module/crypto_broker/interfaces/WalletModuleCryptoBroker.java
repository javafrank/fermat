package com.bitdubai.fermat_cbp_api.layer.cbp_wallet_module.crypto_broker.interfaces;

/**
 * Created by Angel on 2015.09.16..
 */

public interface WalletModuleCryptoBroker{

    String getWalletId();

    String getPublicKey();

    WalletConfiguration getConfigurations();

}

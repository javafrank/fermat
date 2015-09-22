package com.bitdubai.fermat_dmp_plugin.layer.wallet_module.crypto_wallet.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.layer.dmp_wallet_module.crypto_wallet.interfaces.ActorTransactionSummary;

/**
 * The class <code>com.bitdubai.fermat_dmp_plugin.layer.wallet_module.crypto_wallet.developer.bitdubai.version_1.structure.CryptoWalletWalletModuleActorTransactionSummary</code>
 * implements the interface ActorTransactionSummary and all its methods.
 *
 * Created by mati on 2015.09.17..
 * Modified by Leon Acosta - (laion.cj91@gmail.com) on 18/09/15.
 * @version 1.0
 */
public class CryptoWalletWalletModuleActorTransactionSummary implements ActorTransactionSummary {

    private int sentTransactionsNumber;
    private int receivedTransactionsNumber;

    private long sentAmount;
    private long receivedAmount;

    public CryptoWalletWalletModuleActorTransactionSummary(int sentTransactionsNumber, int receivedTransactionsNumber, long sentAmount, long receivedAmount) {
        this.sentTransactionsNumber = sentTransactionsNumber;
        this.receivedTransactionsNumber = receivedTransactionsNumber;
        this.sentAmount = sentAmount;
        this.receivedAmount = receivedAmount;
    }

    @Override
    public int getSentTransactionsNumber() {
        return sentTransactionsNumber;
    }

    @Override
    public int getReceivedTransactionsNumber() {
        return receivedTransactionsNumber;
    }

    @Override
    public int getTotalTransactionsNumber() {
        return sentTransactionsNumber+receivedTransactionsNumber;
    }

    @Override
    public long getSentAmount() {
        return sentAmount;
    }

    @Override
    public long getReceivedAmount() {
        return receivedAmount;
    }

    @Override
    public long getBalanceAmount() {
        return receivedAmount-sentAmount;
    }
}
package com.bitdubai.smartwallet.platform.layer._9_module.wallet_runtime.version_1.transaction;

import com.bitdubai.smartwallet.platform.layer._2_definition.money.Discount;
import com.bitdubai.smartwallet.platform.layer._2_definition.money.MoneySource;

/**
 * Created by ciencias on 21.12.14.
 */
public  class InnerSystemMoneyOut extends FiatCryptoWithSystemUser {

    private MoneySource[] mMoneySource;
    private Discount mDiscount;
}

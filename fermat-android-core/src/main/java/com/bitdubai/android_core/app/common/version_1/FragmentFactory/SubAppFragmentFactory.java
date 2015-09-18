package com.bitdubai.android_core.app.common.version_1.FragmentFactory;


import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.sub_app.developer.FragmentFactory.DeveloperSubAppFragmentFactory;
import com.bitdubai.sub_app.intra_user.fragmentFactory.IntraUserFragmentFactory;
import com.bitdubai.sub_app.wallet_factory.factory.WalletFactoryFragmentFactory;
import com.bitdubai.sub_app.wallet_publisher.FragmentFactory.WalletPublisherFragmentFactory;
import com.bitdubai.sub_app.wallet_store.fragmentFactory.WalletStoreFragmentFactory;

/**
 * Created by Matias Furszyfer on 2015.08.19..
 */

public class SubAppFragmentFactory {

    public static com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.SubAppFragmentFactory getFragmentFactoryBySubAppType(String subAppType) throws InvalidParameterException {
        switch (subAppType) {
            case "CWF":
                return new WalletFactoryFragmentFactory();
            case "CWS":
                return new WalletStoreFragmentFactory();
            case "CWP":
                return new WalletPublisherFragmentFactory();
            case "CDA":
                return new DeveloperSubAppFragmentFactory();
            case "CIU":
                return new IntraUserFragmentFactory();
            default:
                throw new InvalidParameterException(InvalidParameterException.DEFAULT_MESSAGE, null, "Code Received: " + subAppType, "This Code Is Not Valid for the Plugins enum");

        }

    }
}
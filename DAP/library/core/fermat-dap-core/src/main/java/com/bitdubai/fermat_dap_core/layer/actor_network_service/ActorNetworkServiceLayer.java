package com.bitdubai.fermat_dap_core.layer.actor_network_service;

import com.bitdubai.fermat_api.layer.all_definition.common.system.abstract_classes.AbstractLayer;
import com.bitdubai.fermat_api.layer.all_definition.common.system.exceptions.CantRegisterPluginException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.exceptions.CantStartLayerException;
import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_dap_core.layer.actor_network_service.asset_issuer.AssetIssuerPluginSubsystem;
import com.bitdubai.fermat_dap_core.layer.actor_network_service.asset_user.AssetUserPluginSubsystem;
import com.bitdubai.fermat_dap_core.layer.actor_network_service.redeem_point.RedeemPointPluginSubsystem;

/**
 * Created by lnacosta - (laion.cj91@gmail.com) on 11/11/2015.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class ActorNetworkServiceLayer extends AbstractLayer {

    public ActorNetworkServiceLayer() {
        super(Layers.ACTOR_NETWORK_SERVICE);
    }

    public void start() throws CantStartLayerException {

        try {

            registerPlugin(new AssetIssuerPluginSubsystem());
            registerPlugin(new AssetUserPluginSubsystem());
            registerPlugin(new RedeemPointPluginSubsystem());

        } catch(CantRegisterPluginException e) {

            throw new CantStartLayerException(
                    e,
                    "Actor Network Service Layer of DAP Platform",
                    "Problem trying to register a plugin."
            );
        }
    }

}

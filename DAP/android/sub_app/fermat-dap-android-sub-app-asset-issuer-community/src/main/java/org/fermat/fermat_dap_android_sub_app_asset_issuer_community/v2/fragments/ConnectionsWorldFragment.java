package org.fermat.fermat_dap_android_sub_app_asset_issuer_community.v2.fragments;

import android.support.v4.widget.SwipeRefreshLayout;

import com.bitdubai.fermat_android_api.layer.definition.wallet.AbstractFermatFragment;
import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.ReferenceAppFermatSession;
import com.bitdubai.fermat_android_api.ui.interfaces.FermatListItemListeners;
import com.bitdubai.fermat_pip_api.layer.network_service.subapp_resources.SubAppResourcesProviderManager;

import org.fermat.fermat_dap_android_sub_app_asset_issuer_community.models.ActorIssuer;
import org.fermat.fermat_dap_android_sub_app_asset_issuer_community.v2.adapters.CommunityListAdapter;
import org.fermat.fermat_dap_android_sub_app_asset_issuer_community.v2.dialogs.GeolocationDialog;
import org.fermat.fermat_dap_android_sub_app_asset_issuer_community.v2.dialogs.SearchDialog;
import org.fermat.fermat_dap_api.layer.dap_sub_app_module.asset_issuer_community.interfaces.AssetIssuerCommunitySubAppModuleManager;

/**
 * Created by Frank Contreras (contrerasfrank@gmail.com) on 8/23/16.
 */
public class ConnectionsWorldFragment
        extends AbstractFermatFragment<ReferenceAppFermatSession<AssetIssuerCommunitySubAppModuleManager>, SubAppResourcesProviderManager>
        implements SwipeRefreshLayout.OnRefreshListener,
        FermatListItemListeners<ActorIssuer>,
        GeolocationDialog.AdapterCallback , SearchDialog.AdapterCallbackAlias, CommunityListAdapter.AdapterCallbackList {{
}

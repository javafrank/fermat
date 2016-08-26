package org.fermat.fermat_dap_android_sub_app_asset_issuer_community.v2.dialogs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.ReferenceAppFermatSession;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatButton;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_android_api.ui.dialogs.FermatDialog;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedUIExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.enums.UISource;
import com.bitdubai.fermat_api.layer.modules.common_classes.ActiveActorIdentityInformation;
import com.bitdubai.fermat_dap_android_sub_app_asset_issuer_community_bitdubai.R;
import com.bitdubai.fermat_pip_api.layer.network_service.subapp_resources.SubAppResourcesProviderManager;

import org.fermat.fermat_dap_android_sub_app_asset_issuer_community.models.ActorIssuer;
import org.fermat.fermat_dap_api.layer.dap_sub_app_module.asset_issuer_community.interfaces.AssetIssuerCommunitySubAppModuleManager;

/**
 * Created by Frank Contreras (contrerasfrank@gmail.com) on 8/25/16.
 */
public class SearchDialog extends FermatDialog<ReferenceAppFermatSession<AssetIssuerCommunitySubAppModuleManager>, SubAppResourcesProviderManager>
        implements View.OnClickListener {
    /**
     * UI components
     */
    private final ActorIssuer actorIssuer;
    private final ActiveActorIdentityInformation identity;
    private AdapterCallbackAlias mAdapterCallback;
    private FermatTextView title;
    private FermatTextView description;
    private FermatTextView userName;
    private FermatButton positiveBtn;
    private FermatButton negativeBtn;
    private String alias;
    private Context activity;

    public SearchDialog(final Context activity,
                             final ReferenceAppFermatSession<AssetIssuerCommunitySubAppModuleManager> issuerCommunitySubAppSession,
                             final SubAppResourcesProviderManager subAppResources,
                             final ActorIssuer actorIssuer,
                             final ActiveActorIdentityInformation identity,
                             final String alias,
                             AdapterCallbackAlias mAdapterCallback) {

        super(activity, issuerCommunitySubAppSession, subAppResources);

        this.actorIssuer = actorIssuer;
        this.identity = identity;
        this.mAdapterCallback = mAdapterCallback;
        this.activity = activity;
        this.alias = alias;
    }

    public static interface AdapterCallbackAlias {
        void onMethodCallbackAlias(String alias);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title = (FermatTextView) findViewById(R.id.title);
        description = (FermatTextView) findViewById(R.id.description);
        userName = (FermatTextView) findViewById(R.id.user_name);
        positiveBtn = (FermatButton) findViewById(R.id.positive_button);
        negativeBtn = (FermatButton) findViewById(R.id.negative_button);

        positiveBtn.setOnClickListener(this);
        negativeBtn.setOnClickListener(this);

        title.setText(activity.getResources().getString(R.string.dap_issuer_community_location_search));
        description.setText(activity.getResources().getString(R.string.dap_issuer_community_location_search2));
        userName.setText("");

    }

    @Override
    protected int setLayoutId() {
        return R.layout.dap_issuer_community_v2_dialog_builder;
    }

    @Override
    protected int setWindowFeature() {
        return Window.FEATURE_NO_TITLE;
    }

    @Override
    public void onClick(View v) {

        int i = v.getId();

        if (i == R.id.positive_button) {

            try {
                mAdapterCallback.onMethodCallbackAlias(alias);
                dismiss();
            } catch (Exception e) {
                super.getErrorManager().reportUnexpectedUIException(UISource.VIEW, UnexpectedUIExceptionSeverity.UNSTABLE, e);
                //super.toastDefaultError();
            }
            dismiss();

        } else if (i == R.id.negative_button) {
            dismiss();
        }
    }
}

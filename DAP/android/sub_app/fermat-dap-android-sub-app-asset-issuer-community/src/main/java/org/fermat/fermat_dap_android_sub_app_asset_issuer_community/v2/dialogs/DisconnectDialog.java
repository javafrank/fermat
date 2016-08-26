package org.fermat.fermat_dap_android_sub_app_asset_issuer_community.v2.dialogs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.ReferenceAppFermatSession;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatButton;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_android_api.ui.dialogs.FermatDialog;
import com.bitdubai.fermat_api.layer.actor_connection.common.exceptions.ActorConnectionNotFoundException;
import com.bitdubai.fermat_api.layer.actor_connection.common.exceptions.CantDisconnectFromActorException;
import com.bitdubai.fermat_api.layer.actor_connection.common.exceptions.UnexpectedConnectionStateException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedUIExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.enums.UISource;
import com.bitdubai.fermat_api.layer.modules.common_classes.ActiveActorIdentityInformation;
import com.bitdubai.fermat_dap_android_sub_app_asset_issuer_community_bitdubai.R;
import com.bitdubai.fermat_pip_api.layer.network_service.subapp_resources.SubAppResourcesProviderManager;

import org.fermat.fermat_dap_android_sub_app_asset_issuer_community.models.ActorIssuer;
import org.fermat.fermat_dap_api.layer.dap_identity.asset_issuer.interfaces.IdentityAssetIssuer;
import org.fermat.fermat_dap_api.layer.dap_sub_app_module.asset_issuer_community.interfaces.AssetIssuerCommunitySubAppModuleManager;

/**
 * Created by Frank Contreras (contrerasfrank@gmail.com) on 8/23/16.
 */
public class DisconnectDialog extends FermatDialog<ReferenceAppFermatSession<AssetIssuerCommunitySubAppModuleManager>, SubAppResourcesProviderManager>
        implements View.OnClickListener {
    /**
     * UI components
     */
    private FermatButton positiveBtn;
    private FermatButton negativeBtn;
    private FermatTextView mDescription;
    private FermatTextView mUsername;
    private FermatTextView mTitle;
    private CharSequence description;
    private CharSequence username;
    private CharSequence title;

    private final ActorIssuer actorIssuer;
    private final ActiveActorIdentityInformation identity;
    private Context activity;

    public DisconnectDialog(final Context activity,
                            final ReferenceAppFermatSession<AssetIssuerCommunitySubAppModuleManager> chatUserSubAppSession,
                            final SubAppResourcesProviderManager subAppResources,
                            final ActorIssuer actorIssuer,
                            final ActiveActorIdentityInformation identity) {

        super(activity, chatUserSubAppSession, subAppResources);

        this.actorIssuer = actorIssuer;
        this.identity = identity;
        this.activity = activity;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDescription = (FermatTextView) findViewById(R.id.description);
        mUsername = (FermatTextView) findViewById(R.id.user_name);
        mTitle = (FermatTextView) findViewById(R.id.title);
        positiveBtn = (FermatButton) findViewById(R.id.positive_button);
        negativeBtn = (FermatButton) findViewById(R.id.negative_button);
        positiveBtn.setOnClickListener(this);
        negativeBtn.setOnClickListener(this);
        if (actorIssuer != null) {
            setDescription(activity.getResources().getString(R.string.dap_issuer_community_disconnect2) + " " + actorIssuer.getRecord().getName() + "?");
        }
        mDescription.setText(description != null ? description : "");
        mUsername.setText(username != null ? username : "");
        mTitle.setText(title != null ? title : "");
    }

    public void setDescription(CharSequence description) {
        this.description = description;
    }

    public void setUsername(CharSequence username) {
        this.username = username;
    }

    @Override
    public void setTitle(CharSequence title) {
        this.title = title;
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
//            try {
                if (actorIssuer != null && identity != null) {
                    //TODO adaptar a issuer community
//                    getSession().getModuleManager()
//                            .disconnectChatActor(actorIssuer.getConnectionId());
//                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
//                    prefs.edit().putBoolean("Connected", true).apply();
//                    Intent broadcast = new Intent(Constants.LOCAL_BROADCAST_CHANNEL);
//                    broadcast.putExtra(Constants.BROADCAST_DISCONNECTED_UPDATE, true);
//                    sendLocalBroadcast(broadcast);
//                    Toast.makeText(getContext(), activity.getResources().getString(R.string.cht_comm_text_disconnect3), Toast.LENGTH_SHORT).show();
                } else {
                    super.toastDefaultError();
                }
                dismiss();
//            } catch (ChatActorDisconnectingFailedException
//                    | ActorConnectionRequestNotFoundException
//                    | ConnectionRequestNotFoundException
//                    | CantDisconnectFromActorException
//                    | UnexpectedConnectionStateException
//                    | ActorConnectionNotFoundException
//                    e) {
//                super.getErrorManager().reportUnexpectedUIException(UISource.VIEW, UnexpectedUIExceptionSeverity.UNSTABLE, e);
//                super.toastDefaultError();
//            }
        }
        dismiss();
    }
}

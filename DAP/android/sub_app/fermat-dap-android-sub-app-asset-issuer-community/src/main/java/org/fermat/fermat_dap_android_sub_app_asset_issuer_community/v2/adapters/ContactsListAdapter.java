package org.fermat.fermat_dap_android_sub_app_asset_issuer_community.v2.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;

import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.ReferenceAppFermatSession;
import com.bitdubai.fermat_android_api.layer.definition.wallet.utils.ImagesUtils;
import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapter;
import com.bitdubai.fermat_dap_android_sub_app_asset_issuer_community_bitdubai.R;

import org.fermat.fermat_dap_android_sub_app_asset_issuer_community.models.ActorIssuer;
import org.fermat.fermat_dap_android_sub_app_asset_issuer_community.v2.dialogs.ContactDialog;
import org.fermat.fermat_dap_android_sub_app_asset_issuer_community.v2.filters.ContactsFilter;
import org.fermat.fermat_dap_android_sub_app_asset_issuer_community.v2.holders.ContactsListHolder;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.AssetIssuerActorRecord;
import org.fermat.fermat_dap_api.layer.dap_sub_app_module.asset_issuer_community.interfaces.AssetIssuerCommunitySubAppModuleManager;

import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * Created by Frank Contreras (contrerasfrank@gmail.com) on 8/23/16.
 */
public class ContactsListAdapter
        extends FermatAdapter<ActorIssuer, ContactsListHolder>
        implements Filterable {
    private ReferenceAppFermatSession<AssetIssuerCommunitySubAppModuleManager> appSession;
    private AssetIssuerCommunitySubAppModuleManager moduleManager;
    List<ActorIssuer> filteredData;
    private String filterString;
    private String cityAddress;
    private String stateAddress;
    private String countryAddress;

    public ContactsListAdapter(Context context, List<ActorIssuer> dataSet,
                               ReferenceAppFermatSession<AssetIssuerCommunitySubAppModuleManager> appSession,
                               AssetIssuerCommunitySubAppModuleManager moduleManager) {
        super(context, dataSet);
        this.appSession = appSession;
        this.moduleManager = moduleManager;
    }

    @Override
    protected ContactsListHolder createHolder(View itemView, int type) {
        return new ContactsListHolder(itemView);
    }

    @Override
    protected int getCardViewResource() {
        return R.layout.dap_issuer_community_v2_connection_list_item;
    }

    @Override
    protected void bindHolder(ContactsListHolder holder, ActorIssuer actorIssuer, int position) {
        AssetIssuerActorRecord data = actorIssuer.getRecord();
        int max = 10;
        if(max > data.getName().length())
            max = data.getName().length();
        if (data.getActorPublicKey() != null) {
            holder.friendName.setText(data.getName().substring(0, max));
            if (data.getProfileImage() != null && data.getProfileImage().length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data.getProfileImage(), 0, data.getProfileImage().length);
                bitmap = Bitmap.createScaledBitmap(bitmap, 120, 120, true);
                holder.friendAvatar.setImageDrawable(ImagesUtils.getRoundedBitmap(context.getResources(), bitmap));
            } else
                holder.friendAvatar.setImageResource(R.drawable.profile_image);

//            if (data.getLocation() != null) {
//                if (data.getState().equals("null") || data.getState().equals("") || data.getState().equals("state"))
//                    stateAddress = "";
//                else stateAddress = new StringBuilder().append(data.getState()).append(" ").toString();
//            if (data.getCity().equals("null") || data.getState().equals("") || data.getCity().equals("city"))
//                cityAddress = "";
//            else cityAddress = new StringBuilder().append(data.getCity()).append(" ").toString();
//            if (data.getCountry().equals("null") || data.getState().equals("") || data.getCountry().equals("country"))
//                countryAddress = "";
//            else countryAddress = data.getCountry();
//            if (/*stateAddress.equalsIgnoreCase("") && */cityAddress.equalsIgnoreCase("") && countryAddress.equalsIgnoreCase("")) {
//                holder.location.setText(context.getResources().getString(R.string.dao_issuer_community_not_found));
//            } else
//                holder.location.setText(new StringBuilder().append(cityAddress).append(" ").append(countryAddress).toString());
//            } else
//                holder.location.setText(R.string.cht_comm_not_found);

//            if (data.getProfileStatus() != null && data.getProfileStatus().getCode().equalsIgnoreCase("ON"))
//                holder.location.setTextColor(Color.parseColor("#47BF73"));
//            else
//                holder.location.setTextColor(Color.RED);

            final AssetIssuerActorRecord dat = data;
            holder.friendAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContactDialog contact = new ContactDialog(context, appSession, null);
                    contact.setProfileName(dat.getName());
//                    if (dat.getLocation() != null) {
                        /*if (dat.getState().equals("null") || dat.getState().equals(""))
                            stateAddress = "";
                        else stateAddress = new StringBuilder().append(dat.getState()).append(" ").toString();*/
//                    if (dat.getCity().equals("null") || dat.getCity().equals(""))
//                        cityAddress = "";
//                    else cityAddress = new StringBuilder().append(dat.getCity()).append(" ").toString();
//                    if (dat.getCountry().equals("null") || dat.getCountry().equals(""))
//                        countryAddress = "";
//                    else countryAddress = dat.getCountry();
//                    if (/*stateAddress.equalsIgnoreCase("") &&*/ cityAddress.equalsIgnoreCase("") && countryAddress.equalsIgnoreCase("")) {
//                        contact.setCountryText(context.getResources().getString(R.string.dao_issuer_community_not_found));
//                    } else
//                        contact.setCountryText(new StringBuilder().append(cityAddress).append(" ").append(countryAddress).toString());
//                    } else
//                        contact.setCountryText("Not Found");

                    ByteArrayInputStream bytes = new ByteArrayInputStream(dat.getProfileImage());
                    BitmapDrawable bmd = new BitmapDrawable(bytes);
                    contact.setProfilePhoto(bmd.getBitmap());
                    contact.show();
                }
            });
        }
    }

    public int getSize() {
        if (dataSet != null)
            return dataSet.size();
        return 0;
    }

    public void setData(List<ActorIssuer> data) {
        this.filteredData = data;
    }

    public Filter getFilter() {
        return new ContactsFilter(dataSet, this);
    }

    public void setFilterString(String filterString) {
        this.filterString = filterString;
    }

    public String getFilterString() {
        return filterString;
    }
}

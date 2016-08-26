package org.fermat.fermat_dap_android_sub_app_asset_issuer_community.v2.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitdubai.fermat_android_api.ui.holders.FermatViewHolder;
import com.bitdubai.fermat_dap_android_sub_app_asset_issuer_community_bitdubai.R;

/**
 * Created by Frank Contreras (contrerasfrank@gmail.com) on 8/23/16.
 */
public class ContactsListHolder extends FermatViewHolder {
    public ImageView friendAvatar;
    public TextView friendName;
    public TextView location;

    /**
     * Constructor
     *
     * @param itemView cast ui elements
     */
    public ContactsListHolder(View itemView) {
        super(itemView);
        friendName = (TextView) itemView.findViewById(R.id.username);
        friendAvatar = (ImageView) itemView.findViewById(R.id.imageView_avatar);
        location = (TextView) itemView.findViewById(R.id.location_text);
    }
}

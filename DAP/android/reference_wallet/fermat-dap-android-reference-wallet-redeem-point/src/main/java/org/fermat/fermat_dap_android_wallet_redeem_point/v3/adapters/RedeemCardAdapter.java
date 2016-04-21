package org.fermat.fermat_dap_android_wallet_redeem_point.v3.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;

import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.FermatSession;
import com.bitdubai.fermat_android_api.layer.definition.wallet.utils.ImagesUtils;
import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapter;
import com.bitdubai.fermat_android_api.ui.util.BitmapWorkerTask;
import com.bitdubai.fermat_dap_android_wallet_redeem_point_bitdubai.R;

import org.fermat.fermat_dap_android_wallet_redeem_point.models.DigitalAsset;
import org.fermat.fermat_dap_android_wallet_redeem_point.sessions.RedeemPointSession;
import org.fermat.fermat_dap_android_wallet_redeem_point.v3.filters.RedeemHomeCardAdapterFilter;
import org.fermat.fermat_dap_android_wallet_redeem_point.v3.fragments.RedeemHomeCardFragment;
import org.fermat.fermat_dap_android_wallet_redeem_point.v3.holders.RedeemCardViewHolder;
import org.fermat.fermat_dap_api.layer.dap_module.wallet_asset_redeem_point.interfaces.AssetRedeemPointWalletSubAppModule;

import java.util.List;

/**
 * Created by madscientist on 19/04/16.
 */
public class RedeemCardAdapter extends FermatAdapter<DigitalAsset, RedeemCardViewHolder> implements Filterable {

    private  RedeemPointSession assetRedeemSession;
    private  AssetRedeemPointWalletSubAppModule manager;
    private  RedeemHomeCardFragment fragment;

    public RedeemCardAdapter(RedeemHomeCardFragment fragment, Context context, List<DigitalAsset> digitalAssets, AssetRedeemPointWalletSubAppModule manager,
                           FermatSession appSession) {
        super(context, digitalAssets);
        this.fragment = fragment;
        this.manager = manager;
        this.dataSet = digitalAssets;
        this.assetRedeemSession = (RedeemPointSession) appSession;
    }
    @Override
    protected RedeemCardViewHolder createHolder(View itemView, int type) {

        return new RedeemCardViewHolder(itemView, manager, context);
    }

    @Override
    protected int getCardViewResource() {
        return R.layout.dap_v3_wallet_asset_redeem_point_home_card_item;
    }

    @Override
    protected void bindHolder(RedeemCardViewHolder holder, DigitalAsset data, int position) {
        bind(holder,data);
    }

    @Override
    public Filter getFilter() {
        return new RedeemHomeCardAdapterFilter(this.dataSet,this);
    }

    public void bind (RedeemCardViewHolder holder, final DigitalAsset asset){
        Bitmap bitmap;
        if (asset.getImage() != null && asset.getImage().length > 0) {
            bitmap = BitmapFactory.decodeByteArray(asset.getImage(), 0, asset.getImage().length);
        } else {
            bitmap = BitmapFactory.decodeResource(holder.res, R.drawable.img_asset_without_image);
        }
        bitmap = Bitmap.createScaledBitmap(bitmap, 45, 45, true);
        holder.cardAssetImage.setImageDrawable(ImagesUtils.getRoundedBitmap(holder.res, bitmap));

        holder.cardAssetName.setText(asset.getName());
//        cardTime.setText(asset.getFormattedDate()); agregasr este metodo al modelo digital asset
        holder.cardTime.setText(asset.getFormattedExpDate());

        byte[] img = (asset.getImageActorUserFrom() == null) ? new byte[0] : asset.getImage(); /*modificar modelo Digital Asset*/
        BitmapWorkerTask bitmapWorkerTask = new BitmapWorkerTask(holder.cardAssetImage,
                holder.res, R.drawable.img_asset_without_image, false);
        bitmapWorkerTask.execute(img);

        holder.cardActorName.setText(asset.getActorUserNameFrom());


        holder.cardAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assetRedeemSession.setData("asset_data", asset);
                fragment.doAcceptAsset();
            }
        });
        holder.cardRejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assetRedeemSession.setData("asset_data",asset);
                fragment.doRejectAsset();
            }
        });
        holder.cardDeliverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assetRedeemSession.setData("asset_data",asset);
                fragment.doDeliver();
            }
        });

    }
}

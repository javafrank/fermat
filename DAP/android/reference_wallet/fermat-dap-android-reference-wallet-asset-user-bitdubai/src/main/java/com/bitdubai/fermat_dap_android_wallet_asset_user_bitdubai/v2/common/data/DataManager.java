package com.bitdubai.fermat_dap_android_wallet_asset_user_bitdubai.v2.common.data;

import com.bitdubai.fermat_dap_android_wallet_asset_user_bitdubai.v2.models.Asset;
import com.bitdubai.fermat_dap_android_wallet_asset_user_bitdubai.v2.models.Issuer;
import com.bitdubai.fermat_dap_android_wallet_asset_user_bitdubai.v2.models.RedeemPoint;
import com.bitdubai.fermat_dap_api.layer.dap_actor.asset_issuer.interfaces.ActorAssetIssuer;
import com.bitdubai.fermat_dap_api.layer.dap_actor.redeem_point.exceptions.CantGetAssetRedeemPointActorsException;
import com.bitdubai.fermat_dap_api.layer.dap_actor.redeem_point.interfaces.ActorAssetRedeemPoint;
import com.bitdubai.fermat_dap_api.layer.dap_module.wallet_asset_user.interfaces.AssetUserWalletSubAppModuleManager;
import com.bitdubai.fermat_dap_api.layer.dap_wallet.asset_user_wallet.interfaces.AssetUserWalletList;
import com.bitdubai.fermat_dap_api.layer.dap_wallet.asset_user_wallet.interfaces.AssetUserWalletTransaction;
import com.bitdubai.fermat_dap_api.layer.dap_wallet.common.WalletUtilities;
import com.bitdubai.fermat_dap_api.layer.dap_wallet.common.enums.BalanceType;
import com.bitdubai.fermat_dap_api.layer.dap_wallet.common.enums.TransactionType;
import com.bitdubai.fermat_dap_api.layer.dap_wallet.common.exceptions.CantGetTransactionsException;
import com.bitdubai.fermat_dap_api.layer.dap_wallet.common.exceptions.CantLoadWalletException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Frank Contreras (contrerasfrank@gmail.com) on 2/24/16.
 */
public class DataManager {
    private AssetUserWalletSubAppModuleManager moduleManager;
    private String walletPublicKey = WalletUtilities.WALLET_PUBLIC_KEY;

    public DataManager(AssetUserWalletSubAppModuleManager moduleManager) {
        this.moduleManager = moduleManager;
    }

    public List<Issuer> getIssuers() throws CantLoadWalletException {
//        Map<ActorAssetIssuer, AssetUserWalletList> map = moduleManager.getAssetUserWalletBalancesByIssuer(walletPublicKey);
//        Iterator<ActorAssetIssuer> it = map.keySet().iterator();
//        ActorAssetIssuer actorAssetIssuer;
//        AssetUserWalletList assetUserWalletList;
//        List<Issuer> issuers = new ArrayList<>();
//        List<Asset> assets;
//        Issuer issuer;
//        while(it.hasNext()) {
//            actorAssetIssuer = it.next();
//            assetUserWalletList = map.get(actorAssetIssuer);
//            issuer = new Issuer(actorAssetIssuer);
//
//            long quantityAvailableBalance = assetUserWalletList.getQuantityAvailableBalance();
//            assets = new ArrayList<>();
//            for(long i = 0; i < quantityAvailableBalance; i++) {
//                assets.add(new Asset(assetUserWalletList, 0, Asset.Status.CONFIRMED));
//            }
//
//            long quantityBookBalance = assetUserWalletList.getQuantityBookBalance() - quantityAvailableBalance;
//            for(long i = 0; i < quantityBookBalance; i++) {
//                assets.add(new Asset(assetUserWalletList, 0, Asset.Status.PENDING));
//            }
//
//            issuer.setAssets(assets);
//            issuers.add(issuer);
//        }
//        return issuers;
        return null;
    }

    public List<Asset> getAssets() throws CantLoadWalletException, CantGetTransactionsException {
        List<AssetUserWalletList> assetUserWalletBalances = moduleManager.getAssetUserWalletBalances(walletPublicKey);
        List<Asset> assets = new ArrayList<>();
//        Asset asset;
//        for(AssetUserWalletList assetUserWalletList : assetUserWalletBalances) {
//            long timestamp = moduleManager.loadAssetUserWallet(walletPublicKey).getAllTransactions(assetUserWalletList.getDigitalAsset().getPublicKey()).get(0).getTimestamp();
//            long quantityAvailableBalance = assetUserWalletList.getQuantityAvailableBalance();
//            assets = new ArrayList<>();
//            for(long i = 0; i < quantityAvailableBalance; i++) {
//                assets.add(new Asset(assetUserWalletList, timestamp, Asset.Status.CONFIRMED));
//            }
//
//            long quantityBookBalance = assetUserWalletList.getQuantityBookBalance() - quantityAvailableBalance;
//            for(long i = 0; i < quantityBookBalance; i++) {
//                assets.add(new Asset(assetUserWalletList, timestamp, Asset.Status.PENDING));
//            }
//        }

        assets = new ArrayList<>();
        for(AssetUserWalletList assetUserWalletList : assetUserWalletBalances) {
            List<AssetUserWalletTransaction> assetUserWalletTransactions = moduleManager.loadAssetUserWallet(walletPublicKey).getAllTransactions(assetUserWalletList.getDigitalAsset().getPublicKey());
            for(AssetUserWalletTransaction assetUserWalletTransaction : assetUserWalletTransactions) {
                if (assetUserWalletTransaction.getMemo().equals("Asset Delivered")
                        && assetUserWalletTransaction.getTransactionType().equals(TransactionType.CREDIT)) {
                    assets.add(new Asset(assetUserWalletList, assetUserWalletTransaction));
                }
            }
        }

        List<Asset> newAssets = new ArrayList<>();
        for (int i = 0; i < assets.size(); i++) {
            boolean b = false;
            for (int j = i+1; j < assets.size(); j++) {
                if (assets.get(i).getId().equals(assets.get(j).getId())) {
                    newAssets.add(assets.get(j));
                    assets.remove(j);
                    b = true;
                }
            }
            if (!b) {
                newAssets.add(assets.get(i));
            }
        }

        Collections.sort(newAssets, new Comparator<Asset>() {
            @Override
            public int compare(Asset lhs, Asset rhs) {
                if (lhs.getDate().getTime() > rhs.getDate().getTime()) return -1;
                else if (lhs.getDate().getTime() < rhs.getDate().getTime()) return 1;
                return 0;
            }
        });

        return newAssets;
    }

    public List<RedeemPoint> getConnectedRedeemPoints(String assetPublicKey) throws CantGetAssetRedeemPointActorsException {
        List<RedeemPoint> redeemPoints = new ArrayList<>();
        List<ActorAssetRedeemPoint> actorAssetRedeemPoints = moduleManager.getRedeemPointsConnectedForAsset(assetPublicKey);
        for (ActorAssetRedeemPoint actorAssetRedeemPoint : actorAssetRedeemPoints) {
            RedeemPoint newUser = new RedeemPoint(actorAssetRedeemPoint);
            redeemPoints.add(newUser);
        }
        return redeemPoints;
    }
}
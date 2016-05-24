package org.fermat.fermat_dap_android_sub_app_asset_factory.v3.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.bitdubai.fermat_android_api.ui.Views.PresentationDialog;
import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapter;
import com.bitdubai.fermat_android_api.ui.enums.FermatRefreshTypes;
import com.bitdubai.fermat_android_api.ui.fragments.FermatWalletListFragment;
import com.bitdubai.fermat_android_api.ui.interfaces.FermatListItemListeners;
import com.bitdubai.fermat_android_api.ui.interfaces.FermatWorkerCallBack;
import com.bitdubai.fermat_android_api.ui.util.FermatWorker;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.UISource;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Activities;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Wallets;
import com.bitdubai.fermat_api.layer.all_definition.resources_structure.Resource;
import com.bitdubai.fermat_api.layer.all_definition.settings.exceptions.CantPersistSettingsException;
import com.bitdubai.fermat_api.layer.all_definition.settings.structure.SettingsManager;
import com.bitdubai.fermat_api.layer.all_definition.util.BitcoinConverter;
import com.bitdubai.fermat_bch_api.layer.crypto_network.bitcoin.interfaces.BitcoinNetworkConfiguration;
import com.bitdubai.fermat_bch_api.layer.crypto_vault.classes.CryptoVault;
import com.bitdubai.fermat_dap_android_sub_app_asset_factory_bitdubai.R;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedUIExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedWalletExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;
import com.bitdubai.fermat_wpd_api.layer.wpd_middleware.wallet_manager.interfaces.InstalledWallet;
import com.software.shell.fab.ActionButton;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.fermat.fermat_dap_android_sub_app_asset_factory.sessions.AssetFactorySession;
import org.fermat.fermat_dap_android_sub_app_asset_factory.sessions.SessionConstantsAssetFactory;
import org.fermat.fermat_dap_android_sub_app_asset_factory.util.CommonLogger;
import org.fermat.fermat_dap_android_sub_app_asset_factory.util.Utils;
import org.fermat.fermat_dap_android_sub_app_asset_factory.v3.adapters.AssetFactoryDraftAdapter;
import org.fermat.fermat_dap_android_sub_app_asset_factory.v3.filters.AssetFactoryDraftAdapterFilter;
import org.fermat.fermat_dap_api.layer.all_definition.DAPConstants;
import org.fermat.fermat_dap_api.layer.all_definition.enums.State;
import org.fermat.fermat_dap_api.layer.dap_identity.asset_issuer.interfaces.IdentityAssetIssuer;
import org.fermat.fermat_dap_api.layer.dap_middleware.dap_asset_factory.exceptions.CantPublishAssetFactoy;
import org.fermat.fermat_dap_api.layer.dap_middleware.dap_asset_factory.interfaces.AssetFactory;
import org.fermat.fermat_dap_api.layer.dap_module.asset_factory.AssetFactorySettings;
import org.fermat.fermat_dap_api.layer.dap_module.asset_factory.interfaces.AssetFactoryModuleManager;
import org.fermat.fermat_dap_api.layer.dap_transaction.asset_issuing.exceptions.NotAvailableKeysToPublishAssetsException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static android.widget.Toast.makeText;
import static com.bitdubai.fermat_api.layer.all_definition.util.BitcoinConverter.Currency.BITCOIN;
import static com.bitdubai.fermat_api.layer.all_definition.util.BitcoinConverter.Currency.SATOSHI;
import static org.fermat.fermat_dap_api.layer.all_definition.enums.State.FINAL;

/**
 * Created by Jinmy Bohorquez on 19/04/16.
 */
public class PublishedAssetsHomeFragment extends FermatWalletListFragment<AssetFactory> implements FermatListItemListeners<AssetFactory> {

    private static AssetFactory selectedAsset;

    private AssetFactoryModuleManager manager;
    private ErrorManager errorManager;
    private SettingsManager<AssetFactorySettings> settingsManager;
    private List<AssetFactory> dataSet;

    private ActionButton create;
    private long satoshisWalletBalance;
    private View noAssetsView;
    private Activity activity;
    private SearchView searchView;


    public static PublishedAssetsHomeFragment newInstance() {
        return new PublishedAssetsHomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        try {
            appSession.setData("asset_factory", null);
            appSession.setData("redeem_points", null);
            manager = ((AssetFactorySession) appSession).getModuleManager();
            errorManager = appSession.getErrorManager();
            settingsManager = appSession.getModuleManager().getSettingsManager();
            dataSet = (List) getMoreDataAsync(FermatRefreshTypes.NEW, 0);


        } catch (Exception ex) {
            CommonLogger.exception(TAG, ex.getMessage(), ex);
        }
    }

    @Override
    protected void initViews(View layout) {
        super.initViews(layout);

        activity = new Activity();

        create = (ActionButton) layout.findViewById(R.id.draftCreateButton);
        create.setVisibility(View.GONE);
//        create.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                appSession.setData("asset_factory", null);
//                changeActivity(Activities.DAP_ASSET_EDITOR_ACTIVITY.getCode(), appSession.getAppPublicKey(), (AssetFactory) appSession.getData("asset_factory"));
//            }
//        });
//        create.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fab_jump_from_down));
//        create.setVisibility(View.VISIBLE);
//        create.setEnabled(false);

        initSettings();
        configureToolbar();
        noAssetsView = layout.findViewById(R.id.dap_v3_factory_draft_assets_home_fragment_no_assets);

        try {

        /*test*/
            if (dataSet != null)
                showOrHideNoAssetsView(dataSet.isEmpty());

            satoshisWalletBalance = manager.getBitcoinWalletBalance(Utils.getBitcoinWalletPublicKey(manager));
        } catch (Exception e) {
            CommonLogger.exception(TAG, e.getMessage(), e);
        }

    }

    private void initSettings() {
        settingsManager = appSession.getModuleManager().getSettingsManager();
        AssetFactorySettings settings = null;
        try {
            settings = settingsManager.loadAndGetSettings(appSession.getAppPublicKey());
        } catch (Exception e) {
            settings = null;
        }
        if (settings == null) {
            settings = new AssetFactorySettings();
            settings.setIsContactsHelpEnabled(true);
            settings.setIsPresentationHelpEnabled(true);

            try {
                settingsManager.persistSettings(appSession.getAppPublicKey(), settings);
                manager.setAppPublicKey(appSession.getAppPublicKey());

                manager.changeNetworkType(settings.getBlockchainNetwork().get(settings.getBlockchainNetworkPosition()));
            } catch (CantPersistSettingsException e) {
                e.printStackTrace();
            }
        } else {
            manager.changeNetworkType(settings.getBlockchainNetwork().get(settings.getBlockchainNetworkPosition()));
        }

        final AssetFactorySettings assetFactorySettingsTemp = settings;

        if (manager.getLoggedIdentityAssetIssuer() == null) {
            Handler handlerTimer = new Handler();
            handlerTimer.postDelayed(new Runnable() {
                public void run() {
                    if (assetFactorySettingsTemp.isPresentationHelpEnabled()) {
                        setUpPresentation(false);
                    }
                }
            }, 500);
        } else {
//            create.setEnabled(true);
        }
    }

    private void configureToolbar() {
        Toolbar toolbar = getPaintActivtyFeactures().getToolbar();
        if (toolbar != null) {
            toolbar.setBackgroundColor(Color.parseColor("#3f5b77"));
            toolbar.setTitleTextColor(Color.WHITE);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                Window window = getActivity().getWindow();
                window.setStatusBarColor(Color.parseColor("#3f5b77"));
            }
        }
    }

    private void setUpPresentation(boolean checkButton) {
        try {
            PresentationDialog presentationDialog = new PresentationDialog.Builder(getActivity(), appSession)
                    .setBannerRes(R.drawable.banner_asset_factory)
                    .setIconRes(R.drawable.asset_factory)
                    .setImageLeft(R.drawable.asset_issuer_identity)
                    .setVIewColor(R.color.dap_asset_factory_view_color)
                    .setTitleTextColor(R.color.dap_asset_factory_view_color)
                    .setTextNameLeft(R.string.dap_asset_factory_welcome_name_left)
                    .setSubTitle(R.string.dap_asset_factory_welcome_subTitle)
                    .setBody(R.string.dap_asset_factory_welcome_body)
                    .setTextFooter(R.string.dap_asset_factory_welcome_Footer)
                    .setTemplateType((manager.getLoggedIdentityAssetIssuer() == null) ? PresentationDialog.TemplateType.DAP_TYPE_PRESENTATION : PresentationDialog.TemplateType.TYPE_PRESENTATION_WITHOUT_IDENTITIES)
                    .setIsCheckEnabled(checkButton)
                    .build();

            presentationDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    Object o = appSession.getData(SessionConstantsAssetFactory.PRESENTATION_IDENTITY_CREATED);
                    if (o != null) {
                        if ((Boolean) (o)) {
                            //invalidate();
                            appSession.removeData(SessionConstantsAssetFactory.PRESENTATION_IDENTITY_CREATED);
                        }
                    }
                    IdentityAssetIssuer identityAssetIssuer = manager.getLoggedIdentityAssetIssuer();
                    if (identityAssetIssuer == null) {
                        getActivity().onBackPressed();
                    } else {
                        invalidate();
                    }
//                    create.setEnabled(true);
                }
            });

            presentationDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showOrHideNoAssetsView(boolean empty) {
        if (empty) {
            recyclerView.setVisibility(View.GONE);
            noAssetsView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noAssetsView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dap_wallet_asset_factory_draft_menu, menu);
        searchView = (SearchView) menu.findItem(R.id.action_dap_factory_draft_search).getActionView();
        searchView.setQueryHint(getResources().getString(R.string.dap_factory_draft_search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.equals(searchView.getQuery().toString())) {
                    ((AssetFactoryDraftAdapterFilter) ((AssetFactoryDraftAdapter) getAdapter()).getFilter()).filter(s);
                }
                return false;
            }
        });

        menu.add(0, SessionConstantsAssetFactory.IC_ACTION_HELP_FACTORY, 0, "help").setIcon(R.drawable.dap_asset_factory_help_icon)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();

            if (id == SessionConstantsAssetFactory.IC_ACTION_HELP_FACTORY) {
                setUpPresentation(settingsManager.loadAndGetSettings(appSession.getAppPublicKey()).isPresentationHelpEnabled());
                return true;
            }

        } catch (Exception e) {
            errorManager.reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(e));
            makeText(getActivity(), "Asset Issuer system error",
                    Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected boolean hasMenu() {
        return false;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.dap_v3_factory_draft_assets_home_fragment;
    }

    @Override
    protected int getSwipeRefreshLayoutId() {
        return R.id.factory_draft_swipe_refresh;
    }

    @Override
    protected int getRecyclerLayoutId() {
        return R.id.dap_v3_factory_draft_assets_home_fragment_recycler_view;
    }

    @Override
    protected boolean recyclerHasFixedSize() {
        return true;
    }

    @Override
    public void onPostExecute(Object... result) {
        isRefreshing = false;
        if (isAttached) {
            swipeRefreshLayout.setRefreshing(false);
            if (result != null && result.length > 0) {
                dataSet = (ArrayList) result[0];
                if (adapter != null) {
                    adapter.changeDataSet(dataSet);
                    if (searchView != null) {
                        if (!searchView.getQuery().toString().isEmpty())
                            ((AssetFactoryDraftAdapterFilter) ((AssetFactoryDraftAdapter) getAdapter()).getFilter()).filter(searchView.getQuery().toString());
                    }
                }
                showOrHideNoAssetsView(dataSet.isEmpty());
            }
        }
    }

    @Override
    public void onErrorOccurred(Exception ex) {
        isRefreshing = false;
        if (isAttached) {
            swipeRefreshLayout.setRefreshing(false);
            CommonLogger.exception(TAG, ex.getMessage(), ex);
        }
    }

    @Override
    public FermatAdapter getAdapter() {
        if (adapter == null) {
            adapter = new AssetFactoryDraftAdapter(getActivity(), dataSet, manager, appSession);
            adapter.setFermatListEventListener(this);
        }
        return adapter;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        if (layoutManager == null) {
            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        }
        return layoutManager;
    }


    @Override
    public List<AssetFactory> getMoreDataAsync(FermatRefreshTypes refreshType, int pos) {

        List<AssetFactory> items = new ArrayList<>();
        try {
            List<AssetFactory> publishedAssets = manager.getAssetFactoryByState(FINAL);
            if (publishedAssets != null && !publishedAssets.isEmpty()) {
                Collections.sort(publishedAssets, new Comparator<AssetFactory>() {
                    @Override
                    public int compare(AssetFactory lhs, AssetFactory rhs) {
                        if (lhs.getCreationTimestamp().getTime() > rhs.getCreationTimestamp().getTime())
                            return -1;
                        else if (lhs.getCreationTimestamp().getTime() < rhs.getCreationTimestamp().getTime())
                            return 1;
                        return 0;
                    }
                });
                items.addAll(publishedAssets);
            }
            List<Resource> resources;
            for (AssetFactory item : items) {
                resources = item.getResources();
                for (Resource resource : resources) {
                    resource.setResourceBinayData(manager.getAssetFactoryResource(resource).getContent());
                }
            }
        } catch (Exception ex) {
            CommonLogger.exception(TAG, ex.getMessage(), ex);
            if (errorManager != null)
                errorManager.reportUnexpectedWalletException(
                        Wallets.DAP_ASSET_ISSUER_WALLET,
                        UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT,
                        ex);
        }
        return items;
    }

    @Override
    public void onUpdateViewOnUIThread(String code) {
        switch (code) {
            case DAPConstants.DAP_UPDATE_VIEW_ANDROID:
                onRefresh();
                break;
            default:
                super.onUpdateViewOnUIThread(code);
        }
    }


    @Override
    public void onItemClickListener(AssetFactory data, int position) {

    }

    @Override
    public void onLongItemClickListener(AssetFactory data, int position) {

    }
}

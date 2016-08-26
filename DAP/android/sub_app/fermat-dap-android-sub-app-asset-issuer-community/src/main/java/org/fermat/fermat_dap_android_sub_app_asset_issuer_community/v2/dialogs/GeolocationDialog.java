package org.fermat.fermat_dap_android_sub_app_asset_issuer_community.v2.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.ReferenceAppFermatSession;
import com.bitdubai.fermat_android_api.ui.dialogs.FermatDialog;
import com.bitdubai.fermat_android_api.ui.interfaces.FermatWorkerCallBack;
import com.bitdubai.fermat_android_api.ui.util.FermatWorker;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedSubAppExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedUIExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.enums.UISource;
import com.bitdubai.fermat_api.layer.dmp_engine.sub_app_runtime.enums.SubApps;
import com.bitdubai.fermat_api.layer.pip_engine.interfaces.ResourceProviderManager;
import com.bitdubai.fermat_dap_android_sub_app_asset_issuer_community_bitdubai.R;
import com.bitdubai.fermat_pip_api.layer.external_api.geolocation.interfaces.ExtendedCity;
import com.bitdubai.fermat_pip_api.layer.network_service.subapp_resources.SubAppResourcesProviderManager;

import org.fermat.fermat_dap_android_sub_app_asset_issuer_community.v2.adapters.GeolocationAdapter;
import org.fermat.fermat_dap_api.layer.dap_sub_app_module.asset_issuer_community.interfaces.AssetIssuerCommunitySubAppModuleManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frank Contreras (contrerasfrank@gmail.com) on 8/24/16.
 */
public class GeolocationDialog extends FermatDialog<ReferenceAppFermatSession, SubAppResourcesProviderManager>
        implements View.OnClickListener {
    //ATTRIBUTES
    private EditText searchInput;
    private AssetIssuerCommunitySubAppModuleManager moduleManager;
    private ListView mListView;
    private ReferenceAppFermatSession<AssetIssuerCommunitySubAppModuleManager> appSession;
    private ImageView lupaButton;
    private ImageView closeButton;

    //THREAD ATTRIBUTES
    private boolean isRefreshing = false;
    private List<ExtendedCity> lstActorInformations = new ArrayList<>();
    private GeolocationAdapter adapter;
    private ErrorManager errorManager;
    private LinearLayout emptyView;
    private final Activity activity;
    TextView noDatalabel;
    private AdapterCallback mAdapterCallback;

    //SETTERS ATTRIBUTES
    String Country;
    String State;
    String Input;

    public GeolocationDialog(Activity activity, ReferenceAppFermatSession<AssetIssuerCommunitySubAppModuleManager> appSession,
                             ResourceProviderManager resources, AdapterCallback mAdapterCallback) {
        super(activity, appSession, null);
        this.appSession = appSession;
        this.activity = activity;
        this.mAdapterCallback = mAdapterCallback;
    }

    public void onClick(View v) {
        int id = v.getId();
    }

    public static interface AdapterCallback extends GeolocationAdapter.AdapterCallback {
        void onMethodCallback(ExtendedCity cityFromList);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {

            moduleManager = appSession.getModuleManager();
            errorManager = appSession.getErrorManager();
            moduleManager.setAppPublicKey(appSession.getAppPublicKey());

            mListView = (ListView) findViewById(R.id.geolocation_view);
            noDatalabel = (TextView) findViewById(R.id.nodatalabel_geo);
            searchInput = (EditText) findViewById(R.id.geolocation_input);
            emptyView = (LinearLayout) findViewById(R.id.empty_view_geo);
            closeButton = (ImageView) findViewById(R.id.close_geolocation_dialog);
            lupaButton = (ImageView) this.findViewById(R.id.lupita_button);

            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            adapter = new GeolocationAdapter(getActivity(), lstActorInformations, errorManager, mAdapterCallback, GeolocationDialog.this);
            mListView.setAdapter(adapter);
            lupaButton.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  try {
                                                      getMoreData();
//                                lstActorInformations = moduleManager.getExtendedCitiesByFilter(searchInput.getText().toString());
//                                adapter = new GeolocationAdapter(getActivity(), lstActorInformations, errorManager, mAdapterCallback, GeolocationDialog.this);
//                                mListView.setAdapter(adapter);
//                                adapter.refreshEvents(lstActorInformations);
//                              // onRefresh();
                                                  } catch (Exception e) {
                                                      if (getActivity() != null)
                                                          errorManager.reportUnexpectedUIException(UISource.ACTIVITY,
                                                                  UnexpectedUIExceptionSeverity.CRASH, FermatException.wrapException(e));
                                                  }
                                              }
                                          }
            );
            showEmpty(true, emptyView);
        } catch (Exception e) {
            errorManager.reportUnexpectedSubAppException(SubApps.CHT_COMMUNITY,
                    UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
        }
    }

    protected int setLayoutId() {
        return R.layout.dap_issuer_community_v2_geolocation_item;
    }

    protected int setWindowFeature() {
        return Window.FEATURE_NO_TITLE;
    }

//    public void onRefresh(){
//        if (!isRefreshing) {
//            isRefreshing = true;
//            FermatWorker worker = new FermatWorker() {
//                @Override
//                protected Object doInBackground() throws Exception {
//                    return getMoreData(searchInput.getText().toString());
//                }
//            };
//            worker.setContext(getActivity());
//            worker.setCallBack(new FermatWorkerCallBack() {
//                @SuppressWarnings("unchecked")
//                @Override
//                public void onPostExecute(Object... result) {
//                    isRefreshing = false;
//                    if (result != null &&
//                            result.length > 0) {
//                        if (getActivity()!= null && adapter != null) {
//                            lstActorInformations = (ArrayList<ExtendedCity>) result[0];
//                            adapter = new GeolocationAdapter(getActivity(), lstActorInformations, errorManager, mAdapterCallback, GeolocationDialog.this);
//                            mListView.setAdapter(adapter);
//                            adapter.refreshEvents(lstActorInformations);
//                            if (lstActorInformations.isEmpty()) {
//                                showEmpty(true, emptyView);
//                            } else {
//                                showEmpty(false, emptyView);
//                            }
//                        }
//                    } else
//                        showEmpty(true, emptyView);
//                }
//
//                @Override
//                public void onErrorOccurred(Exception ex) {
//                    isRefreshing = false;
//                    if (getActivity() != null)
//                        errorManager.reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.CRASH, FermatException.wrapException(ex));
//                }
//            });
//            worker.execute();
//        }
//    }

    private void getMoreData() {
//        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
//        progressDialog.setMessage("Please wait");
//        progressDialog.setCancelable(false);
//        progressDialog.show();

        final FermatWorker fermatWorker = new FermatWorker(getActivity()) {
            @Override
            protected Object doInBackground() throws Exception {
                //TODO adaptar a dap
//                return moduleManager.getExtendedCitiesByFilter(searchInput.getText().toString());
                return null;
            }
        };

        fermatWorker.setCallBack(new FermatWorkerCallBack() {
            @Override
            public void onPostExecute(Object... result) {
//                progressDialog.dismiss();
                if (result != null && result.length > 0) {
                    adapter = new GeolocationAdapter(getActivity(), (List<ExtendedCity>) result[0], errorManager, mAdapterCallback, GeolocationDialog.this);
                    mListView.setAdapter(adapter);
                    adapter.refreshEvents((List<ExtendedCity>) result[0]);
                    showEmpty(false, emptyView);
                } else showEmpty(true, emptyView);
            }

            @Override
            public void onErrorOccurred(Exception ex) {
//                progressDialog.dismiss();
                errorManager.reportUnexpectedSubAppException(SubApps.DAP_ASSETS_IDENTITY_USER,
                        UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, ex);
            }
        });

        fermatWorker.execute();
    }

    public void showEmpty(boolean show, View emptyView) {
        if (show &&
                (noDatalabel.getVisibility() == View.GONE || noDatalabel.getVisibility() == View.INVISIBLE)) {
            noDatalabel.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.VISIBLE);
            if (adapter != null) {
                adapter.refreshEvents(null);
                mListView.setAdapter(adapter);
            }
        } else if (!show && noDatalabel.getVisibility() == View.VISIBLE) {
            noDatalabel.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
        }
    }
}

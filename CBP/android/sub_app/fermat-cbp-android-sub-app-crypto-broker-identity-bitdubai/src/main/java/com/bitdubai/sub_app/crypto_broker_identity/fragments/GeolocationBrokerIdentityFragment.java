package com.bitdubai.sub_app.crypto_broker_identity.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bitdubai.fermat_android_api.layer.definition.wallet.AbstractFermatFragment;
import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.ReferenceAppFermatSession;
import com.bitdubai.fermat_android_api.ui.interfaces.FermatWorkerCallBack;
import com.bitdubai.fermat_android_api.ui.util.FermatWorker;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedSubAppExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.dmp_engine.sub_app_runtime.enums.SubApps;
import com.bitdubai.fermat_cbp_api.all_definition.enums.Frecuency;
import com.bitdubai.fermat_cbp_api.layer.sub_app_module.crypto_broker_identity.interfaces.CryptoBrokerIdentityInformation;
import com.bitdubai.fermat_cbp_api.layer.sub_app_module.crypto_broker_identity.interfaces.CryptoBrokerIdentityModuleManager;
import com.bitdubai.fermat_cbp_api.layer.sub_app_module.crypto_broker_identity.utils.CryptoBrokerIdentityInformationImpl;
import com.bitdubai.fermat_cht_api.layer.identity.exceptions.CantGetChatIdentityException;
import com.bitdubai.fermat_pip_api.layer.network_service.subapp_resources.SubAppResourcesProviderManager;
import com.bitdubai.sub_app.crypto_broker_identity.R;
import com.bitdubai.sub_app.crypto_broker_identity.util.EditIdentityWorker;
import com.bitdubai.sub_app.crypto_broker_identity.util.FragmentsCommons;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;


/**
 * FERMAT-ORG
 * Developed by Lozadaa on 04/04/16.
 * Updated by Jose Cardozo josejcb (josejcb89@gmail.com) on 16/06/16.
 */

public class GeolocationBrokerIdentityFragment
        extends AbstractFermatFragment<ReferenceAppFermatSession<CryptoBrokerIdentityModuleManager>, SubAppResourcesProviderManager>
        implements FermatWorkerCallBack {

    private ExecutorService executor;

    ErrorManager errorManager;
    EditText accuracy;
    Spinner frequency;
    Toolbar toolbar;
    int accuracyData;
    Frecuency frequencyData;


    public static GeolocationBrokerIdentityFragment newInstance() {
        return new GeolocationBrokerIdentityFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toolbar = getToolbar();
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.arrow_left));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootLayout = inflater.inflate(R.layout.fragment_cbp_identity_geolocation, container, false);
        initViews(rootLayout);
        return rootLayout;
    }


    private void initViews(View layout) {
        // Spinner Drop down elements
        List<Frecuency> dataSpinner = new ArrayList<>();
        dataSpinner.add(Frecuency.LOW);
        dataSpinner.add(Frecuency.NORMAL);
        dataSpinner.add(Frecuency.HIGH);

        // Spinner element
        accuracy = (EditText) layout.findViewById(R.id.accuracy);
        frequency = (Spinner) layout.findViewById(R.id.spinner_frequency);
        frequency.setBackgroundColor(Color.parseColor("#f9f9f9"));

        try {
            ArrayAdapter<Frecuency> dataAdapter = new ArrayAdapter<>(getActivity(), R.layout.cbp_iden_spinner_item, dataSpinner);
            dataAdapter.setDropDownViewResource(R.layout.cbp_iden_spinner_item);
            frequency.setAdapter(dataAdapter);

            setValues(frequency, accuracy, dataAdapter);

            frequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        frequencyData = Frecuency.getByCode(parent.getItemAtPosition(position).toString());
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#616161"));
                        (parent.getChildAt(0)).setBackgroundColor(Color.parseColor("#F9f9f9"));
                    } catch (InvalidParameterException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } catch (CantGetChatIdentityException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        saveIdentityGeolocation();
    }

    private void saveIdentityGeolocation() {
        if (accuracy.getText().length() == 0) {
            Toast.makeText(getActivity(), "Accuracy is empty, please add a value", Toast.LENGTH_SHORT).show();
        } else {
            accuracyData = Integer.parseInt(accuracy.getText().toString());

            final CryptoBrokerIdentityInformation identityInfo = (CryptoBrokerIdentityInformation) appSession.getData(FragmentsCommons.IDENTITY_INFO);

            appSession.setData(FragmentsCommons.FREQUENCY_DATA, frequencyData);
            appSession.setData(FragmentsCommons.ACCURACY_DATA, accuracyData);

            if (identityInfo != null) {
                CryptoBrokerIdentityInformation identity = new CryptoBrokerIdentityInformationImpl(identityInfo, accuracyData, frequencyData);
                FermatWorker fermatWorker = new EditIdentityWorker(getActivity(), appSession, identity, this);
                executor = fermatWorker.execute();
            }
        }
    }

    public void setValues(Spinner frequency, EditText accuracy, ArrayAdapter<Frecuency> dataAdapter) throws CantGetChatIdentityException {
        final CryptoBrokerIdentityInformation identityInfo = (CryptoBrokerIdentityInformation) appSession.getData(FragmentsCommons.IDENTITY_INFO);

        if (identityInfo != null) {
            final String accuracyString = Long.toString(identityInfo.getAccuracy());
            accuracy.setText(accuracyString);

            if (!(identityInfo.getFrecuency() == null)) {
                int spinnerPosition = dataAdapter.getPosition(identityInfo.getFrecuency());
                frequency.setSelection(spinnerPosition);
            }
        } else {
            accuracy.setText("0");
            frequency.setSelection(0);
        }
    }

    @Override
    public void onPostExecute(Object... result) {
        if (executor != null) {
            executor.shutdown();
            executor = null;
        }
    }

    @Override
    public void onErrorOccurred(Exception ex) {
        if (executor != null) {
            executor.shutdown();
            executor = null;
        }

        errorManager.reportUnexpectedSubAppException(SubApps.CBP_CRYPTO_BROKER_IDENTITY,
                UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, ex);
    }
}
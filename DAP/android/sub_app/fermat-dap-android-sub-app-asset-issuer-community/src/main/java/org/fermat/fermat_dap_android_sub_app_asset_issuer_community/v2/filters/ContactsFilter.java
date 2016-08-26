package org.fermat.fermat_dap_android_sub_app_asset_issuer_community.v2.filters;

import android.widget.Filter;

import org.fermat.fermat_dap_android_sub_app_asset_issuer_community.models.ActorIssuer;
import org.fermat.fermat_dap_android_sub_app_asset_issuer_community.v2.adapters.ContactsListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frank Contreras (contrerasfrank@gmail.com) on 8/23/16.
 */
public class ContactsFilter extends Filter {
    //
    private List<ActorIssuer> data;
    private ContactsListAdapter adapter;
    ArrayList<ActorIssuer> nlist = new ArrayList<>();

    public ContactsFilter(List<ActorIssuer> data, ContactsListAdapter adapter) {
        this.data = data;
        this.adapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        String filterString = constraint.toString().toLowerCase();
        adapter.setFilterString(filterString);

        FilterResults results = new FilterResults();

        //final List<ChatActorCommunityInformation> list = data;

        int count = data.size();

        //final ArrayList<ChatActorCommunityInformation> nlist = new ArrayList<>();

        String filterableString;
        ActorIssuer resource;

        for (int i = 0; i < count; i++) {
            resource = data.get(i);
            filterableString = resource.getRecord().getName();
            if (filterableString.toLowerCase().contains(filterString)) {
                nlist.add(data.get(i));
            }
        }

        results.values = nlist;
        results.count = nlist.size();

        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        //adapter.setData((List<ChatActorCommunityInformation>) filterResults.values);
        adapter.changeDataSet((List<ActorIssuer>) filterResults.values);
        adapter.notifyDataSetChanged();
    }
}

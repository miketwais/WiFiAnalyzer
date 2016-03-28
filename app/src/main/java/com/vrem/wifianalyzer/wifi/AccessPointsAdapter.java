/*
 *    Copyright (C) 2015 - 2016 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.vrem.wifianalyzer.wifi;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.scanner.UpdateNotifier;

class AccessPointsAdapter extends BaseExpandableListAdapter implements UpdateNotifier {

    private final Resources resources;
    private final AccessPointsAdapterData accessPointsAdapterData;
    private final AccessPointsDetail accessPointsDetail;

    AccessPointsAdapter(@NonNull Context context) {
        super();
        this.resources = context.getResources();
        this.accessPointsAdapterData = new AccessPointsAdapterData();
        this.accessPointsDetail = new AccessPointsDetail();
        MainContext.INSTANCE.getScanner().addUpdateNotifier(this);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = getView(convertView);
        WiFiDetail details = (WiFiDetail) getGroup(groupPosition);
        accessPointsDetail.setView(resources, convertView, details, false);

        int childrenCount = getChildrenCount(groupPosition);
        if (childrenCount > 0) {
            convertView.findViewById(R.id.groupColumn).setVisibility(View.VISIBLE);
            ImageView groupIndicator = (ImageView) convertView.findViewById(R.id.groupIndicator);
            groupIndicator.setImageResource(isExpanded
                    ? R.drawable.ic_expand_less_black_24dp
                    : R.drawable.ic_expand_more_black_24dp);
            groupIndicator.setColorFilter(resources.getColor(R.color.icons_color));
            ((TextView) convertView.findViewById(R.id.groupCount)).setText(String.format("(%d) ", childrenCount + 1));
        } else {
            convertView.findViewById(R.id.groupColumn).setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = getView(convertView);
        WiFiDetail details = (WiFiDetail) getChild(groupPosition, childPosition);
        accessPointsDetail.setView(resources, convertView, details, true);
        convertView.findViewById(R.id.groupColumn).setVisibility(View.GONE);
        return convertView;
    }

    @Override
    public void update(@NonNull WiFiData wiFiData) {
        accessPointsAdapterData.update(wiFiData);
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return accessPointsAdapterData.parentsCount();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return accessPointsAdapterData.childrenCount(groupPosition);
    }

    @Override
    public Object getGroup(int groupPosition) {
        return accessPointsAdapterData.parent(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return accessPointsAdapterData.child(groupPosition, childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private View getView(View convertView) {
        if (convertView != null) {
            return convertView;
        }

        LayoutInflater inflater = MainContext.INSTANCE.getLayoutInflater();
        return inflater.inflate(R.layout.access_points_details, null);
    }

}

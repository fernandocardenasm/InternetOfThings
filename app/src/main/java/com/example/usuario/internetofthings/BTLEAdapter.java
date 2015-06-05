package com.example.usuario.internetofthings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by usuario on 05/06/2015.
 */
public class BTLEAdapter extends BaseAdapter {

    private Context mContext;
    private BTLE[] mBTLEDevices;

    public BTLEAdapter(Context context, BTLE[] BTLEDevices) {
        mContext = context;
        mBTLEDevices = BTLEDevices;
    }

    @Override
    public int getCount() {
        return mBTLEDevices.length;
    }

    @Override
    public Object getItem(int position) {
        return mBTLEDevices[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.btle_list_item, null);
            holder = new ViewHolder();
            holder.deviceAddress = (TextView) convertView.findViewById(R.id.deviceAddress);
            holder.friendlyName = (TextView) convertView.findViewById(R.id.friendlyNameText);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        BTLE device = mBTLEDevices[position];

        holder.friendlyName.setText(device.getFriendlyName());
        holder.deviceAddress.setText(device.getDeviceAddress());

        return convertView;
    }
    private static class ViewHolder{
        TextView friendlyName;
        TextView deviceAddress;
    }
}

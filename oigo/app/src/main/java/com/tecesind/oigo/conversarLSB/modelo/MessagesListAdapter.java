package com.tecesind.oigo.conversarLSB.modelo;

import java.util.List;




import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tecesind.oigo.R;

public class MessagesListAdapter extends BaseAdapter {

	private Context context;
	private List<Msg> messagesItems;

	public MessagesListAdapter(Context context, List<Msg> navDrawerItems) {
		this.context = context;
		this.messagesItems = navDrawerItems;
	}

	@Override
	public int getCount() {
		return messagesItems.size();
	}

	@Override
	public Msg getItem(int position) {
		return messagesItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Msg m = messagesItems.get(position);

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		if (messagesItems.get(position).isTipo()) {
			convertView = mInflater.inflate(R.layout.list_item_message_right,
					null);
		} else {
			convertView = mInflater.inflate(R.layout.list_item_message_left,
					null);
		}

		TextView lblFrom = (TextView) convertView.findViewById(R.id.lblMsgFrom);
		TextView txtMsg = (TextView) convertView.findViewById(R.id.txtMsg);
		txtMsg.setText(m.getMensaje());
		lblFrom.setText(m.getFecha() + " - " + m.getHora());

		return convertView;
	}

}

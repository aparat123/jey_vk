package com.vk.vktestapp.adapter;
import android.content.*;
import android.view.*;
import android.widget.*;
import com.squareup.picasso.*;
import com.vk.sdk.api.*;
import com.vk.sdk.api.model.*;
import com.vk.vktestapp.*;
import java.util.*;
import org.json.*;

public class DialogAdapter extends BaseAdapter
{
	private ArrayList<String> users, messages, photos;
	Picasso picasso;
	private Context context;
	private VKList<VKApiDialog> list;

	public DialogAdapter(Context context, ArrayList<String> users, ArrayList<String> messages,ArrayList<String> photos, VKList<VKApiDialog> list){
		this.users=users;
		this.messages=messages;
		this.context=context;
		this.photos=photos;
		this.list=list;
	}


	

	@Override
	public int getCount()
	{
		// TODO: Implement this method
		return users.size();
	}

	@Override
	public Object getItem(int position)
	{
		// TODO: Implement this method
		return position;
	}

	@Override
	public long getItemId(int position)
	{
		// TODO: Implement this method
		return position;
	}

	@Override
	public View getView(final int position, View p2, ViewGroup p3)
	{
		final SetData setData = new SetData();
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_item, null);

		setData.user = (TextView) view.findViewById(R.id.user);
		setData.msg = (TextView) view.findViewById(R.id.msg);
		setData.photo = (ImageView) view.findViewById(R.id.photo);

		
		Picasso.with(context).load(photos.get(position)).into(setData.photo);
		setData.user.setText(users.get(position));

		setData.msg.setText(messages.get(position));


		view.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v){
					final ArrayList<String> inlist = new ArrayList<>();
					final ArrayList<String> outlist = new ArrayList<>();
					final int id = list.get(position).message.user_id;
					System.out.println("click");
					VKRequest request = new VKRequest("messages.getHistory", VKParameters.from(VKApiConst.USER_ID, id));
					request.executeWithListener(new VKRequest.VKRequestListener(){
							@Override
							public void onComplete(VKResponse response){
								super.onComplete(response);

								try
								{
									JSONArray array = response.json.getJSONObject("response").getJSONArray("items");
									VKApiMessage [] msg = new VKApiMessage[array.length()];
									for(int i = 0; i < array.length(); i++) {
										VKApiMessage mes = new VKApiMessage(array.getJSONObject(i));
										msg[i] = mes;
									}
									for(VKApiMessage mess : msg){
										if(mess.out){
											outlist.add(mess.body);
										}
										else{
											inlist.add(mess.body);
										}
									}
									//DialogAdapter.this.context.startActivity(new Intent(context, SendMessage.class).putExtra("id",id).putExtra("out", outlist).putExtra("in", inlist));

								}

								catch (JSONException e)
								{
									e.printStackTrace();
								}

							}
						});



				}
			});

		return view;
	}

	public class SetData{
		TextView user, msg;
		ImageView photo;
	}

}

package com.vk.vktestapp;
import android.widget.*;
import android.os.*;
import android.support.v7.app.*;
import com.vk.sdk.api.*;
import com.vk.sdk.api.model.*;
import java.util.*;
import com.vk.vktestapp.adapter.*;

public class MainActivity extends ActionBarActivity
{
	private int position;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout);
		
	
	
	
	final ListView lv = (ListView) findViewById(R.id.dialoglist);


	VKRequest request = VKApi.messages().getDialogs(VKParameters.from(VKApiConst.COUNT, 100));
		request.executeWithListener(new VKRequest.VKRequestListener() {

				
	@Override
	public void onComplete(VKResponse response) {
		super.onComplete(response);

		
		
		
		
		VKApiGetDialogResponse getDialogResponse = ((VKApiGetDialogResponse)response.parsedModel);
		final  VKList<VKApiDialog> list= getDialogResponse.items;

		final ArrayList<String> messages = new ArrayList<>();
		final ArrayList<String> users = new ArrayList<>();
		for (VKApiDialog msg : list){

			//users.add(String.valueOf(msg.message.user_id));
			messages.add(msg.message.body);
		
		VKRequest req = VKApi.users().get(VKParameters.from(VKApiConst.USER_ID, String.valueOf(msg.message.user_id),VKApiConst.FIELDS, "first_name, last_name"));
		req.executeWithListener(new VKRequest.VKRequestListener() {
				@Override
				public void onComplete(VKResponse response) {
					final VKApiUser us = ((VKList<VKApiUserFull>)response.parsedModel).get(0);

					
					final ArrayList<String> names = new ArrayList<>();
					ArrayList<String> photos = new ArrayList<>();
					
					names.add(us.first_name + " " + us.last_name);
					photos.add(us.photo_50);
					
					lv.setAdapter(new DialogAdapter(MainActivity.this, names, messages, photos, list));
					
				}
			});
		
		}
		



	}
	});

	}
	
}

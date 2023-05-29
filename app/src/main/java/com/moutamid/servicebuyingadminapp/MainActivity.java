package com.moutamid.servicebuyingadminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.moutamid.servicebuyingadminapp.adapters.BookingListAdapter;
import com.moutamid.servicebuyingadminapp.databinding.ActivityMainBinding;
import com.moutamid.servicebuyingadminapp.model.Request;
import com.moutamid.servicebuyingadminapp.utils.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private ArrayList<Request> requestArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        requestArrayList = new ArrayList<>();
        getBookingData();
        FirebaseMessaging.getInstance().subscribeToTopic("admin");
    }

    private void getBookingData() {
        DatabaseReference db = Constants.databaseReference().child("Requests");
       // FirebaseUser user =  Constants.auth().getCurrentUser();
       // Query query = db.orderByChild("userId").equalTo(user.getUid());
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    requestArrayList.clear();
                    for (DataSnapshot ds: snapshot.getChildren()){
                        Request model = ds.getValue(Request.class);
                        requestArrayList.add(model);
                    }
                    Collections.reverse(requestArrayList);
                    BookingListAdapter adapter = new BookingListAdapter(MainActivity.this,requestArrayList);
                    binding.recyclerview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

       /* FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(!task.isSuccessful()){
                    System.out.println("Fetching FCM registration token failed ");
                    return;
                }

                String token = task.getResult();
                updatetoken(token);
                // Toast.makeText(DashBoard.this,token,Toast.LENGTH_LONG).show();
            }
        });*/
    }


   /* private void updatetoken(String token) {
        FirebaseUser firebaseUser = Constants.auth().getCurrentUser();

        DatabaseReference db = Constants.databaseReference().child("Tokens");
        Token uToken = new Token(token);
        db.child("admin").setValue(uToken);
    }*/


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
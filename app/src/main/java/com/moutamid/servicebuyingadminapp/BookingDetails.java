package com.moutamid.servicebuyingadminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.servicebuyingadminapp.Notifications.FcmNotificationsSender;
import com.moutamid.servicebuyingadminapp.databinding.ActivityBookingDetailsBinding;
import com.moutamid.servicebuyingadminapp.model.Request;
import com.moutamid.servicebuyingadminapp.model.Users;
import com.moutamid.servicebuyingadminapp.utils.Constants;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingDetails extends AppCompatActivity {

    private ActivityBookingDetailsBinding binding;
    private Request model;
    private String service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        model = getIntent().getParcelableExtra("request");
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        DatabaseReference db = Constants.databaseReference().child("Users").child(model.getUserId());
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Users users = snapshot.getValue(Users.class);
                    binding.fullname.setText(users.getFname()+" " + users.getLname());
                    binding.email.setText(users.getEmail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        service = model.getServiceName() + "(" + model.getSubServiceName() + ")";
        binding.phone.setText(model.getPhone());
        binding.location.setText(model.getLocation());
        binding.service.setText(service);
        binding.reason.setText(model.getDescription());
        binding.date.setText(model.getDate() + " " + model.getTime());

        binding.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptBooking();
            }
        });
        binding.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                declineBooking();
            }
        });
    }

    private void declineBooking() {
        DatabaseReference db = Constants.databaseReference().child("Requests");
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("status","Declined");
        db.child(model.getId()).updateChildren(hashMap);
        sendNotification(model.getUserId(),"Request Declined!","Your request of " + service +" has been declined");
        startActivity(new Intent(BookingDetails.this,MainActivity.class));
        finish();

    }

    private void acceptBooking() {
        DatabaseReference db = Constants.databaseReference().child("Requests");
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("status","Accepted");
        db.child(model.getId()).updateChildren(hashMap);
        sendNotification(model.getUserId(),"Request Accepted!","Your request of "+ service + " has been accepted");
        startActivity(new Intent(BookingDetails.this,MainActivity.class));
        finish();
    }


    private void sendNotification(String userId,String title,String desp) {
        new FcmNotificationsSender(
                "/topics/" + userId,                title,
                desp,                getApplicationContext(),
                BookingDetails.this).SendNotifications();
    }
  /*  private void sendNotification(String uId) {
        DatabaseReference tokens = Constants.databaseReference().child("Tokens");
   //     FirebaseUser user = Constants.auth().getCurrentUser();
        Query query = tokens.orderByKey().equalTo(uId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(uId, R.mipmap.ic_launcher, "You have new Request..",
                            "New Request", uId);

                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if(response.code() == 200){
                                        if (response.body().success != 1){
                                            System.out.println("Failed to send notification!");
                                        }else {

                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }*/
}
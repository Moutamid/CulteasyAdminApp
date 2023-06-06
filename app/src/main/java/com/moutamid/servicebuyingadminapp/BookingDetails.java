package com.moutamid.servicebuyingadminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.servicebuyingadminapp.Notifications.APIService;
import com.moutamid.servicebuyingadminapp.Notifications.Client;
import com.moutamid.servicebuyingadminapp.Notifications.Data;
import com.moutamid.servicebuyingadminapp.Notifications.MyResponse;
import com.moutamid.servicebuyingadminapp.Notifications.Sender;
import com.moutamid.servicebuyingadminapp.Notifications.Token;
import com.moutamid.servicebuyingadminapp.databinding.ActivityBookingDetailsBinding;
import com.moutamid.servicebuyingadminapp.model.Request;
import com.moutamid.servicebuyingadminapp.model.Users;
import com.moutamid.servicebuyingadminapp.utils.Constants;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingDetails extends AppCompatActivity {

    private ActivityBookingDetailsBinding binding;
    private Request model;
    private String service;
    private APIService apiService;
    private String serverKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        model = getIntent().getParcelableExtra("request");
        serverKey = Stash.getString("serverId");
        apiService = Client.getRetrofit("https://fcm.googleapis.com/").create(APIService.class);
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


        if (model.getStatus().equals("Pending")){
            binding.status.setText("Service Request Received");
        }else if (model.getStatus().equals("Accepted")){
            binding.status.setText("Awaiting Payment");
        }else if (model.getStatus().equals("Declined")){
            binding.status.setText("Service Request Canceled");
        }else {
            binding.status.setText(model.getStatus());
        }

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
        binding.paymentConfirmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentConfirmed();
            }
        });
        binding.serviceConfirmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceConfirmed();
            }
        });
        binding.serviceCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceCompleted();
            }
        });

        if (model.getServiceName().equals("Agriculture Service")) {
            binding.imageView.setImageResource(R.drawable.agriculture);
        }else if (model.getServiceName().equals("Horticultural Service")) {
            binding.imageView.setImageResource(R.drawable.horticulture);
        }else if (model.getServiceName().equals("Veterinary Service Function")) {
            binding.imageView.setImageResource(R.drawable.veterinary);
        }else if (model.getServiceName().equals("Animal Husbandry Service")) {
            binding.imageView.setImageResource(R.drawable.pets);
        }else if (model.getServiceName().equals("Commercial Service")) {
            binding.imageView.setImageResource(R.drawable.market);
        }else if (model.getServiceName().equals("Domestic Service")) {
            binding.imageView.setImageResource(R.drawable.domestic);
        }
    }

    private void serviceCompleted() {

        DatabaseReference db = Constants.databaseReference().child("Requests");
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("status","Service Completed");
        db.child(model.getId()).updateChildren(hashMap);
        sendNotification(model.getUserId(),"Service Completed!","Your service has been completed");

        finish();
    }

    private void serviceConfirmed() {

        DatabaseReference db = Constants.databaseReference().child("Requests");
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("status","Service Confirmed");
        db.child(model.getId()).updateChildren(hashMap);
        sendNotification(model.getUserId(),"Service Confirmed!","Your service has been confirmed");

        finish();
    }

    private void paymentConfirmed() {

        DatabaseReference db = Constants.databaseReference().child("Requests");
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("status","Payment Confirmed");
        db.child(model.getId()).updateChildren(hashMap);
        sendNotification(model.getUserId(),"Payment Confirmed!","Your payment has been confirmed");

        finish();
    }

    private void declineBooking() {
        DatabaseReference db = Constants.databaseReference().child("Requests");
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("status","Declined");
        db.child(model.getId()).updateChildren(hashMap);
        sendNotification(model.getUserId(),"Service Request Canceled!","Your request of " + service +" has been canceled");

        finish();

    }

    private void acceptBooking() {
        DatabaseReference db = Constants.databaseReference().child("Requests");
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("status","Accepted");
        db.child(model.getId()).updateChildren(hashMap);
        sendNotification(model.getUserId(),"Service Request Accepted!","Your request of "+ service + " has been accepted");

        finish();
    }


  /*  private void sendNotification(String userId,String title,String desp) {
        new FcmNotificationsSender(
                "/topics/" + userId,                title,
                desp,                getApplicationContext(),
                BookingDetails.this).SendNotifications();
    }*/


    private void sendNotification(String uId, String title, String content) {
        DatabaseReference tokens = Constants.databaseReference().child("Tokens");
        FirebaseUser user = Constants.auth().getCurrentUser();
        Query query = tokens.orderByKey().equalTo(uId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);

                    Data data = new Data("admin", R.mipmap.ic_launcher_round, content,
                            title,uId);

                    Sender sender = new Sender(data, token.getToken());
                    Map<String,String> headers = new HashMap<>();
                    headers.put("Content-Type","application/json");
                    headers.put("Authorization","Bearer "+serverKey);
                    apiService.sendNotification(headers,sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if(response.code() == 200){
                                        if (response.body().success != 1){
                                            System.out.println("Failed to send notification!");
                                        }
                                      //  Toast.makeText(BookingDetails.this, response.message(), Toast.LENGTH_SHORT).show();
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


    }

}
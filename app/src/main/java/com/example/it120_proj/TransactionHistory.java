package com.example.it120_proj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TransactionHistory extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference ref;
    TransactionAdapter transactionAdapter;
    ArrayList<Transaction> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        recyclerView = findViewById(R.id.transactionList);
        ref = FirebaseDatabase.getInstance().getReference("Transactions");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        transactionAdapter = new TransactionAdapter(this,list);
        recyclerView.setAdapter(transactionAdapter);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                System.out.println(user.getUid());
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if(dataSnapshot.child("userID").getValue().toString().equals(user.getUid())) {
                        Transaction transaction = new Transaction(dataSnapshot.getKey(), dataSnapshot.child("userID").getValue().toString(), dataSnapshot.child("action").getValue().toString(), dataSnapshot.child("date").getValue().toString(), dataSnapshot.child("description").getValue().toString());
                        list.add(transaction);
                    }
                }
                transactionAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
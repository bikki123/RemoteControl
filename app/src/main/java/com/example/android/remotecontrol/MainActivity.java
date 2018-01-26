package com.example.android.remotecontrol;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private Switch switchView1;
    private Switch switchView2;
    private Switch switchView3;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Button helpButton;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("switches");

        switchView1 = findViewById(R.id.switch_one);
        switchView2 = findViewById(R.id.switch_two);
        switchView3 = findViewById(R.id.switch_three);
        helpButton = findViewById(R.id.button_help);

        switchView1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switchValuesInFirebase("switch1", b);
            }
        });
        switchView2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switchValuesInFirebase("switch2", b);

            }
        });

        switchView3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switchValuesInFirebase("switch3", b);

            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    Boolean switch1 = dataSnapshot.child("switch1").getValue(Boolean.class);
                    Boolean switch2 = dataSnapshot.child("switch2").getValue(Boolean.class);
                    Boolean switch3 = dataSnapshot.child("switch3").getValue(Boolean.class);
                    if (switch1 != null && switch2 != null && switch3 != null) {
                        switchView1.setChecked(switch1);
                        switchView2.setChecked(switch2);
                        switchView3.setChecked(switch3);
                    } else {
                        Toast.makeText(getApplicationContext(), "Database Error", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Database Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
                startActivity(intent);
            }
        });

    }

    public void switchValuesInFirebase(String switchId, boolean b) {
        myRef.child(switchId).setValue(b);
        String state;
        if(b){
            state = "ON !";
        }
        else{state = "OFF !";}

        snackbar = Snackbar.make(findViewById(android.R.id.content), "  "+ switchId + " was turned "+ state, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

}





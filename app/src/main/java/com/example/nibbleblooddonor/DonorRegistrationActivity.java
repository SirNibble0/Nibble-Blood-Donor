package com.example.nibbleblooddonor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DonorRegistrationActivity extends AppCompatActivity {

    private TextView backButton;
    private CircleImageView profile_image;
    private TextInputEditText registerFullName, registerIdNumber, registerPhoneNumber, registerEmail, registerPassword;
    private Spinner bloodGroupsSpinner;
    private Button registerBtn;
    private Uri resultUri;

    private ProgressDialog loader;
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_registration);

        backButton = findViewById(R.id.dRegisterHaveAcct);
        profile_image = findViewById(R.id.profile_image);
        registerFullName = findViewById(R.id.registerFullName);
        registerIdNumber = findViewById(R.id.registerIdNumber);
        registerPhoneNumber = findViewById(R.id.registerPhoneNumber);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        bloodGroupsSpinner = findViewById(R.id.bloodGroupsSpinner);
        registerBtn = findViewById(R.id.registerBtn);

        loader = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DonorRegistrationActivity.this, LoginActivity.class));
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = registerEmail.getText().toString().trim();
                final String password = registerPassword.getText().toString().trim();
                final String fullName = registerFullName.getText().toString().trim();
                final String id = registerIdNumber.getText().toString().trim();
                final String phone = registerPhoneNumber.getText().toString().trim();
                final String bloodGroup = bloodGroupsSpinner.getSelectedItem().toString();

                if (TextUtils.isEmpty(email)){

                    registerEmail.setError("Email is required");
                    return;

                }

                if (TextUtils.isEmpty(password)){

                    registerPassword.setError("Password is required");
                    return;

                }

                if (TextUtils.isEmpty(fullName)){

                    registerFullName.setError("FullName is required");
                    return;

                }

                if (TextUtils.isEmpty(id)){

                    registerIdNumber.setError("ID Number is required");
                    return;

                }

                if (TextUtils.isEmpty(phone)){

                    registerPhoneNumber.setError("Phone Number is required");
                    return;

                }

                if (bloodGroup.equals("Select your blood group")){

                    Toast.makeText(DonorRegistrationActivity.this, "Blood group is required!!", Toast.LENGTH_SHORT).show();
                    return;

                }

                else {

                    loader.setMessage("Registering you...");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {

                            if (!task.isSuccessful()){

                                String error = task.getException().toString();
                                Toast.makeText(DonorRegistrationActivity.this, "Error" + error, Toast.LENGTH_SHORT).show();

                            }else {

                                String currentUserId = mAuth.getCurrentUser().getUid();
                                userDatabaseRef = FirebaseDatabase.getInstance().getReference()
                                        .child("users").child(currentUserId);

                                HashMap userinfo = new HashMap();
                                userinfo.put("id", currentUserId);
                                userinfo.put("name", fullName);
                                userinfo.put("email", email);
                                userinfo.put("password", password);
                                userinfo.put("phone", phone);
                                userinfo.put("idNumber", id);
                                userinfo.put("bloodGroup", bloodGroup);
                                userinfo.put("type", "donor");
                                userinfo.put("search", "donor" + bloodGroup);//This helps us to filter the blood groups in the spinner

                                userDatabaseRef.updateChildren(userinfo).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(Task task) {

                                        if (task.isSuccessful()){

                                            Toast.makeText(DonorRegistrationActivity.this, "Data set successfully", Toast.LENGTH_SHORT).show();

                                        }else{

                                            Toast.makeText(DonorRegistrationActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();

                                        }finish();
                                        //loader.dismiss();

                                    }
                                });

                                if (resultUri != null){

                                    final StorageReference filePath = FirebaseStorage.getInstance().getReference()
                                            .child("profile image").child(currentUserId);
                                    Bitmap bitmap = null;

                                    try {

                                        bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);

                                    }catch (IOException e){

                                        e.printStackTrace();

                                    }
                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
                                    byte[] data = byteArrayOutputStream.toByteArray();
                                    UploadTask uploadTask = filePath.putBytes(data);

                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(Exception e) {
                                            Toast.makeText(DonorRegistrationActivity.this, "Image upload failed!!!", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            if (taskSnapshot.getMetadata() != null &&
                                                    taskSnapshot.getMetadata().getReference() != null){

                                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {

                                                        String imageUrl = uri.toString();
                                                        Map newImageMap = new HashMap();
                                                        newImageMap.put("profilepictureurl", imageUrl);

                                                        userDatabaseRef.updateChildren(newImageMap).addOnCompleteListener(new OnCompleteListener() {
                                                            @Override
                                                            public void onComplete(Task task) {

                                                                if (task.isSuccessful()){
                                                                    Toast.makeText(DonorRegistrationActivity.this, "imageUrl added to database successfully", Toast.LENGTH_SHORT).show();
                                                                }else {
                                                                    Toast.makeText(DonorRegistrationActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                                }

                                                            }
                                                        }); finish();
                                                    }
                                                });

                                            }
                                        }
                                    });

                                    startActivity(new Intent(DonorRegistrationActivity.this, MainActivity.class));
                                    loader.dismiss();

                                }

                            }
                        }
                    });

                }


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null){

            resultUri = data.getData();
            profile_image.setImageURI(resultUri);

        }
    }
}
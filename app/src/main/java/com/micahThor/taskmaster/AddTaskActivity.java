package com.micahThor.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.amplify.generated.graphql.CreateTaskMutation;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferService;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;


import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import type.CreateTaskInput;

public class AddTaskActivity extends AppCompatActivity {

    TaskDatabase taskDb;
    List<Task> taskList;
    private AWSAppSyncClient mAWSAppSyncClient;
    private static final int OPEN_DOCUMENT_CODE = 2;
    String TAG = "micah.addtask";

    // TASK INSTANCE VARS
    private String taskTitle;
    private String taskDescription;
    private String taskState;
    private String fileName;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // PAGE ELEMENTS
        final Button addTaskButton = findViewById(R.id.addATaskButton);
        final EditText titleTextEdit = findViewById(R.id.taskTextInput);
        final EditText descriptionTextEdit = findViewById(R.id.taskDescriptionInput);
        final RadioGroup stateRadioGroup = findViewById(R.id.taskStateRadioGroup);
        final Button getPhotoButton = findViewById(R.id.getPhotoButton);
        final ImageView imageView = findViewById(R.id.taskImagePreview);
        final Toast resultToast = Toast.makeText(getApplicationContext(), "result", Toast.LENGTH_SHORT);

        // initialize AWS dynamo db and local databases
        mAWSAppSyncClient = AWSAppSyncClient.builder()
                .context(getApplicationContext())
                .awsConfiguration(new AWSConfiguration(getApplicationContext()))
                .build();
        taskDb = Room.databaseBuilder(getApplicationContext(), TaskDatabase.class, "tasks").allowMainThreadQueries().build();

        // wire up ADD TASK button
        addTaskButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Validate inputs
                if (titleTextEdit.getText().toString().length() == 0) {
                    titleTextEdit.setError("Title Required");
                } else if (descriptionTextEdit.getText().toString().length() == 0) {
                    descriptionTextEdit.setError("Description Required");
                } else if (stateRadioGroup.getCheckedRadioButtonId() == -1) {
                    resultToast.setText("Task State Required");
                    resultToast.show();
                } else {
                    // get task title
                    taskTitle = titleTextEdit.getText().toString();

                    // get task description
                    taskDescription = descriptionTextEdit.getText().toString();

                    // get task state
                    int checkedState = stateRadioGroup.getCheckedRadioButtonId();
                    RadioButton selectedStateButton = findViewById(checkedState);
                    taskState = selectedStateButton.getText().toString();

                    fileName = UUID.randomUUID().toString();

                    // add Task to databases
                    runTaskCreateMutation(taskTitle, taskDescription, taskState, fileName);
//                    taskDb.taskDao().saveTask(new Task(taskTitle, taskDescription, taskState, imageUri.toString()));

                    // save image to S3
                    getApplicationContext().startService(new Intent(getApplicationContext(), TransferService.class));
                    AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {
                        @Override
                        public void onResult(UserStateDetails userStateDetails) {
                            Log.i(TAG, "AWSMobileClient initialized. User State is " + userStateDetails.getUserState());
                            uploadWithTransferUtility();
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e(TAG, "Initialization error.", e);
                        }
                    });

                    // empty inputs and display successful message
                    titleTextEdit.setText("");
                    descriptionTextEdit.setText("");
                    selectedStateButton.setChecked(false);
                    imageView.setImageResource(R.drawable.ic_launcher_background);
                    resultToast.setText("Saved Task: " + taskTitle);
                    resultToast.show();
                }
            }
        });

        // wire up GET PHOTO button
        getPhotoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(AddTaskActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddTaskActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, OPEN_DOCUMENT_CODE);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

//        taskList = taskDb.taskDao().getAllTasks();
//        TextView taskCounter = findViewById(R.id.totalTaskCountTextView);
//        taskCounter.setText("Total tasks: " + taskList.size());
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != 0) {
            return;
        }
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, OPEN_DOCUMENT_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == OPEN_DOCUMENT_CODE && resultCode == RESULT_OK) {
            if (resultData != null) {
                // this is the image selected by the user
                imageUri = resultData.getData();
                ImageView imageView = findViewById(R.id.taskImagePreview);
                imageView.setImageURI(imageUri);
            }
        }
    }

    public void runTaskCreateMutation(String taskTitle, String taskBody, String taskState, String fileName) {
        Log.d("FILE NAME--:", fileName);
        CreateTaskInput taskInput = CreateTaskInput.builder()
                .title(taskTitle)
                .body(taskBody)
                .state(taskState)
                .imageFileName(fileName)
                .build();

        mAWSAppSyncClient.mutate(CreateTaskMutation.builder().input(taskInput).build())
                .enqueue(addMutationCallback);
    }

    private GraphQLCall.Callback<CreateTaskMutation.Data> addMutationCallback = new GraphQLCall.Callback<CreateTaskMutation.Data>() {
        @Override
        public void onResponse(@Nonnull Response<CreateTaskMutation.Data> response) {
            Log.i("micah", "Added task");
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e("micah", e.toString());
        }
    };

    public void uploadWithTransferUtility() {
        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(getApplicationContext())
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(new AmazonS3Client(AWSMobileClient.getInstance()))
                        .build();

        String filepath = convertUriToFilePath(imageUri);
        File file = new File(filepath);

        TransferObserver uploadObserver =
                transferUtility.upload(fileName, file);

        uploadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    Log.d(TAG, "successful upload");
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int) percentDonef;

                Log.d(TAG, "ID:" + id + " bytesCurrent: " + bytesCurrent
                        + " bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                // Handle errors
            }
        });
    }

    private String convertUriToFilePath(Uri uri) {
        Log.i("filepath", uri.toString());
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        // String filePath contains the path of selected file
        String filePath = cursor.getString(columnIndex);
        Log.i("filepath", "" + filePath);
        cursor.close();
        return filePath;
    }

}

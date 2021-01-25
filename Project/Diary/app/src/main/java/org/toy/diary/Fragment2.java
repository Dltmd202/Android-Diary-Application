package org.toy.diary;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.channguyen.rsv.RangeSliderView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Fragment2 extends Fragment {
    onTabItemSelectedListener listener;
    onRequestListener requestListener;
    Context context;

    //
    RelativeLayout topLayout;
    ImageView weatherIcon;
    TextView dateTextView;

    RelativeLayout contentsLayout;
    TextView locationTextView;
    EditText contentsInput;
    ImageView pictureImageView;

    CardView moodLayout;
    RangeSliderView moodSlider;

//    for phtodialog
    int selectedPhotoMenu;
    Boolean isPhotoCanceled=false;
    Boolean isPhotoCaptured=false;

//    phothoParentDir
    File file;
    String photoPath;
//    realPhoto
    Bitmap resultPhotoBitmap;



    int mMode = AppConstants.MODE_INSERT;
    int _id = -1;
    int weatherIndex = 0;
    int moodIndex = 2;

    Note item;



    private static final String TAG = "Fragment2";
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context =context;
        if (context instanceof onTabItemSelectedListener) listener = (onTabItemSelectedListener) context;
        if (context instanceof onRequestListener) requestListener = (onRequestListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(context!=null){
            context=null;
            listener=null;
            requestListener=null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment2, container, false);
        initUI(rootView);
        if(requestListener!=null) {
            //WhenCreateFragement 1
            requestListener.onRequest("getCurrentLocation");
        }
        return rootView;
    }
    public void initUI(ViewGroup rootView){

        //Matching
        topLayout = rootView.findViewById(R.id.topLayout);
        weatherIcon = rootView.findViewById(R.id.weatherIcon);
        dateTextView = rootView.findViewById(R.id.dateTextView);

        contentsLayout = rootView.findViewById(R.id.contentsLayout);
        locationTextView = rootView.findViewById(R.id.locationTextView);
        contentsInput = rootView.findViewById(R.id.contentsInput);
        pictureImageView = rootView.findViewById(R.id.pictureImageView);

        pictureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPhotoCaptured || isPhotoCanceled){
                    Log.d(TAG,"Try to Explore Image");
                    showDialog(AppConstants.CONTENT_PHOTO_EX);


                }
                else{
                    Log.d(TAG,"Try to Capture");
                    showDialog(AppConstants.CONTENT_PHOTO);
                }
            }
        });


        moodLayout = rootView.findViewById(R.id.moodLayout);
        moodSlider = rootView.findViewById(R.id.sliderView);

        Button saveButton = rootView.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMode == AppConstants.MODE_INSERT){
                    try {
                        println("Try to save Note");
                        saveNote();
                    }
                    catch (Exception e){
                        Log.e(TAG,"Fail to save",e);
                    }
                }
                else if(mMode == AppConstants.MODE_MODIFY){
                    modifyNote();
                }
//                if(listener!=null) listener.onTabSelected(0);
            }
        });
        Button deleteButton = rootView.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNote();
//                if(listener!=null) listener.onTabSelected(0);
            }
        });
        Button closeButton = rootView.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(listener!=null) listener.onTabSelected(0);
            }
        });
        RangeSliderView sliderView = rootView.findViewById(R.id.sliderView);
        sliderView.setOnSlideListener(new RangeSliderView.OnSlideListener() {
            @Override
            public void onSlide(int index) {
                Toast.makeText(getContext(), "moodIdx changed to"+index, Toast.LENGTH_SHORT).show();
            }
        });
        sliderView.setInitialIndex(2);
    }




    public void setDateString(String dateTextView) {
        this.dateTextView.setText(dateTextView);
    }
    public void setWeather(String data){
        Log.d(TAG,"data() : "+data);
        if (data != null){
            if (data.equals("맑음")){
                weatherIcon.setImageResource(R.drawable.weather_1);
            }
            else if (data.equals("구름 조금")){
                weatherIcon.setImageResource(R.drawable.weather_2);
            }
            else if (data.equals("구름 많음")){
                weatherIcon.setImageResource(R.drawable.weather_3);
            }
            else if (data.equals("흐림")){
                weatherIcon.setImageResource(R.drawable.weather_4);
            }
            else if (data.equals("")){
                weatherIcon.setImageResource(R.drawable.weather_5);
            }
            else if (data.equals("눈/")){
                weatherIcon.setImageResource(R.drawable.weather_6);
            }
            else if (data.equals("눈"))
                weatherIcon.setImageResource(R.drawable.weather_7);
            }
            else{
                Log.d(TAG,"Unknown weather string : "+data);
        }

    }
    public void setAddress(String address){
        locationTextView.setText(address);
    }

    public void showDialog(int id){
        AlertDialog.Builder builder = null;
        switch (id){
            case AppConstants.CONTENT_PHOTO:
                Log.d(TAG,"showDialog in Capture");
                builder = new AlertDialog.Builder(context);

                builder.setTitle("사진 메뉴 선택");
                builder.setSingleChoiceItems(R.array.array_photo, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedPhotoMenu = which;
                    }
                });
                builder.setPositiveButton("선택", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(selectedPhotoMenu  == 0){
                            try {
                                showPhotoCaptureActivity();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else if(selectedPhotoMenu == 1){
                            showPhotoSelectionActivity();
                        }
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                break;
            case AppConstants.CONTENT_PHOTO_EX:
                builder = new AlertDialog.Builder(context);

                builder.setTitle("사진 메뉴 선택");
                builder.setSingleChoiceItems(R.array.array_photo_ex, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedPhotoMenu = which;
                    }
                });
                builder.setPositiveButton("선택", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        사진 촬영
                        if(selectedPhotoMenu == 0){
                            try {
                                showPhotoCaptureActivity();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
//                        앨범에서 사진 선택
                        else if (selectedPhotoMenu == 1){
                            showPhotoSelectionActivity();
                        }
//                        사직 삭제하기
                        else if (selectedPhotoMenu == 2){
                            isPhotoCanceled = false;
                            isPhotoCaptured = false;

                            pictureImageView.setImageResource(R.drawable.imagetoset);
                            photoPath = null;
                        }
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                break;
            default:
                break;
        }
        AlertDialog dialog = builder.create();
        Log.d(TAG,"Try to create dialog" );
        dialog.show();
    }



    public void showPhotoSelectionActivity(){
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,AppConstants.REQ_PHOTO_SELECTION);
    }
    private File createFile() {
        // Create an image file name
        String filename = createFilename();
        File storageDir = Environment.getExternalStorageDirectory();
        File image = new File(storageDir, filename);
        photoPath = image.getAbsolutePath();
        return image;

    }

    public void showPhotoCaptureActivity() throws IOException {
        if (file == null) {
            file = createFile();
            Log.d(TAG,file.getAbsolutePath().toString());
        }
        if (file!=null) {
            Uri fileUri = FileProvider.getUriForFile(context, "org.toy.diary.fileprovider", file);
            Log.d(TAG, "geturi");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                startActivityForResult(intent, AppConstants.REQ_PHOTO_CAPTURE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            switch(requestCode){
                case AppConstants.REQ_PHOTO_CAPTURE:
                    Log.d(TAG,"onActivityResult() for REQ_PHOTO_CAPTURE");
                    Log.d(TAG,"resultCode : "+resultCode);

                    resultPhotoBitmap = decodeSampledBitmapFromResource(file,
                            pictureImageView.getWidth(),pictureImageView.getHeight());
                    try {
                        pictureImageView.setImageBitmap(resultPhotoBitmap);
                    } catch (Exception e) { e.printStackTrace();}
                    break;
                case AppConstants.REQ_PHOTO_SELECTION:
                    Log.d(TAG,"onActivityResult() for REQ_PHOTO_SELECTION");
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = context.getContentResolver().query(
                            selectedImage,filePathColumn,null,null,null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    photoPath = filePath;
                    cursor.close();

                    resultPhotoBitmap = decodeSampledBitmapFromResource(new File(filePath),
                            pictureImageView.getWidth(),pictureImageView.getHeight());
                    pictureImageView.setImageBitmap(resultPhotoBitmap);
                    isPhotoCaptured=true;
                    break;

            }
        }
    }

    public static Bitmap decodeSampledBitmapFromResource(File res , int reqWidth , int reqHeight){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(res.getAbsolutePath(),options);
        options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(res.getAbsolutePath(),options);

    }
    public static int calculateInSampleSize(BitmapFactory.Options options , int reqWidth , int reqHeight){
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth){
            final int halfHeight = height;
            final int halfWidth = width;
            while ((halfHeight/inSampleSize) >= reqHeight && (halfWidth/inSampleSize) >=reqWidth){
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    private String createFilename(){
        Date curDate = new Date();
        String curDateStr = String.valueOf(curDate.getTime());
        return curDateStr;

    }


    private String savePicture(){
        if (photoPath != null){
            return photoPath;
        }
        else{
            //AppContants 상수 설정할 것
            return "noImage";
        }
    }
    private void saveNote(){
        String address = locationTextView.getText().toString();
        String contents = contentsInput.getText().toString();

        String picturePath = savePicture();
        String sql = "INSERT INTO "+NoteDatabase.TABLE_NOTE +" (WEATHER , ADDRESS , LOCATION_X , "
                +" LOCATION_Y , CONTENTS , MOOD , PICTURE ) VALUES ( "
                + "'" + weatherIndex +"', "
                + "'" + address + "', "
                + "'" + "" + "', "
                + "'" + "" + "', "
                + "'" + contents + "', "
                + "'" + moodIndex + "', "
                + "'" + picturePath + "')";
        NoteDatabase database = NoteDatabase.getInstance(context);
        database.execSQL(sql);
    }
    private void modifyNote(){
        if( item != null){
            String address = locationTextView.getText().toString();
            String contents = contentsInput.getText().toString();

            String picturePath = savePicture();

            String sql = "UPDATE "+ NoteDatabase.TABLE_NOTE + "SET "
                    +" WEATHER = '"+ weatherIndex+"'"
                    +" ,ADDRESS = '"+ address+"'"
                    +" ,LOCATION_X = '"+""+"'"
                    +" ,LOCATION_Y = '"+""+"'"
                    +" ,CONTENTS = '"+contents +"'"
                    +" ,MOOD ="+moodIndex+"'"
                    +" ,PICTURE = '"+picturePath+"'"
                    + " WHERE _id = "+ item._id;
            Log.d(TAG,"sql : "+sql);
            NoteDatabase database = NoteDatabase.getInstance(context);
            database.execSQL(sql);
        }
    }

    private void deleteNote(){
        AppConstants.println("deleteNote called.");

        if(item != null){
            String sql = "DELETE FROM " + NoteDatabase.TABLE_NOTE +
                    "WHERE _id = " + item._id;
            Log.d(TAG,"sql : "+sql);
            NoteDatabase database = NoteDatabase.getInstance(context);
            database.execSQL(sql);
        }

    }
    private void println(String data){
        Log.d(TAG,data);
    }
}
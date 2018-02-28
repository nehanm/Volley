package task.com.volley;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;



public class GalleryActivity extends AppCompatActivity {


    private static final int MY_PERMISSIONS_REQUEST_WRITESTORAGE =111 ;
    RecyclerView rvgallery;
    List<String> stringList;
    private Cursor actualimagecursor2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        rvgallery=findViewById(R.id.rvgallery);


        checkExternalStoragePermission();




    }

    private void checkExternalStoragePermission() {


        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITESTORAGE);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else
        {
               new Task().execute();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITESTORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                   new Task().execute();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private ArrayList<String> getAllShownImagesPath() {


        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        getInternal(listOfAllImages);
        String absolutePathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        cursor = getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            Log.e("storageexternal",absolutePathOfImage);
            listOfAllImages.add(absolutePathOfImage);
        }




        return listOfAllImages;
    }

    private void getInternal(List<String> listOfAllImages) {

        if(listOfAllImages==null)
            listOfAllImages=new ArrayList<>();
        int column_index_data, column_index_folder_name;
        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        actualimagecursor2 = managedQuery(MediaStore.Images.Media.INTERNAL_CONTENT_URI, projection,
                null, null,null);
        column_index_data = actualimagecursor2.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        while (actualimagecursor2.moveToNext()) {
            String absolutePathOfImage = actualimagecursor2.getString(column_index_data);
            Log.e("storageinternal",absolutePathOfImage);
            listOfAllImages.add(absolutePathOfImage);
        }
    }


    public class Adapter extends RecyclerView.Adapter<Adapter.VH>
    {

        public class VH extends RecyclerView.ViewHolder
        {

            ImageView iv;

            public VH(View itemView) {
                super(itemView);
                iv=itemView.findViewById(R.id.iv);
            }
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VH(getLayoutInflater().inflate(R.layout.inflate_image,parent,false));
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {

            Glide.with(GalleryActivity.this)
                    .load(stringList.get(position))
                    .into(holder.iv);


        }

        @Override
        public int getItemCount() {

            return stringList.size();
        }
    }


    public class  Task extends AsyncTask<Void,Void,Void>
    {



        @Override
        protected Void doInBackground(Void... voids) {

            //worker thread
            //this mehod will run in background

            stringList=getAllShownImagesPath();
            rvgallery.setLayoutManager(new LinearLayoutManager(GalleryActivity.this));
            rvgallery.setAdapter(new Adapter());

            return null;
        }


    }

}

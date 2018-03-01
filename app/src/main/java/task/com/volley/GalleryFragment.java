package task.com.volley;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;


public class GalleryFragment extends Fragment {


    private static final int MY_PERMISSIONS_REQUEST_WRITESTORAGE = 1234;
    RecyclerView rvgallery;
    List<String> stringList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvgallery = view.findViewById(R.id.rvgallery);
        rvgallery.setLayoutManager(new LinearLayoutManager(getContext()));
        checkExternalStoragePermission();

    }

    private void checkExternalStoragePermission() {


        int permissionCheck = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {

            //we ask user to give permission

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

                builder.setMessage("enable permossion to read galley images");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_WRITESTORAGE);

                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();

                    }
                });


            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITESTORAGE);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

        } else {

            //he has already enabled the permission
            //readImagesFromGallry();
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

                    //  readImagesFromGallry();


                    new Task().execute();

                } else {

                    checkExternalStoragePermission();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private void readImagesFromGallry() {


        Log.v("readimages", "called");
        Toast.makeText(getContext(), "READ IMAGES", Toast.LENGTH_SHORT).show();
        if (stringList == null) {

            stringList = getAllShownImagesPath(getActivity());
            //stringList=new ArrayList<>();
        }
        rvgallery.setLayoutManager(new LinearLayoutManager(getContext()));
        rvgallery.setAdapter(new Adapter());
    }


    private List<String> getAllShownImagesPath(Activity activity) {


        Uri uri;
        Cursor cursor;
        int column_index_data;
        String absolutePathOfImage = null;
        stringList = new ArrayList<>();
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            Log.e("storageexternal", absolutePathOfImage);
            stringList.add(absolutePathOfImage);
        }


        return stringList;
    }


    public class Adapter extends RecyclerView.Adapter<Adapter.VH>  {


        boolean flag;

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType)  {

            return new VH(getLayoutInflater().inflate(R.layout.inflate_image, parent, false));
        }


        public  int calculateInSampleSize(
                BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) >= reqHeight
                        && (halfWidth / inSampleSize) >= reqWidth) {
                    inSampleSize *= 2;
                }
            }

            return inSampleSize;
        }

        @Override
        public void onBindViewHolder(final VH holder, int position) {


            // loadImage(holder.imageView,stringList.get(position));
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(stringList.get(position), options);
            options.inSampleSize = calculateInSampleSize(options,
                    100,100);
            options.inDither = false;                              //optional
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inJustDecodeBounds = false;
            holder.imageView.setImageBitmap(BitmapFactory.decodeFile(stringList.get(position),
                    options));

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {




                }
            });


        }



        private void loadImage(ImageView imageView, String s) {

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.ic_launcher_background);


            Glide.with(GalleryFragment.this)
                    .load(s)
                    .apply(requestOptions)
                    .into(imageView);
        }

        @Override
        public int getItemCount() {
            return stringList.size();
        }

        public class VH extends RecyclerView.ViewHolder {

            ImageView imageView;
            ConstraintLayout parent;

            public VH(View itemView) {
                super(itemView);

                imageView = itemView.findViewById(R.id.iv);
                parent = itemView.findViewById(R.id.parent);

            }
        }


    }


    public class Task extends AsyncTask<Void, Void, Void> {


        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //main thread
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            //worker thread
            //this mehod will run in background

            getAllShownImagesPath(getActivity());
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (progressDialog != null
                    && progressDialog.isShowing())
                progressDialog.dismiss();
            //main thread

            if (stringList != null && stringList.size() > 0) {
                rvgallery.setAdapter(new Adapter());
            } else {
                Toast.makeText(getActivity(), "No images found...", Toast.LENGTH_SHORT).show();
            }
        }
    }


}

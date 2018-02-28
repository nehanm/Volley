package task.com.volley;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;



public class VolleyFragment extends Fragment {

    NetworkImageView iv_avatar;
    Button btoad;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    String IMGURL="https://liliumflorals.com/wp-content/uploads/2015/01/Goodnight-Moon-700x394.jpg";



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_volley,container,false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iv_avatar=view.findViewById(R.id.iv_avatar);
        btoad=view.findViewById(R.id.btload);

        mRequestQueue = Volley.newRequestQueue(getContext());


        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {

            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });


        btoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                iv_avatar.setImageUrl(IMGURL,mImageLoader);
            }
        });
    }
}

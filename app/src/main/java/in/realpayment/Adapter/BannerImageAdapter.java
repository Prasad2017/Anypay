package in.realpayment.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import in.realpayment.Model.BannerResponse;
import in.realpayment.Model.BannerResponseList;
import in.realpayment.R;

public class BannerImageAdapter extends PagerAdapter {

    private List<BannerResponseList> arrayList;
    private LayoutInflater inflater;
    private Context context;

    public BannerImageAdapter(Context context, List<BannerResponseList> arrayList) {

        this.context = context;
        this.arrayList= arrayList;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {

        View itemView = inflater.inflate(R.layout.custom_pager, view, false);
        // assert imageLayout != null;
        ImageView imageView = itemView.findViewById(R.id.imageView);

        try {

            Picasso.with(context)
                    .load("http://www.realpayment.in/realapi/ImageUpload/"+arrayList.get(position).getBannerImageURL())
                    .fit()
                    .placeholder(R.drawable.banner)
                    .into(imageView);
            view.addView(itemView);

        }catch (Exception e){
            e.printStackTrace();
        }

        return itemView;

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}

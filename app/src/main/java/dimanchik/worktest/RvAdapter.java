package dimanchik.worktest;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.ImageSearchViewHolder> {

    public static class ImageSearchViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView nameHolder;
        ImageView imageHolder;

        ImageSearchViewHolder(final View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            nameHolder = (TextView)itemView.findViewById(R.id.name);
            imageHolder = (ImageView)itemView.findViewById(R.id.image);
        }
    }

    List<ImageSearch> images;

    RvAdapter(List<ImageSearch> images){
        this.images = images;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ImageSearchViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview, viewGroup, false);
        ImageSearchViewHolder isvh = new ImageSearchViewHolder(v);

        return isvh;
    }

    @Override
    public void onBindViewHolder(final ImageSearchViewHolder imageSearchViewHolder, int i) {
        imageSearchViewHolder.nameHolder.setText(images.get(i).name);
        imageSearchViewHolder.imageHolder.setImageBitmap(images.get(i).image);
        imageSearchViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                images.remove(imageSearchViewHolder.getAdapterPosition());
                notifyDataSetChanged();
                Log.d("Log"," работает клик");
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}

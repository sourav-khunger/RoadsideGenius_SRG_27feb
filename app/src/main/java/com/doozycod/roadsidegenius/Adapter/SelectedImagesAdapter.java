package com.doozycod.roadsidegenius.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.doozycod.roadsidegenius.Model.ImageListModel.Image;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class SelectedImagesAdapter extends RecyclerView.Adapter<SelectedImagesAdapter.RecyclerHolder> {
    Context context;
    List<Image> imagesSelectedList = new ArrayList<>();
    OnListListener onListListener;
//    ApiService apiService;

    public SelectedImagesAdapter(Context context, List<Image> imagesSelectedList, OnListListener onListListener) {
        this.context = context;
        this.imagesSelectedList = imagesSelectedList;
        this.onListListener = onListListener;
//        this.apiService = apiService;
    }


    public void setListener(OnListListener onListListener) {
        this.onListListener = onListListener;
    }

    public interface OnListListener {
        void OnSelectedSingleImage(String ImageId);
    }

    @NonNull
    @Override
    public SelectedImagesAdapter.RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_image_recyclerview, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedImagesAdapter.RecyclerHolder holder, int position) {
        Glide.with(context).load(Constants.FILES_BASE_URL + imagesSelectedList.get(position).getImage()).into(holder.imageView);
        holder.removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onListListener.OnSelectedSingleImage(imagesSelectedList.get(position).getImid());
//                imagesSelectedList.remove(position);
//                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagesSelectedList.size();
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        ImageView imageView, removeImage;

        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);
            removeImage = itemView.findViewById(R.id.removeImage);
            imageView = itemView.findViewById(R.id.selectedImages);
        }
    }
}


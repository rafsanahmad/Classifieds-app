package com.rafsan.classifiedsapp.ui.detail;

import static com.rafsan.classifiedsapp.utils.Constants.CACHE_SIZE;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.core.app.NavUtils;

import com.rafsan.classifiedsapp.R;
import com.rafsan.classifiedsapp.base.BaseActivity;
import com.rafsan.classifiedsapp.data.model.Result;
import com.rafsan.classifiedsapp.databinding.ActivityDetailBinding;
import com.rafsan.image_lib.ImageLoader;
import com.rafsan.image_lib.cache.CacheType;

import java.util.List;
import java.util.Objects;

public class DetailActivity extends BaseActivity<ActivityDetailBinding> {

    private ImageLoader imageLoader;
    private DetailViewModel detailViewModel;
    private Result item;

    @NonNull
    @Override
    public ActivityDetailBinding setBinding() {
        return ActivityDetailBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onViewReady(Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        detailViewModel = new DetailViewModel();
        //Extract the data…
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            item = bundle.getParcelable("item");
        }
        configImageLoader();
        setupUI();
    }

    public void configImageLoader() {
        imageLoader = ImageLoader.Companion.getInstance(this, CACHE_SIZE, CacheType.DISK);
    }

    public void setupUI() {
        if (item != null) {
            List<String> imageUrls = item.getImage_urls();
            if (imageUrls != null) {
                String url = detailViewModel.getImageUrl(imageUrls);
                if (url != null) {
                    imageLoader.downloadImage(url, binding.itemImage, R.drawable.placeholder);
                }
                binding.tvName.setText(item.getName());
                binding.tvPrice.setText(item.getPrice());
                if (item.getCreated_at() != null) {
                    String convertedDate = detailViewModel.convertDate(item.getCreated_at());
                    binding.tvCreatedAt.setText(convertedDate);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Respond to the action bar's Up/Home button
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

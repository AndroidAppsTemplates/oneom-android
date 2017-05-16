package com.iam.oneom.binding;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by iam on 16.05.17.
 */

public class Image {

    @BindingAdapter({"url", "rounded"})
    public static void loadImageRounded(ImageView view, String url, boolean rounded) {
        if (!rounded) {
            loadImage(view, url);
            return;
        }

        Glide
                .with(view.getContext())
                .load(url)
                .asBitmap()
                .into(new ImageViewTarget<Bitmap>(view) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(view.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        getView().setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    @BindingAdapter({"url", "error", "rounded"})
    public static void loadImageRounded(ImageView view, String url, int error, boolean rounded) {

        if (!rounded) {
            loadImage(view, url, error);
            return;
        }

        Glide
                .with(view.getContext())
                .load(url)
                .asBitmap()
                .error(error)
                .into(new ImageViewTarget<Bitmap>(view) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(view.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        getView().setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    @BindingAdapter("url")
    public static void loadImage(ImageView view, String url) {
        Glide
                .with(view.getContext())
                .load(url)
                .into(view);
    }
    @BindingAdapter({"url", "error"})
    public static void loadImage(ImageView view, String url, int error) {
        Glide
                .with(view.getContext())
                .load(url)
                .error(error)
                .into(view);
    }

    @BindingAdapter({"url", "error", "posterImageCorner"})
    public static void loadImage(ImageView view, String url, int error, float posterImageCorner) {
        Glide
                .with(view.getContext())
                .load(url)
                .error(error)
                .bitmapTransform(new RoundedCornersTransformation(view.getContext(), (int) posterImageCorner, 0))
                .into(view);
    }

}

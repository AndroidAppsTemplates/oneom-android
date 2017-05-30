package com.iam.oneom.binding;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Dimension;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.iam.oneom.util.Decorator;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by iam on 16.05.17.
 */

public class ImageViewBinding {

    @BindingAdapter("image")
    public static void setBackground(ImageView imageView, Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    @BindingAdapter("drawable")
    public static void setBackgroundDrawable(AppCompatImageView imageView, Drawable drawable) {
        imageView.setImageDrawable(drawable);
    }
    @BindingAdapter("drawable")
    public static void setBackgroundDrawable(AppCompatImageButton imageView, Drawable drawable) {
        imageView.setImageDrawable(drawable);
    }

    @BindingAdapter("blur_image")
    public static void setBackground(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .asBitmap()
                .centerCrop()
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        imageView.setImageBitmap(Decorator.fastblur(resource, 1, 50));
                    }
                });;
    }

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
                .centerCrop()
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
    public static void loadImageRounded(ImageView view, String url, Drawable error, boolean rounded) {

        if (!rounded) {
            loadImage(view, url, error);
            return;
        }

        Glide
                .with(view.getContext())
                .load(url)
                .asBitmap()
                .error(error)
                .centerCrop()
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
                .centerCrop()
                .into(view);
    }
    @BindingAdapter({"url", "error"})
    public static void loadImage(ImageView view, String url, Drawable error) {
        Glide
                .with(view.getContext())
                .load(url)
                .centerCrop()
                .error(error)
                .into(view);
    }

    @BindingAdapter({"url", "imageCorner"})
    public static void loadImage(ImageView view, String url, @Dimension float posterImageCorner) {
        Glide
                .with(view.getContext())
                .load(url)
                .centerCrop()
                .bitmapTransform(new RoundedCornersTransformation(view.getContext(), (int) posterImageCorner, 0))
                .into(view);
    }

    @BindingAdapter({"url", "error", "imageCorner"})
    public static void loadImage(ImageView view, String url, Drawable error, @Dimension float posterImageCorner) {
        Glide
                .with(view.getContext())
                .load(url)
                .error(error)
                .centerCrop()
                .bitmapTransform(new RoundedCornersTransformation(view.getContext(), (int) posterImageCorner, 0))
                .into(view);
    }

}

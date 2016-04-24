package com.caoligai.acms.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.caoligai.acms.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Õº∆¨π§æﬂ¿‡
 * 
 * @author Noodle
 * 
 */
public class ImageUtils {

	static DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.loading)
			.showImageOnFail(R.drawable.fail).cacheInMemory(true)
			.cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();

	public static void displayImage(String imageUrl, ImageView mImageView) {

		ImageLoader.getInstance().displayImage(imageUrl, mImageView, options);
	}
}

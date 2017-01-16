package io.intrepid.thirdpartylibraryexcercise;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MainContract.View, DownloadCatImageTask.Callback {

    @BindView(R.id.image_view)
    ImageView imageView;
    @BindView(R.id.new_cat_button)
    Button getNewCatButton;

    private MainPresenter presenter;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter = new MainPresenter();
        presenter.bindView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.bindView(this);
    }

    @OnClick(R.id.new_cat_button)
    public void onNewCatButtonClick() {
        presenter.onNewCatButtonClick();
    }

    @Override
    public void showLoadingIndicator() {
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading");
        dialog.show();
    }

    @Override
    public void dismissLoadingIndicator() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void loadImageFromUrl(String url) {
        Picasso.with(this).load(url).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                dismissLoadingIndicator();
                getNewCatButton.setText(R.string.click_for_new_cat);
            }

            @Override
            public void onError() {
                dismissLoadingIndicator();
                getNewCatButton.setText(R.string.error);
            }
        });
    }

    @Override
    public void onImageResult(Bitmap bitmap) {
        dismissLoadingIndicator();
        imageView.setImageBitmap(bitmap);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.unbindView();
    }
}

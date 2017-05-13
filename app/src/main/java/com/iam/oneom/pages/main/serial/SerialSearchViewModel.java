package com.iam.oneom.pages.main.serial;

import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.iam.oneom.BR;
import com.iam.oneom.R;
import com.iam.oneom.core.entities.model.Serial;
import com.iam.oneom.core.network.Web;

import me.tatarka.bindingcollectionadapter2.ItemBinding;
import rx.Subscription;

/**
 * Created by iam on 12.05.17.
 */

public class SerialSearchViewModel {

    private Subscription searchSubscription;

    private static final String TAG = SerialSearchViewModel.class.getSimpleName();

    public final ObservableList<Serial> serials = new ObservableArrayList<>();
    public final ObservableField<String> serialName = new ObservableField<>("");
    public final ItemBinding<Serial> itemBinding = ItemBinding.of(BR.ep, R.layout.serial_search_item);


    @BindingAdapter("addTextChangedListener")
    public static void setTextWatcher(EditText editText, TextWatcher addTextChangedListener) {
        editText.addTextChangedListener(addTextChangedListener);
    }

    public TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!serialName.get().equals(s.toString())) {
                serialName.set(s.toString());
                Log.i(TAG, "afterTextChanged: " + s.toString());
                if (searchSubscription != null && !searchSubscription.isUnsubscribed()) {
                    searchSubscription.unsubscribe();
                }
                searchSubscription = Web.instance.searchSerials(s.toString())
                        .subscribe(
                                serialsSearchResponse -> {
                                    serials.clear();
                                    serials.addAll(serialsSearchResponse.getResults());
                                }
                        );
            }
        }
    };
}

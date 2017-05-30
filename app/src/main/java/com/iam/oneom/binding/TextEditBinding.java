package com.iam.oneom.binding;

import android.databinding.BindingAdapter;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by iam on 27.05.17.
 */

public class  TextEditBinding {

    private TextEditBinding() {}

    @BindingAdapter("textWatcher")
    public static void setTextEditListener(EditText editText, TextWatcher watcher) {
        editText.addTextChangedListener(watcher);
    }

    @BindingAdapter("caretAtEnd")
    public static void setCaretAtEnd(EditText editText, boolean end) {
        if (editText.getText().length() == 0) {
            return;
        }

        editText.setSelection(editText.getText().length() - 1);
    }

}

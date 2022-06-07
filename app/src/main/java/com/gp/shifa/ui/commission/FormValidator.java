package com.gp.shifa.ui.commission;


import com.gp.shifa.R;
import com.gp.shifa.utils.Validator;

import java.io.File;

public class FormValidator extends Validator {
    public static int validate(String amount,
                               String bankName,
                               String date,
                               String username,
                               File billImage) {

        if (amount.isEmpty())
            return R.string.error_empty_amount;

        if (bankName.isEmpty())
            return R.string.error_empty_bankName;

        if (date.isEmpty())
            return R.string.error_empty_date;

        if (username.isEmpty())
            return R.string.error_empty_username;

        if (billImage == null)
            return R.string.error_empty_billImage;

        return -1;
    }
}

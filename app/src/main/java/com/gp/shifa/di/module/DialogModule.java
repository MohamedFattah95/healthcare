package com.gp.shifa.di.module;

import com.gp.shifa.ui.base.BaseDialog;

import dagger.Module;

@Module
public class DialogModule {

    private BaseDialog dialog;

    public DialogModule(BaseDialog dialog) {
        this.dialog = dialog;
    }

}

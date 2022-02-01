package com.gp.health.di.module;

import com.gp.health.ui.base.BaseDialog;

import dagger.Module;

@Module
public class DialogModule {

    private BaseDialog dialog;

    public DialogModule(BaseDialog dialog) {
        this.dialog = dialog;
    }

}
